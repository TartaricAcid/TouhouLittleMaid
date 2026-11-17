package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLocationUtil.getResourceLocation;

/**
 * 设置面板「保存」按钮：客户端 → 服务器写工作配置。
 * maid 为 NIL_UUID（0）时写全局模板，否则只写给发送者拥有的那只女仆。
 */
public record WabWorkSync(UUID maid, String action, String filter, boolean dropExtra,
                          String dropBlock, int maxRadius) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<WabWorkSync> TYPE =
            new CustomPacketPayload.Type<>(getResourceLocation("wab_work_sync"));

    public static final StreamCodec<FriendlyByteBuf, WabWorkSync> STREAM_CODEC = StreamCodec.of(
            (buf, message) -> {
                buf.writeUUID(message.maid() == null ? Util.NIL_UUID : message.maid());
                buf.writeUtf(message.action() == null ? "" : message.action());
                buf.writeUtf(message.filter() == null ? "" : message.filter());
                buf.writeBoolean(message.dropExtra());
                buf.writeUtf(message.dropBlock() == null ? "" : message.dropBlock());
                buf.writeInt(message.maxRadius());
            },
            buf -> new WabWorkSync(buf.readUUID(), buf.readUtf(), buf.readUtf(),
                    buf.readBoolean(), buf.readUtf(), buf.readInt())
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(WabWorkSync message, IPayloadContext context) {
        if (!context.flow().isServerbound()) return;
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer sender)) return;
            UUID id = message.maid();
            if (id != null && !id.equals(Util.NIL_UUID)) {
                WabBridge bridge = WabBridge.get();
                EntityMaid maid = bridge == null ? null : bridge.ownedMaids().stream()
                        .filter(m -> m.getUUID().equals(id) && m.isOwnedBy(sender) && m.isAlive())
                        .findFirst().orElse(null);
                if (maid != null) {
                    WabWorkConfig.apply(maid, message.filter(), message.dropExtra(),
                            message.dropBlock(), message.maxRadius());
                }
            } else {
                WabWorkConfig.setGlobal(message.filter(), message.dropExtra(),
                        message.dropBlock(), message.maxRadius());
            }
        });
    }
}
