package com.github.tartaricacid.touhoulittlemaid.ai.service.tts.mimo;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.TTSCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ErrorCode;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.gson.JsonSyntaxException;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Base64;

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
        String siteModel = this.site.effectiveSiteModel();
        String voiceCloneAudio;

        try {
            voiceCloneAudio = this.createVoiceCloneAudio(siteModel);
        } catch (Exception e) {
            callback.onFailure(null, e, ErrorCode.REQUEST_SENDING_ERROR);
            return;
        }

        TTSMimoRequest request = TTSMimoRequest.create(siteModel, this.site.voicePrompt(), voiceCloneAudio, message, voice);

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

    @Nullable
    private String createVoiceCloneAudio(String siteModel) throws IOException {
        if (!TTSMimoSite.isVoiceCloneModel(siteModel)) {
            return null;
        }
        String source = this.site.voiceCloneAudio();
        if (StringUtils.isBlank(source)) {
            throw new IllegalStateException("Mimo voiceclone reference audio is empty");
        }

        String base64Audio;
        if (TTSMimoSite.VOICE_CLONE_MODE_FILE.equals(this.site.voiceCloneInputMode())) {
            Path path = Path.of(TTSMimoSite.normalizeVoiceCloneFilePath(source));
            if (!Files.exists(path)) {
                throw new IOException("Mimo voiceclone reference audio file not found on server: " + path);
            }
            if (!Files.isRegularFile(path)) {
                throw new IOException("Mimo voiceclone reference audio path is not a file on server: " + path);
            }
            if (!Files.isReadable(path)) {
                throw new IOException("Mimo voiceclone reference audio file is not readable on server: " + path);
            }
            byte[] audio = Files.readAllBytes(path);
            base64Audio = Base64.getEncoder().encodeToString(audio);
        } else {
            base64Audio = StringUtils.defaultString(source).replaceAll("\\s+", "");
        }
        String mimeType = TTSMimoSite.normalizeVoiceCloneMimeType(this.site.voiceCloneMimeType());
        return "data:%s;base64,%s".formatted(mimeType, base64Audio);
    }
}
