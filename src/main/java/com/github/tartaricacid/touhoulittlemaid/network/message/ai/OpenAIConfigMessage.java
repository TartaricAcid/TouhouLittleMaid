package com.github.tartaricacid.touhoulittlemaid.network.message.ai;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.site.AvailableSites;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.util.GameModeUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.function.Supplier;

@SuppressWarnings("InstantiationOfUtilityClass")
public class OpenAIConfigMessage {
    public static void sendToServer() {
        NetworkHandler.CHANNEL.sendToServer(new OpenAIConfigMessage());
    }

    public static void encode(OpenAIConfigMessage message, FriendlyByteBuf buf) {
    }

    public static OpenAIConfigMessage decode(FriendlyByteBuf buf) {
        return new OpenAIConfigMessage();
    }

    public static void handle(OpenAIConfigMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> onHandle(context.getSender()));
        }
        context.setPacketHandled(true);
    }

    private static void onHandle(@Nullable ServerPlayer player) {
        if (player == null) {
            return;
        }

        // 是否发送站点数据
        if (GameModeUtil.canEditSite(player)) {
            SyncAISitesMessage msg = new SyncAISitesMessage(AvailableSites.LLM_SITES, AvailableSites.TTS_SITES, false);
            NetworkHandler.sendToClientPlayer(msg, player);
        } else {
            // 否则发送一个空的站点数据
            SyncAISitesMessage msg = new SyncAISitesMessage(Collections.emptyMap(), Collections.emptyMap(), true);
            NetworkHandler.sendToClientPlayer(msg, player);
        }
    }
}
