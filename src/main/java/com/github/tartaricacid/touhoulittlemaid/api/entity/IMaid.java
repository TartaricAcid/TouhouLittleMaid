package com.github.tartaricacid.touhoulittlemaid.api.entity;

import com.github.tartaricacid.touhoulittlemaid.api.backpack.IMaidBackpack;
import com.github.tartaricacid.touhoulittlemaid.api.event.ConvertMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.BackpackManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.tartaricacid.touhoulittlemaid.util.BiomeCacheUtil;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.Nullable;

/**
 * 女仆部分方法的接口化
 * <p>
 * 目前仅用于渲染
 */
public interface IMaid {
    /**
     * 转换为接口，同时发送转换事件
     *
     * @param mob 需要转换的实体对象
     * @return 转换的 Maid 对象
     */
    @Nullable
    static IMaid convert(Mob mob) {
        // 如果是继承了这个接口的，可以直接转换
        if (mob instanceof IMaid maid) {
            return maid;
        }
        // 如果不是，那么发送事件进行检查
        // 这样就可以兼容其他模组
        var event = new ConvertMaidEvent(mob);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getMaid();
    }

    /**
     * 将 Mob 转换成 Maid 对象，转换不成功返回 Null
     */
    @Nullable
    static EntityMaid convertToMaid(Mob mob) {
        IMaid convert = convert(mob);
        if (convert == null) {
            return null;
        }
        return convert.asStrictMaid();
    }

    /**
     * 转换成女仆对象
     *
     * @return 为 null 表示转换不了
     */
    @Nullable
    default EntityMaid asStrictMaid() {
        return null;
    }

    /**
     * 获取模型 ID
     */
    String getModelId();

    /**
     * 转成原实体对象
     */
    Mob asEntity();

    // 下方为女仆物品使用接口

    /**
     * 物品使用缓存，应当创建 ItemStack[] 来缓存数据。
     * 如果实体会使用物品，理应Override这个方法，在实体中缓存。
     */
    default ItemStack[] getHandItemsForAnimation() {
        return new ItemStack[]{ItemStack.EMPTY, ItemStack.EMPTY};
    }

    /**
     * 女仆任务：
     * 如果实体会进行类似女仆任务的行为，则可以在这里模拟，以供动画使用。
     * 例如攻击，远程攻击等等
     */
    default IMaidTask getTask() {
        return TaskManager.getIdleTask();
    }

    /**
     * 女仆手臂是否举起：在攻击动画中使用
     */
    default boolean isSwingingArms() {
        return false;
    }

    // 下方为女仆属性接口

    default int getExperience() {
        return 0;
    }

    default boolean isMaidInSittingPose() {
        return false;
    }

    default boolean isBegging() {
        return false;
    }

    default int getFavorability() {
        return 0;
    }

    /**
     * 获取背部显示物品
     */
    default ItemStack getBackpackShowItem() {
        return ItemStack.EMPTY;
    }

    /**
     * 背包类型
     */
    default IMaidBackpack getMaidBackpackType() {
        return BackpackManager.getEmptyBackpack();
    }

    default boolean hasBackpack() {
        return getMaidBackpackType() != BackpackManager.getEmptyBackpack();
    }

    // 下方为 MC 实体属性，无需 Override

    default boolean hasHelmet() {
        return !asEntity().getItemBySlot(EquipmentSlot.HEAD).isEmpty();
    }

    default boolean hasChestPlate() {
        return !asEntity().getItemBySlot(EquipmentSlot.CHEST).isEmpty();
    }

    default boolean hasLeggings() {
        return !asEntity().getItemBySlot(EquipmentSlot.LEGS).isEmpty();
    }

    default boolean hasBoots() {
        return !asEntity().getItemBySlot(EquipmentSlot.FEET).isEmpty();
    }

    default boolean onHurt() {
        return asEntity().hurtTime > 0;
    }

    default boolean hasFishingHook() {
        return false;
    }

    // 下方为 Deprecated 方法，仅用于适配旧版本模型，无需 Override

    @Deprecated
    default String getAtBiomeTemp() {
        float temp = BiomeCacheUtil.getCacheBiome(asEntity()).getBaseTemperature();
        if (temp < 0.15) {
            return "COLD";
        } else if (temp < 0.55) {
            return "OCEAN";
        } else if (temp < 0.95) {
            return "MEDIUM";
        } else {
            return "WARM";
        }
    }

    @Deprecated
    default boolean hasSasimono() {
        return false;
    }

    @Deprecated
    default boolean isSitInJoyBlock() {
        return false;
    }

    @Deprecated
    default int getDim() {
        ResourceKey<Level> dim = asEntity().level.dimension();
        if (dim.equals(Level.OVERWORLD)) {
            return 0;
        }
        if (dim.equals(Level.NETHER)) {
            return -1;
        }
        if (dim.equals(Level.END)) {
            return 1;
        }
        return 0;
    }

}
