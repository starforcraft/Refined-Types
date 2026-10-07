package com.ultramega.refinedtypes;

import com.ultramega.refinedtypes.grid.strategy.energy.EnergyGridInsertionHint;
import com.ultramega.refinedtypes.grid.strategy.essentia.EssentiaGridInsertionHint;
import com.ultramega.refinedtypes.networkenergizer.NetworkEnergizerScreen;
import com.ultramega.refinedtypes.registry.Items;
import com.ultramega.refinedtypes.registry.Menus;
import com.ultramega.refinedtypes.storage.energy.EnergyStorageVariant;
import com.ultramega.refinedtypes.storage.essentia.EssentiaStorageVariant;
import com.ultramega.refinedtypes.type.energy.EnergyResource;
import com.ultramega.refinedtypes.type.energy.EnergyResourceRendering;
import com.ultramega.refinedtypes.type.essentia.EssentiaResource;
import com.ultramega.refinedtypes.type.essentia.EssentiaResourceRendering;

import com.refinedmods.refinedstorage.common.api.RefinedStorageClientApi;
import com.refinedmods.refinedstorage.common.support.tooltip.MouseClientTooltipComponent;

import java.util.Optional;

import com.leclowndu93150.thaumaturge.api.essentia.EssentiaCapabilities;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import static com.ultramega.refinedtypes.RefinedTypesUtil.createRefinedTypesIdentifier;
import static com.ultramega.refinedtypes.RefinedTypesUtil.isArsNouveauLoaded;
import static com.ultramega.refinedtypes.RefinedTypesUtil.isIndustrialForegoingSoulsLoaded;
import static com.ultramega.refinedtypes.RefinedTypesUtil.isThaumaturgeLoaded;

public final class ClientModInitializer {
    private ClientModInitializer() {
    }

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent e) {
        RefinedStorageClientApi.INSTANCE.registerResourceRendering(EnergyResource.class, new EnergyResourceRendering());
        RefinedStorageClientApi.INSTANCE.addAlternativeGridInsertionHint(new EnergyGridInsertionHint());
        final Identifier energyDiskModel = createRefinedTypesIdentifier("block/disk/energy_disk");
        for (final EnergyStorageVariant variant : EnergyStorageVariant.values()) {
            RefinedStorageClientApi.INSTANCE.registerDiskModel(Items.getEnergyStorageDisk(variant), energyDiskModel);
        }
        if (isThaumaturgeLoaded()) {
            RefinedStorageClientApi.INSTANCE.registerResourceRendering(EssentiaResource.class, new EssentiaResourceRendering());
            RefinedStorageClientApi.INSTANCE.addAlternativeGridInsertionHint(new EssentiaGridInsertionHint());
            final Identifier essentiaDiskModel = createRefinedTypesIdentifier("block/disk/essentia_disk");
            for (final EssentiaStorageVariant variant : EssentiaStorageVariant.values()) {
                RefinedStorageClientApi.INSTANCE.registerDiskModel(Items.getEssentiaStorageDisk(variant), essentiaDiskModel);
            }
        }
//        if (isArsNouveauLoaded()) {
//            RefinedStorageClientApi.INSTANCE.registerResourceRendering(SourceResource.class, new SourceResourceRendering(Platform.INSTANCE.getBucketAmount()));
//            final Identifier sourceDiskModel = createRefinedTypesIdentifier("block/disk/source_disk");
//            for (final SourceStorageVariant variant : SourceStorageVariant.values()) {
//                RefinedStorageClientApi.INSTANCE.registerDiskModel(
//                    Items.getSourceStorageDisk(variant),
//                    sourceDiskModel
//                );
//            }
//        }
//        if (isIndustrialForegoingSoulsLoaded()) {
//            RefinedStorageClientApi.INSTANCE.registerResourceRendering(SoulResource.class, new SoulResourceRendering());
//            final Identifier soulDiskModel = createRefinedTypesIdentifier("block/disk/soul_disk");
//            for (final SoulStorageVariant variant : SoulStorageVariant.values()) {
//                RefinedStorageClientApi.INSTANCE.registerDiskModel(
//                    Items.getSoulStorageDisk(variant),
//                    soulDiskModel
//                );
//            }
//        }
    }

    @SubscribeEvent
    public static void onRegisterMenuScreens(final RegisterMenuScreensEvent e) {
        e.register(Menus.getNetworkEnergizer(), NetworkEnergizerScreen::new);

        e.<AbstractContainerMenu, AbstractContainerScreen<AbstractContainerMenu>>register(Menus.getEnergyStorage(), (menu, inventory, title) ->
            RefinedStorageClientApi.INSTANCE.createStorageBlockScreen(menu, inventory, title, EnergyResource.class));
        if (isThaumaturgeLoaded()) {
            e.<AbstractContainerMenu, AbstractContainerScreen<AbstractContainerMenu>>register(Menus.getEssentiaStorage(), (menu, inventory, title) ->
                RefinedStorageClientApi.INSTANCE.createStorageBlockScreen(menu, inventory, title, EssentiaResource.class));
        }
        if (isArsNouveauLoaded()) {
//            e.<AbstractContainerMenu, AbstractContainerScreen<AbstractContainerMenu>>register(Menus.getSourceStorage(), (menu, inventory, title) ->
//                RefinedStorageClientApi.INSTANCE.createStorageBlockScreen(menu, inventory, title, SourceResource.class));
        }
        if (isIndustrialForegoingSoulsLoaded()) {
//            e.<AbstractContainerMenu, AbstractContainerScreen<AbstractContainerMenu>>register(Menus.getSoulStorage(), (menu, inventory, title) ->
//                RefinedStorageClientApi.INSTANCE.createStorageBlockScreen(menu, inventory, title, SoulResource.class));
        }
    }
}
