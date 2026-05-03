package com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai;

import com.github.tartaricacid.touhoulittlemaid.ai.service.SerializableSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.SupportModelSelect;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMApiType;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMSite;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LLMOpenAISite implements LLMSite, SupportModelSelect {
    public static final String API_TYPE = LLMApiType.OPENAI.getName();

    protected final String id;
    protected final ResourceLocation icon;
    protected final Map<String, String> headers;
    protected final Map<String, String> models;
    protected final Map<String, ModelEntry> modelEntries;

    protected String url;
    protected boolean enabled;
    protected String secretKey;
    protected boolean hasThinkingField;

    public LLMOpenAISite(String id, ResourceLocation icon, String url, boolean enabled, String secretKey, boolean hasThinkingField,
                         Map<String, String> headers, Map<String, ModelEntry> modelEntries) {
        this.id = id;
        this.icon = icon;
        this.url = url;
        this.enabled = enabled;
        this.secretKey = secretKey;
        this.hasThinkingField = hasThinkingField;
        this.headers = headers;
        this.models = modelEntries.keySet().stream().collect(Collectors.toMap(Function.identity(), k -> modelEntries.get(k).name()));
        this.modelEntries = modelEntries;
    }

    public LLMOpenAISite(String id, ResourceLocation icon, String url, boolean enabled, String secretKey, boolean hasThinkingField,
                         Map<String, String> headers, List<ModelEntry> modelEntries) {
        this(id, icon, url, enabled, secretKey, hasThinkingField, headers,
                modelEntries.stream().collect(Collectors.toMap(ModelEntry::name, Function.identity())));
    }

    public LLMOpenAISite(String id, ResourceLocation icon, String url, boolean enabled,
                         String secretKey, Map<String, String> headers, List<ModelEntry> modelEntries) {
        this(id, icon, url, enabled, secretKey, false, headers, modelEntries);
    }

    @Override
    public String getApiType() {
        return API_TYPE;
    }

    @Override
    public LLMClient client() {
        return new LLMOpenAIClient(LLM_HTTP_CLIENT, this);
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

    public boolean hasThinkingField() {
        return hasThinkingField;
    }

    public void setHasThinkingField(boolean hasThinkingField) {
        this.hasThinkingField = hasThinkingField;
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
    public void addModel(String id, String name) {
        models.put(id, name);
        modelEntries.put(id, new ModelEntry(name));
    }

    @Override
    public void removeModel(String id) {
        models.remove(id);
        modelEntries.remove(id);
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

    public Map<String, ModelEntry> modelEntries() {
        return modelEntries;
    }

    public boolean isReasoningModel(String id) {
        if (!modelEntries.containsKey(id)) {
            return false;
        }
        return this.modelEntries.get(id).isReasoning();
    }

    /**
     * 因为部分 openai 模型是推理模型，采用了和旧版不兼容的接口，故需要复合类来表面其属性
     *
     * @param name        模型名
     * @param isReasoning 是否为推理模型，默认为 false
     */
    public record ModelEntry(String name, boolean isReasoning) {
        public ModelEntry(String name) {
            this(name, false);
        }
    }

    public static class Serializer implements SerializableSite<LLMOpenAISite> {
        // 复合对象: {name: string, reasoning: boolean}
        protected static final Codec<ModelEntry> MODEL_ENTRY_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("name").forGetter(ModelEntry::name),
                Codec.BOOL.fieldOf("reasoning").forGetter(ModelEntry::isReasoning)
        ).apply(instance, ModelEntry::new));

        // 单个模型：可以是 String 或 ModelEntry 对象
        protected static final Codec<ModelEntry> SINGLE_MODEL_CODEC = Codec.either(Codec.STRING, MODEL_ENTRY_CODEC).xmap(
                either -> either.map(ModelEntry::new, Function.identity()),
                entry -> entry.isReasoning() ? Either.right(entry) : Either.left(entry.name())
        );

        // 列表转 Map<String, ModelEntry> 的 Codec
        protected static final Codec<Map<String, ModelEntry>> MODELS_CODEC = Codec.list(SINGLE_MODEL_CODEC).xmap(
                list -> list.stream().collect(Collectors.toMap(ModelEntry::name, Function.identity())),
                map -> Lists.newArrayList(map.values())
        );

        public static final Codec<LLMOpenAISite> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf(ID).forGetter(LLMOpenAISite::id),
                ResourceLocation.CODEC.fieldOf(ICON).forGetter(LLMOpenAISite::icon),
                Codec.STRING.fieldOf(URL).forGetter(LLMOpenAISite::url),
                Codec.BOOL.fieldOf(ENABLED).forGetter(LLMOpenAISite::enabled),
                Codec.STRING.fieldOf(SECRET_KEY).forGetter(LLMOpenAISite::secretKey),
                Codec.BOOL.optionalFieldOf(HAS_THINKING_FIELD, false).forGetter(LLMOpenAISite::hasThinkingField),
                Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf(HEADERS).forGetter(LLMOpenAISite::headers),
                MODELS_CODEC.fieldOf(MODELS).forGetter(LLMOpenAISite::modelEntries)
        ).apply(instance, LLMOpenAISite::new));

        @Override
        public LLMOpenAISite defaultSite() {
            return new LLMOpenAISite(API_TYPE, SerializableSite.defaultIcon(API_TYPE),
                    "https://api.openai.com/v1/chat/completions", false,
                    StringUtils.EMPTY, Map.of(),
                    List.of(
                            new ModelEntry("gpt-4o"),
                            new ModelEntry("gpt-4.1"),
                            new ModelEntry("gpt-5-mini", true),
                            new ModelEntry("gpt-5.1", true),
                            new ModelEntry("gpt-5.2", true),
                            new ModelEntry("gpt-5.4", true)
                    ));
        }

        @Override
        public Codec<LLMOpenAISite> codec() {
            return CODEC;
        }
    }
}
