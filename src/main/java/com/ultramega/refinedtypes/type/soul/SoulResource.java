package com.ultramega.refinedtypes.type.soul;

import com.ultramega.refinedtypes.registry.Types;
import com.ultramega.refinedtypes.type.Type;

import com.refinedmods.refinedstorage.common.Platform;
import com.refinedmods.refinedstorage.common.api.support.resource.PlatformResourceKey;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceTag;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceType;

import java.util.List;

public record SoulResource(Type type) implements PlatformResourceKey {
    public static SoulResource createSoulResource() {
        return new SoulResource(Types.SOUL.get());
    }

    @Override
    public long getInterfaceExportLimit() {
        return SoulResourceType.INSTANCE.getInterfaceExportLimit();
    }

    @Override
    public long getProcessingPatternLimit() {
        return Platform.INSTANCE.getBucketAmount() * 100;
    }

    @Override
    public List<ResourceTag> getTags() {
        return Types.TYPE_REGISTRY.wrapAsHolder(this.type)
            .tags()
            .flatMap(tagKey -> Types.TYPE_REGISTRY.getTag(tagKey).stream())
            .map(tag -> new ResourceTag(
                tag.key(),
                tag.stream().map(holder -> (PlatformResourceKey) new SoulResource(holder.value())).toList()
            )).toList();
    }

    @Override
    public ResourceType getResourceType() {
        return SoulResourceType.INSTANCE;
    }
}
