package com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.summary;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.MaidAIChatManager;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMMessage;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMSite;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.AIConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import net.minecraft.Util;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;

import static com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.summary.HistorySummaryPrompts.*;

public class HistorySummaryManager {
    private static final int SUMMARY_KEEP_RECENT_COUNT = 32;

    private final MaidAIChatManager chatManager;

    public HistorySummaryManager(MaidAIChatManager chatManager) {
        this.chatManager = chatManager;
    }

    public void appendSummaryMessage(List<LLMMessage> chatList) {
        if (chatManager.hasCompressedSummary()) {
            String message = formatSummarySystemMessage(chatManager);
            LLMMessage llmMessage = LLMMessage.systemChat(chatManager.getMaid(), message);
            chatList.add(llmMessage);
        }
    }

    /**
     * 异步摘要请求完成后的回调入口，负责校验、存储摘要并清理已压缩的旧消息。
     * <p>
     * 整体流程：
     * <ol>
     *   <li>校验摘要内容是否有效（非空白）</li>
     *   <li>校验历史尾部是否仍与发起请求时的快照一致（防止异步期间历史被修改）</li>
     *   <li>存储摘要，并从历史中删除已被压缩的旧消息</li>
     * </ol>
     *
     * @param summary  LLM 返回的摘要文本
     * @param snapshot 发起请求时通过 {@link #snapshotOldestMessages} 取得的快照
     */
    public boolean completeHistorySummary(String summary, List<LLMMessage> snapshot) {
        if (!chatManager.historySummaryRunning) {
            return false;
        }

        // 清理格式：防 null、去首尾空白、将 3 个以上连续换行压缩为 2 个
        String normalized = StringUtils.defaultString(summary).trim()
                .replaceAll("\n{3,}", "\n\n");

        // LLM 返回了空白内容，放弃本轮压缩
        if (StringUtils.isBlank(normalized)) {
            this.stopHistorySummary();
            return false;
        }

        // 异步期间历史可能已变化（玩家继续聊天、其他逻辑删除了消息等）
        // 如果尾部不再匹配，说明这份摘要对应的原始消息已经不在预期位置
        // 丢弃本次结果，避免用过期快照删除了错误的记录
        if (!this.matchesTailSnapshot(snapshot)) {
            this.stopHistorySummary();
            return false;
        }

        // 截断过长的摘要，然后保存
        String abbreviate = StringUtils.abbreviate(normalized, MAX_SUMMARY_LENGTH);
        chatManager.setCompressedSummary(abbreviate);

        // 从历史队列尾部（最旧端）逐条删除已被压缩进摘要的消息
        for (int i = 0; i < snapshot.size(); i++) {
            chatManager.getHistory().getDeque().pollLast();
        }

        this.stopHistorySummary();
        chatManager.setLastChatTokenUsage(0);
        return true;
    }

    public void stopHistorySummary() {
        chatManager.historySummaryRunning = false;
    }

    /**
     * 在玩家发起新聊天前，按上一次聊天请求返回的 token 用量判断是否需要压缩历史。
     * <p>
     * 仅在以下条件全部满足时才会实际发起请求：
     * <ul>
     *   <li>当前没有正在进行的摘要任务</li>
     *   <li>可压缩的消息数 ≥ {@link HistorySummaryPrompts#MIN_MESSAGES_TO_COMPRESS}</li>
     *   <li>上一次普通聊天请求返回的 token 用量达到配置上限</li>
     *   <li>LLM 站点和模型配置有效</li>
     * </ul>
     */
    public boolean tryCompressBeforeChat(Runnable afterSummary) {
        if (chatManager.getLastChatTokenUsage() < AIConfig.getMaidHistoryCompressTokenLimit()) {
            return false;
        }
        return this.tryScheduleHistorySummary(afterSummary);
    }

