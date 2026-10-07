package com.ultramega.refinedtypes.type.essentia;

import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.common.api.support.resource.FuzzyModeNormalizer;
import com.refinedmods.refinedstorage.common.api.support.resource.PlatformResourceKey;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceTag;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceType;

import java.util.List;

import com.leclowndu93150.thaumaturge.api.aspect.Aspects;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jspecify.annotations.Nullable;

import static com.ultramega.refinedtypes.type.essentia.EssentiaResourceType.DEFAULT_TRANSFER_AMOUNT;

public record EssentiaResource(Identifier aspectId) implements PlatformResourceKey, FuzzyModeNormalizer {
    public static EssentiaResource of(final Holder<IAspect> aspect) {
        return new EssentiaResource(aspect.unwrapKey().orElseThrow().identifier());
    }

    public static List<EssentiaResource> allRegistered() {
        final var server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            return List.of();
        }
        return server.registryAccess()
            .lookupOrThrow(IAspect.REGISTRY_KEY)
            .listElements()
            .map(EssentiaResource::of)
            .toList();
    }

    @Nullable
    public Holder<IAspect> resolve(final HolderLookup.Provider registries) {
        return Aspects.resolve(registries, net.minecraft.resources.ResourceKey.create(IAspect.REGISTRY_KEY, this.aspectId));
    }

    @Override
    public long getInterfaceExportLimit() {
        return EssentiaResourceType.INSTANCE.getInterfaceExportLimit();
    }

    @Override
    public long getProcessingPatternLimit() {
        return DEFAULT_TRANSFER_AMOUNT * 1_000_000L;
    }

    @Override
    public List<ResourceTag> getTags() {
        return List.of();
    }

    @Override
    public ResourceKey normalize() {
        return this;
    }

    @Override
    public ResourceType getResourceType() {
        return EssentiaResourceType.INSTANCE;
    }
}
