package com.ultramega.refinedtypes.type.soul;

import com.ultramega.refinedtypes.RefinedTypesUtil;
import com.ultramega.refinedtypes.registry.Types;

import com.refinedmods.refinedstorage.api.network.impl.node.grid.GridOperationsImpl;
import com.refinedmods.refinedstorage.api.network.node.grid.GridOperations;
import com.refinedmods.refinedstorage.api.storage.Actor;
import com.refinedmods.refinedstorage.api.storage.root.RootStorage;
import com.refinedmods.refinedstorage.common.Platform;
import com.refinedmods.refinedstorage.common.api.storage.StorageType;
import com.refinedmods.refinedstorage.common.api.support.resource.PlatformResourceKey;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceType;
import com.refinedmods.refinedstorage.common.storage.SameTypeStorageType;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import static com.ultramega.refinedtypes.RefinedTypesUtil.createRefinedTypesIdentifier;

public enum SoulResourceType implements ResourceType {
    INSTANCE;

    public static final MapCodec<SoulResource> MAP_CODEC = RecordCodecBuilder.mapCodec(ins -> ins.group(
        Types.CODEC.fieldOf("soul").forGetter(SoulResource::type)
    ).apply(ins, SoulResource::new));
    public static final Codec<SoulResource> CODEC = MAP_CODEC.codec();
    public static final StreamCodec<RegistryFriendlyByteBuf, SoulResource> STREAM_CODEC = StreamCodec.composite(
        Types.STREAM_CODEC, SoulResource::type,
        SoulResource::new
    );
    public static final StorageType STORAGE_TYPE = new SameTypeStorageType<>(
        CODEC,
        resource -> resource instanceof SoulResource,
        SoulResource.class::cast,
        Platform.INSTANCE.getBucketAmount(),
        Platform.INSTANCE.getBucketAmount() * 64
    );

    private static final MutableComponent TITLE = RefinedTypesUtil.createRefinedTypesTranslation(
        "misc",
        "resource_type.soul"
    );
    private static final ResourceLocation SPRITE = createRefinedTypesIdentifier("soul_resource_type");

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public MapCodec<PlatformResourceKey> getMapCodec() {
        return (MapCodec) MAP_CODEC;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public StreamCodec<RegistryFriendlyByteBuf, PlatformResourceKey> getStreamCodec() {
        return (StreamCodec) STREAM_CODEC;
    }

    @Override
    public MutableComponent getTitle() {
        return TITLE;
    }

    @Override
    public ResourceLocation getSprite() {
        return SPRITE;
    }

    @Override
    public long normalizeAmount(final double amount) {
        return (long) (amount);
    }

    @Override
    public double getDisplayAmount(final long amount) {
        return amount;
    }

    @Override
    public long getInterfaceExportLimit() {
        return 64;
    }

    @Override
    public GridOperations createGridOperations(final RootStorage rootStorage, final Actor actor) {
        return new GridOperationsImpl(
            rootStorage,
            actor,
            resource -> Long.MAX_VALUE,
            Platform.INSTANCE.getBucketAmount()
        );
    }
}
