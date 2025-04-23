package com.github.tartaricacid.touhoulittlemaid.ai.service.chat.openai.response;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ResponseCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.Service;
import com.google.gson.JsonSyntaxException;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Consumer;

public class ChatCallback implements ResponseCallback<String> {
    private final Consumer<ChatCompletionResponse> consumer;

    public ChatCallback(Consumer<ChatCompletionResponse> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void onFailure(HttpRequest request, Throwable e) {
        TouhouLittleMaid.LOGGER.error("Request failed: {}", request, e);
    }

    @Override
    public void onResponse(HttpResponse<String> response, Consumer<Throwable> failConsumer) {
        try {
            String string = response.body();
            if (isSuccessful(response)) {
                ChatCompletionResponse chatCompletionResponse = Service.GSON.fromJson(string, ChatCompletionResponse.class);
                consumer.accept(chatCompletionResponse);
            } else {
                TouhouLittleMaid.LOGGER.error("Request failed: {}", string);
                String message = String.format("HTTP Error Code: %d, Response %s", response.statusCode(), string);
                failConsumer.accept(new Throwable(message));
            }
        } catch (JsonSyntaxException e) {
            TouhouLittleMaid.LOGGER.error("JSON Syntax Exception: ", e);
            failConsumer.accept(e);
        }
    }
}
