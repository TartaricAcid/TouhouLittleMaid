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
    private final List<LLMMessage> snapshot;
    private final @Nullable Runnable afterSummary;

    public HistorySummaryCallback(MaidAIChatManager manager, List<LLMMessage> messages,
                                  List<LLMMessage> snapshot, @Nullable Runnable afterSummary) {
        super(manager, messages, true);
        this.summaryManager = manager.getHistorySummaryManager();
        this.snapshot = snapshot;
        this.afterSummary = afterSummary;
        this.needAddTools = false;
    }

    @Override
    public boolean shouldCacheTokenUsage() {
        return false;
    }

    @Override
    public void onFailure(@Nullable HttpRequest request, Throwable throwable, int errorCode) {
        this.chatManager.getHistorySummaryManager().stopHistorySummary();
        TouhouLittleMaid.LOGGER.error("Failed to compact maid AI history summary, error code is {}, cause is {}", errorCode, throwable.getMessage());
        this.runAfterSummary();
    }

    @Override
    public void onSuccess(ResponseChat response) {
        String summary = response.getChatText();
        if (StringUtils.isBlank(summary)) {
            this.summaryManager.stopHistorySummary();
            this.runAfterSummary();
            return;
        }
        this.summaryManager.completeHistorySummary(summary, this.snapshot);
        this.runAfterSummary();
    }

    private void runAfterSummary() {
        if (this.afterSummary != null) {
            this.runOnServerThread(this.afterSummary);
        }
    }
}
