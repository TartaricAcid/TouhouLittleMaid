package com.github.tartaricacid.touhoulittlemaid.ai.service;

import javax.annotation.Nullable;
import java.net.http.HttpRequest;

/**
 * 响应回调接口
 * 用于处理异步请求的响应
 *
 * @param <T> 响应数据类型
 */
public interface ResponseCallback<T> {
    /**
     * 请求失败时调用
     *
     * @param request   请求对象
     * @param throwable 异常信息
     * @param errorCode 错误代码
     */
    void onFailure(@Nullable HttpRequest request, Throwable throwable, int errorCode);

    /**
     * 请求成功时调用
     *
     * @param response 响应数据
     */
    void onSuccess(T response);
}
