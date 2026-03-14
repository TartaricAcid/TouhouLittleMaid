package com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai;

import com.github.tartaricacid.touhoulittlemaid.ai.service.SerializableSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMApiType;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMClient;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LLMDoubaoSite extends LLMOpenAISite {
    public static final String API_TYPE = LLMApiType.DOUBAO.getName();

    public LLMDoubaoSite(String id, ResourceLocation icon, String url, boolean enabled,
                         String secretKey, Map<String, String> headers, Map<String, String> models) {
        super(id, icon, url, enabled, secretKey, headers, models);
    }

    public LLMDoubaoSite(String id, ResourceLocation icon, String url, boolean enabled,
                         String secretKey, Map<String, String> headers, List<String> models) {
        super(id, icon, url, enabled, secretKey, headers, models);
    }

    @Override
    public LLMClient client() {
        return new LLMDoubaoClient(LLM_HTTP_CLIENT, this);
    }

    @Override
    public String getApiType() {
        return API_TYPE;
    }

    public static class Serializer implements SerializableSite<LLMDoubaoSite> {
        protected static final Codec<Map<String, String>> MODELS_CODEC = Codec.list(Codec.STRING).xmap(
                list -> list.stream().collect(Collectors.toMap(Function.identity(), Function.identity())),
                map -> map.keySet().stream().toList());

        public static final Codec<LLMDoubaoSite> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf(ID).forGetter(LLMDoubaoSite::id),
                ResourceLocation.CODEC.fieldOf(ICON).forGetter(LLMDoubaoSite::icon),
                Codec.STRING.fieldOf(URL).forGetter(LLMDoubaoSite::url),
                Codec.BOOL.fieldOf(ENABLED).forGetter(LLMDoubaoSite::enabled),
                Codec.STRING.fieldOf(SECRET_KEY).forGetter(LLMDoubaoSite::secretKey),
                Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf(HEADERS).forGetter(LLMDoubaoSite::headers),
                MODELS_CODEC.fieldOf(MODELS).forGetter(LLMDoubaoSite::models)
        ).apply(instance, LLMDoubaoSite::new));

        @Override
        public LLMDoubaoSite defaultSite() {
            return new LLMDoubaoSite(API_TYPE, SerializableSite.defaultIcon(API_TYPE),
                    "https://ark.cn-beijing.volces.com/api/v3/chat/completions", false,
                    StringUtils.EMPTY, Map.of(), List.of(
                    "doubao-seed-2-0-pro-260215",
                    "doubao-seed-2-0-lite-260215",
                    "doubao-seed-2-0-mini-260215"
            ));
        }

        @Override
        public Codec<LLMDoubaoSite> codec() {
            return CODEC;
        }
    }
}
