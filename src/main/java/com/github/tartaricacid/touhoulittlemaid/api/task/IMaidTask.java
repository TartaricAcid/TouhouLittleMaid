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
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public interface IMaidTask {
    /**
     * 垂直搜索范围
     */
    int VERTICAL_SEARCH_RANGE = 4;

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
     * 骑乘、待命状态下执行的 AI，注意此时女仆不能移动，只能站桩执行相关 AI
     *
     * @param maid 女仆对象
     * @return 如果什么都不做，请返回空集合
     */
    default List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createRideBrainTasks(EntityMaid maid) {
        return Collections.emptyList();
    }

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
     * 当前 Task 是否在切换界面隐藏，这不会阻断任务的使用
     *
     * @param maid 女仆对象
     * @return 当前 Task 是否在切换界面隐藏
     */
    @ApiStatus.AvailableSince("1.4.2")
    default boolean isHidden(EntityMaid maid) {
        return false;
    }

    /**
     * 默认情况下，女仆在工作模式启用时，会随机的走动，或者四处张望
     * <p>
     * 但是有些需要专心致志的工作模式，这样做反而会带来问题。将其设置为 false 就能禁止这种情况
     *
     * @param maid 女仆对象
     * @return 是否禁用四处张望和随机走动 AI
     */
    default boolean enableLookAndRandomWalk(EntityMaid maid) {
        return true;
    }

    /**
     * 是否启用慌乱 AI，默认情况下女仆受伤后会乱跑
     * <p>
     * 但是处于攻击模式或者灭火模式时不应当启用
     *
     * @param maid 女仆对象
     * @return 是否禁用慌乱 AI
     */
    default boolean enablePanic(EntityMaid maid) {
        return true;
    }

    /**
     * 是否启用吃饭 AI，默认情况下女仆在工作模式下也会吃东西，但是有些工作模式不能这么做
     *
     * @param maid 女仆对象
     * @return 是否启用吃饭 AI
     */
    default boolean enableEating(EntityMaid maid) {
        return true;
    }

    /**
     * 是否为拥有工作点的 task，比如钓鱼，必须要坐在坐垫或者船上才能执行
     * <p>
     * 那么在空闲模式下，女仆就需要主动脱离坐垫或者船，就需要判断此处
     *
     * @param maid 女仆对象
     * @return 是否为拥有工作点的 task
     */
    default boolean workPointTask(EntityMaid maid) {
        return false;
    }

    /**
     * 处于该工作模式时，女仆是否允许坐在娱乐方块上？
     *
     * @param maid    女仆
     * @param joyType 娱乐方块类型
     * @return 处于该工作模式时，女仆是否允许坐在娱乐方块上？
     */
    default boolean canSitInJoy(EntityMaid maid, String joyType) {
        return false;
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

            @Override
            public boolean shouldTriggerClientSideContainerClosingOnOpen() {
                return false;
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

    /**
     * 实体搜索范围
     * <p>
     * 给一些远程攻击的武器提供另一些搜索范围，实现超远视距打击
     *
     * @param maid 女仆
     * @return 实体搜索范围
     */
    default AABB searchDimension(EntityMaid maid) {
        float radius = this.searchRadius(maid);
        if (maid.hasRestriction()) {
            return new AABB(maid.getRestrictCenter()).inflate(radius, VERTICAL_SEARCH_RANGE, radius);
        } else {
            return maid.getBoundingBox().inflate(radius, VERTICAL_SEARCH_RANGE, radius);
        }
    }

    /**
     * 实体搜索范围的水平范围值
     *
     * @param maid 女仆
     * @return 实体搜索范围水平范围值
     */
    default float searchRadius(EntityMaid maid) {
        // 默认依据女仆的工作范围划定搜索范围
        return maid.getRestrictRadius();
    }
}
