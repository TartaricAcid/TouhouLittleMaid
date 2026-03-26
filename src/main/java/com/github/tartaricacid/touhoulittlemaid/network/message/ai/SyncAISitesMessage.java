package com.github.tartaricacid.touhoulittlemaid.network.message.ai;

import com.github.tartaricacid.touhoulittlemaid.ai.service.SerializableSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.SerializerRegister;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ServiceType;
import com.github.tartaricacid.touhoulittlemaid.ai.service.Site;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.AIChatScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.editor.LLMSiteEditorScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.editor.TTSSiteEditorScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.settings.AIChatSettingsHubScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.settings.AIChatSettingsLLMSiteScreen;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Supplier;

public record SyncAISitesMessage(
        Map<String, LLMSite> llmSites,
        Map<String, TTSSite> ttsSites,
        boolean insufficientPermissions
) {
    public static void encode(SyncAISitesMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.llmSites.size());
        message.llmSites.forEach((key, value) -> {
            buf.writeUtf(key);
            buf.writeUtf(value.getApiType());
            writeSiteToNetwork(value, buf);
        });

        buf.writeInt(message.ttsSites.size());
        message.ttsSites.forEach((key, value) -> {
            buf.writeUtf(key);
            buf.writeUtf(value.getApiType());
            writeSiteToNetwork(value, buf);
        });

        buf.writeBoolean(message.insufficientPermissions);
    }

    public static SyncAISitesMessage decode(FriendlyByteBuf buf) {
        int llmSize = buf.readInt();
        Map<String, LLMSite> llmSites = Maps.newHashMap();
        for (int i = 0; i < llmSize; i++) {
            String key = buf.readUtf();
            String apiType = buf.readUtf();
            LLMSite site = readSiteFromNetwork(ServiceType.LLM, apiType, buf);
            if (site != null) {
                llmSites.put(key, site);
            }
        }

        int ttsSize = buf.readInt();
        Map<String, TTSSite> ttsSites = Maps.newHashMap();
        for (int i = 0; i < ttsSize; i++) {
            String key = buf.readUtf();
            String apiType = buf.readUtf();
            TTSSite site = readSiteFromNetwork(ServiceType.TTS, apiType, buf);
            if (site != null) {
                ttsSites.put(key, site);
            }
        }

        boolean insufficientPermissions = buf.readBoolean();

        return new SyncAISitesMessage(llmSites, ttsSites, insufficientPermissions);
    }

    public static void handle(SyncAISitesMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> onHandle(message));
        }
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void onHandle(SyncAISitesMessage message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen instanceof LLMSiteEditorScreen editor) {
            editor.getParentHub().reopenSelf(message.llmSites, message.ttsSites);
        } else if (mc.screen instanceof TTSSiteEditorScreen editor) {
            editor.getParentHub().reopenSelf(message.llmSites, message.ttsSites);
        } else if (mc.screen instanceof AIChatSettingsHubScreen hubScreen) {
            hubScreen.reopenSelf(message.llmSites, message.ttsSites);
        } else if (mc.screen instanceof AIChatScreen screen) {
            mc.setScreen(new AIChatSettingsLLMSiteScreen(screen, message.llmSites, message.ttsSites, message.insufficientPermissions));
        } else {
            mc.setScreen(AIChatSettingsHubScreen.openDefault(null, message.llmSites, message.ttsSites, message.insufficientPermissions));
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Site> void writeSiteToNetwork(T site, FriendlyByteBuf buf) {
        ((SerializableSite<T>) site.serializer()).writeToNetwork(site, buf);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    private static <T extends Site> T readSiteFromNetwork(ServiceType type, String apiType, FriendlyByteBuf buf) {
        SerializableSite<? extends Site> serializer = SerializerRegister.getSerializer(type, apiType);
        if (serializer == null) {
            return null;
        }
        return ((SerializableSite<T>) serializer).fromNetwork(buf);
    }
}
