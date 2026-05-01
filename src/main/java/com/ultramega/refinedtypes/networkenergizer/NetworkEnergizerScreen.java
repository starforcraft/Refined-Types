package com.ultramega.refinedtypes.networkenergizer;

import com.refinedmods.refinedstorage.common.support.AbstractBaseScreen;
import com.refinedmods.refinedstorage.common.support.containermenu.PropertyTypes;
import com.refinedmods.refinedstorage.common.support.widget.RedstoneModeSideButtonWidget;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jspecify.annotations.Nullable;

import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.createIdentifier;
import static com.ultramega.refinedtypes.RefinedTypesUtil.createRefinedTypesTranslation;

public class NetworkEnergizerScreen extends AbstractBaseScreen<NetworkEnergizerContainerMenu> {
    private static final Identifier TEXTURE = createIdentifier("textures/gui/controller.png");

    @Nullable
    private EnergyProgressWidget progressWidget;

    public NetworkEnergizerScreen(final NetworkEnergizerContainerMenu menu,
                                  final Inventory playerInventory,
                                  final Component title) {
        super(menu, playerInventory, title, 176, 189);
        this.inventoryLabelY = 94;
    }

    @Override
    protected void init() {
        super.init();

        this.addSideButton(new RedstoneModeSideButtonWidget(
            this.getMenu().getProperty(PropertyTypes.REDSTONE_MODE),
            createRefinedTypesTranslation("gui", "network_energizer.redstone_mode_help")
        ));
        if (this.progressWidget == null) {
            this.progressWidget = new EnergyProgressWidget(
                this.leftPos + 80,
                this.topPos + 20,
                16,
                70,
                this.getMenu().getEnergyInfo()::getPercentageFull,
                this.getMenu().getEnergyInfo()::createTooltip
            );
        } else {
            this.progressWidget.setX(this.leftPos + 80);
            this.progressWidget.setY(this.topPos + 20);
        }
        this.addRenderableWidget(this.progressWidget);
    }

    @Override
    protected Identifier getTexture() {
        return TEXTURE;
    }
}
