package com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.summary;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.response.ResponseChat;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ResponseCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMMessage;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.net.http.HttpRequest;
import java.util.List;

public class HistorySummaryCallback implements ResponseCallback<ResponseChat> {
    private final HistorySummaryManager manager;
    private final List<LLMMessage> snapshot;

    public HistorySummaryCallback(HistorySummaryManager manager, List<LLMMessage> snapshot) {
        this.manager = manager;
        this.snapshot = List.copyOf(snapshot);
    }

    @Override
    public void onFailure(@Nullable HttpRequest request, Throwable throwable, int errorCode) {
        manager.stopHistorySummary();
        TouhouLittleMaid.LOGGER.error("Failed to compact maid AI history summary, error code is {}, cause is {}", errorCode, throwable.getMessage());
    }

    @Override
    public void onSuccess(ResponseChat response) {
        String summary = response.getChatText();
        if (StringUtils.isBlank(summary)) {
            manager.stopHistorySummary();
            return;
        }
        manager.completeHistorySummary(summary, snapshot);
    }
}
