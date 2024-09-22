package com.github.tartaricacid.touhoulittlemaid.api.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.AbstractMaidContainer;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.task.DefaultMaidTaskConfigContainer;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public interface IMaidTask {
    /**
     * 模式 ID，用于后续模式的判断，也用于本地化的 key
     *
     * @return 用 ResourceLocation 类描述的模式 ID
     */
    ResourceLocation getUid();

    /**
     * 该模式的图标
     *
     * @return 用 ItemStack 类描述的图标
     */
    ItemStack getIcon();

    /**
     * 该模式的环境音效
     *
     * @param maid 女仆对象
     * @return 对应的环境音效
     */
    @Nullable
    SoundEvent getAmbientSound(EntityMaid maid);

    /**
     * 该模式下调用的 AI
     *
     * @param maid 女仆对象
     * @return 如果什么都不做，请返回空集合
     */
    List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid);

    /**
     * 当前 Task 是否可使用
     *
     * @param maid 女仆对象
     * @return 当前 Task 是否可以用
     */
    default boolean isEnable(EntityMaid maid) {
        return true;
    }

    /**
     * 默认情况下，女仆在工作模式启用时，会随机的走动，或者四处张望
     * <p>
     * 但是有些需要专心致志的工作模式，这样做反而会带来问题。将其设置为 false 就能禁止这种情况
     *
     * @param maid 女仆对象
     * @return 禁用四处张望和随机走动 AI
     */
    default boolean enableLookAndRandomWalk(EntityMaid maid) {
        return true;
    }

    /**
     * 获取任务启用的条件提示文本
     *
     * @param maid 女仆对象
     * @return 条件名（用于自动生成对应的 key）和对应条件布尔值的组合列表
     */
    default List<Pair<String, Predicate<EntityMaid>>> getEnableConditionDesc(EntityMaid maid) {
        return Collections.emptyList();
    }

    /**
     * 获取当前模式名称
     *
     * @return 当前模式名称
     */
    default MutableComponent getName() {
        return Component.translatable(String.format("task.%s.%s", getUid().getNamespace(), getUid().getPath()));
    }

    /**
     * 获取额外的条件提示文本
     *
     * @param maid 女仆对象
     * @return 条件名（用于自动生成对应的 key）和对应条件布尔值的组合列表
     */
    default List<Pair<String, Predicate<EntityMaid>>> getConditionDescription(EntityMaid maid) {
        return Collections.emptyList();
    }

    /**
     * 获取当前模式的描述文本的 key
     *
     * @param maid 女仆对象
     * @return 模式的描述文本，可以多行<br>如果没有文本描述，请返回空集合
     */
    default List<String> getDescription(EntityMaid maid) {
        String key = String.format("task.%s.%s.desc", getUid().getNamespace(), getUid().getPath());
        return Lists.newArrayList(key);
    }

    /**
     * 获取女仆当前任务配置的界面
     *
     * @param maid 女仆对象
     * @return MenuProvider
     */
    default MenuProvider getTaskConfigGuiProvider(EntityMaid maid) {
        final int entityId = maid.getId();
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.literal("Maid Task Config Container");
            }

            @Override
            public AbstractMaidContainer createMenu(int index, Inventory playerInventory, Player player) {
                return new DefaultMaidTaskConfigContainer(index, playerInventory, entityId);
            }
        };
    }

    /**
     * 获取女仆当前任务信息的界面
     *
     * @param maid 女仆对象
     * @return MenuProvider
     */
    default MenuProvider getTaskInfoGuiProvider(EntityMaid maid) {
        return maid.getMaidBackpackType().getGuiProvider(maid.getId());
    }
}
