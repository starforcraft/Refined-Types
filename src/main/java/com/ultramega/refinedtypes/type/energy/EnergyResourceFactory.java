package com.ultramega.refinedtypes.type.energy;

import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceFactory;

import java.util.Optional;

import dev.technici4n.grandpower.api.ILongEnergyStorage;
import net.minecraft.world.item.ItemStack;

import static com.ultramega.refinedtypes.type.energy.EnergyResource.createEnergyResource;

public enum EnergyResourceFactory implements ResourceFactory {
    INSTANCE;

    @Override
    public Optional<ResourceAmount> create(final ItemStack stack) {
        return Optional.ofNullable(stack.getCapability(ILongEnergyStorage.ITEM))
            .map(handler -> handler.extract(Long.MAX_VALUE, true))
            .filter(amount -> amount > 0)
            .map(amount -> new ResourceAmount(createEnergyResource(), amount));
    }

    @Override
    public boolean isValid(final ResourceKey resourceKey) {
        return resourceKey instanceof EnergyResource;
    }
}
