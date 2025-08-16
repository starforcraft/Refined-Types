package com.ultramega.refinedtypes.storage.energy;

import com.ultramega.refinedtypes.registry.Items;

import com.refinedmods.refinedstorage.common.storage.StorageVariant;

import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;

import static com.ultramega.refinedtypes.RefinedTypesUtil.createRefinedTypesIdentifier;

public enum EnergyStorageVariant implements StringRepresentable, StorageVariant {
    K_64(64_000L),
    K_256(256_000L),
    K_1024(1_024_000L),
    K_8192(8_192_000L),
    K_65536(65_536_000L),
    K_262144(262_144_000L),
    K_1048576(1_048_576_000L),
    K_8388608(8_388_608_000L),
    INFINITE(null);

    private final String name;
    private final ResourceLocation storageDiskId;
    private final ResourceLocation storageBlockId;
    private final ResourceLocation storagePartId;
    @Nullable
    private final Long capacity;

    EnergyStorageVariant(@Nullable final Long capacity) {
        this.name = capacity == null ? "infinite" : capacity / 1000 + "k";
        this.storagePartId = createRefinedTypesIdentifier(this.name + "_energy_storage_part");
        this.storageDiskId = createRefinedTypesIdentifier(this.name + "_energy_storage_disk");
        this.storageBlockId = createRefinedTypesIdentifier(this.name + "_energy_storage_block");
        this.capacity = capacity;
    }

    @Override
    @Nullable
    public Long getCapacity() {
        return this.capacity;
    }

    @Override
    public Item getStoragePart() {
        return Items.getEnergyStoragePart(this);
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
