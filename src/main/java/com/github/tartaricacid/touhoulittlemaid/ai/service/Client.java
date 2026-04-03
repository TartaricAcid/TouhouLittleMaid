package com.github.tartaricacid.touhoulittlemaid.ai.service;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.gson.Gson;

import javax.annotation.Nullable;
import java.net.http.HttpResponse;

/**
 * 所有服务的客户端接口
 */
public interface Client {
    /**
     * 默认不序列化 null 值
     */
    Gson GSON = new Gson();

    /**
     * 工具方法，用了判断 http 响应是否成功
     */
    default boolean isSuccessful(HttpResponse<?> response) {
        int statusCode = response.statusCode();
        return 200 <= statusCode && statusCode < 300;
    }

    /**
     * 检查女仆当前是否应停止 AI 对话。
     * 女仆死亡或不存在时，所有的对话应立即中止。
     */
    default boolean shouldStopChat(@Nullable EntityMaid maid) {
        return maid == null || !maid.isAlive();
    }
}
