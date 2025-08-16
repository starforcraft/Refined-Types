package com.ultramega.refinedtypes.type.energy;

import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.common.Platform;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceContainerInsertStrategy;

import java.util.Optional;

import dev.technici4n.grandpower.api.ILongEnergyStorage;
import net.minecraft.world.item.ItemStack;

public class EnergyResourceContainerInsertStrategy implements ResourceContainerInsertStrategy {
    @Override
    public Optional<InsertResult> insert(final ItemStack container, final ResourceAmount resourceAmount) {
        if (!(resourceAmount.resource() instanceof EnergyResource)) {
            return Optional.empty();
        }
        final ItemStack modifiedContainer = container.copy();
        return Optional.ofNullable(modifiedContainer.getCapability(ILongEnergyStorage.ITEM))
            .map(handler -> handler.receive(resourceAmount.amount(), false))
            .map(inserted -> new InsertResult(modifiedContainer, inserted));
    }

    @Override
    public Optional<ConversionInfo> getConversionInfo(final ResourceKey resource, final ItemStack carriedStack) {
        if (!(resource instanceof EnergyResource)) {
            return Optional.empty();
        }
        final ItemStack modifiedStack = carriedStack.copy();
        return Optional.ofNullable(modifiedStack.getCapability(ILongEnergyStorage.ITEM))
            .map(handler -> handler.receive(Platform.INSTANCE.getBucketAmount(), false))
            .filter(amount -> amount > 0)
            .map(result -> new ConversionInfo(carriedStack, modifiedStack));
    }
}
