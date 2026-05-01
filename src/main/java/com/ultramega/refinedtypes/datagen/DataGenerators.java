package com.ultramega.refinedtypes.datagen;

import com.ultramega.refinedtypes.datagen.loot.BlockLootTableProviderImpl;
import com.ultramega.refinedtypes.datagen.model.ModelProviders;
import com.ultramega.refinedtypes.datagen.recipe.MainRecipeProvider;
import com.ultramega.refinedtypes.datagen.tag.BlockTagsProvider;
import com.ultramega.refinedtypes.datagen.tag.ItemTagsProvider;

import java.util.List;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.advancements.AdvancementProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import static com.ultramega.refinedtypes.RefinedTypesUtil.MOD_ID;

@EventBusSubscriber(modid = MOD_ID)
public class DataGenerators {
    private DataGenerators() {
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent.Client e) {
        final DataGenerator generator = e.getGenerator();
        final DataGenerator.PackGenerator pack = generator.getVanillaPack(true);
        pack.addProvider(ModelProviders::new);
        pack.addProvider(output -> new BlockLootTableProviderImpl(output, e.getLookupProvider()));
        pack.addProvider(output -> new MainRecipeProvider.Runner(output, e.getLookupProvider()));
        final BlockTagsProvider blockTagsProvider = pack.addProvider(output ->
            new BlockTagsProvider(output, e.getLookupProvider()));
        pack.addProvider(output ->
            new ItemTagsProvider(output, e.getLookupProvider(), blockTagsProvider.contentsGetter()));
        pack.addProvider(output -> new AdvancementProvider(
            output,
            e.getLookupProvider(),
            List.of(new com.ultramega.refinedtypes.datagen.advancement.AdvancementProvider())
        ));
    }
}
