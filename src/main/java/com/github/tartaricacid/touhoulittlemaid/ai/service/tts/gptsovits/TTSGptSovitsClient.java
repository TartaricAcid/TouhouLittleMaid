package com.github.tartaricacid.touhoulittlemaid.ai.service.tts.gptsovits;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.TTSCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSConfig;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class TTSGptSovitsClient implements TTSClient {
    private static final Duration MAX_TIMEOUT = Duration.ofSeconds(20);
    private static final Duration MODEL_SWITCH_TIMEOUT = Duration.ofSeconds(60);
    private static final String SET_GPT_WEIGHTS = "set_gpt_weights";
    private static final String SET_SOVITS_WEIGHTS = "set_sovits_weights";

    private final HttpClient httpClient;
    private final TTSGptSovitsSite site;

    public TTSGptSovitsClient(HttpClient httpClient, TTSGptSovitsSite site) {
        this.httpClient = httpClient;
        this.site = site;
    }

    @Override
    public void play(String message, TTSConfig config, TTSCallback callback) {
        URI uri = URI.create(this.site.url());
        TTSGptSovitsRequest request = TTSGptSovitsRequest.create()
                .setText(message)
                .setTextLang(config.language())
                .setRefAudioPath(this.site.refAudioPath())
                .setPromptText(this.site.promptText())
                .setPromptLang(this.site.promptLang())
                .setAuxRefAudioPaths(this.site.auxRefAudioPaths())
                .setTextSplitMethod(this.site.textSplitMethod());

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.JSON_UTF_8.toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.site.secretKey())
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(request)))
                .timeout(MAX_TIMEOUT)
                .uri(uri);

        this.site.headers().forEach(builder::header);
        HttpRequest httpRequest = builder.build();

        CompletableFuture<Void> prepareModels = CompletableFuture.completedFuture(null);
        if (isNotBlank(this.site.gptModelPath())) {
            prepareModels = prepareModels.thenCompose(unused ->
                    this.switchModel(SET_GPT_WEIGHTS, this.site.gptModelPath()));
        }
        if (isNotBlank(this.site.sovitsModelPath())) {
            prepareModels = prepareModels.thenCompose(unused ->
                    this.switchModel(SET_SOVITS_WEIGHTS, this.site.sovitsModelPath()));
        }

        prepareModels.thenCompose(unused ->
                        httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofByteArray()))
                .whenComplete((response, throwable) ->
                        handleResponse(callback, response, throwable, httpRequest));
    }

    private CompletableFuture<Void> switchModel(String endpoint, String modelPath) {
        String encodedPath = URLEncoder.encode(modelPath, StandardCharsets.UTF_8);
        URI uri = URI.create(this.site.url()).resolve("./%s?weights_path=%s".formatted(endpoint, encodedPath));

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.site.secretKey())
                .GET()
                .timeout(MODEL_SWITCH_TIMEOUT)
                .uri(uri);

        this.site.headers().forEach(builder::header);
        HttpRequest request = builder.build();
        return this.httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofByteArray())
                .thenAccept(response -> {
                    if (!isSuccessful(response)) {
                        String responseBody = new String(response.body(), StandardCharsets.UTF_8);
                        String message = "Failed to switch GPT-SoVITS model: HTTP %d, Response %s"
                                .formatted(response.statusCode(), responseBody);
                        throw new CompletionException(new IOException(message));
                    }
                });
    }
}
