package com.ultramega.refinedtypes.networkenergizer;

import com.ultramega.refinedtypes.registry.Menus;

import com.refinedmods.refinedstorage.common.support.AbstractBaseContainerMenu;
import com.refinedmods.refinedstorage.common.support.RedstoneMode;
import com.refinedmods.refinedstorage.common.support.containermenu.ClientProperty;
import com.refinedmods.refinedstorage.common.support.containermenu.PropertyTypes;
import com.refinedmods.refinedstorage.common.support.containermenu.ServerProperty;

import java.util.function.Predicate;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class NetworkEnergizerContainerMenu extends AbstractBaseContainerMenu {
    private final NetworkEnergizerEnergyInfo energyInfo;
    private final Predicate<Player> stillValid;

    public NetworkEnergizerContainerMenu(final int syncId,
                                         final Inventory playerInventory,
                                         final NetworkEnergizerData networkEnergizerData) {
        super(Menus.getNetworkEnergizer(), syncId);
        this.energyInfo = NetworkEnergizerEnergyInfo.forClient(
            playerInventory.player,
            networkEnergizerData.stored(),
            networkEnergizerData.capacity()
        );
        this.addPlayerInventory(playerInventory, 8, 107);
        this.registerProperty(new ClientProperty<>(PropertyTypes.REDSTONE_MODE, RedstoneMode.IGNORE));
        this.stillValid = p -> true;
    }

    NetworkEnergizerContainerMenu(final int syncId,
                                  final Inventory playerInventory,
                                  final NetworkEnergizerBlockEntity networkEnergizer,
                                  final Player player) {
        super(Menus.getNetworkEnergizer(), syncId);
        this.energyInfo = NetworkEnergizerEnergyInfo.forServer(
            player,
            networkEnergizer::getStored,
            networkEnergizer::getCapacity
        );
        this.addPlayerInventory(playerInventory, 8, 107);
        this.registerProperty(new ServerProperty<>(
            PropertyTypes.REDSTONE_MODE,
            networkEnergizer::getRedstoneMode,
            networkEnergizer::setRedstoneMode
        ));
        this.stillValid = p -> Container.stillValidBlockEntity(networkEnergizer, p);
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        this.energyInfo.detectChanges();
    }

    @Override
    public boolean stillValid(final Player player) {
        return this.stillValid.test(player);
    }

    public NetworkEnergizerEnergyInfo getEnergyInfo() {
        return this.energyInfo;
    }
}
