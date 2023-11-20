package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.Point;
import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.Statue;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGomoku;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ChessDataToServerMessage {
    private final BlockPos pos;
    private final Point point;

    public ChessDataToServerMessage(BlockPos pos, Point point) {
        this.pos = pos;
        this.point = point;
    }

    public static void encode(ChessDataToServerMessage message, FriendlyByteBuf buf) {
        buf.writeBlockPos(message.pos);
        buf.writeVarInt(message.point.x);
        buf.writeVarInt(message.point.y);
        buf.writeVarInt(message.point.type);
    }

    public static ChessDataToServerMessage decode(FriendlyByteBuf buf) {
        BlockPos blockPos = buf.readBlockPos();
        Point pointIn = new Point(buf.readVarInt(), buf.readVarInt(), buf.readVarInt());
        return new ChessDataToServerMessage(blockPos, pointIn);
    }

    public static void handle(ChessDataToServerMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
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
                if (level.getBlockEntity(message.pos) instanceof TileEntityGomoku gomoku) {
                    if (!gomoku.isInProgress() || gomoku.isPlayerTurn() || gomoku.getChessCounter() <= 0) {
                        return;
                    }
                    Point aiPoint = message.point;
                    gomoku.setChessData(aiPoint.x, aiPoint.y, aiPoint.type);
                    gomoku.setInProgress(TouhouLittleMaid.SERVICE.getStatue(gomoku.getChessData(), aiPoint) == Statue.IN_PROGRESS);
                    level.playSound(null, message.pos, InitSounds.GOMOKU.get(), SoundSource.BLOCKS, 1.0f, 0.8F + level.random.nextFloat() * 0.4F);
                    if (gomoku.isInProgress()) {
                        gomoku.setPlayerTurn(true);
                    }
                    gomoku.refresh();
                }
            });
        }
        context.setPacketHandled(true);
    }
}
