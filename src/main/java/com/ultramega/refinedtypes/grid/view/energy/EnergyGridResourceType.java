package com.ultramega.refinedtypes.grid.view.energy;

import com.ultramega.refinedtypes.RefinedTypesUtil;
import com.ultramega.refinedtypes.grid.TypeGridResourceType;
import com.ultramega.refinedtypes.type.energy.EnergyResource;
import com.ultramega.refinedtypes.type.energy.EnergyResourceType;

import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.common.api.RefinedStorageApi;
import com.refinedmods.refinedstorage.common.api.grid.view.GridResource;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;

import static com.ultramega.refinedtypes.ModInitializer.ENERGY_ID;
import static com.ultramega.refinedtypes.RefinedTypesUtil.createRefinedTypesIdentifier;
import static com.ultramega.refinedtypes.type.energy.EnergyResource.ENERGY_RESOURCE;

public class EnergyGridResourceType extends TypeGridResourceType {
    public static final EnergyGridResourceType INSTANCE = new EnergyGridResourceType();

    static final MapCodec<GridResource> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(EnergyResourceType.CODEC
            .fieldOf(ENERGY_ID.getPath())
            .forGetter(g -> ENERGY_RESOURCE)
        ).apply(instance, energyResource -> RefinedStorageApi.INSTANCE.getGridResourceRepositoryMapper()
            .apply(energyResource)));

    private static final MutableComponent TITLE = RefinedTypesUtil.createRefinedTypesTranslation(
        "misc",
        "resource_type.energy"
    );
    private static final Identifier SPRITE = createRefinedTypesIdentifier("energy_resource_type");

    private EnergyGridResourceType() {
    }

    @Override
    public Class<? extends ResourceKey> getResourceType() {
        return EnergyResource.class;
    }

    @Override
    public MapCodec<GridResource> getMapCodec() {
        return MAP_CODEC;
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
