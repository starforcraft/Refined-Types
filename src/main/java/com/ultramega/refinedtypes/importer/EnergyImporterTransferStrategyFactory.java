package com.ultramega.refinedtypes.importer;

import com.ultramega.refinedtypes.type.energy.EnergyCapabilityCache;

import com.refinedmods.refinedstorage.api.network.impl.node.importer.ImporterTransferStrategyImpl;
import com.refinedmods.refinedstorage.api.network.node.importer.ImporterTransferStrategy;
import com.refinedmods.refinedstorage.common.api.importer.ImporterTransferStrategyFactory;
import com.refinedmods.refinedstorage.common.api.upgrade.UpgradeState;
import com.refinedmods.refinedstorage.common.content.Items;
import com.refinedmods.refinedstorage.common.importer.ImporterTransferQuotaProvider;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;

import static com.ultramega.refinedtypes.type.energy.EnergyResourceType.DEFAULT_TRANSFER_AMOUNT;

public class EnergyImporterTransferStrategyFactory implements ImporterTransferStrategyFactory {
    @Override
    public ImporterTransferStrategy create(final ServerLevel level,
                                           final BlockPos pos,
                                           final Direction direction,
                                           final UpgradeState upgradeState) {
        final EnergyImporterSource source = new EnergyImporterSource(new EnergyCapabilityCache(level, pos, direction));
        final int singleAmount = upgradeState.has(Items.INSTANCE.getStackUpgrade())
            ? (int) DEFAULT_TRANSFER_AMOUNT * 100
            : (int) DEFAULT_TRANSFER_AMOUNT;
        final ImporterTransferQuotaProvider transferQuotaProvider = new ImporterTransferQuotaProvider(
            singleAmount,
            upgradeState,
            source::getAmount
        );
        return new ImporterTransferStrategyImpl(source, transferQuotaProvider);
    }
}
