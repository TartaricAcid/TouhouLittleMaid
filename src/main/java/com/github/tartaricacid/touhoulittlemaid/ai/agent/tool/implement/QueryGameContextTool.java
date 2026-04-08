package com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.ContextCategory;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.GameContextRegister;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.ITool;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.LLMCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.ObjectParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.Parameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.StringParameter;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.stream.Collectors;

public class QueryGameContextTool implements ITool<String> {
    public static final String TOOL_ID = "query_game_context";
    private static final String CATEGORY_ID = "category_id";
    private static final Codec<String> CODEC = Codec.STRING.fieldOf(CATEGORY_ID).codec();

    @Override
    public String id() {
        return TOOL_ID;
    }

    @Override
    public String summary(EntityMaid maid) {
        return """
                Use this when you need authoritative live game context before another tool call or before answering a state-dependent question.
                Load exactly one context category by category_id, such as nearby entities, equipment, etc.
                """.trim();
    }

    @Override
    public Parameter parameters(ObjectParameter root, EntityMaid maid) {
        StringParameter categoryId = StringParameter.create();
        var categories = GameContextRegister.allToolCategories();
        categoryId.setDescription(buildDescription(categories));
        categories.stream().map(ContextCategory::id).forEach(categoryId::addEnumValues);
        root.addProperties(CATEGORY_ID, categoryId);
        return root;
    }

    @Override
    public Codec<String> codec() {
        return CODEC;
    }

    @Override
    public LLMCallback onCall(String toolId, String result, LLMCallback callback) {
        List<String> values = GameContextRegister.allToolCategories().stream().map(ContextCategory::id).toList();

        if (!values.contains(result)) {
            String text = "Unknown game context category '%s'".formatted(result);
            String invalided = ITool.invalidParam(CATEGORY_ID, values, text);
            return callback.addToolResult(invalided, toolId);
        }

        List<String> lines = GameContextRegister.getContext(result, callback.getMaid());
        if (lines.isEmpty()) {
            // 上面其实已经检查一次了，一般不会触发此处
            String text = "Category '%s' currently has no available context".formatted(result);
            String invalided = ITool.invalidParam(CATEGORY_ID, values, text);
            return callback.addToolResult(invalided, toolId);
        }

        String body = String.join("\n", lines);
        return callback.addToolResult(body, toolId);
    }

    @Override
    public Component invocationSummaryComponent(String result) {
        return Component.translatable("ai.touhou_little_maid.chat.tool_call.query_game_context", result)
                .withStyle(ChatFormatting.GRAY);
    }

    private static String buildDescription(List<ContextCategory> categories) {
        String categoryList = categories.stream()
                .map(category -> "- %s: %s".formatted(category.id(), category.summary()))
                .collect(Collectors.joining("\n"));
        return "Available categories: \n%s".formatted(categoryList);
    }
}
