package com.ultramega.refinedtypes.storage.essentia;

import com.ultramega.refinedtypes.RefinedTypesUtil;
import com.ultramega.refinedtypes.registry.Items;
import com.ultramega.refinedtypes.type.essentia.EssentiaResource;
import com.ultramega.refinedtypes.type.essentia.EssentiaResourceType;

import com.refinedmods.refinedstorage.common.api.RefinedStorageApi;
import com.refinedmods.refinedstorage.common.api.RefinedStorageClientApi;
import com.refinedmods.refinedstorage.common.api.storage.AbstractStorageContainerBlockItem;
import com.refinedmods.refinedstorage.common.api.storage.SerializableStorage;
import com.refinedmods.refinedstorage.common.api.storage.StorageRepository;
import com.refinedmods.refinedstorage.common.api.support.HelpTooltipComponent;
import com.refinedmods.refinedstorage.common.content.Blocks;
import com.refinedmods.refinedstorage.common.storage.StorageVariant;
import com.refinedmods.refinedstorage.common.storage.UpgradeableStorageContainer;

import java.util.Optional;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.format;
import static com.ultramega.refinedtypes.RefinedTypesUtil.createRefinedTypesTranslation;

public class EssentiaStorageBlockBlockItem extends AbstractStorageContainerBlockItem implements UpgradeableStorageContainer {
    private static final Component INFINITE_HELP = RefinedTypesUtil.createRefinedTypesTranslation("item", "infinite_essentia_storage_block.help");
    private static final Component CREATIVE_HELP = RefinedTypesUtil.createRefinedTypesTranslation("item", "creative_essentia_storage_block.help");

    private final EssentiaStorageVariant variant;
    private final Component helpText;

    public EssentiaStorageBlockBlockItem(final Identifier id, final Block block, final EssentiaStorageVariant variant) {
        super(
            block,
            new Item.Properties().stacksTo(1).fireResistant().setId(ResourceKey.create(Registries.ITEM, id)),
            RefinedStorageApi.INSTANCE.getStorageContainerItemHelper()
        );
        this.variant = variant;
        this.helpText = getHelpText(variant);
    }

    private static Component getHelpText(final EssentiaStorageVariant variant) {
        return switch (variant) {
            case INFINITE -> INFINITE_HELP;
            case CREATIVE -> CREATIVE_HELP;
            default -> createRefinedTypesTranslation(
                "item",
                "essentia_storage_block.help",
                variant.getCapacity() != null ? format(variant.getCapacity()) : 0
            );
        };
    }

    @Nullable
    @Override
    protected Long getCapacity() {
        return this.variant.getCapacity();
    }

    @Override
    protected String formatAmount(final long amount) {
        return RefinedStorageClientApi.INSTANCE.getResourceRendering(EssentiaResource.class).formatAmount(amount);
    }

    @Override
    protected SerializableStorage createStorage(final StorageRepository storageRepository) {
        return createStorage(this.variant, storageRepository::markAsChanged);
    }

    static SerializableStorage createStorage(final EssentiaStorageVariant variant, final Runnable listener) {
        return EssentiaResourceType.STORAGE_TYPE.create(variant.getCapacity(), listener);
    }

    @Override
    protected ItemStack createPrimaryDisassemblyByproduct(final int count) {
        return new ItemStack(Blocks.INSTANCE.getMachineCasing(), count);
    }

    @Override
    protected ItemStack createSecondaryDisassemblyByproduct(final int count) {
        return new ItemStack(Items.getEssentiaStoragePart(this.variant), count);
    }

    @Override
    protected boolean placeBlock(final BlockPlaceContext ctx, final BlockState state) {
        if (ctx.getPlayer() instanceof ServerPlayer serverPlayer
            && !(RefinedStorageApi.INSTANCE.canPlaceNetworkNode(serverPlayer, ctx.getLevel(), ctx.getClickedPos(), state))) {
            return false;
        }
        return super.placeBlock(ctx, state);
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
