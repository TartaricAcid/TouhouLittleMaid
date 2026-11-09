package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * 由 WAB 主动触发的预设动画。与捡雪球那种固定 ID 不同，这里允许传动画名，
 * 模型没有这个动画时由 GeckoLib 自己忽略，不会中断其他动作。
 */
public class WabAnimationMessage {
    private final int maidId;
    private final String animation;
    private final int durationTicks;

    public WabAnimationMessage(int maidId, String animation, int durationTicks) {
        this.maidId = maidId;
        this.animation = animation;
        this.durationTicks = durationTicks;
    }

    public static void encode(WabAnimationMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.maidId);
        buf.writeUtf(safe(message.animation), 128);
        buf.writeInt(message.durationTicks);
    }

    /**
     * 动画名可能来自 WAB 传的手势参数。writeUtf(…, 128) 超长会直接抛，
     * 落单的代理项也编码不了——这里兜一层，别让一个包把会话拖倒。
     */
    private static String safe(String raw) {
        if (raw == null) return "idle";
        StringBuilder sb = new StringBuilder(Math.min(raw.length(), 120));
        for (int i = 0; i < raw.length() && sb.length() < 120; i++) {
            char c = raw.charAt(i);
            if (!Character.isSurrogate(c)) {
                sb.append(c);
            }
        }
        return sb.isEmpty() ? "idle" : sb.toString();
    }

    public static WabAnimationMessage decode(FriendlyByteBuf buf) {
        return new WabAnimationMessage(buf.readInt(), buf.readUtf(128), buf.readInt());
    }

    public static void handle(WabAnimationMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> handleClient(message));
        }
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClient(WabAnimationMessage message) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        if (level.getEntity(message.maidId) instanceof EntityMaid maid) {
            maid.wabAnimation = message.animation;
            maid.wabAnimationUntil = System.currentTimeMillis() + Math.max(1, message.durationTicks) * 50L;
        }
    }
}
