package com.ultramega.refinedtypes.type.source;

import com.ultramega.refinedtypes.storage.source.ResourceContainerSourceHandlerAdapter;

import com.refinedmods.refinedstorage.common.content.BlockEntities;

import com.hollingsworth.arsnouveau.setup.registry.CapabilityRegistry;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class SourceUtil {
    private SourceUtil() {
    }

    public static void registerCapability(final RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
            CapabilityRegistry.SOURCE_CAPABILITY,
            BlockEntities.INSTANCE.getInterface(),
            (be, side) -> new ResourceContainerSourceHandlerAdapter(be.getExportedResources())
        );
    }
}
