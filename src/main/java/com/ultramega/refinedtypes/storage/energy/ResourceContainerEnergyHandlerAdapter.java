package com.ultramega.refinedtypes.storage.energy;

import com.ultramega.refinedtypes.type.energy.EnergyResource;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceContainer;
import com.refinedmods.refinedstorage.common.support.resource.ResourceTypes;

import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import static com.ultramega.refinedtypes.type.energy.EnergyResource.ENERGY_RESOURCE;

public class ResourceContainerEnergyHandlerAdapter extends SnapshotJournal<ResourceContainer> implements EnergyHandler {
    private final ResourceContainer container;

    public ResourceContainerEnergyHandlerAdapter(final ResourceContainer container) {
        this.container = container;
    }

    @Override
    public int insert(final int amount, final TransactionContext transaction) {
        if (amount <= 0) {
            return 0;
        }
        final long insertedSimulated = this.container.insert(ENERGY_RESOURCE, amount, Action.SIMULATE);
        if (insertedSimulated > 0) {
            this.updateSnapshots(transaction);
        }
        return (int) this.container.insert(ENERGY_RESOURCE, amount, Action.EXECUTE);
    }

    @Override
    public int extract(final int amount, final TransactionContext transaction) {
        if (amount <= 0) {
            return 0;
        }
        final long extractedSimulated = this.container.extract(ENERGY_RESOURCE, amount, Action.SIMULATE);
        if (extractedSimulated > 0) {
            this.updateSnapshots(transaction);
        }
        return (int) this.container.extract(ENERGY_RESOURCE, amount, Action.EXECUTE);
    }

    @Override
    public long getAmountAsLong() {
        long amount = 0;
        for (int i = 0; i < this.container.size(); i++) {
            final ResourceAmount resourceAmount = this.container.get(i);
            if (resourceAmount != null && resourceAmount.resource() instanceof EnergyResource) {
                amount += resourceAmount.amount();
            }
        }

        return amount;
    }

    @Override
    public long getCapacityAsLong() {
        return ResourceTypes.FLUID.getInterfaceExportLimit();
    }

    @Override
    protected ResourceContainer createSnapshot() {
        return this.container.copy();
    }

    @Override
    protected void revertToSnapshot(final ResourceContainer snapshot) {
        for (int i = 0; i < snapshot.size(); ++i) {
            final ResourceAmount snapshotSlot = snapshot.get(i);
            if (snapshotSlot == null) {
                this.container.remove(i);
            } else {
                this.container.set(i, snapshotSlot);
            }
        }
    }
}
