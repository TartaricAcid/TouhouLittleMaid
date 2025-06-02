package com.github.tartaricacid.touhoulittlemaid.ai.service.stt.siliconflow;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ErrorCode;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ResponseCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTConfig;
import com.github.tartaricacid.touhoulittlemaid.client.sound.record.MicrophoneManager;
import com.github.tartaricacid.touhoulittlemaid.util.http.MultipartBody;
import com.github.tartaricacid.touhoulittlemaid.util.http.MultipartBodyBuilder;
import com.google.common.net.HttpHeaders;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.Mixer;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class STTSiliconflowClient implements STTClient {
    private static final AudioFormat FORMAT = new AudioFormat(16000, 16, 1, true, false);
    private static final Duration MAX_TIMEOUT = Duration.ofSeconds(15);

    private final HttpClient httpClient;
    private final STTSiliconflowSite site;

    public STTSiliconflowClient(HttpClient httpClient, STTSiliconflowSite site) {
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
            try {
                MultipartBody multipartBody = new MultipartBodyBuilder()
                        .addText("model", this.site.getModel())
                        .addPart("file", data, "audio/wav", "audio.wav")
                        .build();
                HttpRequest request = HttpRequest.newBuilder().uri(uri)
                        .header(HttpHeaders.CONTENT_TYPE, multipartBody.getContentType())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.site.getSecretKey())
                        .POST(HttpRequest.BodyPublishers.ofByteArray(multipartBody.getBody()))
                        .timeout(MAX_TIMEOUT).build();
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .whenComplete((response, throwable) ->
                                handle(callback, response, throwable, request));
            } catch (IOException e) {
                TouhouLittleMaid.LOGGER.error(e);
            }
        });
    }

    private void handle(ResponseCallback<String> callback, HttpResponse<String> response, Throwable throwable, HttpRequest request) {
        this.<Message>handleResponse(callback, response, throwable, request,
                message -> callback.onSuccess(message.getText()),
                Message.class);
    }

    @Override
    public void stopRecord(STTConfig config, ResponseCallback<String> callback) {
        MicrophoneManager.stopRecord();
    }
}
