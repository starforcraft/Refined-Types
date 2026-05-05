package com.ultramega.refinedtypes.storage;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.Actor;
import com.refinedmods.refinedstorage.api.storage.Storage;

import java.util.Collection;
import java.util.List;

public class CreativeStorageImpl implements Storage {
    private final ResourceKey resource;

    public CreativeStorageImpl(final ResourceKey resource) {
        this.resource = resource;
    }

    @Override
    public long extract(final ResourceKey resource, final long amount, final Action action, final Actor actor) {
        ResourceAmount.validate(resource, amount);
        if (!this.resource.equals(resource)) {
            return 0;
        }
        return amount;
    }

    @Override
    public long insert(final ResourceKey resource, final long amount, final Action action, final Actor actor) {
        ResourceAmount.validate(resource, amount);
        return amount;
    }

    @Override
    public Collection<ResourceAmount> getAll() {
        return List.of(new ResourceAmount(this.resource, Long.MAX_VALUE));
    }

    @Override
    public long getStored() {
        return Long.MAX_VALUE;
    }
}
