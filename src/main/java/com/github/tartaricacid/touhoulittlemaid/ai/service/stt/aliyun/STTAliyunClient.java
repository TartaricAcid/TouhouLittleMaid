package com.github.tartaricacid.touhoulittlemaid.ai.service.stt.aliyun;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ResponseCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTConfig;
import com.github.tartaricacid.touhoulittlemaid.client.sound.record.MicrophoneManager;
import com.google.gson.JsonSyntaxException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.Mixer;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class STTAliyunClient implements STTClient {
    private static final AudioFormat FORMAT = new AudioFormat(16000, 16, 1, true, false);

    private final HttpClient httpClient;
    private final AliyunSite site;

    public STTAliyunClient(HttpClient httpClient, AliyunSite site) {
        this.httpClient = httpClient;
        this.site = site;
    }

    @Override
    public void startRecord(STTConfig config, ResponseCallback<String> callback) {
        List<Mixer.Info> allMicrophoneInfo = MicrophoneManager.getAllMicrophoneInfo(FORMAT);
        Mixer.Info info = allMicrophoneInfo.get(0);
        URI uri = URI.create(this.site.url());

        MicrophoneManager.startRecord(info.getName(), FORMAT, data -> {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("X-NLS-Token", this.site.getSecretKey())
                    .header("Content-type", "application/octet-stream")
                    .POST(HttpRequest.BodyPublishers.ofByteArray(data))
                    .timeout(Duration.ofSeconds(15))
                    .build();
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .whenComplete((response, throwable) -> handle(callback, response, throwable, request));
        });
    }

    private void handle(ResponseCallback<String> callback, HttpResponse<String> response, Throwable throwable, HttpRequest request) {
        if (throwable != null) {
            callback.onFailure(request, throwable);
            return;
        }
        try {
            String string = response.body();
            if (isSuccessful(response)) {
                Message message = GSON.fromJson(string, Message.class);
                if (message.getStatus() == 20000000) {
                    callback.onSuccess(message.getResult());
                } else {
                    TouhouLittleMaid.LOGGER.error("Request failed: {}", message.getMessage());
                    callback.onFailure(request, new Throwable(message.getMessage()));
                }
                return;
            }
            TouhouLittleMaid.LOGGER.error("Request failed: {}", string);
            String message = String.format("HTTP Error Code: %d, Response %s", response.statusCode(), string);
            callback.onFailure(request, new Throwable(message));
        } catch (JsonSyntaxException e) {
            TouhouLittleMaid.LOGGER.error("JSON Syntax Exception: ", e);
            callback.onFailure(request, e);
        }
    }

    @Override
    public void stopRecord(STTConfig config, ResponseCallback<String> callback) {
        MicrophoneManager.stopRecord();
    }
}
