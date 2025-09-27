package com.ultramega.refinedtypes.datagen.recipe;

import com.ultramega.refinedtypes.storage.energy.EnergyStorageVariant;
import com.ultramega.refinedtypes.storage.soul.SoulStorageVariant;
import com.ultramega.refinedtypes.storage.source.SourceStorageVariant;

import com.refinedmods.refinedstorage.common.content.Blocks;
import com.refinedmods.refinedstorage.common.misc.ProcessorItem;
import com.refinedmods.refinedstorage.common.storage.StorageVariant;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;

import static com.ultramega.refinedtypes.RefinedTypesUtil.createRefinedTypesIdentifier;

public class RecipeProviderImpl extends RecipeProvider {
    public RecipeProviderImpl(final PackOutput output,
                              final CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(final RecipeOutput output) {
        record Upgrade(StorageVariant from, StorageVariant to, Item processor, Item upgradeBlock) { }

        final RecipeOutput arsOutput = output.withConditions(new ModLoadedCondition("ars_nouveau"));
        final RecipeOutput soulsOutput = output.withConditions(new ModLoadedCondition("industrialforegoingsouls"));

        for (final EnergyStorageVariant variant : EnergyStorageVariant.values()) {
            this.recipeStorageDisk(variant.getStoragePart(), com.ultramega.refinedtypes.registry.Items.getEnergyStorageDisk(variant), output);
            this.recipeStorageBlock(variant.getStoragePart(), com.ultramega.refinedtypes.registry.Items.getEnergyStorageBlock(variant), Items.COPPER_BLOCK, output);
            this.recipeDiskFromStorageHousing(variant.getStoragePart(), com.ultramega.refinedtypes.registry.Items.getEnergyStorageDisk(variant), output);
        }
        for (final SourceStorageVariant variant : SourceStorageVariant.values()) {
            this.recipeStorageDisk(variant.getStoragePart(), com.ultramega.refinedtypes.registry.Items.getSourceStorageDisk(variant), arsOutput);
            this.recipeStorageBlock(variant.getStoragePart(), com.ultramega.refinedtypes.registry.Items.getSourceStorageBlock(variant), Items.REDSTONE_BLOCK, arsOutput);
            this.recipeDiskFromStorageHousing(variant.getStoragePart(), com.ultramega.refinedtypes.registry.Items.getSourceStorageDisk(variant), arsOutput);
        }
        for (final SoulStorageVariant variant : SoulStorageVariant.values()) {
            this.recipeStorageDisk(variant.getStoragePart(), com.ultramega.refinedtypes.registry.Items.getSoulStorageDisk(variant), soulsOutput);
            this.recipeStorageBlock(variant.getStoragePart(), com.ultramega.refinedtypes.registry.Items.getSoulStorageBlock(variant), Items.REDSTONE_BLOCK, soulsOutput);
            this.recipeDiskFromStorageHousing(variant.getStoragePart(), com.ultramega.refinedtypes.registry.Items.getSoulStorageDisk(variant), soulsOutput);
        }
        this.registerFirstPartRecipe(Items.COPPER_BLOCK, EnergyStorageVariant.K_64.getStoragePart(), output);
        this.registerFirstPartRecipe(ItemsRegistry.MAGE_FIBER.get(), SourceStorageVariant.B_64.getStoragePart(), arsOutput);
        this.registerFirstPartRecipe(Items.SCULK, SoulStorageVariant.K_1.getStoragePart(), soulsOutput);

        final var items = com.refinedmods.refinedstorage.common.content.Items.INSTANCE;
        final List<Upgrade> energyUpgrades = List.of(
            new Upgrade(EnergyStorageVariant.K_64, EnergyStorageVariant.K_256, items.getProcessor(ProcessorItem.Type.BASIC), Items.GOLD_BLOCK),
            new Upgrade(EnergyStorageVariant.K_256, EnergyStorageVariant.K_1024, items.getProcessor(ProcessorItem.Type.IMPROVED), Items.GOLD_BLOCK),
            new Upgrade(EnergyStorageVariant.K_1024, EnergyStorageVariant.K_8192, items.getProcessor(ProcessorItem.Type.ADVANCED), Items.DIAMOND_BLOCK),
            new Upgrade(EnergyStorageVariant.K_8192, EnergyStorageVariant.K_65536, items.getProcessor(ProcessorItem.Type.ADVANCED), Items.DIAMOND_BLOCK),
            new Upgrade(EnergyStorageVariant.K_65536, EnergyStorageVariant.K_262144, items.getProcessor(ProcessorItem.Type.ADVANCED), Items.EMERALD_BLOCK),
            new Upgrade(EnergyStorageVariant.K_262144, EnergyStorageVariant.K_1048576, items.getProcessor(ProcessorItem.Type.ADVANCED), Items.EMERALD_BLOCK),
            new Upgrade(EnergyStorageVariant.K_1048576, EnergyStorageVariant.K_8388608, items.getProcessor(ProcessorItem.Type.ADVANCED), Items.NETHERITE_BLOCK),
            new Upgrade(EnergyStorageVariant.K_8388608, EnergyStorageVariant.INFINITE, items.getProcessor(ProcessorItem.Type.ADVANCED), Items.NETHERITE_BLOCK)
        );
        final List<Upgrade> sourceUgrades = List.of(
            new Upgrade(SourceStorageVariant.B_64, SourceStorageVariant.B_256, items.getProcessor(ProcessorItem.Type.BASIC), Items.GOLD_BLOCK),
            new Upgrade(SourceStorageVariant.B_256, SourceStorageVariant.B_1024, items.getProcessor(ProcessorItem.Type.IMPROVED), Items.GOLD_BLOCK),
            new Upgrade(SourceStorageVariant.B_1024, SourceStorageVariant.B_8192, items.getProcessor(ProcessorItem.Type.ADVANCED), Items.DIAMOND_BLOCK),
            new Upgrade(SourceStorageVariant.B_8192, SourceStorageVariant.B_65536, items.getProcessor(ProcessorItem.Type.ADVANCED), Items.DIAMOND_BLOCK),
            new Upgrade(SourceStorageVariant.B_65536, SourceStorageVariant.B_262144, items.getProcessor(ProcessorItem.Type.ADVANCED), Items.EMERALD_BLOCK),
            new Upgrade(SourceStorageVariant.B_262144, SourceStorageVariant.B_1048576, items.getProcessor(ProcessorItem.Type.ADVANCED), Items.EMERALD_BLOCK),
            new Upgrade(SourceStorageVariant.B_1048576, SourceStorageVariant.B_8388608, items.getProcessor(ProcessorItem.Type.ADVANCED), Items.NETHERITE_BLOCK),
            new Upgrade(SourceStorageVariant.B_8388608, SourceStorageVariant.INFINITE, items.getProcessor(ProcessorItem.Type.ADVANCED), Items.NETHERITE_BLOCK)
        );
        final List<Upgrade> soulUpgrades = List.of(
            new Upgrade(SoulStorageVariant.K_1, SoulStorageVariant.K_8, items.getProcessor(ProcessorItem.Type.BASIC), Items.GOLD_BLOCK),
            new Upgrade(SoulStorageVariant.K_8, SoulStorageVariant.K_64, items.getProcessor(ProcessorItem.Type.IMPROVED), Items.GOLD_BLOCK),
            new Upgrade(SoulStorageVariant.K_64, SoulStorageVariant.K_512, items.getProcessor(ProcessorItem.Type.ADVANCED), Items.DIAMOND_BLOCK),
            new Upgrade(SoulStorageVariant.K_512, SoulStorageVariant.K_4096, items.getProcessor(ProcessorItem.Type.ADVANCED), Items.DIAMOND_BLOCK),
            new Upgrade(SoulStorageVariant.K_4096, SoulStorageVariant.K_32768, items.getProcessor(ProcessorItem.Type.ADVANCED), Items.EMERALD_BLOCK),
            new Upgrade(SoulStorageVariant.K_32768, SoulStorageVariant.K_262144, items.getProcessor(ProcessorItem.Type.ADVANCED), Items.EMERALD_BLOCK),
            new Upgrade(SoulStorageVariant.K_262144, SoulStorageVariant.K_2097152, items.getProcessor(ProcessorItem.Type.ADVANCED), Items.NETHERITE_BLOCK),
            new Upgrade(SoulStorageVariant.K_2097152, SoulStorageVariant.INFINITE, items.getProcessor(ProcessorItem.Type.ADVANCED), Items.NETHERITE_BLOCK)
        );

        energyUpgrades.forEach(up ->
            this.registerUpgradePartRecipe(up.from(), up.to(), up.processor(), Items.COPPER_BLOCK, up.upgradeBlock(), output));
        sourceUgrades.forEach(up ->
            this.registerUpgradePartRecipe(up.from(), up.to(), up.processor(), Items.REDSTONE_BLOCK, up.upgradeBlock(), arsOutput));
        soulUpgrades.forEach(up ->
            this.registerUpgradePartRecipe(up.from(), up.to(), up.processor(), Items.REDSTONE_BLOCK, up.upgradeBlock(), soulsOutput));
    }

    private void recipeStorageDisk(final Item storagePart, final Item result, final RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result)
            .pattern("GRG")
            .pattern("RPR")
            .pattern("EEE")
            .define('G', Tags.Items.GLASS_BLOCKS)
            .define('R', Tags.Items.DUSTS_REDSTONE)
            .define('P', storagePart)
            .define('E', Items.REDSTONE_BLOCK)
            .unlockedBy("has_storage_part", has(storagePart))
            .save(output, createRefinedTypesIdentifier("disk/" + BuiltInRegistries.ITEM.getKey(result).getPath()));
    }

