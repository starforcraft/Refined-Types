package com.ultramega.refinedtypes.autocrafting;

import com.ultramega.refinedtypes.type.TypeStack;
import com.ultramega.refinedtypes.type.energy.EnergyCapabilityCache;
import com.ultramega.refinedtypes.type.energy.EnergyResource;

import com.refinedmods.refinedstorage.api.autocrafting.task.ExternalPatternSink;
import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.common.api.autocrafting.PlatformPatternProviderExternalPatternSink;

import java.util.Collection;

import dev.technici4n.grandpower.api.ILongEnergyStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class EnergyPatternProviderExternalPatternSink implements PlatformPatternProviderExternalPatternSink {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnergyPatternProviderExternalPatternSink.class);

    private final EnergyCapabilityCache capabilityCache;

    EnergyPatternProviderExternalPatternSink(final EnergyCapabilityCache capabilityCache) {
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
                                              final ILongEnergyStorage handler) {
        for (final ResourceAmount resource : resources) {
            if (resource.resource() instanceof EnergyResource energyResource
                && !this.accept(action, handler, resource.amount(), energyResource)) {
                return ExternalPatternSink.Result.REJECTED;
            }
        }
        return ExternalPatternSink.Result.ACCEPTED;
    }

    private boolean accept(final Action action,
                           final ILongEnergyStorage handler,
                           final long amount,
                           final EnergyResource energyResource) {
        final long inserted = handler.receive(amount, action == Action.SIMULATE);
        if (inserted != amount) {
            if (action == Action.EXECUTE) {
                LOGGER.warn(
                    "{} unexpectedly didn't accept all of {}, the remainder has been voided",
                    handler,
                    new TypeStack(energyResource.type(), amount)
                );
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean isEmpty() {
        return this.capabilityCache.getCapability()
            .map(handler -> handler.getAmount() == 0)
            .orElse(true);
    }
}
