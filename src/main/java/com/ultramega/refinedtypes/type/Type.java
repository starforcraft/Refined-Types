package com.ultramega.refinedtypes.type;

import java.util.Locale;
import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public record Type(String name, @Nullable ResourceLocation icon, MutableComponent tooltip) {
    @Override
    public String name() {
        return this.name.toLowerCase(Locale.ROOT);
    }

    public Component getDisplayName() {
        return Component.literal(this.name);
    }

    public MutableComponent getTooltip() {
        return this.tooltip.withStyle(ChatFormatting.AQUA);
    }
}
