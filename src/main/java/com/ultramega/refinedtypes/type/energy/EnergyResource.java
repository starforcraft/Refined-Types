package com.ultramega.refinedtypes.type.energy;

import com.ultramega.refinedtypes.registry.Types;
import com.ultramega.refinedtypes.type.Type;

import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.common.api.support.resource.FuzzyModeNormalizer;
import com.refinedmods.refinedstorage.common.api.support.resource.PlatformResourceKey;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceTag;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceType;

import java.util.List;

import static com.ultramega.refinedtypes.type.energy.EnergyResourceType.DEFAULT_TRANSFER_AMOUNT;

public record EnergyResource(Type type) implements PlatformResourceKey, FuzzyModeNormalizer {
    public static final EnergyResource ENERGY_RESOURCE = new EnergyResource(Types.FE.get());

    @Override
    public long getInterfaceExportLimit() {
        return EnergyResourceType.INSTANCE.getInterfaceExportLimit();
    }

    @Override
    public long getProcessingPatternLimit() {
        return DEFAULT_TRANSFER_AMOUNT * 1_000_000L;
    }

    @Override
    public List<ResourceTag> getTags() {
        return List.of();
    }

    @Override
    public ResourceKey normalize() {
        return ENERGY_RESOURCE;
    }

    @Override
    public ResourceType getResourceType() {
        return EnergyResourceType.INSTANCE;
    }
}
