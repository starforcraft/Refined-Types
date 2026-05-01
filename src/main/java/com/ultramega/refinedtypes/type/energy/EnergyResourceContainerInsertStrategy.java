package com.ultramega.refinedtypes.type.energy;

import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceContainerInsertStrategy;

import java.util.Optional;

import net.minecraft.world.item.ItemStack;

import static com.ultramega.refinedtypes.RefinedTypesUtil.chargeContainer;
import static com.ultramega.refinedtypes.type.energy.EnergyResourceType.DEFAULT_TRANSFER_AMOUNT;

public class EnergyResourceContainerInsertStrategy implements ResourceContainerInsertStrategy {
    @Override
    public Optional<InsertResult> insert(final ItemStack container, final ResourceAmount resourceAmount) {
        if (!(resourceAmount.resource() instanceof EnergyResource)) {
            return Optional.empty();
        }
        return chargeContainer(container, resourceAmount).map(
            result -> new InsertResult(result.container(), result.amount())
        );
    }

    @Override
    public Optional<ConversionInfo> getConversionInfo(final ResourceKey resource, final ItemStack carriedStack) {
        if (!(resource instanceof EnergyResource energyResource)) {
            return Optional.empty();
        }
        final ResourceAmount toFill = new ResourceAmount(energyResource, DEFAULT_TRANSFER_AMOUNT);
        return chargeContainer(carriedStack, toFill)
            .filter(result -> result.amount() > 0)
            .map(result -> new ConversionInfo(carriedStack, result.container()));
    }
}
