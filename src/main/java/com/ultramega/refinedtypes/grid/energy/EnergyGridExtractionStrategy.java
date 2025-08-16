package com.ultramega.refinedtypes.grid.energy;

import com.ultramega.refinedtypes.type.energy.EnergyResource;
import com.ultramega.refinedtypes.type.energy.EnergyResourceType;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.network.node.grid.GridExtractMode;
import com.refinedmods.refinedstorage.api.network.node.grid.GridOperations;
import com.refinedmods.refinedstorage.common.api.grid.Grid;
import com.refinedmods.refinedstorage.common.api.grid.strategy.GridExtractionStrategy;
import com.refinedmods.refinedstorage.common.api.support.resource.PlatformResourceKey;

import javax.annotation.Nullable;

import dev.technici4n.grandpower.api.ILongEnergyStorage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class EnergyGridExtractionStrategy implements GridExtractionStrategy {
    private final AbstractContainerMenu menu;
    private final GridOperations gridOperations;

    public EnergyGridExtractionStrategy(final AbstractContainerMenu containerMenu,
                                        final ServerPlayer player,
                                        final Grid grid) {
        this.menu = containerMenu;
        this.gridOperations = grid.createOperations(EnergyResourceType.INSTANCE, player);
    }

    @Override
    public boolean onExtract(final PlatformResourceKey resource,
                             final GridExtractMode extractMode,
                             final boolean cursor) {
        if (resource instanceof EnergyResource energyResource && this.isEnergyContainerOnCursor()) {
            this.extractWithContainerOnCursor(energyResource, extractMode);
            return true;
        }
        return false;
    }

    @Nullable
    private ILongEnergyStorage getEnergyStorage(final ItemStack stack) {
        return stack.getCapability(ILongEnergyStorage.ITEM);
    }

    private void extractWithContainerOnCursor(final EnergyResource energyResource,
                                              final GridExtractMode mode) {
        this.gridOperations.extract(energyResource, mode, (resource, amount, action, source) -> {
            if (!(resource instanceof EnergyResource)) {
                return 0;
            }
            final ILongEnergyStorage destination = this.getEnergyStorage(this.menu.getCarried());
            if (destination == null) {
                return 0;
            }
            return destination.receive(amount, action == Action.SIMULATE);
        });
    }

    private boolean isEnergyContainerOnCursor() {
        return this.getEnergyStorage(this.menu.getCarried()) != null;
    }
}
