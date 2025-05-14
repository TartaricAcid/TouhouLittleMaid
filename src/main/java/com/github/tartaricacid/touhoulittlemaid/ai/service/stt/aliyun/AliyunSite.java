package com.github.tartaricacid.touhoulittlemaid.ai.service.stt.aliyun;

import com.github.tartaricacid.touhoulittlemaid.ai.service.SerializableSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTApiType;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTSite;
import com.github.tartaricacid.touhoulittlemaid.util.UrlTool;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class AliyunSite implements STTSite {
    public static final String API_TYPE = STTApiType.ALIYUN.getName();

    private final String id;
    private final ResourceLocation icon;

    private boolean enabled;
    private String url;
    private String secretKey;
    private String appKey;
    private String vocabularyId;
    private String customizationId;
    private boolean enablePunctuationPrediction;
    private boolean enableInverseTextNormalization;
    private boolean enableVoiceDetection;
    private boolean disfluency;

    public AliyunSite(String id, ResourceLocation icon, boolean enabled, String url, String secretKey, String appKey,
                      String vocabularyId, String customizationId, boolean enablePunctuationPrediction,
                      boolean enableInverseTextNormalization, boolean enableVoiceDetection, boolean disfluency) {
        this.id = id;
        this.icon = icon;
        this.enabled = enabled;
        this.url = url;
        this.secretKey = secretKey;
        this.appKey = appKey;
        this.vocabularyId = vocabularyId;
        this.customizationId = customizationId;
        this.enablePunctuationPrediction = enablePunctuationPrediction;
        this.enableInverseTextNormalization = enableInverseTextNormalization;
        this.enableVoiceDetection = enableVoiceDetection;
        this.disfluency = disfluency;
    }

    @Override
    public String id() {
        return this.id;
    }

    @Override
    public ResourceLocation icon() {
        return this.icon;
    }

    @Override
    public boolean enabled() {
        return this.enabled;
    }

    @Override
    public String getApiType() {
        return API_TYPE;
    }

    @Override
    public STTClient client() {
        return new STTAliyunClient(STT_HTTP_CLIENT, this);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public void setVocabularyId(String vocabularyId) {
        this.vocabularyId = vocabularyId;
    }

    public void setCustomizationId(String customizationId) {
        this.customizationId = customizationId;
    }

    public void setEnablePunctuationPrediction(boolean enablePunctuationPrediction) {
        this.enablePunctuationPrediction = enablePunctuationPrediction;
    }

    public void setEnableInverseTextNormalization(boolean enableInverseTextNormalization) {
        this.enableInverseTextNormalization = enableInverseTextNormalization;
    }

    public void setEnableVoiceDetection(boolean enableVoiceDetection) {
        this.enableVoiceDetection = enableVoiceDetection;
    }

    public void setDisfluency(boolean disfluency) {
        this.disfluency = disfluency;
    }

    @Override
    public String url() {
        Map<String, String> params = Maps.newHashMap();

        params.put("appkey", getAppKey());
        params.put("format", "pcm");
        params.put("sample_rate", "16000");
        params.put("enable_punctuation_prediction", String.valueOf(isEnablePunctuationPrediction()));
        params.put("enable_inverse_text_normalization", String.valueOf(isEnableInverseTextNormalization()));
        params.put("enable_voice_detection", String.valueOf(isEnableVoiceDetection()));
        params.put("disfluency", String.valueOf(isDisfluency()));
        if (StringUtils.isNotBlank(getVocabularyId())) {
            params.put("vocabulary_id", getVocabularyId());
        }
        if (StringUtils.isNotBlank(getCustomizationId())) {
            params.put("customization_id", getCustomizationId());
        }

        return UrlTool.buildQueryString(this.getBaseUrl(), params);
    }

    private String getBaseUrl() {
        return url;
    }

    @Override
    public Map<String, String> headers() {
        return Map.of();
    }

    public String getSecretKey() {
        return secretKey;
    }

    private String getAppKey() {
        return appKey;
    }

    private String getVocabularyId() {
        return vocabularyId;
    }

    private String getCustomizationId() {
        return customizationId;
    }

    private boolean isEnablePunctuationPrediction() {
        return enablePunctuationPrediction;
    }

    private boolean isEnableInverseTextNormalization() {
        return enableInverseTextNormalization;
    }

    private boolean isEnableVoiceDetection() {
        return enableVoiceDetection;
    }

    private boolean isDisfluency() {
        return disfluency;
    }

    public static class Serializer implements SerializableSite<AliyunSite> {
        public static final Codec<AliyunSite> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf(ID).forGetter(AliyunSite::id),
                ResourceLocation.CODEC.fieldOf(ICON).forGetter(AliyunSite::icon),
                Codec.BOOL.fieldOf(ENABLED).forGetter(AliyunSite::enabled),
                Codec.STRING.fieldOf(URL).forGetter(AliyunSite::getBaseUrl),
                Codec.STRING.fieldOf(SECRET_KEY).forGetter(AliyunSite::getSecretKey),
                Codec.STRING.fieldOf("app_key").forGetter(AliyunSite::getAppKey),
                Codec.STRING.optionalFieldOf("vocabulary_id", StringUtils.EMPTY).forGetter(AliyunSite::getVocabularyId),
                Codec.STRING.optionalFieldOf("customization_id", StringUtils.EMPTY).forGetter(AliyunSite::getCustomizationId),
                Codec.BOOL.optionalFieldOf("enable_punctuation_prediction", false).forGetter(AliyunSite::isEnablePunctuationPrediction),
                Codec.BOOL.optionalFieldOf("enable_inverse_text_normalization", false).forGetter(AliyunSite::isEnableInverseTextNormalization),
                Codec.BOOL.optionalFieldOf("enable_voice_detection", false).forGetter(AliyunSite::isEnableVoiceDetection),
                Codec.BOOL.optionalFieldOf("disfluency", false).forGetter(AliyunSite::isDisfluency)
        ).apply(instance, AliyunSite::new));

        @Override
        public AliyunSite defaultSite() {
            return new AliyunSite(API_TYPE, SerializableSite.defaultIcon(API_TYPE), false,
                    "https://nls-gateway-cn-shanghai.aliyuncs.com/stream/v1/asr",
                    StringUtils.EMPTY, StringUtils.EMPTY,
                    StringUtils.EMPTY, StringUtils.EMPTY,
                    false, false, false, false);
        }

        @Override
        public Codec<AliyunSite> codec() {
            return CODEC;
        }
    }
}
