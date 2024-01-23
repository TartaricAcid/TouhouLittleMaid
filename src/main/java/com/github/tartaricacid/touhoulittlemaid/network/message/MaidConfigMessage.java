package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.MaidSchedule;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySit;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MaidConfigMessage {
    private final int id;
    private final boolean home;
    private final boolean pick;
    private final boolean ride;
    private final MaidSchedule schedule;

    public MaidConfigMessage(int id, boolean home, boolean pick, boolean ride, MaidSchedule schedule) {
        this.id = id;
        this.home = home;
        this.pick = pick;
        this.ride = ride;
        this.schedule = schedule;
    }

    public static void encode(MaidConfigMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.id);
        buf.writeBoolean(message.home);
        buf.writeBoolean(message.pick);
        buf.writeBoolean(message.ride);
        buf.writeEnum(message.schedule);
    }

    public static MaidConfigMessage decode(FriendlyByteBuf buf) {
        return new MaidConfigMessage(buf.readInt(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readEnum(MaidSchedule.class));
    }

    public static void handle(MaidConfigMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender == null) {
                    return;
                }
                Entity entity = sender.level.getEntity(message.id);
                if (entity instanceof EntityMaid && ((EntityMaid) entity).isOwnedBy(sender)) {
                    EntityMaid maid = (EntityMaid) entity;
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
            });
        }
        context.setPacketHandled(true);
    }

    private static void handleHome(MaidConfigMessage message, ServerPlayer sender, EntityMaid maid) {
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
