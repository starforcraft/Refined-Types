package com.ultramega.refinedtypes.autocrafting;

import com.ultramega.refinedtypes.type.TypeStack;
import com.ultramega.refinedtypes.type.source.SourceCapabilityCache;
import com.ultramega.refinedtypes.type.source.SourceResource;

import com.refinedmods.refinedstorage.api.autocrafting.task.ExternalPatternSink;
import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.common.api.autocrafting.PlatformPatternProviderExternalPatternSink;

import java.util.Collection;

import com.hollingsworth.arsnouveau.api.source.ISourceCap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class SourcePatternProviderExternalPatternSink implements PlatformPatternProviderExternalPatternSink {
    private static final Logger LOGGER = LoggerFactory.getLogger(SourcePatternProviderExternalPatternSink.class);

    private final SourceCapabilityCache capabilityCache;

    SourcePatternProviderExternalPatternSink(final SourceCapabilityCache capabilityCache) {
        this.capabilityCache = capabilityCache;
    }

    @Override
    public ExternalPatternSink.Result accept(final Collection<ResourceAmount> resources, final Action action) {
        return this.capabilityCache.getCapability()
            .map(handler -> this.accept(resources, action, handler))
            .orElse(ExternalPatternSink.Result.SKIPPED);
    }

    private ExternalPatternSink.Result accept(final Collection<ResourceAmount> resources,
                                              final Action action,
                                              final ISourceCap handler) {
        for (final ResourceAmount resource : resources) {
            if (resource.resource() instanceof SourceResource sourceResource
                && !this.accept(action, handler, resource.amount(), sourceResource)) {
                return ExternalPatternSink.Result.REJECTED;
            }
        }
        return ExternalPatternSink.Result.ACCEPTED;
    }

    private boolean accept(final Action action,
                           final ISourceCap handler,
                           final long amount,
                           final SourceResource sourceResource) {
        final long inserted = handler.receiveSource((int) amount, action == Action.SIMULATE);
        if (inserted != amount) {
            if (action == Action.EXECUTE) {
                LOGGER.warn(
                    "{} unexpectedly didn't accept all of {}, the remainder has been voided",
                    handler,
                    new TypeStack(sourceResource.type(), amount)
                );
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean isEmpty() {
        return this.capabilityCache.getCapability()
            .map(handler -> handler.getSource() == 0)
            .orElse(true);
    }
}
