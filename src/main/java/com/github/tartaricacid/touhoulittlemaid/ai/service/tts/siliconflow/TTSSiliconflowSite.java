package com.github.tartaricacid.touhoulittlemaid.ai.service.tts.siliconflow;

import com.github.tartaricacid.touhoulittlemaid.ai.service.SerializableSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.SupportModelSelect;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSApiType;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class TTSSiliconflowSite implements TTSSite, SupportModelSelect {
    public static final String API_TYPE = TTSApiType.SILICONFLOW.getName();
    public static final String VOICE_MODEL = "FunAudioLLM/CosyVoice2-0.5B";

    private final String id;
    private final ResourceLocation icon;
    private final Map<String, String> headers;
    private final Map<String, String> models;

    private String url;
    private boolean enabled;
    private String secretKey;

    public TTSSiliconflowSite(String id, ResourceLocation icon, String url, boolean enabled,
                              String secretKey, Map<String, String> headers, Map<String, String> models) {
        this.id = id;
        this.icon = icon;
        this.url = url;
        this.enabled = enabled;
        this.secretKey = secretKey;
        this.headers = headers;
        this.models = models;
    }

    @Override
    public String getApiType() {
        return API_TYPE;
    }

    @Override
    public TTSClient client() {
        return new TTSSiliconflowClient(TTS_HTTP_CLIENT, this);
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public ResourceLocation icon() {
        return icon;
    }

    @Override
    public String url() {
        return url;
    }

    public String secretKey() {
        return secretKey;
    }

    @Override
    public Map<String, String> headers() {
        return headers;
    }

    @Override
    public Map<String, String> models() {
        return this.models;
    }

    @Override
    public boolean enabled() {
        return enabled;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public static class Serializer implements SerializableSite<TTSSiliconflowSite> {
        public static final Codec<TTSSiliconflowSite> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf(ID).forGetter(TTSSiliconflowSite::id),
                ResourceLocation.CODEC.fieldOf(ICON).forGetter(TTSSiliconflowSite::icon),
                Codec.STRING.fieldOf(URL).forGetter(TTSSiliconflowSite::url),
                Codec.BOOL.fieldOf(ENABLED).forGetter(TTSSiliconflowSite::enabled),
                Codec.STRING.fieldOf(SECRET_KEY).forGetter(TTSSiliconflowSite::secretKey),
                Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf(HEADERS).forGetter(TTSSiliconflowSite::headers),
                Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf(MODELS).forGetter(TTSSiliconflowSite::models)
        ).apply(instance, TTSSiliconflowSite::new));

        @Override
        public TTSSiliconflowSite defaultSite() {
            return new TTSSiliconflowSite(API_TYPE, SerializableSite.defaultIcon(API_TYPE),
                    "https://api.siliconflow.cn/v1/audio/speech", false, StringUtils.EMPTY, Map.of(),
                    Map.of(VOICE_MODEL + ":anna", "anna",
                            VOICE_MODEL + ":bella", "bella",
                            VOICE_MODEL + ":claire", "claire",
                            VOICE_MODEL + ":diana", "diana"));
        }

        @Override
        public Codec<TTSSiliconflowSite> codec() {
            return CODEC;
        }
    }
}
