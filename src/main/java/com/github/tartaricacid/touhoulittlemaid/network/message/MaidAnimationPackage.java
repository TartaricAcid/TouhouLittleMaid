package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLocationUtil.getResourceLocation;

/**
 * 用于同步客户端播放动画的消息
 * 目前只包含拾取雪球的动画
 */
public record MaidAnimationPackage(int maidId, int animationId) implements CustomPacketPayload {
    public static final int NONE = 0;
    public static final int PICK_UP_SNOWBALL = 1;

    public static final CustomPacketPayload.Type<MaidAnimationPackage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("maid_animation"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MaidAnimationPackage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, MaidAnimationPackage::maidId,
            ByteBufCodecs.VAR_INT, MaidAnimationPackage::animationId,
            MaidAnimationPackage::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static MaidAnimationPackage pickUpSnowball(EntityMaid maid) {
        // 播放丢雪球动画之前，先禁止女仆移动
        // 标记服务端事件
        maid.animationId = PICK_UP_SNOWBALL;
        maid.animationRecordTime = System.currentTimeMillis();
        // 返回消息
        return new MaidAnimationPackage(maid.getId(), PICK_UP_SNOWBALL);
    }

    public static void handle(MaidAnimationPackage message, IPayloadContext context) {
        if (context.flow().isClientbound()) {
            context.enqueueWork(() -> handle(message));
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void handle(MaidAnimationPackage message) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        if (level.getEntity(message.maidId) instanceof EntityMaid maid) {
            maid.animationId = message.animationId;
            maid.animationRecordTime = System.currentTimeMillis();
        }
    }
}
