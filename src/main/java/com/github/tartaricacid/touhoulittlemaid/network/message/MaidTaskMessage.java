package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MaidTaskMessage {
    private final int id;
    private final ResourceLocation uid;

    public MaidTaskMessage(int id, ResourceLocation uid) {
        this.id = id;
        this.uid = uid;
    }

    public static void encode(MaidTaskMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.id);
        buf.writeResourceLocation(message.uid);
    }

    public static MaidTaskMessage decode(FriendlyByteBuf buf) {
        return new MaidTaskMessage(buf.readInt(), buf.readResourceLocation());
    }

    public static void handle(MaidTaskMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
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
                    // 客户端也同步一次,便于任务配置界面的一些信息获取
                    NetworkHandler.sendToClientPlayer(new ClientMaidTaskMessage(message.id, message.uid), sender);
                }
            });
        }
        context.setPacketHandled(true);
    }
}
