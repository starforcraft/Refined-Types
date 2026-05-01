//package com.ultramega.refinedtypes.type.soul;
//
//import javax.annotation.Nullable;
//
//import com.buuz135.industrialforegoingsouls.block.tile.NetworkBlockEntity;
//import com.buuz135.industrialforegoingsouls.block_network.SoulNetwork;
//import com.buuz135.industrialforegoingsouls.capabilities.ISoulHandler;
//import com.hrznstudio.titanium.block_network.NetworkManager;
//import com.hrznstudio.titanium.block_network.element.NetworkElement;
//import net.minecraft.world.level.Level;
//
//public class SoulNetworkSoulHandlerAdapter implements ISoulHandler {
//    private final Level level;
//    private final NetworkBlockEntity<?> blockEntity;
//
//    public SoulNetworkSoulHandlerAdapter(final Level level, final NetworkBlockEntity<?> blockEntity) {
//        this.level = level;
//        this.blockEntity = blockEntity;
//    }
//
//    @Nullable
//    private SoulNetwork getNetwork() {
//        if (this.level.isClientSide) {
//            return null;
//        }
//
//        final NetworkElement element = NetworkManager.get(this.level).getElement(this.blockEntity.getBlockPos());
//        if (element == null || !(element.getNetwork() instanceof SoulNetwork network)) {
//            return null;
//        }
//
//        return network;
//    }
//
//    @Override
//    public int getSoulTanks() {
//        return 1;
//    }
//
//    @Override
//    public int getSoulInTank(final int tank) {
//        final SoulNetwork network = this.getNetwork();
//        return tank == 0 && network != null ? network.getSoulAmount() : 0;
//    }
//
//    @Override
//    public int getTankCapacity(final int tank) {
//        final SoulNetwork network = this.getNetwork();
//        return tank == 0 && network != null ? network.getMaxSouls() : 0;
//    }
//
//    @Override
//    public int fill(final int amount, final Action action) {
//        if (amount <= 0) {
//            return 0;
//        }
//
//        final SoulNetwork network = this.getNetwork();
//        if (network == null) {
//            return 0;
//        }
//
//        final int accepted = Math.clamp(network.getMaxSouls() - network.getSoulAmount(), 0, amount);
//        if (accepted <= 0) {
//            return 0;
//        }
//
//        return action.execute() ? network.addSouls(this.level, accepted) : accepted;
//    }
//
//    @Override
//    public int drain(final int maxDrain, final Action action) {
//        // draining isn't supported by SoulNetwork
//        return 0;
//    }
//}
