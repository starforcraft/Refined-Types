package com.ultramega.refinedtypes.type.soul;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.Actor;
import com.refinedmods.refinedstorage.api.storage.InsertableStorage;

import static com.ultramega.refinedtypes.type.soul.SoulUtil.toSoulAction;

public class SoulInsertableStorage implements InsertableStorage {
    private final SoulCapabilityCache capabilityCache;

    public SoulInsertableStorage(final SoulCapabilityCache capabilityCache) {
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
    public long insert(final ResourceKey resource, final long amount, final Action action, final Actor actor) {
        if (!(resource instanceof SoulResource)) {
            return 0;
        }
        return this.capabilityCache.getCapability()
            .map(handler -> handler.fill((int) Math.min(Integer.MAX_VALUE, amount), toSoulAction(action)))
            .orElse(0);
    }
}
