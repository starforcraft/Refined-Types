package com.ultramega.refinedtypes.datagen.tag;

import com.ultramega.refinedtypes.registry.Items;
import com.ultramega.refinedtypes.storage.energy.EnergyStorageVariant;
import com.ultramega.refinedtypes.storage.soul.SoulStorageVariant;
import com.ultramega.refinedtypes.storage.source.SourceStorageVariant;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import static com.ultramega.refinedtypes.RefinedTypesUtil.MOD_ID;
import static com.ultramega.refinedtypes.registry.Tags.ENERGY_STORAGE_DISKS;
import static com.ultramega.refinedtypes.registry.Tags.SOUL_STORAGE_DISKS;
import static com.ultramega.refinedtypes.registry.Tags.SOURCE_STORAGE_DISKS;

public class ItemTagsProviderImpl extends ItemTagsProvider {
    public ItemTagsProviderImpl(final PackOutput packOutput,
                                final CompletableFuture<HolderLookup.Provider> registries,
                                final TagsProvider<Block> blockTagsProvider,
                                final ExistingFileHelper existingFileHelper) {
        super(packOutput, registries, blockTagsProvider.contentsGetter(), MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(final HolderLookup.Provider provider) {
        this.addAllToTag(ENERGY_STORAGE_DISKS,
            Arrays.stream(EnergyStorageVariant.values())
                .map(Items::getEnergyStorageDisk)
                .map(t -> (Supplier<Item>) () -> t)
                .toList(), false);
        this.addAllToTag(SOURCE_STORAGE_DISKS,
            Arrays.stream(SourceStorageVariant.values())
                .map(Items::getSourceStorageDisk)
                .map(t -> (Supplier<Item>) () -> t)
                .toList(), true);
        this.addAllToTag(SOUL_STORAGE_DISKS,
            Arrays.stream(SoulStorageVariant.values())
                .map(Items::getSoulStorageDisk)
                .map(t -> (Supplier<Item>) () -> t)
                .toList(), true);
    }

    private <T extends Item> void addAllToTag(final TagKey<Item> tag,
                                              final Collection<Supplier<T>> items,
                                              final boolean optional) {
        final var builder = this.tag(tag);

        for (final Supplier<T> supplier : items) {
            final Item item = supplier.get();
            if (optional) {
                builder.addOptional(BuiltInRegistries.ITEM.getKey(item));
            } else {
                builder.add(item);
            }
        }

        builder.replace(false);
    }
}
