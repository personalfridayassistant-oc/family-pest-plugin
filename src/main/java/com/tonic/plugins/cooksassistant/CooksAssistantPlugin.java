package com.tonic.plugins.cooksassistant;

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

import static com.tonic.api.widgets.DialogueAPI.dialoguePresent;
import static com.tonic.plugins.cooksassistant.CooksAssistantConstants.*;

@PluginDescriptor(
    name = "Cook's Assistant Auto",
    description = "Automates the Cook's Assistant quest turn-in when ingredients are ready",
    tags = {"quest", "cooks", "assistant", "auto"}
)
@Slf4j
public class CooksAssistantPlugin extends VitaPlugin {

    @Inject
    private Client client;

    @Inject
    private CooksAssistantConfig config;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private CooksAssistantOverlay overlay;

    @Getter
    private State state = State.IDLE;

    @Getter
    private boolean hasEgg;

    @Getter
    private boolean hasMilk;

    @Getter
    private boolean hasFlour;

    @Getter
    private String questStateText = "Unknown";

    @Provides
    CooksAssistantConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(CooksAssistantConfig.class);
    }

    @Override
    protected void startUp() {
        overlayManager.add(overlay);
        reset();
    }

    @Override
    protected void shutDown() {
        overlayManager.remove(overlay);
        Walker.cancel();
        reset();
    }

    private void reset() {
        state = State.IDLE;
        hasEgg = false;
        hasMilk = false;
        hasFlour = false;
        questStateText = "Unknown";
    }

    @Override
    public void loop() {
        refreshState();

        switch (state) {
            case IDLE:
                handleIdle();
                break;
            case CHECK_REQUIREMENTS:
                handleCheckRequirements();
                break;
            case TRAVEL_TO_COOK:
                handleTravelToCook();
                break;
            case TALK_TO_COOK:
                handleTalkToCook();
                break;
            case WAIT_FOR_INGREDIENTS:
                Delays.tick(2);
                break;
            case COMPLETE:
                Delays.tick(2);
                break;
        }
    }

    private void refreshState() {
        hasEgg = InventoryAPI.getCount(ITEM_EGG) >= REQUIRED_EGG_COUNT;
        hasMilk = InventoryAPI.getCount(ITEM_BUCKET_OF_MILK) >= REQUIRED_MILK_COUNT;
        hasFlour = InventoryAPI.getCount(ITEM_POT_OF_FLOUR) >= REQUIRED_FLOUR_COUNT;

        QuestState questState = Quest.COOKS_ASSISTANT.getState(client);
        questStateText = questState.name();

        if (questState == QuestState.FINISHED) {
            state = State.COMPLETE;
        }
    }

    private void handleIdle() {
        if (state == State.COMPLETE) {
            return;
        }
        state = State.CHECK_REQUIREMENTS;
    }

    private void handleCheckRequirements() {
        if (!hasAllIngredients()) {
            log.warn("Missing ingredients: egg={}, milk={}, flour={}", hasEgg, hasMilk, hasFlour);
            state = State.WAIT_FOR_INGREDIENTS;
            return;
        }

        state = State.TRAVEL_TO_COOK;
    }

    private void handleTravelToCook() {
        if (isNear(TILE_LUMBRIDGE_CASTLE_KITCHEN, 8)) {
            state = State.TALK_TO_COOK;
            return;
        }

        try {
            Walker.walkTo(TILE_LUMBRIDGE_CASTLE_KITCHEN);
        } catch (Exception e) {
            log.warn("Failed walking to Cook: {}", e.getMessage());
        }
    }

    private void handleTalkToCook() {
        if (!hasAllIngredients()) {
            state = State.WAIT_FOR_INGREDIENTS;
            return;
        }

        if (dialoguePresent()) {
            Dialogues.processDialogues(
                DIALOG_WHATS_WRONG,
                DIALOG_HELP_COOK,
                DIALOG_YES,
                DIALOG_EGG,
                DIALOG_MILK,
                DIALOG_FLOUR
            );
            return;
        }

        NpcEx cook = new NpcQuery().withIds(NPC_COOK).nearest();
        if (cook != null) {
            cook.interact("Talk-to");
            Delays.tick(2);
        }
    }

    private boolean hasAllIngredients() {
        return hasEgg && hasMilk && hasFlour;
    }

    private boolean isNear(WorldPoint target, int distance) {
        WorldPoint player = PlayerEx.getLocal().getWorldPoint();
        return player.distanceTo(target) <= distance;
    }

    public enum State {
        IDLE,
        CHECK_REQUIREMENTS,
        TRAVEL_TO_COOK,
        TALK_TO_COOK,
        WAIT_FOR_INGREDIENTS,
        COMPLETE
    }
}
