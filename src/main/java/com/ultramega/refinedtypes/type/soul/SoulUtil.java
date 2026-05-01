//package com.ultramega.refinedtypes.type.soul;
//
//import com.ultramega.refinedtypes.storage.soul.ResourceContainerSoulHandlerAdapter;
//
//import com.refinedmods.refinedstorage.api.core.Action;
//import com.refinedmods.refinedstorage.common.content.BlockEntities;
//
//import com.buuz135.industrialforegoingsouls.IndustrialForegoingSouls;
//import com.buuz135.industrialforegoingsouls.block.tile.NetworkBlockEntity;
//import com.buuz135.industrialforegoingsouls.capabilities.ISoulHandler;
//import com.buuz135.industrialforegoingsouls.capabilities.SoulCapabilities;
//import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
//
//public class SoulUtil {
//    private SoulUtil() {
//    }
//
//    public static void registerCapability(final RegisterCapabilitiesEvent event) {
//        event.registerBlockEntity(
//            SoulCapabilities.BLOCK,
//            BlockEntities.INSTANCE.getInterface(),
//            (be, side) -> new ResourceContainerSoulHandlerAdapter(be.getExportedResources())
//        );
//        event.registerBlock(
//            SoulCapabilities.BLOCK,
//            (level, pos, state, blockEntity, side) -> {
//                if (blockEntity instanceof NetworkBlockEntity<?> networkBlockEntity) {
//                    return new SoulNetworkSoulHandlerAdapter(level, networkBlockEntity);
//                }
//                return null;
//            },
//            IndustrialForegoingSouls.SOUL_PIPE_BLOCK.block().get(),
//            IndustrialForegoingSouls.SOUL_SURGE_BLOCK.block().get()
//        );
//    }
//
//    public static ISoulHandler.Action toSoulAction(final Action action) {
//        return switch (action) {
//            case EXECUTE -> ISoulHandler.Action.EXECUTE;
//            case SIMULATE -> ISoulHandler.Action.SIMULATE;
//        };
//    }
//
//    public static Action fromSoulAction(final ISoulHandler.Action action) {
//        return switch (action) {
//            case EXECUTE -> Action.EXECUTE;
//            case SIMULATE -> Action.SIMULATE;
//        };
//    }
//}
