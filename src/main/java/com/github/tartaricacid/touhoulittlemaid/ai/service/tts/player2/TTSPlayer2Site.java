package com.github.tartaricacid.touhoulittlemaid.ai.service.tts.player2;

import com.github.tartaricacid.touhoulittlemaid.ai.service.SerializableSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.SupportModelSelect;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSApiType;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class TTSPlayer2Site implements TTSSite, SupportModelSelect {
    public static final String API_TYPE = TTSApiType.PLAYER2.getName();

    private final String id;
    private final ResourceLocation icon;
    private final String url;
    private final Map<String, String> models;
    private final Map<String, String> headers;
    private boolean enabled;

    public TTSPlayer2Site(String id, ResourceLocation icon, String url, boolean enabled,
                          Map<String, String> models, Map<String, String> headers) {
        this.id = id;
        this.icon = icon;
        this.url = url;
        this.enabled = enabled;
        this.models = models;
        this.headers = headers;
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
        return this.headers;
    }

    @Override
    public String getApiType() {
        return API_TYPE;
    }

    @Override
    public TTSClient client() {
        return new TTSPlayer2Client(TTS_HTTP_CLIENT, this);
    }

    @Override
    public Map<String, String> models() {
        return this.models;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public static class Serializer implements SerializableSite<TTSPlayer2Site> {
        public static final Codec<TTSPlayer2Site> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf(ID).forGetter(TTSPlayer2Site::id),
                ResourceLocation.CODEC.fieldOf(ICON).forGetter(TTSPlayer2Site::icon),
                Codec.STRING.fieldOf(URL).forGetter(TTSPlayer2Site::url),
                Codec.BOOL.fieldOf(ENABLED).forGetter(TTSPlayer2Site::enabled),
                Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf(MODELS).forGetter(TTSPlayer2Site::models),
                Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf(HEADERS).forGetter(TTSPlayer2Site::headers)
        ).apply(instance, TTSPlayer2Site::new));

        @Override
        public TTSPlayer2Site defaultSite() {
            return new TTSPlayer2Site(API_TYPE, SerializableSite.defaultIcon(API_TYPE),
                    "http://127.0.0.1:4315/v1/tts/speak", true,
                    Map.of(
                            "01955d76-ed5b-7426-8748-4b0e5aea1974", "Olivia (EN)",
                            "01955d76-ed5b-73e0-a88d-cbeb3c5b499d", "Sophia (EN)",
                            "01955d76-ed5b-75ad-afe3-ac5eb3d0a16e", "Hana (JP)",
                            "01955d76-ed5b-757a-9bdb-94fa0a2b7893", "Sakura (JP)",
                            "01955d76-ed5b-75c8-8386-b83ff9c45856", "Mei (CN)",
                            "01955d76-ed5b-75d4-8338-3d7108137cd1", "Ling (CN)",
                            "01955d76-ed5b-75eb-b509-e7bf29b3b530", "Qiuyue (CN)",
                            "01955d76-ed5b-762a-9a2a-0fec3b7ace8b", "Carmen (ES)",
                            "01955d76-ed5b-7668-877b-2fa240c1d5ee", "Sophie (FR)",
                            "01955d76-ed5b-76c6-8b9e-b713d3f0b866", "Isabela (PT-BR)"
                    ),
                    Map.of("player2-game-key", "TouhouLittleMaid"));
        }

        @Override
        public Codec<TTSPlayer2Site> codec() {
            return CODEC;
        }
    }
}
