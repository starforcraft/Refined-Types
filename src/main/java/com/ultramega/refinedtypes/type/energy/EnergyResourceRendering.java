package com.ultramega.refinedtypes.type.energy;

import com.ultramega.refinedtypes.TypeRenderer;
import com.ultramega.refinedtypes.type.Type;

import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceRendering;
import com.refinedmods.refinedstorage.common.util.IdentifierUtil;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public class EnergyResourceRendering implements ResourceRendering {
    private static final DecimalFormat FORMATTER = new DecimalFormat(
        "#,###.###",
        DecimalFormatSymbols.getInstance(Locale.US)
    );

    @Override
    public String formatAmount(final long amount, final boolean withUnits) {
        return (!withUnits ? format(amount) : IdentifierUtil.formatWithUnits(amount));
    }

    @Override
    public Component getDisplayName(final ResourceKey resourceKey) {
        if (resourceKey instanceof EnergyResource(Type type)) {
            return type.getDisplayName();
        }
        return Component.empty();
    }

    @Override
    public List<Component> getTooltip(final ResourceKey resourceKey) {
        if (resourceKey instanceof EnergyResource(Type type)) {
            return List.of(type.getDisplayName());
        }
        return Collections.emptyList();
    }

    @Override
    public void render(final ResourceKey resourceKey, final GuiGraphics graphics, final int x, final int y) {
        if (resourceKey instanceof EnergyResource(Type type)) {
            TypeRenderer.render(graphics.pose(), x, y, type);
        }
    }

    @Override
    public void render(final ResourceKey resourceKey,
                       final PoseStack poseStack,
                       final MultiBufferSource multiBufferSource,
                       final int light,
                       final Level level) {
        if (resourceKey instanceof EnergyResource(Type type)) {
            TypeRenderer.render(poseStack, multiBufferSource, light, type);
        }
    }

    public static String format(final long amount) {
        return FORMATTER.format(amount);
    }
}
