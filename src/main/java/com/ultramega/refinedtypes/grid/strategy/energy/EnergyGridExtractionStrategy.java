package com.ultramega.refinedtypes.grid.strategy.energy;

import com.ultramega.refinedtypes.type.energy.EnergyResource;
import com.ultramega.refinedtypes.type.energy.EnergyResourceType;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.network.node.grid.GridExtractMode;
import com.refinedmods.refinedstorage.api.network.node.grid.GridOperations;
import com.refinedmods.refinedstorage.common.api.grid.Grid;
import com.refinedmods.refinedstorage.common.api.grid.strategy.GridExtractionStrategy;
import com.refinedmods.refinedstorage.common.api.support.resource.PlatformResourceKey;
import com.refinedmods.refinedstorage.neoforge.support.resource.SimpleItemStackResourceHandler;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.item.CarriedSlotWrapper;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.PlayerInventoryWrapper;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class EnergyGridExtractionStrategy implements GridExtractionStrategy {
    private final GridOperations gridOperations;
    private final ResourceHandler<net.neoforged.neoforge.transfer.item.ItemResource> playerInventory;
    private final ResourceHandler<net.neoforged.neoforge.transfer.item.ItemResource> playerCursor;

    public EnergyGridExtractionStrategy(final AbstractContainerMenu containerMenu,
                                        final ServerPlayer player,
                                        final Grid grid) {
        this.gridOperations = grid.createOperations(EnergyResourceType.INSTANCE, player);
        this.playerInventory = PlayerInventoryWrapper.of(player);
        this.playerCursor = CarriedSlotWrapper.of(containerMenu);
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

    private void extractWithContainerOnCursor(final EnergyResource energyResource,
                                              final GridExtractMode mode) {
        try (Transaction tx = Transaction.openRoot()) {
            final ItemStack stack = this.extractContainerFromCursor(tx);
            if (stack.isEmpty()) {
                return;
            }
            final SimpleItemStackResourceHandler interceptingHandler = SimpleItemStackResourceHandler.forStack(stack);
            final ItemAccess access = ItemAccess.forHandlerIndex(interceptingHandler, 0);
            final EnergyHandler dest = interceptingHandler.getStack().getCapability(Capabilities.Energy.ITEM, access);
            if (dest == null) {
                return;
            }
            this.gridOperations.extract(energyResource, mode, (resource2, amount, action, source) -> {
                if (!(resource2 instanceof EnergyResource)) {
                    return 0;
                }
                try (Transaction innerTx = Transaction.open(tx)) {
                    final long inserted = dest.insert((int) amount, innerTx);
                    final boolean couldInsertContainer = this.insertResultingContainerIntoInventory(
                        interceptingHandler,
                        true,
                        innerTx
                    );
                    if (!couldInsertContainer) {
                        return 0;
                    }
                    if (action == Action.EXECUTE) {
                        innerTx.commit();
                        tx.commit();
                    }
                    return inserted;
                }
            });
        }
    }

    private ItemStack extractContainerFromCursor(final Transaction tx) {
        final var result = ResourceHandlerUtil.extractFirst(this.playerCursor, r -> true, 1, tx);
        if (result == null || result.isEmpty()) {
            return ItemStack.EMPTY;
        }
        return result.resource().toStack();
    }

    private boolean insertResultingContainerIntoInventory(final SimpleItemStackResourceHandler interceptingHandler,
                                                          final boolean cursor,
                                                          final Transaction innerTx) {
        final ResourceHandler<ItemResource> relevantStorage = cursor
            ? this.playerCursor
            : this.playerInventory;
        final net.neoforged.neoforge.transfer.item.ItemResource platformResource =
            net.neoforged.neoforge.transfer.item.ItemResource.of(interceptingHandler.getStack());
        return relevantStorage.insert(platformResource, 1, innerTx) != 0;
    }

    private boolean isEnergyContainerOnCursor() {
        final net.neoforged.neoforge.transfer.item.ItemResource platformResource =
            ResourceHandlerUtil.findExtractableResource(this.playerCursor, r -> true, null);
        if (platformResource == null) {
            return false;
        }
        final ItemStack stack = platformResource.toStack();
        return stack.getCapability(Capabilities.Energy.ITEM, ItemAccess.forStack(stack)) != null;
    }
}
