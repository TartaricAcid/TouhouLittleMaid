package com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.ContextCategory;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.MaidContextRegister;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.ITool;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.LLMCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.ObjectParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.Parameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.StringParameter;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.serialization.Codec;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

public class QueryMaidContextTool implements ITool<String> {
    public static final String TOOL_ID = "query_maid_context";
    private static final String CATEGORY_ID = "category_id";
    private static final Codec<String> CODEC = Codec.STRING.fieldOf(CATEGORY_ID).codec();

    @Override
    public String id() {
        return TOOL_ID;
    }

    @Override
    public String summary(EntityMaid maid) {
        return "Load one maid context category by category id.";
    }

    @Override
    public Parameter parameters(ObjectParameter root, EntityMaid maid) {
        StringParameter categoryId = StringParameter.create();
        List<ContextCategory> categories = getAvailableCategories();
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
        List<String> values = getAvailableCategories().stream()
                .map(ContextCategory::id).toList();

        if (!MaidContextRegister.hasCategory(result)) {
            String text = "unknown maid context category '%s'".formatted(result);
            String invalided = ITool.invalidParam(CATEGORY_ID, values, text);
            return callback.addToolResult(invalided, toolId);
        }

        List<String> lines = MaidContextRegister.getContextDescriptionsByCategory(result, callback.getMaid());
        if (lines.isEmpty()) {
            // 上面其实已经检查一次了，一般不会触发此处
            String text = "category '%s' currently has no available context".formatted(result);
            String invalided = ITool.invalidParam(CATEGORY_ID, values, text);
            return callback.addToolResult(invalided, toolId);
        }

        String summary = MaidContextRegister.getCategorySummary(result);
        String body = String.join("\n", lines);
        return callback.addToolResult("""
                ## Maid Context Category: %s
                - %s
                %s
                """.formatted(result, summary, body), toolId);
    }

    private static String buildDescription(List<ContextCategory> categories) {
        String categoryList = categories.stream()
                .map(category -> "- %s: %s".formatted(category.id(), category.summary()))
                .collect(Collectors.joining("\n"));
        if (StringUtils.isBlank(categoryList)) {
            categoryList = "- No context categories";
        }
        return """
                category_id (string, required): The maid context category to load.
                Available categories:
                %s
                """.formatted(categoryList);
    }

    private static List<ContextCategory> getAvailableCategories() {
        return MaidContextRegister.getAllCategories();
    }
}
