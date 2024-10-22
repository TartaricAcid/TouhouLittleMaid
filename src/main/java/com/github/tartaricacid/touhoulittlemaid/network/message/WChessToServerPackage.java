package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.block.BlockWChess;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.getResourceLocation;

public record WChessToServerPackage(BlockPos pos, int move, boolean maidLost,
                                    boolean playerLost) implements CustomPacketPayload {
    public static final Type<WChessToServerPackage> TYPE = new Type<>(getResourceLocation("wchess_to_server"));
    public static final StreamCodec<ByteBuf, WChessToServerPackage> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, WChessToServerPackage::pos,
            ByteBufCodecs.VAR_INT, WChessToServerPackage::move,
            ByteBufCodecs.BOOL, WChessToServerPackage::maidLost,
            ByteBufCodecs.BOOL, WChessToServerPackage::playerLost,
            WChessToServerPackage::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(WChessToServerPackage message, IPayloadContext context) {
        if (context.flow().isServerbound()) {
            context.enqueueWork(() -> {
                if (!(context.player() instanceof ServerPlayer sender)) {
                    return;
                }
                Level level = sender.level;
                if (!level.isLoaded(message.pos)) {
                    return;
                }
                BlockWChess.maidMove(sender, level, message.pos, message.move, message.maidLost, message.playerLost);
            });
        }
    }
}
