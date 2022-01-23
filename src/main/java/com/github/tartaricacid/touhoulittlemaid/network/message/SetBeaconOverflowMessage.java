package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SetBeaconOverflowMessage {
    private final BlockPos pos;
    private final boolean overflowDelete;

    public SetBeaconOverflowMessage(BlockPos pos, boolean overflowDelete) {
        this.pos = pos;
        this.overflowDelete = overflowDelete;
    }

    public static void encode(SetBeaconOverflowMessage message, PacketBuffer buf) {
        buf.writeBlockPos(message.pos);
        buf.writeBoolean(message.overflowDelete);
    }

    public static SetBeaconOverflowMessage decode(PacketBuffer buf) {
        return new SetBeaconOverflowMessage(buf.readBlockPos(), buf.readBoolean());
    }

    public static void handle(SetBeaconOverflowMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayerEntity sender = context.getSender();
                if (sender == null) {
                    return;
                }
                World world = sender.level;
                if (world.isLoaded(message.pos)) {
                    TileEntity te = world.getBlockEntity(message.pos);
                    if (te instanceof TileEntityMaidBeacon) {
                        ((TileEntityMaidBeacon) te).setOverflowDelete(message.overflowDelete);
                    }
                }
            });
        }
        context.setPacketHandled(true);
    }
}
