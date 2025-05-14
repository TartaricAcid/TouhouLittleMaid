package com.github.tartaricacid.touhoulittlemaid.ai.service;

import com.google.gson.Gson;

import java.net.http.HttpResponse;

public interface Client {
    Gson GSON = new Gson();

    default boolean isSuccessful(HttpResponse<?> response) {
        int statusCode = response.statusCode();
        return 200 <= statusCode && statusCode < 300;
    }
}
