package com.ultramega.refinedtypes.storage.source;

import com.ultramega.refinedtypes.type.source.SourceResource;
import com.ultramega.refinedtypes.type.source.SourceResourceType;

import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceContainer;
import com.refinedmods.refinedstorage.common.support.resource.ItemResource;

import com.hollingsworth.arsnouveau.api.source.ISourceCap;
import net.minecraft.world.item.ItemStack;

import static com.ultramega.refinedtypes.type.source.SourceResource.createSourceResource;

public record ResourceContainerSourceHandlerAdapter(ResourceContainer container) implements ISourceCap {
    @Override
    public int receiveSource(final int amount, final boolean simulate) {
        for (int i = 0; i < this.container.size(); i++) {
            final ResourceAmount currentResource = this.container.get(i);
            if (currentResource == null) {
                return (int) this.insertSourceInEmptySlot(i, amount, simulate);
            } else if (currentResource.resource() instanceof SourceResource) {
                final int received = (int) this.insertSourceInFilledSlot(i, amount, simulate, currentResource);
                if (received > 0) {
                    return received;
                }
            }
        }
        return 0;
    }

    private long insertSourceInFilledSlot(final int index,
                                          final long maxReceive,
                                          final boolean simulate,
                                          final ResourceAmount currentResource) {
        final long currentAmount = currentResource.amount();
        final long toInsert = Math.min(
            maxReceive,
            Math.max(this.container.getMaxAmount(currentResource.resource()), SourceResourceType.INSTANCE.getInterfaceExportLimit()) - currentAmount
        );
        if (toInsert <= 0) {
            return 0L;
        }
        if (!simulate) {
            this.container.set(index, new ResourceAmount(currentResource.resource(), currentAmount + toInsert));
        }
        return toInsert;
    }

    private long insertSourceInEmptySlot(final int tank,
                                         final long maxReceive,
                                         final boolean simulate) {
        final long toInsert = Math.min(
            maxReceive,
            Math.max(this.container.getMaxAmount(ItemResource.ofItemStack(ItemStack.EMPTY)), SourceResourceType.INSTANCE.getInterfaceExportLimit())
        );
        if (toInsert <= 0) {
            return 0L;
        }
        if (!simulate) {
            this.container.set(tank, new ResourceAmount(createSourceResource(), toInsert));
        }
        return toInsert;
    }

    @Override
    public int extractSource(final int drain, final boolean simulate) {
        if (drain <= 0) {
            return 0;
        }

        for (int i = 0; i < this.container.size(); i++) {
            final ResourceAmount resourceAmount = this.container.get(i);

            if (resourceAmount == null || !(resourceAmount.resource() instanceof SourceResource)) {
                continue;
            }

            final long available = resourceAmount.amount();
            if (available <= 0) {
                return 0;
            }

            final int toExtract = (int) Math.min(drain, available);
            if (!simulate) {
                this.container.shrink(i, toExtract);
            }
            return toExtract;
        }

        return 0;
    }

    @Override
    public void setSource(final int source) {
        // Unsupported
    }

    @Override
    public int getSource() {
        long amount = 0;
        for (int i = 0; i < this.container.size(); i++) {
            final ResourceAmount resourceAmount = this.container.get(i);
            if (resourceAmount != null && resourceAmount.resource() instanceof SourceResource) {
                amount += resourceAmount.amount();
            }
        }

        return Math.clamp(amount, 0, Integer.MAX_VALUE);
    }

    @Override
    public void setMaxSource(final int max) {
    }

    @Override
    public int getSourceCapacity() {
        long capacity = 0;
        for (int i = 0; i < this.container.size(); i++) {
            final ResourceAmount resource = this.container.get(i);
            if (resource == null || resource.resource() instanceof SourceResource) {
                capacity += Math.max(this.container.getMaxAmount(resource != null ? resource.resource() : ItemResource.ofItemStack(ItemStack.EMPTY)),
                    SourceResourceType.INSTANCE.getInterfaceExportLimit());
            }
        }

        return Math.clamp(capacity, 0, Integer.MAX_VALUE);
    }

    @Override
    public int getMaxExtract() {
        return Math.clamp(SourceResourceType.INSTANCE.getInterfaceExportLimit(), 0, Integer.MAX_VALUE);
    }

    @Override
    public int getMaxReceive() {
        return Math.clamp(SourceResourceType.INSTANCE.getInterfaceExportLimit(), 0, Integer.MAX_VALUE);
    }

    @Override
    public boolean canAcceptSource(final int source) {
        return this.receiveSource(source, true) > 0;
    }

    @Override
    public boolean canProvideSource(final int source) {
        return this.extractSource(source, true) > 0;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    @Override
    public boolean canExtract() {
        return true;
    }
}
