package com.ultramega.refinedtypes.grid.strategy.essentia;

import com.ultramega.refinedtypes.type.essentia.EssentiaResource;

import com.refinedmods.refinedstorage.common.api.grid.GridInsertionHint;
import com.refinedmods.refinedstorage.common.support.tooltip.MouseClientTooltipComponent;

import java.util.Optional;

import com.leclowndu93150.thaumaturge.api.essentia.EssentiaCapabilities;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;

public class EssentiaGridInsertionHint implements GridInsertionHint {
    @Override
    public Optional<ClientTooltipComponent> getHint(final ItemStack carried) {
        final var storage = carried.getCapability(EssentiaCapabilities.ITEM_STORAGE);
        if (carried.getCount() != 1 || storage == null || storage.contents().isEmpty()) {
            return Optional.empty();
        }
        final var first = storage.contents().entries().getFirst();
        final int amount = storage.extract(first.aspect(), Integer.MAX_VALUE).amountMoved();
        if (amount <= 0) {
            return Optional.empty();
        }
        return Optional.of(MouseClientTooltipComponent.resource(
            MouseClientTooltipComponent.Type.RIGHT,
            EssentiaResource.of(first.aspect()),
            Long.toString(amount)
        ));
    }
}
