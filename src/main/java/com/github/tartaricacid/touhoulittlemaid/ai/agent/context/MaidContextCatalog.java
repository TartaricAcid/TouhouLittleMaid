package com.github.tartaricacid.touhoulittlemaid.ai.agent.context;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

final class MaidContextCatalog {
    private final Map<String, IMaidContext> contexts = Maps.newLinkedHashMap();
    private final Map<String, ContextCategory> categories = Maps.newLinkedHashMap();

    void clear() {
        contexts.clear();
        categories.clear();
    }

    void registerCategory(String categoryId, String categorySummary) {
        if (categories.containsKey(categoryId)) {
            throw new IllegalArgumentException("Duplicate maid context category id: " + categoryId);
        }
        categories.put(categoryId, new ContextCategory(categoryId, categorySummary));
    }

    void registerContext(String categoryId, IMaidContext context) {
        if (contexts.containsKey(context.key())) {
            throw new IllegalArgumentException("Duplicate maid context key: " + context.key());
        }

        ContextCategory category = categories.get(categoryId);
        if (category == null) {
            throw new IllegalArgumentException("Unknown maid context category id: " + categoryId);
        }

        category.addContextKey(context.key());
        contexts.put(context.key(), context);
    }

    @Nullable
    IMaidContext getContext(String key) {
        return contexts.get(key);
    }

    String getContextValue(String key, EntityMaid maid) {
        IMaidContext context = contexts.get(key);
        if (context == null) {
            return StringUtils.EMPTY;
        }
        return context.getValue(maid);
    }

    List<String> getContextDescriptions(EntityMaid maid) {
        List<String> lines = Lists.newArrayList();
        for (IMaidContext context : contexts.values()) {
            String value = getContextValue(context.key(), maid);
            lines.add("- %s: %s".formatted(context.label(), value));
        }
        return lines;
    }

    List<ContextCategory> getAllCategories() {
        return categories.values().stream()
                .filter(ContextCategory::hasContexts)
                .toList();
    }

    boolean hasCategory(String categoryId) {
        return categories.containsKey(categoryId);
    }

    List<String> getContextDescriptionsByCategory(String categoryId, EntityMaid maid) {
        ContextCategory category = categories.get(categoryId);
        if (category == null) {
            return List.of();
        }
        List<String> lines = Lists.newArrayList();
        for (String key : category.contextKeys()) {
            IMaidContext context = contexts.get(key);
            if (context == null) {
                continue;
            }
            String value = getContextValue(context.key(), maid);
            lines.add("- %s: %s".formatted(context.label(), value));
        }
        return lines;
    }

    String getCategorySummary(String categoryId) {
        ContextCategory category = categories.get(categoryId);
        return category == null ? StringUtils.EMPTY : category.summary();
    }
}
