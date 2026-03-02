package com.tonic.plugins.familypest;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;

import javax.inject.Inject;
import java.awt.*;

/**
 * Overlay for Family Pest miniquest plugin.
 * Shows current state and quest progress.
 */
@Slf4j
public class FamilyPestOverlay extends Overlay {

    private final FamilyPestPlugin plugin;
    private final PanelComponent panelComponent = new PanelComponent();

    @Inject
    private FamilyPestOverlay(FamilyPestPlugin plugin) {
        this.plugin = plugin;
        setPosition(OverlayPosition.TOP_LEFT);
        panelComponent.setPreferredSize(new Dimension(200, 0));
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        panelComponent.getChildren().clear();

        // Title
        panelComponent.getChildren().add(LineComponent.builder()
            .left("Family Pest")
            .leftColor(Color.CYAN)
            .build());

        // State
        panelComponent.getChildren().add(LineComponent.builder()
            .left("State:")
            .right(plugin.getState().toString())
            .build());

        // Progress
        String progressText;
        switch (plugin.getQuestProgress()) {
            case 0:
                progressText = "Not started";
                break;
            case 1:
                progressText = "Talk to brothers";
                break;
            case 2:
                progressText = "Complete!";
                break;
            default:
                progressText = String.valueOf(plugin.getQuestProgress());
        }
        panelComponent.getChildren().add(LineComponent.builder()
            .left("Progress:")
            .right(progressText)
            .build());

        // Brother status
        panelComponent.getChildren().add(LineComponent.builder()
            .left("Avan:")
            .right(plugin.isTalkedToAvan() ? "✓" : "✗")
            .rightColor(plugin.isTalkedToAvan() ? Color.GREEN : Color.RED)
            .build());

        panelComponent.getChildren().add(LineComponent.builder()
            .left("Caleb:")
            .right(plugin.isTalkedToCaleb() ? "✓" : "✗")
            .rightColor(plugin.isTalkedToCaleb() ? Color.GREEN : Color.RED)
            .build());

        panelComponent.getChildren().add(LineComponent.builder()
            .left("Johnathon:")
            .right(plugin.isTalkedToJohnathon() ? "✓" : "✗")
            .rightColor(plugin.isTalkedToJohnathon() ? Color.GREEN : Color.RED)
            .build());

        return panelComponent.render(graphics);
    }
}