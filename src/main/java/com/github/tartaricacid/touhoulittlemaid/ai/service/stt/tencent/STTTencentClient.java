package com.github.tartaricacid.touhoulittlemaid.ai.service.stt.tencent;

import com.github.tartaricacid.touhoulittlemaid.ai.service.Client;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ErrorCode;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ResponseCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTConfig;
import com.github.tartaricacid.touhoulittlemaid.client.sound.record.MicrophoneManager;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.Mixer;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class STTTencentClient implements STTClient {
    private static final AudioFormat FORMAT = new AudioFormat(16000, 16, 1, true, false);
    private static final Duration MAX_TIMEOUT = Duration.ofSeconds(15);

    private static final String ACTION_KEY = "X-TC-Action";
    private static final String ACTION_VALUE = "SentenceRecognition";
    private static final String VERSION_KEY = "X-TC-Version";
    private static final String VERSION_VALUE = "2019-06-14";
    private static final String TIMESTAMP_KEY = "X-TC-Timestamp";

    private final HttpClient httpClient;
    private final STTTencentSite site;

    public STTTencentClient(HttpClient httpClient, STTTencentSite site) {
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
            long timestamp = System.currentTimeMillis() / 1000;
            String requestMsg = Client.GSON.toJson(RequestMessage.createWav(this.site, data));
            String authorization = TencentSign.authorization(this.site, timestamp, requestMsg);

            HttpRequest request = HttpRequest.newBuilder().uri(uri)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.JSON_UTF_8.toString())
                    .header(ACTION_KEY, ACTION_VALUE)
                    .header(VERSION_KEY, VERSION_VALUE)
                    .header(TIMESTAMP_KEY, String.valueOf(timestamp))
                    .header(HttpHeaders.AUTHORIZATION, authorization)
                    .POST(HttpRequest.BodyPublishers.ofString(requestMsg))
                    .timeout(MAX_TIMEOUT).build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .whenComplete((response, throwable) ->
                            handle(callback, response, throwable, request));
        });
    }

    private void handle(ResponseCallback<String> callback, HttpResponse<String> response, Throwable throwable, HttpRequest request) {
        this.<ResponseMessage>handleResponse(callback, response, throwable, request, message -> {
            ResponseMessage.Response msg = message.response();
            ResponseMessage.Error error = msg.error();
            if (error == null) {
                callback.onSuccess(msg.result());
            } else {
                Throwable errMsg = new Throwable("Error Code: %s, Error Message: %s".formatted(error.code(), error.message()));
                callback.onFailure(request, errMsg, ErrorCode.REQUEST_RECEIVED_ERROR);
            }
        }, ResponseMessage.class);
    }

    @Override
    public void stopRecord(STTConfig config, ResponseCallback<String> callback) {
        MicrophoneManager.stopRecord();
    }
}
