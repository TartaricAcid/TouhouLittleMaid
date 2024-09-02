package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.ItemServantBell;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServantBellSetMessage {
    private final int id;
    private final String tip;

    public ServantBellSetMessage(int id, String tip) {
        this.id = id;
        this.tip = tip;
    }

    public static void encode(ServantBellSetMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.id);
        buf.writeUtf(message.tip);
    }

    public static ServantBellSetMessage decode(FriendlyByteBuf buf) {
        return new ServantBellSetMessage(buf.readInt(), buf.readUtf());
    }

    public static void handle(ServantBellSetMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender == null) {
                    return;
                }
                Entity entity = sender.level.getEntity(message.id);
                if (entity instanceof EntityMaid maid && maid.isOwnedBy(sender)) {
                    ItemStack mainHandItem = sender.getMainHandItem();
                    ItemServantBell.recordMaidInfo(mainHandItem, maid.getUUID(), message.tip);
                }
            });
        }
        context.setPacketHandled(true);
    }
}
