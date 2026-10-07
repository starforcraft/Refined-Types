package com.ultramega.refinedtypes.externalstorage;

import com.ultramega.refinedtypes.type.essentia.EssentiaCapabilityCache;

import com.refinedmods.refinedstorage.api.storage.external.ExternalStorageProvider;
import com.refinedmods.refinedstorage.common.api.storage.externalstorage.ExternalStorageProviderFactory;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;

public class EssentiaPlatformExternalStorageProviderFactory implements ExternalStorageProviderFactory {
    @Override
    public ExternalStorageProvider create(final ServerLevel level, final BlockPos pos, final Direction direction) {
        final EssentiaCapabilityCache capabilityCache = new EssentiaCapabilityCache(level, pos, direction);
        return new EssentiaExternalStorageProvider(capabilityCache);
    }
}
