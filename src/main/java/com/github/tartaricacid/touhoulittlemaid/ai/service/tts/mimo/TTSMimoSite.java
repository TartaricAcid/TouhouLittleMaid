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

import java.util.List;
import java.util.Map;

public final class TTSMimoSite implements TTSSite, SupportModelSelect {
    public static final String API_TYPE = TTSApiType.MIMO.getName();
    public static final String DEFAULT_URL = "https://api.xiaomimimo.com/v1/chat/completions";
    public static final String MODEL_TTS = "mimo-v2.5-tts";
    public static final String MODEL_VOICE_DESIGN = "mimo-v2.5-tts-voicedesign";
    public static final String MODEL_VOICE_CLONE = "mimo-v2.5-tts-voiceclone";
    public static final String VOICE_CLONE_MODE_BASE64 = "base64";
    public static final String VOICE_CLONE_MODE_FILE = "file";
    public static final String MIME_AUDIO_WAV = "audio/wav";
    public static final String MIME_AUDIO_MPEG = "audio/mpeg";
    private static final List<String> PRESET_MODELS = List.of(MODEL_TTS, MODEL_VOICE_DESIGN, MODEL_VOICE_CLONE);
    private static final List<String> VOICE_CLONE_MODES = List.of(VOICE_CLONE_MODE_BASE64, VOICE_CLONE_MODE_FILE);
    private static final List<String> VOICE_CLONE_MIME_TYPES = List.of(MIME_AUDIO_WAV, MIME_AUDIO_MPEG);

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
    private String customModel;
    private String voicePrompt;
    private String voiceCloneInputMode;
    private String voiceCloneMimeType;
    private String voiceCloneAudio;

