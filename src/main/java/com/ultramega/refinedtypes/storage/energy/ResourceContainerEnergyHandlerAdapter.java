package com.ultramega.refinedtypes.storage.energy;

import com.ultramega.refinedtypes.type.energy.EnergyResource;
import com.ultramega.refinedtypes.type.energy.EnergyResourceType;

import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceContainer;

import dev.technici4n.grandpower.api.ILongEnergyStorage;

import static com.ultramega.refinedtypes.type.energy.EnergyResource.createEnergyResource;

public class ResourceContainerEnergyHandlerAdapter implements ILongEnergyStorage { //TODO: abstract this with ResourceContainerSoulHandlerAdapter
    private final ResourceContainer container;

    public ResourceContainerEnergyHandlerAdapter(final ResourceContainer container) {
        this.container = container;
    }

    @Override
    public long receive(final long maxReceive, final boolean simulate) {
        if (maxReceive <= 0) {
            return 0L;
        }

        for (int i = 0; i < this.container.size(); i++) {
            final ResourceAmount currentResource = this.container.get(i);
            if (currentResource == null) {
                return this.insertEnergyInEmptySlot(i, maxReceive, simulate);
            } else if (currentResource.resource() instanceof EnergyResource) {
                final long received = this.insertEnergyInFilledSlot(i, maxReceive, simulate, currentResource);
                if (received > 0) {
                    return received;
                }
            }
        }

        return 0L;
    }

    private long insertEnergyInFilledSlot(final int index,
                                          final long maxReceive,
                                          final boolean simulate,
                                          final ResourceAmount currentResource) {
        final long currentAmount = currentResource.amount();
        final long toInsert = Math.min(
            maxReceive,
            EnergyResourceType.INSTANCE.getInterfaceExportLimit() - currentAmount
        );
        if (toInsert <= 0) {
            return 0L;
        }
        if (!simulate) {
            this.container.set(index, new ResourceAmount(currentResource.resource(), currentAmount + toInsert));
        }
        return toInsert;
    }

    private long insertEnergyInEmptySlot(final int tank,
                                         final long maxReceive,
                                         final boolean simulate) {
        final long toInsert = Math.min(
            maxReceive,
            EnergyResourceType.INSTANCE.getInterfaceExportLimit()
        );
        if (!simulate) {
            this.container.set(tank, new ResourceAmount(createEnergyResource(), toInsert));
        }
        return toInsert;
    }

    @Override
    public long extract(final long maxExtract, final boolean simulate) {
        if (maxExtract <= 0) {
            return 0L;
        }

        for (int i = 0; i < this.container.size(); i++) {
            final ResourceAmount resourceAmount = this.container.get(i);

            if (resourceAmount == null || !(resourceAmount.resource() instanceof EnergyResource)) {
                continue;
            }

            final long available = resourceAmount.amount();
            if (available <= 0) {
                return 0L;
            }

            final long toExtract = Math.min(maxExtract, available);
            if (!simulate) {
                this.container.shrink(i, toExtract);
            }
            return toExtract;
        }

        return 0L;
    }

    @Override
    public long getAmount() {
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
    public long getCapacity() {
        long capacity = 0;
        for (int i = 0; i < this.container.size(); i++) {
            final ResourceAmount resourceAmount = this.container.get(i);
            if (resourceAmount == null || resourceAmount.resource() instanceof EnergyResource) {
                capacity += EnergyResourceType.INSTANCE.getInterfaceExportLimit();
            }
        }

        return capacity;
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
