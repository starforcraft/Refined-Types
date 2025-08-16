package com.ultramega.refinedtypes;

import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;

import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.MOD_ID;

public final class RefinedTypesUtil {
    public static final String MOD_ID = "refinedtypes";
    public static final ResourceLocation CREATIVE_MODE_TAB = createRefinedTypesIdentifier(MOD_ID);
    public static final MutableComponent MOD = Component.translatable("refinedtypes.configuration.title");
    @Nullable
    private static Boolean arsLoaded = null;
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
        if (arsLoaded == null) {
            arsLoaded = ModList.get().isLoaded("ars_nouveau");
        }
        return arsLoaded;
    }

    public static boolean isIndustrialForegoingSoulsLoaded() {
        if (industrialForegoingSoulsLoaded == null) {
            industrialForegoingSoulsLoaded = ModList.get().isLoaded("industrialforegoingsouls");
        }
        return industrialForegoingSoulsLoaded;
    }
}
