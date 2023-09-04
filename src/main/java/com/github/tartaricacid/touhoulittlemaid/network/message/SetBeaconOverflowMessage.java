package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SetBeaconOverflowMessage {
    private final BlockPos pos;
    private final boolean overflowDelete;

    public SetBeaconOverflowMessage(BlockPos pos, boolean overflowDelete) {
        this.pos = pos;
        this.overflowDelete = overflowDelete;
    }

    public static void encode(SetBeaconOverflowMessage message, FriendlyByteBuf buf) {
        buf.writeBlockPos(message.pos);
        buf.writeBoolean(message.overflowDelete);
    }

    public static SetBeaconOverflowMessage decode(FriendlyByteBuf buf) {
        return new SetBeaconOverflowMessage(buf.readBlockPos(), buf.readBoolean());
    }

    public static void handle(SetBeaconOverflowMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender == null) {
                    return;
                }
                Level world = sender.level();
                if (world.isLoaded(message.pos)) {
                    BlockEntity te = world.getBlockEntity(message.pos);
                    if (te instanceof TileEntityMaidBeacon) {
                        ((TileEntityMaidBeacon) te).setOverflowDelete(message.overflowDelete);
                    }
                }
            });
        }
        context.setPacketHandled(true);
    }
}
