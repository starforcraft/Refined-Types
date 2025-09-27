package com.ultramega.refinedtypes.packet.s2c;

import com.ultramega.refinedtypes.networkenergizer.NetworkEnergizerContainerMenu;

import com.refinedmods.refinedstorage.common.support.packet.PacketContext;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.inventory.AbstractContainerMenu;

import static com.ultramega.refinedtypes.RefinedTypesUtil.createRefinedTypesIdentifier;

public record NetworkEnergizerEnergyInfoPacket(long stored, long capacity) implements CustomPacketPayload {
    public static final Type<NetworkEnergizerEnergyInfoPacket> PACKET_TYPE = new Type<>(createRefinedTypesIdentifier("network_energizer_energy_info"));
    public static final StreamCodec<RegistryFriendlyByteBuf, NetworkEnergizerEnergyInfoPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_LONG, NetworkEnergizerEnergyInfoPacket::stored,
        ByteBufCodecs.VAR_LONG, NetworkEnergizerEnergyInfoPacket::capacity,
        NetworkEnergizerEnergyInfoPacket::new
    );

    public static void handle(final NetworkEnergizerEnergyInfoPacket packet, final PacketContext ctx) {
        final AbstractContainerMenu menu = ctx.getPlayer().containerMenu;
        if (menu instanceof NetworkEnergizerContainerMenu energy) {
            energy.getEnergyInfo().setEnergy(packet.stored, packet.capacity);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_TYPE;
    }
}
