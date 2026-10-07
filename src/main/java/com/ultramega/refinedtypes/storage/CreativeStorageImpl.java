package com.ultramega.refinedtypes.storage;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.Actor;
import com.refinedmods.refinedstorage.api.storage.Storage;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class CreativeStorageImpl implements Storage {
    private final Supplier<? extends Collection<? extends ResourceKey>> resources;


    public CreativeStorageImpl(final ResourceKey resource) {
        this(() -> List.of(resource));
    }

    public CreativeStorageImpl(final Supplier<? extends Collection<? extends ResourceKey>> resources) {
        this.resources = resources;
    }

    @Override
    public long extract(final ResourceKey resource, final long amount, final Action action, final Actor actor) {
        ResourceAmount.validate(resource, amount);
        if (!this.resources.get().contains(resource)) {
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
        return this.resources.get().stream()
            .map(resource -> new ResourceAmount(resource, Long.MAX_VALUE))
            .toList();
    }

    @Override
    public long getStored() {
        return Long.MAX_VALUE;
    }
}
