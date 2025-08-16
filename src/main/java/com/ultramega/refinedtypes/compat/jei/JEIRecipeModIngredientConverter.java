package com.ultramega.refinedtypes.compat.jei;

import com.ultramega.refinedtypes.registry.Types;
import com.ultramega.refinedtypes.type.Type;
import com.ultramega.refinedtypes.type.TypeStack;
import com.ultramega.refinedtypes.type.energy.EnergyResource;
import com.ultramega.refinedtypes.type.soul.SoulResource;
import com.ultramega.refinedtypes.type.source.SourceResource;

import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.common.api.support.resource.PlatformResourceKey;
import com.refinedmods.refinedstorage.common.api.support.resource.RecipeModIngredientConverter;

import java.util.Optional;

import net.neoforged.neoforge.fluids.FluidType;

import static com.ultramega.refinedtypes.type.energy.EnergyResource.createEnergyResource;
import static com.ultramega.refinedtypes.type.soul.SoulResource.createSoulResource;
import static com.ultramega.refinedtypes.type.source.SourceResource.createSourceResource;

public class JEIRecipeModIngredientConverter implements RecipeModIngredientConverter {
    @Override
    public Optional<PlatformResourceKey> convertToResource(final Object ingredient) {
        if (ingredient instanceof TypeStack stack) {
            if (stack.type() == Types.FE.get()) {
                return Optional.of(createEnergyResource());
            } else if (stack.type() == Types.SOURCE.get()) {
                return Optional.of(createSourceResource());
            } else if (stack.type() == Types.SOUL.get()) {
                return Optional.of(createSoulResource());
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<ResourceAmount> convertToResourceAmount(final Object ingredient) {
        if (ingredient instanceof TypeStack stack) {
            if (stack.type() == Types.FE.get()) {
                return Optional.of(new ResourceAmount(createEnergyResource(), stack.amount()));
            } else if (stack.type() == Types.SOURCE.get()) {
                return Optional.of(new ResourceAmount(createSourceResource(), stack.amount()));
            } else if (stack.type() == Types.SOUL.get()) {
                return Optional.of(new ResourceAmount(createSoulResource(), stack.amount()));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Object> convertToIngredient(final PlatformResourceKey resourceKey) {
        return switch (resourceKey) {
            case EnergyResource(Type type) -> Optional.of(new TypeStack(type, 1));
            case SourceResource(Type type) -> Optional.of(new TypeStack(type, FluidType.BUCKET_VOLUME));
            case SoulResource(Type type) -> Optional.of(new TypeStack(type, 1));
            default -> Optional.empty();
        };
    }
}
