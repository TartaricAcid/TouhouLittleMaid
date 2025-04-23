package com.github.tartaricacid.touhoulittlemaid.ai.service.chat.openai;

import com.github.tartaricacid.touhoulittlemaid.ai.service.Service;
import com.github.tartaricacid.touhoulittlemaid.ai.service.chat.openai.request.ChatCompletion;
import com.github.tartaricacid.touhoulittlemaid.ai.service.chat.openai.response.ChatCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.chat.openai.response.ChatCompletionResponse;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.function.Consumer;

public final class ChatClient {
    private final HttpClient httpClient;
    private String baseUrl = "";
    private String apiKey = "";
    private ChatCompletion chatCompletion;

    public static ChatClient create(final HttpClient httpClient) {
        return new ChatClient(httpClient);
    }

    private ChatClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public ChatClient baseUrl(final String baseUrl) {
        if (baseUrl.endsWith("/")) {
            this.baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        } else {
            this.baseUrl = baseUrl;
        }
        return this;
    }

    public ChatClient apiKey(final String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public ChatClient chat(final ChatCompletion chatCompletion) {
        this.chatCompletion = chatCompletion;
        return this;
    }

    public void handle(Consumer<ChatCompletionResponse> consumer, Consumer<Throwable> failConsumer) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.JSON_UTF_8.toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(Service.GSON.toJson(chatCompletion)))
                .timeout(Duration.ofSeconds(20))
                .uri(URI.create(baseUrl + ChatCompletion.getUrl()))
                .build();
        httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                .whenComplete((response, throwable) -> {
                    ChatCallback callback = new ChatCallback(consumer);
                    if (throwable != null) {
                        callback.onFailure(httpRequest, throwable);
                        failConsumer.accept(throwable);
                    } else {
                        callback.onResponse(response, failConsumer);
                    }
                });
    }
}
