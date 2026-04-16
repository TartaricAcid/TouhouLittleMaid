package com.github.tartaricacid.touhoulittlemaid.compat.extracontainer.curios;

import com.github.tartaricacid.touhoulittlemaid.compat.extracontainer.ContainerRef;
import com.github.tartaricacid.touhoulittlemaid.compat.extracontainer.ExtraContainerManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

public abstract class CuriosSlotRef implements ContainerRef {
    public final String slotType;
    public final int slotIndex;
    public final int priority;

    protected CuriosSlotRef(String slotType, int slotIndex) {
        this.slotType = slotType;
        this.slotIndex = slotIndex;
        this.priority = ExtraContainerManager.getSlotPriority(slotType);
    }

    protected ItemStack getCuriosStack(EntityMaid maid) {
        var inventory = CuriosApi.getCuriosInventory(maid);
        return inventory.map(handler -> handler.getStacksHandler(slotType)
                .map(stacksHandler -> {
                    IDynamicStackHandler stacks = stacksHandler.getStacks();
                    if (slotIndex >= stacks.getSlots()) {
                        return ItemStack.EMPTY;
                    }
                    return stacks.getStackInSlot(slotIndex);
                }).orElse(ItemStack.EMPTY)
        ).orElse(ItemStack.EMPTY);
    }

    public int compareTo(CuriosSlotRef other) {
        int pc = Integer.compare(this.priority, other.priority);
        return pc != 0 ? pc : Integer.compare(this.slotIndex, other.slotIndex);
    }
}
