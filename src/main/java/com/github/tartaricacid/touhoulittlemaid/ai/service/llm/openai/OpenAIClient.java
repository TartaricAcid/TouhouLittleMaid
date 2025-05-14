package com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai;


import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ResponseCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMConfig;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMMessage;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.Role;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.request.ChatCompletion;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.request.ResponseFormat;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.response.ChatCompletionResponse;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import com.google.gson.JsonSyntaxException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public final class OpenAIClient implements LLMClient {
    private final HttpClient httpClient;
    private final OpenAISite site;

    public OpenAIClient(HttpClient httpClient, OpenAISite site) {
        this.httpClient = httpClient;
        this.site = site;
    }

    @Override
    public void chat(List<LLMMessage> messages, LLMConfig config, ResponseCallback<String> callback) {
        URI url = URI.create(this.site.url());
        String apiKey = this.site.secretKey();
        String model = config.model();
        double temperature = config.temperature();
        int maxTokens = config.maxTokens();

        // 构建对话
        ChatCompletion chatCompletion = ChatCompletion.create().model(model).maxTokens(maxTokens)
                .temperature(temperature).setResponseFormat(ResponseFormat.json());
        // 添加消息
        for (LLMMessage message : messages) {
            if (message.role() == Role.USER) {
                chatCompletion.userChat(message.message());
            } else if (message.role() == Role.ASSISTANT) {
                chatCompletion.assistantChat(message.message());
            } else if (message.role() == Role.SYSTEM) {
                chatCompletion.systemChat(message.message());
            }
        }

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.JSON_UTF_8.toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(chatCompletion)))
                .timeout(Duration.ofSeconds(20))
                .uri(url);

        this.site.headers().forEach(builder::header);
        HttpRequest httpRequest = builder.build();
        httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                .whenComplete((response, throwable) -> handle(callback, response, throwable, httpRequest));
    }

    private void handle(ResponseCallback<String> callback, HttpResponse<String> response, Throwable throwable, HttpRequest httpRequest) {
        if (throwable != null) {
            callback.onFailure(httpRequest, throwable);
            return;
        }
        try {
            String string = response.body();
            if (isSuccessful(response)) {
                ChatCompletionResponse chatCompletionResponse = GSON.fromJson(string, ChatCompletionResponse.class);
                callback.onSuccess(chatCompletionResponse.getFirstChoiceMessage());
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
