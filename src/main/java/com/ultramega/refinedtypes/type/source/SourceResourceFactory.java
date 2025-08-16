package com.ultramega.refinedtypes.type.source;

import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceFactory;

import java.util.Optional;

import net.minecraft.world.item.ItemStack;

public enum SourceResourceFactory implements ResourceFactory {
    INSTANCE;

    @Override
    public Optional<ResourceAmount> create(final ItemStack stack) {
        // There's no source cap for items
        return Optional.empty();
    }

    @Override
    public boolean isValid(final ResourceKey resourceKey) {
        return resourceKey instanceof SourceResource;
    }
}
