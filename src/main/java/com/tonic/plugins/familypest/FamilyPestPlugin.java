package com.tonic.plugins.familypest;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.tonic.api.game.MovementAPI;
import com.tonic.api.game.VarAPI;
import com.tonic.api.threaded.Delays;
import com.tonic.api.threaded.Dialogues;
import com.tonic.api.widgets.InventoryAPI;
import com.tonic.data.wrappers.ItemEx;
import com.tonic.data.wrappers.NpcEx;
import com.tonic.data.wrappers.PlayerEx;
import com.tonic.data.wrappers.TileObjectEx;
import com.tonic.queries.NpcQuery;
import com.tonic.queries.TileObjectQuery;
import com.tonic.services.pathfinder.Walker;
import com.tonic.util.VitaPlugin;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.Quest;
import net.runelite.api.QuestState;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import static com.tonic.plugins.familypest.FamilyPestConstants.*;
import static com.tonic.api.widgets.DialogueAPI.*;

@PluginDescriptor(
    name = "Family Pest",
    description = "Automates the Family Pest miniquest",
    tags = {"family", "pest", "miniquest", "quest", "auto"}
)
@Slf4j
public class FamilyPestPlugin extends VitaPlugin {

    @Inject
    private Client client;

    @Inject
    private FamilyPestConfig config;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private FamilyPestOverlay overlay;

    @Getter
    private State state = State.IDLE;

    @Getter
    private int questProgress = 0;

    @Getter
    private boolean talkedToAvan = false;

    @Getter
    private boolean talkedToCaleb = false;

    @Getter
    private boolean talkedToJohnathon = false;

