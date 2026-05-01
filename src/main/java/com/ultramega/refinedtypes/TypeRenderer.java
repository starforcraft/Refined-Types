package com.ultramega.refinedtypes;

import com.ultramega.refinedtypes.type.Type;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;

public final class TypeRenderer {
    private TypeRenderer() {
    }

    public static void render(final GuiGraphicsExtractor graphics, final int x, final int y, final Type type) {
        final Minecraft minecraft = Minecraft.getInstance();
        final Identifier icon = type.icon();
        if (icon == null) {
            return;
        }
        final TextureAtlasSprite sprite = minecraft.getAtlasManager().getAtlasOrThrow(AtlasIds.BLOCKS).getSprite(icon);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, x, y, 16, 16, 0xFFFFFFFF);
    }

    public static void render(final PoseStack poseStack, final SubmitNodeCollector nodes, final int light, final Type type) {
        final Minecraft minecraft = Minecraft.getInstance();
        final Identifier icon = type.icon();
        if (icon == null) {
            return;
        }
        final TextureAtlasSprite sprite = minecraft.getAtlasManager().getAtlasOrThrow(AtlasIds.BLOCKS).getSprite(icon);
        nodes.submitCustomGeometry(poseStack, RenderTypes.entitySolid(sprite.atlasLocation()), (pose, buffer) -> {
            final float scale = 0.3F;
            final var x0 = -scale / 2;
            final var y0 = scale / 2;
            final var x1 = scale / 2;
            final var y1 = -scale / 2;
            buffer.addVertex(pose, x0, y1, 0)
                .setColor(0xFFFFFFFF)
                .setUv(sprite.getU0(), sprite.getV1())
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(0, 0, 1);
            buffer.addVertex(pose, x1, y1, 0)
                .setColor(0xFFFFFFFF)
                .setUv(sprite.getU1(), sprite.getV1())
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(0, 0, 1);
            buffer.addVertex(pose, x1, y0, 0)
                .setColor(0xFFFFFFFF)
                .setUv(sprite.getU1(), sprite.getV0())
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(0, 0, 1);
            buffer.addVertex(pose, x0, y0, 0)
                .setColor(0xFFFFFFFF)
                .setUv(sprite.getU0(), sprite.getV0())
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(0, 0, 1);
        });
    }
}
