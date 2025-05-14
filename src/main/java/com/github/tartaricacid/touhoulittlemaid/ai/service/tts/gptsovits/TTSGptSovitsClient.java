package com.github.tartaricacid.touhoulittlemaid.ai.service.tts.gptsovits;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ResponseCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSConfig;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class TTSGptSovitsClient implements TTSClient {
    private final HttpClient httpClient;
    private final GptSovitsSite site;

    public TTSGptSovitsClient(HttpClient httpClient, GptSovitsSite site) {
        this.httpClient = httpClient;
        this.site = site;
    }

    @Override
    public void play(String message, TTSConfig config, ResponseCallback<byte[]> callback) {
        URI uri = URI.create(this.site.url());
        TTSGptSovitsRequest request = TTSGptSovitsRequest.create()
                .setText(message)
                .setTextLang(config.language())
                .setRefAudioPath(this.site.refAudioPath())
                .setPromptText(this.site.promptText())
                .setPromptLang(this.site.promptLang())
                .setAuxRefAudioPaths(this.site.auxRefAudioPaths())
                .setTextSplitMethod(this.site.textSplitMethod());

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.JSON_UTF_8.toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.site.secretKey())
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(request)))
                .timeout(Duration.ofSeconds(20))
                .uri(uri).build();

        httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofByteArray())
                .whenComplete((response, throwable) -> handle(callback, response, throwable, httpRequest));
    }

    private void handle(ResponseCallback<byte[]> callback, HttpResponse<byte[]> response, Throwable throwable, HttpRequest httpRequest) {
        if (throwable != null) {
            callback.onFailure(httpRequest, throwable);
            return;
        }
        if (isSuccessful(response)) {
            callback.onSuccess(response.body());
        } else {
            TouhouLittleMaid.LOGGER.error("Request failed: {}", response.statusCode());
            String error = String.format("HTTP Error Code: %d, Response %s", response.statusCode(), new String(response.body(), StandardCharsets.UTF_8));
            callback.onFailure(httpRequest, new Throwable(error));
        }
    }
}
