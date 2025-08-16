package com.ultramega.refinedtypes.datagen.recipe;

import com.ultramega.refinedtypes.registry.Items;
import com.ultramega.refinedtypes.storage.energy.EnergyStorageVariant;
import com.ultramega.refinedtypes.storage.soul.SoulStorageVariant;
import com.ultramega.refinedtypes.storage.source.SourceStorageVariant;

import com.refinedmods.refinedstorage.common.content.Blocks;
import com.refinedmods.refinedstorage.common.misc.ProcessorItem;

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
        // The following lines aren't my proudest bit of code

        record Upgrade(Item from, Item to, Item processor, Item upgradeBlock) { }

        final RecipeOutput arsOutput = output.withConditions(new ModLoadedCondition("ars_nouveau"));
        final RecipeOutput soulsOutput = output.withConditions(new ModLoadedCondition("industrialforegoingsouls"));

        for (final EnergyStorageVariant variant : EnergyStorageVariant.values()) {
            this.recipeStorageDisk(variant.getStoragePart(), Items.getEnergyStorageDisk(variant), output);
            this.recipeStorageBlock(variant.getStoragePart(), Items.getEnergyStorageBlock(variant), output);
            this.recipeDiskFromStorageHousing(variant.getStoragePart(), Items.getEnergyStorageDisk(variant), output);
        }
        for (final SourceStorageVariant variant : SourceStorageVariant.values()) {
            this.recipeStorageDisk(variant.getStoragePart(), Items.getSourceStorageDisk(variant), arsOutput);
            this.recipeStorageBlock(variant.getStoragePart(), Items.getSourceStorageBlock(variant), arsOutput);
            this.recipeDiskFromStorageHousing(variant.getStoragePart(), Items.getSourceStorageDisk(variant), arsOutput);
        }
        for (final SoulStorageVariant variant : SoulStorageVariant.values()) {
            this.recipeStorageDisk(variant.getStoragePart(), Items.getSoulStorageDisk(variant), soulsOutput);
            this.recipeStorageBlock(variant.getStoragePart(), Items.getSoulStorageBlock(variant), soulsOutput);
            this.recipeDiskFromStorageHousing(variant.getStoragePart(), Items.getSoulStorageDisk(variant), soulsOutput);
        }
        this.registerFirstPartRecipe(net.minecraft.world.item.Items.REDSTONE_BLOCK, EnergyStorageVariant.K_64.getStoragePart(), output);
        this.registerFirstPartRecipe(ItemsRegistry.MAGE_FIBER.get(), SourceStorageVariant.B_64.getStoragePart(), arsOutput);
        this.registerFirstPartRecipe(net.minecraft.world.item.Items.SCULK, SoulStorageVariant.K_1.getStoragePart(), soulsOutput);

        final var items = com.refinedmods.refinedstorage.common.content.Items.INSTANCE;
        final List<Upgrade> energyUpgrades = List.of(
            new Upgrade(EnergyStorageVariant.K_64.getStoragePart(),      EnergyStorageVariant.K_256.getStoragePart(),     items.getProcessor(ProcessorItem.Type.BASIC),    net.minecraft.world.item.Items.GOLD_BLOCK),
            new Upgrade(EnergyStorageVariant.K_256.getStoragePart(),     EnergyStorageVariant.K_1024.getStoragePart(),    items.getProcessor(ProcessorItem.Type.IMPROVED), net.minecraft.world.item.Items.GOLD_BLOCK),
            new Upgrade(EnergyStorageVariant.K_1024.getStoragePart(),    EnergyStorageVariant.K_8192.getStoragePart(),    items.getProcessor(ProcessorItem.Type.ADVANCED), net.minecraft.world.item.Items.DIAMOND_BLOCK),
            new Upgrade(EnergyStorageVariant.K_8192.getStoragePart(),    EnergyStorageVariant.K_65536.getStoragePart(),   items.getProcessor(ProcessorItem.Type.ADVANCED), net.minecraft.world.item.Items.DIAMOND_BLOCK),
            new Upgrade(EnergyStorageVariant.K_65536.getStoragePart(),   EnergyStorageVariant.K_262144.getStoragePart(),  items.getProcessor(ProcessorItem.Type.ADVANCED), net.minecraft.world.item.Items.EMERALD_BLOCK),
            new Upgrade(EnergyStorageVariant.K_262144.getStoragePart(),  EnergyStorageVariant.K_1048576.getStoragePart(), items.getProcessor(ProcessorItem.Type.ADVANCED), net.minecraft.world.item.Items.EMERALD_BLOCK),
            new Upgrade(EnergyStorageVariant.K_1048576.getStoragePart(), EnergyStorageVariant.K_8388608.getStoragePart(), items.getProcessor(ProcessorItem.Type.ADVANCED), net.minecraft.world.item.Items.NETHERITE_BLOCK),
            new Upgrade(EnergyStorageVariant.K_8388608.getStoragePart(), EnergyStorageVariant.INFINITE.getStoragePart(),  items.getProcessor(ProcessorItem.Type.ADVANCED), net.minecraft.world.item.Items.NETHERITE_BLOCK)
        );
        final List<Upgrade> sourceUgrades = List.of(
            new Upgrade(SourceStorageVariant.B_64.getStoragePart(),      SourceStorageVariant.B_256.getStoragePart(),     items.getProcessor(ProcessorItem.Type.BASIC),    net.minecraft.world.item.Items.GOLD_BLOCK),
            new Upgrade(SourceStorageVariant.B_256.getStoragePart(),     SourceStorageVariant.B_1024.getStoragePart(),    items.getProcessor(ProcessorItem.Type.IMPROVED), net.minecraft.world.item.Items.GOLD_BLOCK),
            new Upgrade(SourceStorageVariant.B_1024.getStoragePart(),    SourceStorageVariant.B_8192.getStoragePart(),    items.getProcessor(ProcessorItem.Type.ADVANCED), net.minecraft.world.item.Items.DIAMOND_BLOCK),
            new Upgrade(SourceStorageVariant.B_8192.getStoragePart(),    SourceStorageVariant.B_65536.getStoragePart(),   items.getProcessor(ProcessorItem.Type.ADVANCED), net.minecraft.world.item.Items.DIAMOND_BLOCK),
            new Upgrade(SourceStorageVariant.B_65536.getStoragePart(),   SourceStorageVariant.B_262144.getStoragePart(),  items.getProcessor(ProcessorItem.Type.ADVANCED), net.minecraft.world.item.Items.EMERALD_BLOCK),
            new Upgrade(SourceStorageVariant.B_262144.getStoragePart(),  SourceStorageVariant.B_1048576.getStoragePart(), items.getProcessor(ProcessorItem.Type.ADVANCED), net.minecraft.world.item.Items.EMERALD_BLOCK),
            new Upgrade(SourceStorageVariant.B_1048576.getStoragePart(), SourceStorageVariant.B_8388608.getStoragePart(), items.getProcessor(ProcessorItem.Type.ADVANCED), net.minecraft.world.item.Items.NETHERITE_BLOCK),
            new Upgrade(SourceStorageVariant.B_8388608.getStoragePart(), SourceStorageVariant.INFINITE.getStoragePart(),  items.getProcessor(ProcessorItem.Type.ADVANCED), net.minecraft.world.item.Items.NETHERITE_BLOCK)
        );
        final List<Upgrade> soulUpgrades = List.of(
            new Upgrade(SoulStorageVariant.K_1.getStoragePart(),       SoulStorageVariant.K_8.getStoragePart(),       items.getProcessor(ProcessorItem.Type.BASIC),    net.minecraft.world.item.Items.GOLD_BLOCK),
            new Upgrade(SoulStorageVariant.K_8.getStoragePart(),       SoulStorageVariant.K_64.getStoragePart(),      items.getProcessor(ProcessorItem.Type.IMPROVED), net.minecraft.world.item.Items.GOLD_BLOCK),
            new Upgrade(SoulStorageVariant.K_64.getStoragePart(),      SoulStorageVariant.K_512.getStoragePart(),     items.getProcessor(ProcessorItem.Type.ADVANCED), net.minecraft.world.item.Items.DIAMOND_BLOCK),
            new Upgrade(SoulStorageVariant.K_512.getStoragePart(),     SoulStorageVariant.K_4096.getStoragePart(),    items.getProcessor(ProcessorItem.Type.ADVANCED), net.minecraft.world.item.Items.DIAMOND_BLOCK),
            new Upgrade(SoulStorageVariant.K_4096.getStoragePart(),    SoulStorageVariant.K_32768.getStoragePart(),   items.getProcessor(ProcessorItem.Type.ADVANCED), net.minecraft.world.item.Items.EMERALD_BLOCK),
            new Upgrade(SoulStorageVariant.K_32768.getStoragePart(),   SoulStorageVariant.K_262144.getStoragePart(),  items.getProcessor(ProcessorItem.Type.ADVANCED), net.minecraft.world.item.Items.EMERALD_BLOCK),
            new Upgrade(SoulStorageVariant.K_262144.getStoragePart(),  SoulStorageVariant.K_2097152.getStoragePart(), items.getProcessor(ProcessorItem.Type.ADVANCED), net.minecraft.world.item.Items.NETHERITE_BLOCK),
            new Upgrade(SoulStorageVariant.K_2097152.getStoragePart(), SoulStorageVariant.INFINITE.getStoragePart(),  items.getProcessor(ProcessorItem.Type.ADVANCED), net.minecraft.world.item.Items.NETHERITE_BLOCK)
        );

        energyUpgrades.forEach(up -> this.registerUpgradePartRecipe(up.from(), up.to(), up.processor(), up.upgradeBlock(), output));
        sourceUgrades.forEach(up -> this.registerUpgradePartRecipe(up.from(), up.to(), up.processor(), up.upgradeBlock(), arsOutput));
        soulUpgrades.forEach(up -> this.registerUpgradePartRecipe(up.from(), up.to(), up.processor(), up.upgradeBlock(), soulsOutput));
    }

    private void recipeStorageDisk(final Item storagePart, final Item result, final RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result)
            .pattern("GRG")
            .pattern("RPR")
            .pattern("EEE")
            .define('G', Tags.Items.GLASS_BLOCKS)
            .define('R', Tags.Items.DUSTS_REDSTONE)
            .define('P', storagePart)
            .define('E', net.minecraft.world.item.Items.REDSTONE_BLOCK)
            .unlockedBy("has_storage_part", has(storagePart))
            .save(output, createRefinedTypesIdentifier("disk/" + BuiltInRegistries.ITEM.getKey(result).getPath()));
    }

    private void recipeStorageBlock(final Item storagePart, final Item result, final RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result)
            .pattern("EPE")
            .pattern("EME")
            .pattern("ERE")
            .define('M', Blocks.INSTANCE.getMachineCasing())
            .define('R', Tags.Items.DUSTS_REDSTONE)
            .define('P', storagePart)
            .define('E', net.minecraft.world.item.Items.REDSTONE_BLOCK)
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
            .unlockedBy("has_redstone_block", has(craftBlock))
            .save(output, createRefinedTypesIdentifier("part/" + BuiltInRegistries.ITEM.getKey(result).getPath()));
    }

    private void registerUpgradePartRecipe(final Item prevStoragePart,
                                           final Item result,
                                           final Item processor,
                                           final Item upgradeBlock,
                                           final RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result)
            .pattern("PEP")
            .pattern("SRS")
            .pattern("PSP")
            .define('P', processor)
            .define('E', net.minecraft.world.item.Items.REDSTONE_BLOCK)
            .define('S', prevStoragePart)
            .define('R', upgradeBlock)
            .unlockedBy("has_prev_part", has(prevStoragePart))
            .save(output, createRefinedTypesIdentifier("part/" + BuiltInRegistries.ITEM.getKey(result).getPath()));
    }

    private void recipeDiskFromStorageHousing(final Item storagePart, final Item result, final RecipeOutput output) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result)
            .requires(com.refinedmods.refinedstorage.common.content.Items.INSTANCE.getStorageHousing())
            .requires(storagePart)
            .unlockedBy("has_storage_part", has(storagePart))
            .save(output, createRefinedTypesIdentifier("part/" + BuiltInRegistries.ITEM.getKey(result).getPath()));
    }
}
