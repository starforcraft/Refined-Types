package com.ultramega.refinedtypes.grid;

import com.ultramega.refinedtypes.registry.Types;
import com.ultramega.refinedtypes.type.Type;

import com.refinedmods.refinedstorage.api.network.node.grid.GridExtractMode;
import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.api.resource.repository.ResourceRepository;
import com.refinedmods.refinedstorage.common.api.RefinedStorageClientApi;
import com.refinedmods.refinedstorage.common.api.grid.GridScrollMode;
import com.refinedmods.refinedstorage.common.api.grid.strategy.GridExtractionStrategy;
import com.refinedmods.refinedstorage.common.api.grid.strategy.GridScrollingStrategy;
import com.refinedmods.refinedstorage.common.api.grid.view.AbstractGridResource;
import com.refinedmods.refinedstorage.common.api.grid.view.GridResource;
import com.refinedmods.refinedstorage.common.api.grid.view.GridResourceAttributeKey;
import com.refinedmods.refinedstorage.common.api.support.resource.PlatformResourceKey;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceRendering;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceType;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.Nullable;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractTypedGridResource<R extends PlatformResourceKey> extends AbstractGridResource<R> {
    private final int id;
    private final ResourceRendering rendering;
    private final ResourceType resourceType;
    private final long autocraftingAmount;
    private final List<Component> tooltip;

    protected AbstractTypedGridResource(final R resource,
                                        final String name,
                                        final Function<GridResourceAttributeKey, Set<String>> attributes,
                                        final Class<R> resourceClass,
                                        final Type type,
                                        final ResourceType resourceType,
                                        final long autocraftingAmount) {
        super(resource, name, attributes);
        this.id = Types.TYPE_REGISTRY.getId(type);
        this.rendering = RefinedStorageClientApi.INSTANCE.getResourceRendering(resourceClass);
        this.resourceType = resourceType;
        this.autocraftingAmount = autocraftingAmount;
        this.tooltip = List.of(type.getDisplayName());
    }

    @Override
    public final int getRegistryId() {
        return this.id;
    }

    @Override
    public List<ClientTooltipComponent> getExtractionHints(final ItemStack carriedStack,
                                                           final ResourceRepository<GridResource> repository) {
        return List.of();
    }

    @Nullable
    @Override
    public ResourceAmount getAutocraftingRequest() {
        return new ResourceAmount(this.resource, this.autocraftingAmount);
    }

    @Override
    public boolean canExtract(final ItemStack carriedStack, final ResourceRepository<GridResource> repository) {
        return false;
    }

    @Override
    public final void onExtract(final GridExtractMode extractMode,
                                final boolean cursor,
                                final GridExtractionStrategy extractionStrategy) {
        extractionStrategy.onExtract(this.resource, extractMode, cursor);
    }

    @Override
    public final void onScroll(final GridScrollMode scrollMode, final GridScrollingStrategy scrollingStrategy) {
        // no-op
    }

    @Override
    public final void render(final GuiGraphics graphics, final int x, final int y) {
        this.rendering.render(this.resource, graphics, x, y);
    }

    @Override
    public final String getDisplayedAmount(final ResourceRepository<GridResource> repository) {
        return this.rendering.formatAmount(this.getAmount(repository), true);
    }

    @Override
    public final String getAmountInTooltip(final ResourceRepository<GridResource> repository) {
        return this.rendering.formatAmount(this.getAmount(repository));
    }

    @Override
    public final boolean belongsToResourceType(final ResourceType resourceType) {
        return resourceType == this.resourceType;
    }

    @Override
    public final List<Component> getTooltip() {
        return this.tooltip;
    }

    @Override
    public final Optional<TooltipComponent> getTooltipImage() {
        return Optional.empty();
    }
}
