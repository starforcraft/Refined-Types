package com.ultramega.refinedtypes.compat.jei;

import com.ultramega.refinedtypes.registry.Types;
import com.ultramega.refinedtypes.type.TypeStack;
import com.ultramega.refinedtypes.type.energy.EnergyResource;

import com.refinedmods.refinedstorage.common.api.RefinedStorageClientApi;

import java.util.List;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.registration.IModIngredientRegistration;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.TooltipFlag;
import org.jspecify.annotations.Nullable;

import static com.ultramega.refinedtypes.RefinedTypesUtil.createRefinedTypesIdentifier;
import static com.ultramega.refinedtypes.type.energy.EnergyResource.ENERGY_RESOURCE;

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

        @Override
        public String getUid(final TypeStack ingredient, final UidContext context) {
            return ingredient.type().name();
        }

        @Override
        public Identifier getIdentifier(final TypeStack ingredient) {
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
        public void render(final GuiGraphicsExtractor graphics, final TypeStack ingredient) {
            if (ingredient.type() == Types.FE.get()) {
                RefinedStorageClientApi.INSTANCE.getResourceRendering(EnergyResource.class).render(ENERGY_RESOURCE, graphics, 0, 0);
            } /*else if (ingredient.type() == Types.SOURCE.get()) {
                RefinedStorageClientApi.INSTANCE.getResourceRendering(SourceResource.class).render(SOURCE_RESOURCE, graphics, 0, 0);
            } else if (ingredient.type() == Types.SOUL.get()) {
                RefinedStorageClientApi.INSTANCE.getResourceRendering(SoulResource.class).render(SOUL_RESOURCE, graphics, 0, 0);
            }*/
        }

        @SuppressWarnings("removal")
        @Override
        public List<Component> getTooltip(final TypeStack ingredient, final TooltipFlag tooltipFlag) {
//            if (ingredient.type() == Types.SOUL.get()) {
//                return List.of(ingredient.type().getDisplayName(), Component.translatable("item.refinedtypes.soul.help")
//                    .withStyle(ChatFormatting.AQUA));
//            }
            return List.of(ingredient.type().getDisplayName());
        }
    };

    private static final Identifier ID = createRefinedTypesIdentifier("plugin");

    @Override
    public void registerIngredients(final IModIngredientRegistration registration) {
        final List<TypeStack> types = List.of(
            new TypeStack(Types.FE.get(), 1)/*,
            new TypeStack(Types.SOURCE.get(), FluidType.BUCKET_VOLUME),
            new TypeStack(Types.SOUL.get(), 1)*/
        );
        registration.register(TYPE, types, TYPE_HELPER, TYPE_RENDER, TypeStack.CODEC);
    }

    @Override
    public Identifier getPluginUid() {
        return ID;
    }
}
