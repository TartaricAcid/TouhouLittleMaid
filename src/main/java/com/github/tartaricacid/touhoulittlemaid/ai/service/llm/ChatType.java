package com.github.tartaricacid.touhoulittlemaid.ai.service.llm;

public enum ChatType {
    /**
     * 自动生成设定文件
     */
    AUTO_GEN_SETTING,
    /**
     * 历史对话摘要压缩
     */
    HISTORY_SUMMARY,
    /**
     * 基于已解析知识的二次回答流程
     */
    GROUNDED_ANSWER_PASS,
    /**
     * 普通交谈
     */
    NORMAL_CHAT,
    /**
     * 多轮 Function Call
     */
    MULTI_FUNCTION_CALL;

    /**
     * 首次生成角色设定时、生成历史摘要、研读知识库d等情况下不需要添加 skill
     */
    public static boolean notNeedSkill(ChatType chatType) {
        return chatType == ChatType.AUTO_GEN_SETTING
               || chatType == ChatType.HISTORY_SUMMARY
               || chatType == ChatType.GROUNDED_ANSWER_PASS;
    }
}
