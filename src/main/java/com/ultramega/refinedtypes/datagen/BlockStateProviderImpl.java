package com.ultramega.refinedtypes.datagen;

import com.ultramega.refinedtypes.registry.Blocks;
import com.ultramega.refinedtypes.storage.energy.EnergyStorageVariant;
import com.ultramega.refinedtypes.storage.soul.SoulStorageVariant;
import com.ultramega.refinedtypes.storage.source.SourceStorageVariant;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import static com.ultramega.refinedtypes.RefinedTypesUtil.MOD_ID;
import static com.ultramega.refinedtypes.RefinedTypesUtil.createRefinedTypesIdentifier;

public class BlockStateProviderImpl extends BlockStateProvider {
    private static final String BLOCK_PREFIX = "block";

    private final ExistingFileHelper existingFileHelper;

    public BlockStateProviderImpl(final PackOutput output, final ExistingFileHelper existingFileHelper) {
        super(output, MOD_ID, existingFileHelper);
        this.existingFileHelper = existingFileHelper;
    }

    @Override
    protected void registerStatesAndModels() {
        this.registerEnergyStorageBlocks();
        this.registerSourceStorageBlocks();
        this.registerSoulStorageBlocks();
    }

    private void registerEnergyStorageBlocks() {
        for (final EnergyStorageVariant variant : EnergyStorageVariant.values()) {
            final String name = variant.getName() + "_energy_storage_block";
            this.simpleBlockWithItem(Blocks.getEnergyStorageBlock(variant), this.modelFile(createRefinedTypesIdentifier(BLOCK_PREFIX + "/" + name)));
        }
    }

    private void registerSourceStorageBlocks() {
        for (final SourceStorageVariant variant : SourceStorageVariant.values()) {
            final String name = variant.getName() + "_source_storage_block";
            this.simpleBlockWithItem(Blocks.getSourceStorageBlock(variant), this.modelFile(createRefinedTypesIdentifier(BLOCK_PREFIX + "/" + name)));
        }
    }

    private void registerSoulStorageBlocks() {
        for (final SoulStorageVariant variant : SoulStorageVariant.values()) {
            final String name = variant.getName() + "_soul_storage_block";
            this.simpleBlockWithItem(Blocks.getSoulStorageBlock(variant), this.modelFile(createRefinedTypesIdentifier(BLOCK_PREFIX + "/" + name)));
        }
    }

    private ModelFile modelFile(final ResourceLocation location) {
        return new ModelFile.ExistingModelFile(location, this.existingFileHelper);
    }
}
