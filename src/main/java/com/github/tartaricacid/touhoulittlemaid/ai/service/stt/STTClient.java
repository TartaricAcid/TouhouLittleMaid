package com.github.tartaricacid.touhoulittlemaid.ai.service.stt;

import com.github.tartaricacid.touhoulittlemaid.ai.service.Client;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ErrorCode;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ResponseCallback;
import com.google.gson.JsonSyntaxException;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Consumer;

/**
 * 语音识别客户端接口
 * 因为不同的语音识别服务，返回结果的时机不一致，故分别对起始和结束都传递了回调
 */
public interface STTClient extends Client {
    /**
     * 开始语音识别，在按下语音识别键时调用
     *
     * @param config   语音识别配置，目前为空
     * @param callback 回调，返回识别结果字符串
     */
    void startRecord(STTConfig config, ResponseCallback<String> callback);

    /**
     * 停止语音识别，在松开语音识别键时调用
     *
     * @param config   语音识别配置，目前为空
     * @param callback 回调，返回识别结果字符串
     */
    void stopRecord(STTConfig config, ResponseCallback<String> callback);

    /**
     * 提供的工具方法，用来处理 HTTP 响应信息
     *
     * @param callback  回调
     * @param response  响应信息
     * @param throwable 响应的错误，没有错误时为 null
     * @param request   之前 HTTP 发送的的请求
     */
    default <T> void handleResponse(ResponseCallback<String> callback, HttpResponse<String> response,
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
