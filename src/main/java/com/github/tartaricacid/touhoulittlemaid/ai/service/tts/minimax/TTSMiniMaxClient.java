package com.github.tartaricacid.touhoulittlemaid.ai.service.tts.minimax;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.TTSCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ErrorCode;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSConfig;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.minimax.request.TTSMiniMaxRequest;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.minimax.response.AudioResponse;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HexFormat;

public class TTSMiniMaxClient implements TTSClient {
    private static final Duration MAX_TIMEOUT = Duration.ofSeconds(20);

    private final HttpClient httpClient;
    private final TTSMiniMaxSite site;

    public TTSMiniMaxClient(HttpClient httpClient, TTSMiniMaxSite site) {
        this.httpClient = httpClient;
        this.site = site;
    }

    @Override
    public void play(String message, TTSConfig config, TTSCallback callback) {
        URI url = URI.create(this.site.url());
        String apiKey = this.site.secretKey();
        String voiceId = config.model();
        String siteModel = this.site.siteModel();

        TTSMiniMaxRequest request = TTSMiniMaxRequest.create()
                .setSiteModel(siteModel)
                .setVoiceId(voiceId)
                .setText(message);

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.JSON_UTF_8.toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
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
        // 优先检查女仆是否存在
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
                AudioResponse audioResponse = GSON.fromJson(new String(response.body(), StandardCharsets.UTF_8), AudioResponse.class);
                AudioResponse.BaseResponse baseResponse = audioResponse.response();
                int code = baseResponse.statusCode();
                if (code == 0) {
                    callback.onSuccess(HexFormat.of().parseHex(audioResponse.data().audio()));
                } else {
                    String message = "API Error Code: %d, Message: %s".formatted(code, baseResponse.statusMsg());
                    callback.onFailure(request, new Throwable(message), ErrorCode.REQUEST_RECEIVED_ERROR);
                }
            } catch (Exception e) {
                callback.onFailure(request, e, ErrorCode.REQUEST_RECEIVED_ERROR);
            }
        } else {
            String message = "HTTP Error Code: %d, Response %s".formatted(response.statusCode(), response);
            callback.onFailure(request, new Throwable(message), ErrorCode.REQUEST_RECEIVED_ERROR);
        }
    }
}
