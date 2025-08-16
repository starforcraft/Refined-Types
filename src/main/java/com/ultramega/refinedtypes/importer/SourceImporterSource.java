package com.ultramega.refinedtypes.importer;

import com.ultramega.refinedtypes.type.source.SourceCapabilityCache;
import com.ultramega.refinedtypes.type.source.SourceExtractableStorage;
import com.ultramega.refinedtypes.type.source.SourceInsertableStorage;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.network.impl.node.importer.ImporterSource;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.Actor;

import java.util.Iterator;

class SourceImporterSource implements ImporterSource {
    private final SourceCapabilityCache capabilityCache;
    private final SourceInsertableStorage insertTarget;
    private final SourceExtractableStorage extractTarget;

    SourceImporterSource(final SourceCapabilityCache capabilityCache) {
        this.capabilityCache = capabilityCache;
        this.insertTarget = new SourceInsertableStorage(capabilityCache);
        this.extractTarget = new SourceExtractableStorage(capabilityCache);
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
