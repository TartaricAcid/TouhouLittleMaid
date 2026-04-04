package com.github.tartaricacid.touhoulittlemaid.network.message.ai;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.site.AvailableSites;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSConfig;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSystemServices;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class TTSSystemAudioToClientMessage {
    private final String siteName;
    private final String chatText;
    private final TTSConfig config;
    private final TTSSystemServices services;

    public TTSSystemAudioToClientMessage(String siteName, String chatText, TTSConfig config, TTSSystemServices services) {
        this.siteName = siteName;
        this.chatText = chatText;
        this.config = config;
        this.services = services;
    }

    public static void encode(TTSSystemAudioToClientMessage message, FriendlyByteBuf buf) {
        buf.writeUtf(message.siteName);
        message.services.writeToNetwork(message.chatText, message.config, buf);
    }

    @Nullable
    public static TTSSystemAudioToClientMessage decode(FriendlyByteBuf buf) {
        String siteName = buf.readUtf();
        TTSSite ttsSite = AvailableSites.getTTSSite(siteName);
        if (ttsSite.client() instanceof TTSSystemServices services) {
            Pair<String, TTSConfig> pair = services.readFromNetwork(buf);
            return new TTSSystemAudioToClientMessage(siteName, pair.getLeft(), pair.getRight(), services);
        }
        return null;
    }

    public static void handle(@Nullable TTSSystemAudioToClientMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient() && message != null) {
            context.enqueueWork(() -> onHandle(message));
        }
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void onHandle(TTSSystemAudioToClientMessage message) {
        TTSSite ttsSite = AvailableSites.getTTSSite(message.siteName);
        if (ttsSite == null || !ttsSite.enabled()) {
            return;
        }
        ttsSite.client().play(message.chatText, message.config, null);
    }
}
