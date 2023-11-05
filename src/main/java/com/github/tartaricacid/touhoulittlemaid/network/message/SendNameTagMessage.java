package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SendNameTagMessage {
    private static final int MAX_STRING_LENGTH = 1024;
    private final int id;
    private final String name;
    private final boolean alwaysShow;

    public SendNameTagMessage(int id, String name, boolean alwaysShow) {
        this.id = id;
        this.name = name;
        this.alwaysShow = alwaysShow;
    }

    public static void encode(SendNameTagMessage message, PacketBuffer buf) {
        buf.writeInt(message.id);
        buf.writeUtf(message.name, MAX_STRING_LENGTH);
        buf.writeBoolean(message.alwaysShow);
    }

    public static SendNameTagMessage decode(PacketBuffer buf) {
        return new SendNameTagMessage(buf.readInt(), buf.readUtf(MAX_STRING_LENGTH), buf.readBoolean());
    }

    public static void handle(SendNameTagMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayerEntity sender = context.getSender();
                if (sender == null) {
                    return;
                }
                Entity entity = sender.level.getEntity(message.id);
                if (entity instanceof EntityMaid) {
                    setMaidNameTag(message, sender, (EntityMaid) entity);
                }
            });
        }
        context.setPacketHandled(true);
    }

    private static void setMaidNameTag(SendNameTagMessage message, ServerPlayerEntity player, EntityMaid maid) {
        String name = message.name.substring(0, Math.min(32, message.name.length()));
        if (player.equals(maid.getOwner()) && player.getMainHandItem().getItem() == Items.NAME_TAG) {
            maid.setCustomName(new StringTextComponent(name));
            maid.setCustomNameVisible(message.alwaysShow);
            maid.setPersistenceRequired();
            if (!player.isCreative()) {
                player.getMainHandItem().shrink(1);
            }
        }
    }
}
