package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SetMaidSoundIdMessage {
    private final int entityId;
    private final String soundId;

    public SetMaidSoundIdMessage(int entityId, String soundId) {
        this.entityId = entityId;
        this.soundId = soundId;
    }

    public static void encode(SetMaidSoundIdMessage message, PacketBuffer buf) {
        buf.writeInt(message.entityId);
        buf.writeUtf(message.soundId);
    }

    public static SetMaidSoundIdMessage decode(PacketBuffer buf) {
        return new SetMaidSoundIdMessage(buf.readInt(), buf.readUtf());
    }

    public static void handle(SetMaidSoundIdMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayerEntity sender = context.getSender();
                if (sender == null) {
                    return;
                }
                Entity entity = sender.level.getEntity(message.entityId);
                if (entity instanceof EntityMaid && ((EntityMaid) entity).isOwnedBy(sender)) {
                    EntityMaid maid = (EntityMaid) entity;
                    maid.setSoundPackId(message.soundId);
                }
            });
        }
        context.setPacketHandled(true);
    }
}
