package com.github.tartaricacid.touhoulittlemaid.ai.service.stt.aliyun;

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

public class STTAliyunClient implements STTClient {
    private static final AudioFormat FORMAT = new AudioFormat(16000, 16, 1, true, false);
    private static final Duration MAX_TIMEOUT = Duration.ofSeconds(15);
    private static final String TOKEN = "X-NLS-Token";
    private static final int OK_STATUS = 20000000;

    private final HttpClient httpClient;
    private final STTAliyunSite site;

    public STTAliyunClient(HttpClient httpClient, STTAliyunSite site) {
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
            HttpRequest request = HttpRequest.newBuilder().uri(uri)
                    .header(TOKEN, this.site.getSecretKey())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.OCTET_STREAM.toString())
                    .POST(HttpRequest.BodyPublishers.ofByteArray(data))
                    .timeout(MAX_TIMEOUT).build();
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .whenComplete((response, throwable) ->
                            handle(callback, response, throwable, request));
        });
    }

    private void handle(ResponseCallback<String> callback, HttpResponse<String> response, Throwable throwable, HttpRequest request) {
        this.<Message>handleResponse(callback, response, throwable, request, message -> {
            if (message.getStatus() == OK_STATUS) {
                callback.onSuccess(message.getResult());
            } else {
                callback.onFailure(request, new Throwable(message.getMessage()), ErrorCode.REQUEST_RECEIVED_ERROR);
            }
        }, Message.class);
    }

    @Override
    public void stopRecord(STTConfig config, ResponseCallback<String> callback) {
        MicrophoneManager.stopRecord();
    }
}
