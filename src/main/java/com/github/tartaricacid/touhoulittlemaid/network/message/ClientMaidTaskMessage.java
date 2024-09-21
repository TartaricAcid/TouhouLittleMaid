package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ClientMaidTaskMessage(int id, ResourceLocation uid) {

    public static void encode(ClientMaidTaskMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.id);
        buf.writeResourceLocation(message.uid);
    }

    public static ClientMaidTaskMessage decode(FriendlyByteBuf buf) {
        return new ClientMaidTaskMessage(buf.readInt(), buf.readResourceLocation());
    }

    public static void handle(ClientMaidTaskMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> {
                LocalPlayer sender = Minecraft.getInstance().player;
                if (sender == null) {
                    return;
                }
                Entity entity = sender.level.getEntity(message.id);
                if (entity instanceof EntityMaid maid && maid.isOwnedBy(sender)) {
                    IMaidTask task = TaskManager.findTask(message.uid).orElse(TaskManager.getIdleTask());
                    if (!task.isEnable(maid)) {
                        return;
                    }
                    maid.setTask(task);
                }
            });
        }
        context.setPacketHandled(true);
    }
}