    public TTSMimoSite(String id, ResourceLocation icon, String url, boolean enabled, String secretKey,
                       String siteModel, String customModel, String voicePrompt, String voiceCloneInputMode,
                       String voiceCloneMimeType, String voiceCloneAudio, Map<String, String> headers,
                       Map<String, String> models) {
        this.id = id;
        this.icon = icon;
        this.url = url;
        this.enabled = enabled;
        this.secretKey = secretKey;
        this.siteModel = normalizePresetModel(siteModel);
        this.customModel = normalizeCustomModel(siteModel, customModel);
        this.voicePrompt = voicePrompt;
        this.voiceCloneInputMode = normalizeVoiceCloneInputMode(voiceCloneInputMode);
        this.voiceCloneMimeType = normalizeVoiceCloneMimeType(voiceCloneMimeType);
        this.voiceCloneAudio = voiceCloneAudio;
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

    public String customModel() {
        return customModel;
    }

    public String effectiveSiteModel() {
        return StringUtils.defaultIfBlank(this.customModel, this.siteModel);
    }

    public String voicePrompt() {
        return voicePrompt;
    }

    public String voiceCloneInputMode() {
        return voiceCloneInputMode;
    }

    public String voiceCloneMimeType() {
        return voiceCloneMimeType;
    }

    public String voiceCloneAudio() {
        return voiceCloneAudio;
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
        this.siteModel = normalizePresetModel(siteModel);
    }

    public void setCustomModel(String customModel) {
        this.customModel = customModel;
    }

    public void setVoicePrompt(String voicePrompt) {
        this.voicePrompt = voicePrompt;
    }

    public void setVoiceCloneInputMode(String voiceCloneInputMode) {
        this.voiceCloneInputMode = normalizeVoiceCloneInputMode(voiceCloneInputMode);
    }

    public void setVoiceCloneMimeType(String voiceCloneMimeType) {
        this.voiceCloneMimeType = normalizeVoiceCloneMimeType(voiceCloneMimeType);
    }

    public void setVoiceCloneAudio(String voiceCloneAudio) {
        this.voiceCloneAudio = voiceCloneAudio;
    }

    /**
     * Map internal ASCII voice key to Mimo API voice name.
     * Falls back to the key itself for keys that are already API names (e.g., mimo_default, Mia, Chloe).
     */
    public static String getApiVoiceName(String voiceKey) {
        return VOICE_KEY_TO_API.getOrDefault(voiceKey, voiceKey);
    }

    public static List<String> presetModels() {
        return PRESET_MODELS;
    }

    public static List<String> voiceCloneModes() {
        return VOICE_CLONE_MODES;
    }

    public static List<String> voiceCloneMimeTypes() {
        return VOICE_CLONE_MIME_TYPES;
    }

    public static boolean isVoiceDesignModel(String model) {
        return MODEL_VOICE_DESIGN.equals(model);
    }

    public static boolean isVoiceCloneModel(String model) {
        return MODEL_VOICE_CLONE.equals(model);
    }

    public static String normalizeVoiceCloneInputMode(String inputMode) {
        if (VOICE_CLONE_MODES.contains(inputMode)) {
            return inputMode;
        }
        return VOICE_CLONE_MODE_BASE64;
    }

    public static String normalizeVoiceCloneMimeType(String mimeType) {
        if ("audio/mp3".equalsIgnoreCase(mimeType)) {
            return MIME_AUDIO_MPEG;
        }
        if (VOICE_CLONE_MIME_TYPES.contains(mimeType)) {
            return mimeType;
        }
        return MIME_AUDIO_WAV;
    }

    public static String normalizeVoiceCloneFilePath(String filePath) {
        String value = StringUtils.trimToEmpty(filePath);
        if (value.length() >= 2) {
            char first = value.charAt(0);
            char last = value.charAt(value.length() - 1);
            if ((first == '"' && last == '"') || (first == '\'' && last == '\'')) {
                return value.substring(1, value.length() - 1);
            }
        }
        return value;
    }

    private static String normalizePresetModel(String siteModel) {
        if (PRESET_MODELS.contains(siteModel)) {
            return siteModel;
        }
        return MODEL_TTS;
    }

    private static String normalizeCustomModel(String siteModel, String customModel) {
        if (StringUtils.isNotBlank(customModel)) {
            return customModel;
        }
        if (StringUtils.isNotBlank(siteModel) && !PRESET_MODELS.contains(siteModel)) {
            return siteModel;
        }
        return StringUtils.EMPTY;
    }

    public static class Serializer implements SerializableSite<TTSMimoSite> {
        public static final Codec<TTSMimoSite> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf(ID).forGetter(TTSMimoSite::id),
                ResourceLocation.CODEC.fieldOf(ICON).forGetter(TTSMimoSite::icon),
                Codec.STRING.fieldOf(URL).forGetter(TTSMimoSite::url),
                Codec.BOOL.fieldOf(ENABLED).forGetter(TTSMimoSite::enabled),
                Codec.STRING.fieldOf(SECRET_KEY).forGetter(TTSMimoSite::secretKey),
                Codec.STRING.fieldOf(SITE_MODEL).forGetter(TTSMimoSite::siteModel),
                Codec.STRING.optionalFieldOf(CUSTOM_MODEL, StringUtils.EMPTY).forGetter(TTSMimoSite::customModel),
                Codec.STRING.optionalFieldOf(VOICE_PROMPT, StringUtils.EMPTY).forGetter(TTSMimoSite::voicePrompt),
                Codec.STRING.optionalFieldOf(VOICE_CLONE_INPUT_MODE, VOICE_CLONE_MODE_BASE64).forGetter(TTSMimoSite::voiceCloneInputMode),
                Codec.STRING.optionalFieldOf(VOICE_CLONE_MIME_TYPE, MIME_AUDIO_WAV).forGetter(TTSMimoSite::voiceCloneMimeType),
                Codec.STRING.optionalFieldOf(VOICE_CLONE_AUDIO, StringUtils.EMPTY).forGetter(TTSMimoSite::voiceCloneAudio),
                Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf(HEADERS).forGetter(TTSMimoSite::headers),
                Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf(MODELS).forGetter(TTSMimoSite::models)
        ).apply(instance, TTSMimoSite::new));

        @Override
        public TTSMimoSite defaultSite() {
            return new TTSMimoSite(API_TYPE, SerializableSite.defaultIcon("mimo"), DEFAULT_URL, false,
                    StringUtils.EMPTY, MODEL_TTS, StringUtils.EMPTY, StringUtils.EMPTY,
                    VOICE_CLONE_MODE_BASE64, MIME_AUDIO_WAV, StringUtils.EMPTY, Map.of(),
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
