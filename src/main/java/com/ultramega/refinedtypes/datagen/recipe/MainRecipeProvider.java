package com.ultramega.refinedtypes.datagen.recipe;

import com.ultramega.refinedtypes.registry.Blocks;
import com.ultramega.refinedtypes.registry.Items;
import com.ultramega.refinedtypes.storage.energy.EnergyStorageVariant;

import com.refinedmods.refinedstorage.common.misc.ProcessorItem;
import com.refinedmods.refinedstorage.common.storage.StorageContainerUpgradeRecipe;
import com.refinedmods.refinedstorage.common.storage.StorageVariant;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;

import static com.ultramega.refinedtypes.RefinedTypesUtil.createRefinedTypesIdentifier;

public class MainRecipeProvider extends RecipeProvider {
    private static final TagKey<Item> SILICON = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "silicon"));

    public MainRecipeProvider(final HolderLookup.Provider registries, final RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        this.storageStuff();
        this.storageUpgrades();
        this.networkEnergizer();
    }

    private void storageStuff() {
        record Upgrade(StorageVariant from, StorageVariant to, ProcessorItem.Type processor, Item upgradeBlock) { }

        for (final EnergyStorageVariant variant : EnergyStorageVariant.values()) {
            if (variant == EnergyStorageVariant.CREATIVE) {
                continue;
            }
            this.recipeStorageDisk(variant.getStoragePart(), Items.getEnergyStorageDisk(variant));
            this.recipeStorageBlock(variant.getStoragePart(), Items.getEnergyStorageBlock(variant), net.minecraft.world.item.Items.COPPER_BLOCK);
            this.recipeDiskFromStorageHousing(variant.getStoragePart(), Items.getEnergyStorageDisk(variant));
        }

        this.registerFirstPartRecipe(net.minecraft.world.item.Items.COPPER_BLOCK, EnergyStorageVariant.K_64.getStoragePart());
        final List<Upgrade> energyUpgrades = List.of(
            new Upgrade(EnergyStorageVariant.K_64, EnergyStorageVariant.K_256, ProcessorItem.Type.BASIC, net.minecraft.world.item.Items.GOLD_BLOCK),
            new Upgrade(EnergyStorageVariant.K_256, EnergyStorageVariant.K_1024, ProcessorItem.Type.IMPROVED, net.minecraft.world.item.Items.GOLD_BLOCK),
            new Upgrade(EnergyStorageVariant.K_1024, EnergyStorageVariant.K_8192, ProcessorItem.Type.ADVANCED, net.minecraft.world.item.Items.DIAMOND_BLOCK),
            new Upgrade(EnergyStorageVariant.K_8192, EnergyStorageVariant.K_65536, ProcessorItem.Type.ADVANCED, net.minecraft.world.item.Items.DIAMOND_BLOCK),
            new Upgrade(EnergyStorageVariant.K_65536, EnergyStorageVariant.K_262144, ProcessorItem.Type.ADVANCED, net.minecraft.world.item.Items.EMERALD_BLOCK),
            new Upgrade(EnergyStorageVariant.K_262144, EnergyStorageVariant.K_1048576, ProcessorItem.Type.ADVANCED, net.minecraft.world.item.Items.EMERALD_BLOCK),
            new Upgrade(EnergyStorageVariant.K_1048576, EnergyStorageVariant.K_8388608, ProcessorItem.Type.ADVANCED, net.minecraft.world.item.Items.NETHERITE_BLOCK)
        );
        energyUpgrades.forEach(up ->
            this.registerUpgradePartRecipe(up.from(), up.to(), com.refinedmods.refinedstorage.common.content.Items.INSTANCE.getProcessor(up.processor()),
                net.minecraft.world.item.Items.COPPER_BLOCK, up.upgradeBlock()));
    }

    private void storageUpgrades() {
        this.storageUpgrades(EnergyStorageVariant.values(), Items::getEnergyStoragePart,
            Items::getEnergyStorageDisk, "energy_storage_disk_upgrade");
        this.storageUpgrades(EnergyStorageVariant.values(), Items::getEnergyStoragePart,
            Blocks::getEnergyStorageBlock, "energy_storage_block_upgrade");
    }

    private void networkEnergizer() {
        final Block interfaceBlock = com.refinedmods.refinedstorage.common.content.Blocks.INSTANCE.getInterface();
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, com.ultramega.refinedtypes.registry.Blocks.getNetworkEnergizer())
            .pattern("CCC")
            .pattern("CIC")
            .pattern("CCC")
            .define('C', net.minecraft.world.item.Items.COPPER_BLOCK)
            .define('I', interfaceBlock)
            .unlockedBy("has_interface", this.has(interfaceBlock))
            .save(this.output);
    }

    private void recipeStorageDisk(final Item storagePart, final Item result) {
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, result)
            .pattern("GRG")
            .pattern("RPR")
            .pattern("EEE")
            .define('G', Tags.Items.GLASS_BLOCKS)
            .define('R', Tags.Items.DUSTS_REDSTONE)
            .define('P', storagePart)
            .define('E', net.minecraft.world.item.Items.REDSTONE_BLOCK)
            .unlockedBy("has_storage_part", this.has(storagePart))
            .save(this.output);
    }

    private void recipeStorageBlock(final Item storagePart, final Item result, final Item resourceBlock) {
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, result)
            .pattern("EPE")
            .pattern("EME")
            .pattern("ERE")
            .define('M', com.refinedmods.refinedstorage.common.content.Blocks.INSTANCE.getMachineCasing())
            .define('R', Tags.Items.DUSTS_REDSTONE)
            .define('P', storagePart)
            .define('E', resourceBlock)
            .unlockedBy("has_storage_part", this.has(storagePart))
            .save(this.output);
    }

    private void recipeDiskFromStorageHousing(final Item storagePart, final Item result) {
        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.MISC, result)
            .requires(com.refinedmods.refinedstorage.common.content.Items.INSTANCE.getStorageHousing())
            .requires(storagePart)
            .unlockedBy("has_storage_part", this.has(storagePart))
            .save(this.output, BuiltInRegistries.ITEM.getKey(result).withSuffix("_from_storage_housing").toString());
    }

    private void registerFirstPartRecipe(final Item craftBlock, final Item result) {
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, result)
            .pattern("SES")
            .pattern("GRG")
            .pattern("SGS")
            .define('S', SILICON)
            .define('E', craftBlock)
            .define('G', Tags.Items.GLASS_BLOCKS)
            .define('R', Tags.Items.DUSTS_REDSTONE)
            .unlockedBy("has_craft_block", this.has(craftBlock))
            .save(this.output);
    }

    private void registerUpgradePartRecipe(final StorageVariant prevPart,
                                           final StorageVariant resultPart,
                                           final Item processor,
                                           final Item resourceBlock,
                                           final Item upgradeBlock) {
        final Item prevPartItem = prevPart.getStoragePart();
        final Item resultPartItem = resultPart.getStoragePart();
        if (prevPartItem == null || resultPartItem == null) {
            return;
        }

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, resultPartItem)
            .pattern("PEP")
            .pattern("SRS")
            .pattern("PSP")
            .define('P', processor)
            .define('E', resourceBlock)
            .define('S', prevPartItem)
            .define('R', upgradeBlock)
            .unlockedBy("has_prev_part", this.has(prevPartItem))
            .save(this.output);
    }

    /**
     * Exact copy of {@link com.refinedmods.refinedstorage.neoforge.datagen.recipe.MainRecipeProvider#storageUpgrades(StorageVariant[], Function, Function, String)}
     */
    @SuppressWarnings("deprecation")
    private <T extends StorageVariant> void storageUpgrades(final T[] variants,
                                                            final Function<T, ItemLike> partProvider,
                                                            final Function<T, ItemLike> containerProvider,
                                                            final String name) {
        for (final T variant : variants) {
            if (variant.getCapacity() == null) {
                continue;
            }
            final List<T> lowerVariants = Arrays.stream(variants)
                .filter(otherVariant -> otherVariant.getCapacity() != null)
                .filter(otherVariant -> otherVariant.getCapacity() < variant.getCapacity())
                .toList();
            if (lowerVariants.isEmpty()) {
                continue;
            }
            final ItemLike part = partProvider.apply(variant);
            final Identifier recipeId = createRefinedTypesIdentifier(variant.getName() + "_" + name);
            this.output.accept(ResourceKey.create(Registries.RECIPE, recipeId), new StorageContainerUpgradeRecipe(
                lowerVariants.stream()
                    .map(containerProvider)
                    .map(ItemLike::asItem)
                    .map(Item::builtInRegistryHolder)
                    .map(holder -> this.registries.holderOrThrow(holder.key()))
                    .toList(),
                this.registries.holderOrThrow(part.asItem().builtInRegistryHolder().key()),
                this.registries.holderOrThrow(containerProvider.apply(variant).asItem().builtInRegistryHolder().key())
            ), null);
        }
    }

    public static final class Runner extends RecipeProvider.Runner {
        public Runner(final PackOutput packOutput, final CompletableFuture<Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(final HolderLookup.Provider registries,
                                                      final RecipeOutput output) {
            return new MainRecipeProvider(registries, output);
        }

        @Override
        public String getName() {
            return "Refined Types recipes";
        }
    }
}
