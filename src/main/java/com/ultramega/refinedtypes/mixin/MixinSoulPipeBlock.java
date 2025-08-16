package com.ultramega.refinedtypes.mixin;

import com.refinedmods.refinedstorage.common.autocrafting.autocrafter.AutocrafterBlock;
import com.refinedmods.refinedstorage.common.exporter.ExporterBlock;

import com.buuz135.industrialforegoingsouls.block.SoulPipeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(SoulPipeBlock.class)
public class MixinSoulPipeBlock {
    @Inject(method = "getConnectionType", at = @At("HEAD"), cancellable = true)
    protected void getConnectionType(final Level level,
                                     final BlockPos pos,
                                     final Direction direction,
                                     final BlockState state,
                                     final CallbackInfoReturnable<SoulPipeBlock.PipeState> cir) {
        final Block relativeBlock = level.getBlockState(pos.relative(direction)).getBlock();
        if (relativeBlock instanceof ExporterBlock || relativeBlock instanceof AutocrafterBlock) {
            cir.setReturnValue(SoulPipeBlock.PipeState.BLOCK);
        }
    }
}
