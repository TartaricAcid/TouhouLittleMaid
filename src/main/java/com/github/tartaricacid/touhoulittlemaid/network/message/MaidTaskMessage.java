package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.config.Config;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MaidTaskMessage {
    private final int id;
    private final ResourceLocation uid;

    public MaidTaskMessage(int id, ResourceLocation uid) {
        this.id = id;
        this.uid = uid;
    }

    public static void encode(MaidTaskMessage message, PacketBuffer buf) {
        buf.writeInt(message.id);
        buf.writeResourceLocation(message.uid);
    }

    public static MaidTaskMessage decode(PacketBuffer buf) {
        return new MaidTaskMessage(buf.readInt(), buf.readResourceLocation());
    }

    public static void handle(MaidTaskMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayerEntity sender = context.getSender();
                if (sender == null) {
                    return;
                }
                Entity entity = sender.level.getEntity(message.id);
                if (entity instanceof EntityMaid && ((EntityMaid) entity).isOwnedBy(sender)) {
                    if (sender.isCreative() || Config.MAID_CHANGE_MODEL.get()) {
                        ((EntityMaid) entity).setTask(TaskManager.findTask(message.uid).orElse(TaskManager.getIdleTask()));
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
