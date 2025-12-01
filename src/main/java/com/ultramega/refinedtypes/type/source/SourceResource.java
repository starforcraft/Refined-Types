package com.ultramega.refinedtypes.type.source;

import com.ultramega.refinedtypes.registry.Types;
import com.ultramega.refinedtypes.type.Type;

import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.common.Platform;
import com.refinedmods.refinedstorage.common.api.support.resource.FuzzyModeNormalizer;
import com.refinedmods.refinedstorage.common.api.support.resource.PlatformResourceKey;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceTag;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceType;

import java.util.List;

public record SourceResource(Type type) implements PlatformResourceKey, FuzzyModeNormalizer {
    public static SourceResource createSourceResource() {
        return new SourceResource(Types.SOURCE.get());
    }

    @Override
    public long getInterfaceExportLimit() {
        return SourceResourceType.INSTANCE.getInterfaceExportLimit();
    }

    @Override
    public long getProcessingPatternLimit() {
        return Platform.INSTANCE.getBucketAmount() * 64;
    }

    @Override
    public List<ResourceTag> getTags() {
        return List.of();
    }

    @Override
    public ResourceKey normalize() {
        return new SourceResource(this.type);
    }

    @Override
    public ResourceType getResourceType() {
        return SourceResourceType.INSTANCE;
    }
}
