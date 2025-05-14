package com.github.tartaricacid.touhoulittlemaid.ai.service.stt;

import com.github.tartaricacid.touhoulittlemaid.ai.service.Client;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ResponseCallback;

public interface STTClient extends Client {
    void startRecord(STTConfig config, ResponseCallback<String> callback);

    void stopRecord(STTConfig config, ResponseCallback<String> callback);
}
