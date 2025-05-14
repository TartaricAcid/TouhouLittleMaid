package com.github.tartaricacid.touhoulittlemaid.ai.service.llm;


import com.github.tartaricacid.touhoulittlemaid.ai.service.Client;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ResponseCallback;

import java.util.List;

public interface LLMClient extends Client {
    void chat(List<LLMMessage> messages, LLMConfig config, ResponseCallback<String> callback);
}
