package com.ultramega.refinedtypes.type.source;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.Actor;
import com.refinedmods.refinedstorage.api.storage.InsertableStorage;

import com.hollingsworth.arsnouveau.api.source.ISourceCap;

public class SourceInsertableStorage implements InsertableStorage {
    private final SourceCapabilityCache capabilityCache;

    public SourceInsertableStorage(final SourceCapabilityCache capabilityCache) {
        this.capabilityCache = capabilityCache;
    }

    public long getAmount(final ResourceKey resource) {
        if (!(resource instanceof SourceResource)) {
            return 0;
        }
        return this.capabilityCache.getCapability()
            .map(ISourceCap::getSource)
            .orElse(0);
    }

    @Override
    public long insert(final ResourceKey resource, final long amount, final Action action, final Actor actor) {
        if (!(resource instanceof SourceResource)) {
            return 0;
        }
        return this.capabilityCache.getCapability()
            .map(handler -> handler.receiveSource((int) amount, action == Action.SIMULATE))
            .orElse(0);
    }
}
