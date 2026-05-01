package com.ultramega.refinedtypes.networkenergizer;

import com.refinedmods.refinedstorage.common.support.widget.ProgressWidget;

import java.util.List;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import static com.ultramega.refinedtypes.RefinedTypesUtil.createRefinedTypesIdentifier;
import static net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED;

public class EnergyProgressWidget extends ProgressWidget {
    private static final Identifier SPRITE = createRefinedTypesIdentifier("widget/energy_progress_bar");

    private final DoubleSupplier progressSupplier;
    private final Supplier<List<Component>> tooltipSupplier;

    public EnergyProgressWidget(final int x,
                                final int y,
                                final int width,
                                final int height,
                                final DoubleSupplier progressSupplier,
                                final Supplier<List<Component>> tooltipSupplier) {
        super(x, y, width, height, progressSupplier, tooltipSupplier);
        this.progressSupplier = progressSupplier;
        this.tooltipSupplier = tooltipSupplier;
    }

    @Override
    protected void extractWidgetRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float partialTicks) {
        final int correctedHeight = (int) (this.progressSupplier.getAsDouble() * this.height);
        final int correctedY = this.getY() + this.height - correctedHeight;
        final int u = 0;
        final int v = this.height - correctedHeight;
        graphics.blitSprite(GUI_TEXTURED, SPRITE, 16, 70, u, v, this.getX(), correctedY, this.width, correctedHeight);
        if (this.isHovered) {
            graphics.setComponentTooltipForNextFrame(Minecraft.getInstance().font, this.tooltipSupplier.get(), mouseX, mouseY);
        }
    }
}
