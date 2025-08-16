package com.ultramega.refinedtypes.type.soul;

import com.ultramega.refinedtypes.type.Type;

import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceRendering;
import com.refinedmods.refinedstorage.common.util.IdentifierUtil;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class SoulResourceRendering implements ResourceRendering {
    private static final DecimalFormat FORMATTER = new DecimalFormat(
        "#,###.###",
        DecimalFormatSymbols.getInstance(Locale.US)
    );

    private final List<GuiParticle> particleList = new ArrayList<>();
    private long lastCheckedForParticle;

    @Override
    public String formatAmount(final long amount, final boolean withUnits) {
        return (!withUnits ? format(amount) : IdentifierUtil.formatWithUnits(amount));
    }

    @Override
    public Component getDisplayName(final ResourceKey resourceKey) {
        if (resourceKey instanceof SoulResource(Type type)) {
            return type.getDisplayName();
        }
        return Component.empty();
    }

    @Override
    public List<Component> getTooltip(final ResourceKey resourceKey) {
        if (resourceKey instanceof SoulResource(Type type)) {
            return List.of(type.getDisplayName());
        }
        return Collections.emptyList();
    }

    @Override
    public void render(final ResourceKey resourceKey, final GuiGraphics graphics, final int x, final int y) {
        if (resourceKey instanceof SoulResource) {
            this.render(graphics, x, y);
        }
    }

    @Override
    public void render(final ResourceKey resourceKey,
                       final PoseStack poseStack,
                       final MultiBufferSource multiBufferSource,
                       final int light,
                       final Level level) {
        // TODO: implement this at some point
        //if (resourceKey instanceof SoulResource) {
            //TypeRenderer.render(poseStack, multiBufferSource, light, type);
        //}
    }

    /**
     * Copied from <a href="https://github.com/InnovativeOnlineIndustries/Soulplied-Energistics/blob/master/src/main/java/com/buuz135/soulplied_energistics/client/SoulKeyRenderHandler.java#L31">Industrial Foregoing Souls</a>
     */
    public void render(final GuiGraphics graphics, final int x, final int y) {
        graphics.pose().pushPose();
        final ResourceLocation wardenTexture = ResourceLocation.withDefaultNamespace("textures/entity/warden/warden.png");
        final ResourceLocation wardenHeart = ResourceLocation.withDefaultNamespace("textures/entity/warden/warden_heart.png");
        graphics.blit(wardenTexture, x, y, 12, 14, 16, 16, 128, 128);

        graphics.pose().pushPose();
        float heartTiming = 30f;
        heartTiming = 1 - ((Minecraft.getInstance().level.getGameTime() % heartTiming) / heartTiming);
        RenderSystem.setShaderColor(heartTiming, heartTiming, heartTiming, heartTiming);
        graphics.blit(wardenHeart, x - 1, y - 1, 11, 13, 18, 18, 128, 128);
        RenderSystem.setShaderColor(1, 1, 1, 1f);
        graphics.pose().popPose();

        final var rotation = Minecraft.getInstance().level.getGameTime() % 160 - 80;

        graphics.pose().pushPose();
        graphics.pose().translate(x, y - 1, 100);
        graphics.pose().mulPose(Axis.YP.rotationDegrees(rotation));
        graphics.blit(wardenTexture, 0, 0, 91, 13, 17, 18, 128, 128);
        graphics.pose().popPose();

        graphics.pose().pushPose();
        graphics.pose().translate(x + 16, y + 17, 100);
        graphics.pose().mulPose(Axis.ZP.rotationDegrees(180));
        graphics.pose().mulPose(Axis.YP.rotationDegrees(rotation));

        graphics.blit(wardenTexture, 0, 0, 91, 13, 17, 18, 128, 128);
        graphics.pose().popPose();

        graphics.pose().scale(0.75F, 0.75F, 0.75F);
        final var fullAmount = 0.05;
        final var xSize = 8;
        final var ySize = 6;
        final var currentTime = Minecraft.getInstance().level.getGameTime();
        if (this.lastCheckedForParticle != currentTime) {
            if (Minecraft.getInstance().level.random.nextDouble() <= fullAmount) {
                this.particleList.add(new GuiParticle(Minecraft.getInstance().level.random.nextInt(xSize),
                    ySize - Minecraft.getInstance().level.random.nextInt(3), currentTime));
            }
            this.lastCheckedForParticle = currentTime;
        }
        final var ageTick = 3;
        if (currentTime % ageTick == 0) {
            this.particleList.removeIf(guiParticle -> ((currentTime - guiParticle.age) / ageTick) > 10);
        }
        graphics.pose().translate(0, 0, 200);
        for (final GuiParticle guiParticle : this.particleList.reversed()) {
            final var particleAge = ((currentTime - guiParticle.age) / (double) ageTick);
            final var extraY = ((ySize - 32) / 20D) * particleAge;
            graphics.blit(ResourceLocation.withDefaultNamespace("textures/particle/sculk_soul_" + Math.max(0, Math.min(10, (int) particleAge)) + ".png"),
                (int) ((x + guiParticle.x) * (1 / 0.75f)), (int) ((int) (y + guiParticle.y + extraY) * (1 / 0.75f)), 0, 0, 16, 16, 16, 16);
        }
        graphics.pose().popPose();
    }

    public static String format(final long amount) {
        return FORMATTER.format(amount);
    }

    private record GuiParticle(int x, int y, long age) {
    }
}
