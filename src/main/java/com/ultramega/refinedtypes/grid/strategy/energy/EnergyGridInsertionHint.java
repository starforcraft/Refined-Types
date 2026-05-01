package com.ultramega.refinedtypes.grid.strategy.energy;

import com.ultramega.refinedtypes.type.TypeOperationResult;
import com.ultramega.refinedtypes.type.energy.EnergyResource;

import com.refinedmods.refinedstorage.common.api.RefinedStorageClientApi;
import com.refinedmods.refinedstorage.common.api.grid.GridInsertionHint;
import com.refinedmods.refinedstorage.common.support.tooltip.MouseClientTooltipComponent;

import java.util.Optional;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;

import static com.ultramega.refinedtypes.RefinedTypesUtil.dischargeContainer;
import static com.ultramega.refinedtypes.type.energy.EnergyResource.ENERGY_RESOURCE;

public class EnergyGridInsertionHint implements GridInsertionHint {
    @Override
    public Optional<ClientTooltipComponent> getHint(final ItemStack carried) {
        return dischargeContainer(carried).map(this::createComponent);
    }

    private ClientTooltipComponent createComponent(final TypeOperationResult result) {
        return MouseClientTooltipComponent.resource(MouseClientTooltipComponent.Type.RIGHT, ENERGY_RESOURCE, doFormat(result.amount()));
    }

    private static String doFormat(final long resultAmount) {
        return RefinedStorageClientApi.INSTANCE.getResourceRendering(EnergyResource.class)
            .formatAmount(resultAmount);
    }
}
