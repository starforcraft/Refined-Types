package com.ultramega.refinedtypes.autocrafting;

import com.ultramega.refinedtypes.type.TypeStack;
import com.ultramega.refinedtypes.type.soul.SoulCapabilityCache;
import com.ultramega.refinedtypes.type.soul.SoulResource;

import com.refinedmods.refinedstorage.api.autocrafting.task.ExternalPatternSink;
import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.common.api.autocrafting.PlatformPatternProviderExternalPatternSink;

import java.util.Collection;

import com.buuz135.industrialforegoingsouls.capabilities.ISoulHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ultramega.refinedtypes.type.soul.SoulUtil.toSoulAction;

class SoulPatternProviderExternalPatternSink implements PlatformPatternProviderExternalPatternSink {
    private static final Logger LOGGER = LoggerFactory.getLogger(SoulPatternProviderExternalPatternSink.class);

    private final SoulCapabilityCache capabilityCache;

    SoulPatternProviderExternalPatternSink(final SoulCapabilityCache capabilityCache) {
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
                                              final ISoulHandler handler) {
        for (final ResourceAmount resource : resources) {
            if (resource.resource() instanceof SoulResource soulResource
                && !this.accept(action, handler, resource.amount(), soulResource)) {
                return ExternalPatternSink.Result.REJECTED;
            }
        }
        return ExternalPatternSink.Result.ACCEPTED;
    }

    private boolean accept(final Action action,
                           final ISoulHandler handler,
                           final long amount,
                           final SoulResource soulResource) {
        final long inserted = handler.fill((int) amount, toSoulAction(action));
        if (inserted != amount) {
            if (action == Action.EXECUTE) {
                LOGGER.warn(
                    "{} unexpectedly didn't accept all of {}, the remainder has been voided",
                    handler,
                    new TypeStack(soulResource.type(), amount)
                );
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean isEmpty() {
        return this.capabilityCache.getCapability()
            .map(handler -> handler.getSoulInTank(0) == 0)
            .orElse(true);
    }
}
