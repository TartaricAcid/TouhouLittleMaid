package com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.summary;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.LLMCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.MaidAIChatManager;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.response.ResponseChat;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMMessage;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.net.http.HttpRequest;
import java.util.List;

public class HistorySummaryCallback extends LLMCallback {
    private final HistorySummaryManager summaryManager;

    public HistorySummaryCallback(MaidAIChatManager manager, List<LLMMessage> messages) {
        super(manager, messages, true);
        this.summaryManager = manager.getHistorySummaryManager();
        this.needAddTools = false;
    }

    @Override
    public void onFailure(@Nullable HttpRequest request, Throwable throwable, int errorCode) {
        this.chatManager.getHistorySummaryManager().stopHistorySummary();
        TouhouLittleMaid.LOGGER.error("Failed to compact maid AI history summary, error code is {}, cause is {}", errorCode, throwable.getMessage());
    }

    @Override
    public void onSuccess(ResponseChat response) {
        String summary = response.getChatText();
        if (StringUtils.isBlank(summary)) {
            this.summaryManager.stopHistorySummary();
            return;
        }
        this.summaryManager.completeHistorySummary(summary, this.messages);
    }
}
