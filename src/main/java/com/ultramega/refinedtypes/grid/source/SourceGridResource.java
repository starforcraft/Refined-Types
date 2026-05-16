package com.ultramega.refinedtypes.grid.source;

import com.ultramega.refinedtypes.grid.AbstractTypedGridResource;
import com.ultramega.refinedtypes.registry.Types;
import com.ultramega.refinedtypes.type.source.SourceResource;
import com.ultramega.refinedtypes.type.source.SourceResourceType;

import com.refinedmods.refinedstorage.common.Platform;
import com.refinedmods.refinedstorage.common.api.grid.view.GridResourceAttributeKey;

import java.util.Set;
import java.util.function.Function;

public class SourceGridResource extends AbstractTypedGridResource<SourceResource> {
    public SourceGridResource(final SourceResource resource,
                              final String name,
                              final Function<GridResourceAttributeKey, Set<String>> attributes) {
        super(resource, name, attributes, SourceResource.class, Types.SOURCE.get(), SourceResourceType.INSTANCE, Platform.INSTANCE.getBucketAmount());
    }
}
