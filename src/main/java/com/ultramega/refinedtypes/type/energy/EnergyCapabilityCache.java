package com.ultramega.refinedtypes.type.energy;

import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;

import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import org.jspecify.annotations.Nullable;

import static com.ultramega.refinedtypes.type.energy.EnergyResource.ENERGY_RESOURCE;

public class EnergyCapabilityCache {
    private final BlockCapabilityCache<EnergyHandler, @Nullable Direction> cache;

    public EnergyCapabilityCache(final ServerLevel level, final BlockPos pos, final Direction direction) {
        this.cache = BlockCapabilityCache.create(Capabilities.Energy.BLOCK, level, pos, direction);
    }

    public Optional<EnergyHandler> getCapability() {
        return Optional.ofNullable(this.cache.getCapability());
    }

    public Iterator<ResourceAmount> createAmountIterator() {
        return this.getCapability().map(handler -> {
            final long amount = handler.getAmountAsLong();
            if (amount > 0) {
                return Collections.singletonList(new ResourceAmount(ENERGY_RESOURCE, amount)).iterator();
            }
            return Collections.<ResourceAmount>emptyIterator();
        }).orElse(Collections.emptyListIterator());
    }

    public Iterator<ResourceKey> createIterator() {
        return this.getCapability().map(handler -> {
            final long amount = handler.getAmountAsLong();
            if (amount > 0) {
                return Collections.<ResourceKey>singletonList(ENERGY_RESOURCE).iterator();
            }
            return Collections.<ResourceKey>emptyListIterator();
        }).orElse(Collections.emptyListIterator());
    }
}
