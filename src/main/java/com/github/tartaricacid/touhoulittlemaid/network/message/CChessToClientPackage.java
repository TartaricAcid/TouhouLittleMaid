package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.api.game.xqwlight.Position;
import com.github.tartaricacid.touhoulittlemaid.api.game.xqwlight.Search;
import com.github.tartaricacid.touhoulittlemaid.util.CChessUtil;
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

import java.util.concurrent.CompletableFuture;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.getResourceLocation;

public record CChessToClientPackage(BlockPos pos, String fenData) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<CChessToClientPackage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("cchess_to_client"));
    public static final StreamCodec<ByteBuf, CChessToClientPackage> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            CChessToClientPackage::pos,
            ByteBufCodecs.STRING_UTF8,
            CChessToClientPackage::fenData,
            CChessToClientPackage::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(CChessToClientPackage message, IPayloadContext context) {
        if (context.flow().isClientbound()) {
            context.enqueueWork(() -> CompletableFuture.runAsync(() -> onHandle(message), Util.backgroundExecutor()));
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void onHandle(CChessToClientPackage message) {
        int levelTime = 1000;
        long timeStart = System.currentTimeMillis();
        int move = 0;

        Position position = new Position();
        position.fromFen(message.fenData);

        // 先判断玩家是否赢了
        // 是的，我放客户端，减轻服务端压力，理论上你可直接传布尔值判断女仆输掉来作弊
        boolean maidLost = CChessUtil.isMaid(position) && position.isMate();
        boolean playerLost = false;
        if (!maidLost) {
            // TODO: 暂时不做女仆的棋技系统
            move = new Search(position, 12).searchMain(levelTime);
            // 玩家是否输了
            playerLost = position.makeMove(move) && CChessUtil.isPlayer(position) && position.isMate();
        }

        // 如果时间还有剩余，那么 sleep 一会儿
        long timeRemain = Math.max(0, levelTime - (int) (System.currentTimeMillis() - timeStart));
        try {
            if (timeRemain > 0) {
                Thread.sleep(timeRemain);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        final int moveFinal = move;
        final boolean playerLostFinal = playerLost;
        Minecraft.getInstance().submitAsync(() -> PacketDistributor.sendToServer(new CChessToServerPackage(message.pos, moveFinal, maidLost, playerLostFinal)));
    }
}
