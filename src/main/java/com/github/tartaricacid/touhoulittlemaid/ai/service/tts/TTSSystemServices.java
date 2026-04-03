package com.github.tartaricacid.touhoulittlemaid.ai.service.tts;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.TTSCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ResponseCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.SystemServices;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;

public interface TTSSystemServices extends SystemServices {
    void play(String message, TTSConfig config, @Nullable TTSCallback callback);

    default boolean isClient() {
        return Dist.CLIENT.isClient();
    }

    default void writeToNetwork(String message, TTSConfig config, FriendlyByteBuf buffer) {
        buffer.writeUtf(message);
        buffer.writeUtf(config.model());
        buffer.writeUtf(config.language());
    }

    default Pair<String, TTSConfig> readFromNetwork(FriendlyByteBuf buffer) {
        String message = buffer.readUtf();
        String model = buffer.readUtf();
        String language = buffer.readUtf();
        return Pair.of(message, new TTSConfig(model, language));
    }
}
