package com.ultramega.refinedtypes.type;

import java.util.Locale;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public record Type(String name, @Nullable Identifier icon) {
    @Override
    public String name() {
        return this.name.toLowerCase(Locale.ROOT);
    }

    public Component getDisplayName() {
        return Component.literal(this.name);
    }
}
