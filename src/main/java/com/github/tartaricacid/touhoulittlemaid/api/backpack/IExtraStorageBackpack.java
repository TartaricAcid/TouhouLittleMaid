package com.github.tartaricacid.touhoulittlemaid.api.backpack;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nullable;

/**
 * 额外存储背包接口。
 * <p>
 * 实现此接口的背包类型可暴露额外的物品存储槽位（{@code extra_N}），
 * AI manage_item Tool 可对此存储空间进行枚举，并按 {@link IItemHandler}
 * 的实际能力尝试拿取/放入，无需依赖 compat 层。
 * </p>
 * <p>
 * 示例：{@link com.github.tartaricacid.touhoulittlemaid.entity.backpack.EnderChestBackpack}
 * 通过此接口暴露拥有者的末影箱背包。
 * </p>
 */
public interface IExtraStorageBackpack {
    @Nullable
    IItemHandler getExtraStorage(EntityMaid maid);
}
