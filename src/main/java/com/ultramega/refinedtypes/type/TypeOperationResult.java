package com.ultramega.refinedtypes.type;

import net.minecraft.world.item.ItemStack;

public record TypeOperationResult(ItemStack container, Type type, long amount) {
}
