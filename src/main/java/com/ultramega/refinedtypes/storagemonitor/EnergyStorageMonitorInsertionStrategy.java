package com.ultramega.refinedtypes.storagemonitor;

import com.ultramega.refinedtypes.type.TypeOperationResult;
import com.ultramega.refinedtypes.type.energy.EnergyResource;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.network.Network;
import com.refinedmods.refinedstorage.api.network.storage.StorageNetworkComponent;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.Actor;
import com.refinedmods.refinedstorage.api.storage.root.RootStorage;
import com.refinedmods.refinedstorage.common.api.storagemonitor.StorageMonitorInsertionStrategy;

import java.util.Optional;

import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import static com.ultramega.refinedtypes.RefinedTypesUtil.dischargeContainer;
import static com.ultramega.refinedtypes.type.energy.EnergyResource.ENERGY_RESOURCE;

public class EnergyStorageMonitorInsertionStrategy implements StorageMonitorInsertionStrategy {
    @Override
    public Optional<ItemStack> insert(final ResourceKey configuredResource,
                                      final ItemStack stack,
                                      final Actor actor,
                                      final Network network) {
        if (!(configuredResource instanceof EnergyResource configuredEnergyResource)) {
            return Optional.empty();
        }
        final RootStorage rootStorage = network.getComponent(StorageNetworkComponent.class);
        return dischargeContainer(stack)
            .map(extracted -> this.tryInsert(actor, configuredEnergyResource, extracted, rootStorage))
            .map(extracted -> this.doInsert(actor, extracted, rootStorage));
    }

    @Nullable
    private TypeOperationResult tryInsert(final Actor actor,
                                final EnergyResource configuredResource,
                                final TypeOperationResult result,
                                final RootStorage rootStorage) {
        if (!result.type().equals(configuredResource.type())) {
            return null;
        }
        final long insertedSimulated = rootStorage.insert(ENERGY_RESOURCE, result.amount(), Action.SIMULATE, actor);
        final boolean insertedSuccessfully = insertedSimulated == result.amount();
        return insertedSuccessfully ? result : null;
    }

    private ItemStack doInsert(final Actor actor, final TypeOperationResult result, final RootStorage rootStorage) {
        rootStorage.insert(ENERGY_RESOURCE, result.amount(), Action.EXECUTE, actor);
        return result.container();
    }
}
