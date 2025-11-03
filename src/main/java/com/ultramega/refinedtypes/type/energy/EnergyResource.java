package com.ultramega.refinedtypes.type.energy;

import com.ultramega.refinedtypes.registry.Types;
import com.ultramega.refinedtypes.type.Type;

import com.refinedmods.refinedstorage.common.Platform;
import com.refinedmods.refinedstorage.common.api.support.resource.PlatformResourceKey;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceTag;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceType;

import java.util.List;

public record EnergyResource(Type type) implements PlatformResourceKey { //TODO: make an abstract class with the other Resource Types
    public static EnergyResource createEnergyResource() {
        return new EnergyResource(Types.FE.get());
    }

    @Override
    public long getInterfaceExportLimit() {
        return EnergyResourceType.INSTANCE.getInterfaceExportLimit();
    }

    @Override
    public long getProcessingPatternLimit() {
        return Platform.INSTANCE.getBucketAmount() * 1000;
    }

    @Override
    public List<ResourceTag> getTags() {
        return List.of();
    }

    @Override
    public ResourceType getResourceType() {
        return EnergyResourceType.INSTANCE;
    }
}
