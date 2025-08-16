package com.ultramega.refinedtypes.type.source;

import com.refinedmods.refinedstorage.api.core.NullableType;
import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;

import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;

import com.hollingsworth.arsnouveau.api.source.ISourceCap;
import com.hollingsworth.arsnouveau.setup.registry.CapabilityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;

import static com.ultramega.refinedtypes.type.source.SourceResource.createSourceResource;

public class SourceCapabilityCache {
    private final BlockCapabilityCache<ISourceCap, @NullableType Direction> cache;

    public SourceCapabilityCache(final ServerLevel level, final BlockPos pos, final Direction direction) {
        this.cache = BlockCapabilityCache.create(CapabilityRegistry.SOURCE_CAPABILITY, level, pos, direction);
    }

    public Optional<ISourceCap> getCapability() {
        return Optional.ofNullable(this.cache.getCapability());
    }

    public Iterator<ResourceAmount> createAmountIterator() {
        return this.getCapability().map(handler -> {
            final long amount = handler.getSource();
            if (amount > 0) {
                return Collections.singletonList(new ResourceAmount(createSourceResource(), amount)).iterator();
            }
            return Collections.<ResourceAmount>emptyIterator();
        }).orElse(Collections.emptyListIterator());
    }

    public Iterator<ResourceKey> createIterator() {
        return this.getCapability().map(handler -> {
            final long amount = handler.getSource();
            if (amount > 0) {
                return Collections.<ResourceKey>singletonList(createSourceResource()).iterator();
            }
            return Collections.<ResourceKey>emptyListIterator();
        }).orElse(Collections.emptyListIterator());
    }
}
