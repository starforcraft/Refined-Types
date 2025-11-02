package com.ultramega.refinedtypes.storage.soul;

import com.ultramega.refinedtypes.type.soul.SoulResource;
import com.ultramega.refinedtypes.type.soul.SoulResourceType;

import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceContainer;
import com.refinedmods.refinedstorage.common.support.resource.ItemResource;

import com.buuz135.industrialforegoingsouls.capabilities.ISoulHandler;
import net.minecraft.world.item.ItemStack;

import static com.ultramega.refinedtypes.type.soul.SoulResource.createSoulResource;

public record ResourceContainerSoulHandlerAdapter(ResourceContainer container) implements ISoulHandler {
    @Override
    public int fill(final int amount, final Action action) {
        for (int i = 0; i < this.container.size(); i++) {
            final ResourceAmount currentResource = this.container.get(i);
            if (currentResource == null) {
                return (int) this.insertSoulInEmptySlot(i, amount, action);
            } else if (currentResource.resource() instanceof SoulResource) {
                final int received = (int) this.insertSoulInFilledSlot(i, amount, action, currentResource);
                if (received > 0) {
                    return received;
                }
            }
        }
        return 0;
    }

    private long insertSoulInFilledSlot(final int index,
                                        final long maxReceive,
                                        final Action action,
                                        final ResourceAmount currentResource) {
        final long currentAmount = currentResource.amount();
        final long toInsert = Math.min(
            maxReceive,
            Math.max(this.container.getMaxAmount(currentResource.resource()), SoulResourceType.INSTANCE.getInterfaceExportLimit()) - currentAmount
        );
        if (toInsert <= 0) {
            return 0L;
        }
        if (action == Action.EXECUTE) {
            this.container.set(index, new ResourceAmount(currentResource.resource(), currentAmount + toInsert));
        }
        return toInsert;
    }

    private long insertSoulInEmptySlot(final int tank,
                                       final long maxReceive,
                                       final Action action) {
        final long toInsert = Math.min(
            maxReceive,
            Math.max(this.container.getMaxAmount(ItemResource.ofItemStack(ItemStack.EMPTY)), SoulResourceType.INSTANCE.getInterfaceExportLimit())
        );
        if (toInsert <= 0) {
            return 0L;
        }
        if (action == Action.EXECUTE) {
            this.container.set(tank, new ResourceAmount(createSoulResource(), toInsert));
        }
        return toInsert;
    }

    @Override
    public int drain(final int maxDrain, final Action action) {
        if (maxDrain <= 0) {
            return 0;
        }

        for (int i = 0; i < this.container.size(); i++) {
            final ResourceAmount resourceAmount = this.container.get(i);

            if (resourceAmount == null || !(resourceAmount.resource() instanceof SoulResource)) {
                continue;
            }

            final long available = resourceAmount.amount();
            if (available <= 0) {
                return 0;
            }

            final int toExtract = (int) Math.min(maxDrain, available);
            if (action == Action.EXECUTE) {
                this.container.shrink(i, toExtract);
            }
            return toExtract;
        }

        return 0;
    }

    @Override
    public int getSoulTanks() {
        return this.container.size();
    }

    @Override
    public int getSoulInTank(final int tank) {
        final ResourceAmount resourceAmount = this.container.get(tank);
        if (resourceAmount == null || !(resourceAmount.resource() instanceof SoulResource)) {
            return 0;
        }
        return (int) resourceAmount.amount();
    }

    @Override
    public int getTankCapacity(final int tank) {
        final ResourceKey resource = this.container.getResource(tank);
        if (resource == null || resource instanceof SoulResource) {
            return (int) Math.max(this.container.getMaxAmount(resource != null ? resource : ItemResource.ofItemStack(ItemStack.EMPTY)),
                SoulResourceType.INSTANCE.getInterfaceExportLimit());
        }
        return 0;
    }
}
