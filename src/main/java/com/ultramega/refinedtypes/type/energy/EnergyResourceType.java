package com.ultramega.refinedtypes.type.energy;

import com.ultramega.refinedtypes.registry.Types;
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

import static com.ultramega.refinedtypes.ModInitializer.ENERGY_ID;
import static com.ultramega.refinedtypes.type.energy.EnergyResource.ENERGY_RESOURCE;

public enum EnergyResourceType implements ResourceType {
    INSTANCE;

    public static final long DEFAULT_TRANSFER_AMOUNT = 1_000L;
    public static final MapCodec<EnergyResource> MAP_CODEC = RecordCodecBuilder.mapCodec(ins -> ins.group(
        Types.CODEC.fieldOf(ENERGY_ID.getPath()).forGetter(EnergyResource::type)
    ).apply(ins, EnergyResource::new));
    public static final Codec<EnergyResource> CODEC = MAP_CODEC.codec();
    public static final StreamCodec<RegistryFriendlyByteBuf, EnergyResource> STREAM_CODEC = StreamCodec.composite(
        Types.STREAM_CODEC, EnergyResource::type,
        EnergyResource::new
    );
    public static final Codec<ResourceKey> NATIVE_CODEC = CODEC.xmap(
        energyResource -> energyResource,
        resourceKey -> {
            if (resourceKey instanceof EnergyResource energyResource) {
                return energyResource;
            }
            throw new IllegalArgumentException("Expected EnergyResource");
        }
    );
    public static final ImprovedResourceStorageType STORAGE_TYPE = new ImprovedResourceStorageType(
        NATIVE_CODEC,
        EnergyResource.class::isInstance,
        ENERGY_RESOURCE,
        DEFAULT_TRANSFER_AMOUNT,
        DEFAULT_TRANSFER_AMOUNT * 100L
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
        return new GridOperationsImpl(
            rootStorage,
            actor,
            resource -> Long.MAX_VALUE,
            DEFAULT_TRANSFER_AMOUNT
        );
    }
}
