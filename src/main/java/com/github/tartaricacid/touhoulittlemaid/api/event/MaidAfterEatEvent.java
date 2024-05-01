package com.github.tartaricacid.touhoulittlemaid.api.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

public class MaidAfterEatEvent extends Event {
    private final EntityMaid maid;
    private final ItemStack foodAfterEat;

    public MaidAfterEatEvent(EntityMaid maid, ItemStack foodAfterEat) {
        this.maid = maid;
        this.foodAfterEat = foodAfterEat;
    }

    public EntityMaid getMaid() {
        return maid;
    }

    public ItemStack getFoodAfterEat() {
        return foodAfterEat;
    }
}
