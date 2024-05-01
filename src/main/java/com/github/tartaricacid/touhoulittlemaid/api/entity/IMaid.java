package com.github.tartaricacid.touhoulittlemaid.api.entity;

import com.github.tartaricacid.touhoulittlemaid.api.backpack.IMaidBackpack;
import com.github.tartaricacid.touhoulittlemaid.api.event.ConvertMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.BackpackManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
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
}
