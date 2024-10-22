package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.advancements.maid.TriggerType;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitTrigger;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.getResourceLocation;

public record SetMaidSoundIdPackage(int entityId, String soundId) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SetMaidSoundIdPackage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("set_maid_sound_id"));
    public static final StreamCodec<ByteBuf, SetMaidSoundIdPackage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            SetMaidSoundIdPackage::entityId,
            ByteBufCodecs.STRING_UTF8,
            SetMaidSoundIdPackage::soundId,
            SetMaidSoundIdPackage::new
    );

    public static void handle(SetMaidSoundIdPackage message, IPayloadContext context) {
        if (context.flow().isServerbound()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = (ServerPlayer) context.player();
                Entity entity = sender.level.getEntity(message.entityId);
                if (entity instanceof EntityMaid maid && maid.isOwnedBy(sender)) {
                    maid.setSoundPackId(message.soundId);
                    InitTrigger.MAID_EVENT.get().trigger(sender, TriggerType.CHANGE_MAID_SOUND);
                }
            });
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
