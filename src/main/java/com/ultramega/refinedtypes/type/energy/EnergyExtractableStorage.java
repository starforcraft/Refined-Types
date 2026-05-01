package com.ultramega.refinedtypes.type.energy;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.Actor;
import com.refinedmods.refinedstorage.api.storage.ExtractableStorage;

import com.google.common.primitives.Ints;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class EnergyExtractableStorage implements ExtractableStorage {
    private final EnergyCapabilityCache capabilityCache;

    public EnergyExtractableStorage(final EnergyCapabilityCache capabilityCache) {
        this.capabilityCache = capabilityCache;
    }

    public long getAmount(final ResourceKey resource) {
        if (!(resource instanceof EnergyResource)) {
            return 0;
        }
        return this.capabilityCache.getCapability()
            .map(EnergyHandler::getAmountAsLong)
            .orElse(0L);
    }

    @Override
    public long extract(final ResourceKey resource, final long amount, final Action action, final Actor actor) {
        if (!(resource instanceof EnergyResource)) {
            return 0;
        }
        return this.capabilityCache.getCapability()
            .map(handler -> this.extract(amount, action, handler))
            .orElse(0L);
    }

    @SuppressWarnings("deprecation")
    private long extract(final long amount, final Action action, final EnergyHandler handler) {
        final TransactionContext potentialOpenTransactionFromEarlierInTheStack = Transaction.getCurrentOpenedTransaction();
        try (Transaction tx = Transaction.open(potentialOpenTransactionFromEarlierInTheStack)) {
            final int extracted = handler.extract(Ints.saturatedCast(amount), tx);
            if (action == Action.EXECUTE) {
                tx.commit();
            }
            return extracted;
        }
    }
}
