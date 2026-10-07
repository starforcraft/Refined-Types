package com.ultramega.refinedtypes.storage.essentia;

import com.ultramega.refinedtypes.registry.Items;

import com.refinedmods.refinedstorage.common.storage.StorageVariant;

import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.Nullable;

import static com.ultramega.refinedtypes.RefinedTypesUtil.createRefinedTypesIdentifier;

public enum EssentiaStorageVariant implements StringRepresentable, StorageVariant {
    K_64(64_000L),
    K_256(256_000L),
    K_1024(1_024_000L),
    K_8192(8_192_000L),
    K_65536(65_536_000L),
    K_262144(262_144_000L),
    K_1048576(1_048_576_000L),
    K_8388608(8_388_608_000L),
    INFINITE(-1L, "infinite"),
    CREATIVE(null, "creative");

    private final String name;
    private final Identifier storageDiskId;
    private final Identifier storageBlockId;
    private final Identifier storagePartId;
    @Nullable
    private final Long capacity;

    EssentiaStorageVariant(final long capacity) {
        this(capacity, capacity / 1000 + "k");
    }

    EssentiaStorageVariant(@Nullable final Long capacity, final String name) {
        this.name = name;
        this.storagePartId = createRefinedTypesIdentifier(this.name + "_essentia_storage_part");
        this.storageDiskId = createRefinedTypesIdentifier(this.name + "_essentia_storage_disk");
        this.storageBlockId = createRefinedTypesIdentifier(this.name + "_essentia_storage_block");
        this.capacity = capacity;
    }

    @Override
    @Nullable
    public Long getCapacity() {
        return this.capacity;
    }

    @Override
    public Item getStoragePart() {
        return Items.getEssentiaStoragePart(this);
    }

    public Identifier getStorageDiskId() {
        return this.storageDiskId;
    }

    public Identifier getStorageBlockId() {
        return this.storageBlockId;
    }

    public Identifier getStoragePartId() {
        return this.storagePartId;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
