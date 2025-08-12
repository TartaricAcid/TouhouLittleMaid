package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.client.sound.data.MaidSoundInstanceAtPos;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLocationUtil.getResourceLocation;

public record PlayMaidSoundAtPosPackage(ResourceLocation soundEvent, String id,
                                        double x, double y, double z,
                                        float volume, float pitch) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PlayMaidSoundAtPosPackage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("play_maid_sound_at_pos"));
    public static final StreamCodec<ByteBuf, PlayMaidSoundAtPosPackage> STREAM_CODEC = StreamCodec.of(
            (buf, msg) -> {
                ResourceLocation.STREAM_CODEC.encode(buf, msg.soundEvent);
                ByteBufCodecs.STRING_UTF8.encode(buf, msg.id);
                buf.writeDouble(msg.x);
                buf.writeDouble(msg.y);
                buf.writeDouble(msg.z);
                buf.writeFloat(msg.volume);
                buf.writeFloat(msg.pitch);
            },
            buf -> new PlayMaidSoundAtPosPackage(
                    ResourceLocation.STREAM_CODEC.decode(buf),
                    ByteBufCodecs.STRING_UTF8.decode(buf),
                    buf.readDouble(),
                    buf.readDouble(),
                    buf.readDouble(),
                    buf.readFloat(),
                    buf.readFloat()
            )
    );

    public static void handle(PlayMaidSoundAtPosPackage message, IPayloadContext context) {
        if (context.flow().isClientbound()) {
            context.enqueueWork(() -> playSound(message));
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void playSound(PlayMaidSoundAtPosPackage message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        SoundEvent event = BuiltInRegistries.SOUND_EVENT.get(message.soundEvent);
        if (event == null) {
            return;
        }
        mc.getSoundManager().play(new MaidSoundInstanceAtPos(event, message.id,
                message.x, message.y, message.z, message.volume, message.pitch));
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}


