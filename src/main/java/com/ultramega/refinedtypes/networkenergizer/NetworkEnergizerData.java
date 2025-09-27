package com.ultramega.refinedtypes.networkenergizer;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record NetworkEnergizerData(long stored, long capacity) {
    public static final StreamCodec<RegistryFriendlyByteBuf, NetworkEnergizerData> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_LONG, NetworkEnergizerData::stored,
        ByteBufCodecs.VAR_LONG, NetworkEnergizerData::capacity,
        NetworkEnergizerData::new
    );
}
