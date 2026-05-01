//package com.ultramega.refinedtypes.type.soul;
//
//import com.ultramega.refinedtypes.type.Type;
//
//import com.refinedmods.refinedstorage.api.resource.ResourceKey;
//import com.refinedmods.refinedstorage.common.api.support.resource.ResourceRendering;
//import com.refinedmods.refinedstorage.common.util.IdentifierUtil;
//
//import java.text.DecimalFormat;
//import java.text.DecimalFormatSymbols;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Locale;
//
//import com.mojang.blaze3d.systems.RenderSystem;
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.blaze3d.vertex.PoseStack.Pose;
//import com.mojang.blaze3d.vertex.VertexConsumer;
//import com.mojang.math.Axis;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.GuiGraphics;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.client.renderer.RenderType;
//import net.minecraft.client.renderer.texture.OverlayTexture;
//import net.minecraft.network.chat.Component;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.level.Level;
//
//public class SoulResourceRendering implements ResourceRendering {
//    private static final DecimalFormat FORMATTER = new DecimalFormat(
//        "#,###.###",
//        DecimalFormatSymbols.getInstance(Locale.US)
//    );
//
//    private static final float IN_WORLD_SIZE = 0.3F;
//    private static final float IN_WORLD_PIXEL = IN_WORLD_SIZE / 16F;
//    private static final int WARDEN_TEXTURE_SIZE = 128;
//
//    private final List<GuiParticle> particleList = new ArrayList<>();
//    private long lastCheckedForParticle;
//
//    @Override
//    public String formatAmount(final long amount, final boolean withUnits) {
//        return (!withUnits ? format(amount) : IdentifierUtil.formatWithUnits(amount));
//    }
//
//    @Override
//    public Component getDisplayName(final ResourceKey resourceKey) {
//        if (resourceKey instanceof SoulResource(Type type)) {
//            return type.getDisplayName();
//        }
//        return Component.empty();
//    }
//
//    @Override
//    public List<Component> getTooltip(final ResourceKey resourceKey) {
//        if (resourceKey instanceof SoulResource(Type type)) {
//            return List.of(type.getDisplayName());
//        }
//        return Collections.emptyList();
//    }
//
//    @Override
//    public void render(final ResourceKey resourceKey, final GuiGraphics graphics, final int x, final int y) {
//        if (resourceKey instanceof SoulResource) {
//            this.render(graphics, x, y);
//        }
//    }
//
//    @Override
//    public void render(final ResourceKey resourceKey,
//                       final PoseStack poseStack,
//                       final MultiBufferSource multiBufferSource,
//                       final int light,
//                       final Level level) {
//        if (resourceKey instanceof SoulResource) {
//            this.render(poseStack, multiBufferSource, light, level);
//        }
//    }
//
//    /**
//     * From <a href="https://github.com/InnovativeOnlineIndustries/Soulplied-Energistics/blob/3ba158d730c35e6b7b7e1d403659f0a1e621d0af/src/main/java/com/buuz135/soulplied_energistics/client/SoulKeyRenderHandler.java#L31">Industrial Foregoing Souls</a>
//     */
//    public void render(final GuiGraphics graphics, final int x, final int y) {
//        final Level level = Minecraft.getInstance().level;
//        if (level == null) {
//            return;
//        }
//
//        final ResourceLocation wardenTexture = ResourceLocation.withDefaultNamespace("textures/entity/warden/warden.png");
//        final ResourceLocation wardenHeart = ResourceLocation.withDefaultNamespace("textures/entity/warden/warden_heart.png");
//        graphics.pose().pushPose();
//
//        graphics.blit(wardenTexture, x, y, 12, 14, 16, 16, 128, 128);
//
//        graphics.pose().pushPose();
//        float heartTiming = 30f;
//        heartTiming = 1 - ((level.getGameTime() % heartTiming) / heartTiming);
//        RenderSystem.setShaderColor(heartTiming, heartTiming, heartTiming, heartTiming);
//        graphics.blit(wardenHeart, x - 1, y - 1, 11, 13, 18, 18, 128, 128);
//        RenderSystem.setShaderColor(1, 1, 1, 1f);
//        graphics.pose().popPose();
//
//        final var rotation = level.getGameTime() % 160 - 80;
//
//        graphics.pose().pushPose();
//        graphics.pose().translate(x, y - 1, 100);
//        graphics.pose().mulPose(Axis.YP.rotationDegrees(rotation));
//        graphics.blit(wardenTexture, 0, 0, 91, 13, 17, 18, 128, 128);
//        graphics.pose().popPose();
//
//        graphics.pose().pushPose();
//        graphics.pose().translate(x + 16, y + 17, 100);
//        graphics.pose().mulPose(Axis.ZP.rotationDegrees(180));
//        graphics.pose().mulPose(Axis.YP.rotationDegrees(rotation));
//
//        graphics.blit(wardenTexture, 0, 0, 91, 13, 17, 18, 128, 128);
//        graphics.pose().popPose();
//
//        graphics.pose().scale(0.75F, 0.75F, 0.75F);
//        final var fullAmount = 0.05;
//        final var xSize = 8;
//        final var ySize = 6;
//        final var currentTime = level.getGameTime();
//        if (this.lastCheckedForParticle != currentTime) {
//            if (level.random.nextDouble() <= fullAmount) {
//                this.particleList.add(new GuiParticle(level.random.nextInt(xSize),
//                    ySize - level.random.nextInt(3), currentTime));
//            }
//            this.lastCheckedForParticle = currentTime;
//        }
//        final var ageTick = 3;
//        if (currentTime % ageTick == 0) {
//            this.particleList.removeIf(guiParticle -> ((currentTime - guiParticle.age) / ageTick) > 10);
//        }
//        graphics.pose().translate(0, 0, 200);
//        for (final GuiParticle guiParticle : this.particleList.reversed()) {
//            final var particleAge = ((currentTime - guiParticle.age) / (double) ageTick);
//            final var extraY = ((ySize - 32) / 20D) * particleAge;
//            graphics.blit(ResourceLocation.withDefaultNamespace("textures/particle/sculk_soul_" + Math.clamp((int) particleAge, 0, 10) + ".png"),
//                (int) ((x + guiParticle.x) * (1 / 0.75f)), (int) ((int) (y + guiParticle.y + extraY) * (1 / 0.75f)), 0, 0, 16, 16, 16, 16);
//        }
//        graphics.pose().popPose();
//    }
//
//    public void render(final PoseStack poseStack,
//                       final MultiBufferSource multiBufferSource,
//                       final int light,
//                       final Level level) {
//        final ResourceLocation wardenTexture = ResourceLocation.withDefaultNamespace("textures/entity/warden/warden.png");
//        final ResourceLocation wardenHeart = ResourceLocation.withDefaultNamespace("textures/entity/warden/warden_heart.png");
//
//        final long currentTime = level.getGameTime();
//
//        poseStack.pushPose();
//        poseStack.translate(-IN_WORLD_SIZE / 2F, IN_WORLD_SIZE / 2F, 0);
//
//        // Base Warden face
//        blitInWorld(poseStack, multiBufferSource, wardenTexture, light, 0, 0, 0.000F, 16, 16, 12, 14,
//            WARDEN_TEXTURE_SIZE, WARDEN_TEXTURE_SIZE, 255, 255, 255, 255);
//
//        // Pulsing heart overlay
//        final float heartTiming = 1F - ((currentTime % 30F) / 30F);
//        final int heartColor = Math.clamp((int) (heartTiming * 255F), 0, 255);
//
//        blitInWorld(poseStack, multiBufferSource, wardenHeart, light, -1, -1, 0.001F, 18, 18, 11, 13,
//            WARDEN_TEXTURE_SIZE, WARDEN_TEXTURE_SIZE, heartColor, heartColor, heartColor, heartColor);
//
//        // Ribcage
//        final var rotation = (float) ((Math.sin(currentTime * 0.06F) + 1D) / 2D) * 3.5F;
//
//        poseStack.pushPose();
//        poseStack.translate(0, IN_WORLD_PIXEL, 0.002F);
//        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
//        blitInWorld(poseStack, multiBufferSource, wardenTexture, light, 0, 0, 0, 17, 18, 91, 13,
//            WARDEN_TEXTURE_SIZE, WARDEN_TEXTURE_SIZE, 255, 255, 255, 255);
//        poseStack.popPose();
//
//        poseStack.pushPose();
//        poseStack.translate(16F * IN_WORLD_PIXEL, -17F * IN_WORLD_PIXEL, 0.002F);
//        poseStack.mulPose(Axis.ZP.rotationDegrees(180));
//        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
//        blitInWorld(poseStack, multiBufferSource, wardenTexture, light, 0, 0, 0, 17, 18, 91, 13,
//            WARDEN_TEXTURE_SIZE, WARDEN_TEXTURE_SIZE, 255, 255, 255, 255);
//        poseStack.popPose();
//
//        // Soul particles
//        final var fullAmount = 0.05;
//        final var xSize = 8;
//        final var ySize = 6;
//
//        if (this.lastCheckedForParticle != currentTime) {
//            if (level.random.nextDouble() <= fullAmount) {
//                this.particleList.add(new GuiParticle(level.random.nextInt(xSize), ySize - level.random.nextInt(3), currentTime));
//            }
//            this.lastCheckedForParticle = currentTime;
//        }
//
//        final var ageTick = 3;
//        if (currentTime % ageTick == 0) {
//            this.particleList.removeIf(guiParticle -> ((currentTime - guiParticle.age) / ageTick) > 10);
//        }
//
//        for (final GuiParticle guiParticle : this.particleList.reversed()) {
//            final var particleAge = ((currentTime - guiParticle.age) / (double) ageTick);
//            final var extraY = ((ySize - 32) / 20D) * particleAge;
//            final int particleFrame = Math.clamp((int) particleAge, 0, 10);
//
//            blitInWorld(poseStack, multiBufferSource, ResourceLocation.withDefaultNamespace("textures/particle/sculk_soul_" + particleFrame + ".png"),
//                light, guiParticle.x, (float) (guiParticle.y + extraY), 0.003F, 12, 12, 0, 0, 16, 16,
//                255, 255, 255, 255);
//        }
//
//        poseStack.popPose();
//    }
//
//    private static void blitInWorld(final PoseStack poseStack,
//                                    final MultiBufferSource multiBufferSource,
//                                    final ResourceLocation texture,
//                                    final int light,
//                                    final float x,
//                                    final float y,
//                                    final float z,
//                                    final float width,
//                                    final float height,
//                                    final float u,
//                                    final float v,
//                                    final float textureWidth,
//                                    final float textureHeight,
//                                    final int red,
//                                    final int green,
//                                    final int blue,
//                                    final int alpha) {
//        final VertexConsumer buffer = multiBufferSource.getBuffer(RenderType.text(texture));
//        final PoseStack.Pose pose = poseStack.last();
//
//        final float x0 = x * IN_WORLD_PIXEL;
//        final float x1 = (x + width) * IN_WORLD_PIXEL;
//        final float y0 = -(y + height) * IN_WORLD_PIXEL;
//        final float y1 = -y * IN_WORLD_PIXEL;
//
//        final float u0 = u / textureWidth;
//        final float u1 = (u + width) / textureWidth;
//        final float v0 = v / textureHeight;
//        final float v1 = (v + height) / textureHeight;
//
//        // Front face
//        addVertex(buffer, pose, x0, y0, z, u0, v1, light, red, green, blue, alpha);
//        addVertex(buffer, pose, x1, y0, z, u1, v1, light, red, green, blue, alpha);
//        addVertex(buffer, pose, x1, y1, z, u1, v0, light, red, green, blue, alpha);
//        addVertex(buffer, pose, x0, y1, z, u0, v0, light, red, green, blue, alpha);
//    }
//
//    private static void addVertex(final VertexConsumer buffer,
//                                  final Pose pose,
//                                  final float x,
//                                  final float y,
//                                  final float z,
//                                  final float u,
//                                  final float v,
//                                  final int light,
//                                  final int red,
//                                  final int green,
//                                  final int blue,
//                                  final int alpha) {
//        buffer.addVertex(pose, x, y, z)
//            .setColor(red, green, blue, alpha)
//            .setUv(u, v)
//            .setOverlay(OverlayTexture.NO_OVERLAY)
//            .setLight(light)
//            .setNormal(pose, 0, 0, 1);
//    }
//
//    public static String format(final long amount) {
//        return FORMATTER.format(amount);
//    }
//
//    private record GuiParticle(int x, int y, long age) {
//    }
//}
