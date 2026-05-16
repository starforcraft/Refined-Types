package com.ultramega.refinedtypes.grid.soul;

import com.ultramega.refinedtypes.grid.AbstractTypedGridResource;
import com.ultramega.refinedtypes.registry.Types;
import com.ultramega.refinedtypes.type.soul.SoulResource;
import com.ultramega.refinedtypes.type.soul.SoulResourceType;

import com.refinedmods.refinedstorage.common.api.grid.view.GridResourceAttributeKey;

import java.util.Set;
import java.util.function.Function;

public class SoulGridResource extends AbstractTypedGridResource<SoulResource> {
    public SoulGridResource(final SoulResource resource,
                            final String name,
                            final Function<GridResourceAttributeKey, Set<String>> attributes) {
        super(resource, name, attributes, SoulResource.class, Types.SOUL.get(), SoulResourceType.INSTANCE, 1);
    }
}
