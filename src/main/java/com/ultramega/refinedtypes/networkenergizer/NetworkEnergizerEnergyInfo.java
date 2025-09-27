package com.ultramega.refinedtypes.networkenergizer;

import com.ultramega.refinedtypes.packet.s2c.NetworkEnergizerEnergyInfoPacket;

import com.refinedmods.refinedstorage.common.Platform;

import java.util.Collections;
import java.util.List;
import java.util.function.LongSupplier;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.createTranslation;
import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.format;

public class NetworkEnergizerEnergyInfo {
    private final Player player;
    private final LongSupplier storedSupplier;
    private final LongSupplier capacitySupplier;

    private long stored;
    private long capacity;

    private NetworkEnergizerEnergyInfo(final ServerPlayer player,
                       final LongSupplier storedSupplier,
                       final LongSupplier capacitySupplier) {
        this.player = player;
        this.storedSupplier = storedSupplier;
        this.capacitySupplier = capacitySupplier;
        this.stored = storedSupplier.getAsLong();
        this.capacity = capacitySupplier.getAsLong();
    }

    private NetworkEnergizerEnergyInfo(final Player player, final long stored, final long capacity) {
        this.player = player;
        this.storedSupplier = () -> 0L;
        this.capacitySupplier = () -> 0L;
        this.stored = stored;
        this.capacity = capacity;
    }

    public void detectChanges() {
        final long newStored = this.storedSupplier.getAsLong();
        final long newCapacity = this.capacitySupplier.getAsLong();
        final boolean changed = this.stored != newStored || this.capacity != newCapacity;
        if (changed) {
            this.setEnergy(newStored, newCapacity);
            Platform.INSTANCE.sendPacketToClient((ServerPlayer) this.player, new NetworkEnergizerEnergyInfoPacket(newStored, newCapacity));
        }
    }

    public void setEnergy(final long newStored, final long newCapacity) {
        this.stored = newStored;
        this.capacity = newCapacity;
    }

    public List<Component> createTooltip() {
        return Collections.singletonList(createTranslation(
            "gui",
            "autocrafting_preview.available",
            Component.literal(this.stored == Long.MAX_VALUE ? "∞" : format(this.stored))
                .withStyle(ChatFormatting.WHITE)
        ).withStyle(ChatFormatting.GRAY));
    }

    public double getPercentageFull() {
        return (double) this.stored / (double) this.capacity;
    }

    public static NetworkEnergizerEnergyInfo forServer(final Player player,
                                                       final LongSupplier storedSupplier,
                                                       final LongSupplier capacitySupplier) {
        return new NetworkEnergizerEnergyInfo((ServerPlayer) player, storedSupplier, capacitySupplier);
    }

    public static NetworkEnergizerEnergyInfo forClient(final Player player, final long stored, final long capacity) {
        return new NetworkEnergizerEnergyInfo(player, stored, capacity);
    }
}
