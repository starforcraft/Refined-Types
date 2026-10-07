package com.ultramega.refinedtypes.externalstorage;

import com.ultramega.refinedtypes.type.essentia.EssentiaCapabilityCache;
import com.ultramega.refinedtypes.type.essentia.EssentiaExtractableStorage;
import com.ultramega.refinedtypes.type.essentia.EssentiaInsertableStorage;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.Actor;
import com.refinedmods.refinedstorage.api.storage.ExtractableStorage;
import com.refinedmods.refinedstorage.api.storage.InsertableStorage;
import com.refinedmods.refinedstorage.api.storage.external.ExternalStorageProvider;

import java.util.Iterator;

class EssentiaExternalStorageProvider implements ExternalStorageProvider {
    private final EssentiaCapabilityCache capabilityCache;
    private final InsertableStorage insertTarget;
    private final ExtractableStorage extractTarget;

    EssentiaExternalStorageProvider(final EssentiaCapabilityCache capabilityCache) {
        this.capabilityCache = capabilityCache;
        this.insertTarget = new EssentiaInsertableStorage(capabilityCache);
        this.extractTarget = new EssentiaExtractableStorage(capabilityCache);
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
