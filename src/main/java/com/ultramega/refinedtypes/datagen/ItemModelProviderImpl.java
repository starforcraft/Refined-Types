package com.ultramega.refinedtypes.datagen;

import com.ultramega.refinedtypes.registry.Items;
import com.ultramega.refinedtypes.storage.energy.EnergyStorageVariant;
import com.ultramega.refinedtypes.storage.soul.SoulStorageVariant;
import com.ultramega.refinedtypes.storage.source.SourceStorageVariant;

import java.util.Objects;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import static com.ultramega.refinedtypes.RefinedTypesUtil.MOD_ID;

public class ItemModelProviderImpl extends ItemModelProvider {
    public ItemModelProviderImpl(final PackOutput output, final ExistingFileHelper existingFileHelper) {
        super(output, MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.registerEnergyStoragePartsDisks();
        this.registerSourceStoragePartsDisks();
        this.registerSoulStoragePartsDisks();
    }

    private void registerEnergyStoragePartsDisks() {
        for (final EnergyStorageVariant variant : EnergyStorageVariant.values()) {
            this.basicItem("energy", Items.getEnergyStoragePart(variant));
            this.basicItem("energy", Items.getEnergyStorageDisk(variant));
        }
    }

    private void registerSourceStoragePartsDisks() {
        for (final SourceStorageVariant variant : SourceStorageVariant.values()) {
            this.basicItem("source", Items.getSourceStoragePart(variant));
            this.basicItem("source", Items.getSourceStorageDisk(variant));
        }
    }

    private void registerSoulStoragePartsDisks() {
        for (final SoulStorageVariant variant : SoulStorageVariant.values()) {
            this.basicItem("soul", Items.getSoulStoragePart(variant));
            this.basicItem("soul", Items.getSoulStorageDisk(variant));
        }
    }

    public void basicItem(final String subFolder, final Item item) {
        final ResourceLocation itemPath = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item));
        this.getBuilder(itemPath.toString())
            .parent(new ModelFile.UncheckedModelFile("item/generated"))
            .texture("layer0", ResourceLocation.fromNamespaceAndPath(itemPath.getNamespace(), "item/" + subFolder + "/" + itemPath.getPath()));
    }
}
