package com.ultramega.refinedtypes.autocrafting;

import com.ultramega.refinedtypes.type.soul.SoulCapabilityCache;
import com.ultramega.refinedtypes.type.soul.SoulResource;

import com.refinedmods.refinedstorage.api.autocrafting.task.ExternalPatternSink;
import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.common.api.autocrafting.PlatformPatternProviderExternalPatternSink;

import java.util.Collection;

import com.buuz135.industrialforegoingsouls.block_network.SoulNetwork;

import static com.ultramega.refinedtypes.type.soul.SoulUtil.getNetwork;

class SoulPatternProviderExternalPatternSink implements PlatformPatternProviderExternalPatternSink {
    private final SoulCapabilityCache capabilityCache;

    SoulPatternProviderExternalPatternSink(final SoulCapabilityCache capabilityCache) {
        this.capabilityCache = capabilityCache;
    }

    @Override
    public ExternalPatternSink.Result accept(final Collection<ResourceAmount> resources, final Action action) {
        for (final ResourceAmount resource : resources) {
            if (resource.resource() instanceof SoulResource && !this.accept(action, resource.amount())) {
                return ExternalPatternSink.Result.REJECTED;
            }
        }
        return ExternalPatternSink.Result.ACCEPTED;
    }

    private boolean accept(final Action action,
                           final long amount) {
        final SoulNetwork network = getNetwork(this.capabilityCache);
        if (network != null) {
            if (action == Action.EXECUTE) {
                network.addSouls(this.capabilityCache.getLevel(), (int) amount);
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean isEmpty() {
        final SoulNetwork network = getNetwork(this.capabilityCache);
        if (network != null) {
            return network.getSoulAmount() == 0;
        }
        return true;
    }
}
