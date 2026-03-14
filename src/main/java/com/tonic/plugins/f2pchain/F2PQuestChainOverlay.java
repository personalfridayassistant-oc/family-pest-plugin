package com.tonic.plugins.f2pchain;

import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;

import javax.inject.Inject;
import java.awt.*;

public class F2PQuestChainOverlay extends Overlay {

    private final F2PQuestChainPlugin plugin;
    private final F2PQuestChainConfig config;
    private final PanelComponent panelComponent = new PanelComponent();

    @Inject
    public F2PQuestChainOverlay(F2PQuestChainPlugin plugin, F2PQuestChainConfig config) {
        this.plugin = plugin;
        this.config = config;
        setPosition(OverlayPosition.TOP_LEFT);
        panelComponent.setPreferredSize(new Dimension(260, 0));
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!config.enableOverlay()) {
            return null;
        }

        panelComponent.getChildren().clear();
        panelComponent.getChildren().add(LineComponent.builder().left("F2P Quest Chain").leftColor(Color.CYAN).build());
        panelComponent.getChildren().add(LineComponent.builder().left("State").right(plugin.getState().name()).build());
        panelComponent.getChildren().add(LineComponent.builder().left("Active quest").right(plugin.getActiveQuest()).build());
        panelComponent.getChildren().add(LineComponent.builder().left("Selected").right(plugin.getSelectedQuestCountText()).build());
        panelComponent.getChildren().add(LineComponent.builder().left("Missing items").right(String.valueOf(plugin.getMissingItemCount())).build());
        panelComponent.getChildren().add(LineComponent.builder().left("Status").right(plugin.getStatusText()).build());

        return panelComponent.render(graphics);
    }
}
