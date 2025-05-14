package com.github.tartaricacid.touhoulittlemaid.ai.service;

import java.net.http.HttpRequest;

public interface ResponseCallback<T> {
    void onFailure(HttpRequest request, Throwable e);

    void onSuccess(T response);
}
