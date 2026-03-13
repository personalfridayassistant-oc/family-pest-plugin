package com.tonic.plugins.f2pchain;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.tonic.api.threaded.Delays;
import com.tonic.api.threaded.Dialogues;
import com.tonic.api.widgets.InventoryAPI;
import com.tonic.data.wrappers.NpcEx;
import com.tonic.data.wrappers.PlayerEx;
import com.tonic.queries.NpcQuery;
import com.tonic.services.pathfinder.Walker;
import com.tonic.util.VitaPlugin;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Quest;
import net.runelite.api.QuestState;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.tonic.api.widgets.DialogueAPI.dialoguePresent;
import static com.tonic.plugins.f2pchain.F2PQuestChainConstants.*;

@PluginDescriptor(
    name = "F2P Quest Chain Auto",
    description = "Automates selected free-to-play quests with optional GE item acquisition and chaining",
    tags = {"quest", "f2p", "chain", "auto", "ge"}
)
@Slf4j
public class F2PQuestChainPlugin extends VitaPlugin {

    @Inject private Client client;
    @Inject private F2PQuestChainConfig config;
    @Inject private OverlayManager overlayManager;
    @Inject private F2PQuestChainOverlay overlay;

    @Getter private State state = State.IDLE;
    @Getter private String activeQuest = "None";
    @Getter private String statusText = "Idle";
    @Getter private int missingItemCount = 0;

    private final List<QuestTask> chain = new ArrayList<>();
    private QuestTask currentTask;

