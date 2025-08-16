package com.ultramega.refinedtypes.storage.energy;

import com.ultramega.refinedtypes.RefinedTypesUtil;
import com.ultramega.refinedtypes.registry.Items;
import com.ultramega.refinedtypes.type.energy.EnergyResourceRendering;

import com.refinedmods.refinedstorage.common.api.storage.AbstractStorageContainerItem;
import com.refinedmods.refinedstorage.common.api.storage.SerializableStorage;
import com.refinedmods.refinedstorage.common.api.storage.StorageContainerItemHelper;
import com.refinedmods.refinedstorage.common.api.storage.StorageRepository;
import com.refinedmods.refinedstorage.common.api.support.HelpTooltipComponent;
import com.refinedmods.refinedstorage.common.storage.StorageVariant;
import com.refinedmods.refinedstorage.common.storage.UpgradeableStorageContainer;

import java.util.Optional;
import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.format;
import static com.ultramega.refinedtypes.RefinedTypesUtil.createRefinedTypesTranslation;

public class EnergyStorageDiskItem extends AbstractStorageContainerItem implements UpgradeableStorageContainer {
    private static final Component INFINITE_HELP = RefinedTypesUtil.createRefinedTypesTranslation(
        "item",
        "infinite_energy_storage_disk.help"
    );

    private final EnergyStorageVariant variant;
    private final Component helpText;

    public EnergyStorageDiskItem(final StorageContainerItemHelper helper,
                                 final EnergyStorageVariant variant) {
        super(new Item.Properties().stacksTo(1).fireResistant(), helper);
        this.variant = variant;
        this.helpText = getHelpText(variant);
    }

    private static Component getHelpText(final EnergyStorageVariant variant) {
        if (variant.getCapacity() == null) {
            return INFINITE_HELP;
        }
        return createRefinedTypesTranslation(
            "item",
            "energy_storage_disk.help",
            format(variant.getCapacity())
        );
    }


    @Override
    @Nullable
    protected Long getCapacity() {
        return this.variant.getCapacity();
    }

    @Override
    protected String formatAmount(final long amount) {
        return EnergyResourceRendering.format(amount);
    }

    @Override
    protected SerializableStorage createStorage(final StorageRepository storageRepository) {
        return EnergyStorageBlockBlockItem.createStorage(this.variant, storageRepository::markAsChanged);
    }

    @Override
    protected ItemStack createPrimaryDisassemblyByproduct(final int count) {
        return new ItemStack(com.refinedmods.refinedstorage.common.content.Items.INSTANCE.getStorageHousing(), count);
    }

    @Override
    protected ItemStack createSecondaryDisassemblyByproduct(final int count) {
        return new ItemStack(Items.getEnergyStoragePart(this.variant), count);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(final ItemStack stack) {
        return Optional.of(new HelpTooltipComponent(this.helpText));
    }

    @Override
    public StorageVariant getVariant() {
        return this.variant;
    }

    @Override
    public void transferTo(final ItemStack from, final ItemStack to) {
        this.helper.markAsToTransfer(from, to);
    }
}
