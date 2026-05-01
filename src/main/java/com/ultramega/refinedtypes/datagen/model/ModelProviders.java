package com.ultramega.refinedtypes.datagen.model;

import com.ultramega.refinedtypes.networkenergizer.NetworkEnergizerBlock;
import com.ultramega.refinedtypes.registry.Blocks;
import com.ultramega.refinedtypes.registry.Items;
import com.ultramega.refinedtypes.storage.energy.EnergyStorageVariant;

import java.util.stream.Stream;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.createIdentifier;
import static com.ultramega.refinedtypes.ModInitializer.ENERGY_ID;
import static com.ultramega.refinedtypes.RefinedTypesUtil.MOD_ID;
import static com.ultramega.refinedtypes.RefinedTypesUtil.createRefinedTypesIdentifier;
import static net.minecraft.client.data.models.BlockModelGenerators.plainVariant;

public class ModelProviders extends ModelProvider {
    private static final TextureSlot CUTOUT = TextureSlot.create("cutout");

    private static final ModelTemplate EMISSIVE_ALL_CUTOUT_MODEL = ModelTemplates.create(
        "refinedstorage:emissive_all_cutout",
        TextureSlot.PARTICLE,
        TextureSlot.ALL,
        CUTOUT
    );
    private static final ModelTemplate ALL_CUTOUT_MODEL = ModelTemplates.create(
        "refinedstorage:all_cutout",
        TextureSlot.PARTICLE,
        TextureSlot.ALL,
        CUTOUT
    );

    public ModelProviders(final PackOutput output) {
        super(output, MOD_ID);
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.of();
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return Stream.of();
    }

    @Override
    protected void registerModels(final BlockModelGenerators blockModels, final ItemModelGenerators itemModels) {
        this.registerStorageBlocks(itemModels, blockModels);
        this.registerStorageItems(itemModels);
        this.registerNetworkEnergizer(itemModels, blockModels);
    }

    private void registerStorageBlocks(final ItemModelGenerators itemModels, final BlockModelGenerators blockModels) {
        for (final EnergyStorageVariant variant : EnergyStorageVariant.values()) {
            final Identifier blockModel = ModelTemplates.CUBE_ALL.create(
                createRefinedTypesIdentifier("block/energy_storage_block/" + variant.getName() + "_energy_storage_block"),
                TextureMapping.cube(texture(
                    createRefinedTypesIdentifier("block/energy_storage_block/" + variant.getName() + "_energy_storage_block"))),
                blockModels.modelOutput
            );
            final Block block = Blocks.getEnergyStorageBlock(variant);
            blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, plainVariant(blockModel)));
            itemModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(blockModel));
        }
    }

    private void registerStorageItems(final ItemModelGenerators itemModels) {
        for (final EnergyStorageVariant variant : EnergyStorageVariant.values()) {
            this.generateStorageItems(Items.getEnergyStorageDisk(variant), variant.getStorageDiskId(), ENERGY_ID, itemModels);
            if (variant != EnergyStorageVariant.CREATIVE) {
                this.generateStorageItems(Items.getEnergyStoragePart(variant), variant.getStoragePartId(), ENERGY_ID, itemModels);
            }
        }
    }

    private void registerNetworkEnergizer(final ItemModelGenerators itemModels, final BlockModelGenerators blockModels) {
        final Identifier offModel = createRefinedTypesIdentifier("block/network_energizer_off");
        final Identifier onModel = createRefinedTypesIdentifier("block/network_energizer_on");

        final Identifier outside = createRefinedTypesIdentifier("block/network_energizer_outside");
        final Identifier cutoutOn = createRefinedTypesIdentifier("block/network_energizer");
        final Identifier cutoutOff = createIdentifier("block/controller/cutouts/off");
        final TexturedModel onTexturedModel = new TexturedModel(new TextureMapping()
            .put(TextureSlot.PARTICLE, texture(outside))
            .put(TextureSlot.ALL, texture(outside))
            .put(CUTOUT, texture(cutoutOn)), EMISSIVE_ALL_CUTOUT_MODEL);
        onTexturedModel.getTemplate().create(onModel, onTexturedModel.getMapping(), blockModels.modelOutput);
        final TexturedModel offTexturedModel = new TexturedModel(new TextureMapping()
            .put(TextureSlot.PARTICLE, texture(outside))
            .put(TextureSlot.ALL, texture(outside))
            .put(CUTOUT, texture(cutoutOff)), ALL_CUTOUT_MODEL);
        offTexturedModel.getTemplate().create(offModel, offTexturedModel.getMapping(), blockModels.modelOutput);

        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(Blocks.getNetworkEnergizer())
            .with(PropertyDispatch.initial(NetworkEnergizerBlock.ACTIVE)
                .select(false, plainVariant(offModel))
                .select(true, plainVariant(onModel))));
        itemModels.itemModelOutput.accept(Blocks.getNetworkEnergizer().asItem(), ItemModelUtils.plainModel(onModel));
    }

    private void generateStorageItems(final Item item, final Identifier variantId, final Identifier type, final ItemModelGenerators itemModels) {
        final Identifier id = createRefinedTypesIdentifier("item/" + type.getPath() + "/" + variantId.getPath());
        itemModels.itemModelOutput.accept(item, ItemModelUtils.plainModel(
            ModelTemplates.FLAT_ITEM.create(id, TextureMapping.layer0(texture(id)), itemModels.modelOutput)));
    }

    private static Material texture(final Identifier location) {
        return new Material(location);
    }
}
