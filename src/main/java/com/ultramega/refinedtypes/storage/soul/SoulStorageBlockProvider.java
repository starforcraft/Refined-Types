package com.ultramega.refinedtypes.storage.soul;

import com.ultramega.refinedtypes.ModInitializer;
import com.ultramega.refinedtypes.RefinedTypesUtil;
import com.ultramega.refinedtypes.registry.BlockEntities;
import com.ultramega.refinedtypes.registry.Menus;
import com.ultramega.refinedtypes.type.soul.SoulResourceFactory;
import com.ultramega.refinedtypes.type.soul.SoulResourceType;

import com.refinedmods.refinedstorage.common.api.storage.SerializableStorage;
import com.refinedmods.refinedstorage.common.api.storage.StorageBlockProvider;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceFactory;

import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SoulStorageBlockProvider implements StorageBlockProvider {
    private final SoulStorageVariant variant;
    private final Component displayName;

    public SoulStorageBlockProvider(final SoulStorageVariant variant) {
        this.variant = variant;
        this.displayName = RefinedTypesUtil.createRefinedTypesTranslation(
            "block",
            String.format("%s_soul_storage_block", variant.getName())
        );
    }

    @Override
    public SerializableStorage createStorage(final Runnable runnable) {
        return SoulResourceType.STORAGE_TYPE.create(this.variant.getCapacity(), runnable);
    }

    @Override
    public Component getDisplayName() {
        return this.displayName;
    }

    @Override
    public long getEnergyUsage() {
        return switch (this.variant) {
            case K_1 -> ModInitializer.getConfig().getSoulStorageBlock().get1KEnergyUsage();
            case K_8 -> ModInitializer.getConfig().getSoulStorageBlock().get8KEnergyUsage();
            case K_64 -> ModInitializer.getConfig().getSoulStorageBlock().get64KEnergyUsage();
            case K_512 -> ModInitializer.getConfig().getSoulStorageBlock().get512KEnergyUsage();
            case K_4096 -> ModInitializer.getConfig().getSoulStorageBlock().get4096KEnergyUsage();
            case K_32768 -> ModInitializer.getConfig().getSoulStorageBlock().get32768KEnergyUsage();
            case K_262144 -> ModInitializer.getConfig().getSoulStorageBlock().get262144KEnergyUsage();
            case K_2097152 -> ModInitializer.getConfig().getSoulStorageBlock().get2097152KEnergyUsage();
            case INFINITE -> ModInitializer.getConfig().getSoulStorageBlock().getInfiniteEnergyUsage();
        };
    }

    @Override
    public ResourceFactory getResourceFactory() {
        return SoulResourceFactory.INSTANCE;
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return BlockEntities.getSoulStorageBlock(this.variant);
    }

    @Override
    public MenuType<?> getMenuType() {
        return Menus.getSoulStorage();
    }
}
