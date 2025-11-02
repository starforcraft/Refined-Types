package com.ultramega.refinedtypes.compat.emi;

import com.ultramega.refinedtypes.registry.Types;
import com.ultramega.refinedtypes.type.Type;
import com.ultramega.refinedtypes.type.TypeStack;
import com.ultramega.refinedtypes.type.energy.EnergyResource;
import com.ultramega.refinedtypes.type.soul.SoulResource;
import com.ultramega.refinedtypes.type.source.SourceResource;

import com.refinedmods.refinedstorage.common.api.RefinedStorageClientApi;
import com.refinedmods.refinedstorage.common.api.support.resource.PlatformResourceKey;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceRendering;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

import dev.emi.emi.api.render.EmiTooltipComponents;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import static com.ultramega.refinedtypes.RefinedTypesUtil.createRefinedTypesIdentifier;
import static com.ultramega.refinedtypes.type.energy.EnergyResource.createEnergyResource;
import static com.ultramega.refinedtypes.type.soul.SoulResource.createSoulResource;
import static com.ultramega.refinedtypes.type.source.SourceResource.createSourceResource;

public class TypeEmiStack extends EmiStack {
    private final Type type;
    private final ResourceRendering rendering;
    private final PlatformResourceKey resource;

    public TypeEmiStack(final Type type, final long amount) {
        this.type = type;
        this.amount = amount;

        if (this.type == Types.FE.get()) {
            this.rendering = RefinedStorageClientApi.INSTANCE.getResourceRendering(EnergyResource.class);
            this.resource = createEnergyResource();
        } else if (this.type == Types.SOURCE.get()) {
            this.rendering = RefinedStorageClientApi.INSTANCE.getResourceRendering(SourceResource.class);
            this.resource = createSourceResource();
        } else if (this.type == Types.SOUL.get()) {
            this.rendering = RefinedStorageClientApi.INSTANCE.getResourceRendering(SoulResource.class);
            this.resource = createSoulResource();
        } else {
            throw new RuntimeException("Invalid resource type " + type);
        }
    }

    @Override
    public EmiStack copy() {
        return new TypeEmiStack(this.type, this.amount);
    }

    @Override
    public void render(final GuiGraphics graphics, final int x, final int y, final float delta, final int flags) {
        this.rendering.render(this.resource, graphics, x, y);
    }

    @Override
    public boolean isEmpty() {
        return this.amount <= 0;
    }

    @Override
    public DataComponentPatch getComponentChanges() {
        return DataComponentPatch.EMPTY;
    }

    @Override
    public Object getKey() {
        return this.type.name();
    }

    @Override
    public ResourceLocation getId() {
        return createRefinedTypesIdentifier(this.type.name());
    }

    @Override
    public List<Component> getTooltipText() {
        if (this.isEmpty()) {
            return Collections.emptyList();
        }
        return List.of(this.getName(), this.type.tooltip());
    }

    @Override
    public List<ClientTooltipComponent> getTooltip() {
        final List<ClientTooltipComponent> tooltips = this.getTooltipText().stream()
            .map(EmiTooltipComponents::of)
            .collect(Collectors.toList());
        if (this.amount > 1) {
            tooltips.add(EmiTooltipComponents.of(Component.literal(this.rendering.formatAmount(this.amount))));
        }

        EmiTooltipComponents.appendModName(tooltips, this.getId().getNamespace());
        tooltips.addAll(super.getTooltip());
        return tooltips;
    }

    @Override
    public Component getName() {
        return this.type.getDisplayName();
    }

    @Nullable
    public TypeStack getStack() {
        if (this.isEmpty()) {
            return null;
        }
        return new TypeStack(this.type, this.amount);
    }

    public PlatformResourceKey getResource() {
        return this.resource;
    }
}
