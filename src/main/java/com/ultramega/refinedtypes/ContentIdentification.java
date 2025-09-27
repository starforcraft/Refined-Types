package com.ultramega.refinedtypes;

import net.minecraft.network.chat.MutableComponent;

import static com.ultramega.refinedtypes.RefinedTypesUtil.createRefinedTypesTranslation;

public final class ContentIdentification {
    public static final String NETWORK_ENERGIZER = "network_energizer";
    public static final MutableComponent NETWORK_ENERGIZER_NAME = name(NETWORK_ENERGIZER);

    private ContentIdentification() {
    }

    private static MutableComponent name(final String name) {
        return createRefinedTypesTranslation("block", name);
    }
}