    private void recipeStorageBlock(final Item storagePart, final Item result, final Item resourceBlock, final RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result)
            .pattern("EPE")
            .pattern("EME")
            .pattern("ERE")
            .define('M', Blocks.INSTANCE.getMachineCasing())
            .define('R', Tags.Items.DUSTS_REDSTONE)
            .define('P', storagePart)
            .define('E', resourceBlock)
            .unlockedBy("has_storage_part", has(storagePart))
            .save(output, createRefinedTypesIdentifier("blocks/" + BuiltInRegistries.ITEM.getKey(result).getPath()));
    }

    private void registerFirstPartRecipe(final Item craftBlock, final Item result, final RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result)
            .pattern("SES")
            .pattern("GRG")
            .pattern("SGS")
            .define('S', com.ultramega.refinedtypes.registry.Tags.SILICON)
            .define('E', craftBlock)
            .define('G', Tags.Items.GLASS_BLOCKS)
            .define('R', Tags.Items.DUSTS_REDSTONE)
            .unlockedBy("has_craft_block", has(craftBlock))
            .save(output, createRefinedTypesIdentifier("part/" + BuiltInRegistries.ITEM.getKey(result).getPath()));
    }

    private void registerUpgradePartRecipe(final StorageVariant prevPart,
                                           final StorageVariant resultPart,
                                           final Item processor,
                                           final Item resourceBlock,
                                           final Item upgradeBlock,
                                           final RecipeOutput output) {
        final Item prevPartItem = prevPart.getStoragePart();
        final Item resultPartItem = resultPart.getStoragePart();
        if (prevPartItem == null || resultPartItem == null) {
            return;
        }

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, resultPartItem)
            .pattern("PEP")
            .pattern("SRS")
            .pattern("PSP")
            .define('P', processor)
            .define('E', resourceBlock)
            .define('S', prevPartItem)
            .define('R', upgradeBlock)
            .unlockedBy("has_prev_part", has(prevPartItem))
            .save(output, createRefinedTypesIdentifier("part/" + BuiltInRegistries.ITEM.getKey(resultPartItem).getPath()));
    }

    private void recipeDiskFromStorageHousing(final Item storagePart, final Item result, final RecipeOutput output) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result)
            .requires(com.refinedmods.refinedstorage.common.content.Items.INSTANCE.getStorageHousing())
            .requires(storagePart)
            .unlockedBy("has_storage_part", has(storagePart))
            .save(output, createRefinedTypesIdentifier("part/" + BuiltInRegistries.ITEM.getKey(result).getPath()));
    }
}
