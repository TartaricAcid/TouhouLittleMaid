package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * 客户端「女仆工作设置」面板保存时发来的配置。
 * UUID 为 NIL_UUID 表示写全局模板；否则只写对应女仆（且必须是发送者自己的）的 NBT。
 */
public class WabWorkSync {
    private final UUID maid;
    private final String action;
    private final String filter;
    private final boolean dropExtra;
    private final String dropBlock;
    private final int maxRadius;

    public WabWorkSync(UUID maid, String action, String filter, boolean dropExtra, String dropBlock, int maxRadius) {
        this.maid = maid == null ? Util.NIL_UUID : maid;
        this.action = action == null ? "" : action;
        this.filter = filter == null ? "" : filter;
        this.dropExtra = dropExtra;
        this.dropBlock = dropBlock == null ? "" : dropBlock;
        this.maxRadius = maxRadius;
    }

    public static void encode(WabWorkSync message, FriendlyByteBuf buf) {
        buf.writeUUID(message.maid);
        buf.writeUtf(message.action);
        buf.writeUtf(message.filter);
        buf.writeBoolean(message.dropExtra);
        buf.writeUtf(message.dropBlock);
        buf.writeInt(message.maxRadius);
    }

    public static WabWorkSync decode(FriendlyByteBuf buf) {
        return new WabWorkSync(buf.readUUID(), buf.readUtf(), buf.readUtf(), buf.readBoolean(),
                buf.readUtf(), buf.readInt());
    }

    public static void handle(WabWorkSync message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender == null) {
                    return;
                }
                if (Util.NIL_UUID.equals(message.maid)) {
                    WabWorkConfig.setTemplate(message.action, message.filter, message.dropExtra,
                            message.dropBlock, message.maxRadius);
                    return;
                }
                EntityMaid maid = sender.level().getEntitiesOfClass(EntityMaid.class,
                        sender.getBoundingBox().inflate(96),
                        m -> message.maid.equals(m.getUUID()) && m.isOwnedBy(sender) && m.isAlive())
                        .stream().findFirst().orElse(null);
                if (maid == null) {
                    return;
                }
                WabWorkConfig.setFilter(maid, message.filter);
                WabWorkConfig.setDropExtra(maid, message.dropExtra);
                WabWorkConfig.setDropBlock(maid, message.dropBlock);
                WabWorkConfig.setMaxRadius(maid, message.maxRadius);
            });
        }
        context.setPacketHandled(true);
    }
}
