package com.github.tartaricacid.touhoulittlemaid.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TTSSystemAudioToClientMessage {
    private final String chatText;

    public TTSSystemAudioToClientMessage(String chatText) {
        this.chatText = chatText;
    }

    public static void encode(TTSSystemAudioToClientMessage message, FriendlyByteBuf buf) {
        buf.writeUtf(message.chatText);
    }

    public static TTSSystemAudioToClientMessage decode(FriendlyByteBuf buf) {
        return new TTSSystemAudioToClientMessage(buf.readUtf());
    }

    public static void handle(TTSSystemAudioToClientMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> onHandle(message));
        }
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void onHandle(TTSSystemAudioToClientMessage message) {
        Minecraft mc = Minecraft.getInstance();
        mc.getNarrator().narrator.say(message.chatText, true);
    }
}
