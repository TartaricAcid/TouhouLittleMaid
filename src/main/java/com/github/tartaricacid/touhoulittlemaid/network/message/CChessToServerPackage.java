package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.block.BlockCChess;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.getResourceLocation;

public record CChessToServerPackage(BlockPos pos, int move, boolean maidLost,
                                    boolean playerLost) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<CChessToServerPackage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("cchess_to_server"));
    public static final StreamCodec<ByteBuf, CChessToServerPackage> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, CChessToServerPackage::pos,
            ByteBufCodecs.VAR_INT, CChessToServerPackage::move,
            ByteBufCodecs.BOOL, CChessToServerPackage::maidLost,
            ByteBufCodecs.BOOL, CChessToServerPackage::playerLost,
            CChessToServerPackage::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(CChessToServerPackage message, IPayloadContext context) {
        if (context.flow().isServerbound()) {
            context.enqueueWork(() -> {
                if (!(context.player() instanceof ServerPlayer sender)) {
                    return;
                }
                Level level = sender.level;
                if (!level.isLoaded(message.pos)) {
                    return;
                }
                BlockCChess.maidMove(sender, level, message.pos, message.move, message.maidLost, message.playerLost);
            });
        }
    }
}
