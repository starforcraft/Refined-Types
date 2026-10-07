package com.ultramega.refinedtypes.importer;

import com.ultramega.refinedtypes.type.essentia.EssentiaCapabilityCache;
import com.ultramega.refinedtypes.type.essentia.EssentiaExtractableStorage;
import com.ultramega.refinedtypes.type.essentia.EssentiaInsertableStorage;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.network.impl.node.importer.ImporterSource;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.Actor;

import java.util.Iterator;

class EssentiaImporterSource implements ImporterSource {
    private final EssentiaCapabilityCache capabilityCache;
    private final EssentiaInsertableStorage insertTarget;
    private final EssentiaExtractableStorage extractTarget;

    EssentiaImporterSource(final EssentiaCapabilityCache capabilityCache) {
        this.capabilityCache = capabilityCache;
        this.insertTarget = new EssentiaInsertableStorage(capabilityCache);
        this.extractTarget = new EssentiaExtractableStorage(capabilityCache);
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
