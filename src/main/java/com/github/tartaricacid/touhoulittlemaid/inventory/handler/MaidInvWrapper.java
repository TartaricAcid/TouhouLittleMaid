package com.github.tartaricacid.touhoulittlemaid.inventory.handler;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

/**
 * 女仆物品栏包装，继承自 {@link CombinedInvWrapper} 并保持对女仆实体的引用。
 * 以此在只有 {@link IItemHandler} 引用的情况下，
 * 获取关联的女仆实体，从而支持对其持有的其它物品的访问。
 */
public class MaidInvWrapper extends CombinedInvWrapper {
    private final EntityMaid maid;

    public MaidInvWrapper(EntityMaid maid, IItemHandlerModifiable... itemHandler) {
        super(itemHandler);
        this.maid = maid;
    }

    public EntityMaid getMaid() {
        return maid;
    }
}
