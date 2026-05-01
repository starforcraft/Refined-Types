package com.ultramega.refinedtypes.grid.strategy.energy;

import com.ultramega.refinedtypes.type.energy.EnergyResource;
import com.ultramega.refinedtypes.type.energy.EnergyResourceType;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.network.node.grid.GridInsertMode;
import com.refinedmods.refinedstorage.api.network.node.grid.GridOperations;
import com.refinedmods.refinedstorage.common.api.grid.Grid;
import com.refinedmods.refinedstorage.common.api.grid.strategy.GridInsertionStrategy;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import static com.ultramega.refinedtypes.type.energy.EnergyResource.ENERGY_RESOURCE;

public class EnergyGridInsertionStrategy implements GridInsertionStrategy {
    private final AbstractContainerMenu menu;
    private final ServerPlayer player;
    private final GridOperations gridOperations;

    public EnergyGridInsertionStrategy(final AbstractContainerMenu menu,
                                       final ServerPlayer player,
                                       final Grid grid) {
        this.menu = menu;
        this.player = player;
        this.gridOperations = grid.createOperations(EnergyResourceType.INSTANCE, player);
    }

    @Override
    public boolean onInsert(final GridInsertMode insertMode, final boolean tryAlternatives) {
        final ItemAccess itemAccess = ItemAccess.forPlayerCursor(this.player, this.menu);
        final EnergyHandler playerCursor = this.menu.getCarried().getCapability(Capabilities.Energy.ITEM, itemAccess);
        if (playerCursor == null) {
            return false;
        }
        final long extractableResource = playerCursor.getAmountAsLong();
        if (extractableResource <= 0) {
            return false;
        }
        this.gridOperations.insert(ENERGY_RESOURCE, insertMode, (resource, amount, action, source) -> {
            if (!(resource instanceof EnergyResource)) {
                return 0;
            }
            try (Transaction tx = Transaction.openRoot()) {
                final long extracted = playerCursor.extract((int) amount, tx);
                if (action == Action.EXECUTE) {
                    tx.commit();
                }
                return extracted;
            }
        });
        return true;
    }

    @Override
    public boolean onTransfer(final int slotIndex) {
        throw new UnsupportedOperationException();
    }
}
