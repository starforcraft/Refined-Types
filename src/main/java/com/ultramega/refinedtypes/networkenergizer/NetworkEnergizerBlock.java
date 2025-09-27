package com.ultramega.refinedtypes.networkenergizer;

import com.ultramega.refinedtypes.registry.BlockEntities;

import com.refinedmods.refinedstorage.common.content.BlockConstants;
import com.refinedmods.refinedstorage.common.support.AbstractBaseBlock;
import com.refinedmods.refinedstorage.common.support.AbstractBlockEntityTicker;
import com.refinedmods.refinedstorage.common.support.NetworkNodeBlockItem;
import com.refinedmods.refinedstorage.common.support.network.NetworkNodeBlockEntityTicker;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import static com.ultramega.refinedtypes.RefinedTypesUtil.createRefinedTypesTranslation;

public class NetworkEnergizerBlock extends AbstractBaseBlock implements EntityBlock {
    private static final Component HELP = createRefinedTypesTranslation("item", "network_energizer.help");
    private static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    private static final AbstractBlockEntityTicker<NetworkEnergizerBlockEntity> TICKER = new NetworkNodeBlockEntityTicker<>(
        BlockEntities::getNetworkEnergizer,
        ACTIVE
    );

    public NetworkEnergizerBlock() {
        super(BlockConstants.PROPERTIES);
    }

    @Override
    protected BlockState getDefaultState() {
        return super.getDefaultState().setValue(ACTIVE, false);
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ACTIVE);
    }

    @Override
    public BlockEntity newBlockEntity(final BlockPos pos, final BlockState state) {
        return new NetworkEnergizerBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(final Level level,
                                                                  final BlockState blockState,
                                                                  final BlockEntityType<T> type) {
        return TICKER.get(level, type);
    }

    public BlockItem createBlockItem() {
        return new NetworkNodeBlockItem(this, HELP);
    }
}
