package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.Point;
import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.Statue;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.MaidGomokuAI;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySit;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGomoku;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.getResourceLocation;

public record GomokuServerPackage(BlockPos pos, Point point) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<GomokuServerPackage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("gomoku_to_server"));
    public static final StreamCodec<ByteBuf, GomokuServerPackage> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            GomokuServerPackage::pos,
            Point.POINT_STREAM_CODEC,
            GomokuServerPackage::point,
            GomokuServerPackage::new
    );

    public static void handle(GomokuServerPackage message, IPayloadContext context) {
        if (context.flow().isServerbound()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = (ServerPlayer) context.player();
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
                    if (level instanceof ServerLevel serverLevel && serverLevel.getEntity(gomoku.getSitId()) instanceof EntitySit sit && sit.getFirstPassenger() instanceof EntityMaid maid) {
                        maid.swing(InteractionHand.MAIN_HAND);
                    }
                    gomoku.setInProgress(MaidGomokuAI.getStatue(gomoku.getChessData(), aiPoint) == Statue.IN_PROGRESS);
                    level.playSound(null, message.pos, InitSounds.GOMOKU.get(), SoundSource.BLOCKS, 1.0f, 0.8F + level.random.nextFloat() * 0.4F);
                    if (gomoku.isInProgress()) {
                        gomoku.setPlayerTurn(true);
                    }
                    gomoku.refresh();
                }
            });
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
