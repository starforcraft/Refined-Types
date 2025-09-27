package com.ultramega.refinedtypes.networkenergizer;

import com.ultramega.refinedtypes.registry.BlockEntities;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.network.energy.EnergyStorage;
import com.refinedmods.refinedstorage.api.network.impl.energy.EnergyStorageImpl;
import com.refinedmods.refinedstorage.common.support.containermenu.NetworkNodeExtendedMenuProvider;
import com.refinedmods.refinedstorage.common.support.energy.BlockEntityEnergyStorage;
import com.refinedmods.refinedstorage.common.support.network.AbstractBaseNetworkNodeContainerBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamEncoder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static com.ultramega.refinedtypes.ContentIdentification.NETWORK_ENERGIZER_NAME;

public class NetworkEnergizerBlockEntity extends AbstractBaseNetworkNodeContainerBlockEntity<NetworkEnergizerNetworkNode>
    implements NetworkNodeExtendedMenuProvider<NetworkEnergizerData> {

    public NetworkEnergizerBlockEntity(final BlockPos pos, final BlockState state) {
        super(BlockEntities.getNetworkEnergizer(), pos, state, new NetworkEnergizerNetworkNode());

        final EnergyStorage energyStorage = createEnergyStorage(this);
        energyStorage.receive(1000, Action.EXECUTE);
        this.mainNetworkNode.setEnergyStorage(energyStorage);
    }

    private static EnergyStorage createEnergyStorage(final BlockEntity blockEntity) {
        return new BlockEntityEnergyStorage(
            new EnergyStorageImpl(1000),
            blockEntity
        );
    }

    @Override
    public Component getName() {
        return this.overrideName(NETWORK_ENERGIZER_NAME);
    }

    @Override
    public AbstractContainerMenu createMenu(final int syncId, final Inventory inv, final Player player) {
        return new NetworkEnergizerContainerMenu(syncId, inv, this, player);
    }

    @Override
    public NetworkEnergizerData getMenuData() {
        // Unfortunately, it's currently not possible to find out the capacity of all energy disks in the network, that's why we just pass stored as the capacity instead
        return new NetworkEnergizerData(this.getStored(), this.getStored());
    }

    @Override
    public StreamEncoder<RegistryFriendlyByteBuf, NetworkEnergizerData> getMenuCodec() {
        return NetworkEnergizerData.STREAM_CODEC;
    }

    long getStored() {
        return this.mainNetworkNode.getStored();
    }

    long getCapacity() {
        // Unfortunately, it's currently not possible to find out the capacity of all energy disks in the network, that's why we just pass stored as the capacity instead
        return this.mainNetworkNode.getStored();
    }
}
