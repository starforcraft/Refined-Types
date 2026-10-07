package com.ultramega.refinedtypes.type.essentia;

import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.leclowndu93150.thaumaturge.api.essentia.EssentiaCapabilities;
import com.leclowndu93150.thaumaturge.api.essentia.IEssentiaStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;

public class EssentiaCapabilityCache {
    private final BlockCapabilityCache<IEssentiaStorage, Direction> cache;
    private final ServerLevel level;

    public EssentiaCapabilityCache(final ServerLevel level, final BlockPos pos, final Direction direction) {
        this.level = level;
        this.cache = BlockCapabilityCache.create(EssentiaCapabilities.STORAGE, level, pos, direction);
    }

    public ServerLevel getLevel() {
        return this.level;
    }

    public Optional<IEssentiaStorage> getCapability() {
        return Optional.ofNullable(this.cache.getCapability());
    }

    private List<ResourceAmount> snapshot() {
        return this.getCapability().map(handler -> handler.contents().entries().stream()
            .filter(entry -> entry.amount() > 0)
            .map(entry -> new ResourceAmount(EssentiaResource.of(entry.aspect()), entry.amount()))
            .toList()).orElse(List.of());
    }

    public Iterator<ResourceAmount> createAmountIterator() {
        return this.snapshot().iterator();
    }

    public Iterator<ResourceKey> createIterator() {
        return this.snapshot().stream().map(ResourceAmount::resource).iterator();
    }
}
