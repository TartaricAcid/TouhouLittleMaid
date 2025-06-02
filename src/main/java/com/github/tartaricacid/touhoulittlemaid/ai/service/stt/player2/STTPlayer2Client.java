package com.github.tartaricacid.touhoulittlemaid.ai.service.stt.player2;

import com.github.tartaricacid.touhoulittlemaid.ai.service.ResponseCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTConfig;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class STTPlayer2Client implements STTClient {
    private static final Duration MAX_TIMEOUT = Duration.ofSeconds(15);
    private static final String START_URL = "/start";
    private static final String STOP_URL = "/stop";
    private static final String START_REQUEST_BODY = """
            {"timeout":30}""";

    private final HttpClient httpClient;
    private final STTPlayer2Site site;

    public STTPlayer2Client(HttpClient httpClient, STTPlayer2Site site) {
        this.httpClient = httpClient;
        this.site = site;
    }

    @Override
    public void startRecord(STTConfig config, ResponseCallback<String> callback) {
        URI uri = URI.create(this.site.url() + START_URL);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.JSON_UTF_8.toString())
                .POST(HttpRequest.BodyPublishers.ofString(START_REQUEST_BODY))
                .timeout(MAX_TIMEOUT).uri(uri);
        this.site.headers().forEach(builder::header);
        HttpRequest httpRequest = builder.build();
        httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                .whenComplete((response, throwable) ->
                        handleStart(callback, response, throwable, httpRequest));
    }

    private void handleStart(ResponseCallback<String> callback, HttpResponse<String> response, Throwable throwable, HttpRequest request) {
        this.<Message>handleResponse(callback, response, throwable, request, message -> {
            // 开始录音时，什么都不需要做
        }, Message.class);
    }

    @Override
    public void stopRecord(STTConfig config, ResponseCallback<String> callback) {
        URI uri = URI.create(this.site.url() + STOP_URL);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.JSON_UTF_8.toString())
                .POST(HttpRequest.BodyPublishers.noBody())
                .timeout(MAX_TIMEOUT).uri(uri);
        this.site.headers().forEach(builder::header);
        HttpRequest httpRequest = builder.build();
        httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                .whenComplete((response, throwable) ->
                        handleStop(callback, response, throwable, httpRequest));
    }

    private void handleStop(ResponseCallback<String> callback, HttpResponse<String> response, Throwable throwable, HttpRequest request) {
        this.<Message>handleResponse(callback, response, throwable, request, message -> callback.onSuccess(message.getText()), Message.class);
    }
}
