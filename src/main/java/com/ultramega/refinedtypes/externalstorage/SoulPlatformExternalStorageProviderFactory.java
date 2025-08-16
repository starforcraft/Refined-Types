package com.ultramega.refinedtypes.externalstorage;

import com.ultramega.refinedtypes.type.soul.SoulCapabilityCache;

import com.refinedmods.refinedstorage.api.storage.external.ExternalStorageProvider;
import com.refinedmods.refinedstorage.common.api.storage.externalstorage.ExternalStorageProviderFactory;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;

public class SoulPlatformExternalStorageProviderFactory implements ExternalStorageProviderFactory {
    @Override
    public ExternalStorageProvider create(final ServerLevel level, final BlockPos pos, final Direction direction) {
        final SoulCapabilityCache capabilityCache = new SoulCapabilityCache(level, pos, direction);
        return new SoulExternalStorageProvider(capabilityCache);
    }
}
