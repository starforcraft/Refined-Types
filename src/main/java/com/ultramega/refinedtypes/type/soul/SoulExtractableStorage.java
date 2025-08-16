package com.ultramega.refinedtypes.type.soul;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.Actor;
import com.refinedmods.refinedstorage.api.storage.ExtractableStorage;

import static com.ultramega.refinedtypes.type.soul.SoulUtil.toSoulAction;

public class SoulExtractableStorage implements ExtractableStorage {
    private final SoulCapabilityCache capabilityCache;

    public SoulExtractableStorage(final SoulCapabilityCache capabilityCache) {
        this.capabilityCache = capabilityCache;
    }

    public long getAmount(final ResourceKey resource) {
        if (!(resource instanceof SoulResource)) {
            return 0;
        }

        return this.capabilityCache.getCapability()
            .map(handler -> {
                long amount = 0;
                for (int i = 0; i < handler.getSoulTanks(); ++i) {
                    amount += handler.getSoulInTank(i);
                }
                return amount;
            })
            .orElse(0L);
    }

    @Override
    public long extract(final ResourceKey resource, final long amount, final Action action, final Actor actor) {
        if (!(resource instanceof SoulResource)) {
            return 0;
        }
        return this.capabilityCache.getCapability().map(handler ->
            handler.drain((int) amount, toSoulAction(action))
        ).orElse(0);
    }
}
