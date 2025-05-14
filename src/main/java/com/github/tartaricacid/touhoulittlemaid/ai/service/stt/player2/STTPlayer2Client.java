package com.github.tartaricacid.touhoulittlemaid.ai.service.stt.player2;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ResponseCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTConfig;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import com.google.gson.JsonSyntaxException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class STTPlayer2Client implements STTClient {
    private static final String START_URL = "/start";
    private static final String STOP_URL = "/stop";

    private final HttpClient httpClient;
    private final Player2Site site;

    public STTPlayer2Client(HttpClient httpClient, Player2Site site) {
        this.httpClient = httpClient;
        this.site = site;
    }

    @Override
    public void startRecord(STTConfig config, ResponseCallback<String> callback) {
        URI uri = URI.create(this.site.url() + START_URL);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.JSON_UTF_8.toString())
                .POST(HttpRequest.BodyPublishers.ofString("{\"timeout\":30}"))
                .timeout(Duration.ofSeconds(20))
                .uri(uri);
        this.site.headers().forEach(builder::header);
        HttpRequest httpRequest = builder.build();
        httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                .whenComplete((response, throwable) ->
                        handleStart(callback, response, throwable, httpRequest));
    }

    private void handleStart(ResponseCallback<String> callback, HttpResponse<String> response, Throwable throwable, HttpRequest httpRequest) {
        if (throwable != null) {
            callback.onFailure(httpRequest, throwable);
            return;
        }
        try {
            String string = response.body();
            if (!isSuccessful(response)) {
                TouhouLittleMaid.LOGGER.error("Request failed: {}", string);
                String message = String.format("HTTP Error Code: %d, Response %s", response.statusCode(), string);
                callback.onFailure(httpRequest, new Throwable(message));
            }
        } catch (JsonSyntaxException e) {
            TouhouLittleMaid.LOGGER.error("JSON Syntax Exception: ", e);
            callback.onFailure(httpRequest, e);
        }
    }

    @Override
    public void stopRecord(STTConfig config, ResponseCallback<String> callback) {
        URI uri = URI.create(this.site.url() + STOP_URL);

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .header(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .POST(HttpRequest.BodyPublishers.noBody())
                .timeout(Duration.ofSeconds(20))
                .uri(uri);
        this.site.headers().forEach(builder::header);
        HttpRequest httpRequest = builder.build();
        httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                .whenComplete((response, throwable) ->
                        handleStop(callback, response, throwable, httpRequest));
    }

    private void handleStop(ResponseCallback<String> callback, HttpResponse<String> response, Throwable throwable, HttpRequest httpRequest) {
        if (throwable != null) {
            callback.onFailure(httpRequest, throwable);
            return;
        }
        try {
            String string = response.body();
            if (isSuccessful(response)) {
                Message message = GSON.fromJson(string, Message.class);
                callback.onSuccess(message.getText());
            } else {
                TouhouLittleMaid.LOGGER.error("Request failed: {}", string);
                String message = String.format("HTTP Error Code: %d, Response %s", response.statusCode(), string);
                callback.onFailure(httpRequest, new Throwable(message));
            }
        } catch (JsonSyntaxException e) {
            TouhouLittleMaid.LOGGER.error("JSON Syntax Exception: ", e);
            callback.onFailure(httpRequest, e);
        }
    }
}
