package com.github.tartaricacid.touhoulittlemaid.ai.service.tts.system;

import com.github.tartaricacid.touhoulittlemaid.ai.service.SerializableSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSApiType;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Map;

public final class TTSSystemSite implements TTSSite {
    public static final String API_TYPE = TTSApiType.SYSTEM.getName();

    private final String id;
    private final ResourceLocation icon;
    private boolean enabled;

    public TTSSystemSite(String id, ResourceLocation icon, boolean enabled) {
        this.id = id;
        this.icon = icon;
        this.enabled = enabled;
    }

    @Override
    public TTSClient client() {
        return new TTSSystemClient();
    }

    @Override
    public String url() {
        return StringUtils.EMPTY;
    }

    @Override
    public Map<String, String> headers() {
        return Collections.emptyMap();
    }

    @Override
    public String getApiType() {
        return API_TYPE;
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
    public boolean enabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public static class Serializer implements SerializableSite<TTSSystemSite> {
        public static final Codec<TTSSystemSite> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf(ID).forGetter(TTSSystemSite::id),
                ResourceLocation.CODEC.fieldOf(ICON).forGetter(TTSSystemSite::icon),
                Codec.BOOL.fieldOf(ENABLED).forGetter(TTSSystemSite::enabled)
        ).apply(instance, TTSSystemSite::new));

        @Override
        public TTSSystemSite defaultSite() {
            return new TTSSystemSite(API_TYPE, SerializableSite.defaultIcon(API_TYPE), true);
        }

        @Override
        public Codec<TTSSystemSite> codec() {
            return CODEC;
        }
    }
}
