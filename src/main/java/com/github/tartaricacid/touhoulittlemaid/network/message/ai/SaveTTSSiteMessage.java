package com.github.tartaricacid.touhoulittlemaid.network.message.ai;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.site.AvailableSites;
import com.github.tartaricacid.touhoulittlemaid.ai.service.SerializableSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.SerializerRegister;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ServiceType;
import com.github.tartaricacid.touhoulittlemaid.ai.service.Site;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.util.GameModeUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public record SaveTTSSiteMessage(Action action, @Nullable String siteId, boolean enabled, @Nullable TTSSite site) {
    public static SaveTTSSiteMessage update(TTSSite site) {
        return new SaveTTSSiteMessage(Action.UPDATE, site.id(), site.enabled(), site);
    }

    public static SaveTTSSiteMessage toggle(String siteId, boolean enabled) {
        return new SaveTTSSiteMessage(Action.TOGGLE, siteId, enabled, null);
    }

    public static void encode(SaveTTSSiteMessage message, FriendlyByteBuf buf) {
        buf.writeUtf(message.action.name());
        buf.writeUtf(StringUtils.defaultString(message.siteId));
        buf.writeBoolean(message.enabled);

        boolean writeSite = message.site != null && message.action == Action.UPDATE;
        buf.writeBoolean(writeSite);
        if (writeSite) {
            buf.writeUtf(message.site.getApiType());
            SerializableSite<TTSSite> serializer = getSerializer(message.site.getApiType());
            if (serializer != null) {
                serializer.writeToNetwork(message.site, buf);
            }
        }
    }

    public static SaveTTSSiteMessage decode(FriendlyByteBuf buf) {
        Action action = Action.valueOf(buf.readUtf());
        String siteId = StringUtils.trimToNull(buf.readUtf());
        boolean enabled = buf.readBoolean();
        TTSSite site = null;

        if (buf.readBoolean()) {
            String apiType = buf.readUtf();
            SerializableSite<TTSSite> serializer = getSerializer(apiType);
            site = serializer == null ? null : serializer.fromNetwork(buf);
        }
        return new SaveTTSSiteMessage(action, siteId, enabled, site);
    }

    public static void handle(SaveTTSSiteMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> onHandle(message, context.getSender()));
        }
        context.setPacketHandled(true);
    }

    private static void onHandle(SaveTTSSiteMessage message, @Nullable ServerPlayer player) {
        if (!GameModeUtil.canEditSite(player)) {
            return;
        }

        boolean changed = switch (message.action) {
            case UPDATE -> updateSite(message.site);
            case TOGGLE -> toggleSite(message.siteId, message.enabled);
        };
        if (!changed) {
            return;
        }

        AvailableSites.saveSites();
        NetworkHandler.sendToClientPlayer(new SyncAISitesMessage(AvailableSites.LLM_SITES, AvailableSites.TTS_SITES, true), player);
    }

    private static boolean updateSite(@Nullable TTSSite site) {
        if (site == null || StringUtils.isBlank(site.id())) {
            return false;
        }
        AvailableSites.TTS_SITES.put(site.id(), site);
        return true;
    }

    private static boolean toggleSite(@Nullable String siteId, boolean enabled) {
        if (StringUtils.isBlank(siteId)) {
            return false;
        }
        TTSSite site = AvailableSites.TTS_SITES.get(siteId);
        if (site == null) {
            return false;
        }
        site.setEnabled(enabled);
        return true;
    }

    @SuppressWarnings("unchecked")
    private static SerializableSite<TTSSite> getSerializer(String apiType) {
        SerializableSite<? extends Site> serializer = SerializerRegister.getSerializer(ServiceType.TTS, apiType);
        if (serializer == null) {
            return null;
        }
        return (SerializableSite<TTSSite>) serializer;
    }

    public enum Action {
        UPDATE,
        TOGGLE
    }
}
