package com.ultramega.refinedtypes.datagen;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class AdvancementProviderImpl extends AdvancementProvider {
    public AdvancementProviderImpl(final PackOutput output,
                                   final CompletableFuture<HolderLookup.Provider> registries,
                                   final ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(new AdvancementGeneratorImpl()));
    }

    private static final class AdvancementGeneratorImpl implements AdvancementProvider.AdvancementGenerator {
        @Override
        public void generate(final HolderLookup.Provider provider, final Consumer<AdvancementHolder> saver, final ExistingFileHelper existingFileHelper) {
            // TODO
            //  Caused by: java.util.concurrent.CompletionException: java.lang.IllegalStateException: The parent: 'refinedstorage:drives' of advancement 'refinedtypes:storing_energies', has not been saved yet!
            /*Advancement.Builder.advancement()
                .parent(createIdentifier("drives"))
                .display(Items.getEnergyStoragePart(EnergyStorageVariant.SIXTY_FOUR_K).getDefaultInstance(),
                    Component.translatable("advancements.refinedtypes.storing_energies"),
                    Component.translatable("advancements.refinedtypes.storing_energies.description"),
                    null,
                    AdvancementType.GOAL,
                    true,
                    true,
                    false)
                .addCriterion("energy_storage_disk_in_inventory", InventoryChangeTrigger.TriggerInstance.hasItems(
                    ItemPredicate.Builder.item().of(Tags.ENERGY_STORAGE_DISKS).build()
                ))
                .save(saver, createRefinedTypesIdentifier("storing_energies"), existingFileHelper);*/
        }
    }
}
