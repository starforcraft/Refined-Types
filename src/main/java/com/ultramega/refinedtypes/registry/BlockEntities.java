package com.ultramega.refinedtypes.registry;

import com.ultramega.refinedtypes.storage.energy.EnergyStorageVariant;
import com.ultramega.refinedtypes.storage.soul.SoulStorageVariant;
import com.ultramega.refinedtypes.storage.source.SourceStorageVariant;

import com.refinedmods.refinedstorage.common.api.support.network.AbstractNetworkNodeContainerBlockEntity;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.world.level.block.entity.BlockEntityType;

public final class BlockEntities {
    private static final Map<EnergyStorageVariant, Supplier<BlockEntityType<AbstractNetworkNodeContainerBlockEntity<?>>>> ENERGY_STORAGE_BLOCKS =
        new EnumMap<>(EnergyStorageVariant.class);
    private static final Map<SourceStorageVariant, Supplier<BlockEntityType<AbstractNetworkNodeContainerBlockEntity<?>>>> SOURCE_STORAGE_BLOCKS =
        new EnumMap<>(SourceStorageVariant.class);
    private static final Map<SoulStorageVariant, Supplier<BlockEntityType<AbstractNetworkNodeContainerBlockEntity<?>>>> SOUL_STORAGE_BLOCKS =
        new EnumMap<>(SoulStorageVariant.class);

    private BlockEntities() {
    }

    public static BlockEntityType<AbstractNetworkNodeContainerBlockEntity<?>> getEnergyStorageBlock(final EnergyStorageVariant variant) {
        return ENERGY_STORAGE_BLOCKS.get(variant).get();
    }

    public static void setEnergyStorageBlock(final EnergyStorageVariant variant,
                                             final Supplier<BlockEntityType<AbstractNetworkNodeContainerBlockEntity<?>>> supplier) {
        ENERGY_STORAGE_BLOCKS.put(variant, supplier);
    }

    public static BlockEntityType<AbstractNetworkNodeContainerBlockEntity<?>> getSourceStorageBlock(final SourceStorageVariant variant) {
        return SOURCE_STORAGE_BLOCKS.get(variant).get();
    }

    public static void setSourceStorageBlock(final SourceStorageVariant variant,
                                             final Supplier<BlockEntityType<AbstractNetworkNodeContainerBlockEntity<?>>> supplier) {
        SOURCE_STORAGE_BLOCKS.put(variant, supplier);
    }

    public static BlockEntityType<AbstractNetworkNodeContainerBlockEntity<?>> getSoulStorageBlock(final SoulStorageVariant variant) {
        return SOUL_STORAGE_BLOCKS.get(variant).get();
    }

    public static void setSoulStorageBlock(final SoulStorageVariant variant,
                                             final Supplier<BlockEntityType<AbstractNetworkNodeContainerBlockEntity<?>>> supplier) {
        SOUL_STORAGE_BLOCKS.put(variant, supplier);
    }
}
