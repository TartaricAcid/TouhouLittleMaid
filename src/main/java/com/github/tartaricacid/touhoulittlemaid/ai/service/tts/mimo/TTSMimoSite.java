package com.github.tartaricacid.touhoulittlemaid.ai.service.tts.mimo;

import com.github.tartaricacid.touhoulittlemaid.ai.service.SerializableSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.SupportModelSelect;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSApiType;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.layout.TTSMimoFormLayout;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.layout.TTSSiteFormLayout;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public final class TTSMimoSite implements TTSSite, SupportModelSelect {
    public static final String API_TYPE = TTSApiType.MIMO.getName();
    public static final String DEFAULT_URL = "https://api.xiaomimimo.com/v1/chat/completions";
    public static final String MODEL_TTS = "mimo-v2.5-tts";
    public static final String MODEL_VOICE_DESIGN = "mimo-v2.5-tts-voicedesign";

    // internal ASCII key -> Mimo API voice name (some are Chinese)
    private static final Map<String, String> VOICE_KEY_TO_API = Map.of(
            "bingtang", "冰糖",
            "moli", "茉莉",
            "soda", "苏打",
            "baihua", "白桦"
    );

    private final String id;
    private final ResourceLocation icon;
    private final Map<String, String> headers;
    private final Map<String, String> models;

    private String url;
    private boolean enabled;
    private String secretKey;
    private String siteModel;
    private String voicePrompt;

    public TTSMimoSite(String id, ResourceLocation icon, String url, boolean enabled, String secretKey,
                       String siteModel, String voicePrompt, Map<String, String> headers, Map<String, String> models) {
        this.id = id;
        this.icon = icon;
        this.url = url;
        this.enabled = enabled;
        this.secretKey = secretKey;
        this.siteModel = siteModel;
        this.voicePrompt = voicePrompt;
        this.headers = headers;
        this.models = models;
    }

    @Override
    public String getApiType() {
        return API_TYPE;
    }

    @Override
    public TTSClient client() {
        return new TTSMimoClient(TTS_HTTP_CLIENT, this);
    }

    @Override
    public TTSSiteFormLayout formLayout() {
        return new TTSMimoFormLayout(this);
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

    public String siteModel() {
        return siteModel;
    }

    public String voicePrompt() {
        return voicePrompt;
    }

    @Override
    public Map<String, String> headers() {
        return headers;
    }

    @Override
    public Map<String, String> models() {
        return models;
    }

    @Override
    public boolean enabled() {
        return enabled;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setSiteModel(String siteModel) {
        this.siteModel = siteModel;
    }

    public void setVoicePrompt(String voicePrompt) {
        this.voicePrompt = voicePrompt;
    }

    /**
     * Map internal ASCII voice key to Mimo API voice name.
     * Falls back to the key itself for keys that are already API names (e.g., mimo_default, Mia, Chloe).
     */
    public static String getApiVoiceName(String voiceKey) {
        return VOICE_KEY_TO_API.getOrDefault(voiceKey, voiceKey);
    }

    public static class Serializer implements SerializableSite<TTSMimoSite> {
        public static final Codec<TTSMimoSite> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf(ID).forGetter(TTSMimoSite::id),
                ResourceLocation.CODEC.fieldOf(ICON).forGetter(TTSMimoSite::icon),
                Codec.STRING.fieldOf(URL).forGetter(TTSMimoSite::url),
                Codec.BOOL.fieldOf(ENABLED).forGetter(TTSMimoSite::enabled),
                Codec.STRING.fieldOf(SECRET_KEY).forGetter(TTSMimoSite::secretKey),
                Codec.STRING.fieldOf(SITE_MODEL).forGetter(TTSMimoSite::siteModel),
                Codec.STRING.optionalFieldOf(VOICE_PROMPT, StringUtils.EMPTY).forGetter(TTSMimoSite::voicePrompt),
                Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf(HEADERS).forGetter(TTSMimoSite::headers),
                Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf(MODELS).forGetter(TTSMimoSite::models)
        ).apply(instance, TTSMimoSite::new));

        @Override
        public TTSMimoSite defaultSite() {
            return new TTSMimoSite(API_TYPE, SerializableSite.defaultIcon("mimo"), DEFAULT_URL, false,
                    StringUtils.EMPTY, MODEL_TTS, StringUtils.EMPTY, Map.of(),
                    defaultVoices());
        }

        @Override
        public Codec<TTSMimoSite> codec() {
            return CODEC;
        }

        private static Map<String, String> defaultVoices() {
            Map<String, String> voices = Maps.newLinkedHashMap();
            voices.put("mimo_default", "MiMo Default");
            voices.put("bingtang", "Bingtang (CN)");
            voices.put("moli", "Moli (CN)");
            voices.put("soda", "Soda (CN)");
            voices.put("baihua", "Baihua (CN)");
            voices.put("Mia", "Mia (EN)");
            voices.put("Chloe", "Chloe (EN)");
            voices.put("Milo", "Milo (EN)");
            voices.put("Dean", "Dean (EN)");
            return voices;
        }
    }
}
