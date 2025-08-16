package com.ultramega.refinedtypes.autocrafting;

import com.ultramega.refinedtypes.type.energy.EnergyCapabilityCache;

import com.refinedmods.refinedstorage.common.api.autocrafting.PatternProviderExternalPatternSinkFactory;
import com.refinedmods.refinedstorage.common.api.autocrafting.PlatformPatternProviderExternalPatternSink;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;

public class EnergyPatternProviderExternalPatternSinkFactory implements PatternProviderExternalPatternSinkFactory {
    @Override
    public PlatformPatternProviderExternalPatternSink create(final ServerLevel level,
                                                             final BlockPos pos,
                                                             final Direction direction) {
        final EnergyCapabilityCache capabilityCache = new EnergyCapabilityCache(level, pos, direction);
        return new EnergyPatternProviderExternalPatternSink(capabilityCache);
    }
}
