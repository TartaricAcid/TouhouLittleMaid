package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * 用于同步客户端播放动画的消息
 * 目前只包含拾取雪球的动画
 */
public class MaidAnimationMessage {
    public static final int NONE = 0;
    public static final int PICK_UP_SNOWBALL = 1;
    public static final int SWF_AIM = 2;
    public static final int SWF_RELOAD = 3;
    public static final int SWF_FIRE = 4;

    private final int maidId;
    private final int animationId;

    public MaidAnimationMessage(int maidId, int animationId) {
        this.maidId = maidId;
        this.animationId = animationId;
    }

    public static MaidAnimationMessage pickUpSnowball(EntityMaid maid) {
        // 播放丢雪球动画之前，先禁止女仆移动
        // 标记服务端事件
        maid.animationId = PICK_UP_SNOWBALL;
        maid.animationRecordTime = System.currentTimeMillis();
        // 返回消息
        return new MaidAnimationMessage(maid.getId(), PICK_UP_SNOWBALL);
    }

    public static void encode(MaidAnimationMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.maidId);
        buf.writeInt(message.animationId);
    }

    public static MaidAnimationMessage decode(FriendlyByteBuf buf) {
        return new MaidAnimationMessage(buf.readInt(), buf.readInt());
    }

    public static void handle(MaidAnimationMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> handle(message));
        }
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handle(MaidAnimationMessage message) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        if (level.getEntity(message.maidId) instanceof EntityMaid maid) {
            maid.animationId = message.animationId;
            maid.animationRecordTime = System.currentTimeMillis();
            maid.shouldReset = true;
        }
    }
}
