package com.ultramega.refinedtypes.storage.source;

import com.ultramega.refinedtypes.ModInitializer;
import com.ultramega.refinedtypes.RefinedTypesUtil;
import com.ultramega.refinedtypes.registry.BlockEntities;
import com.ultramega.refinedtypes.registry.Menus;
import com.ultramega.refinedtypes.type.source.SourceResourceFactory;
import com.ultramega.refinedtypes.type.source.SourceResourceType;

import com.refinedmods.refinedstorage.common.api.storage.SerializableStorage;
import com.refinedmods.refinedstorage.common.api.storage.StorageBlockProvider;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceFactory;

import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SourceStorageBlockProvider implements StorageBlockProvider {
    private final SourceStorageVariant variant;
    private final Component displayName;

    public SourceStorageBlockProvider(final SourceStorageVariant variant) {
        this.variant = variant;
        this.displayName = RefinedTypesUtil.createRefinedTypesTranslation(
            "block",
            String.format("%s_source_storage_block", variant.getName())
        );
    }

    @Override
    public SerializableStorage createStorage(final Runnable runnable) {
        return SourceResourceType.STORAGE_TYPE.create(this.variant.getCapacity(), runnable);
    }

    @Override
    public Component getDisplayName() {
        return this.displayName;
    }

    @Override
    public long getEnergyUsage() {
        return switch (this.variant) {
            case B_64 -> ModInitializer.getConfig().getSourceStorageBlock().get64BSourceUsage();
            case B_256 -> ModInitializer.getConfig().getSourceStorageBlock().get256BSourceUsage();
            case B_1024 -> ModInitializer.getConfig().getSourceStorageBlock().get1024BSourceUsage();
            case B_8192 -> ModInitializer.getConfig().getSourceStorageBlock().get8192BSourceUsage();
            case B_65536 -> ModInitializer.getConfig().getSourceStorageBlock().get65536BSourceUsage();
            case B_262144 -> ModInitializer.getConfig().getSourceStorageBlock().get262144BSourceUsage();
            case B_1048576 -> ModInitializer.getConfig().getSourceStorageBlock().get1048576BSourceUsage();
            case B_8388608 -> ModInitializer.getConfig().getSourceStorageBlock().get8388608BSourceUsage();
            case INFINITE -> ModInitializer.getConfig().getSourceStorageBlock().getInfiniteSourceUsage();
        };
    }

    @Override
    public ResourceFactory getResourceFactory() {
        return SourceResourceFactory.INSTANCE;
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return BlockEntities.getSourceStorageBlock(this.variant);
    }

    @Override
    public MenuType<?> getMenuType() {
        return Menus.getSourceStorage();
    }
}
