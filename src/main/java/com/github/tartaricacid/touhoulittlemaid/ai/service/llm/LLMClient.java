package com.github.tartaricacid.touhoulittlemaid.ai.service.llm;


import com.github.tartaricacid.touhoulittlemaid.ai.manager.response.ResponseChat;
import com.github.tartaricacid.touhoulittlemaid.ai.service.Client;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ErrorCode;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ResponseCallback;
import com.google.gson.JsonSyntaxException;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.function.Consumer;

public interface LLMClient extends Client {
    /**
     * 大语言模型聊天接口
     *
     * @param messages 聊天的上下文，包括提示词，历史记录，用户输入内容
     * @param config   聊天配置
     * @param callback 回调，返回聊天结果字符串
     */
    void chat(List<LLMMessage> messages, LLMConfig config, ResponseCallback<ResponseChat> callback);

    /**
     * 提供的工具方法，用来处理 HTTP 响应信息
     *
     * @param callback  回调
     * @param response  响应信息
     * @param throwable 响应的错误，没有错误时为 null
     * @param request   之前 HTTP 发送的的请求
     */
    default <T> void handleResponse(ResponseCallback<ResponseChat> callback, HttpResponse<String> response,
                                    @Nullable Throwable throwable, HttpRequest request,
                                    Consumer<T> onSuccess, Type type) {
        if (throwable != null) {
            callback.onFailure(request, throwable, ErrorCode.REQUEST_SENDING_ERROR);
            return;
        }
        try {
            String string = response.body();
            int statusCode = response.statusCode();
            if (isSuccessful(response)) {
                T message = GSON.fromJson(string, type);
                onSuccess.accept(message);
            } else {
                String message = "HTTP Error Code: %d, Response %s".formatted(statusCode, response);
                callback.onFailure(request, new Throwable(message), ErrorCode.REQUEST_RECEIVED_ERROR);
            }
        } catch (JsonSyntaxException e) {
            String message = "Exception %s, JSON is: %s".formatted(e.getLocalizedMessage(), response.body());
            callback.onFailure(request, new Throwable(message), ErrorCode.JSON_DECODE_ERROR);
        }
    }
}
