package com.github.tartaricacid.touhoulittlemaid.ai.service;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.AvailableSites;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.OpenAISite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.aliyun.AliyunSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.player2.Player2Site;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.fishaudio.FishAudioSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.gptsovits.GptSovitsSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.system.SystemSite;
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

        register.register(ServiceType.LLM, OpenAISite.API_TYPE, new OpenAISite.Serializer());

        register.register(ServiceType.STT, Player2Site.API_TYPE, new Player2Site.Serializer());
        register.register(ServiceType.STT, AliyunSite.API_TYPE, new AliyunSite.Serializer());

        register.register(ServiceType.TTS, SystemSite.API_TYPE, new SystemSite.Serializer());
        register.register(ServiceType.TTS, FishAudioSite.API_TYPE, new FishAudioSite.Serializer());
        register.register(ServiceType.TTS, GptSovitsSite.API_TYPE, new GptSovitsSite.Serializer());

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
