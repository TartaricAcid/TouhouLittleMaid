package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.advancements.maid.TriggerType;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.MaidSchedule;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBroom;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySit;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.SchedulePos;
import com.github.tartaricacid.touhoulittlemaid.init.InitTrigger;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLocationUtil.getResourceLocation;

public record MaidConfigPackage(int id, boolean home, boolean pick, boolean ride,
                                MaidSchedule schedule) implements CustomPacketPayload {
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

    public static void handle(MaidConfigPackage message, IPayloadContext context) {
        if (context.flow().isServerbound()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = (ServerPlayer) context.player();
                Entity entity = sender.level.getEntity(message.id);
                if (entity instanceof EntityMaid maid && maid.isOwnedBy(sender)) {
                    if (maid.isHomeModeEnable() != message.home) {
                        handleHome(message, sender, maid);
                    }
                    if (maid.isPickup() != message.pick) {
                        maid.setPickup(message.pick);
                    }
                    if (maid.isRideable() != message.ride) {
                        maid.setRideable(message.ride);
                        Entity vehicle = maid.getVehicle();
                        if (!message.ride && vehicle != null && !isStopRideBlocklist(vehicle)) {
                            maid.stopRiding();
                        }
                    }
                    if (maid.getSchedule() != message.schedule) {
                        maid.setSchedule(message.schedule);
                        maid.getSchedulePos().restrictTo(maid);
                        if (maid.isHomeModeEnable()) {
                            BehaviorUtils.setWalkAndLookTargetMemories(maid, maid.getRestrictCenter(), 0.7f, 3);
                        }
                        if (maid.getOwner() instanceof ServerPlayer serverPlayer) {
                            InitTrigger.MAID_EVENT.get().trigger(serverPlayer, TriggerType.SWITCH_SCHEDULE);
                        }
                    }
                }
            });
        }
    }

    private static boolean isStopRideBlocklist(Entity vehicle) {
        // 娱乐方块骑乘不受影响
        boolean isSit = vehicle instanceof EntitySit;
        // 飞行中的扫帚不能脱离，有风险
        boolean isBroom = vehicle instanceof EntityBroom broom && !broom.onGround();
        return isSit || isBroom;
    }

    private static void handleHome(MaidConfigPackage message, ServerPlayer sender, EntityMaid maid) {
        if (message.home) {
            SchedulePos schedulePos = maid.getSchedulePos();
            if (schedulePos.isConfigured()) {
                ResourceLocation dimension = schedulePos.getDimension();
                if (!dimension.equals(maid.level.dimension().location())) {
                    CheckSchedulePosPacket tips = new CheckSchedulePosPacket("message.touhou_little_maid.check_schedule_pos.dimension");
                    PacketDistributor.sendToPlayer(sender, tips);
                    return;
                }
                BlockPos nearestPos = schedulePos.getNearestPos(maid);
                if (nearestPos != null && nearestPos.distSqr(maid.blockPosition()) > 32 * 32) {
                    CheckSchedulePosPacket tips = new CheckSchedulePosPacket("message.touhou_little_maid.check_schedule_pos.too_far");
                    PacketDistributor.sendToPlayer(sender, tips);
                    return;
                }
            }
            schedulePos.setHomeModeEnable(maid, maid.blockPosition());
        } else {
            maid.restrictTo(BlockPos.ZERO, MaidConfig.MAID_NON_HOME_RANGE.get());
        }
        maid.setHomeModeEnable(message.home);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}