package com.ultramega.refinedtypes.importer;

import com.ultramega.refinedtypes.type.soul.SoulCapabilityCache;
import com.ultramega.refinedtypes.type.soul.SoulExtractableStorage;
import com.ultramega.refinedtypes.type.soul.SoulInsertableStorage;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.network.impl.node.importer.ImporterSource;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.Actor;

import java.util.Iterator;

class SoulImporterSource implements ImporterSource {
    private final SoulCapabilityCache capabilityCache;
    private final SoulInsertableStorage insertTarget;
    private final SoulExtractableStorage extractTarget;

    SoulImporterSource(final SoulCapabilityCache capabilityCache) {
        this.capabilityCache = capabilityCache;
        this.insertTarget = new SoulInsertableStorage(capabilityCache);
        this.extractTarget = new SoulExtractableStorage(capabilityCache);
    }

    public long getAmount(final ResourceKey resource) {
        return this.extractTarget.getAmount(resource);
    }

    @Override
    public Iterator<ResourceKey> getResources() {
        return this.capabilityCache.createIterator();
    }

    @Override
    public long extract(final ResourceKey resource, final long amount, final Action action, final Actor actor) {
        return this.extractTarget.extract(resource, amount, action, actor);
    }

    @Override
    public long insert(final ResourceKey resource, final long amount, final Action action, final Actor actor) {
        return this.insertTarget.insert(resource, amount, action, actor);
    }
}
