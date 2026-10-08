package com.ultramega.refinedtypes.compat.jei;

import com.ultramega.refinedtypes.type.essentia.EssentiaResource;

import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.common.api.support.resource.PlatformResourceKey;
import com.refinedmods.refinedstorage.common.api.support.resource.RecipeModIngredientConverter;

import java.util.Optional;

import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import net.minecraft.client.Minecraft;

public class EssentiaJEIRecipeModIngredientConverter implements RecipeModIngredientConverter {
    @Override
    public Optional<PlatformResourceKey> convertToResource(final Object ingredient) {
        if (ingredient instanceof AspectInstance aspect) {
            return aspect.aspect().unwrapKey().map(key -> new EssentiaResource(key.identifier()));
        }
        return Optional.empty();
    }

    @Override
    public Optional<ResourceAmount> convertToResourceAmount(final Object ingredient) {
        if (ingredient instanceof AspectInstance aspect) {
            return this.convertToResource(aspect).map(resource -> new ResourceAmount(resource, aspect.amount()));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Object> convertToIngredient(final PlatformResourceKey resourceKey) {
        if (!(resourceKey instanceof EssentiaResource essentia)) {
            return Optional.empty();
        }
        final var level = Minecraft.getInstance().level;
        if (level == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(essentia.resolve(level.registryAccess()))
            .map(aspect -> new AspectInstance(aspect, 1));
    }
}
