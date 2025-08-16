package com.ultramega.refinedtypes.grid.energy;

import com.ultramega.refinedtypes.type.energy.EnergyResource;

import com.refinedmods.refinedstorage.common.Platform;
import com.refinedmods.refinedstorage.common.api.RefinedStorageClientApi;
import com.refinedmods.refinedstorage.common.api.grid.GridInsertionHint;
import com.refinedmods.refinedstorage.common.support.tooltip.MouseClientTooltipComponent;

import java.util.Optional;

import dev.technici4n.grandpower.api.ILongEnergyStorage;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;

import static com.ultramega.refinedtypes.type.energy.EnergyResource.createEnergyResource;

public class EnergyGridInsertionHint implements GridInsertionHint {
    @Override
    public Optional<ClientTooltipComponent> getHint(final ItemStack stack) {
        return Optional.ofNullable(stack.getCapability(ILongEnergyStorage.ITEM))
            .map(handler -> handler.extract(Platform.INSTANCE.getBucketAmount(), true))
            .filter(result -> result > 0)
            .map(this::createComponent);
    }

    private ClientTooltipComponent createComponent(final long resultAmount) {
        return MouseClientTooltipComponent.resource(MouseClientTooltipComponent.Type.RIGHT, createEnergyResource(), doFormat(resultAmount));
    }

    private static String doFormat(final long resultAmount) {
        return RefinedStorageClientApi.INSTANCE.getResourceRendering(EnergyResource.class)
            .formatAmount(resultAmount);
    }
}
