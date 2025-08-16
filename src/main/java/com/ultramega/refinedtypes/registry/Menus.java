package com.ultramega.refinedtypes.registry;

import java.util.function.Supplier;
import javax.annotation.Nullable;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import static java.util.Objects.requireNonNull;

public final class Menus {
    @Nullable
    private static Supplier<MenuType<AbstractContainerMenu>> energyStorage;
    private static Supplier<MenuType<AbstractContainerMenu>> sourceStorage;
    private static Supplier<MenuType<AbstractContainerMenu>> soulStorage;

    private Menus() {
    }

    public static MenuType<AbstractContainerMenu> getEnergyStorage() {
        return requireNonNull(energyStorage).get();
    }

    public static void setEnergyStorage(final Supplier<MenuType<AbstractContainerMenu>> supplier) {
        energyStorage = supplier;
    }

    public static MenuType<AbstractContainerMenu> getSourceStorage() {
        return requireNonNull(sourceStorage).get();
    }

    public static void setSourceStorage(final Supplier<MenuType<AbstractContainerMenu>> supplier) {
        sourceStorage = supplier;
    }

    public static MenuType<AbstractContainerMenu> getSoulStorage() {
        return requireNonNull(soulStorage).get();
    }

    public static void setSoulStorage(final Supplier<MenuType<AbstractContainerMenu>> supplier) {
        soulStorage = supplier;
    }
}
