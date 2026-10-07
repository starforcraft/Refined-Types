package com.ultramega.refinedtypes.grid.strategy.essentia;

import com.ultramega.refinedtypes.type.essentia.EssentiaResource;
import com.ultramega.refinedtypes.type.essentia.EssentiaResourceType;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.network.node.grid.GridExtractMode;
import com.refinedmods.refinedstorage.api.network.node.grid.GridOperations;
import com.refinedmods.refinedstorage.common.api.grid.Grid;
import com.refinedmods.refinedstorage.common.api.grid.strategy.GridExtractionStrategy;
import com.refinedmods.refinedstorage.common.api.support.resource.PlatformResourceKey;

import com.leclowndu93150.thaumaturge.api.essentia.EssentiaCapabilities;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class EssentiaGridExtractionStrategy implements GridExtractionStrategy {
    private final AbstractContainerMenu menu;
    private final ServerPlayer player;
    private final GridOperations operations;

    public EssentiaGridExtractionStrategy(final AbstractContainerMenu menu, final ServerPlayer player, final Grid grid) {
        this.menu = menu;
        this.player = player;
        this.operations = grid.createOperations(EssentiaResourceType.INSTANCE, player);
    }

    @Override
    public boolean onExtract(final PlatformResourceKey resource, final GridExtractMode mode, final boolean cursor) {
        if (!(resource instanceof EssentiaResource essentia) || this.menu.getCarried().getCount() != 1) {
            return false;
        }
        final var aspect = essentia.resolve(this.player.registryAccess());
        final var storage = this.menu.getCarried().getCapability(EssentiaCapabilities.ITEM_STORAGE);
        if (aspect == null || storage == null) {
            return false;
        }
        this.operations.extract(essentia, mode, (key, amount, action, actor) -> {
            if (!key.equals(essentia) || amount <= 0) {
                return 0;
            }
            final var current = this.menu.getCarried().getCapability(EssentiaCapabilities.ITEM_STORAGE);
            if (current == null || this.menu.getCarried().getCount() != 1) {
                return 0;
            }
            final var result = current.insert(aspect, (int) Math.min(amount, Integer.MAX_VALUE));
            if (action == Action.EXECUTE && result.amountMoved() > 0) {
                this.menu.setCarried(result.resultingStack());
                this.menu.broadcastChanges();
            }
            return result.amountMoved();
        });
        return true;
    }
}
