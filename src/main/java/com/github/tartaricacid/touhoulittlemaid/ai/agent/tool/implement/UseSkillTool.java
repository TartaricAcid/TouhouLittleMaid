package com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.SkillInstance;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.SkillLoader;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.ITool;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.LLMCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.MaidAIChatManager;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.grounded.GroundedAnswerCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.ObjectParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.Parameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.StringParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMClient;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class UseSkillTool implements ITool<String> {
    public static final String TOOL_ID = "use_skill";
    private static final String NAME_PARAMETER_ID = "name";
    private static final String SUMMARY = """
            Load a skill to get detailed instructions for a specific task.
            Skills provide specialized knowledge and guidance. Use this when a task matches an available skill.
            """.trim();
    private static final String PARAMETER_DESC = """
            The exact skill name to load.
            Choose one of the available skill names exposed in this schema. Do not invent names.
            """.trim();

    private static final Codec<String> CODEC = Codec.STRING.fieldOf(NAME_PARAMETER_ID).codec();

    @Override
    public String id() {
        return TOOL_ID;
    }

    @Override
    public String summary(EntityMaid maid) {
        return SUMMARY;
    }

    @Override
    public Parameter parameters(ObjectParameter root, EntityMaid maid) {
        Map<String, SkillInstance> skills = SkillLoader.getAllSkills();
        StringParameter skillId = StringParameter.create();
        skillId.setDescription(PARAMETER_DESC);
        if (!skills.isEmpty()) {
            skillId.addEnumValues(skills.keySet().toArray(String[]::new));
        }
        root.addProperties(NAME_PARAMETER_ID, skillId);
        return root;
    }

    @Override
    public Codec<String> codec() {
        return CODEC;
    }

    @Override
    public LLMCallback onCall(String toolId, String result, LLMCallback callback) {
        // 不会触发这个同步回调，因为 onCallAsync 已经覆盖了它
        return callback;
    }

    @Override
    public CompletableFuture<LLMCallback> onCallAsync(String toolCallId, String result, LLMCallback callback, LLMClient client) {
        SkillInstance selected = SkillLoader.getSkill(result);

        if (selected == null) {
            List<String> values = Lists.newArrayList(SkillLoader.getAllSkills().keySet());
            String text = "Unknown skill name '%s'".formatted(result);
            String invalidMsg = ITool.invalidParam(NAME_PARAMETER_ID, values, text);
            LLMCallback toolResult = callback.addToolResult(invalidMsg, toolCallId);
            return CompletableFuture.completedFuture(toolResult);
        }

        // 如果是知识库查询，那么需要新建空白回调，并异步阻塞等待
        if (selected.isKnowledgeType()) {
            long bubbleId = callback.getWaitingChatBubbleId();
            MaidAIChatManager chatManager = callback.getChatManager();
            String knowledge = this.getKnowledge(selected, chatManager);

            CompletableFuture<String> summaryFuture = new CompletableFuture<>();
            GroundedAnswerCallback groundedAnswerCallback = new GroundedAnswerCallback(chatManager, knowledge, bubbleId, summaryFuture);
            client.chat(groundedAnswerCallback);

            return summaryFuture.orTimeout(10, TimeUnit.SECONDS)
                    // 使用 handleAsync 确保无论成功还是失败
                    // 最终都能在主线程上回调结果给主 Agent
                    .handleAsync((summary, throwable) -> {
                        CompletableFuture<LLMCallback> finalResult = new CompletableFuture<>();
                        // 最终返回工具结果，必须在主线程上
                        callback.runOnServerThread(() -> {
                            if (throwable != null) {
                                String error = "Error: " + throwable.getMessage();
                                finalResult.complete(callback.addToolResult(error, toolCallId));
                            } else {
                                // 将子 Agent 提取的知识回填给主 Agent
                                String handleSummary = "Source: Minecraft Wiki\nSummary: " + summary;
                                finalResult.complete(callback.addToolResult(handleSummary, toolCallId));
                            }
                        });
                        return finalResult;
                    }, Runnable::run).thenCompose(f -> f);
        }

        // 普通 skill 回调
        String body = selected.body().trim();
        LLMCallback toolResult = callback.addToolResult(body, toolCallId);
        return CompletableFuture.completedFuture(toolResult);
    }

    @Override
    public String invocationSummary(String result) {
        return "%s { %s }".formatted(TOOL_ID, result);
    }

    private String getKnowledge(SkillInstance selected, MaidAIChatManager chatManager) {
        String knowledge = selected.body();

        // 把 en-US 这样的改成 en_us 这样的规范格式
        String langKey = chatManager.getChatLanguage()
                .replace('-', '_')
                .toLowerCase(Locale.ENGLISH);

        // 检查语言是否存在
        var fileName = langKey + ".md";
        var references = selected.references();
        if (references.containsKey(fileName)) {
            knowledge += references.get(fileName);
        } else if (references.containsKey("en_us.md")) {
            knowledge += references.get("en_us.md");
        }

        return knowledge;
    }
}
