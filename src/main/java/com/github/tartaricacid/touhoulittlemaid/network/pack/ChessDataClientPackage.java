package com.github.tartaricacid.touhoulittlemaid.network.pack;

import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.Point;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.MaidGomokuAI;
import io.netty.buffer.ByteBuf;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.getResourceLocation;

public record ChessDataClientPackage(BlockPos pos, List<byte[]> chessData, Point point, int count) implements CustomPacketPayload {
    public ChessDataClientPackage(BlockPos pos, byte[][] chessData, Point point, int count) {
        this(pos, Arrays.stream(chessData).toList(), point, count);
    }
    public static final CustomPacketPayload.Type<ChessDataClientPackage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("chess_data_to_client"));
    public static final StreamCodec<ByteBuf , List<byte[]>> BYTE_BUF_LIST_STREAM_CODEC = ByteBufCodecs.collection(
                ArrayList::new,
                ByteBufCodecs.BYTE_ARRAY,
                20
            );

    public static final StreamCodec<ByteBuf, ChessDataClientPackage> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            ChessDataClientPackage::pos,
            BYTE_BUF_LIST_STREAM_CODEC,
            ChessDataClientPackage::chessData,
            Point.POINT_STREAM_CODEC,
            ChessDataClientPackage::point,
            ByteBufCodecs.VAR_INT,
            ChessDataClientPackage::count,
            ChessDataClientPackage::new
    );

    public static void handle(ChessDataClientPackage message, IPayloadContext context) {
        if (context.flow().isClientbound()) {
            context.enqueueWork(() -> CompletableFuture.runAsync(() -> onHandle(message), Util.backgroundExecutor()));
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void onHandle(ChessDataClientPackage message) {
        Point aiPoint = MaidGomokuAI.getService(message.count).getPoint(message.chessData.toArray(new byte[15][]), message.point);
        int time = (int) (Math.random() * 1250) + 250;
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //TODO 这可能是一个双向网络包
        Minecraft.getInstance().submitAsync(() -> PacketDistributor.sendToServer(new ChessDataServerPackage(message.pos, aiPoint)));
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
