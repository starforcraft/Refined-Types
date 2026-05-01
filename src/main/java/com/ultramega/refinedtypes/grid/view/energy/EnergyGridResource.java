package com.ultramega.refinedtypes.grid.view.energy;

import com.ultramega.refinedtypes.grid.AbstractTypedGridResource;
import com.ultramega.refinedtypes.registry.Types;
import com.ultramega.refinedtypes.type.TypeOperationResult;
import com.ultramega.refinedtypes.type.energy.EnergyResource;

import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.api.resource.repository.ResourceRepository;
import com.refinedmods.refinedstorage.common.Platform;
import com.refinedmods.refinedstorage.common.api.grid.view.GridResource;
import com.refinedmods.refinedstorage.common.api.grid.view.GridResourceAttributeKey;
import com.refinedmods.refinedstorage.common.api.grid.view.GridResourceType;
import com.refinedmods.refinedstorage.common.support.tooltip.MouseClientTooltipComponent;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;

import static com.ultramega.refinedtypes.RefinedTypesUtil.chargeContainer;
import static com.ultramega.refinedtypes.type.energy.EnergyResourceType.DEFAULT_TRANSFER_AMOUNT;

public class EnergyGridResource extends AbstractTypedGridResource<EnergyResource> {
    public EnergyGridResource(final EnergyResource resource,
                              final String name,
                              final Function<GridResourceAttributeKey, Set<String>> attributes) {
        super(resource, name, attributes, EnergyResource.class, Types.FE.get(), DEFAULT_TRANSFER_AMOUNT / 10);
    }

    @Override
    public List<ClientTooltipComponent> getExtractionHints(final ItemStack carriedStack,
                                                           final ResourceRepository<GridResource> repository) {
        return this.tryChargeEnergyContainer(carriedStack)
            .filter(result -> result.amount() > 0)
            .map(result -> MouseClientTooltipComponent.item(
                MouseClientTooltipComponent.Type.LEFT,
                result.container(),
                null
            )).stream().toList();
    }

    private Optional<TypeOperationResult> tryChargeEnergyContainer(final ItemStack carriedStack) {
        final ResourceAmount toFill = new ResourceAmount(this.resource, DEFAULT_TRANSFER_AMOUNT);
        return carriedStack.isEmpty()
            ? Optional.empty()
            : chargeContainer(carriedStack, toFill);
    }

    @Override
    public boolean canExtract(final ItemStack carriedStack, final ResourceRepository<GridResource> repository) {
        if (this.getAmount(repository) == 0) {
            return false;
        }
        if (carriedStack.isEmpty()) {
            return true;
        }
        final ResourceAmount toFill = new ResourceAmount(this.resource, repository.getAmount(this.resource));
        return chargeContainer(carriedStack, toFill)
            .map(result -> result.amount() > 0)
            .orElse(false);
    }

    @Override
    public boolean is(final GridResource other) {
        if (other instanceof EnergyGridResource otherEnergy) {
            return this.resource.equals(otherEnergy.resource);
        }
        return false;
    }

    @Override
    public GridResourceType getType() {
        return EnergyGridResourceType.INSTANCE;
    }
}
