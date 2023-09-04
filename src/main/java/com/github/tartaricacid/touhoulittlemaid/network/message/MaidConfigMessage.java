package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.MaidSchedule;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
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
                    maid.setHomeModeEnable(message.home);
                    maid.setPickup(message.pick);
                    maid.setRideable(message.ride);
                    if (maid.getVehicle() != null) {
                        maid.stopRiding();
                    }
                    if (maid.getSchedule() != message.schedule) {
                        maid.setSchedule(message.schedule);
                    }
                }
            });
        }
        context.setPacketHandled(true);
    }
}
