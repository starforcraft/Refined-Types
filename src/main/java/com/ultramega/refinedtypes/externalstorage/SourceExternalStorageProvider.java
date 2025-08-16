package com.ultramega.refinedtypes.externalstorage;

import com.ultramega.refinedtypes.type.source.SourceCapabilityCache;
import com.ultramega.refinedtypes.type.source.SourceExtractableStorage;
import com.ultramega.refinedtypes.type.source.SourceInsertableStorage;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.Actor;
import com.refinedmods.refinedstorage.api.storage.ExtractableStorage;
import com.refinedmods.refinedstorage.api.storage.InsertableStorage;
import com.refinedmods.refinedstorage.api.storage.external.ExternalStorageProvider;

import java.util.Iterator;

class SourceExternalStorageProvider implements ExternalStorageProvider {
    private final SourceCapabilityCache capabilityCache;
    private final InsertableStorage insertTarget;
    private final ExtractableStorage extractTarget;

    SourceExternalStorageProvider(final SourceCapabilityCache capabilityCache) {
        this.capabilityCache = capabilityCache;
        this.insertTarget = new SourceInsertableStorage(capabilityCache);
        this.extractTarget = new SourceExtractableStorage(capabilityCache);
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
