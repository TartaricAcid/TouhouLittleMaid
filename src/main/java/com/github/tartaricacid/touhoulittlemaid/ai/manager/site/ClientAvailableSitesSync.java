package com.github.tartaricacid.touhoulittlemaid.ai.manager.site;

import com.github.tartaricacid.touhoulittlemaid.ai.service.SupportModelSelect;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.google.common.collect.Maps;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Collections;
import java.util.Map;

public class ClientAvailableSitesSync {
    // 需要向玩家侧同步的 AI 站点信息，不包含秘钥等敏感信息
    // 客户端不需要同步 STT 站点信息，因为 STT 就是在客户端使用的
    private static volatile Map<String, Map<String, String>> CLIENT_LLM_SITES = Collections.emptyMap();
    private static volatile Map<String, Map<String, String>> CLIENT_TTS_SITES = Collections.emptyMap();

    private static void init(Map<String, LLMSite> llmSites, Map<String, TTSSite> ttsSites) {
        Map<String, Map<String, String>> llmSnapshot = Maps.newLinkedHashMap();
        for (String key : llmSites.keySet()) {
            LLMSite llmSite = llmSites.get(key);
            if (!llmSite.enabled()) {
                continue;
            }
            if (llmSite instanceof SupportModelSelect select) {
                llmSnapshot.put(key, copyModelMap(select.models()));
            } else {
                llmSnapshot.put(key, Collections.emptyMap());
            }
        }

        Map<String, Map<String, String>> ttsSnapshot = Maps.newLinkedHashMap();
        for (String key : ttsSites.keySet()) {
            TTSSite ttsSite = ttsSites.get(key);
            if (!ttsSite.enabled()) {
                continue;
            }
            if (ttsSite instanceof SupportModelSelect select) {
                ttsSnapshot.put(key, copyModelMap(select.models()));
            } else {
                ttsSnapshot.put(key, Collections.emptyMap());
            }
        }

        CLIENT_LLM_SITES = Collections.unmodifiableMap(llmSnapshot);
        CLIENT_TTS_SITES = Collections.unmodifiableMap(ttsSnapshot);
    }

    public static void readFromNetwork(FriendlyByteBuf buf) {
        Map<String, Map<String, String>> llmSnapshot = Maps.newLinkedHashMap();
        int llmSize = buf.readInt();
        for (int i = 0; i < llmSize; i++) {
            String key = buf.readUtf();
            Map<String, String> models = readMapFromNetwork(buf);
            llmSnapshot.put(key, copyModelMap(models));
        }

        Map<String, Map<String, String>> ttsSnapshot = Maps.newLinkedHashMap();
        int ttsSize = buf.readInt();
        for (int i = 0; i < ttsSize; i++) {
            String key = buf.readUtf();
            Map<String, String> models = readMapFromNetwork(buf);
            ttsSnapshot.put(key, copyModelMap(models));
        }

        CLIENT_LLM_SITES = Collections.unmodifiableMap(llmSnapshot);
        CLIENT_TTS_SITES = Collections.unmodifiableMap(ttsSnapshot);
    }

    public static void writeToNetwork(FriendlyByteBuf buf) {
        // 先从站点数据里读取
        init(AvailableSites.LLM_SITES, AvailableSites.TTS_SITES);

        // 然后再发送到客户端
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

    public static String getLLMModelName(String site, String modelId) {
        if (CLIENT_LLM_SITES.containsKey(site)) {
            Map<String, String> models = CLIENT_LLM_SITES.get(site);
            if (models.containsKey(modelId)) {
                return models.get(modelId);
            }
        }
        return "*";
    }

    public static String getTTSModelName(String site, String modelId) {
        if (CLIENT_TTS_SITES.containsKey(site)) {
            Map<String, String> models = CLIENT_TTS_SITES.get(site);
            if (models.containsKey(modelId)) {
                return models.get(modelId);
            }
        }
        return "*";
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

    private static Map<String, String> copyModelMap(Map<String, String> source) {
        return Collections.unmodifiableMap(Maps.newLinkedHashMap(source));
    }

    private static void writeMapToNetwork(FriendlyByteBuf buf, Map<String, String> map) {
        buf.writeInt(map.size());
        for (var entry : map.entrySet()) {
            buf.writeUtf(entry.getKey());
            buf.writeUtf(entry.getValue());
        }
    }
}
