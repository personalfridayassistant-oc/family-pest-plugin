package com.tonic.plugins.cooksassistant;

import net.runelite.api.ItemID;
import net.runelite.api.NpcID;
import net.runelite.api.coords.WorldPoint;

public final class CooksAssistantConstants {

    private CooksAssistantConstants() {
    }

    public static final int NPC_COOK = NpcID.COOK;

    public static final int ITEM_EGG = ItemID.EGG;
    public static final int ITEM_BUCKET_OF_MILK = ItemID.BUCKET_OF_MILK;
    public static final int ITEM_POT_OF_FLOUR = ItemID.POT_OF_FLOUR;

    public static final int REQUIRED_EGG_COUNT = 1;
    public static final int REQUIRED_MILK_COUNT = 1;
    public static final int REQUIRED_FLOUR_COUNT = 1;

    public static final WorldPoint TILE_LUMBRIDGE_CASTLE_KITCHEN = new WorldPoint(3209, 3215, 0);

    public static final String DIALOG_WHATS_WRONG = "What's wrong?";
    public static final String DIALOG_HELP_COOK = "I'm always happy to help a cook in distress.";
    public static final String DIALOG_YES = "Yes";
    public static final String DIALOG_EGG = "egg";
    public static final String DIALOG_MILK = "bucket of milk";
    public static final String DIALOG_FLOUR = "pot of flour";
}
