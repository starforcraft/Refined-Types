package com.ultramega.refinedtypes.type.essentia;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.Actor;
import com.refinedmods.refinedstorage.api.storage.ExtractableStorage;

import com.google.common.primitives.Ints;
import com.leclowndu93150.thaumaturge.api.essentia.IEssentiaStorage;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class EssentiaExtractableStorage implements ExtractableStorage {
    private final EssentiaCapabilityCache capabilityCache;

    public EssentiaExtractableStorage(final EssentiaCapabilityCache capabilityCache) {
        this.capabilityCache = capabilityCache;
    }

    public long getAmount(final ResourceKey resource) {
        if (!(resource instanceof EssentiaResource essentia)) {
            return 0;
        }
        return this.capabilityCache.getCapability()
            .map(handler -> {
                final var aspect = essentia.resolve(this.capabilityCache.getLevel().registryAccess());
                return aspect == null ? 0L : (long) handler.amount(aspect);
            })
            .orElse(0L);
    }

    @Override
    public long extract(final ResourceKey resource, final long amount, final Action action, final Actor actor) {
        if (!(resource instanceof EssentiaResource essentia)) {
            return 0;
        }
        return this.capabilityCache.getCapability()
            .map(handler -> this.extract(essentia, amount, action, handler))
            .orElse(0L);
    }

    @SuppressWarnings("deprecation")
    private long extract(final EssentiaResource resource, final long amount, final Action action, final IEssentiaStorage handler) {
        if (amount <= 0) {
            return 0;
        }
        final var aspect = resource.resolve(this.capabilityCache.getLevel().registryAccess());
        if (aspect == null) {
            return 0;
        }
        final TransactionContext potentialOpenTransactionFromEarlierInTheStack = Transaction.getCurrentOpenedTransaction();
        try (Transaction tx = Transaction.open(potentialOpenTransactionFromEarlierInTheStack)) {
            final int extracted = handler.extract(aspect, Ints.saturatedCast(amount), tx);
            if (action == Action.EXECUTE) {
                tx.commit();
            }
            return extracted;
        }
    }
}
