package com.ultramega.refinedtypes.autocrafting;

import com.ultramega.refinedtypes.type.source.SourceCapabilityCache;

import com.refinedmods.refinedstorage.common.api.autocrafting.PatternProviderExternalPatternSinkFactory;
import com.refinedmods.refinedstorage.common.api.autocrafting.PlatformPatternProviderExternalPatternSink;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;

public class SourcePatternProviderExternalPatternSinkFactory implements PatternProviderExternalPatternSinkFactory {
    @Override
    public PlatformPatternProviderExternalPatternSink create(final ServerLevel level,
                                                             final BlockPos pos,
                                                             final Direction direction) {
        final SourceCapabilityCache capabilityCache = new SourceCapabilityCache(level, pos, direction);
        return new SourcePatternProviderExternalPatternSink(capabilityCache);
    }
}
