package com.ultramega.refinedtypes.compat.emi;

import com.ultramega.refinedtypes.type.Type;
import com.ultramega.refinedtypes.type.energy.EnergyResource;
import com.ultramega.refinedtypes.type.soul.SoulResource;
import com.ultramega.refinedtypes.type.source.SourceResource;

import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.common.api.support.resource.PlatformResourceKey;
import com.refinedmods.refinedstorage.common.api.support.resource.RecipeModIngredientConverter;

import java.util.Optional;

import dev.emi.emi.api.stack.EmiStack;
import net.neoforged.neoforge.fluids.FluidType;

public class EmiEnergyResourceModIngredientConverter implements RecipeModIngredientConverter {
    @Override
    public Optional<PlatformResourceKey> convertToResource(final Object ingredient) {
        if (ingredient instanceof TypeEmiStack emiStack) {
            return this.findResourceType(emiStack);
        }
        return Optional.empty();
    }

    @Override
    public Optional<ResourceAmount> convertToResourceAmount(final Object ingredient) {
        if (ingredient instanceof EmiStack emiStack) {
            return this.findResourceType(emiStack)
                .map(resource -> new ResourceAmount(resource, emiStack.getAmount()));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Object> convertToIngredient(final PlatformResourceKey resourceKey) {
        return switch (resourceKey) {
            case EnergyResource(Type type) -> Optional.of(this.createEmiStack(type, 1));
            case SourceResource(Type type) -> Optional.of(this.createEmiStack(type, FluidType.BUCKET_VOLUME));
            case SoulResource(Type type) -> Optional.of(this.createEmiStack(type, 1));
            default -> Optional.empty();
        };
    }

    public EmiStack createEmiStack(final Type type, final long size) {
        if (size < 1) {
            return EmiStack.EMPTY;
        }
        return new TypeEmiStack(type, size);
    }

    public Optional<PlatformResourceKey> findResourceType(final EmiStack stack) {
        if (stack instanceof TypeEmiStack typeEmiStack) {
            return Optional.of(typeEmiStack.getResource());
        }
        return Optional.empty();
    }
}
