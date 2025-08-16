package com.ultramega.refinedtypes.storage.energy;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.network.Network;
import com.refinedmods.refinedstorage.api.network.storage.StorageNetworkComponent;
import com.refinedmods.refinedstorage.api.storage.Actor;
import com.refinedmods.refinedstorage.common.iface.InterfaceBlockEntity;

import dev.technici4n.grandpower.api.ILongEnergyStorage;

import static com.ultramega.refinedtypes.type.energy.EnergyResource.createEnergyResource;

public record EnergyStorageInterface(InterfaceBlockEntity interfaceBlockEntity) implements ILongEnergyStorage {
    @Override
    public long receive(final long maxReceive, final boolean simulate) {
        final Network network = this.interfaceBlockEntity.getNetworkForItem();
        if (network != null) {
            final StorageNetworkComponent storageComponent = this.interfaceBlockEntity.getNetworkForItem().getComponent(StorageNetworkComponent.class);
            return storageComponent.insert(createEnergyResource(), maxReceive, simulate ? Action.SIMULATE : Action.EXECUTE, Actor.EMPTY);
        }
        return 0;
    }

    @Override
    public long extract(final long maxExtract, final boolean simulate) {
        final Network network = this.interfaceBlockEntity.getNetworkForItem();
        if (network != null) {
            final StorageNetworkComponent storageComponent = this.interfaceBlockEntity.getNetworkForItem().getComponent(StorageNetworkComponent.class);
            return storageComponent.extract(createEnergyResource(), maxExtract, simulate ? Action.SIMULATE : Action.EXECUTE, Actor.EMPTY);
        }
        return 0;
    }

    @Override
    public long getAmount() {
        final Network network = this.interfaceBlockEntity.getNetworkForItem();
        if (network != null) {
            final StorageNetworkComponent storageComponent = this.interfaceBlockEntity.getNetworkForItem().getComponent(StorageNetworkComponent.class);
            return storageComponent.get(createEnergyResource());
        }
        return 0;
    }

    @Override
    public long getCapacity() {
        return Long.MAX_VALUE;
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return true;
    }
}
