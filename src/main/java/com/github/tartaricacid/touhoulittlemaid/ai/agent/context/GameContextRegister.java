package com.github.tartaricacid.touhoulittlemaid.ai.agent.context;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.builtin.*;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class GameContextRegister {
    private static Map<String, IMaidContext> CONTEXTS = Maps.newLinkedHashMap();
    private static Map<String, ContextCategory> CATEGORIES = Maps.newLinkedHashMap();

    public static void init() {
        CONTEXTS.clear();
        CATEGORIES.clear();

        GameContextRegister register = new GameContextRegister();

        WorldContexts.registerAll(register);
        EquipmentMaidContexts.registerAll(register);
        UserContexts.registerAll(register);
        StatusMaidContexts.registerAll(register);
        BehaviorMaidContexts.registerAll(register);
        PositionMaidContexts.registerAll(register);
        NearbyEntityMaidContexts.registerAll(register);

        for (ILittleMaid littleMaid : TouhouLittleMaid.EXTENSIONS) {
            littleMaid.registerAIMaidContext(register);
        }

        CONTEXTS = ImmutableMap.copyOf(CONTEXTS);
        CATEGORIES = ImmutableMap.copyOf(CATEGORIES);
    }

    /**
     * 注册一个上下文分类。
     * <p>
     * 分类应尽量保持语义稳定，便于模型通过 tool 只加载当前所需的一类上下文，
     * 从而减少 token 占用并提升上下文选择的可预测性。
     *
     * @param categoryId      分类唯一标识，如 {@code world}、{@code equipment}、{@code owner}
     * @param categorySummary 分类摘要，会展示给模型用于选择分类
     */
    public void registerCategory(String categoryId, String categorySummary) {
        CATEGORIES.put(categoryId, new ContextCategory(categoryId, categorySummary));
    }

    /**
     * 将一个上下文项注册到已存在的分类中。
     * <p>
     * 调用前必须先通过 {@link #registerCategory(String, String)} 注册分类，或者使用已经注册过的分类
     * 否则会抛出异常。没有上下文项的分类不会出现在 maid_context skill 提供给模型的可选分类列表中。
     *
     * @param categoryId 分类唯一标识
     * @param context    上下文项
     */
    public void registerContext(String categoryId, IMaidContext context) {
        if (CONTEXTS.containsKey(context.key())) {
            throw new IllegalArgumentException("Duplicate maid context key: " + context.key());
        }

        ContextCategory category = CATEGORIES.get(categoryId);
        if (category == null) {
            throw new IllegalArgumentException("Unknown maid context category id: " + categoryId);
        }

        category.addContextKey(context.key());
        CONTEXTS.put(context.key(), context);
    }

    public static boolean hasCategory(String categoryId) {
        return CATEGORIES.containsKey(categoryId);
    }

    public static List<ContextCategory> allCategories() {
        return List.copyOf(CATEGORIES.values());
    }

    public static List<String> getContext(String categoryId, EntityMaid maid) {
        if (!CATEGORIES.containsKey(categoryId)) {
            return Collections.emptyList();
        }
        ContextCategory category = CATEGORIES.get(categoryId);
        return category.contextKeys().stream().map(key -> {
            IMaidContext context = CONTEXTS.get(key);
            if (context == null) {
                return null;
            }
            return "- %s: %s".formatted(context.label(), context.getValue(maid));
        }).filter(StringUtils::isNotBlank).toList();
    }
}
