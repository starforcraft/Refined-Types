package com.ultramega.refinedtypes.type.soul;

import com.ultramega.refinedtypes.storage.soul.ResourceContainerSoulHandlerAdapter;

import com.refinedmods.refinedstorage.api.core.Action;

import javax.annotation.Nullable;

import com.buuz135.industrialforegoingsouls.block.tile.NetworkBlockEntity;
import com.buuz135.industrialforegoingsouls.block_network.SoulNetwork;
import com.buuz135.industrialforegoingsouls.capabilities.ISoulHandler;
import com.buuz135.industrialforegoingsouls.capabilities.SoulCapabilities;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class SoulUtil {
    private SoulUtil() {
    }

    public static void registerCapability(final RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
            SoulCapabilities.BLOCK,
            com.refinedmods.refinedstorage.common.content.BlockEntities.INSTANCE.getInterface(),
            (be, side) -> new ResourceContainerSoulHandlerAdapter(be.getExportedResources())
        );
    }

    @Nullable
    public static SoulNetwork getNetwork(final SoulCapabilityCache capabilityCache) {
        final Level level = capabilityCache.getLevel();
        if (level.getBlockEntity(capabilityCache.getPos()) instanceof NetworkBlockEntity<?> networkBlock) {
            return networkBlock.getNetwork();
        }

        return null;
    }

    public static ISoulHandler.Action toSoulAction(final Action action) {
        return switch (action) {
            case EXECUTE -> ISoulHandler.Action.EXECUTE;
            case SIMULATE -> ISoulHandler.Action.SIMULATE;
        };
    }

    public static Action fromSoulAction(final ISoulHandler.Action action) {
        return switch (action) {
            case EXECUTE -> Action.EXECUTE;
            case SIMULATE -> Action.SIMULATE;
        };
    }
}
