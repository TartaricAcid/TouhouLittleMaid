package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.advancements.maid.TriggerType;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitTrigger;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record YsmMaidModelMessage(int maidId, String modeId, String texture) {
    public static void encode(YsmMaidModelMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.maidId);
        buf.writeUtf(message.modeId);
        buf.writeUtf(message.texture);
    }

    public static YsmMaidModelMessage decode(FriendlyByteBuf buf) {
        return new YsmMaidModelMessage(buf.readInt(), buf.readUtf(), buf.readUtf());
    }

    public static void handle(YsmMaidModelMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender == null) {
                    return;
                }
                Entity entity = sender.level.getEntity(message.maidId);
                if (entity instanceof EntityMaid maid && maid.isOwnedBy(sender)) {
                    if (sender.isCreative() || MaidConfig.MAID_CHANGE_MODEL.get()) {
                        maid.setIsYsmModel(true);
                        maid.setYsmModel(message.modeId, message.texture);
                        InitTrigger.MAID_EVENT.trigger(sender, TriggerType.CHANGE_MAID_MODEL);
                    } else {
                        sender.sendSystemMessage(Component.translatable("message.touhou_little_maid.change_model.disabled"));
                    }
                }
            });
        }
        context.setPacketHandled(true);
    }
}