    private boolean tryScheduleHistorySummary(Runnable afterSummary) {
        // 已有摘要任务在跑时，不要吞掉当前玩家消息；直接按未压缩上下文继续本次聊天。
        if (chatManager.historySummaryRunning) {
            return false;
        }

        // 可压缩消息数不够，暂不压缩
        int compressCount = this.getCompressibleMessageCount();
        if (compressCount < MIN_MESSAGES_TO_COMPRESS) {
            return false;
        }

        // LLM 配置无效则跳过
        @Nullable LLMSite site = chatManager.getLLMSite();
        if (site == null || !site.enabled()) {
            return false;
        }
        String model = chatManager.getLLMModel();
        if (StringUtils.isBlank(model)) {
            return false;
        }

        // 从队列尾部（最旧端）取出需要压缩的消息快照
        List<LLMMessage> snapshot = this.snapshotOldestMessages(compressCount);
        if (snapshot.isEmpty()) {
            return false;
        }

        // 标记正在进行摘要，后续调用会被上面的守卫拦住
        chatManager.historySummaryRunning = true;

        // 构造摘要请求并异步发送，回调会调用 completeHistorySummary 处理结果
        EntityMaid maid = chatManager.getMaid();
        List<LLMMessage> messages = Util.make(Lists.newArrayList(), msg -> {
            msg.add(LLMMessage.systemChat(maid, SUMMARY_SYSTEM_PROMPT));
            msg.add(LLMMessage.userChat(maid, this.buildSummaryRequest(snapshot)));
        });

        // 进行通信压缩上下文
        HistorySummaryCallback callback = new HistorySummaryCallback(this.chatManager, messages, snapshot, afterSummary);
        site.client().chat(callback);
        return true;
    }

    /**
     * 构建发送给 LLM 的历史摘要请求。
     * <p>
     * 将现有的压缩摘要和需要压缩的历史消息快照组合成一个请求文本，
     * 格式如下：
     * <pre>
     * Existing summary:
     * [之前的摘要内容，如果没有则为 "[NONE]"]
     *
     * Older messages to compress:
     * [USER] 玩家发送的消息
     * [ASSISTANT] 女仆的回复文本
     * [ASSISTANT_TOOL_CALL] 工具调用名称列表
     * [TOOL] 工具返回的结果（截断至300字符）
     * [SYSTEM] 系统消息（截断至300字符）
     * ...
     * </pre>
     *
     * @param snapshot 需要压缩的历史消息快照列表
     * @return 格式化后的摘要请求文本
     */
    private String buildSummaryRequest(List<LLMMessage> snapshot) {
        String summary = chatManager.hasCompressedSummary() ? chatManager.getCompressedSummary() : "[NONE]";

        StringJoiner joiner = new StringJoiner("\n");
        for (LLMMessage message : snapshot) {
            joiner.add(buildSummaryEntry(message));
        }
        String body = joiner.length() > 0 ? joiner.toString() : "[NONE]";

        return HistorySummaryPrompts.buildSummaryRequest(summary, body);
    }

    /**
     * 摘要压缩时需要保留的最近消息数量（不会被压缩掉的部分）。
     *
     * @return 保留的最近消息条数
     */
    private int getSummaryKeepRecentCount() {
        return SUMMARY_KEEP_RECENT_COUNT;
    }

    /**
     * 当前可以被压缩进摘要的消息数量，即总历史记录减去需要保留的最近消息。
     * <p>
     * 例如总共 12 条记录、保留 8 条时，返回 4（最旧的 4 条可以压缩）。
     *
     * @return 可压缩的消息条数，最小为 0
     */
    private int getCompressibleMessageCount() {
        int triggerSize = this.getSummaryKeepRecentCount();
        int maxHistory = chatManager.getHistory().size();
        return Math.max(0, maxHistory - triggerSize);
    }

    /**
     * 从历史记录的尾部（最旧一端）取出最多 {@code count} 条消息作为快照。
     *
     * @param count 需要取出的消息数量
     * @return 最旧的消息列表，顺序与 {@link java.util.Deque#descendingIterator()} 一致
     */
    private List<LLMMessage> snapshotOldestMessages(int count) {
        List<LLMMessage> result = Lists.newArrayList();
        Iterator<LLMMessage> iterator = chatManager.getHistory().getDeque().descendingIterator();
        while (iterator.hasNext() && result.size() < count) {
            result.add(iterator.next());
        }
        return result;
    }

    /**
     * 检查当前历史记录的尾部（最旧一端）是否仍与给定的快照一致。
     * <p>
     * 因为摘要请求是异步的，返回结果前历史记录可能已经发生变化，
     * 用此方法验证快照是否仍然有效，避免用过期快照删除了错误的记录。
     *
     * @param snapshot 之前通过 {@link #snapshotOldestMessages} 取得的快照
     * @return 尾部仍然匹配返回 {@code true}，否则返回 {@code false}
     */
    private boolean matchesTailSnapshot(List<LLMMessage> snapshot) {
        if (snapshot.isEmpty() || chatManager.getHistory().size() < snapshot.size()) {
            return false;
        }
        Iterator<LLMMessage> iterator = chatManager.getHistory().getDeque().descendingIterator();
        for (LLMMessage expected : snapshot) {
            if (!iterator.hasNext()) {
                return false;
            }
            LLMMessage current = iterator.next();
            if (!expected.equals(current)) {
                return false;
            }
        }
        return true;
    }
}
