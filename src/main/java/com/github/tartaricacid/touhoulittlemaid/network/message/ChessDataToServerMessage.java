package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.gomoku.Point;
import com.github.tartaricacid.touhoulittlemaid.api.gomoku.Statue;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySit;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGomoku;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ChessDataToServerMessage {
    private final BlockPos pos;
    private final Point point;

    public ChessDataToServerMessage(BlockPos pos, Point point) {
        this.pos = pos;
        this.point = point;
    }

    public static void encode(ChessDataToServerMessage message, PacketBuffer buf) {
        buf.writeBlockPos(message.pos);
        buf.writeVarInt(message.point.x);
        buf.writeVarInt(message.point.y);
        buf.writeVarInt(message.point.type);
    }

    public static ChessDataToServerMessage decode(PacketBuffer buf) {
        BlockPos blockPos = buf.readBlockPos();
        Point pointIn = new Point(buf.readVarInt(), buf.readVarInt(), buf.readVarInt());
        return new ChessDataToServerMessage(blockPos, pointIn);
    }

    public static void handle(ChessDataToServerMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayerEntity sender = context.getSender();
                if (sender == null) {
                    return;
                }
                World level = sender.level;
                if (!level.isLoaded(message.pos)) {
                    return;
                }
                if (level.getBlockEntity(message.pos) instanceof TileEntityGomoku gomoku) {
                    if (!gomoku.isInProgress() || gomoku.isPlayerTurn() || gomoku.getChessCounter() <= 0) {
                        return;
                    }
                    Point aiPoint = message.point;
                    gomoku.setChessData(aiPoint.x, aiPoint.y, aiPoint.type);
                    if (level instanceof ServerWorld && ((ServerWorld) level).getEntity(gomoku.getSitId()) instanceof EntitySit
                            && ((EntitySit) ((ServerWorld) level).getEntity(gomoku.getSitId())).getFirstPassenger() instanceof EntityMaid) {
                        EntityMaid maid = (EntityMaid) ((EntitySit) ((ServerWorld) level).getEntity(gomoku.getSitId())).getFirstPassenger();
                        EntitySit sit = (EntitySit) ((ServerWorld) level).getEntity(gomoku.getSitId());
                        ServerWorld serverLevel = (ServerWorld) level;
                        maid.swing(Hand.MAIN_HAND);
                    }
                    gomoku.setInProgress(TouhouLittleMaid.SERVICE.getStatue(gomoku.getChessData(), aiPoint) == Statue.IN_PROGRESS);
                    level.playSound(null, message.pos, InitSounds.GOMOKU.get(), SoundCategory.BLOCKS, 1.0f, 0.8F + level.random.nextFloat() * 0.4F);
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
