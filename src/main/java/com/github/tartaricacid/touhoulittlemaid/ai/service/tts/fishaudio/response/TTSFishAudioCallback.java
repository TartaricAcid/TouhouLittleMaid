package com.github.tartaricacid.touhoulittlemaid.ai.service.tts.fishaudio.response;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ResponseCallback;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class TTSFishAudioCallback implements ResponseCallback<byte[]> {
    private final Consumer<byte[]> consumer;

    public TTSFishAudioCallback(Consumer<byte[]> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void onFailure(HttpRequest request, Throwable e) {
        TouhouLittleMaid.LOGGER.error("Request failed: {}", request, e);
    }

    @Override
    public void onResponse(HttpResponse<byte[]> response, Consumer<Throwable> failConsumer) {
        if (isSuccessful(response)) {
            consumer.accept(response.body());
        } else {
            TouhouLittleMaid.LOGGER.error("Request failed: {}", response.statusCode());
            String message = String.format("HTTP Error Code: %d, Response %s", response.statusCode(), new String(response.body(), StandardCharsets.UTF_8));
            failConsumer.accept(new Throwable(message));
        }
    }
}
