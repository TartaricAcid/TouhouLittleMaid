package com.github.tartaricacid.touhoulittlemaid.ai.agent.context;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.builtin.*;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;

import javax.annotation.Nullable;
import java.util.List;

public final class MaidContextRegister {
    private static final MaidContextCatalog CATALOG = new MaidContextCatalog();

    public static void init() {
        CATALOG.clear();
        MaidContextRegister register = new MaidContextRegister();

        WorldMaidContexts.registerAll(register);
        EquipmentMaidContexts.registerAll(register);
        OwnerMaidContexts.registerAll(register);
        StatusMaidContexts.registerAll(register);
        BehaviorMaidContexts.registerAll(register);
        PositionMaidContexts.registerAll(register);
        NearbyEntityMaidContexts.registerAll(register);

        for (ILittleMaid littleMaid : TouhouLittleMaid.EXTENSIONS) {
            littleMaid.registerAIMaidContext(register);
        }
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
        CATALOG.registerCategory(categoryId, categorySummary);
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
        CATALOG.registerContext(categoryId, context);
    }

    @Nullable
    public static IMaidContext getContext(String key) {
        return CATALOG.getContext(key);
    }

    public static String getContextValue(String key, EntityMaid maid) {
        return CATALOG.getContextValue(key, maid);
    }

    public static List<String> getContextDescriptions(EntityMaid maid) {
        return CATALOG.getContextDescriptions(maid);
    }

    public static List<ContextCategory> getAllCategories() {
        return CATALOG.getAllCategories();
    }

    public static boolean hasCategory(String categoryId) {
        return CATALOG.hasCategory(categoryId);
    }

    public static List<String> getContextDescriptionsByCategory(String categoryId, EntityMaid maid) {
        return CATALOG.getContextDescriptionsByCategory(categoryId, maid);
    }

    public static String getCategorySummary(String categoryId) {
        return CATALOG.getCategorySummary(categoryId);
    }
}
