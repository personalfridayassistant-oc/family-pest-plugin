package com.tonic.plugins.cooksassistant;

import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;

import javax.inject.Inject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

public class CooksAssistantOverlay extends Overlay {

    private final CooksAssistantPlugin plugin;
    private final CooksAssistantConfig config;
    private final PanelComponent panelComponent = new PanelComponent();

    @Inject
    private CooksAssistantOverlay(CooksAssistantPlugin plugin, CooksAssistantConfig config) {
        this.plugin = plugin;
        this.config = config;
        setPosition(OverlayPosition.TOP_LEFT);
        panelComponent.setPreferredSize(new Dimension(230, 0));
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!config.enableOverlay()) {
            return null;
        }

        panelComponent.getChildren().clear();

        panelComponent.getChildren().add(LineComponent.builder()
            .left("Cook's Assistant")
            .leftColor(Color.ORANGE)
            .build());

        panelComponent.getChildren().add(LineComponent.builder()
            .left("State")
            .right(plugin.getState().name())
            .build());

        panelComponent.getChildren().add(LineComponent.builder()
            .left("Quest")
            .right(plugin.getQuestStateText())
            .build());

        panelComponent.getChildren().add(LineComponent.builder()
            .left("Egg")
            .right(plugin.hasEgg() ? "✓" : "✗")
            .rightColor(plugin.hasEgg() ? Color.GREEN : Color.RED)
            .build());

        panelComponent.getChildren().add(LineComponent.builder()
            .left("Bucket of milk")
            .right(plugin.hasMilk() ? "✓" : "✗")
            .rightColor(plugin.hasMilk() ? Color.GREEN : Color.RED)
            .build());

        panelComponent.getChildren().add(LineComponent.builder()
            .left("Pot of flour")
            .right(plugin.hasFlour() ? "✓" : "✗")
            .rightColor(plugin.hasFlour() ? Color.GREEN : Color.RED)
            .build());

        return panelComponent.render(graphics);
    }
}
