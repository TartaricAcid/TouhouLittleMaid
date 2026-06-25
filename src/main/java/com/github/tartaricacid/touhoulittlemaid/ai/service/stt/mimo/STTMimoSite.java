package com.github.tartaricacid.touhoulittlemaid.ai.service.stt.mimo;

import com.github.tartaricacid.touhoulittlemaid.ai.service.SerializableSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTApiType;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTSite;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.layout.STTMimoFormLayout;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.layout.STTSiteFormLayout;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class STTMimoSite implements STTSite {
    public static final String API_TYPE = STTApiType.MIMO.getName();
    public static final String DEFAULT_URL = "https://api.xiaomimimo.com/v1/chat/completions";
    public static final String DEFAULT_MODEL = "mimo-v2.5-asr";
    public static final String DEFAULT_LANGUAGE = "auto";
    private static final String MODEL = "model";
    private static final String LANGUAGE = "language";

    private final String id;
    private final ResourceLocation icon;

    private boolean enabled;
    private String url;
    private String secretKey;
    private String model;
    private String language;

    public STTMimoSite(String id, ResourceLocation icon, boolean enabled, String url, String secretKey,
                       String model, String language) {
        this.id = id;
        this.icon = icon;
        this.enabled = enabled;
        this.url = url;
        this.secretKey = secretKey;
        this.model = StringUtils.defaultIfBlank(model, DEFAULT_MODEL);
        this.language = StringUtils.defaultIfBlank(language, DEFAULT_LANGUAGE);
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
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    @Override
    public STTClient client() {
        return new STTMimoClient(STT_HTTP_CLIENT, this);
    }

    @Override
    public STTSiteFormLayout formLayout() {
        return new STTMimoFormLayout(this);
    }

    public String secretKey() {
        return this.secretKey;
    }

    public String model() {
        return this.model;
    }

    public String language() {
        return this.language;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setModel(String model) {
        this.model = StringUtils.defaultIfBlank(model, DEFAULT_MODEL);
    }

    public void setLanguage(String language) {
        this.language = StringUtils.defaultIfBlank(language, DEFAULT_LANGUAGE);
    }

    public static class Serializer implements SerializableSite<STTMimoSite> {
        public static final Codec<STTMimoSite> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf(ID).forGetter(STTMimoSite::id),
                ResourceLocation.CODEC.fieldOf(ICON).forGetter(STTMimoSite::icon),
                Codec.BOOL.fieldOf(ENABLED).forGetter(STTMimoSite::enabled),
                Codec.STRING.fieldOf(URL).forGetter(STTMimoSite::url),
                Codec.STRING.fieldOf(SECRET_KEY).forGetter(STTMimoSite::secretKey),
                Codec.STRING.optionalFieldOf(MODEL, DEFAULT_MODEL).forGetter(STTMimoSite::model),
                Codec.STRING.optionalFieldOf(LANGUAGE, DEFAULT_LANGUAGE).forGetter(STTMimoSite::language)
        ).apply(instance, STTMimoSite::new));

        @Override
        public Codec<STTMimoSite> codec() {
            return CODEC;
        }

        @Override
        public STTMimoSite defaultSite() {
            return new STTMimoSite(API_TYPE, SerializableSite.defaultIcon("mimo"), false,
                    DEFAULT_URL, StringUtils.EMPTY, DEFAULT_MODEL, DEFAULT_LANGUAGE);
        }
    }
}
