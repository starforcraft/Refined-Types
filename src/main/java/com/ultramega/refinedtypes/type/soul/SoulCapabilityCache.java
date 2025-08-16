package com.ultramega.refinedtypes.type.soul;

import com.refinedmods.refinedstorage.api.core.NullableType;
import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;

import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;
import javax.annotation.Nullable;

import com.buuz135.industrialforegoingsouls.capabilities.ISoulHandler;
import com.buuz135.industrialforegoingsouls.capabilities.SoulCapabilities;
import com.google.common.collect.AbstractIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;

import static com.ultramega.refinedtypes.type.soul.SoulResource.createSoulResource;

public class SoulCapabilityCache {
    private final BlockCapabilityCache<ISoulHandler, @NullableType Direction> cache;
    private final Level level;
    private final BlockPos pos;

    public SoulCapabilityCache(final ServerLevel level, final BlockPos pos, final Direction direction) {
        this.cache = BlockCapabilityCache.create(SoulCapabilities.BLOCK, level, pos, direction);
        this.level = level;
        this.pos = pos;
    }

    public Optional<ISoulHandler> getCapability() {
        return Optional.ofNullable(this.cache.getCapability());
    }

    public Level getLevel() {
        return this.level;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public Iterator<ResourceAmount> createAmountIterator() {
        return this.getCapability().map(handler -> (Iterator<ResourceAmount>) new AbstractIterator<ResourceAmount>() {
            private int index;

            @Nullable
            @Override
            protected ResourceAmount computeNext() {
                if (this.index > handler.getSoulTanks()) {
                    return this.endOfData();
                }
                for (; this.index < handler.getSoulTanks(); ++this.index) {
                    final int amount = handler.getSoulInTank(this.index);
                    if (amount > 0) {
                        this.index++;
                        return new ResourceAmount(createSoulResource(), amount);
                    }
                }
                return this.endOfData();
            }
        }).orElse(Collections.emptyListIterator());
    }

    public Iterator<ResourceKey> createIterator() {
        return this.getCapability().map(handler -> (Iterator<ResourceKey>) new AbstractIterator<ResourceKey>() {
            private int index;

            @Nullable
            @Override
            protected ResourceKey computeNext() {
                if (this.index > handler.getSoulTanks()) {
                    return this.endOfData();
                }
                for (; this.index < handler.getSoulTanks(); ++this.index) {
                    final int amount = handler.getSoulInTank(this.index);
                    if (amount > 0) {
                        this.index++;
                        return createSoulResource();
                    }
                }
                return this.endOfData();
            }
        }).orElse(Collections.emptyListIterator());
    }
}
