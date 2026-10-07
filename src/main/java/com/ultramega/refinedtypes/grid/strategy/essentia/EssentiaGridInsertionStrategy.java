package com.ultramega.refinedtypes.grid.strategy.essentia;

import com.ultramega.refinedtypes.type.essentia.EssentiaResource;
import com.ultramega.refinedtypes.type.essentia.EssentiaResourceType;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.network.node.grid.GridInsertMode;
import com.refinedmods.refinedstorage.api.network.node.grid.GridOperations;
import com.refinedmods.refinedstorage.common.api.grid.Grid;
import com.refinedmods.refinedstorage.common.api.grid.strategy.GridInsertionStrategy;

import com.leclowndu93150.thaumaturge.api.essentia.EssentiaCapabilities;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class EssentiaGridInsertionStrategy implements GridInsertionStrategy {
    private final AbstractContainerMenu menu;
    private final GridOperations operations;

    public EssentiaGridInsertionStrategy(final AbstractContainerMenu menu, final ServerPlayer player, final Grid grid) {
        this.menu = menu;
        this.operations = grid.createOperations(EssentiaResourceType.INSTANCE, player);
    }

    @Override
    public boolean onInsert(final GridInsertMode mode, final boolean tryAlternatives) {
        final var stack = this.menu.getCarried();
        final var storage = stack.getCapability(EssentiaCapabilities.ITEM_STORAGE);
        if (!tryAlternatives || stack.getCount() != 1 || storage == null || storage.contents().isEmpty()) {
            return false;
        }
        for (final var entry : storage.contents().entries()) {
            final var essentia = EssentiaResource.of(entry.aspect());
            this.operations.insert(essentia, mode, (resource, amount, action, actor) -> {
                if (!resource.equals(essentia) || amount <= 0) {
                    return 0;
                }
                final var current = this.menu.getCarried().getCapability(EssentiaCapabilities.ITEM_STORAGE);
                if (current == null || this.menu.getCarried().getCount() != 1) {
                    return 0;
                }
                final var result = current.extract(entry.aspect(), (int) Math.min(amount, Integer.MAX_VALUE));
                if (action == Action.EXECUTE && result.amountMoved() > 0) {
                    this.menu.setCarried(result.resultingStack());
                    this.menu.broadcastChanges();
                }
                return result.amountMoved();
            });
        }
        return true;
    }

    @Override
    public boolean onTransfer(final int slotIndex) {
        return false;
    }
}
