package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SetBeaconPotionMessage {
    private final BlockPos pos;
    private final int potionIndex;

    public SetBeaconPotionMessage(BlockPos pos, int potionIndex) {
        this.pos = pos;
        this.potionIndex = potionIndex;
    }

    public static void encode(SetBeaconPotionMessage message, PacketBuffer buf) {
        buf.writeBlockPos(message.pos);
        buf.writeInt(message.potionIndex);
    }

    public static SetBeaconPotionMessage decode(PacketBuffer buf) {
        return new SetBeaconPotionMessage(buf.readBlockPos(), buf.readInt());
    }

    public static void handle(SetBeaconPotionMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
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
                        ((TileEntityMaidBeacon) te).setPotionIndex(message.potionIndex);
                    }
                }
            });
        }
        context.setPacketHandled(true);
    }
}
