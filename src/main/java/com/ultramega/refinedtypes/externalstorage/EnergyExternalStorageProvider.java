package com.ultramega.refinedtypes.externalstorage;

import com.ultramega.refinedtypes.type.energy.EnergyCapabilityCache;
import com.ultramega.refinedtypes.type.energy.EnergyExtractableStorage;
import com.ultramega.refinedtypes.type.energy.EnergyInsertableStorage;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.Actor;
import com.refinedmods.refinedstorage.api.storage.ExtractableStorage;
import com.refinedmods.refinedstorage.api.storage.InsertableStorage;
import com.refinedmods.refinedstorage.api.storage.external.ExternalStorageProvider;

import java.util.Iterator;

class EnergyExternalStorageProvider implements ExternalStorageProvider {
    private final EnergyCapabilityCache capabilityCache;
    private final InsertableStorage insertTarget;
    private final ExtractableStorage extractTarget;

    EnergyExternalStorageProvider(final EnergyCapabilityCache capabilityCache) {
        this.capabilityCache = capabilityCache;
        this.insertTarget = new EnergyInsertableStorage(capabilityCache);
        this.extractTarget = new EnergyExtractableStorage(capabilityCache);
    }

    @Override
    public Iterator<ResourceAmount> iterator() {
        return this.capabilityCache.createAmountIterator();
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
