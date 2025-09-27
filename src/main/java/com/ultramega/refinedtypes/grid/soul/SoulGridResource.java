package com.ultramega.refinedtypes.grid.soul;

import com.ultramega.refinedtypes.registry.Types;
import com.ultramega.refinedtypes.type.soul.SoulResource;
import com.ultramega.refinedtypes.type.soul.SoulResourceType;

import com.refinedmods.refinedstorage.api.network.node.grid.GridExtractMode;
import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.api.resource.repository.ResourceRepository;
import com.refinedmods.refinedstorage.common.Platform;
import com.refinedmods.refinedstorage.common.api.RefinedStorageClientApi;
import com.refinedmods.refinedstorage.common.api.grid.GridScrollMode;
import com.refinedmods.refinedstorage.common.api.grid.strategy.GridExtractionStrategy;
import com.refinedmods.refinedstorage.common.api.grid.strategy.GridScrollingStrategy;
import com.refinedmods.refinedstorage.common.api.grid.view.AbstractGridResource;
import com.refinedmods.refinedstorage.common.api.grid.view.GridResource;
import com.refinedmods.refinedstorage.common.api.grid.view.GridResourceAttributeKey;
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

public class SoulGridResource extends AbstractGridResource<SoulResource> {
    private final int id;
    private final ResourceRendering rendering;
    private final List<Component> tooltip;

    public SoulGridResource(final SoulResource resource,
                            final String name,
                            final Function<GridResourceAttributeKey, Set<String>> attributes) {
        super(resource, name, attributes);
        this.id = Types.TYPE_REGISTRY.getId(resource.type());
        this.rendering = RefinedStorageClientApi.INSTANCE.getResourceRendering(SoulResource.class);
        this.tooltip = List.of(resource.type().getDisplayName());
    }

    @Override
    public int getRegistryId() {
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
        return new ResourceAmount(this.resource, Platform.INSTANCE.getBucketAmount());
    }

    @Override
    public boolean canExtract(final ItemStack carriedStack, final ResourceRepository<GridResource> repository) {
        // There's no source cap for items
        return false;
    }

    @Override
    public void onExtract(final GridExtractMode extractMode,
                          final boolean cursor,
                          final GridExtractionStrategy extractionStrategy) {
        extractionStrategy.onExtract(this.resource, extractMode, cursor);
    }

    @Override
    public void onScroll(final GridScrollMode scrollMode, final GridScrollingStrategy scrollingStrategy) {
        // no-op
    }

    @Override
    public void render(final GuiGraphics graphics, final int x, final int y) {
        this.rendering.render(this.resource, graphics, x, y);
    }

    @Override
    public String getDisplayedAmount(final ResourceRepository<GridResource> repository) {
        return this.rendering.formatAmount(this.getAmount(repository), true);
    }

    @Override
    public String getAmountInTooltip(final ResourceRepository<GridResource> repository) {
        return this.rendering.formatAmount(this.getAmount(repository));
    }

    @Override
    public boolean belongsToResourceType(final ResourceType resourceType) {
        return resourceType == SoulResourceType.INSTANCE;
    }

    @Override
    public List<Component> getTooltip() {
        return this.tooltip;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage() {
        return Optional.empty();
    }
}
