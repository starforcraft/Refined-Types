package com.ultramega.refinedtypes.datagen;

import com.ultramega.refinedtypes.storage.energy.EnergyStorageVariant;
import com.ultramega.refinedtypes.storage.soul.SoulStorageVariant;
import com.ultramega.refinedtypes.storage.source.SourceStorageVariant;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import static com.ultramega.refinedtypes.RefinedTypesUtil.MOD_ID;
import static com.ultramega.refinedtypes.RefinedTypesUtil.createRefinedTypesIdentifier;

public class BlockModelProviderImpl extends BlockModelProvider {
    private static final String BLOCK_PREFIX = "block";

    public BlockModelProviderImpl(final PackOutput output, final ExistingFileHelper existingFileHelper) {
        super(output, MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.registerEnergyStorageBlock();
        this.registerSourceStorageBlock();
        this.registerSoulStorageBlock();
    }

    private void registerEnergyStorageBlock() {
        for (final EnergyStorageVariant variant : EnergyStorageVariant.values()) {
            final String name = variant.getName() + "_energy_storage_block";
            this.cubeAll(name, createRefinedTypesIdentifier(BLOCK_PREFIX + "/energy_storage_block/" + name));
        }
    }

    private void registerSourceStorageBlock() {
        for (final SourceStorageVariant variant : SourceStorageVariant.values()) {
            final String name = variant.getName() + "_source_storage_block";
            this.cubeAll(name, createRefinedTypesIdentifier(BLOCK_PREFIX + "/source_storage_block/" + name));
        }
    }

    private void registerSoulStorageBlock() {
        for (final SoulStorageVariant variant : SoulStorageVariant.values()) {
            final String name = variant.getName() + "_soul_storage_block";
            this.cubeAll(name, createRefinedTypesIdentifier(BLOCK_PREFIX + "/soul_storage_block/" + name));
        }
    }
}
