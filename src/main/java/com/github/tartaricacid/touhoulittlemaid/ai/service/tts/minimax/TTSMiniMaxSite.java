package com.github.tartaricacid.touhoulittlemaid.ai.service.tts.minimax;

import com.github.tartaricacid.touhoulittlemaid.ai.service.SerializableSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.SupportModelSelect;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSApiType;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.layout.TTSGptSovitsFormLayout;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.layout.TTSMiniMaxFormLayout;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.layout.TTSSiteFormLayout;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public final class TTSMiniMaxSite implements TTSSite, SupportModelSelect {
    public static final String API_TYPE = TTSApiType.MINIMAX.getName();

    private final String id;
    private final ResourceLocation icon;
    private final Map<String, String> headers;
    /**
     * 对应 voice_id
     */
    private final Map<String, String> models;

    private String url;
    private boolean enabled;
    private String secretKey;
    /**
     * MiniMax 的语音合成是有多个模型的，音色又是另一个参数
     */
    private String siteModel;

    public TTSMiniMaxSite(String id, ResourceLocation icon, String url, boolean enabled, String secretKey,
                          String siteModel, Map<String, String> headers, Map<String, String> models) {
        this.id = id;
        this.icon = icon;
        this.url = url;
        this.enabled = enabled;
        this.secretKey = secretKey;
        this.siteModel = siteModel;
        this.headers = headers;
        this.models = models;
    }

    @Override
    public String getApiType() {
        return API_TYPE;
    }

    @Override
    public TTSClient client() {
        return new TTSMiniMaxClient(TTS_HTTP_CLIENT, this);
    }

    @Override
    public TTSSiteFormLayout formLayout() {
        return new TTSMiniMaxFormLayout(this);
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

    public static class Serializer implements SerializableSite<TTSMiniMaxSite> {
        public static final Codec<TTSMiniMaxSite> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf(ID).forGetter(TTSMiniMaxSite::id),
                ResourceLocation.CODEC.fieldOf(ICON).forGetter(TTSMiniMaxSite::icon),
                Codec.STRING.fieldOf(URL).forGetter(TTSMiniMaxSite::url),
                Codec.BOOL.fieldOf(ENABLED).forGetter(TTSMiniMaxSite::enabled),
                Codec.STRING.fieldOf(SECRET_KEY).forGetter(TTSMiniMaxSite::secretKey),
                Codec.STRING.fieldOf(SITE_MODEL).forGetter(TTSMiniMaxSite::siteModel),
                Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf(HEADERS).forGetter(TTSMiniMaxSite::headers),
                Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf(MODELS).forGetter(TTSMiniMaxSite::models)
        ).apply(instance, TTSMiniMaxSite::new));

        @Override
        public TTSMiniMaxSite defaultSite() {
            return new TTSMiniMaxSite(API_TYPE, SerializableSite.defaultIcon(API_TYPE),
                    "https://api.minimaxi.com/v1/t2a_v2", false, StringUtils.EMPTY,
                    "speech-2.8-turbo", Map.of(),
                    Map.of("Chinese (Mandarin)_Mature_Woman", "Mature (CN)",
                            "Chinese (Mandarin)_Warm_Girl", "Warm (CN)",
                            "Chinese (Mandarin)_BashfulGirl", "Bashful (CN)",
                            "English_PlayfulGirl", "Playful (EN)",
                            "English_Soft-spokenGirl", "Soft (EN)",
                            "Japanese_DecisivePrincess", "Decisive (JP)",
                            "Japanese_DependableWoman", "Dependable (JP)",
                            "Japanese_obedient_girl_vv1", "Obedient (JP)"
                    ));
        }

        @Override
        public Codec<TTSMiniMaxSite> codec() {
            return CODEC;
        }
    }
}
