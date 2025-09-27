package com.ultramega.refinedtypes.grid;

import com.google.common.base.Suppliers;
import com.ultramega.refinedtypes.grid.energy.EnergyGridResource;
import com.ultramega.refinedtypes.grid.soul.SoulGridResource;
import com.ultramega.refinedtypes.grid.source.SourceGridResource;
import com.ultramega.refinedtypes.registry.Types;
import com.ultramega.refinedtypes.type.Type;
import com.ultramega.refinedtypes.type.energy.EnergyResource;
import com.ultramega.refinedtypes.type.soul.SoulResource;
import com.ultramega.refinedtypes.type.source.SourceResource;

import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.resource.repository.ResourceRepositoryMapper;
import com.refinedmods.refinedstorage.common.api.grid.GridResourceAttributeKeys;
import com.refinedmods.refinedstorage.common.api.grid.view.GridResource;
import com.refinedmods.refinedstorage.common.api.grid.view.GridResourceAttributeKey;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import net.minecraft.core.Holder;
import net.neoforged.fml.ModList;

public class TypeGridResourceFactory implements ResourceRepositoryMapper<GridResource> {
    @Override
    public GridResource apply(final ResourceKey resource) {
        final Type type = this.extractType(resource);

        final String name = this.getName(type);
        final String modId = this.getModId(type);
        final String modName = this.getModName(modId);
        final Map<GridResourceAttributeKey, Supplier<Set<String>>> attributes = Map.of(
            GridResourceAttributeKeys.MOD_ID, Suppliers.ofInstance(Set.of(modId)),
            GridResourceAttributeKeys.MOD_NAME, Suppliers.ofInstance(Set.of(modName)),
            GridResourceAttributeKeys.TAGS, Suppliers.ofInstance(this.getTags(type)),
            GridResourceAttributeKeys.TOOLTIP, Suppliers.ofInstance(Set.of(this.getTooltip(type)))
        );

        final Function<GridResourceAttributeKey, Set<String>> attributesFunction = k -> attributes.getOrDefault(k, Collections::emptySet).get();
        return switch (resource) {
            case EnergyResource energyResource -> new EnergyGridResource(energyResource, name, attributesFunction);
            case SourceResource sourceResource -> new SourceGridResource(sourceResource, name, attributesFunction);
            case SoulResource soulResource -> new SoulGridResource(soulResource, name, attributesFunction);
            default -> throw new IllegalArgumentException("Unsupported resource: " + resource);
        };
    }

    private Type extractType(final ResourceKey resource) { //TODO: remove once Resource Types have an abstract class
        return switch (resource) {
            case EnergyResource(Type type) -> type;
            case SourceResource(Type type) -> type;
            case SoulResource(Type type) -> type;
            default -> throw new IllegalArgumentException("Type couldn't be identified for resource: " + resource);
        };
    }

    private Set<String> getTags(final Type type) {
        return Types.TYPE_REGISTRY.getResourceKey(type)
            .flatMap(Types.TYPE_REGISTRY::getHolder)
            .stream()
            .flatMap(Holder::tags)
            .map(tagKey -> tagKey.location().getPath())
            .collect(Collectors.toSet());
    }

    private String getModId(final Type type) {
        return Types.TYPE_REGISTRY.getKey(type).getNamespace();
    }

    private String getTooltip(final Type type) {
        return this.getName(type);
    }

    private String getModName(final String modId) {
        return ModList
            .get()
            .getModContainerById(modId)
            .map(container -> container.getModInfo().getDisplayName())
            .orElse("");
    }

    private String getName(final Type type) {
        return type.getDisplayName().getString();
    }
}
