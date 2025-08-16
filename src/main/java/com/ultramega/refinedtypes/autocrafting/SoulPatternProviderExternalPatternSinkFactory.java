package com.ultramega.refinedtypes.autocrafting;

import com.ultramega.refinedtypes.type.soul.SoulCapabilityCache;

import com.refinedmods.refinedstorage.common.api.autocrafting.PatternProviderExternalPatternSinkFactory;
import com.refinedmods.refinedstorage.common.api.autocrafting.PlatformPatternProviderExternalPatternSink;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;

public class SoulPatternProviderExternalPatternSinkFactory implements PatternProviderExternalPatternSinkFactory {
    @Override
    public PlatformPatternProviderExternalPatternSink create(final ServerLevel level,
                                                             final BlockPos pos,
                                                             final Direction direction) {
        final SoulCapabilityCache capabilityCache = new SoulCapabilityCache(level, pos, direction);
        return new SoulPatternProviderExternalPatternSink(capabilityCache);
    }
}
