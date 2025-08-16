package com.ultramega.refinedtypes.type;

import com.ultramega.refinedtypes.registry.Types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record TypeStack(Type type, long amount) {
    public static final Codec<TypeStack> CODEC = RecordCodecBuilder.create(i -> i.group(
        Types.CODEC.fieldOf("type").forGetter(p -> p.type),
        Codec.LONG.fieldOf("amount").forGetter(p -> p.amount)
    ).apply(i, TypeStack::new));

    public boolean isEmpty() {
        return this.amount <= 0;
    }
}
