package com.ultramega.refinedtypes.datagen.advancement;

import com.ultramega.refinedtypes.registry.Items;
import com.ultramega.refinedtypes.registry.Tags;
import com.ultramega.refinedtypes.storage.energy.EnergyStorageVariant;

import java.util.function.Consumer;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.advancements.AdvancementSubProvider;

import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.createIdentifier;
import static com.ultramega.refinedtypes.RefinedTypesUtil.MOD_ID;
import static com.ultramega.refinedtypes.RefinedTypesUtil.createRefinedTypesTranslation;

public class AdvancementProvider implements AdvancementSubProvider {
    @Override
    public void generate(final Provider registries, final Consumer<AdvancementHolder> consumer) {
        final var items = registries.lookupOrThrow(Registries.ITEM);

        Advancement.Builder.advancement()
            .parent(createIdentifier("drives"))
            .display(Items.getEnergyStorageDisk(EnergyStorageVariant.K_64),
                createRefinedTypesTranslation("advancements", "storing_energies"),
                createRefinedTypesTranslation("advancements", "storing_energies.description"),
                null,
                AdvancementType.GOAL,
                true,
                true,
                false)
            .addCriterion("energy_storage_disk_in_inventory", InventoryChangeTrigger.TriggerInstance.hasItems(
                ItemPredicate.Builder.item().of(items, Tags.ENERGY_STORAGE_DISKS).build()
            ))
            .save(consumer, MOD_ID + ":storing_energies");

//        Advancement.Builder.advancement()
//            .parent(createIdentifier("drives"))
//            .display(Items.getSourceStorageDisk(SourceStorageVariant.B_64).getDefaultInstance(),
//                Component.translatable("advancements.refinedtypes.storing_sources"),
//                Component.translatable("advancements.refinedtypes.storing_sources.description"),
//                null,
//                AdvancementType.GOAL,
//                true,
//                true,
//                false)
//            .addCriterion("source_storage_disk_in_inventory", InventoryChangeTrigger.TriggerInstance.hasItems(
//                ItemPredicate.Builder.item().of(Tags.SOURCE_STORAGE_DISKS).build()
//            ))
//            .save(consumer, MOD_ID + ":storing_sources");
//
//        Advancement.Builder.advancement()
//            .parent(createIdentifier("drives"))
//            .display(Items.getSoulStorageDisk(SoulStorageVariant.K_64).getDefaultInstance(),
//                Component.translatable("advancements.refinedtypes.storing_souls"),
//                Component.translatable("advancements.refinedtypes.storing_souls.description"),
//                null,
//                AdvancementType.GOAL,
//                true,
//                true,
//                false)
//            .addCriterion("soul_storage_disk_in_inventory", InventoryChangeTrigger.TriggerInstance.hasItems(
//                ItemPredicate.Builder.item().of(Tags.SOUL_STORAGE_DISKS).build()
//            ))
//            .save(consumer, MOD_ID + ":storing_souls");
    }
}
