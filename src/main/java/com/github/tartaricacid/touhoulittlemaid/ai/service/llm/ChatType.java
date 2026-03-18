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
     * 普通交谈
     */
    NORMAL_CHAT,
    /**
     * 多轮 Function Call
     */
    MULTI_FUNCTION_CALL
}
