package com.ultramega.refinedtypes.storagemonitor;

import com.ultramega.refinedtypes.registry.Types;
import com.ultramega.refinedtypes.type.TypeStack;
import com.ultramega.refinedtypes.type.energy.EnergyResource;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.network.Network;
import com.refinedmods.refinedstorage.api.network.storage.StorageNetworkComponent;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.Actor;
import com.refinedmods.refinedstorage.api.storage.root.RootStorage;
import com.refinedmods.refinedstorage.common.api.storagemonitor.StorageMonitorInsertionStrategy;

import java.util.Optional;
import javax.annotation.Nullable;

import dev.technici4n.grandpower.api.ILongEnergyStorage;
import net.minecraft.world.item.ItemStack;

import static com.ultramega.refinedtypes.type.energy.EnergyResource.createEnergyResource;

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
        final ItemStack modifiedStack = stack.copy();
        return Optional.ofNullable(modifiedStack.getCapability(ILongEnergyStorage.ITEM))
            .map(handler -> this.handleInsert(actor, configuredEnergyResource, handler, rootStorage, modifiedStack));
    }

    @Nullable
    private ItemStack handleInsert(final Actor actor,
                                   final EnergyResource configuredEnergyResource,
                                   final ILongEnergyStorage handler,
                                   final RootStorage rootStorage,
                                   final ItemStack modifiedStack) {
        final TypeStack extractedSimulated = new TypeStack(Types.FE.get(), handler.extract(Long.MAX_VALUE, true));
        if (extractedSimulated.isEmpty()) {
            return null;
        }
        final long insertedSimulated = this.tryInsert(actor, configuredEnergyResource, extractedSimulated, rootStorage);
        if (insertedSimulated == 0) {
            return null;
        }
        final TypeStack extracted = new TypeStack(Types.FE.get(), handler.extract(insertedSimulated, false));
        if (extracted.isEmpty()) {
            return null;
        }
        this.doInsert(actor, extracted, rootStorage);
        return modifiedStack;
    }

    private long tryInsert(final Actor actor,
                           final EnergyResource configuredResource,
                           final TypeStack result,
                           final RootStorage rootStorage) {
        if (!result.type().equals(configuredResource.type())) {
            return 0;
        }
        return rootStorage.insert(
            createEnergyResource(),
            result.amount(),
            Action.SIMULATE,
            actor
        );
    }

    private void doInsert(final Actor actor, final TypeStack result, final RootStorage rootStorage) {
        rootStorage.insert(
            createEnergyResource(),
            result.amount(),
            Action.EXECUTE,
            actor
        );
    }
}
