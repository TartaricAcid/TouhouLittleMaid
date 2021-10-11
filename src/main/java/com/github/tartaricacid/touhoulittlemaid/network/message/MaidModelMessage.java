package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MaidModelMessage {
    private final int id;
    private final ResourceLocation modelId;

    public MaidModelMessage(int id, ResourceLocation modelId) {
        this.id = id;
        this.modelId = modelId;
    }

    public static void encode(MaidModelMessage message, PacketBuffer buf) {
        buf.writeInt(message.id);
        buf.writeResourceLocation(message.modelId);
    }

    public static MaidModelMessage decode(PacketBuffer buf) {
        return new MaidModelMessage(buf.readInt(), buf.readResourceLocation());
    }

    public static void handle(MaidModelMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayerEntity sender = context.getSender();
                if (sender == null) {
                    return;
                }
                Entity entity = sender.level.getEntity(message.id);
                if (entity instanceof EntityMaid && ((EntityMaid) entity).isOwnedBy(sender)) {
                    if (sender.isCreative() || MaidConfig.MAID_CHANGE_MODEL.get()) {
                        ((EntityMaid) entity).setModelId(message.modelId.toString());
                    } else {
                        sender.sendMessage(new TranslationTextComponent("message.touhou_little_maid.change_model.disabled"),
                                ChatType.GAME_INFO, Util.NIL_UUID);
                    }
                }
            });
        }
        context.setPacketHandled(true);
    }
}