    @Provides
    FamilyPestConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(FamilyPestConfig.class);
    }

    @Override
    protected void startUp() {
        log.info("Family Pest plugin started");
        overlayManager.add(overlay);
        reset();
    }

    @Override
    protected void shutDown() {
        log.info("Family Pest plugin stopped");
        overlayManager.remove(overlay);
        Walker.cancel();
        reset();
    }

    private boolean completed = false;  // Track if miniquest is done
    
    private void reset() {
        state = State.IDLE;
        completed = false;  // Reset completion flag
        questProgress = 0;
        talkedToAvan = false;
        talkedToCaleb = false;
        talkedToJohnathon = false;
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        // Read varbits every tick
        questProgress = VarAPI.getVar(VARBIT_QUEST_PROGRESS);
        talkedToAvan = VarAPI.getVar(VARBIT_TALKED_AVAN) == 1;
        talkedToCaleb = VarAPI.getVar(VARBIT_TALKED_CALEB) == 1;
        talkedToJohnathon = VarAPI.getVar(VARBIT_TALKED_JOHNATHON) == 1;
    }

    @Override
    public void loop() throws Exception {
        // State machine
        switch (state) {
            case IDLE:
                handleIdle();
                break;
            case CHECK_REQUIREMENTS:
                handleCheckRequirements();
                break;
            case TRAVEL_TO_DIMINTHEIS:
                handleTravelToDimintheis();
                break;
            case TALK_TO_DIMINTHEIS_1:
                handleTalkToDimintheis1();
                break;
            case VISIT_BROTHERS:
                handleVisitBrothers();
                break;
            case TALK_TO_AVAN:
                handleTalkToAvan();
                break;
            case TALK_TO_CALEB:
                handleTalkToCaleb();
                break;
            case TRAVEL_TO_JOHNATHON:
                handleTravelToJohnathon();
                break;
            case TALK_TO_JOHNATHON:
                handleTalkToJohnathon();
                break;
            case RETURN_TO_DIMINTHEIS:
                handleReturnToDimintheis();
                break;
            case TALK_TO_DIMINTHEIS_2:
                handleTalkToDimintheis2();
                break;
            case COMPLETE:
                log.info("Family Pest miniquest complete!");
                completed = true;
                break;
        }
    }

    // ==================== //
    // State Handlers       //
    // ==================== //

    private void handleIdle() {
        // Check if already marked as completed
        if (completed) {
            state = State.COMPLETE;  // Show COMPLETE state
            return;
        }
        
        // Check if quest is already complete
        if (questProgress >= PROGRESS_COMPLETE) {
            log.info("Family Pest already complete!");
            completed = true;
            state = State.COMPLETE;  // Show COMPLETE state
            return;
        }

        // Start the quest
        log.info("Starting Family Pest miniquest");
        state = State.CHECK_REQUIREMENTS;
    }

    private void handleCheckRequirements() {
        log.info("Checking requirements...");
        
        // Check Family Crest
        boolean familyCrestDone = isFamilyCrestComplete();
        log.info("Family Crest complete: {}", familyCrestDone);
        
        // For testing, continue even if check fails
        if (!familyCrestDone) {
            log.warn("Family Crest check returned false - continuing anyway for testing");
            // Don't return - continue to check quest progress
        }

        log.info("Quest progress varbit value: {}", questProgress);

        // If already started, resume
        if (questProgress >= PROGRESS_TALK_TO_BROTHERS) {
            log.info("Transitioning to VISIT_BROTHERS");
            state = State.VISIT_BROTHERS;
        } else {
            log.info("Transitioning to TRAVEL_TO_DIMINTHEIS");
            state = State.TRAVEL_TO_DIMINTHEIS;
        }
        
        log.info("New state: {}", state);
    }

    private void handleTravelToDimintheis() {
        if (isNear(TILE_DIMINTHEIS, 10)) {
            state = State.TALK_TO_DIMINTHEIS_1;
            return;
        }

        try {
            Walker.walkTo(TILE_DIMINTHEIS);
        } catch (IndexOutOfBoundsException e) {
            log.warn("Cannot find path to Dimintheis. Please use teleport.");
        }
    }

    private void handleTalkToDimintheis1() {
        // Check if we've progressed past the initial talk
        if (questProgress >= PROGRESS_TALK_TO_BROTHERS) {
            state = State.VISIT_BROTHERS;
            return;
        }

        // Handle dialogue if present
        if (dialoguePresent()) {
            // Process dialogue with the two options:
            // 1. "Have you got any quests for me?"
            // 2. "Oh come on, however menial, I want to help!"
            Dialogues.processDialogues("Have you got any quests for me?", "Oh come on, however menial, I want to help!");
            return;
        }

        // Talk to NPC
        talkToNpc(NPC_DIMINTHEIS);
    }

    private void handleVisitBrothers() {
        // Check if all brothers visited
        if (talkedToAvan && talkedToCaleb && talkedToJohnathon) {
            state = State.RETURN_TO_DIMINTHEIS;
            return;
        }

        // Visit brothers in order (config can change this)
        BrotherOrder order = config.brotherOrder();
        switch (order) {
            case AVAN_FIRST:
                if (!talkedToAvan) state = State.TALK_TO_AVAN;
                else if (!talkedToCaleb) state = State.TALK_TO_CALEB;
                else state = State.TRAVEL_TO_JOHNATHON;
                break;
            case CALEB_FIRST:
                if (!talkedToCaleb) state = State.TALK_TO_CALEB;
                else if (!talkedToAvan) state = State.TALK_TO_AVAN;
                else state = State.TRAVEL_TO_JOHNATHON;
                break;
            default:
                if (!talkedToAvan) state = State.TALK_TO_AVAN;
                else if (!talkedToCaleb) state = State.TALK_TO_CALEB;
                else state = State.TRAVEL_TO_JOHNATHON;
        }
    }

    private void handleTalkToAvan() {
        if (talkedToAvan) {
            state = State.VISIT_BROTHERS;
            return;
        }

        // Handle dialogue - just select "Family Pest"
        if (dialoguePresent()) {
            Dialogues.processDialogues(DIALOG_FAMILY_PEST);
            return;
        }

        // Find Avan NPC first (try all variants)
        NpcEx avan = new NpcQuery().withIds(NPC_AVAN_IDS).nearest();
        
        // If NPC found, walk to them directly
        if (avan != null) {
            WorldPoint avanPos = avan.getWorldPoint();
            if (!isNear(avanPos, 10)) {
                Walker.walkTo(avanPos);
                return;
            }
            talkToNpc(NPC_AVAN);
            return;
        }
        
        // NPC not found - try teleport if far
        if (!isNear(TILE_AVAN, 50)) {
            // Try Ring of Dueling to Emir's Arena (near Al Kharid)
            ItemEx ringOfDueling = InventoryAPI.getItem(ItemID.RING_OF_DUELING8);
            if (ringOfDueling == null) ringOfDueling = InventoryAPI.getItem(ItemID.RING_OF_DUELING7);
            if (ringOfDueling == null) ringOfDueling = InventoryAPI.getItem(ItemID.RING_OF_DUELING6);
            if (ringOfDueling == null) ringOfDueling = InventoryAPI.getItem(ItemID.RING_OF_DUELING5);
            if (ringOfDueling == null) ringOfDueling = InventoryAPI.getItem(ItemID.RING_OF_DUELING4);
            if (ringOfDueling == null) ringOfDueling = InventoryAPI.getItem(ItemID.RING_OF_DUELING3);
            if (ringOfDueling == null) ringOfDueling = InventoryAPI.getItem(ItemID.RING_OF_DUELING2);
            if (ringOfDueling == null) ringOfDueling = InventoryAPI.getItem(ItemID.RING_OF_DUELING1);
            
            if (ringOfDueling != null) {
                log.info("Using Ring of Dueling to teleport to Emir's Arena (near Al Kharid)");
                InventoryAPI.interact(ringOfDueling, "Emir's Arena");
                Delays.tick(8);
                return;
            }
            
            log.info("Please teleport to Al Kharid area to find Avan.");
            return;
        }

        // NPC not found - walk to Avan's location
        WorldPoint playerPos = PlayerEx.getLocal().getWorldPoint();
        int distanceToAvan = playerPos.distanceTo(TILE_AVAN);
        
        log.info("Avan not visible, distance to tile: {}", distanceToAvan);
        
        if (distanceToAvan > 30) {
            // Long distance - use MovementAPI
            log.info("Long distance walk to Avan...");
            MovementAPI.walkToWorldPoint(TILE_AVAN);
            Delays.tick(5);
        } else if (distanceToAvan > 10) {
            // Close distance - try Walker for local pathfinding (handles doors)
            log.info("Close distance, using Walker for local pathfinding...");
            try {
                Walker.walkTo(TILE_AVAN);
            } catch (Exception e) {
                log.warn("Walker failed, using MovementAPI: {}", e.getMessage());
                MovementAPI.walkToWorldPoint(TILE_AVAN);
                Delays.tick(3);
            }
        } else {
            log.info("At Avan's location but NPC not visible yet, trying to talk...");
            talkToNpc(NPC_AVAN);
        }
    }

    private void handleTalkToCaleb() {
        if (talkedToCaleb) {
            state = State.VISIT_BROTHERS;
            return;
        }

        // Handle dialogue - just select "Family Pest"
        if (dialoguePresent()) {
            Dialogues.processDialogues(DIALOG_FAMILY_PEST);
            return;
        }

        // Find Caleb NPC first (try all variants)
        NpcEx caleb = new NpcQuery().withIds(NPC_CALEB_IDS).nearest();
        
        // If NPC found, check if we can reach them
        if (caleb != null) {
            WorldPoint calebPos = caleb.getWorldPoint();
            WorldPoint playerPos = PlayerEx.getLocal().getWorldPoint();
            int distanceToNpc = playerPos.distanceTo(calebPos);
            
            if (distanceToNpc > 5) {
                // Walk to NPC using Walker for local pathfinding
                try {
                    Walker.walkTo(calebPos);
                    Delays.tick(2);
                } catch (Exception e) {
                    log.warn("Walker failed for Caleb: {}", e.getMessage());
                    // Fallback to MovementAPI
                    MovementAPI.walkToWorldPoint(calebPos);
                    Delays.tick(3);
                }
                return;
            }
            talkToNpc(NPC_CALEB);
            return;
        }
        
        // NPC not found - walk to Caleb's tile location
        WorldPoint playerPos = PlayerEx.getLocal().getWorldPoint();
        int distanceToCaleb = playerPos.distanceTo(TILE_CALEB);
        
        log.info("Caleb not visible, distance to tile: {}", distanceToCaleb);
        
        // Walk to Caleb's location using MovementAPI for long distance
        if (distanceToCaleb > 10) {
            log.info("Walking to Caleb's location...");
            MovementAPI.walkToWorldPoint(TILE_CALEB);
            Delays.tick(5);
        } else {
            log.info("At Caleb's location but NPC not visible yet, waiting...");
        }
    }
    
    private boolean hasCamelotTeleportRunes() {
        // Need 1 law rune and 5 air runes
        int lawRunes = InventoryAPI.getCount(ItemID.LAW_RUNE);
        int airRunes = InventoryAPI.getCount(ItemID.AIR_RUNE);
        return lawRunes >= 1 && airRunes >= 5;
    }

    private void handleTravelToJohnathon() {
        if (talkedToJohnathon) {
            state = State.VISIT_BROTHERS;
            return;
        }

        // Check if upstairs
        if (isAtJollyBoarUpstairs()) {
            state = State.TALK_TO_JOHNATHON;
            return;
        }

        // Near stairs? Climb them
        if (isNear(TILE_JOLLY_STAIRS, 30)) {
            TileObjectEx stairs = new TileObjectQuery().withId(OBJ_JOLLY_BOAR_STAIRS).nearest();
            if (stairs != null) {
                stairs.interact("Climb-up");
                Delays.tick(3);
            }
            return;
        }
        
        // Try Varrock Teleport tablet (lands in Varrock square, close to Jolly Boar)
        if (!isNear(TILE_JOLLY_STAIRS, 100)) {
            ItemEx varrockTab = InventoryAPI.getItem(ItemID.VARROCK_TELEPORT);
            if (varrockTab != null) {
                log.info("Using Varrock Teleport tablet to reach Jolly Boar");
                InventoryAPI.interact(varrockTab, "Break");
                Delays.tick(8);
                return;
            }
        }

        // Walk to stairs
        try {
            Walker.walkTo(TILE_JOLLY_STAIRS);
        } catch (IndexOutOfBoundsException e) {
            log.warn("Cannot find path to Jolly Boar. Please use Varrock teleport.");
        }
    }

    private void handleTalkToJohnathon() {
        if (talkedToJohnathon) {
            state = State.VISIT_BROTHERS;
            return;
        }

        // Handle dialogue - just select "Family Pest"
        if (dialoguePresent()) {
            Dialogues.processDialogues(DIALOG_FAMILY_PEST);
            return;
        }

        // Find Johnathon NPC (try all variants)
        NpcEx johnathon = new NpcQuery().withIds(NPC_JOHNATHON_IDS).nearest();
        if (johnathon != null) {
            WorldPoint johnPos = johnathon.getWorldPoint();
            if (!isNear(johnPos, 5)) {
                Walker.walkTo(johnPos);
                return;
            }
        }
        
        talkToNpc(NPC_JOHNATHON);
    }

    private void handleReturnToDimintheis() {
        if (isNear(TILE_DIMINTHEIS, 20)) {
            state = State.TALK_TO_DIMINTHEIS_2;
            return;
        }
        
        // Try Lumberyard Teleport tablet (lands near sawmill, close to Dimintheis)
        if (!isNear(TILE_DIMINTHEIS, 100)) {
            ItemEx lumberyardTab = InventoryAPI.getItem(ItemID.LUMBERYARD_TELEPORT);
            if (lumberyardTab != null) {
                log.info("Using Lumberyard Teleport tablet to return to Dimintheis");
                InventoryAPI.interact(lumberyardTab, "Teleport");
                Delays.tick(8);
                return;
            }
            
            // Try Varrock Teleport tablet as fallback (lands in Varrock square)
            ItemEx varrockTab = InventoryAPI.getItem(ItemID.VARROCK_TELEPORT);
            if (varrockTab != null) {
                log.info("Using Varrock Teleport tablet to return to Dimintheis");
                InventoryAPI.interact(varrockTab, "Break");
                Delays.tick(8);
                return;
            }
        }

        try {
            Walker.walkTo(TILE_DIMINTHEIS);
        } catch (IndexOutOfBoundsException e) {
            log.warn("Cannot find path to Dimintheis. Please use Lumberyard/Varrock teleport.");
        }
    }

    private void handleTalkToDimintheis2() {
        if (questProgress >= PROGRESS_COMPLETE) {
            state = State.COMPLETE;
            return;
        }

        // Check for coins before talking
        int coins = InventoryAPI.getCount(995); // Use raw ID for coins
        log.info("Coin count: {} (ITEM_COINS={})", coins, ITEM_COINS);
        if (coins < COINS_REQUIRED) {
            log.warn("Need {} coins but only have {} - please get coins before final turn-in!", COINS_REQUIRED, coins);
            return;
        }

        // Handle dialogue - select "Family Pest" then "Yes"
        if (dialoguePresent()) {
            Dialogues.processDialogues(DIALOG_FAMILY_PEST, DIALOG_YES);
            return;
        }

        talkToNpc(NPC_DIMINTHEIS);
    }

    // ==================== //
    // Helper Methods       //
    // ==================== //

    private boolean isNear(WorldPoint target, int distance) {
        WorldPoint current = PlayerEx.getLocal().getWorldPoint();
        return current.distanceTo(target) <= distance;
    }

    private boolean isAtJollyBoarUpstairs() {
        WorldPoint current = PlayerEx.getLocal().getWorldPoint();
        return current.getPlane() == JOLLY_BOAR_UPSTAIRS_PLANE &&
               current.getX() >= JOLLY_BOAR_UPSTAIRS_MIN_X &&
               current.getX() <= JOLLY_BOAR_UPSTAIRS_MAX_X &&
               current.getY() >= JOLLY_BOAR_UPSTAIRS_MIN_Y &&
               current.getY() <= JOLLY_BOAR_UPSTAIRS_MAX_Y;
    }

    private boolean isFamilyCrestComplete() {
        try {
            Quest familyCrest = Quest.FAMILY_CREST;
            QuestState questState = familyCrest.getState(client);
            return questState == QuestState.FINISHED;
        } catch (Exception e) {
            log.warn("Error checking Family Crest state: {}", e.getMessage());
            return false;
        }
    }

    private void talkToNpc(int npcId) {
        NpcEx npc = new NpcQuery().withIds(npcId).nearest();
        if (npc != null) {
            npc.interact("Talk-to");
            Delays.tick(2);
        }
    }

    // ==================== //
    // State Enum           //
    // ==================== //

    public enum State {
        IDLE,
        CHECK_REQUIREMENTS,
        TRAVEL_TO_DIMINTHEIS,
        TALK_TO_DIMINTHEIS_1,
        VISIT_BROTHERS,
        TALK_TO_AVAN,
        TALK_TO_CALEB,
        TRAVEL_TO_JOHNATHON,
        TALK_TO_JOHNATHON,
        RETURN_TO_DIMINTHEIS,
        TALK_TO_DIMINTHEIS_2,
        COMPLETE
    }

    public enum BrotherOrder {
        AVAN_FIRST,
        CALEB_FIRST
    }
}