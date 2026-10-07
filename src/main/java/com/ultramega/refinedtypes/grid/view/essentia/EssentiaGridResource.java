package com.ultramega.refinedtypes.grid.view.essentia;

import com.ultramega.refinedtypes.grid.AbstractTypedGridResource;
import com.ultramega.refinedtypes.type.essentia.EssentiaResource;

import com.refinedmods.refinedstorage.api.resource.repository.ResourceRepository;
import com.refinedmods.refinedstorage.common.api.grid.view.GridResource;
import com.refinedmods.refinedstorage.common.api.grid.view.GridResourceAttributeKey;
import com.refinedmods.refinedstorage.common.api.grid.view.GridResourceType;

import java.util.Set;
import java.util.function.Function;

import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.essentia.EssentiaCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

public class EssentiaGridResource extends AbstractTypedGridResource<EssentiaResource> {
    public EssentiaGridResource(final EssentiaResource resource,
                                final String name,
                                final Function<GridResourceAttributeKey, Set<String>> attributes) {
        super(resource, name, attributes, EssentiaResource.class, getRegistryId(resource), 1);
    }

    private static int getRegistryId(final EssentiaResource resource) {
        final var level = Minecraft.getInstance().level;
        final var aspect = level == null ? null : resource.resolve(level.registryAccess());
        return aspect == null ? -1 : level.registryAccess()
            .lookupOrThrow(IAspect.REGISTRY_KEY).getId(aspect.unwrapKey().orElseThrow());
    }

    public EssentiaResource getEssentia() {
        return this.resource;
    }

    @Override
    public boolean is(final GridResource other) {
        return other instanceof EssentiaGridResource essentia && this.resource.equals(essentia.resource);
    }

    @Override
    public boolean canExtract(final ItemStack carriedStack, final ResourceRepository<GridResource> repository) {
        final long amount = this.getAmount(repository);
        if (carriedStack.getCount() != 1 || amount <= 0) {
            return false;
        }
        final var level = Minecraft.getInstance().level;
        final var aspect = level == null ? null : this.resource.resolve(level.registryAccess());
        final var storage = carriedStack.getCapability(EssentiaCapabilities.ITEM_STORAGE);
        return aspect != null && storage != null
            && storage.insert(aspect, (int) Math.min(amount, Integer.MAX_VALUE)).amountMoved() > 0;
    }

    @Override
    public GridResourceType getType() {
        return EssentiaGridResourceType.INSTANCE;
    }
}
