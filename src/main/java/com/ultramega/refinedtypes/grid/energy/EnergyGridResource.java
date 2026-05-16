package com.ultramega.refinedtypes.grid.energy;

import com.ultramega.refinedtypes.grid.AbstractTypedGridResource;
import com.ultramega.refinedtypes.registry.Types;
import com.ultramega.refinedtypes.type.energy.EnergyResource;
import com.ultramega.refinedtypes.type.energy.EnergyResourceType;

import com.refinedmods.refinedstorage.api.resource.repository.ResourceRepository;
import com.refinedmods.refinedstorage.common.Platform;
import com.refinedmods.refinedstorage.common.api.grid.view.GridResource;
import com.refinedmods.refinedstorage.common.api.grid.view.GridResourceAttributeKey;
import com.refinedmods.refinedstorage.common.support.tooltip.MouseClientTooltipComponent;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import dev.technici4n.grandpower.api.ILongEnergyStorage;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;

public class EnergyGridResource extends AbstractTypedGridResource<EnergyResource> {
    public EnergyGridResource(final EnergyResource resource,
                              final String name,
                              final Function<GridResourceAttributeKey, Set<String>> attributes) {
        super(resource, name, attributes, EnergyResource.class, Types.FE.get(), EnergyResourceType.INSTANCE, Platform.INSTANCE.getBucketAmount());
    }

    @Override
    public List<ClientTooltipComponent> getExtractionHints(final ItemStack carriedStack,
                                                           final ResourceRepository<GridResource> repository) {
        final ItemStack modifiedStack = carriedStack.copy();
        return Optional.ofNullable(modifiedStack.getCapability(ILongEnergyStorage.ITEM))
            .map(handler -> handler.receive(Platform.INSTANCE.getBucketAmount(), false))
            .filter(inserted -> inserted > 0)
            .map(inserted -> MouseClientTooltipComponent.item(
                MouseClientTooltipComponent.Type.LEFT,
                modifiedStack,
                null
            ))
            .stream()
            .toList();
    }

    @Override
    public boolean canExtract(final ItemStack carriedStack, final ResourceRepository<GridResource> repository) {
        if (this.getAmount(repository) == 0) {
            return false;
        }
        if (carriedStack.isEmpty()) {
            return true;
        }
        final long toFill = repository.getAmount(this.resource);
        return Optional.ofNullable(carriedStack.getCapability(ILongEnergyStorage.ITEM))
            .map(handler -> handler.receive(toFill, true))
            .map(inserted -> inserted > 0)
            .orElse(false);
    }
}
