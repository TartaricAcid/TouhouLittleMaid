package com.github.tartaricacid.touhoulittlemaid.network.pack;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.MaidSchedule;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySit;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.neoforged.neoforge.network.PacketDistributor;
import com.github.tartaricacid.touhoulittlemaid.network.message.CheckSchedulePosMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.getResourceLocation;

public record MaidConfigPackage(int id, boolean home, boolean pick, boolean ride, MaidSchedule schedule) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MaidConfigPackage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("maid_config"));
    public static final StreamCodec<ByteBuf, MaidConfigPackage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            MaidConfigPackage::id,
            ByteBufCodecs.BOOL,
            MaidConfigPackage::home,
            ByteBufCodecs.BOOL,
            MaidConfigPackage::pick,
            ByteBufCodecs.BOOL,
            MaidConfigPackage::ride,
            MaidSchedule.STREAM_CODEC,
            MaidConfigPackage::schedule,
            MaidConfigPackage::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(MaidConfigPackage message, IPayloadContext context) {
        ServerPlayer sender = (ServerPlayer) context.player();
        Entity entity = sender.level.getEntity(message.id);
        if (entity instanceof EntityMaid maid && ((EntityMaid) entity).isOwnedBy(sender)) {
            if (maid.isHomeModeEnable() != message.home) {
                handleHome(message, sender, maid);
            }
            if (maid.isPickup() != message.pick) {
                maid.setPickup(message.pick);
            }
            if (maid.isRideable() != message.ride) {
                maid.setRideable(message.ride);
            }
            if (maid.getVehicle() != null && !(maid.getVehicle() instanceof EntitySit)) {
                maid.stopRiding();
            }
            if (maid.getSchedule() != message.schedule) {
                maid.setSchedule(message.schedule);
                maid.getSchedulePos().restrictTo(maid);
                if (maid.isHomeModeEnable()) {
                    BehaviorUtils.setWalkAndLookTargetMemories(maid, maid.getRestrictCenter(), 0.7f, 3);
                }
            }
        }
    }

    private static void handleHome(MaidConfigPackage message, ServerPlayer sender, EntityMaid maid) {
        if (message.home) {
            ResourceLocation dimension = maid.getSchedulePos().getDimension();
            if (!dimension.equals(maid.level.dimension().location())) {
                CheckSchedulePosMessage tips = new CheckSchedulePosMessage(Component.translatable("message.touhou_little_maid.check_schedule_pos.dimension"));
                NetworkHandler.sendToClientPlayer(tips, sender);
                return;
            }
            BlockPos nearestPos = maid.getSchedulePos().getNearestPos(maid);
            if (nearestPos != null && nearestPos.distSqr(maid.blockPosition()) > 32 * 32) {
                CheckSchedulePosMessage tips = new CheckSchedulePosMessage(Component.translatable("message.touhou_little_maid.check_schedule_pos.too_far"));
                NetworkHandler.sendToClientPlayer(tips, sender);
                return;
            }
            maid.getSchedulePos().setHomeModeEnable(maid, maid.blockPosition());
        } else {
            maid.restrictTo(BlockPos.ZERO, MaidConfig.MAID_NON_HOME_RANGE.get());
        }
        maid.setHomeModeEnable(message.home);
    }


}
