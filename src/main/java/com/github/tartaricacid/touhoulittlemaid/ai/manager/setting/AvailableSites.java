package com.github.tartaricacid.touhoulittlemaid.ai.manager.setting;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.DefaultLLMSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.google.common.collect.Maps;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static com.github.tartaricacid.touhoulittlemaid.ai.service.SerializerRegister.*;

@SuppressWarnings("all")
public class AvailableSites {
    private static final String FOLDER_NAME = "sites";

    // 服务端缓存的站点信息，包含秘钥等敏感信息
    private static final Map<String, LLMSite> LLM_SITES = Maps.newLinkedHashMap();
    private static final Map<String, TTSSite> TTS_SITES = Maps.newLinkedHashMap();
    private static final Map<String, STTSite> STT_SITES = Maps.newLinkedHashMap();

    public static void init() {
        clearSites();
        addDefaultSites();
        readSites();
        saveSites();

        ClientAvailableSitesSync.init(LLM_SITES, TTS_SITES);
    }

    private static void clearSites() {
        LLM_SITES.clear();
        TTS_SITES.clear();
        STT_SITES.clear();
    }

    private static void addDefaultSites() {
        LLM_SERIALIZER.forEach((key, value) -> AvailableSites.LLM_SITES.put(key, value.defaultSite()));
        TTS_SERIALIZER.forEach((key, value) -> AvailableSites.TTS_SITES.put(key, value.defaultSite()));
        STT_SERIALIZER.forEach((key, value) -> AvailableSites.STT_SITES.put(key, value.defaultSite()));

        // 其他额外的默认站点
        AvailableSites.LLM_SITES.put(DefaultLLMSite.PLAYER2.id(), DefaultLLMSite.PLAYER2);
        AvailableSites.LLM_SITES.put(DefaultLLMSite.ALIYUN.id(), DefaultLLMSite.ALIYUN);
        AvailableSites.LLM_SITES.put(DefaultLLMSite.DEEPSEEK.id(), DefaultLLMSite.DEEPSEEK);
        AvailableSites.LLM_SITES.put(DefaultLLMSite.TENCENT.id(), DefaultLLMSite.TENCENT);
    }

    private static void readSites() {
        Path root = createFolder();
        Path llmConfig = root.resolve("llm.json");
        Path ttsConfig = root.resolve("tts.json");
        Path sttConfig = root.resolve("stt.json");

        if (Files.exists(llmConfig)) {
            try {
                LLM_SITES.putAll(LLMSite.readSites(llmConfig));
            } catch (Exception e) {
                TouhouLittleMaid.LOGGER.error("Failed to read LLM sites", e);
            }
        }

        if (Files.exists(ttsConfig)) {
            try {
                TTS_SITES.putAll(TTSSite.readSites(ttsConfig));
            } catch (Exception e) {
                TouhouLittleMaid.LOGGER.error("Failed to read TTS sites", e);
            }
        }

        if (Files.exists(sttConfig)) {
            try {
                STT_SITES.putAll(STTSite.readSites(sttConfig));
            } catch (Exception e) {
                TouhouLittleMaid.LOGGER.error("Failed to read STT sites", e);
            }
        }
    }

    public static void saveSites() {
        Path root = createFolder();
        Path llmConfig = root.resolve("llm.json");
        Path ttsConfig = root.resolve("tts.json");
        Path sttConfig = root.resolve("stt.json");

        try {
            LLMSite.writeSites(llmConfig, LLM_SITES);
        } catch (Exception e) {
            TouhouLittleMaid.LOGGER.error("Failed to save LLM sites", e);
        }

        try {
            TTSSite.writeSites(ttsConfig, TTS_SITES);
        } catch (Exception e) {
            TouhouLittleMaid.LOGGER.error("Failed to save TTS sites", e);
        }

        try {
            STTSite.writeSites(sttConfig, STT_SITES);
        } catch (Exception e) {
            TouhouLittleMaid.LOGGER.error("Failed to save STT sites", e);
        }
    }

    public static LLMSite getLLMSite(String siteName) {
        return LLM_SITES.get(siteName);
    }

    public static TTSSite getTTSSite(String siteName) {
        return TTS_SITES.get(siteName);
    }

    public static STTSite getSTTSite(String siteName) {
        return STT_SITES.get(siteName);
    }

    private static Path createFolder() {
        Path root = FMLPaths.CONFIGDIR.get().resolve(TouhouLittleMaid.MOD_ID).resolve(FOLDER_NAME);
        if (!root.toFile().isDirectory()) {
            try {
                Files.createDirectories(root);
            } catch (Exception e) {
                TouhouLittleMaid.LOGGER.error("Failed to create sites folder", e);
            }
        }
        return root;
    }
}
