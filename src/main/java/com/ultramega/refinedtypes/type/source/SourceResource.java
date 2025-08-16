package com.ultramega.refinedtypes.type.source;

import com.ultramega.refinedtypes.registry.Types;
import com.ultramega.refinedtypes.type.Type;

import com.refinedmods.refinedstorage.common.Platform;
import com.refinedmods.refinedstorage.common.api.support.resource.PlatformResourceKey;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceTag;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceType;

import java.util.List;

public record SourceResource(Type type) implements PlatformResourceKey {
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
        return Types.TYPE_REGISTRY.wrapAsHolder(this.type)
            .tags()
            .flatMap(tagKey -> Types.TYPE_REGISTRY.getTag(tagKey).stream())
            .map(tag -> new ResourceTag(
                tag.key(),
                tag.stream().map(holder -> (PlatformResourceKey) new SourceResource(holder.value())).toList()
            )).toList();
    }

    @Override
    public ResourceType getResourceType() {
        return SourceResourceType.INSTANCE;
    }
}
