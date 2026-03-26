package com.github.tartaricacid.touhoulittlemaid.network.message.ai;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.site.AvailableSites;
import com.github.tartaricacid.touhoulittlemaid.ai.service.SerializableSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.SerializerRegister;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ServiceType;
import com.github.tartaricacid.touhoulittlemaid.ai.service.Site;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMSite;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.util.GameModeUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public record SaveLLMSiteMessage(Action action, @Nullable String siteId, boolean enabled, @Nullable LLMSite site) {
    public static SaveLLMSiteMessage create(LLMSite site) {
        return new SaveLLMSiteMessage(Action.CREATE, site.id(), site.enabled(), site);
    }

    public static SaveLLMSiteMessage update(LLMSite site) {
        return new SaveLLMSiteMessage(Action.UPDATE, site.id(), site.enabled(), site);
    }

    public static SaveLLMSiteMessage delete(String siteId) {
        return new SaveLLMSiteMessage(Action.DELETE, siteId, false, null);
    }

    public static SaveLLMSiteMessage toggle(String siteId, boolean enabled) {
        return new SaveLLMSiteMessage(Action.TOGGLE, siteId, enabled, null);
    }

    public static void encode(SaveLLMSiteMessage message, FriendlyByteBuf buf) {
        buf.writeUtf(message.action.name());
        buf.writeUtf(StringUtils.defaultString(message.siteId));
        buf.writeBoolean(message.enabled);

        boolean writeSite = message.site != null && (message.action == Action.CREATE || message.action == Action.UPDATE);
        buf.writeBoolean(writeSite);
        if (writeSite) {
            buf.writeUtf(message.site.getApiType());
            SerializableSite<LLMSite> serializer = getSerializer(message.site.getApiType());
            if (serializer != null) {
                serializer.writeToNetwork(message.site, buf);
            }
        }
    }

    public static SaveLLMSiteMessage decode(FriendlyByteBuf buf) {
        Action action = Action.valueOf(buf.readUtf());
        String siteId = StringUtils.trimToNull(buf.readUtf());
        boolean enabled = buf.readBoolean();
        LLMSite site = null;

        if (buf.readBoolean()) {
            String apiType = buf.readUtf();
            SerializableSite<LLMSite> serializer = getSerializer(apiType);
            site = serializer == null ? null : serializer.fromNetwork(buf);
        }
        return new SaveLLMSiteMessage(action, siteId, enabled, site);
    }

    public static void handle(SaveLLMSiteMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> onHandle(message, context.getSender()));
        }
        context.setPacketHandled(true);
    }

    private static void onHandle(SaveLLMSiteMessage message, @Nullable ServerPlayer player) {
        if (player == null) {
            return;
        }
        if (!GameModeUtil.canEditSite(player)) {
            return;
        }

        boolean changed = switch (message.action) {
            case CREATE -> createSite(message.site);
            case UPDATE -> updateSite(message.site);
            case DELETE -> deleteSite(message.siteId);
            case TOGGLE -> toggleSite(message.siteId, message.enabled);
        };
        if (!changed) {
            return;
        }

        AvailableSites.saveSites();
        NetworkHandler.sendToClientPlayer(new SyncAISitesMessage(AvailableSites.LLM_SITES, AvailableSites.TTS_SITES, false), player);
    }

    private static boolean createSite(@Nullable LLMSite site) {
        if (site == null || StringUtils.isBlank(site.id()) || AvailableSites.LLM_SITES.containsKey(site.id())) {
            return false;
        }
        AvailableSites.LLM_SITES.put(site.id(), site);
        return true;
    }

    private static boolean updateSite(@Nullable LLMSite site) {
        if (site == null || StringUtils.isBlank(site.id())) {
            return false;
        }
        AvailableSites.LLM_SITES.put(site.id(), site);
        return true;
    }

    private static boolean deleteSite(@Nullable String siteId) {
        if (StringUtils.isBlank(siteId)) {
            return false;
        }
        return AvailableSites.LLM_SITES.remove(siteId) != null;
    }

    private static boolean toggleSite(@Nullable String siteId, boolean enabled) {
        if (StringUtils.isBlank(siteId)) {
            return false;
        }
        LLMSite site = AvailableSites.LLM_SITES.get(siteId);
        if (site == null) {
            return false;
        }
        site.setEnabled(enabled);
        return true;
    }

    @SuppressWarnings("unchecked")
    private static SerializableSite<LLMSite> getSerializer(String apiType) {
        SerializableSite<? extends Site> serializer = SerializerRegister.getSerializer(ServiceType.LLM, apiType);
        if (serializer == null) {
            return null;
        }
        return (SerializableSite<LLMSite>) serializer;
    }

    public enum Action {
        CREATE,
        UPDATE,
        DELETE,
        TOGGLE
    }
}
