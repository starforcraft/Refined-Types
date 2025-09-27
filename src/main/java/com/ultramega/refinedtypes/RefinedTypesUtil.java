package com.ultramega.refinedtypes;

import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;

public final class RefinedTypesUtil {
    public static final String MOD_ID = "refinedtypes";
    public static final ResourceLocation CREATIVE_MODE_TAB = createRefinedTypesIdentifier(MOD_ID);
    public static final MutableComponent MOD = Component.translatable("refinedtypes.configuration.title");

    public static final String ARS_NOUVEAU = "ars_nouveau";
    public static final String INDUSTRIAL_FOREGOING_SOULS = "industrialforegoingsouls";
    @Nullable
    private static Boolean arsNouveauLoaded = null;
    @Nullable
    private static Boolean industrialForegoingSoulsLoaded = null;

    private RefinedTypesUtil() {
    }

    public static ResourceLocation createRefinedTypesIdentifier(final String value) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, value);
    }

    public static String createRefinedTypesTranslationKey(final String category, final String value) {
        return String.format("%s.%s.%s", category, MOD_ID, value);
    }

    public static MutableComponent createRefinedTypesTranslation(final String category, final String value) {
        return Component.translatable(createRefinedTypesTranslationKey(category, value));
    }

    public static MutableComponent createRefinedTypesTranslation(final String category,
                                                                 final String value,
                                                                 final Object... args) {
        return Component.translatable(createRefinedTypesTranslationKey(category, value), args);
    }

    public static boolean isArsNouveauLoaded() {
        if (arsNouveauLoaded == null) {
            arsNouveauLoaded = ModList.get().isLoaded(ARS_NOUVEAU);
        }
        return arsNouveauLoaded;
    }

    public static boolean isIndustrialForegoingSoulsLoaded() {
        if (industrialForegoingSoulsLoaded == null) {
            industrialForegoingSoulsLoaded = ModList.get().isLoaded(INDUSTRIAL_FOREGOING_SOULS);
        }
        return industrialForegoingSoulsLoaded;
    }
}
