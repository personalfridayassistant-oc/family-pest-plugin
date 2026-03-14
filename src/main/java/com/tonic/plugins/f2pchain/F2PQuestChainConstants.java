package com.tonic.plugins.f2pchain;

import lombok.Value;
import net.runelite.api.ItemID;
import net.runelite.api.NpcID;
import net.runelite.api.coords.WorldPoint;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class F2PQuestChainConstants {

    private F2PQuestChainConstants() {}

    public static final WorldPoint GRAND_EXCHANGE_TILE = new WorldPoint(3164, 3487, 0);
    public static final int NPC_GRAND_EXCHANGE_CLERK = NpcID.GRAND_EXCHANGE_CLERK;

    @Value
    public static class RequiredItem {
        int itemId;
        String name;
        int quantity;
    }

    @Value
    public static class QuestTask {
        String questEnumName;
        String name;
        WorldPoint targetTile;
        int targetNpcId;
        List<String> dialogueChoices;
        List<RequiredItem> requiredItems;
        String requirementsSummary;
    }

    public static List<QuestTask> allF2PTasks() {
        return Arrays.asList(
            new QuestTask("COOKS_ASSISTANT", "Cook's Assistant", new WorldPoint(3209, 3215, 0), NpcID.COOK,
                Arrays.asList("What's wrong?", "I'm always happy to help a cook in distress.", "Yes"),
                Arrays.asList(new RequiredItem(ItemID.EGG, "Egg", 1), new RequiredItem(ItemID.BUCKET_OF_MILK, "Bucket of milk", 1), new RequiredItem(ItemID.POT_OF_FLOUR, "Pot of flour", 1)),
                "No skill requirements. Items: Egg, Bucket of milk, Pot of flour."),
            new QuestTask("SHEEP_SHEARER", "Sheep Shearer", new WorldPoint(3190, 3273, 0), NpcID.FRED_THE_FARMER,
                Collections.singletonList("I'm looking for a quest."),
                Collections.singletonList(new RequiredItem(ItemID.BALL_OF_WOOL, "Ball of wool", 20)),
                "No skill requirements. Items: 20 Balls of wool."),
            new QuestTask("DORICS_QUEST", "Doric's Quest", new WorldPoint(2952, 3451, 0), NpcID.DORIC,
                Collections.singletonList("I wanted to use your anvils."),
                Arrays.asList(new RequiredItem(ItemID.CLAY, "Clay", 6), new RequiredItem(ItemID.COPPER_ORE, "Copper ore", 4), new RequiredItem(ItemID.IRON_ORE, "Iron ore", 2)),
                "No skill requirements. Items: 6 Clay, 4 Copper ore, 2 Iron ore."),
            new QuestTask("WITCHS_POTION", "Witch's Potion", new WorldPoint(2968, 3205, 0), NpcID.HETTY,
                Collections.singletonList("I'm in search of a quest."),
                Arrays.asList(new RequiredItem(ItemID.BURNT_MEAT, "Burnt meat", 1), new RequiredItem(ItemID.EYE_OF_NEWT, "Eye of newt", 1), new RequiredItem(ItemID.ONION, "Onion", 1)),
                "No skill requirements. Items: Burnt meat, Eye of newt, Onion."),
            new QuestTask("IMP_CATCHER", "Imp Catcher", new WorldPoint(3104, 3162, 1), NpcID.WIZARD_MIZGOG,
                Collections.singletonList("I can get those for you."),
                Arrays.asList(new RequiredItem(ItemID.BLACK_BEAD, "Black bead", 1), new RequiredItem(ItemID.RED_BEAD, "Red bead", 1), new RequiredItem(ItemID.WHITE_BEAD, "White bead", 1), new RequiredItem(ItemID.YELLOW_BEAD, "Yellow bead", 1)),
                "No skill requirements. Items: Red/Yellow/Black/White beads."),
            new QuestTask("RUNE_MYSTERIES", "Rune Mysteries", new WorldPoint(3219, 3218, 0), NpcID.DUKE_HORACIO,
                Collections.singletonList("Have you any quests for me?"), Collections.emptyList(),
                "No skill requirements. Multi-step handoff between Duke and Aubury."),
            new QuestTask("THE_RESTLESS_GHOST", "The Restless Ghost", new WorldPoint(3243, 3210, 0), NpcID.FATHER_AERECK,
                Collections.singletonList("I'm looking for a quest!"), Collections.emptyList(),
                "No skill requirements. Requires speaking with Father Aereck / Father Urhney."),
            new QuestTask("ROMEO__JULIET", "Romeo & Juliet", new WorldPoint(3212, 3424, 0), NpcID.ROMEO,
                Collections.singletonList("Yes, ok, I'll let her know."), Collections.emptyList(),
                "No skill requirements. Multi-NPC item/message sequence."),
            new QuestTask("ERNEST_THE_CHICKEN", "Ernest the Chicken", new WorldPoint(3109, 3330, 0), NpcID.VERONICA,
                Collections.singletonList("Aha, sounds like a quest!"), Collections.emptyList(),
                "No skill requirements. Mansion puzzle quest."),
            new QuestTask("PIRATES_TREASURE", "Pirate's Treasure", new WorldPoint(3042, 3253, 0), NpcID.REDBEARD_FRANK,
                Collections.singletonList("Can I offer you some rum?"),
                Collections.singletonList(new RequiredItem(ItemID.WHITE_APRON, "White apron", 1)),
                "No skill requirements. Often needs White apron and Karamja trip items."),
            new QuestTask("PRINCE_ALI_RESCUE", "Prince Ali Rescue", new WorldPoint(3305, 3163, 0), -1,
                Collections.singletonList("I can offer to help."), Collections.emptyList(),
                "No skill requirements. Multi-item rescue sequence."),
            new QuestTask("VAMPIRE_SLAYER", "Vampire Slayer", new WorldPoint(3097, 3266, 0), NpcID.MORGAN,
                Collections.singletonList("Yes."), Collections.emptyList(),
                "No skill requirements. Combat with Count Draynor recommended."),
            new QuestTask("SHIELD_OF_ARRAV", "Shield of Arrav", new WorldPoint(3209, 3495, 0), NpcID.RELDO,
                Collections.singletonList("I'm in search of a quest."), Collections.emptyList(),
                "No skill requirements. Requires gang choice and partner coordination."),
            new QuestTask("THE_KNIGHTS_SWORD", "The Knight's Sword", new WorldPoint(2961, 3338, 0), NpcID.SQUIRE,
                Collections.singletonList("And how is life as a squire?"), Collections.emptyList(),
                "10 Mining recommended. Items include redberry pie and blurite sequence."),
            new QuestTask("GOBLIN_DIPLOMACY", "Goblin Diplomacy", new WorldPoint(3212, 3254, 0), NpcID.GENERAL_BENTNOZE,
                Collections.singletonList("Why are you arguing about armor?"),
                Arrays.asList(new RequiredItem(ItemID.ORANGE_DYE, "Orange dye", 1), new RequiredItem(ItemID.BLUE_DYE, "Blue dye", 1)),
                "No skill requirements. Usually needs orange/blue dyes and goblin mail."),
            new QuestTask("DEMON_SLAYER", "Demon Slayer", new WorldPoint(3219, 3473, 0), NpcID.GYPSY,
                Collections.singletonList("I seek a mighty weapon."), Collections.emptyList(),
                "No skill requirements. Combat quest with Delrith fight."),
            new QuestTask("BLACK_KNIGHTS_FORTRESS", "Black Knights' Fortress", new WorldPoint(2959, 3338, 0), NpcID.SIR_AMIK_VARZE,
                Collections.singletonList("I seek a quest."), Collections.emptyList(),
                "12 Quest Points required."),
            new QuestTask("DRAGON_SLAYER_I", "Dragon Slayer I", new WorldPoint(3191, 3362, 0), NpcID.GUILDMASTER,
                Collections.singletonList("I seek a quest."), Collections.emptyList(),
                "32 Quest Points required. High combat + anti-dragon gear recommended."),
            new QuestTask("X_MARKS_THE_SPOT", "X Marks the Spot", new WorldPoint(3228, 3218, 0), NpcID.VEOS,
                Collections.singletonList("Do you have anything for me to do?"), Collections.emptyList(),
                "No skill requirements. Beginner clue-style route."),
            new QuestTask("BELOW_ICE_MOUNTAIN", "Below Ice Mountain", new WorldPoint(3001, 3453, 0), -1,
                Collections.singletonList("Can I help?"), Collections.emptyList(),
                "No skill requirements. Camdozaal questline route."),
            new QuestTask("MISTHALIN_MYSTERY", "Misthalin Mystery", new WorldPoint(3245, 3383, 0), -1,
                Collections.singletonList("I'll help investigate."), Collections.emptyList(),
                "No skill requirements. Lumbridge manor investigation."),
            new QuestTask("THE_CORSAIR_CURSE", "The Corsair Curse", new WorldPoint(3008, 3049, 0), -1,
                Collections.singletonList("Any jobs for me?"), Collections.emptyList(),
                "No skill requirements. Corsair Cove travel and multi-NPC sequence.")
        );
    }
}
