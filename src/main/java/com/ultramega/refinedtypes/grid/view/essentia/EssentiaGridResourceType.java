package com.ultramega.refinedtypes.grid.view.essentia;

import com.ultramega.refinedtypes.RefinedTypesUtil;
import com.ultramega.refinedtypes.type.essentia.EssentiaResource;
import com.ultramega.refinedtypes.type.essentia.EssentiaResourceType;

import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.common.api.RefinedStorageClientApi;
import com.refinedmods.refinedstorage.common.api.grid.GridResourceAttributeKeys;
import com.refinedmods.refinedstorage.common.api.grid.view.GridResource;
import com.refinedmods.refinedstorage.common.api.grid.view.GridResourceType;

import java.util.Set;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;

import static com.ultramega.refinedtypes.RefinedTypesUtil.createRefinedTypesIdentifier;

public final class EssentiaGridResourceType implements GridResourceType {
    public static final EssentiaGridResourceType INSTANCE = new EssentiaGridResourceType();

    private static final MapCodec<GridResource> CODEC = EssentiaResourceType.CODEC.fieldOf("essentia")
        .xmap(INSTANCE, resource -> ((EssentiaGridResource) resource).getEssentia());

    private static final MutableComponent TITLE = RefinedTypesUtil.createRefinedTypesTranslation(
        "misc",
        "resource_type.essentia"
    );
    private static final Identifier SPRITE = createRefinedTypesIdentifier("essentia_resource_type");

    @Override
    public GridResource apply(final ResourceKey resource) {
        final var essentia = (EssentiaResource) resource;
        final String name = RefinedStorageClientApi.INSTANCE.getResourceRendering(EssentiaResource.class)
            .getDisplayName(essentia).getString();
        return new EssentiaGridResource(essentia, name, key -> {
            if (key.equals(GridResourceAttributeKeys.MOD_ID)) {
                return Set.of(essentia.aspectId().getNamespace());
            }
            if (key.equals(GridResourceAttributeKeys.MOD_NAME)) {
                return Set.of("Thaumaturge");
            }
            if (key.equals(GridResourceAttributeKeys.TOOLTIP)) {
                return Set.of(name, "Essentia");
            }
            return Set.of();
        });
    }

    @Override
    public Class<? extends ResourceKey> getResourceType() {
        return EssentiaResource.class;
    }

    @Override
    public MapCodec<GridResource> getMapCodec() {
        return CODEC;
    }

    @Override
    public MutableComponent getTitle() {
        return TITLE;
    }

    @Override
    public Identifier getSprite() {
        return SPRITE;
    }
}
