package com.ultramega.refinedtypes.networkenergizer;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.network.energy.EnergyProvider;
import com.refinedmods.refinedstorage.api.network.energy.EnergyStorage;
import com.refinedmods.refinedstorage.api.network.impl.node.AbstractNetworkNode;
import com.refinedmods.refinedstorage.api.network.storage.StorageNetworkComponent;
import com.refinedmods.refinedstorage.api.storage.Actor;

import javax.annotation.Nullable;

import static com.ultramega.refinedtypes.type.energy.EnergyResource.createEnergyResource;

public class NetworkEnergizerNetworkNode extends AbstractNetworkNode implements EnergyProvider {
    // This energy storage kick-starts the network when loading into a world, preventing it from shutting down
    // This ugly workaround is necessary because resources in the storage network are loaded after the network components receive any power.
    // As a result, users effectively get a free 1k FE buffer, and once the network exceeds that amount,
    // we have to pray to god that the disk drive initializes first before total energy usage surpasses the limit
    @Nullable
    private EnergyStorage energyStorage;

    public void setEnergyStorage(@Nullable final EnergyStorage energyStorage) {
        this.energyStorage = energyStorage;
    }

    @Override
    public long getStored() {
        if (!this.isActive()) {
            return 0;
        }

        if (this.network != null) {
            final StorageNetworkComponent storageComponent = this.network.getComponent(StorageNetworkComponent.class);
            long stored = storageComponent.get(createEnergyResource());
            if (stored <= 0 && this.energyStorage != null) {
                stored = this.energyStorage.getStored();
            }
            return stored;
        }

        return 0;
    }

    @Override
    public long getCapacity() {
        if (!this.isActive()) {
            return 0;
        }

        return Long.MAX_VALUE;
    }

    @Override
    public long extract(final long amount) {
        if (amount <= 0) {
            return 0;
        }

        if (this.network != null) {
            final StorageNetworkComponent storageComponent = this.network.getComponent(StorageNetworkComponent.class);
            long extracted = storageComponent.extract(createEnergyResource(), amount, Action.EXECUTE, Actor.EMPTY);
            if (extracted <= 0 && this.energyStorage != null) {
                extracted = this.energyStorage.extract(amount, Action.EXECUTE);
            }

            return extracted;
        }

        return 0;
    }

    @Override
    public long getEnergyUsage() {
        return 0L;
    }
}
