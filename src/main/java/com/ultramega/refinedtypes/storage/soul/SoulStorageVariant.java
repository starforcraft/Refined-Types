package com.ultramega.refinedtypes.storage.soul;

import com.ultramega.refinedtypes.registry.Items;

import com.refinedmods.refinedstorage.common.storage.StorageVariant;

import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;

import static com.ultramega.refinedtypes.RefinedTypesUtil.createRefinedTypesIdentifier;

public enum SoulStorageVariant implements StringRepresentable, StorageVariant {
    K_1(1L),
    K_8(8L),
    K_64(64L),
    K_512(512L),
    K_4096(4096L),
    K_32768(32768L),
    K_262144(262144L),
    K_2097152(2097152L),
    INFINITE(null);

    private final String name;
    private final ResourceLocation storageDiskId;
    private final ResourceLocation storageBlockId;
    private final ResourceLocation storagePartId;
    @Nullable
    private final Long capacity;

    SoulStorageVariant(@Nullable final Long capacity) {
        this.name = capacity == null ? "infinite" : capacity + "k";
        this.storagePartId = createRefinedTypesIdentifier(this.name + "_soul_storage_part");
        this.storageDiskId = createRefinedTypesIdentifier(this.name + "_soul_storage_disk");
        this.storageBlockId = createRefinedTypesIdentifier(this.name + "_soul_storage_block");
        this.capacity = capacity != null ? capacity * 1024 : null;
    }

    @Override
    @Nullable
    public Long getCapacity() {
        return this.capacity;
    }

    @Override
    public Item getStoragePart() {
        return Items.getSoulStoragePart(this);
    }

    public ResourceLocation getStorageDiskId() {
        return this.storageDiskId;
    }

    public ResourceLocation getStorageBlockId() {
        return this.storageBlockId;
    }

    public ResourceLocation getStoragePartId() {
        return this.storagePartId;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
