package com.ultramega.refinedtypes.type.energy;

import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceFactory;

import java.util.Optional;

import net.minecraft.world.item.ItemStack;

import static com.ultramega.refinedtypes.RefinedTypesUtil.dischargeContainer;
import static com.ultramega.refinedtypes.type.energy.EnergyResource.ENERGY_RESOURCE;

public enum EnergyResourceFactory implements ResourceFactory {
    INSTANCE;

    @Override
    public Optional<ResourceAmount> create(final ItemStack stack) {
        return dischargeContainer(stack).map(result -> new ResourceAmount(ENERGY_RESOURCE, Integer.MAX_VALUE));
    }

    @Override
    public boolean isValid(final ResourceKey resourceKey) {
        return resourceKey instanceof EnergyResource;
    }
}
