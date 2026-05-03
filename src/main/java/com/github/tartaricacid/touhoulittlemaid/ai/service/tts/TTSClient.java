package com.github.tartaricacid.touhoulittlemaid.ai.service.tts;


import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.TTSCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.Client;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ErrorCode;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;

import javax.annotation.Nullable;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public interface TTSClient extends Client {
    /**
     * 语音合成接口
     *
     * @param message  需要合成的文本
     * @param config   语音合成配置
     * @param callback 回调，返回合成的音频数据
     */
    void play(String message, TTSConfig config, TTSCallback callback);

    /**
     * 提供的工具方法，用来处理 HTTP 响应信息
     *
     * @param callback  回调
     * @param response  响应信息
     * @param throwable 响应的错误，没有错误时为 null
     * @param request   之前 HTTP 发送的的请求
     */
    default void handleResponse(TTSCallback callback, HttpResponse<byte[]> response,
                                @Nullable Throwable throwable, HttpRequest request) {
        // 优先检查女仆是否存在
        EntityMaid maid = callback.getMaid();
        if (this.shouldStopChat(maid)) {
            return;
        }
        if (throwable != null) {
            callback.onFailure(request, throwable, ErrorCode.REQUEST_SENDING_ERROR);
            return;
        }
        if (isSuccessful(response)) {
            callback.onSuccess(response.body());
        } else {
            String errorMsg = new String(response.body(), StandardCharsets.UTF_8);
            String message = "HTTP Error Code: %d, Response %s".formatted(response.statusCode(), errorMsg);
            callback.onFailure(request, new Throwable(message), ErrorCode.REQUEST_RECEIVED_ERROR);
        }
    }
}
