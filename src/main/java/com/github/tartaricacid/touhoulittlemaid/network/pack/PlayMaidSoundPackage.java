package com.github.tartaricacid.touhoulittlemaid.network.pack;

import com.github.tartaricacid.touhoulittlemaid.client.sound.data.MaidSoundInstance;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.getResourceLocation;

public record PlayMaidSoundPackage(ResourceLocation soundEvent, String id,
                                   int entityId) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PlayMaidSoundPackage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("play_maid_sound"));
    public static final StreamCodec<ByteBuf, PlayMaidSoundPackage> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC,
            PlayMaidSoundPackage::soundEvent,
            ByteBufCodecs.STRING_UTF8,
            PlayMaidSoundPackage::id,
            ByteBufCodecs.VAR_INT,
            PlayMaidSoundPackage::entityId,
            PlayMaidSoundPackage::new
    );

    public static void handle(PlayMaidSoundPackage message, IPayloadContext context) {
        if (context.flow().isClientbound()) {
            context.enqueueWork(() -> playSound(message));
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void playSound(PlayMaidSoundPackage message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            Entity entity = mc.level.getEntity(message.entityId);
            if (entity instanceof EntityMaid maid) {
                SoundEvent event = BuiltInRegistries.SOUND_EVENT.get(message.soundEvent);
                if (event != null) {
                    mc.getSoundManager().play(new MaidSoundInstance(event, message.id, maid));
                }
            }
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
