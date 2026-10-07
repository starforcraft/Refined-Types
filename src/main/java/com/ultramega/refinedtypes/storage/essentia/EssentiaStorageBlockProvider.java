package com.ultramega.refinedtypes.storage.essentia;

import com.ultramega.refinedtypes.ModInitializer;
import com.ultramega.refinedtypes.RefinedTypesUtil;
import com.ultramega.refinedtypes.registry.BlockEntities;
import com.ultramega.refinedtypes.registry.Menus;
import com.ultramega.refinedtypes.type.essentia.EssentiaResourceFactory;
import com.ultramega.refinedtypes.type.essentia.EssentiaResourceType;

import com.refinedmods.refinedstorage.common.api.storage.SerializableStorage;
import com.refinedmods.refinedstorage.common.api.storage.StorageBlockProvider;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceFactory;

import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class EssentiaStorageBlockProvider implements StorageBlockProvider {
    private final EssentiaStorageVariant variant;
    private final Component displayName;

    public EssentiaStorageBlockProvider(final EssentiaStorageVariant variant) {
        this.variant = variant;
        this.displayName = RefinedTypesUtil.createRefinedTypesTranslation(
            "block",
            String.format("%s_essentia_storage_block", variant.getName())
        );
    }

    @Override
    public SerializableStorage createStorage(final Runnable runnable) {
        return EssentiaResourceType.STORAGE_TYPE.create(this.variant.getCapacity(), runnable);
    }

    @Override
    public Component getDisplayName() {
        return this.displayName;
    }

    @Override
    public long getEnergyUsage() {
        return switch (this.variant) {
            case K_64 -> ModInitializer.getConfig().getEssentiaStorageBlock().get64KEnergyUsage();
            case K_256 -> ModInitializer.getConfig().getEssentiaStorageBlock().get256KEnergyUsage();
            case K_1024 -> ModInitializer.getConfig().getEssentiaStorageBlock().get1024KEnergyUsage();
            case K_8192 -> ModInitializer.getConfig().getEssentiaStorageBlock().get8192KEnergyUsage();
            case K_65536 -> ModInitializer.getConfig().getEssentiaStorageBlock().get65536KEnergyUsage();
            case K_262144 -> ModInitializer.getConfig().getEssentiaStorageBlock().get262144KEnergyUsage();
            case K_1048576 -> ModInitializer.getConfig().getEssentiaStorageBlock().get1048576KEnergyUsage();
            case K_8388608 -> ModInitializer.getConfig().getEssentiaStorageBlock().get8388608KEnergyUsage();
            case INFINITE -> ModInitializer.getConfig().getEssentiaStorageBlock().getInfiniteEnergyUsage();
            case CREATIVE -> 0;
        };
    }

    @Override
    public ResourceFactory getResourceFactory() {
        return EssentiaResourceFactory.INSTANCE;
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return BlockEntities.getEssentiaStorageBlock(this.variant);
    }

    @Override
    public MenuType<?> getMenuType() {
        return Menus.getEssentiaStorage();
    }
}
