package com.github.tartaricacid.touhoulittlemaid.api;

import com.github.tartaricacid.touhoulittlemaid.block.multiblock.MultiBlockManager;
import com.github.tartaricacid.touhoulittlemaid.client.overlay.MaidTipsOverlay;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.GeckoEntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.ExtraMaidBrainManager;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.BackpackManager;
import com.github.tartaricacid.touhoulittlemaid.entity.data.TaskDataRegister;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.tartaricacid.touhoulittlemaid.entity.task.meal.MaidMealManager;
import com.github.tartaricacid.touhoulittlemaid.inventory.chest.ChestManager;
import com.github.tartaricacid.touhoulittlemaid.item.bauble.BaubleManager;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface ILittleMaid {
    /**
     * 为物品绑定女仆饰品属性
     *
     * @param manager 注册器
     */
    default void bindMaidBauble(BaubleManager manager) {
    }

    /**
     * 添加女仆的 Task
     *
     * @param manager 注册器
     */
    default void addMaidTask(TaskManager manager) {
    }

    /**
     * 添加女仆的背包
     *
     * @param manager 注册器
     */
    default void addMaidBackpack(BackpackManager manager) {
    }

    /**
     * 添加多方块结构
     *
     * @param manager 注册器
     */
    default void addMultiBlock(MultiBlockManager manager) {
    }

    /**
     * 添加箱子类型，用于隙间饰品的箱子识别
     *
     * @param manager 注册器
     */
    default void addChestType(ChestManager manager) {
    }

    /**
     * 添加女仆饭类型
     *
     * @param manager 注册器
     */
    default void addMaidMeal(MaidMealManager manager) {
    }

    /**
     * 注册任务数据，任务数据是一种可以自定义添加到女仆上的数据
     *
     * @param register 注册器
     */
    default void registerTaskData(TaskDataRegister register) {
    }

    /**
     * 给女仆添加额外的 AI 数据，比如 MemoryModuleType 或者 SensorType
     *
     * @param manager 注册器
     */
    default void addExtraMaidBrain(ExtraMaidBrainManager manager) {
    }

    /**
     * 添加女仆相关提示
     * <p>
     * 有些物品在指向女仆时，能够在屏幕上显示相关提示文本
     */
    @OnlyIn(Dist.CLIENT)
    default void addMaidTips(MaidTipsOverlay maidTipsOverlay) {
    }

    /**
     * 添加默认模型风格的实体 layer 渲染
     */
    @OnlyIn(Dist.CLIENT)
    default void addAdditionMaidLayer(EntityMaidRenderer renderer, EntityRendererProvider.Context context) {
    }

    /**
     * 添加 Gecko 风格的实体 layer 渲染
     */
    @OnlyIn(Dist.CLIENT)
    default void addAdditionGeckoMaidLayer(GeckoEntityMaidRenderer<? extends Mob> renderer, EntityRendererProvider.Context context) {
    }
}
