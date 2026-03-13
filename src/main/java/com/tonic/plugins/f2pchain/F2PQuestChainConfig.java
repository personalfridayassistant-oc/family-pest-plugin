package com.tonic.plugins.f2pchain;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("f2pquestchain")
public interface F2PQuestChainConfig extends Config {

    @ConfigItem(
        keyName = "enableOverlay",
        name = "Enable Overlay",
        description = "Show chain state and selected quest",
        position = 0
    )
    default boolean enableOverlay() {
        return true;
    }

    @ConfigItem(
        keyName = "chainMode",
        name = "Chain selected quests",
        description = "Complete selected quests automatically in sequence",
        position = 1
    )
    default boolean chainMode() {
        return true;
    }

    @ConfigItem(
        keyName = "buyMissingFromGE",
        name = "Buy missing items at GE",
        description = "Walk to Grand Exchange and attempt to buy required items",
        position = 2
    )
    default boolean buyMissingFromGE() {
        return true;
    }

    @ConfigItem(keyName = "cooksAssistant", name = "Cook's Assistant", description = "Include in chain", position = 10)
    default boolean cooksAssistant() { return true; }

    @ConfigItem(keyName = "sheepShearer", name = "Sheep Shearer", description = "Include in chain", position = 11)
    default boolean sheepShearer() { return true; }

    @ConfigItem(keyName = "doricsQuest", name = "Doric's Quest", description = "Include in chain", position = 12)
    default boolean doricsQuest() { return true; }

    @ConfigItem(keyName = "witchsPotion", name = "Witch's Potion", description = "Include in chain", position = 13)
    default boolean witchsPotion() { return true; }

    @ConfigItem(keyName = "impCatcher", name = "Imp Catcher", description = "Include in chain", position = 14)
    default boolean impCatcher() { return true; }

    @ConfigItem(keyName = "runeMysteries", name = "Rune Mysteries", description = "Include in chain", position = 15)
    default boolean runeMysteries() { return true; }

    @ConfigItem(keyName = "restlessGhost", name = "The Restless Ghost", description = "Include in chain", position = 16)
    default boolean restlessGhost() { return false; }

    @ConfigItem(keyName = "romeoAndJuliet", name = "Romeo & Juliet", description = "Include in chain", position = 17)
    default boolean romeoAndJuliet() { return false; }

    @ConfigItem(keyName = "ernestTheChicken", name = "Ernest the Chicken", description = "Include in chain", position = 18)
    default boolean ernestTheChicken() { return false; }

    @ConfigItem(keyName = "piratesTreasure", name = "Pirate's Treasure", description = "Include in chain", position = 19)
    default boolean piratesTreasure() { return false; }

    @ConfigItem(keyName = "princeAliRescue", name = "Prince Ali Rescue", description = "Include in chain", position = 20)
    default boolean princeAliRescue() { return false; }

    @ConfigItem(keyName = "vampireSlayer", name = "Vampire Slayer", description = "Include in chain", position = 21)
    default boolean vampireSlayer() { return false; }

    @ConfigItem(keyName = "shieldOfArrav", name = "Shield of Arrav", description = "Include in chain", position = 22)
    default boolean shieldOfArrav() { return false; }

    @ConfigItem(keyName = "knightsSword", name = "The Knight's Sword", description = "Include in chain", position = 23)
    default boolean knightsSword() { return false; }

    @ConfigItem(keyName = "goblinDiplomacy", name = "Goblin Diplomacy", description = "Include in chain", position = 24)
    default boolean goblinDiplomacy() { return false; }

    @ConfigItem(keyName = "demonSlayer", name = "Demon Slayer", description = "Include in chain", position = 25)
    default boolean demonSlayer() { return false; }

    @ConfigItem(keyName = "blackKnightsFortress", name = "Black Knights' Fortress", description = "Include in chain", position = 26)
    default boolean blackKnightsFortress() { return false; }

    @ConfigItem(keyName = "dragonSlayerI", name = "Dragon Slayer I", description = "Include in chain", position = 27)
    default boolean dragonSlayerI() { return false; }

    @ConfigItem(keyName = "xMarksTheSpot", name = "X Marks the Spot", description = "Include in chain", position = 28)
    default boolean xMarksTheSpot() { return false; }

    @ConfigItem(keyName = "belowIceMountain", name = "Below Ice Mountain", description = "Include in chain", position = 29)
    default boolean belowIceMountain() { return false; }

    @ConfigItem(keyName = "misthalinMystery", name = "Misthalin Mystery", description = "Include in chain", position = 30)
    default boolean misthalinMystery() { return false; }

    @ConfigItem(keyName = "corsairCurse", name = "The Corsair Curse", description = "Include in chain", position = 31)
    default boolean corsairCurse() { return false; }
}
