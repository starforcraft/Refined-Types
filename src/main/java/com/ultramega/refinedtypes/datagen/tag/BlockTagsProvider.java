package com.ultramega.refinedtypes.datagen.tag;

import com.ultramega.refinedtypes.registry.Blocks;
import com.ultramega.refinedtypes.storage.energy.EnergyStorageVariant;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import static com.ultramega.refinedtypes.RefinedTypesUtil.MOD_ID;

public class BlockTagsProvider extends IntrinsicHolderTagsProvider<Block> {
    public static final TagKey<Block> MINEABLE = TagKey.create(Registries.BLOCK, Identifier.withDefaultNamespace("mineable/pickaxe"));

    @SuppressWarnings("deprecation")
    public BlockTagsProvider(final PackOutput packOutput, final CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, Registries.BLOCK, registries, block -> block.builtInRegistryHolder().key(), MOD_ID);
    }

    @Override
    protected void addTags(final HolderLookup.Provider provider) {
        this.markAsMineable(Blocks.getNetworkEnergizer(), true);

        for (final EnergyStorageVariant variant : EnergyStorageVariant.values()) {
            this.markAsMineable(Blocks.getEnergyStorageBlock(variant), false);
        }
//        for (final SourceStorageVariant variant : SourceStorageVariant.values()) {
//            this.markAsMineable(Blocks.getSourceStorageBlock(variant), true);
//        }
//        for (final SoulStorageVariant variant : SoulStorageVariant.values()) {
//            this.markAsMineable(Blocks.getSoulStorageBlock(variant), true);
//        }
    }

    private void markAsMineable(final Block block, final boolean optional) {
        if (optional) {
            this.tag(MINEABLE).addOptional(block);
        } else {
            this.tag(MINEABLE).add(block);
        }
    }
}
