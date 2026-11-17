package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.nbt.CompoundTag;

/**
 * 女仆「挖矿 / 伐木」工作模式的参数配置。
 * <p>
 * 选中了具体女仆时参数写进她的 {@code getPersistentData()}（随存档走、服务端权威）；
 * 没有选中女仆（空魂符对着空地、设置页、暂停页打开面板）时写到本类里的「全局模板」，
 * 之后每台女仆起 job 时都会套用这份模板。
 */
public final class WabWorkConfig {
    // 女仆 NBT key（契约约定，跨版本保持一致）
    public static final String KEY_FILTER = "WabMineFilter";
    public static final String KEY_DROP_EXTRA = "WabDropExtra";
    public static final String KEY_DROP_BLOCK = "WabDropBlock";
    public static final String KEY_MAX_RADIUS = "WabMaxRadius";

    public static final String DEFAULT_FILTER = "";
    public static final boolean DEFAULT_DROP_EXTRA = false;
    public static final String DEFAULT_DROP_BLOCK = "minecraft:cobblestone";
    public static final int DEFAULT_MAX_RADIUS = 24;
    public static final int MIN_RADIUS = 4;
    public static final int MAX_RADIUS = 96;

    // 全局模板（服务端内存态）。玩家在没有选中女仆时保存的参数落到这里。
    private static String globalFilter = DEFAULT_FILTER;
    private static boolean globalDropExtra = DEFAULT_DROP_EXTRA;
    private static String globalDropBlock = DEFAULT_DROP_BLOCK;
    private static int globalMaxRadius = DEFAULT_MAX_RADIUS;

    private WabWorkConfig() {
    }

    /** 一组最终生效的工作参数。 */
    public record Settings(String filter, boolean dropExtra, String dropBlock, int maxRadius) {
    }

    // ==================== 女仆 NBT ====================

    public static String getFilter(EntityMaid maid) {
        return sanitizeFilter(maid.getPersistentData().getString(KEY_FILTER));
    }

    public static void setFilter(EntityMaid maid, String value) {
        maid.getPersistentData().putString(KEY_FILTER, sanitizeFilter(value));
    }

    public static boolean getDropExtra(EntityMaid maid) {
        return maid.getPersistentData().getBoolean(KEY_DROP_EXTRA);
    }

    public static void setDropExtra(EntityMaid maid, boolean value) {
        maid.getPersistentData().putBoolean(KEY_DROP_EXTRA, value);
    }

    public static String getDropBlock(EntityMaid maid) {
        return sanitizeBlock(maid.getPersistentData().getString(KEY_DROP_BLOCK));
    }

    public static void setDropBlock(EntityMaid maid, String value) {
        maid.getPersistentData().putString(KEY_DROP_BLOCK, sanitizeBlock(value));
    }

    public static int getMaxRadius(EntityMaid maid) {
        return clampRadius(maid.getPersistentData().getInt(KEY_MAX_RADIUS));
    }

    public static void setMaxRadius(EntityMaid maid, int value) {
        maid.getPersistentData().putInt(KEY_MAX_RADIUS, clampRadius(value));
    }

    public static void apply(EntityMaid maid, String filter, boolean dropExtra, String dropBlock, int maxRadius) {
        setFilter(maid, filter);
        setDropExtra(maid, dropExtra);
        setDropBlock(maid, dropBlock);
        setMaxRadius(maid, maxRadius);
    }

    // ==================== 全局模板 ====================

    public static String getGlobalFilter() {
        return globalFilter;
    }

    public static boolean getGlobalDropExtra() {
        return globalDropExtra;
    }

    public static String getGlobalDropBlock() {
        return globalDropBlock;
    }

    public static int getGlobalMaxRadius() {
        return globalMaxRadius;
    }

    public static void setGlobal(String filter, boolean dropExtra, String dropBlock, int maxRadius) {
        globalFilter = sanitizeFilter(filter);
        globalDropExtra = dropExtra;
        globalDropBlock = sanitizeBlock(dropBlock);
        globalMaxRadius = clampRadius(maxRadius);
    }

    /** 取某个女仆最终生效的设置；maid 为 null 时用全局模板。 */
    public static Settings of(EntityMaid maid) {
        if (maid == null) {
            return new Settings(globalFilter, globalDropExtra, globalDropBlock, globalMaxRadius);
        }
        // 女仆没单独配过半径时退回全局模板，这样「设置页改一次，所有女仆都生效」。
        CompoundTag data = maid.getPersistentData();
        int radius = data.contains(KEY_MAX_RADIUS) ? clampRadius(data.getInt(KEY_MAX_RADIUS)) : globalMaxRadius;
        String filter = data.contains(KEY_FILTER) ? sanitizeFilter(data.getString(KEY_FILTER)) : globalFilter;
        String block = data.contains(KEY_DROP_BLOCK) ? sanitizeBlock(data.getString(KEY_DROP_BLOCK)) : globalDropBlock;
        boolean drop = data.contains(KEY_DROP_EXTRA) ? data.getBoolean(KEY_DROP_EXTRA) : globalDropExtra;
        return new Settings(filter, drop, block, radius);
    }

    public static int clampRadius(int value) {
        if (value < MIN_RADIUS) return MIN_RADIUS;
        if (value > MAX_RADIUS) return MAX_RADIUS;
        return value;
    }

    private static String sanitizeFilter(String value) {
        return value == null ? "" : value.trim();
    }

    private static String sanitizeBlock(String value) {
        if (value == null || value.trim().isEmpty()) return DEFAULT_DROP_BLOCK;
        return value.trim();
    }
}
