package com.ultramega.refinedtypes;

import com.ultramega.refinedtypes.grid.energy.EnergyGridInsertionHint;
import com.ultramega.refinedtypes.networkenergizer.NetworkEnergizerScreen;
import com.ultramega.refinedtypes.registry.Items;
import com.ultramega.refinedtypes.registry.Menus;
import com.ultramega.refinedtypes.storage.energy.EnergyStorageVariant;
import com.ultramega.refinedtypes.storage.soul.SoulStorageVariant;
import com.ultramega.refinedtypes.storage.source.SourceStorageVariant;
import com.ultramega.refinedtypes.type.energy.EnergyResource;
import com.ultramega.refinedtypes.type.energy.EnergyResourceRendering;
import com.ultramega.refinedtypes.type.soul.SoulResource;
import com.ultramega.refinedtypes.type.soul.SoulResourceRendering;
import com.ultramega.refinedtypes.type.source.SourceResource;
import com.ultramega.refinedtypes.type.source.SourceResourceRendering;

import com.refinedmods.refinedstorage.common.Platform;
import com.refinedmods.refinedstorage.common.api.RefinedStorageClientApi;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import static com.ultramega.refinedtypes.RefinedTypesUtil.createRefinedTypesIdentifier;
import static com.ultramega.refinedtypes.RefinedTypesUtil.isArsNouveauLoaded;
import static com.ultramega.refinedtypes.RefinedTypesUtil.isIndustrialForegoingSoulsLoaded;

public final class ClientModInitializer {
    private ClientModInitializer() {
    }

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent e) {
        RefinedStorageClientApi.INSTANCE.registerResourceRendering(EnergyResource.class, new EnergyResourceRendering());
        RefinedStorageClientApi.INSTANCE.addAlternativeGridInsertionHint(new EnergyGridInsertionHint());
        final ResourceLocation energyDiskModel = createRefinedTypesIdentifier("block/disk/energy_disk");
        for (final EnergyStorageVariant variant : EnergyStorageVariant.values()) {
            RefinedStorageClientApi.INSTANCE.registerDiskModel(
                Items.getEnergyStorageDisk(variant),
                energyDiskModel
            );
        }
        if (isArsNouveauLoaded()) {
            RefinedStorageClientApi.INSTANCE.registerResourceRendering(SourceResource.class, new SourceResourceRendering(Platform.INSTANCE.getBucketAmount()));
            final ResourceLocation sourceDiskModel = createRefinedTypesIdentifier("block/disk/source_disk");
            for (final SourceStorageVariant variant : SourceStorageVariant.values()) {
                RefinedStorageClientApi.INSTANCE.registerDiskModel(
                    Items.getSourceStorageDisk(variant),
                    sourceDiskModel
                );
            }
        }
        if (isIndustrialForegoingSoulsLoaded()) {
            RefinedStorageClientApi.INSTANCE.registerResourceRendering(SoulResource.class, new SoulResourceRendering());
            final ResourceLocation soulDiskModel = createRefinedTypesIdentifier("block/disk/soul_disk");
            for (final SoulStorageVariant variant : SoulStorageVariant.values()) {
                RefinedStorageClientApi.INSTANCE.registerDiskModel(
                    Items.getSoulStorageDisk(variant),
                    soulDiskModel
                );
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterMenuScreens(final RegisterMenuScreensEvent e) {
        e.register(Menus.getNetworkEnergizer(), NetworkEnergizerScreen::new);

        e.<AbstractContainerMenu, AbstractContainerScreen<AbstractContainerMenu>>register(Menus.getEnergyStorage(), (menu, inventory, title) ->
            RefinedStorageClientApi.INSTANCE.createStorageBlockScreen(menu, inventory, title, EnergyResource.class));
        if (isArsNouveauLoaded()) {
            e.<AbstractContainerMenu, AbstractContainerScreen<AbstractContainerMenu>>register(Menus.getSourceStorage(), (menu, inventory, title) ->
                RefinedStorageClientApi.INSTANCE.createStorageBlockScreen(menu, inventory, title, SourceResource.class));
        }
        if (isIndustrialForegoingSoulsLoaded()) {
            e.<AbstractContainerMenu, AbstractContainerScreen<AbstractContainerMenu>>register(Menus.getSoulStorage(), (menu, inventory, title) ->
                RefinedStorageClientApi.INSTANCE.createStorageBlockScreen(menu, inventory, title, SoulResource.class));
        }
    }
}
