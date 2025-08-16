package com.ultramega.refinedtypes.exporter;

import com.ultramega.refinedtypes.type.source.SourceCapabilityCache;
import com.ultramega.refinedtypes.type.source.SourceInsertableStorage;
import com.ultramega.refinedtypes.type.source.SourceResource;

import com.refinedmods.refinedstorage.api.network.impl.node.exporter.ExporterTransferStrategyImpl;
import com.refinedmods.refinedstorage.api.network.impl.node.exporter.MissingResourcesListeningExporterTransferStrategy;
import com.refinedmods.refinedstorage.api.network.node.exporter.ExporterTransferStrategy;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.common.Platform;
import com.refinedmods.refinedstorage.common.api.exporter.ExporterTransferStrategyFactory;
import com.refinedmods.refinedstorage.common.api.storage.root.FuzzyRootStorage;
import com.refinedmods.refinedstorage.common.api.upgrade.UpgradeState;
import com.refinedmods.refinedstorage.common.content.Items;
import com.refinedmods.refinedstorage.common.exporter.ExporterTransferQuotaProvider;

import java.util.function.ToLongFunction;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;

import static com.refinedmods.refinedstorage.api.network.impl.node.exporter.MissingResourcesListeningExporterTransferStrategy.OnMissingResources.scheduleAutocrafting;

public class SourceExporterTransferStrategyFactory implements ExporterTransferStrategyFactory {
    @Override
    public Class<? extends ResourceKey> getResourceType() {
        return SourceResource.class;
    }

    @Override
    public ExporterTransferStrategy create(final ServerLevel level,
                                           final BlockPos pos,
                                           final Direction direction,
                                           final UpgradeState upgradeState,
                                           final boolean fuzzyMode) {
        final SourceCapabilityCache capabilityCache = new SourceCapabilityCache(level, pos, direction);
        final SourceInsertableStorage destination = new SourceInsertableStorage(capabilityCache);
        final long singleAmount = (upgradeState.has(Items.INSTANCE.getStackUpgrade()) ? 64 : 1) * Platform.INSTANCE.getBucketAmount();
        final ExporterTransferStrategy strategy = this.create(
            fuzzyMode,
            destination,
            new ExporterTransferQuotaProvider(singleAmount, upgradeState, destination::getAmount, true)
        );
        if (upgradeState.has(Items.INSTANCE.getAutocraftingUpgrade())) {
            return new MissingResourcesListeningExporterTransferStrategy(strategy, scheduleAutocrafting(
                new ExporterTransferQuotaProvider(singleAmount, upgradeState, destination::getAmount, false)));
        }
        return strategy;
    }

    private ExporterTransferStrategy create(final boolean fuzzyMode,
                                            final SourceInsertableStorage destination,
                                            final ToLongFunction<ResourceKey> transferQuotaProvider) {
        if (fuzzyMode) {
            return new ExporterTransferStrategyImpl(destination, transferQuotaProvider, FuzzyRootStorage.expander());
        }
        return new ExporterTransferStrategyImpl(destination, transferQuotaProvider);
    }
}
