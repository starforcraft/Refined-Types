package com.ultramega.refinedtypes.externalstorage;

import com.ultramega.refinedtypes.type.soul.SoulCapabilityCache;
import com.ultramega.refinedtypes.type.soul.SoulExtractableStorage;
import com.ultramega.refinedtypes.type.soul.SoulInsertableStorage;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.Actor;
import com.refinedmods.refinedstorage.api.storage.ExtractableStorage;
import com.refinedmods.refinedstorage.api.storage.InsertableStorage;
import com.refinedmods.refinedstorage.api.storage.external.ExternalStorageProvider;

import java.util.Iterator;

class SoulExternalStorageProvider implements ExternalStorageProvider {
    private final SoulCapabilityCache capabilityCache;
    private final InsertableStorage insertTarget;
    private final ExtractableStorage extractTarget;

    SoulExternalStorageProvider(final SoulCapabilityCache capabilityCache) {
        this.capabilityCache = capabilityCache;
        this.insertTarget = new SoulInsertableStorage(capabilityCache);
        this.extractTarget = new SoulExtractableStorage(capabilityCache);
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
