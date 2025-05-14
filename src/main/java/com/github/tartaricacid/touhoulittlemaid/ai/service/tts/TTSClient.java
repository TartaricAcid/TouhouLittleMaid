package com.github.tartaricacid.touhoulittlemaid.ai.service.tts;


import com.github.tartaricacid.touhoulittlemaid.ai.service.Client;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ResponseCallback;

public interface TTSClient extends Client {
    void play(String message, TTSConfig config, ResponseCallback<byte[]> callback);
}
