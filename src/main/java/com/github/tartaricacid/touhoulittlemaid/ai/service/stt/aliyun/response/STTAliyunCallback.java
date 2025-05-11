package com.github.tartaricacid.touhoulittlemaid.ai.service.stt.aliyun.response;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ResponseCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.Service;
import com.google.gson.JsonSyntaxException;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Consumer;

public class STTAliyunCallback implements ResponseCallback<String> {
    private final Consumer<Message> consumer;

    public STTAliyunCallback(Consumer<Message> consumer) {
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
                Message message = Service.GSON.fromJson(string, Message.class);
                if (message.getStatus() == 20000000) {
                    consumer.accept(message);
                } else {
                    TouhouLittleMaid.LOGGER.error("Request failed: {}", message.getMessage());
                    failConsumer.accept(new Throwable(message.getMessage()));
                }
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
