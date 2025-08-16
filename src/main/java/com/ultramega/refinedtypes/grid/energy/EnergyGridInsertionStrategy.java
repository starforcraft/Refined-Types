package com.ultramega.refinedtypes.grid.energy;

import com.ultramega.refinedtypes.type.energy.EnergyResource;
import com.ultramega.refinedtypes.type.energy.EnergyResourceType;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.network.node.grid.GridInsertMode;
import com.refinedmods.refinedstorage.api.network.node.grid.GridOperations;
import com.refinedmods.refinedstorage.common.api.grid.Grid;
import com.refinedmods.refinedstorage.common.api.grid.strategy.GridInsertionStrategy;

import javax.annotation.Nullable;

import dev.technici4n.grandpower.api.ILongEnergyStorage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import static com.ultramega.refinedtypes.type.energy.EnergyResource.createEnergyResource;

public class EnergyGridInsertionStrategy implements GridInsertionStrategy {
    private final AbstractContainerMenu menu;
    private final GridOperations gridOperations;

    public EnergyGridInsertionStrategy(final AbstractContainerMenu menu,
                                       final ServerPlayer player,
                                       final Grid grid) {
        this.menu = menu;
        this.gridOperations = grid.createOperations(EnergyResourceType.INSTANCE, player);
    }

    @Override
    public boolean onInsert(final GridInsertMode insertMode, final boolean tryAlternatives) {
        final ILongEnergyStorage cursorStorage = this.getEnergyCursorStorage();
        if (cursorStorage == null) {
            return false;
        }
        final long extractableResource = cursorStorage.getAmount();
        if (extractableResource <= 0) {
            return false;
        }
        this.gridOperations.insert(createEnergyResource(), insertMode, (resource, amount, action, source) -> {
            if (!(resource instanceof EnergyResource)) {
                return 0;
            }
            return cursorStorage.extract(amount, action == Action.SIMULATE);
        });
        return true;
    }

    @Nullable
    private ILongEnergyStorage getEnergyCursorStorage() {
        return this.getEnergyStorage(this.menu.getCarried());
    }

    @Nullable
    private ILongEnergyStorage getEnergyStorage(final ItemStack stack) {
        return stack.getCapability(ILongEnergyStorage.ITEM);
    }

    @Override
    public boolean onTransfer(final int slotIndex) {
        throw new UnsupportedOperationException();
    }
}