    @Provides
    F2PQuestChainConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(F2PQuestChainConfig.class);
    }

    @Override
    protected void startUp() {
        overlayManager.add(overlay);
        rebuildChain();
    }

    @Override
    protected void shutDown() {
        overlayManager.remove(overlay);
        Walker.cancel();
        state = State.IDLE;
    }

    @Override
    public void loop() {
        if (chain.isEmpty()) {
            rebuildChain();
        }

        switch (state) {
            case IDLE: selectNextQuest(); break;
            case CHECK_REQUIREMENTS: checkRequirements(); break;
            case ACQUIRE_ITEMS_GE: acquireItemsAtGE(); break;
            case TRAVEL_TO_TARGET: travelToTarget(); break;
            case TALK_TO_TARGET: talkToTarget(); break;
            case COMPLETE_QUEST: completeQuest(); break;
            case CHAIN_COMPLETE: Delays.tick(2); break;
        }
    }

    private void rebuildChain() {
        chain.clear();
        for (QuestTask task : allF2PTasks()) {
            if (isQuestSelected(task.getQuestEnumName())) {
                chain.add(task);
            }
        }
    }

    private boolean isQuestSelected(String questEnumName) {
        switch (questEnumName) {
            case "COOKS_ASSISTANT": return config.cooksAssistant();
            case "SHEEP_SHEARER": return config.sheepShearer();
            case "DORICS_QUEST": return config.doricsQuest();
            case "WITCHS_POTION": return config.witchsPotion();
            case "IMP_CATCHER": return config.impCatcher();
            case "RUNE_MYSTERIES": return config.runeMysteries();
            case "THE_RESTLESS_GHOST": return config.restlessGhost();
            case "ROMEO__JULIET": return config.romeoAndJuliet();
            case "ERNEST_THE_CHICKEN": return config.ernestTheChicken();
            case "PIRATES_TREASURE": return config.piratesTreasure();
            case "PRINCE_ALI_RESCUE": return config.princeAliRescue();
            case "VAMPIRE_SLAYER": return config.vampireSlayer();
            case "SHIELD_OF_ARRAV": return config.shieldOfArrav();
            case "THE_KNIGHTS_SWORD": return config.knightsSword();
            case "GOBLIN_DIPLOMACY": return config.goblinDiplomacy();
            case "DEMON_SLAYER": return config.demonSlayer();
            case "BLACK_KNIGHTS_FORTRESS": return config.blackKnightsFortress();
            case "DRAGON_SLAYER_I": return config.dragonSlayerI();
            case "X_MARKS_THE_SPOT": return config.xMarksTheSpot();
            case "BELOW_ICE_MOUNTAIN": return config.belowIceMountain();
            case "MISTHALIN_MYSTERY": return config.misthalinMystery();
            case "THE_CORSAIR_CURSE": return config.corsairCurse();
            default: return false;
        }
    }

    private void selectNextQuest() {
        if (chain.isEmpty()) {
            statusText = "No selected quests";
            state = State.CHAIN_COMPLETE;
            return;
        }

        if (currentTask != null && !config.chainMode()) {
            statusText = "Chain mode disabled";
            state = State.CHAIN_COMPLETE;
            return;
        }

        currentTask = chain.stream().filter(task -> getQuestState(task) != QuestState.FINISHED).findFirst().orElse(null);
        if (currentTask == null) {
            activeQuest = "None";
            statusText = "All selected quests complete";
            state = State.CHAIN_COMPLETE;
            return;
        }

        activeQuest = currentTask.getName();
        statusText = currentTask.getRequirementsSummary();
        state = State.CHECK_REQUIREMENTS;
    }

    private void checkRequirements() {
        missingItemCount = countMissingItems(currentTask);
        if (missingItemCount > 0 && config.buyMissingFromGE()) {
            statusText = "Missing items, buying from GE";
            state = State.ACQUIRE_ITEMS_GE;
            return;
        }
        if (missingItemCount > 0) {
            statusText = "Missing items; GE buy disabled";
            Delays.tick(2);
            return;
        }
        state = State.TRAVEL_TO_TARGET;
    }

    private int countMissingItems(QuestTask task) {
        int missing = 0;
        for (RequiredItem item : task.getRequiredItems()) {
            int have = InventoryAPI.getCount(item.getItemId());
            if (have < item.getQuantity()) {
                missing += (item.getQuantity() - have);
            }
        }
        return missing;
    }

    private void acquireItemsAtGE() {
        if (!isNear(GRAND_EXCHANGE_TILE, 10)) {
            Walker.walkTo(GRAND_EXCHANGE_TILE);
            return;
        }

        NpcEx clerk = new NpcQuery().withIds(NPC_GRAND_EXCHANGE_CLERK).nearest();
        if (clerk != null && !dialoguePresent()) {
            clerk.interact("Exchange");
            Delays.tick(2);
        }

        for (RequiredItem item : currentTask.getRequiredItems()) {
            int need = item.getQuantity() - InventoryAPI.getCount(item.getItemId());
            if (need > 0) {
                attemptGeBuy(item.getItemId(), need);
            }
        }

        missingItemCount = countMissingItems(currentTask);
        state = missingItemCount == 0 ? State.TRAVEL_TO_TARGET : State.ACQUIRE_ITEMS_GE;
        Delays.tick(2);
    }

    private boolean attemptGeBuy(int itemId, int quantity) {
        String[] apiClasses = {"com.tonic.api.game.GrandExchangeAPI", "com.tonic.api.widgets.GrandExchangeAPI", "com.tonic.api.grandexchange.GrandExchangeAPI"};
        String[] methods = {"buyItem", "createBuyOffer", "buy"};
        for (String className : apiClasses) {
            try {
                Class<?> clazz = Class.forName(className);
                for (String methodName : methods) {
                    try {
                        Method method = clazz.getMethod(methodName, int.class, int.class);
                        method.invoke(null, itemId, quantity);
                        return true;
                    } catch (NoSuchMethodException ignored) {
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    private void travelToTarget() {
        if (isNear(currentTask.getTargetTile(), 8)) {
            state = State.TALK_TO_TARGET;
            return;
        }
        Walker.walkTo(currentTask.getTargetTile());
    }

    private void talkToTarget() {
        if (getQuestState(currentTask) == QuestState.FINISHED) {
            state = State.COMPLETE_QUEST;
            return;
        }

        if (dialoguePresent()) {
            Dialogues.processDialogues(currentTask.getDialogueChoices().toArray(new String[0]));
            return;
        }

        if (currentTask.getTargetNpcId() > 0) {
            NpcEx npc = new NpcQuery().withIds(currentTask.getTargetNpcId()).nearest();
            if (npc != null) {
                npc.interact("Talk-to");
                Delays.tick(2);
                return;
            }
        }

        Delays.tick(1);
    }

    private void completeQuest() {
        statusText = "Completed " + currentTask.getName();
        state = State.IDLE;
    }

    private QuestState getQuestState(QuestTask task) {
        try {
            Quest quest = Quest.valueOf(task.getQuestEnumName());
            return quest.getState(client);
        } catch (Exception e) {
            return QuestState.NOT_STARTED;
        }
    }

    private boolean isNear(WorldPoint target, int distance) {
        return PlayerEx.getLocal().getWorldPoint().distanceTo(target) <= distance;
    }

    public String getSelectedQuestCountText() {
        return chain.size() + " selected";
    }

    public enum State {
        IDLE,
        CHECK_REQUIREMENTS,
        ACQUIRE_ITEMS_GE,
        TRAVEL_TO_TARGET,
        TALK_TO_TARGET,
        COMPLETE_QUEST,
        CHAIN_COMPLETE
    }
}
