package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.nbt.CompoundTag;

/**
 * 每女仆的「挖矿/伐木」工作配置，写进女仆的持久化 NBT，随存档走。
 * 没有选中具体女仆时，面板改的是这里的全局模板；起 Job 时女仆 NBT 未写过的项回落到模板。
 */
public final class WabWorkConfig {
    public static final String KEY_FILTER = "WabMineFilter";
    public static final String KEY_DROP_EXTRA = "WabDropExtra";
    public static final String KEY_DROP_BLOCK = "WabDropBlock";
    public static final String KEY_MAX_RADIUS = "WabMaxRadius";

    public static final String DEFAULT_DROP_BLOCK = "minecraft:cobblestone";
    public static final int DEFAULT_MAX_RADIUS = 24;
    public static final int MIN_RADIUS = 4;
    public static final int MAX_RADIUS = 96;

    /** 全局模板：无女仆选中时编辑，由 WabWorkSync(UUID=0) 写入。 */
    private static volatile String templateFilter = "";
    private static volatile boolean templateDropExtra = false;
    private static volatile String templateDropBlock = DEFAULT_DROP_BLOCK;
    private static volatile int templateMaxRadius = DEFAULT_MAX_RADIUS;
    private static volatile String templateAction = "mine";

    private WabWorkConfig() {
    }

    // ==================== 每女仆 NBT ====================

    public static String getFilter(EntityMaid maid) {
        CompoundTag tag = maid.getPersistentData();
        return tag.contains(KEY_FILTER) ? tag.getString(KEY_FILTER) : templateFilter;
    }

    public static void setFilter(EntityMaid maid, String value) {
        maid.getPersistentData().putString(KEY_FILTER, value == null ? "" : value);
    }

    public static boolean getDropExtra(EntityMaid maid) {
        CompoundTag tag = maid.getPersistentData();
        return tag.contains(KEY_DROP_EXTRA) ? tag.getBoolean(KEY_DROP_EXTRA) : templateDropExtra;
    }

    public static void setDropExtra(EntityMaid maid, boolean value) {
        maid.getPersistentData().putBoolean(KEY_DROP_EXTRA, value);
    }

    public static String getDropBlock(EntityMaid maid) {
        CompoundTag tag = maid.getPersistentData();
        String value = tag.contains(KEY_DROP_BLOCK) ? tag.getString(KEY_DROP_BLOCK) : templateDropBlock;
        return value == null || value.isEmpty() ? DEFAULT_DROP_BLOCK : value;
    }

    public static void setDropBlock(EntityMaid maid, String value) {
        maid.getPersistentData().putString(KEY_DROP_BLOCK,
                value == null || value.isEmpty() ? DEFAULT_DROP_BLOCK : value);
    }

    public static int getMaxRadius(EntityMaid maid) {
        CompoundTag tag = maid.getPersistentData();
        int value = tag.contains(KEY_MAX_RADIUS) ? tag.getInt(KEY_MAX_RADIUS) : templateMaxRadius;
        return clampRadius(value);
    }

    public static void setMaxRadius(EntityMaid maid, int value) {
        maid.getPersistentData().putInt(KEY_MAX_RADIUS, clampRadius(value));
    }

    public static int clampRadius(int value) {
        return Math.max(MIN_RADIUS, Math.min(MAX_RADIUS, value));
    }

    // ==================== 全局模板 ====================

    public static String getTemplateFilter() {
        return templateFilter;
    }

    public static boolean getTemplateDropExtra() {
        return templateDropExtra;
    }

    public static String getTemplateDropBlock() {
        return templateDropBlock;
    }

    public static int getTemplateMaxRadius() {
        return templateMaxRadius;
    }

    public static String getTemplateAction() {
        return templateAction;
    }

    public static void setTemplate(String action, String filter, boolean dropExtra, String dropBlock, int maxRadius) {
        templateAction = action == null ? "mine" : action;
        templateFilter = filter == null ? "" : filter;
        templateDropExtra = dropExtra;
        templateDropBlock = dropBlock == null || dropBlock.isEmpty() ? DEFAULT_DROP_BLOCK : dropBlock;
        templateMaxRadius = clampRadius(maxRadius);
    }
}
