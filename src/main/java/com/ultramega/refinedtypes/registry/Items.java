package com.ultramega.refinedtypes.registry;

import com.ultramega.refinedtypes.storage.energy.EnergyStorageVariant;
import com.ultramega.refinedtypes.storage.soul.SoulStorageVariant;
import com.ultramega.refinedtypes.storage.source.SourceStorageVariant;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.world.item.Item;

public final class Items {
    private static final Map<EnergyStorageVariant, Supplier<Item>> ENERGY_STORAGE_PARTS = new EnumMap<>(EnergyStorageVariant.class);
    private static final Map<EnergyStorageVariant, Supplier<Item>> ENERGY_STORAGE_DISKS = new EnumMap<>(EnergyStorageVariant.class);
    private static final Map<EnergyStorageVariant, Supplier<Item>> ENERGY_STORAGE_BLOCKS = new EnumMap<>(EnergyStorageVariant.class);

    private static final Map<SourceStorageVariant, Supplier<Item>> SOURCE_STORAGE_PARTS = new EnumMap<>(SourceStorageVariant.class);
    private static final Map<SourceStorageVariant, Supplier<Item>> SOURCE_STORAGE_DISKS = new EnumMap<>(SourceStorageVariant.class);
    private static final Map<SourceStorageVariant, Supplier<Item>> SOURCE_STORAGE_BLOCKS = new EnumMap<>(SourceStorageVariant.class);

    private static final Map<SoulStorageVariant, Supplier<Item>> SOUL_STORAGE_PARTS = new EnumMap<>(SoulStorageVariant.class);
    private static final Map<SoulStorageVariant, Supplier<Item>> SOUL_STORAGE_DISKS = new EnumMap<>(SoulStorageVariant.class);
    private static final Map<SoulStorageVariant, Supplier<Item>> SOUL_STORAGE_BLOCKS = new EnumMap<>(SoulStorageVariant.class);

    private Items() {
    }

    public static Item getEnergyStoragePart(final EnergyStorageVariant variant) {
        return ENERGY_STORAGE_PARTS.get(variant).get();
    }

    public static void setEnergyStoragePart(final EnergyStorageVariant variant, final Supplier<Item> supplier) {
        ENERGY_STORAGE_PARTS.put(variant, supplier);
    }

    public static Item getEnergyStorageDisk(final EnergyStorageVariant variant) {
        return ENERGY_STORAGE_DISKS.get(variant).get();
    }

    public static void setEnergyStorageDisk(final EnergyStorageVariant variant, final Supplier<Item> supplier) {
        ENERGY_STORAGE_DISKS.put(variant, supplier);
    }

    public static Item getEnergyStorageBlock(final EnergyStorageVariant variant) {
        return ENERGY_STORAGE_BLOCKS.get(variant).get();
    }

    public static void setEnergyStorageBlock(final EnergyStorageVariant variant, final Supplier<Item> supplier) {
        ENERGY_STORAGE_BLOCKS.put(variant, supplier);
    }

    public static Item getSourceStoragePart(final SourceStorageVariant variant) {
        return SOURCE_STORAGE_PARTS.get(variant).get();
    }

    public static void setSourceStoragePart(final SourceStorageVariant variant, final Supplier<Item> supplier) {
        SOURCE_STORAGE_PARTS.put(variant, supplier);
    }

    public static Item getSourceStorageDisk(final SourceStorageVariant variant) {
        return SOURCE_STORAGE_DISKS.get(variant).get();
    }

    public static void setSourceStorageDisk(final SourceStorageVariant variant, final Supplier<Item> supplier) {
        SOURCE_STORAGE_DISKS.put(variant, supplier);
    }

    public static Item getSourceStorageBlock(final SourceStorageVariant variant) {
        return SOURCE_STORAGE_BLOCKS.get(variant).get();
    }

    public static void setSourceStorageBlock(final SourceStorageVariant variant, final Supplier<Item> supplier) {
        SOURCE_STORAGE_BLOCKS.put(variant, supplier);
    }

    public static Item getSoulStoragePart(final SoulStorageVariant variant) {
        return SOUL_STORAGE_PARTS.get(variant).get();
    }

    public static void setSoulStoragePart(final SoulStorageVariant variant, final Supplier<Item> supplier) {
        SOUL_STORAGE_PARTS.put(variant, supplier);
    }

    public static Item getSoulStorageDisk(final SoulStorageVariant variant) {
        return SOUL_STORAGE_DISKS.get(variant).get();
    }

    public static void setSoulStorageDisk(final SoulStorageVariant variant, final Supplier<Item> supplier) {
        SOUL_STORAGE_DISKS.put(variant, supplier);
    }

    public static Item getSoulStorageBlock(final SoulStorageVariant variant) {
        return SOUL_STORAGE_BLOCKS.get(variant).get();
    }

    public static void setSoulStorageBlock(final SoulStorageVariant variant, final Supplier<Item> supplier) {
        SOUL_STORAGE_BLOCKS.put(variant, supplier);
    }
}
