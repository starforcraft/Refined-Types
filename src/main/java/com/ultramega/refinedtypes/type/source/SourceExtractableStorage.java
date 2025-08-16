package com.ultramega.refinedtypes.type.source;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.Actor;
import com.refinedmods.refinedstorage.api.storage.ExtractableStorage;

import com.hollingsworth.arsnouveau.api.source.ISourceCap;

public class SourceExtractableStorage implements ExtractableStorage {
    private final SourceCapabilityCache capabilityCache;

    public SourceExtractableStorage(final SourceCapabilityCache capabilityCache) {
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
    public long extract(final ResourceKey resource, final long amount, final Action action, final Actor actor) {
        if (!(resource instanceof SourceResource)) {
            return 0;
        }
        return this.capabilityCache.getCapability().map(handler ->
            handler.extractSource((int) amount, action == Action.SIMULATE)
        ).orElse(0);
    }
}
