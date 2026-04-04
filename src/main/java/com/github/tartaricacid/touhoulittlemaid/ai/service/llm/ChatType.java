package com.github.tartaricacid.touhoulittlemaid.ai.service.llm;

public enum ChatType {
    /**
     * 自动生成设定文件
     */
    AUTO_GEN_SETTING,
    /**
     * 历史对话摘要压缩，告诉 LLM 构建一个单独完整的上下文压缩历史记录
     */
    HISTORY_SUMMARY,
    /**
     * 知识库结果传递，告诉 LLM 构建一个单独的、完全洁净的上下文进行继续后续对话
     */
    GROUNDED_ANSWER_PASS,
    /**
     * 默认，普通的工具信息返回
     */
    NORMAL_CHAT;

    /**
     * 首次生成角色设定时、生成历史摘要、研读知识库等情况下不需要添加上下文和工具列表
     * <p>
     * LLM 只需要根据系统提示词进行单轮生成即可
     */
    public static boolean notNeedContextAndTools(ChatType chatType) {
        return chatType == ChatType.AUTO_GEN_SETTING
               || chatType == ChatType.HISTORY_SUMMARY
               || chatType == ChatType.GROUNDED_ANSWER_PASS;
    }
}
