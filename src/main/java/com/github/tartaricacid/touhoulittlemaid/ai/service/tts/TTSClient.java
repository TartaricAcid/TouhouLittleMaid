package com.github.tartaricacid.touhoulittlemaid.ai.service.tts;

import java.util.function.Consumer;

public interface TTSClient<T extends TTSRequest> {
    TTSClient<T> request(T request);

    void handle(Consumer<byte[]> consumer, Consumer<Throwable> failConsumer);
}
