package com.github.tartaricacid.touhoulittlemaid.ai.service.tts.fishaudio;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ResponseCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.Format;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSConfig;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.fishaudio.request.OpusBitRate;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.fishaudio.request.TTSFishAudioRequest;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class TTSFishAudioClient implements TTSClient {
    private final HttpClient httpClient;
    private final FishAudioSite site;

    public TTSFishAudioClient(HttpClient httpClient, FishAudioSite site) {
        this.httpClient = httpClient;
        this.site = site;
    }

    @Override
    public void play(String message, TTSConfig config, ResponseCallback<byte[]> callback) {
        URI url = URI.create(this.site.url());
        String apiKey = this.site.secretKey();
        String model = config.model();

        TTSFishAudioRequest request = TTSFishAudioRequest.create()
                .setReferenceId(model)
                .setFormat(Format.OPUS)
                // OPUS 极低比特率情况下，音质效果也还不错
                .setOpusBitrate(OpusBitRate.LOWEST)
                .setText(message);

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.JSON_UTF_8.toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(request)))
                .timeout(Duration.ofSeconds(20))
                .uri(url);

        this.site.headers().forEach(builder::header);
        HttpRequest httpRequest = builder.build();

        httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofByteArray())
                .whenComplete((response, throwable) -> handle(callback, response, throwable, httpRequest));
    }

    private void handle(ResponseCallback<byte[]> callback, HttpResponse<byte[]> response, Throwable throwable, HttpRequest httpRequest) {
        if (throwable != null) {
            callback.onFailure(httpRequest, throwable);
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
