package com.ultramega.refinedtypes.exporter;

import com.ultramega.refinedtypes.type.essentia.EssentiaCapabilityCache;
import com.ultramega.refinedtypes.type.essentia.EssentiaInsertableStorage;
import com.ultramega.refinedtypes.type.essentia.EssentiaResource;

import com.refinedmods.refinedstorage.api.network.impl.node.exporter.ExporterTransferStrategyImpl;
import com.refinedmods.refinedstorage.api.network.impl.node.exporter.MissingResourcesListeningExporterTransferStrategy;
import com.refinedmods.refinedstorage.api.network.node.exporter.ExporterTransferStrategy;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
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
import static com.ultramega.refinedtypes.type.essentia.EssentiaResourceType.DEFAULT_TRANSFER_AMOUNT;

public class EssentiaExporterTransferStrategyFactory implements ExporterTransferStrategyFactory {
    @Override
    public Class<? extends ResourceKey> getResourceType() {
        return EssentiaResource.class;
    }

    @Override
    public ExporterTransferStrategy create(final ServerLevel level,
                                           final BlockPos pos,
                                           final Direction direction,
                                           final UpgradeState upgradeState,
                                           final boolean fuzzyMode) {
        final EssentiaCapabilityCache capabilityCache = new EssentiaCapabilityCache(level, pos, direction);
        final EssentiaInsertableStorage destination = new EssentiaInsertableStorage(capabilityCache);
        final int singleAmount = upgradeState.has(Items.INSTANCE.getStackUpgrade())
            ? (int) DEFAULT_TRANSFER_AMOUNT * 100
            : (int) DEFAULT_TRANSFER_AMOUNT;
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
                                            final EssentiaInsertableStorage destination,
                                            final ToLongFunction<ResourceKey> transferQuotaProvider) {
        if (fuzzyMode) {
            return new ExporterTransferStrategyImpl(destination, transferQuotaProvider, FuzzyRootStorage.expander());
        }
        return new ExporterTransferStrategyImpl(destination, transferQuotaProvider);
    }
}
