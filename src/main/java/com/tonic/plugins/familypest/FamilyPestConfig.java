package com.tonic.plugins.familypest;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

/**
 * Configuration for Family Pest miniquest plugin.
 */
@ConfigGroup("familypest")
public interface FamilyPestConfig extends Config {

    @ConfigSection(
        name = "General Settings",
        description = "General plugin settings",
        position = 0
    )
    String generalSection = "general";

    @ConfigItem(
        keyName = "brotherOrder",
        name = "Brother Visit Order",
        description = "Order to visit the Fitzharmon brothers",
        position = 0,
        section = generalSection
    )
    default FamilyPestPlugin.BrotherOrder brotherOrder() {
        return FamilyPestPlugin.BrotherOrder.AVAN_FIRST;
    }

    @ConfigItem(
        keyName = "enableOverlay",
        name = "Enable Overlay",
        description = "Show the debug overlay with quest progress",
        position = 1,
        section = generalSection
    )
    default boolean enableOverlay() {
        return true;
    }
}