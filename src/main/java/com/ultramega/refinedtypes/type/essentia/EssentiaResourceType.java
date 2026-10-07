package com.ultramega.refinedtypes.type.essentia;

import com.ultramega.refinedtypes.storage.CreativeStorageImpl;
import com.ultramega.refinedtypes.storage.ImprovedResourceStorageType;

import com.refinedmods.refinedstorage.api.network.impl.node.grid.GridOperationsImpl;
import com.refinedmods.refinedstorage.api.network.node.grid.GridOperations;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.Actor;
import com.refinedmods.refinedstorage.api.storage.root.RootStorage;
import com.refinedmods.refinedstorage.common.api.support.resource.PlatformResourceKey;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceType;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

public enum EssentiaResourceType implements ResourceType {
    INSTANCE;

    public static final long DEFAULT_TRANSFER_AMOUNT = 64L;
    public static final MapCodec<EssentiaResource> MAP_CODEC = RecordCodecBuilder.mapCodec(ins -> ins.group(
        Identifier.CODEC.fieldOf("aspect").forGetter(EssentiaResource::aspectId)
    ).apply(ins, EssentiaResource::new));
    public static final Codec<EssentiaResource> CODEC = MAP_CODEC.codec();
    public static final StreamCodec<RegistryFriendlyByteBuf, EssentiaResource> STREAM_CODEC = StreamCodec.composite(
        Identifier.STREAM_CODEC, EssentiaResource::aspectId,
        EssentiaResource::new
    );
    public static final Codec<ResourceKey> NATIVE_CODEC = CODEC.xmap(
        essentiaResource -> essentiaResource,
        resourceKey -> {
            if (resourceKey instanceof EssentiaResource essentiaResource) {
                return essentiaResource;
            }
            throw new IllegalArgumentException("Expected EssentiaResource");
        }
    );
    public static final ImprovedResourceStorageType STORAGE_TYPE = new ImprovedResourceStorageType(
        NATIVE_CODEC,
        EssentiaResource.class::isInstance,
        DEFAULT_TRANSFER_AMOUNT,
        DEFAULT_TRANSFER_AMOUNT * 100L,
        () -> new CreativeStorageImpl(EssentiaResource::allRegistered)
    );

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
    public long normalizeAmount(final double amount) {
        return (long) (amount);
    }

    @Override
    public double getDisplayAmount(final long amount) {
        return amount;
    }

    @Override
    public long getInterfaceExportLimit() {
        return DEFAULT_TRANSFER_AMOUNT;
    }

    @Override
    public GridOperations createGridOperations(final RootStorage rootStorage, final Actor actor) {
        return new GridOperationsImpl(rootStorage, actor, resource -> Long.MAX_VALUE, DEFAULT_TRANSFER_AMOUNT);
    }
}
