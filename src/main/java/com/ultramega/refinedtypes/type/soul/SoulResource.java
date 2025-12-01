package com.ultramega.refinedtypes.type.soul;

import com.ultramega.refinedtypes.registry.Types;
import com.ultramega.refinedtypes.type.Type;

import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.common.api.support.resource.FuzzyModeNormalizer;
import com.refinedmods.refinedstorage.common.api.support.resource.PlatformResourceKey;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceTag;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceType;

import java.util.List;

public record SoulResource(Type type) implements PlatformResourceKey, FuzzyModeNormalizer {
    public static SoulResource createSoulResource() {
        return new SoulResource(Types.SOUL.get());
    }

    @Override
    public long getInterfaceExportLimit() {
        return SoulResourceType.INSTANCE.getInterfaceExportLimit();
    }

    @Override
    public long getProcessingPatternLimit() {
        return 1000;
    }

    @Override
    public List<ResourceTag> getTags() {
        return List.of();
    }

    @Override
    public ResourceKey normalize() {
        return new SoulResource(this.type);
    }

    @Override
    public ResourceType getResourceType() {
        return SoulResourceType.INSTANCE;
    }
}
