package com.ultramega.refinedtypes.registry;

import com.ultramega.refinedtypes.networkenergizer.NetworkEnergizerBlock;
import com.ultramega.refinedtypes.storage.energy.EnergyStorageVariant;
import com.ultramega.refinedtypes.storage.soul.SoulStorageVariant;
import com.ultramega.refinedtypes.storage.source.SourceStorageVariant;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.Nullable;

import net.minecraft.world.level.block.Block;

import static java.util.Objects.requireNonNull;

public final class Blocks {
    private static final Map<EnergyStorageVariant, Supplier<Block>> ENERGY_STORAGE_BLOCKS = new EnumMap<>(EnergyStorageVariant.class);
    private static final Map<SourceStorageVariant, Supplier<Block>> SOURCE_STORAGE_BLOCKS = new EnumMap<>(SourceStorageVariant.class);
    private static final Map<SoulStorageVariant, Supplier<Block>> SOUL_STORAGE_BLOCKS = new EnumMap<>(SoulStorageVariant.class);

    @Nullable
    private static Supplier<NetworkEnergizerBlock> networkEnergizer;

    private Blocks() {
    }

    public static Block getEnergyStorageBlock(final EnergyStorageVariant variant) {
        return ENERGY_STORAGE_BLOCKS.get(variant).get();
    }

    public static void setEnergyStorageBlock(final EnergyStorageVariant variant, final Supplier<Block> supplier) {
        ENERGY_STORAGE_BLOCKS.put(variant, supplier);
    }

    public static Block getSourceStorageBlock(final SourceStorageVariant variant) {
        return SOURCE_STORAGE_BLOCKS.get(variant).get();
    }

    public static void setSourceStorageBlock(final SourceStorageVariant variant, final Supplier<Block> supplier) {
        SOURCE_STORAGE_BLOCKS.put(variant, supplier);
    }

    public static Block getSoulStorageBlock(final SoulStorageVariant variant) {
        return SOUL_STORAGE_BLOCKS.get(variant).get();
    }

    public static void setSoulStorageBlock(final SoulStorageVariant variant, final Supplier<Block> supplier) {
        SOUL_STORAGE_BLOCKS.put(variant, supplier);
    }

    public static void setNetworkEnergizer(final Supplier<NetworkEnergizerBlock> networkEnergizerSupplier) {
        networkEnergizer = networkEnergizerSupplier;
    }

    public static NetworkEnergizerBlock getNetworkEnergizer() {
        return requireNonNull(networkEnergizer).get();
    }
}
