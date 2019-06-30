package com.github.tartaricacid.touhoulittlemaid.entity.passive;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public enum MaidMode {
    IDLE("idle", 0, Items.FEATHER), // 空闲模式：除了跟随玩家什么都不做
    ATTACK("attack", 1, Items.DIAMOND_SWORD), // 近战攻击模式
    RANGE_ATTACK("range_attack", 2, Items.BOW), // 远程攻击模式
    DANMAKU_ATTACK("danmaku_attack", 3, Items.ENDER_PEARL), // 弹幕攻击：吃我梦想封印
    FARM("farm", 4, Items.IRON_HOE), // 农夫模式：还是种地适合老子
    BREW("brew", 5, Items.BREWING_STAND), // 酿造模式：永远亭技术加成，妙手回春
    FURNACE("furnace", 6, ItemBlock.getItemFromBlock(Blocks.FURNACE)), // 熔炉模式：此时会烧制物品
    FEED("feed", 7, Items.COOKED_BEEF), // 喂养模式：来，喂两位公子吃饼
    SHEARS("shears", 8, Items.SHEARS), // 剪羊毛模式
    TORCH("torch", 9, ItemBlock.getItemFromBlock(Blocks.TORCH)); // 插火把模式

    private String name;
    private int modeIndex;
    private Item itemIcon;

    /**
     * 构造女仆所属模式的枚举类型
     *
     * @param name      所属模式的名称
     * @param modeIndex 所属模式的数字类型索引，用于数据存储
     * @param itemIcon  所属模式到时候显示的图标
     */
    MaidMode(String name, int modeIndex, Item itemIcon) {
        this.name = name;
        this.modeIndex = modeIndex;
        this.itemIcon = itemIcon;
    }

    public static MaidMode getMode(int modeIndex) {
        for (MaidMode maidMode : MaidMode.values()) {
            if (modeIndex == maidMode.getModeIndex()) {
                return maidMode;
            }
        }
        return IDLE;
    }

    public static int getLength() {
        return MaidMode.values().length;
    }

    public String getName() {
        return name;
    }

    public int getModeIndex() {
        return modeIndex;
    }

    public Item getItemIcon() {
        return itemIcon;
    }
}
