package com.ultramega.refinedtypes;

import com.ultramega.refinedtypes.registry.Types;
import com.ultramega.refinedtypes.type.TypeOperationResult;

import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.neoforge.support.resource.SimpleItemStackResourceHandler;

import java.util.Optional;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

public final class RefinedTypesUtil {
    public static final String MOD_ID = "refinedtypes";
    public static final Identifier CREATIVE_MODE_TAB = createRefinedTypesIdentifier(MOD_ID);
    public static final MutableComponent MOD = Component.translatable("refinedtypes.configuration.title");

    public static final String ARS_NOUVEAU = "ars_nouveau";
    public static final String INDUSTRIAL_FOREGOING_SOULS = "industrialforegoingsouls";
    @Nullable
    private static Boolean arsNouveauLoaded = null;
    @Nullable
    private static Boolean industrialForegoingSoulsLoaded = null;

    private RefinedTypesUtil() {
    }

    public static Identifier createRefinedTypesIdentifier(final String value) {
        return Identifier.fromNamespaceAndPath(MOD_ID, value);
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

    public static Optional<TypeOperationResult> dischargeContainer(final ItemStack container) {
        if (container.isEmpty()) {
            return Optional.empty();
        }
        final SimpleItemStackResourceHandler interceptingHandler = SimpleItemStackResourceHandler.forStack(container);
        final EnergyHandler handler = container.getCapability(Capabilities.Energy.ITEM, ItemAccess.forHandlerIndex(interceptingHandler, 0));
        if (handler == null) {
            return Optional.empty();
        }
        try (Transaction tx = Transaction.openRoot()) {
            final int extracted = handler.extract(Integer.MAX_VALUE, tx);
            if (extracted > 0) {
                tx.commit();
            } else {
                return Optional.empty();
            }
            return Optional.of(new TypeOperationResult(
                interceptingHandler.getStack(),
                Types.FE.get(),
                extracted
            ));
        }
    }

    public static Optional<TypeOperationResult> chargeContainer(final ItemStack container,
                                                          final ResourceAmount resourceAmount) {
        final SimpleItemStackResourceHandler interceptingHandler = SimpleItemStackResourceHandler.forStack(container);
        final EnergyHandler storage = container.getCapability(Capabilities.Energy.ITEM, ItemAccess.forHandlerIndex(interceptingHandler, 0));
        if (storage == null) {
            return Optional.empty();
        }
        try (Transaction tx = Transaction.openRoot()) {
            final long inserted = storage.insert((int) resourceAmount.amount(), tx);
            return Optional.of(new TypeOperationResult(
                interceptingHandler.getStack(),
                Types.FE.get(),
                inserted
            ));
        }
    }
}
