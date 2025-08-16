package com.ultramega.refinedtypes.datagen.tag;

import com.ultramega.refinedtypes.registry.Blocks;
import com.ultramega.refinedtypes.storage.energy.EnergyStorageVariant;
import com.ultramega.refinedtypes.storage.soul.SoulStorageVariant;
import com.ultramega.refinedtypes.storage.source.SourceStorageVariant;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import static com.refinedmods.refinedstorage.neoforge.datagen.tag.BlockTagsProvider.MINEABLE;
import static com.ultramega.refinedtypes.RefinedTypesUtil.MOD_ID;

public class BlockTagsProviderImpl extends TagsProvider<Block> {
    public BlockTagsProviderImpl(final PackOutput packOutput,
                                 final CompletableFuture<HolderLookup.Provider> providerCompletableFuture,
                                 final ExistingFileHelper existingFileHelper) {
        super(packOutput, Registries.BLOCK, providerCompletableFuture, MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(final HolderLookup.Provider provider) {
        for (final EnergyStorageVariant variant : EnergyStorageVariant.values()) {
            this.markAsMineable(Blocks.getEnergyStorageBlock(variant));
        }
        for (final SourceStorageVariant variant : SourceStorageVariant.values()) {
            this.markAsMineable(Blocks.getSourceStorageBlock(variant));
        }
        for (final SoulStorageVariant variant : SoulStorageVariant.values()) {
            this.markAsMineable(Blocks.getSoulStorageBlock(variant));
        }
    }

    private void markAsMineable(final Block block) {
        this.tag(MINEABLE).add(ResourceKey.create(
            Registries.BLOCK,
            BuiltInRegistries.BLOCK.getKey(block)
        ));
    }
}
