package com.ultramega.refinedtypes.registry;

import com.ultramega.refinedtypes.type.Type;

import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import static com.ultramega.refinedtypes.RefinedTypesUtil.MOD_ID;
import static com.ultramega.refinedtypes.RefinedTypesUtil.createRefinedTypesIdentifier;

public final class Types {
    public static final ResourceKey<Registry<Type>> TYPE_REGISTRY_KEY = ResourceKey.createRegistryKey(createRefinedTypesIdentifier("type"));
    public static final Registry<Type> TYPE_REGISTRY = new RegistryBuilder<>(TYPE_REGISTRY_KEY)
        .sync(true)
        .create();

    public static final Codec<Type> CODEC = TYPE_REGISTRY.byNameCodec();
    public static final StreamCodec<RegistryFriendlyByteBuf, Type> STREAM_CODEC = ByteBufCodecs.registry(TYPE_REGISTRY_KEY);

    public static final DeferredRegister<Type> TYPES = DeferredRegister.create(TYPE_REGISTRY, MOD_ID);

    public static final Supplier<Type> FE = TYPES.register("fe", () -> new Type("FE",
        createRefinedTypesIdentifier("types/fe")));
    public static final Supplier<Type> SOURCE = TYPES.register("source", () -> new Type("Source",
        ResourceLocation.fromNamespaceAndPath("ars_nouveau", "block/mana_still")));
    public static final Supplier<Type> SOUL = TYPES.register("soul", () -> new Type("Soul", null));

    private Types() {
    }
}
