package com.ultramega.refinedtypes.storage.source;

import com.ultramega.refinedtypes.registry.Items;

import com.refinedmods.refinedstorage.common.Platform;
import com.refinedmods.refinedstorage.common.storage.StorageVariant;

import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;

import static com.ultramega.refinedtypes.RefinedTypesUtil.createRefinedTypesIdentifier;

public enum SourceStorageVariant implements StringRepresentable, StorageVariant {
    B_64(64L),
    B_256(256L),
    B_1024(1_024L),
    B_8192(8_192L),
    B_65536(65_536L),
    B_262144(262_144L),
    B_1048576(1_048_576L),
    B_8388608(8_388_608L),
    INFINITE(null);

    private final String name;
    private final ResourceLocation storageDiskId;
    private final ResourceLocation storageBlockId;
    private final ResourceLocation storagePartId;
    @Nullable
    private final Long capacityInBuckets;

    SourceStorageVariant(@Nullable final Long capacityInBuckets) {
        this.name = capacityInBuckets == null ? "infinite" : capacityInBuckets + "b";
        this.storagePartId = createRefinedTypesIdentifier(this.name + "_source_storage_part");
        this.storageDiskId = createRefinedTypesIdentifier(this.name + "_source_storage_disk");
        this.storageBlockId = createRefinedTypesIdentifier(this.name + "_source_storage_block");
        this.capacityInBuckets = capacityInBuckets;
    }

    @Nullable
    public Long getCapacityInBuckets() {
        return this.capacityInBuckets;
    }

    @Override
    @Nullable
    public Long getCapacity() {
        if (this.capacityInBuckets == null) {
            return null;
        }
        return this.capacityInBuckets * Platform.INSTANCE.getBucketAmount();
    }

    @Override
    public Item getStoragePart() {
        return Items.getSourceStoragePart(this);
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
