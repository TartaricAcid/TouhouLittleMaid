package com.github.tartaricacid.touhoulittlemaid.ai.service.stt.player2;

import com.github.tartaricacid.touhoulittlemaid.ai.service.SerializableSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTApiType;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTSite;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class Player2Site implements STTSite {
    public static final String API_TYPE = STTApiType.PLAYER2.getName();

    private final String id;
    private final ResourceLocation icon;
    private final String url;
    private final Map<String, String> headers;

    private boolean enabled;

    public Player2Site(String id, ResourceLocation icon, String url, boolean enabled, Map<String, String> headers) {
        this.id = id;
        this.icon = icon;
        this.url = url;
        this.enabled = enabled;
        this.headers = headers;
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
    public String getApiType() {
        return API_TYPE;
    }

    @Override
    public Map<String, String> headers() {
        return headers;
    }

    @Override
    public STTClient client() {
        return new STTPlayer2Client(STT_HTTP_CLIENT, this);
    }

    @Override
    public String url() {
        return url;
    }

    @Override
    public boolean enabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public static class Serializer implements SerializableSite<Player2Site> {
        public static final Codec<Player2Site> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf(ID).forGetter(Player2Site::id),
                ResourceLocation.CODEC.fieldOf(ICON).forGetter(Player2Site::icon),
                Codec.STRING.fieldOf(URL).forGetter(Player2Site::url),
                Codec.BOOL.optionalFieldOf(ENABLED, true).forGetter(Player2Site::enabled),
                Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf(HEADERS).forGetter(Player2Site::headers)
        ).apply(instance, Player2Site::new));

        @Override
        public Player2Site defaultSite() {
            return new Player2Site(API_TYPE, SerializableSite.defaultIcon(API_TYPE),
                    "http://127.0.0.1:4315/v1/stt", true,
                    Map.of("player2-game-key", "TouhouLittleMaid"));
        }

        @Override
        public Codec<Player2Site> codec() {
            return CODEC;
        }
    }
}
