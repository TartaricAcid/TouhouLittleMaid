package com.github.tartaricacid.touhoulittlemaid.ai.service.tts.fishaudio;

import com.github.tartaricacid.touhoulittlemaid.ai.service.Service;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.fishaudio.request.TTSFishAudioRequest;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.fishaudio.response.TTSFishAudioCallback;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.function.Consumer;

public class TTSFishAudioClient implements TTSClient<TTSFishAudioRequest> {
    private final HttpClient httpClient;
    private String baseUrl = "";
    private String apiKey = "";
    private TTSFishAudioRequest request;

    public static TTSFishAudioClient create(final HttpClient httpClient) {
        return new TTSFishAudioClient(httpClient);
    }

    private TTSFishAudioClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public TTSFishAudioClient baseUrl(final String baseUrl) {
        if (baseUrl.endsWith("/")) {
            this.baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        } else {
            this.baseUrl = baseUrl;
        }
        return this;
    }

    public TTSFishAudioClient apiKey(final String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public TTSFishAudioClient request(TTSFishAudioRequest request) {
        this.request = request;
        return this;
    }

    public void handle(Consumer<byte[]> consumer, Consumer<Throwable> failConsumer) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.JSON_UTF_8.toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(Service.GSON.toJson(request)))
                .timeout(Duration.ofSeconds(20))
                .uri(URI.create(baseUrl + TTSFishAudioRequest.getUrl()))
                .build();
        httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofByteArray())
                .whenComplete((response, throwable) -> {
                    TTSFishAudioCallback callback = new TTSFishAudioCallback(consumer);
                    if (throwable != null) {
                        callback.onFailure(httpRequest, throwable);
                        failConsumer.accept(throwable);
                    } else {
                        callback.onResponse(response, failConsumer);
                    }
                });
    }
}
