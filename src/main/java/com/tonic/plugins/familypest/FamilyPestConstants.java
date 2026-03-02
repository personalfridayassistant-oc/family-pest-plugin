package com.tonic.plugins.familypest;

import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;

/**
 * Game constants for Family Pest miniquest plugin.
 * 
 * All IDs are documented with their symbolic names for reference.
 * Raw integer values are used where symbolic constants may not exist in RuneLite.
 */
public final class FamilyPestConstants {

    private FamilyPestConstants() {
        // Utility class
    }

    // ==================== //
    // NPCs                 //
    // ==================== //

    /**
     * NPC IDs used in Family Pest miniquest.
     * Note: The _2OPS variants are used after Family Crest completion.
     */
    public static final int NPC_DIMINTHEIS = NpcID.DIMINTHEIS;                           // 4984
    // NPC variants - try all possible IDs for each brother (use raw IDs)
    // Avan: 385=avan_fitzharmon_man, 386=avan_fitzharmon_avan_1op, 387=avan_fitzharmon_avan_2ops
    public static final int[] NPC_AVAN_IDS = {385, 386, 387};
    // Caleb: 4317=caleb_fitzharmon_1op, 4986=caleb_fitzharmon, 5433=caleb_fitzharmon_2ops
    public static final int[] NPC_CALEB_IDS = {4317, 4986, 5433};
    // Johnathon: 4988=johnathon_fitzharmon, 5443=johnathon_fitzharmon_1op, 5445=johnathon_fitzharmon_2ops
    public static final int[] NPC_JOHNATHON_IDS = {4988, 5443, 5445};
    
    // Keep single IDs for backward compatibility
    public static final int NPC_AVAN = 387;
    public static final int NPC_CALEB = 5433;
    public static final int NPC_JOHNATHON = 5445;

    // ==================== //
    // Objects              //
    // ==================== //

    /**
     * Object IDs used in Family Pest miniquest.
     */
    public static final int OBJ_JOLLY_BOAR_STAIRS = 11797;                                // Jolly Boar stairs

    // ==================== //
    // Items                //
    // ==================== //

    /**
     * Item IDs used in Family Pest miniquest.
     */
    public static final int ITEM_COINS = ItemID.COINS;                                   // 995
    
    /**
     * Required coins for the miniquest (500,000).
     */
    public static final int COINS_REQUIRED = 500000;

    // ==================== //
    // Varbits              //
    // ==================== //

    /**
     * Main quest progress varbit.
     * Values: 0=Not started, 1=Talking to brothers, 2=Complete
     */
    public static final int VARBIT_QUEST_PROGRESS = 5347;                                // FAMILY_QUEST_PROGRESS

    /**
     * Brother visited flags.
     */
    public static final int VARBIT_TALKED_CALEB = 5348;                                   // FAMILY_QUEST_CALEB
    public static final int VARBIT_TALKED_AVAN = 5349;                                    // FAMILY_QUEST_AVAN
    public static final int VARBIT_TALKED_JOHNATHON = 5350;                               // FAMILY_QUEST_JOHNATHON

    // ==================== //
    // Quest Progress Values //
    // ==================== //

    /**
     * Quest progress values.
     */
    public static final int PROGRESS_NOT_STARTED = 0;
    public static final int PROGRESS_TALK_TO_BROTHERS = 1;
    public static final int PROGRESS_COMPLETE = 2;

    // ==================== //
    // Locations            //
    // ==================== //

    /**
     * Key WorldPoint locations for Family Pest miniquest.
     */
    // From quest-helper FamilyCrest.java - verified coordinates
    public static final WorldPoint TILE_DIMINTHEIS = new WorldPoint(3280, 3402, 0);  // SE Varrock
    public static final WorldPoint TILE_AVAN = new WorldPoint(3295, 3275, 0);        // South of Al Kharid mine
    public static final WorldPoint TILE_CALEB = new WorldPoint(2819, 3452, 0);       // Catherby
    public static final WorldPoint TILE_JOHNATHON = new WorldPoint(3277, 3504, 1);   // Jolly Boar upstairs
    public static final WorldPoint TILE_JOLLY_STAIRS = new WorldPoint(3286, 3494, 0);
    public static final WorldPoint TILE_VARROCK_EAST_BANK = new WorldPoint(3250, 3420, 0);

    // ==================== //
    // Zone Bounds (for Jolly Boar upstairs) //
    // ==================== //

    /**
     * Jolly Boar Inn upstairs bounds (where Johnathon is located).
     * From quest-helper: Zone(new WorldPoint(3271, 3485, 1), new WorldPoint(3288, 3511, 1))
     */
    public static final int JOLLY_BOAR_UPSTAIRS_MIN_X = 3271;
    public static final int JOLLY_BOAR_UPSTAIRS_MAX_X = 3288;
    public static final int JOLLY_BOAR_UPSTAIRS_MIN_Y = 3485;
    public static final int JOLLY_BOAR_UPSTAIRS_MAX_Y = 3511;
    public static final int JOLLY_BOAR_UPSTAIRS_PLANE = 1;

    // ==================== //
    // Dialog Options       //
    // ==================== //

    /**
     * Dialog options for initial Dimintheis conversation.
     */
    public static final String[] DIALOG_DIMINTHEIS_START = {
        "Have you got any quests for me?",
        "Oh come on, however menial, I want to help!"
    };

    /**
     * Dialog option for Family Pest topic.
     */
    public static final String DIALOG_FAMILY_PEST = "Family Pest";

    /**
     * Dialog option for confirming 500k coin payment.
     */
    public static final String DIALOG_YES = "Yes";

    // ==================== //
    // Quest Prerequisite   //
    // ==================== //

    /**
     * Quest prerequisite: Family Crest must be completed.
     * Use Quest.FAMILY_CREST for checking completion.
     */
    public static final String QUEST_FAMILY_CREST_NAME = "Family Crest";
}