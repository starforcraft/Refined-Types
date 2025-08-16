package com.ultramega.refinedtypes.storage.soul;

import com.ultramega.refinedtypes.type.soul.SoulResource;
import com.ultramega.refinedtypes.type.soul.SoulResourceType;

import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceContainer;

import com.buuz135.industrialforegoingsouls.capabilities.ISoulHandler;

import static com.ultramega.refinedtypes.type.soul.SoulResource.createSoulResource;
import static com.ultramega.refinedtypes.type.soul.SoulUtil.fromSoulAction;

public record SoulStorageInterface(ResourceContainer container) implements ISoulHandler {
    @Override
    public int fill(final int amount, final Action action) {
        return 0;
    }

    @Override
    public int drain(final int maxDrain, final Action action) {
        if (maxDrain <= 0) {
            return 0;
        }

        return (int) this.container.extract(createSoulResource(), maxDrain, fromSoulAction(action));
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
            return (int) SoulResourceType.INSTANCE.getInterfaceExportLimit();
        }
        return 0;
    }
}
