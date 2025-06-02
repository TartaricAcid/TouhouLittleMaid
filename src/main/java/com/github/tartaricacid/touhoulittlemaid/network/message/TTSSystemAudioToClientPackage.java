package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.site.AvailableSites;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSConfig;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSystemServices;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLocationUtil.getResourceLocation;

public record TTSSystemAudioToClientPackage(String siteName, String chatText, TTSConfig config,
                                            TTSSystemServices services) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<TTSSystemAudioToClientPackage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("tts_system_audio_to_client"));
    public static final StreamCodec<ByteBuf, TTSSystemAudioToClientPackage> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public TTSSystemAudioToClientPackage decode(ByteBuf byteBuf) {
            FriendlyByteBuf buf = new FriendlyByteBuf(byteBuf);
            String siteName = buf.readUtf();
            TTSSite ttsSite = AvailableSites.getTTSSite(siteName);
            if (ttsSite.client() instanceof TTSSystemServices services) {
                Pair<String, TTSConfig> pair = services.readFromNetwork(buf);
                return new TTSSystemAudioToClientPackage(siteName, pair.getLeft(), pair.getRight(), services);
            }
            throw new IllegalArgumentException("Invalid TTS site: " + siteName);
        }

        @Override
        public void encode(ByteBuf byteBuf, TTSSystemAudioToClientPackage message) {
            FriendlyByteBuf buf = new FriendlyByteBuf(byteBuf);
            buf.writeUtf(message.siteName);
            message.services.writeToNetwork(message.chatText, message.config, buf);
        }
    };

    public static void handle(TTSSystemAudioToClientPackage message, IPayloadContext context) {
        if (context.flow().isClientbound()) {
            context.enqueueWork(() -> onHandle(message));
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void onHandle(TTSSystemAudioToClientPackage message) {
        TTSSite ttsSite = AvailableSites.getTTSSite(message.siteName);
        if (ttsSite == null || !ttsSite.enabled()) {
            return;
        }
        ttsSite.client().play(message.chatText, message.config, null);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
