package com.ultramega.refinedtypes.datagen.loot;

import com.ultramega.refinedtypes.registry.Blocks;
import com.ultramega.refinedtypes.storage.energy.EnergyStorageVariant;
import com.ultramega.refinedtypes.storage.soul.SoulStorageVariant;
import com.ultramega.refinedtypes.storage.source.SourceStorageVariant;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;

public class BlockDropProvider extends BlockLootSubProvider {
    public BlockDropProvider(final HolderLookup.Provider provider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    protected void generate() {
        for (final EnergyStorageVariant variant : EnergyStorageVariant.values()) {
            this.drop(Blocks.getEnergyStorageBlock(variant));
        }
        for (final SourceStorageVariant variant : SourceStorageVariant.values()) {
            this.drop(Blocks.getSourceStorageBlock(variant));
        }
        for (final SoulStorageVariant variant : SoulStorageVariant.values()) {
            this.drop(Blocks.getSoulStorageBlock(variant));
        }
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        final List<Block> blocks = new ArrayList<>();

        for (final EnergyStorageVariant variant : EnergyStorageVariant.values()) {
            blocks.add(Blocks.getEnergyStorageBlock(variant));
        }
        for (final SourceStorageVariant variant : SourceStorageVariant.values()) {
            blocks.add(Blocks.getSourceStorageBlock(variant));
        }
        for (final SoulStorageVariant variant : SoulStorageVariant.values()) {
            blocks.add(Blocks.getSoulStorageBlock(variant));
        }

        return blocks;
    }

    private void drop(final Block block) {
        this.add(block, this.createSingleItemTable(block)
            .apply(copyName()));
    }

    private static CopyComponentsFunction.Builder copyName() {
        return CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
            .include(DataComponents.CUSTOM_NAME);
    }
}
