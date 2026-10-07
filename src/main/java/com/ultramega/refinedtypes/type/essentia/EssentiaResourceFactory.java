package com.ultramega.refinedtypes.type.essentia;

import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceFactory;

import java.util.Optional;

import com.leclowndu93150.thaumaturge.api.essentia.EssentiaCapabilities;
import net.minecraft.world.item.ItemStack;

public enum EssentiaResourceFactory implements ResourceFactory {
    INSTANCE;

    @Override
    public Optional<ResourceAmount> create(final ItemStack stack) {
        final var container = stack.getCapability(EssentiaCapabilities.CONTAINER);
        if (container == null || container.getAspects().size() != 1) {
            return Optional.empty();
        }
        final var entry = container.getAspects().entries().getFirst();
        return Optional.of(new ResourceAmount(EssentiaResource.of(entry.aspect()), entry.amount()));
    }

    @Override
    public boolean isValid(final ResourceKey resource) {
        return resource instanceof EssentiaResource;
    }
}
