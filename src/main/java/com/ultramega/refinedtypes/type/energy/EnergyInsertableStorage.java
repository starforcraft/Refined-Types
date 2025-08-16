package com.ultramega.refinedtypes.type.energy;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.Actor;
import com.refinedmods.refinedstorage.api.storage.InsertableStorage;

import dev.technici4n.grandpower.api.ILongEnergyStorage;

public class EnergyInsertableStorage implements InsertableStorage {
    private final EnergyCapabilityCache capabilityCache;

    public EnergyInsertableStorage(final EnergyCapabilityCache capabilityCache) {
        this.capabilityCache = capabilityCache;
    }

    public long getAmount(final ResourceKey resource) {
        if (!(resource instanceof EnergyResource)) {
            return 0;
        }
        return this.capabilityCache.getCapability()
            .map(ILongEnergyStorage::getAmount)
            .orElse(0L);
    }

    @Override
    public long insert(final ResourceKey resource, final long amount, final Action action, final Actor actor) {
        if (!(resource instanceof EnergyResource)) {
            return 0;
        }
        return this.capabilityCache.getCapability()
            .map(handler -> handler.receive(amount, action == Action.SIMULATE))
            .orElse(0L);
    }
}
