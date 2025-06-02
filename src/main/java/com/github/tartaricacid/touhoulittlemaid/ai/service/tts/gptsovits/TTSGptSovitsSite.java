package com.github.tartaricacid.touhoulittlemaid.ai.service.tts.gptsovits;

import com.github.tartaricacid.touhoulittlemaid.ai.service.SerializableSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSApiType;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

public final class TTSGptSovitsSite implements TTSSite {
    public static final String API_TYPE = TTSApiType.GPT_SOVITS.getName();

    private final String id;
    private final ResourceLocation icon;
    private final List<String> auxRefAudioPaths;
    private final Map<String, String> headers;

    private String url;
    private boolean enabled;
    private String secretKey;
    private String refAudioPath;
    private String promptText;
    private String promptLang;
    private String textSplitMethod;

    public TTSGptSovitsSite(String id, ResourceLocation icon, String url, boolean enabled,
                            String secretKey, String refAudioPath,
                            String promptText, String promptLang,
                            String textSplitMethod, List<String> auxRefAudioPaths,
                            Map<String, String> headers) {
        this.id = id;
        this.icon = icon;
        this.url = url;
        this.enabled = enabled;
        this.secretKey = secretKey;
        this.refAudioPath = refAudioPath;
        this.promptText = promptText;
        this.promptLang = promptLang;
        this.textSplitMethod = textSplitMethod;
        this.auxRefAudioPaths = auxRefAudioPaths;
        this.headers = headers;
    }

    @Override
    public String getApiType() {
        return API_TYPE;
    }

    @Override
    public TTSClient client() {
        return new TTSGptSovitsClient(TTS_HTTP_CLIENT, this);
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

    public String refAudioPath() {
        return refAudioPath;
    }

    public String promptText() {
        return promptText;
    }

    public String promptLang() {
        return promptLang;
    }

    public String textSplitMethod() {
        return textSplitMethod;
    }

    public List<String> auxRefAudioPaths() {
        return auxRefAudioPaths;
    }

    @Override
    public Map<String, String> headers() {
        return headers;
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

    public void setRefAudioPath(String refAudioPath) {
        this.refAudioPath = refAudioPath;
    }

    public void setPromptText(String promptText) {
        this.promptText = promptText;
    }

    public void setPromptLang(String promptLang) {
        this.promptLang = promptLang;
    }

    public void setTextSplitMethod(String textSplitMethod) {
        this.textSplitMethod = textSplitMethod;
    }

    public static class Serializer implements SerializableSite<TTSGptSovitsSite> {
        public static final Codec<TTSGptSovitsSite> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf(ID).forGetter(TTSGptSovitsSite::id),
                ResourceLocation.CODEC.fieldOf(ICON).forGetter(TTSGptSovitsSite::icon),
                Codec.STRING.fieldOf(URL).forGetter(TTSGptSovitsSite::url),
                Codec.BOOL.fieldOf(ENABLED).forGetter(TTSGptSovitsSite::enabled),
                Codec.STRING.fieldOf(SECRET_KEY).forGetter(TTSGptSovitsSite::secretKey),
                Codec.STRING.fieldOf("ref_audio_path").forGetter(TTSGptSovitsSite::refAudioPath),
                Codec.STRING.fieldOf("prompt_text").forGetter(TTSGptSovitsSite::promptText),
                Codec.STRING.fieldOf("prompt_lang").forGetter(TTSGptSovitsSite::promptLang),
                Codec.STRING.fieldOf("text_split_method").forGetter(TTSGptSovitsSite::textSplitMethod),
                Codec.list(Codec.STRING).fieldOf("aux_ref_audio_paths").forGetter(TTSGptSovitsSite::auxRefAudioPaths),
                Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf(HEADERS).forGetter(TTSGptSovitsSite::headers)
        ).apply(instance, TTSGptSovitsSite::new));

        @Override
        public TTSGptSovitsSite defaultSite() {
            return new TTSGptSovitsSite(API_TYPE, SerializableSite.defaultIcon(API_TYPE),
                    "http://127.0.0.1:9880/tts", false,
                    StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                    "zh", "cut1", List.of(), Map.of()
            );
        }

        @Override
        public Codec<TTSGptSovitsSite> codec() {
            return CODEC;
        }
    }
}
