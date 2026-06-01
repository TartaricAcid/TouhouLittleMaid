package com.github.tartaricacid.touhoulittlemaid.ai.service.tts.mimo;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.TTSCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ErrorCode;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.gson.JsonSyntaxException;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class TTSMimoClient implements TTSClient {
    private static final Duration MAX_TIMEOUT = Duration.ofSeconds(20);
    private static final String API_KEY_HEADER = "api-key";

    private final HttpClient httpClient;
    private final TTSMimoSite site;

    public TTSMimoClient(HttpClient httpClient, TTSMimoSite site) {
        this.httpClient = httpClient;
        this.site = site;
    }

    @Override
    public void play(String message, TTSConfig config, TTSCallback callback) {
        URI url = URI.create(this.site.url());
        String apiKey = this.site.secretKey();
        String voice = TTSMimoSite.getApiVoiceName(config.model());

        TTSMimoRequest request = TTSMimoRequest.create(this.site.siteModel(), this.site.voicePrompt(), message, voice);

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.JSON_UTF_8.toString())
                .header(API_KEY_HEADER, apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(request)))
                .timeout(MAX_TIMEOUT).uri(url);

        this.site.headers().forEach(builder::header);
        HttpRequest httpRequest = builder.build();

        httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofByteArray())
                .whenComplete((response, throwable) ->
                        handleResponse(callback, response, throwable, httpRequest));
    }

    @Override
    public void handleResponse(TTSCallback callback, HttpResponse<byte[]> response,
                               @Nullable Throwable throwable, HttpRequest request) {
        EntityMaid maid = callback.getMaid();
        if (this.shouldStopChat(maid)) {
            return;
        }

        if (throwable != null) {
            callback.onFailure(request, throwable, ErrorCode.REQUEST_SENDING_ERROR);
            return;
        }

        if (isSuccessful(response)) {
            try {
                callback.onSuccess(decodeAudioResponse(response.body()));
            } catch (Exception e) {
                callback.onFailure(request, e, ErrorCode.REQUEST_RECEIVED_ERROR);
            }
        } else {
            String errorMsg = new String(response.body(), StandardCharsets.UTF_8);
            String message = "HTTP Error Code: %d, Response %s".formatted(response.statusCode(), errorMsg);
            callback.onFailure(request, new IOException(message), ErrorCode.REQUEST_RECEIVED_ERROR);
        }
    }

    static byte[] decodeAudioResponse(byte[] body) {
        String responseText = new String(body, StandardCharsets.UTF_8);
        TTSMimoResponse mimoResponse;
        try {
            mimoResponse = GSON.fromJson(responseText, TTSMimoResponse.class);
        } catch (JsonSyntaxException e) {
            throw new IllegalStateException("Failed to parse Mimo TTS response: " + responseText, e);
        }
        if (mimoResponse == null) {
            throw new IllegalStateException("Mimo TTS returned empty response body");
        }
        return mimoResponse.decodeAudioData();
    }
}
