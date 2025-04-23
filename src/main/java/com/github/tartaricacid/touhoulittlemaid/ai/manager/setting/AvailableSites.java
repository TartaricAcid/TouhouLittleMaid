package com.github.tartaricacid.touhoulittlemaid.ai.manager.setting;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.service.chat.ChatApiType;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSApiType;
import com.github.tartaricacid.touhoulittlemaid.util.GetJarResources;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AvailableSites {
    public static final String FILE_NAME = "available_sites.yml";

    // 服务端缓存的站点信息，包含秘钥等敏感信息
    private static final Map<String, Site> CHAT_SITES = Maps.newLinkedHashMap();
    private static final Map<String, Site> TTS_SITES = Maps.newLinkedHashMap();

    // 用于向客户端发送的站点信息，用来供玩家选择不同的站点，不包含敏感信息
    private static final Map<String, List<String>> CLIENT_CHAT_SITES = Maps.newLinkedHashMap();
    private static final Map<String, List<String>> CLIENT_TTS_SITES = Maps.newLinkedHashMap();

    private static final Path SITES_FILES = Paths.get("config", TouhouLittleMaid.MOD_ID, FILE_NAME);
    private static final String JAR_SITES_FILES = String.format("/assets/%s/config/%s", TouhouLittleMaid.MOD_ID, FILE_NAME);

    public static void readSites() {
        CHAT_SITES.clear();
        TTS_SITES.clear();
        CLIENT_CHAT_SITES.clear();
        CLIENT_TTS_SITES.clear();

        Yaml yaml = new Yaml();
        Map<String, LinkedHashMap<String, Object>> allSites = Maps.newLinkedHashMap();

        // 先尝试读取 jar 包内的文件
        try (InputStream stream = GetJarResources.readTouhouLittleMaidFile(JAR_SITES_FILES)) {
            allSites.putAll(yaml.load(stream));
        } catch (Exception e) {
            TouhouLittleMaid.LOGGER.error("Failed to read available sites jar file", e);
        }

        // 再尝试读取配置文件下的内容
        if (SITES_FILES.toFile().isFile()) {
            try (FileReader reader = new FileReader(SITES_FILES.toFile(), StandardCharsets.UTF_8)) {
                allSites.putAll(yaml.load(reader));
            } catch (Exception e) {
                TouhouLittleMaid.LOGGER.error("Failed to read available sites file", e);
            }
        }

        // 分类
        allSites.forEach((key, value) -> {
            fixOldVersionConfig(value);
            try {
                Site site = new Site(value);
                // 必须设置了 key 的才能用于聊天
                if (site.isChat() && StringUtils.isNotBlank(site.getApiKey())) {
                    CHAT_SITES.put(key, site);
                    CLIENT_CHAT_SITES.put(key, site.getModels());
                }
                // 必须设置了 key 的才能用于 tts
                if (site.isTts() && StringUtils.isNotBlank(site.getApiKey())) {
                    TTS_SITES.put(key, site);
                    CLIENT_TTS_SITES.put(key, site.getModels());
                }
            } catch (Exception e) {
                TouhouLittleMaid.LOGGER.error("Failed to load site: {}", key, e);
            }
        });

        // 保存
        saveSites(allSites);
    }

    public static void saveSites(Map<String, LinkedHashMap<String, Object>> allSites) {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);

        // 保存
        try (FileWriter writer = new FileWriter(SITES_FILES.toFile(), StandardCharsets.UTF_8)) {
            yaml.dump(allSites, writer);
        } catch (Exception e) {
            TouhouLittleMaid.LOGGER.error("Failed to save available sites file", e);
        }
    }

    public static Site getChatSite(String key) {
        return CHAT_SITES.get(key);
    }

    public static Site getTtsSite(String key) {
        return TTS_SITES.get(key);
    }

    @Nullable
    public static Site getFirstAvailableChatSite() {
        if (CHAT_SITES.isEmpty()) {
            return null;
        }
        return CHAT_SITES.values().stream().findFirst().orElse(null);
    }

    @Nullable
    public static Site getFirstAvailableTtsSite() {
        if (TTS_SITES.isEmpty()) {
            return null;
        }
        return TTS_SITES.values().stream().findFirst().orElse(null);
    }

    public static Map<String, List<String>> getClientChatSites() {
        return CLIENT_CHAT_SITES;
    }

    public static Map<String, List<String>> getClientTtsSites() {
        return CLIENT_TTS_SITES;
    }

    private static void fixOldVersionConfig(LinkedHashMap<String, Object> map) {
        String type = Objects.requireNonNullElse((String) map.get("type"), StringUtils.EMPTY);
        String apiType = Objects.requireNonNullElse((String) map.get("api_type"), StringUtils.EMPTY);
        // 如果缺失 api_type 字段，那么依据类型给其补上默认的
        if (apiType.isBlank()) {
            if ("chat".equals(type)) {
                map.put("api_type", ChatApiType.OPENAI.getName());
            }
            if ("tts".equals(type)) {
                map.put("api_type", TTSApiType.FISH_AUDIO.getName());
            }
        }
    }
}
