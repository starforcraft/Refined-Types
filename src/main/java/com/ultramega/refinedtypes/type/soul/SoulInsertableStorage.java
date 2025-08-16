package com.ultramega.refinedtypes.type.soul;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.Actor;
import com.refinedmods.refinedstorage.api.storage.InsertableStorage;

import com.buuz135.industrialforegoingsouls.block_network.SoulNetwork;

import static com.ultramega.refinedtypes.type.soul.SoulUtil.getNetwork;

public class SoulInsertableStorage implements InsertableStorage {
    private final SoulCapabilityCache capabilityCache;

    public SoulInsertableStorage(final SoulCapabilityCache capabilityCache) {
        this.capabilityCache = capabilityCache;
    }

    public long getAmount(final ResourceKey resource) {
        if (!(resource instanceof SoulResource)) {
            return 0;
        }

        final SoulNetwork network = getNetwork(this.capabilityCache);
        if (network != null) {
            return network.getSoulAmount();
        }

        return 0;
    }

    @Override
    public long insert(final ResourceKey resource, final long amount, final Action action, final Actor actor) {
        if (!(resource instanceof SoulResource)) {
            return 0;
        }

        final SoulNetwork network = getNetwork(this.capabilityCache);
        if (network != null) {
            if (action == Action.EXECUTE) {
                network.addSouls(this.capabilityCache.getLevel(), (int) amount);
            }

            return (int) amount;
        }

        return 0;
    }
}
