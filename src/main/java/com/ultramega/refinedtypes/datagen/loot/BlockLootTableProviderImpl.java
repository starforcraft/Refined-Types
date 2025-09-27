package com.ultramega.refinedtypes.datagen.loot;

import com.ultramega.refinedtypes.registry.Blocks;
import com.ultramega.refinedtypes.storage.energy.EnergyStorageVariant;
import com.ultramega.refinedtypes.storage.soul.SoulStorageVariant;
import com.ultramega.refinedtypes.storage.source.SourceStorageVariant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.RandomSequence;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.conditions.WithConditions;
import org.apache.commons.lang3.Validate;

import static com.ultramega.refinedtypes.RefinedTypesUtil.ARS_NOUVEAU;
import static com.ultramega.refinedtypes.RefinedTypesUtil.INDUSTRIAL_FOREGOING_SOULS;

/**
 * Inspired by <a href="https://github.com/MinecraftschurliMods/Bibliocraft-Legacy/blob/main/src/api/java/com/github/minecraftschurlimods/bibliocraft/api/datagen/BlockLootTableProvider.java">Bibliocraft-Legacy</a>
 */
public class BlockLootTableProviderImpl implements DataProvider {
    private static final Codec<Optional<WithConditions<LootTable>>> CONDITIONAL_CODEC = ConditionalOps.createConditionalCodecWithConditions(LootTable.DIRECT_CODEC);
    private final CompletableFuture<HolderLookup.Provider> registries;
    private final PackOutput.PathProvider pathProvider;
    private final Map<ResourceKey<LootTable>, WithConditionsBuilder<LootTable.Builder>> map = new HashMap<>();

    public BlockLootTableProviderImpl(final PackOutput output, final CompletableFuture<HolderLookup.Provider> registries) {
        this.pathProvider = output.createRegistryElementsPathProvider(Registries.LOOT_TABLE);
        this.registries = registries;
    }

    public void generate() {
        this.addLootTableWithCondition(Blocks.getNetworkEnergizer(), null);

        for (final EnergyStorageVariant variant : EnergyStorageVariant.values()) {
            this.addLootTableWithCondition(Blocks.getEnergyStorageBlock(variant), null);
        }
        for (final SourceStorageVariant variant : SourceStorageVariant.values()) {
            this.addLootTableWithCondition(Blocks.getSourceStorageBlock(variant), ARS_NOUVEAU);
        }
        for (final SoulStorageVariant variant : SoulStorageVariant.values()) {
            this.addLootTableWithCondition(Blocks.getSoulStorageBlock(variant), INDUSTRIAL_FOREGOING_SOULS);
        }
    }

    private void addLootTableWithCondition(final Block block, @Nullable final String modId) {
        final BlockLootTableProviderImpl.WithConditionsBuilder<LootTable.Builder> builder = BlockLootTableProviderImpl.wrapLootTable(this.createNameableTable(block));
        if (modId != null) {
            builder.addCondition(new ModLoadedCondition(modId));
        }
        this.addLootTable(block, builder);
    }

    public LootTable.Builder createNameableTable(final Block block) {
        return this.createStandardTable(LootItem.lootTableItem(block).apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)));
    }

    public LootTable.Builder createStandardTable(final LootPoolSingletonContainer.Builder<?> builder) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(builder).when(ExplosionCondition.survivesExplosion()));
    }

    @Override
    public CompletableFuture<?> run(final CachedOutput output) {
        return this.registries.thenCompose(provider -> this.run(output, provider));
    }

    private CompletableFuture<?> run(final CachedOutput output, final HolderLookup.Provider provider) {
        this.generate();
        final Map<RandomSupport.Seed128bit, ResourceLocation> seeds = new Object2ObjectOpenHashMap<>();
        return CompletableFuture.allOf(this.map.entrySet().stream().map(entry -> {
            final ResourceLocation location = entry.getKey().location();
            final ResourceLocation sequence = seeds.put(RandomSequence.seedForKey(location), location);
            if (sequence != null) {
                Util.logAndPauseIfInIde("Loot table random sequence seed collision on " + sequence + " and " + location);
            }
            final WithConditions<LootTable> conditional = entry.getValue()
                .map(builder -> builder.setRandomSequence(location).setParamSet(LootContextParamSets.BLOCK).build())
                .build();
            return conditional.conditions().isEmpty()
                ? DataProvider.saveStable(output, provider, LootTable.DIRECT_CODEC, conditional.carrier(), this.pathProvider.json(location))
                : DataProvider.saveStable(output, provider, CONDITIONAL_CODEC, Optional.of(conditional), this.pathProvider.json(location));
        }).toArray(CompletableFuture[]::new));
    }

    public void addLootTable(final Block block, final WithConditionsBuilder<LootTable.Builder> builder) {
        this.map.put(block.getLootTable(), builder);
    }

    @Override
    public String getName() {
        return "Loot Tables";
    }

    public static WithConditionsBuilder<LootTable.Builder> wrapLootTable(final LootTable.Builder table) {
        return new WithConditionsBuilder<LootTable.Builder>().withCarrier(table);
    }

    /**
     * A variant of {@link WithConditions.Builder} that has a {@link WithConditionsBuilder#map(Function) map} operation
     * and does no validation on whether there are actually conditions added to the builder.
     *
     * @param <T> The wrapped builder's type.
     */
    public static class WithConditionsBuilder<T> extends WithConditions.Builder<T> {
        private final List<ICondition> conditions = new ArrayList<>();
        private T carrier;

        public WithConditionsBuilder(final List<ICondition> conditions) {
            this.conditions.addAll(conditions);
        }

        public WithConditionsBuilder() {
            this(new ArrayList<>());
        }

        public <N> WithConditionsBuilder<N> map(final Function<T, N> mapper) {
            return new WithConditionsBuilder<N>(this.conditions).withCarrier(mapper.apply(this.carrier));
        }

        public WithConditionsBuilder<T> addCondition(final ICondition... conditions) {
            this.conditions.addAll(List.of(conditions));
            return this;
        }

        public WithConditionsBuilder<T> withCarrier(final T carrier) {
            this.carrier = carrier;
            return this;
        }

        public WithConditions<T> build() {
            Validate.notNull(this.carrier, "You need to supply a carrier to create a WithConditions");
            return new WithConditions<>(this.conditions, this.carrier);
        }
    }
}
