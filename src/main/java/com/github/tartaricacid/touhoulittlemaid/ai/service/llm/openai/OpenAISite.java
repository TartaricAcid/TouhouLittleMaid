package com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai;

import com.github.tartaricacid.touhoulittlemaid.ai.service.SerializableSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.SupportModelSelect;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMApiType;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMSite;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class OpenAISite implements LLMSite, SupportModelSelect {
    public static final String API_TYPE = LLMApiType.OPENAI.getName();

    private final String id;
    private final ResourceLocation icon;
    private final Map<String, String> headers;
    private final Map<String, String> models;

    private String url;
    private boolean enabled;
    private String secretKey;

    public OpenAISite(String id, ResourceLocation icon, String url, boolean enabled,
                      String secretKey, Map<String, String> headers, Map<String, String> models) {
        this.id = id;
        this.icon = icon;
        this.url = url;
        this.enabled = enabled;
        this.secretKey = secretKey;
        this.headers = headers;
        this.models = models;
    }

    public OpenAISite(String id, ResourceLocation icon, String url, boolean enabled,
                      String secretKey, Map<String, String> headers, List<String> models) {
        this(id, icon, url, enabled, secretKey, headers,
                models.stream().collect(Collectors.toMap(Function.identity(), Function.identity())));
    }

    @Override
    public String getApiType() {
        return API_TYPE;
    }

    @Override
    public LLMClient client() {
        return new OpenAIClient(LLM_HTTP_CLIENT, this);
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
        return models;
    }

    public void addModel(String model) {
        this.addModel(model, model);
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

    public static class Serializer implements SerializableSite<OpenAISite> {
        private static final Codec<Map<String, String>> MODELS_CODEC = Codec.list(Codec.STRING).xmap(
                list -> list.stream().collect(Collectors.toMap(Function.identity(), Function.identity())),
                map -> map.keySet().stream().toList());

        public static final Codec<OpenAISite> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf(ID).forGetter(OpenAISite::id),
                ResourceLocation.CODEC.fieldOf(ICON).forGetter(OpenAISite::icon),
                Codec.STRING.fieldOf(URL).forGetter(OpenAISite::url),
                Codec.BOOL.fieldOf(ENABLED).forGetter(OpenAISite::enabled),
                Codec.STRING.fieldOf(SECRET_KEY).forGetter(OpenAISite::secretKey),
                Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf(HEADERS).forGetter(OpenAISite::headers),
                MODELS_CODEC.fieldOf(MODELS).forGetter(OpenAISite::models)
        ).apply(instance, OpenAISite::new));

        @Override
        public OpenAISite defaultSite() {
            return new OpenAISite(API_TYPE, SerializableSite.defaultIcon(API_TYPE),
                    "https://api.openai.com/v1/chat/completions", false,
                    StringUtils.EMPTY, Map.of(),
                    List.of("gpt-4o", "chatgpt-4o-latest", "gpt-4o-mini",
                            "o1", "o1-mini", "o3-mini", "o1-preview"));
        }

        @Override
        public Codec<OpenAISite> codec() {
            return CODEC;
        }
    }
}
