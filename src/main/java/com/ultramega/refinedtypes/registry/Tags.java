package com.ultramega.refinedtypes.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static com.ultramega.refinedtypes.RefinedTypesUtil.createRefinedTypesIdentifier;

public final class Tags {
    public static final TagKey<Item> ENERGY_STORAGE_DISKS = createTag("energy_storage_disks");
    public static final TagKey<Item> SOURCE_STORAGE_DISKS = createTag("source_storage_disks");
    public static final TagKey<Item> SOUL_STORAGE_DISKS = createTag("soul_storage_disks");

    public static final TagKey<Item> SILICON = createConventionTag("silicon");

    private Tags() {
    }

    private static TagKey<Item> createTag(final String id) {
        return TagKey.create(Registries.ITEM, createRefinedTypesIdentifier(id));
    }

    private static TagKey<Item> createConventionTag(final String id) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", id));
    }
}
