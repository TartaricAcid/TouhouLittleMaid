package com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.ContextCategory;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.MaidContextRegister;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.ITool;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.response.ToolResponse;
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
        return "Load one category of live maid context by category id.";
    }

    @Override
    public Parameter parameters(ObjectParameter root, EntityMaid maid) {
        StringParameter categoryId = StringParameter.create();
        List<ContextCategory> categories = MaidContextRegister.getAllCategories();
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
    public ToolResponse onCall(String result, EntityMaid maid) {
        if (!MaidContextRegister.hasCategory(result)) {
            return new ToolResponse("Unknown maid context category: %s".formatted(result));
        }
        List<String> lines = MaidContextRegister.getContextDescriptionsByCategory(result, maid);
        if (lines.isEmpty()) {
            return new ToolResponse("No context is available for category: %s".formatted(result));
        }
        String summary = MaidContextRegister.getCategorySummary(result);
        String body = String.join("\n", lines);
        return new ToolResponse("""
                ## Maid Context Category: %s
                - %s
                %s
                """.formatted(result, summary, body));
    }

    private static String buildDescription(List<ContextCategory> categories) {
        String categoryList = categories.stream()
                .map(category -> "- %s: %s".formatted(category.id(), category.summary()))
                .collect(Collectors.joining("\n"));
        if (StringUtils.isBlank(categoryList)) {
            categoryList = "- No context categories are currently registered.";
        }
        return """
                category_id (string, required): The maid context category to load.
                Choose one category from the currently registered maid context categories.
                
                Available categories:
                %s
                """.formatted(categoryList);
    }
}
