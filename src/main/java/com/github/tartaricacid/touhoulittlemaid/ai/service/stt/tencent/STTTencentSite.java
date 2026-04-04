package com.github.tartaricacid.touhoulittlemaid.ai.service.stt.tencent;

import com.github.tartaricacid.touhoulittlemaid.ai.service.SerializableSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTApiType;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTSite;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.layout.STTSiteFormLayout;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.layout.STTTencentFormLayout;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class STTTencentSite implements STTSite {
    public static final String API_TYPE = STTApiType.TENCENT.getName();

    private final String id;
    private final ResourceLocation icon;

    private boolean enabled;
    private String url;

    private String secretId;
    private String secretKey;

    private String engSerViceType;
    private String hotWord;

    public STTTencentSite(String id, ResourceLocation icon, boolean enabled, String url,
                          String secretId, String secretKey, String engSerViceType, String hotWord) {
        this.id = id;
        this.icon = icon;
        this.enabled = enabled;
        this.url = url;
        this.secretId = secretId;
        this.secretKey = secretKey;
        this.engSerViceType = engSerViceType;
        this.hotWord = hotWord;
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
        return new STTTencentClient(STT_HTTP_CLIENT, this);
    }

    @Override
    public STTSiteFormLayout formLayout() {
        return new STTTencentFormLayout(this);
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public Map<String, String> headers() {
        return Map.of();
    }

    @Override
    public String url() {
        return url;
    }

    public void setEngSerViceType(String engSerViceType) {
        this.engSerViceType = engSerViceType;
    }

    public String getHotWord() {
        return hotWord;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getSecretId() {
        return secretId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getEngSerViceType() {
        return engSerViceType;
    }

    public void setHotWord(String hotWord) {
        this.hotWord = hotWord;
    }

    public static class Serializer implements SerializableSite<STTTencentSite> {
        public static final Codec<STTTencentSite> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf(ID).forGetter(STTTencentSite::id),
                ResourceLocation.CODEC.fieldOf(ICON).forGetter(STTTencentSite::icon),
                Codec.BOOL.fieldOf(ENABLED).forGetter(STTTencentSite::enabled),
                Codec.STRING.fieldOf(URL).forGetter(STTTencentSite::url),
                Codec.STRING.fieldOf(SECRET_ID).forGetter(STTTencentSite::getSecretId),
                Codec.STRING.fieldOf(SECRET_KEY).forGetter(STTTencentSite::getSecretKey),
                Codec.STRING.fieldOf(ENG_SER_VICE_TYPE).forGetter(STTTencentSite::getEngSerViceType),
                Codec.STRING.fieldOf(HOT_WORD).forGetter(STTTencentSite::getHotWord)
        ).apply(instance, STTTencentSite::new));

        @Override
        public STTTencentSite defaultSite() {
            return new STTTencentSite(API_TYPE, SerializableSite.defaultIcon(API_TYPE),
                    false, "https://asr.tencentcloudapi.com",
                    StringUtils.EMPTY, StringUtils.EMPTY, "16k_zh", StringUtils.EMPTY);
        }

        @Override
        public Codec<STTTencentSite> codec() {
            return CODEC;
        }
    }
}
