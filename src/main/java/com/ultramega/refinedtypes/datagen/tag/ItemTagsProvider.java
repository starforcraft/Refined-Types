package com.ultramega.refinedtypes.datagen.tag;

import com.ultramega.refinedtypes.registry.Items;
import com.ultramega.refinedtypes.storage.energy.EnergyStorageVariant;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagCopyingItemTagProvider;

import static com.ultramega.refinedtypes.RefinedTypesUtil.MOD_ID;
import static com.ultramega.refinedtypes.registry.Tags.ENERGY_STORAGE_DISKS;

public class ItemTagsProvider extends BlockTagCopyingItemTagProvider {
    public ItemTagsProvider(final PackOutput packOutput,
                            final CompletableFuture<HolderLookup.Provider> registries,
                            final CompletableFuture<TagLookup<Block>> blockTagsProvider) {
        super(packOutput, registries, blockTagsProvider, MOD_ID);
    }

    @Override
    protected void addTags(final HolderLookup.Provider provider) {
        this.addAllToTag(ENERGY_STORAGE_DISKS,
            Arrays.stream(EnergyStorageVariant.values())
                .map(Items::getEnergyStorageDisk)
                .map(t -> (Supplier<Item>) () -> t)
                .toList(), false);
//        this.addAllToTag(SOURCE_STORAGE_DISKS,
//            Arrays.stream(SourceStorageVariant.values())
//                .map(Items::getSourceStorageDisk)
//                .map(t -> (Supplier<Item>) () -> t)
//                .toList(), true);
//        this.addAllToTag(SOUL_STORAGE_DISKS,
//            Arrays.stream(SoulStorageVariant.values())
//                .map(Items::getSoulStorageDisk)
//                .map(t -> (Supplier<Item>) () -> t)
//                .toList(), true);
    }

    private <T extends Item> void addAllToTag(final TagKey<Item> tag,
                                              final Collection<Supplier<T>> items,
                                              final boolean optional) {
        final var builder = this.tag(tag);

        for (final Supplier<T> supplier : items) {
            final Item item = supplier.get();
            if (optional) {
                builder.addOptional(item);
            } else {
                builder.add(item);
            }
        }

        builder.replace(false);
    }
}
