package com.tonic.plugins.cooksassistant;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("cooksassistant")
public interface CooksAssistantConfig extends Config {

    @ConfigItem(
        keyName = "enableOverlay",
        name = "Enable Overlay",
        description = "Show current plugin state and ingredient readiness",
        position = 0
    )
    default boolean enableOverlay() {
        return true;
    }
}
