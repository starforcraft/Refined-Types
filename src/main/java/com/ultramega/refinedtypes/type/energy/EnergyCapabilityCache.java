package com.ultramega.refinedtypes.type.energy;

import com.refinedmods.refinedstorage.api.core.NullableType;
import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;

import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;

import dev.technici4n.grandpower.api.ILongEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;

import static com.ultramega.refinedtypes.type.energy.EnergyResource.createEnergyResource;

public class EnergyCapabilityCache {
    private final BlockCapabilityCache<ILongEnergyStorage, @NullableType Direction> cache;

    public EnergyCapabilityCache(final ServerLevel level, final BlockPos pos, final Direction direction) {
        this.cache = BlockCapabilityCache.create(ILongEnergyStorage.BLOCK, level, pos, direction);
    }

    public Optional<ILongEnergyStorage> getCapability() {
        return Optional.ofNullable(this.cache.getCapability());
    }

    public Iterator<ResourceAmount> createAmountIterator() {
        return this.getCapability().map(handler -> {
            final long amount = handler.getAmount();
            if (amount > 0) {
                return Collections.singletonList(new ResourceAmount(createEnergyResource(), amount)).iterator();
            }
            return Collections.<ResourceAmount>emptyIterator();
        }).orElse(Collections.emptyListIterator());
    }

    public Iterator<ResourceKey> createIterator() {
        return this.getCapability().map(handler -> {
            final long amount = handler.getAmount();
            if (amount > 0) {
                return Collections.<ResourceKey>singletonList(createEnergyResource()).iterator();
            }
            return Collections.<ResourceKey>emptyListIterator();
        }).orElse(Collections.emptyListIterator());
    }
}
