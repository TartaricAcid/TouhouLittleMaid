package com.github.tartaricacid.touhoulittlemaid.ai.service;

import com.google.gson.Gson;

import java.net.http.HttpResponse;

/**
 * 所有服务的客户端接口
 */
public interface Client {
    Gson GSON = new Gson();

    /**
     * 工具方法，用了判断 http 响应是否成功
     */
    default boolean isSuccessful(HttpResponse<?> response) {
        int statusCode = response.statusCode();
        return 200 <= statusCode && statusCode < 300;
    }
}
