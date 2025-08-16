package com.ultramega.refinedtypes.type;

import java.util.Locale;
import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public record Type(String name, @Nullable ResourceLocation icon) {
    @Override
    public String name() {
        return this.name.toLowerCase(Locale.ROOT);
    }

    public Component getDisplayName() {
        return Component.literal(this.name);
    }
}
