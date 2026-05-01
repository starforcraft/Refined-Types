package com.ultramega.refinedtypes.compat.jei;

import com.ultramega.refinedtypes.registry.Types;
import com.ultramega.refinedtypes.type.Type;
import com.ultramega.refinedtypes.type.TypeStack;
import com.ultramega.refinedtypes.type.energy.EnergyResource;

import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.common.api.support.resource.PlatformResourceKey;
import com.refinedmods.refinedstorage.common.api.support.resource.RecipeModIngredientConverter;

import java.util.Optional;

import static com.ultramega.refinedtypes.type.energy.EnergyResource.ENERGY_RESOURCE;

public class JEIRecipeModIngredientConverter implements RecipeModIngredientConverter {
    @Override
    public Optional<PlatformResourceKey> convertToResource(final Object ingredient) {
        if (ingredient instanceof TypeStack stack) {
            if (stack.type() == Types.FE.get()) {
                return Optional.of(ENERGY_RESOURCE);
            } /*else if (stack.type() == Types.SOURCE.get()) {
                return Optional.of(SOURCE_RESOURCE);
            } else if (stack.type() == Types.SOUL.get()) {
                return Optional.of(SOUL_RESOURCE);
            }*/
        }
        return Optional.empty();
    }

    @Override
    public Optional<ResourceAmount> convertToResourceAmount(final Object ingredient) {
        if (ingredient instanceof TypeStack(Type type, long amount)) {
            if (type == Types.FE.get()) {
                return Optional.of(new ResourceAmount(ENERGY_RESOURCE, amount));
            } /*else if (type == Types.SOURCE.get()) {
                return Optional.of(new ResourceAmount(SOURCE_RESOURCE, amount));
            } else if (type == Types.SOUL.get()) {
                return Optional.of(new ResourceAmount(SOUL_RESOURCE, amount));
            }*/
        }
        return Optional.empty();
    }

    @Override
    public Optional<Object> convertToIngredient(final PlatformResourceKey resourceKey) {
        return switch (resourceKey) {
            case EnergyResource(Type type) -> Optional.of(new TypeStack(type, 1));
//            case SourceResource(Type type) -> Optional.of(new TypeStack(type, FluidType.BUCKET_VOLUME));
//            case SoulResource(Type type) -> Optional.of(new TypeStack(type, 1));
            default -> Optional.empty();
        };
    }
}
