package com.github.tartaricacid.touhoulittlemaid.ai.service;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.site.AvailableSites;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.LLMOpenAISite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.aliyun.STTAliyunSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.player2.STTPlayer2Site;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.siliconflow.STTSiliconflowSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.fishaudio.TTSFishAudioSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.gptsovits.TTSGptSovitsSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.player2.TTSPlayer2Site;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.siliconflow.TTSSiliconflowSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.system.TTSSystemSite;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Map;

public final class SerializerRegister {
    public static Map<String, SerializableSite<LLMSite>> LLM_SERIALIZER = Maps.newHashMap();
    public static Map<String, SerializableSite<TTSSite>> TTS_SERIALIZER = Maps.newHashMap();
    public static Map<String, SerializableSite<STTSite>> STT_SERIALIZER = Maps.newHashMap();

    public static void init() {
        SerializerRegister register = new SerializerRegister();

        register.register(ServiceType.LLM, LLMOpenAISite.API_TYPE, new LLMOpenAISite.Serializer());

        register.register(ServiceType.STT, STTPlayer2Site.API_TYPE, new STTPlayer2Site.Serializer());
        register.register(ServiceType.STT, STTAliyunSite.API_TYPE, new STTAliyunSite.Serializer());
        register.register(ServiceType.STT, STTSiliconflowSite.API_TYPE, new STTSiliconflowSite.Serializer());

        register.register(ServiceType.TTS, TTSSystemSite.API_TYPE, new TTSSystemSite.Serializer());
        register.register(ServiceType.TTS, TTSFishAudioSite.API_TYPE, new TTSFishAudioSite.Serializer());
        register.register(ServiceType.TTS, TTSGptSovitsSite.API_TYPE, new TTSGptSovitsSite.Serializer());
        register.register(ServiceType.TTS, TTSPlayer2Site.API_TYPE, new TTSPlayer2Site.Serializer());
        register.register(ServiceType.TTS, TTSSiliconflowSite.API_TYPE, new TTSSiliconflowSite.Serializer());

        for (ILittleMaid littleMaid : TouhouLittleMaid.EXTENSIONS) {
            littleMaid.registerAIChatSerializer(register);
        }

        LLM_SERIALIZER = ImmutableMap.copyOf(LLM_SERIALIZER);
        TTS_SERIALIZER = ImmutableMap.copyOf(TTS_SERIALIZER);
        STT_SERIALIZER = ImmutableMap.copyOf(STT_SERIALIZER);

        AvailableSites.init();
    }

    @SuppressWarnings("unchecked")
    public void register(ServiceType type, String apiType, SerializableSite<? extends Site> serializableSite) {
        if (type == ServiceType.LLM) {
            LLM_SERIALIZER.put(apiType, (SerializableSite<LLMSite>) serializableSite);
        } else if (type == ServiceType.TTS) {
            TTS_SERIALIZER.put(apiType, (SerializableSite<TTSSite>) serializableSite);
        } else if (type == ServiceType.STT) {
            STT_SERIALIZER.put(apiType, (SerializableSite<STTSite>) serializableSite);
        } else {
            TouhouLittleMaid.LOGGER.error("Unknown service type {}", type);
        }
    }

    @SuppressWarnings("all")
    public static SerializableSite<LLMSite> getLLMSerializer(String apiType) {
        return (SerializableSite<LLMSite>) getSerializer(ServiceType.LLM, apiType);
    }

    @SuppressWarnings("all")
    public static SerializableSite<TTSSite> getTTSSerializer(String apiType) {
        return (SerializableSite<TTSSite>) getSerializer(ServiceType.TTS, apiType);
    }

    @SuppressWarnings("all")
    public static SerializableSite<STTSite> getSTTSerializer(String apiType) {
        return (SerializableSite<STTSite>) getSerializer(ServiceType.STT, apiType);
    }

    public static SerializableSite<? extends Site> getSerializer(ServiceType type, String apiType) {
        if (type == ServiceType.LLM) {
            return LLM_SERIALIZER.get(apiType);
        } else if (type == ServiceType.TTS) {
            return TTS_SERIALIZER.get(apiType);
        } else if (type == ServiceType.STT) {
            return STT_SERIALIZER.get(apiType);
        } else {
            TouhouLittleMaid.LOGGER.error("Unknown service type {}", type);
            return null;
        }
    }
}
