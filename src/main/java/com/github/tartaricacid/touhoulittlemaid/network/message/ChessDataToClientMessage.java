package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.Point;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.MaidGomokuAI;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ChessDataToClientMessage {
    private final BlockPos pos;
    private final int[][] chessData;
    private final Point point;
    private final int count;

    public ChessDataToClientMessage(BlockPos pos, int[][] chessData, Point point, int count) {
        this.pos = pos;
        this.chessData = chessData;
        this.point = point;
        this.count = count;
    }

    public static void encode(ChessDataToClientMessage message, FriendlyByteBuf buf) {
        buf.writeBlockPos(message.pos);
        buf.writeVarInt(message.chessData.length);
        for (int[] row : message.chessData) {
            buf.writeVarIntArray(row);
        }
        buf.writeVarInt(message.point.x);
        buf.writeVarInt(message.point.y);
        buf.writeVarInt(message.point.type);
        buf.writeVarInt(message.count);
    }

    public static ChessDataToClientMessage decode(FriendlyByteBuf buf) {
        BlockPos blockPos = buf.readBlockPos();
        int length = buf.readVarInt();
        int[][] chessData = new int[length][length];
        for (int i = 0; i < length; i++) {
            chessData[i] = buf.readVarIntArray();
        }
        Point pointIn = new Point(buf.readVarInt(), buf.readVarInt(), buf.readVarInt());
        int count = buf.readVarInt();
        return new ChessDataToClientMessage(blockPos, chessData, pointIn, count);
    }

    public static void handle(ChessDataToClientMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> CompletableFuture.runAsync(() -> onHandle(message), Util.backgroundExecutor()));
        }
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void onHandle(ChessDataToClientMessage message) {
        Point aiPoint = MaidGomokuAI.getService(message.count).getPoint(message.chessData, message.point);
        int time = (int) (Math.random() * 1250) + 250;
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Minecraft.getInstance().submitAsync(() -> NetworkHandler.CHANNEL.sendToServer(new ChessDataToServerMessage(message.pos, aiPoint)));
    }
}
