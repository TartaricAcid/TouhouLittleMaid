package com.github.tartaricacid.touhoulittlemaid.ai.service.stt.siliconflow;

import com.github.tartaricacid.touhoulittlemaid.ai.service.SerializableSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTApiType;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTSite;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class STTSiliconflowSite implements STTSite {
    public static final String API_TYPE = STTApiType.SILICONFLOW.getName();

    private final String id;
    private final ResourceLocation icon;

    private boolean enabled;
    private String url;
    private String secretKey;
    private String model;

    public STTSiliconflowSite(String id, ResourceLocation icon, boolean enabled, String url, String secretKey, String model) {
        this.id = id;
        this.icon = icon;
        this.enabled = enabled;
        this.url = url;
        this.secretKey = secretKey;
        this.model = model;
    }

    @Override
    public String id() {
        return this.id;
    }

    @Override
    public boolean enabled() {
        return this.enabled;
    }

    @Override
    public ResourceLocation icon() {
        return this.icon;
    }

    @Override
    public String url() {
        return this.url;
    }

    @Override
    public Map<String, String> headers() {
        return Map.of();
    }

    @Override
    public String getApiType() {
        return API_TYPE;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getModel() {
        return model;
    }

    @Override
    public STTSiliconflowClient client() {
        return new STTSiliconflowClient(STT_HTTP_CLIENT, this);
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

    public void setModel(String model) {
        this.model = model;
    }

    public static class Serializer implements SerializableSite<STTSiliconflowSite> {
        public static final Codec<STTSiliconflowSite> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf(ID).forGetter(STTSiliconflowSite::id),
                ResourceLocation.CODEC.fieldOf(ICON).forGetter(STTSiliconflowSite::icon),
                Codec.BOOL.fieldOf(ENABLED).forGetter(STTSiliconflowSite::enabled),
                Codec.STRING.fieldOf(URL).forGetter(STTSiliconflowSite::url),
                Codec.STRING.fieldOf(SECRET_KEY).forGetter(STTSiliconflowSite::getSecretKey),
                Codec.STRING.fieldOf("model").forGetter(STTSiliconflowSite::getModel)
        ).apply(instance, STTSiliconflowSite::new));

        @Override
        public Codec<STTSiliconflowSite> codec() {
            return CODEC;
        }

        @Override
        public STTSiliconflowSite defaultSite() {
            return new STTSiliconflowSite(
                    API_TYPE,
                    SerializableSite.defaultIcon(API_TYPE),
                    false,
                    "https://api.siliconflow.cn/v1/audio/transcriptions",
                    StringUtils.EMPTY,
                    "FunAudioLLM/SenseVoiceSmall"
            );
        }
    }
}
