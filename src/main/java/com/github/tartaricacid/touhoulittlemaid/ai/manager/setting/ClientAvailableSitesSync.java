package com.github.tartaricacid.touhoulittlemaid.ai.manager.setting;

import com.github.tartaricacid.touhoulittlemaid.ai.service.SupportModelSelect;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.google.common.collect.Maps;
import net.minecraft.network.FriendlyByteBuf;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.Map;

public class ClientAvailableSitesSync {
    // 需要向玩家侧同步的 AI 站点信息，不包含秘钥等敏感信息
    // 客户端不需要同步 STT 站点信息，因为 STT 就是在客户端使用的
    private static final Map<String, Map<String, String>> CLIENT_LLM_SITES = Maps.newLinkedHashMap();
    private static final Map<String, Map<String, String>> CLIENT_TTS_SITES = Maps.newLinkedHashMap();

    public static void init(Map<String, LLMSite> llmSites, Map<String, TTSSite> ttsSites) {
        CLIENT_LLM_SITES.clear();
        CLIENT_TTS_SITES.clear();

        for (String key : llmSites.keySet()) {
            LLMSite llmSite = llmSites.get(key);
            if (!llmSite.enabled()) {
                continue;
            }
            if (llmSite instanceof SupportModelSelect select) {
                CLIENT_LLM_SITES.put(key, select.models());
            } else {
                CLIENT_LLM_SITES.put(key, Collections.emptyMap());
            }
        }

        for (String key : ttsSites.keySet()) {
            TTSSite ttsSite = ttsSites.get(key);
            if (!ttsSite.enabled()) {
                continue;
            }
            if (ttsSite instanceof SupportModelSelect select) {
                CLIENT_TTS_SITES.put(key, select.models());
            } else {
                CLIENT_TTS_SITES.put(key, Collections.emptyMap());
            }
        }
    }

    public static Pair<Map<String, Map<String, String>>, Map<String, Map<String, String>>> readFromNetwork(FriendlyByteBuf buf) {
        Map<String, Map<String, String>> llmSites = Maps.newLinkedHashMap();
        Map<String, Map<String, String>> ttsSites = Maps.newLinkedHashMap();

        int llmSize = buf.readInt();
        for (int i = 0; i < llmSize; i++) {
            String key = buf.readUtf();
            Map<String, String> models = readMapFromNetwork(buf);
            llmSites.put(key, models);
        }

        int ttsSize = buf.readInt();
        for (int i = 0; i < ttsSize; i++) {
            String key = buf.readUtf();
            Map<String, String> models = readMapFromNetwork(buf);
            ttsSites.put(key, models);
        }

        return Pair.of(llmSites, ttsSites);
    }

    public static void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeInt(CLIENT_LLM_SITES.size());
        for (var entry : CLIENT_LLM_SITES.entrySet()) {
            buf.writeUtf(entry.getKey());
            writeMapToNetwork(buf, entry.getValue());
        }
        buf.writeInt(CLIENT_TTS_SITES.size());
        for (var entry : CLIENT_TTS_SITES.entrySet()) {
            buf.writeUtf(entry.getKey());
            writeMapToNetwork(buf, entry.getValue());
        }
    }

    public static Map<String, Map<String, String>> getClientLLMSites() {
        return CLIENT_LLM_SITES;
    }

    public static Map<String, Map<String, String>> getClientTTSSites() {
        return CLIENT_TTS_SITES;
    }

    private static Map<String, String> readMapFromNetwork(FriendlyByteBuf buf) {
        Map<String, String> map = Maps.newLinkedHashMap();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            String key = buf.readUtf();
            String value = buf.readUtf();
            map.put(key, value);
        }
        return map;
    }

    private static void writeMapToNetwork(FriendlyByteBuf buf, Map<String, String> map) {
        buf.writeInt(map.size());
        for (var entry : map.entrySet()) {
            buf.writeUtf(entry.getKey());
            buf.writeUtf(entry.getValue());
        }
    }
}
