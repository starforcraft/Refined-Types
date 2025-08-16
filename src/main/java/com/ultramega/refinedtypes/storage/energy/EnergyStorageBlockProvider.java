package com.ultramega.refinedtypes.storage.energy;

import com.ultramega.refinedtypes.ModInitializer;
import com.ultramega.refinedtypes.RefinedTypesUtil;
import com.ultramega.refinedtypes.registry.BlockEntities;
import com.ultramega.refinedtypes.registry.Menus;
import com.ultramega.refinedtypes.type.energy.EnergyResourceFactory;
import com.ultramega.refinedtypes.type.energy.EnergyResourceType;

import com.refinedmods.refinedstorage.common.api.storage.SerializableStorage;
import com.refinedmods.refinedstorage.common.api.storage.StorageBlockProvider;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceFactory;

import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class EnergyStorageBlockProvider implements StorageBlockProvider {
    private final EnergyStorageVariant variant;
    private final Component displayName;

    public EnergyStorageBlockProvider(final EnergyStorageVariant variant) {
        this.variant = variant;
        this.displayName = RefinedTypesUtil.createRefinedTypesTranslation(
            "block",
            String.format("%s_energy_storage_block", variant.getName())
        );
    }

    @Override
    public SerializableStorage createStorage(final Runnable runnable) {
        return EnergyResourceType.STORAGE_TYPE.create(this.variant.getCapacity(), runnable);
    }

    @Override
    public Component getDisplayName() {
        return this.displayName;
    }

    @Override
    public long getEnergyUsage() {
        return switch (this.variant) {
            case K_64 -> ModInitializer.getConfig().getEnergyStorageBlock().get64KEnergyUsage();
            case K_256 -> ModInitializer.getConfig().getEnergyStorageBlock().get256KEnergyUsage();
            case K_1024 -> ModInitializer.getConfig().getEnergyStorageBlock().get1024KEnergyUsage();
            case K_8192 -> ModInitializer.getConfig().getEnergyStorageBlock().get8192KEnergyUsage();
            case K_65536 -> ModInitializer.getConfig().getEnergyStorageBlock().get65536KEnergyUsage();
            case K_262144 -> ModInitializer.getConfig().getEnergyStorageBlock().get262144KEnergyUsage();
            case K_1048576 -> ModInitializer.getConfig().getEnergyStorageBlock().get1048576KEnergyUsage();
            case K_8388608 -> ModInitializer.getConfig().getEnergyStorageBlock().get8388608KEnergyUsage();
            case INFINITE -> ModInitializer.getConfig().getEnergyStorageBlock().getInfiniteEnergyUsage();
        };
    }

    @Override
    public ResourceFactory getResourceFactory() {
        return EnergyResourceFactory.INSTANCE;
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return BlockEntities.getEnergyStorageBlock(this.variant);
    }

    @Override
    public MenuType<?> getMenuType() {
        return Menus.getEnergyStorage();
    }
}
