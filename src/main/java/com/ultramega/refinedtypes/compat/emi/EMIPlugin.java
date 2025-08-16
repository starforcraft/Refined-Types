package com.ultramega.refinedtypes.compat.emi;

import com.ultramega.refinedtypes.registry.Types;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;

@EmiEntrypoint
public class EMIPlugin implements EmiPlugin {
    @Override
    public void register(final EmiRegistry registry) {
        Types.TYPE_REGISTRY.stream().forEach(type -> registry.addEmiStack(new TypeEmiStack(type, 1)));
    }
}
