package com.ultramega.refinedtypes.compat.jei;

import com.ultramega.refinedtypes.registry.Types;
import com.ultramega.refinedtypes.type.TypeStack;
import com.ultramega.refinedtypes.type.energy.EnergyResource;
import com.ultramega.refinedtypes.type.soul.SoulResource;
import com.ultramega.refinedtypes.type.source.SourceResource;

import com.refinedmods.refinedstorage.common.api.RefinedStorageClientApi;

import java.util.List;
import javax.annotation.Nullable;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.registration.IModIngredientRegistration;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.fluids.FluidType;

import static com.ultramega.refinedtypes.RefinedTypesUtil.createRefinedTypesIdentifier;
import static com.ultramega.refinedtypes.type.energy.EnergyResource.createEnergyResource;
import static com.ultramega.refinedtypes.type.soul.SoulResource.createSoulResource;
import static com.ultramega.refinedtypes.type.source.SourceResource.createSourceResource;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    public static final IIngredientType<TypeStack> TYPE = () -> TypeStack.class;
    public static final IIngredientHelper<TypeStack> TYPE_HELPER = new IIngredientHelper<>() {
        @Override
        public IIngredientType<TypeStack> getIngredientType() {
            return TYPE;
        }

        @Override
        public String getDisplayName(final TypeStack ingredient) {
            return ingredient.type().getDisplayName().getString();
        }

        @SuppressWarnings("removal")
        @Override
        public String getUniqueId(final TypeStack ingredient, final UidContext context) {
            return ingredient.type().name();
        }

        @Override
        public ResourceLocation getResourceLocation(final TypeStack ingredient) {
            return createRefinedTypesIdentifier(ingredient.type().name());
        }

        @Override
        public TypeStack copyIngredient(final TypeStack ingredient) {
            return ingredient;
        }

        @Override
        public String getErrorInfo(@Nullable final TypeStack ingredient) {
            return ingredient != null ? ingredient.type().getDisplayName().getString() : "Error";
        }
    };
    public static final IIngredientRenderer<TypeStack> TYPE_RENDER = new IIngredientRenderer<>() {
        @Override
        public void render(final GuiGraphics graphics, final TypeStack ingredient) {
            if (ingredient.type() == Types.FE.get()) {
                RefinedStorageClientApi.INSTANCE.getResourceRendering(EnergyResource.class).render(createEnergyResource(), graphics, 0, 0);
            } else if (ingredient.type() == Types.SOURCE.get()) {
                RefinedStorageClientApi.INSTANCE.getResourceRendering(SourceResource.class).render(createSourceResource(), graphics, 0, 0);
            } else if (ingredient.type() == Types.SOUL.get()) {
                RefinedStorageClientApi.INSTANCE.getResourceRendering(SoulResource.class).render(createSoulResource(), graphics, 0, 0);
            }
        }

        @SuppressWarnings("removal")
        @Override
        public List<Component> getTooltip(final TypeStack ingredient, final TooltipFlag tooltipFlag) {
            return List.of(ingredient.type().getDisplayName());
        }
    };

    private static final ResourceLocation ID = createRefinedTypesIdentifier("plugin");

    @Override
    public void registerIngredients(final IModIngredientRegistration registration) {
        final List<TypeStack> types = List.of(
            new TypeStack(Types.FE.get(), 1),
            new TypeStack(Types.SOURCE.get(), FluidType.BUCKET_VOLUME),
            new TypeStack(Types.SOUL.get(), 1)
        );
        registration.register(TYPE, types, TYPE_HELPER, TYPE_RENDER, TypeStack.CODEC);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }
}
