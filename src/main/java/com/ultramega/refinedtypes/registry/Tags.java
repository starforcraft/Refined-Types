package com.ultramega.refinedtypes.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static com.ultramega.refinedtypes.RefinedTypesUtil.createRefinedTypesIdentifier;

public final class Tags {
    public static final TagKey<Item> ENERGY_STORAGE_DISKS = createTag("energy_storage_disks");
//    public static final TagKey<Item> SOURCE_STORAGE_DISKS = createTag("source_storage_disks");
//    public static final TagKey<Item> SOUL_STORAGE_DISKS = createTag("soul_storage_disks");

    private Tags() {
    }

    private static TagKey<Item> createTag(final String id) {
        return TagKey.create(Registries.ITEM, createRefinedTypesIdentifier(id));
    }
}
