package com.ultramega.refinedtypes.type.essentia;

import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceRendering;
import com.refinedmods.refinedstorage.common.util.IdentifierUtil;

import java.util.List;

import com.leclowndu93150.thaumaturge.api.aspect.AspectComponents;
import com.leclowndu93150.thaumaturge.api.aspect.AspectKnowledgeAccess;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.client.AspectRendering;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

public class EssentiaResourceRendering implements ResourceRendering {
    public static String format(final long amount) {
        return Long.toString(amount);
    }

    @Nullable
    private static Holder<IAspect> resolve(final EssentiaResource resource) {
        final var level = Minecraft.getInstance().level;
        return level == null ? null : resource.resolve(level.registryAccess());
    }

    @Override
    public String formatAmount(final long amount, final boolean withUnits) {
        return withUnits ? IdentifierUtil.formatWithUnits(amount) : format(amount);
    }

    @Override
    public Component getDisplayName(final ResourceKey resource) {
        if (!(resource instanceof EssentiaResource essentia)) {
            return Component.empty();
        }
        final var aspect = resolve(essentia);
        return aspect == null ? Component.literal(essentia.aspectId().toString()) : AspectComponents.name(aspect);
    }

    @Override
    public List<Component> getTooltip(final ResourceKey resource) {
        return List.of(this.getDisplayName(resource), Component.translatable("misc.refinedtypes.resource_type.essentia"));
    }

    @Override
    public void render(final ResourceKey resource, final GuiGraphicsExtractor graphics, final int x, final int y) {
        if (resource instanceof EssentiaResource essentia) {
            final var aspect = resolve(essentia);
            if (aspect != null) {
                AspectRendering.renderGui(graphics, Minecraft.getInstance().font, x, y, aspect, 0);
            }
        }
    }

    @Override
    public void render(final ResourceKey resource, final PoseStack poseStack,
                       final SubmitNodeCollector nodes, final int light, final long seed) {
        if (!(resource instanceof EssentiaResource essentia)) {
            return;
        }
        final var aspect = resolve(essentia);
        if (aspect == null) {
            return;
        }
        final var knowledge = AspectKnowledgeAccess.of(aspect);
        poseStack.pushPose();
        poseStack.scale(0.3F, 0.3F, 0.3F);
        nodes.submitCustomGeometry(poseStack, AspectRendering.renderType(aspect, knowledge, AspectRendering.BlendMode.ALPHA),
            (pose, buffer) -> AspectRendering.renderQuad(pose, buffer, aspect, knowledge, 1.0F, false, light));
        poseStack.popPose();
    }
}
