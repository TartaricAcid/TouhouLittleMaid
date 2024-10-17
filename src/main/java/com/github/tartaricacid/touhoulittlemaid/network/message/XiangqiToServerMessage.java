package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.block.BlockCChess;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class XiangqiToServerMessage {
    private final BlockPos pos;
    private final int move;
    private final boolean maidLost;
    private final boolean playerLost;

    public XiangqiToServerMessage(BlockPos pos, int move, boolean maidLost, boolean playerLost) {
        this.pos = pos;
        this.move = move;
        this.maidLost = maidLost;
        this.playerLost = playerLost;
    }

    public static void encode(XiangqiToServerMessage message, FriendlyByteBuf buf) {
        buf.writeBlockPos(message.pos);
        buf.writeVarInt(message.move);
        buf.writeBoolean(message.maidLost);
        buf.writeBoolean(message.playerLost);
    }

    public static XiangqiToServerMessage decode(FriendlyByteBuf buf) {
        return new XiangqiToServerMessage(buf.readBlockPos(), buf.readVarInt(), buf.readBoolean(), buf.readBoolean());
    }

    public static void handle(XiangqiToServerMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender == null) {
                    return;
                }
                Level level = sender.level;
                if (!level.isLoaded(message.pos)) {
                    return;
                }
                BlockCChess.maidMove(sender, level, message.pos, message.move, message.maidLost, message.playerLost);
            });
        }
        context.setPacketHandled(true);
    }
}
