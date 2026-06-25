package com.github.tartaricacid.touhoulittlemaid.ai.service.stt.mimo;

import com.github.tartaricacid.touhoulittlemaid.ai.service.ErrorCode;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ResponseCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTConfig;
import com.github.tartaricacid.touhoulittlemaid.client.sound.record.MicrophoneManager;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import org.apache.commons.lang3.StringUtils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.Mixer;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class STTMimoClient implements STTClient {
    private static final AudioFormat FORMAT = new AudioFormat(16000, 16, 1, true, false);
    private static final Duration MAX_TIMEOUT = Duration.ofSeconds(20);
    private static final String API_KEY_HEADER = "api-key";

    private final HttpClient httpClient;
    private final STTMimoSite site;

    public STTMimoClient(HttpClient httpClient, STTMimoSite site) {
        this.httpClient = httpClient;
        this.site = site;
    }

    @Override
    public void startRecord(STTConfig config, ResponseCallback<String> callback) {
        Mixer.Info info = MicrophoneManager.getMicrophoneInfo(FORMAT);
        if (info == null) {
            callback.onFailure(null, new Throwable("No suitable microphone found"), ErrorCode.MICROPHONE_NOT_FOUND);
            return;
        }
        URI uri = URI.create(this.site.url());

        MicrophoneManager.startRecord(info.getName(), FORMAT, data -> {
            STTMimoRequest requestMessage = STTMimoRequest.create(this.site.model(), this.site.language(), data);
            HttpRequest request = HttpRequest.newBuilder().uri(uri)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.JSON_UTF_8.toString())
                    .header(API_KEY_HEADER, this.site.secretKey())
                    .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(requestMessage)))
                    .timeout(MAX_TIMEOUT).build();
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .whenComplete((response, throwable) -> handle(callback, response, throwable, request));
        });
    }

    private void handle(ResponseCallback<String> callback, HttpResponse<String> response, Throwable throwable,
                        HttpRequest request) {
        this.<STTMimoResponse>handleResponse(callback, response, throwable, request, message -> {
            String text = StringUtils.trimToEmpty(message.text());
            if (StringUtils.isBlank(text)) {
                callback.onFailure(request, new Throwable("Mimo ASR returned empty text"), ErrorCode.REQUEST_RECEIVED_ERROR);
            } else {
                callback.onSuccess(text);
            }
        }, STTMimoResponse.class);
    }

    @Override
    public void stopRecord(STTConfig config, ResponseCallback<String> callback) {
        MicrophoneManager.stopRecord();
    }
}
