package com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai;


import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.request.ChatCompletion;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.request.DoubaoThinking;

import java.net.http.HttpClient;

/**
 * 豆包变种的 OpenAI API
 */
public final class LLMDoubaoClient extends LLMOpenAIClient {
    public LLMDoubaoClient(HttpClient httpClient, LLMOpenAISite site) {
        super(httpClient, site);
    }

    @Override
    protected ChatCompletion extraArgs(ChatCompletion chatCompletion) {
        // 禁用豆包的思考链模式，因为思考链速度慢，不适合游戏对话
        // 但是字节跳动的 api 会默认开启这个选项，故需主动关闭
        return chatCompletion.thinking(DoubaoThinking.disabled());
    }
}
