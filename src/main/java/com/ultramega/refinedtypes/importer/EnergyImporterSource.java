package com.ultramega.refinedtypes.importer;

import com.ultramega.refinedtypes.type.energy.EnergyCapabilityCache;
import com.ultramega.refinedtypes.type.energy.EnergyExtractableStorage;
import com.ultramega.refinedtypes.type.energy.EnergyInsertableStorage;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.network.impl.node.importer.ImporterSource;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.Actor;

import java.util.Iterator;

class EnergyImporterSource implements ImporterSource {
    private final EnergyCapabilityCache capabilityCache;
    private final EnergyInsertableStorage insertTarget;
    private final EnergyExtractableStorage extractTarget;

    EnergyImporterSource(final EnergyCapabilityCache capabilityCache) {
        this.capabilityCache = capabilityCache;
        this.insertTarget = new EnergyInsertableStorage(capabilityCache);
        this.extractTarget = new EnergyExtractableStorage(capabilityCache);
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
