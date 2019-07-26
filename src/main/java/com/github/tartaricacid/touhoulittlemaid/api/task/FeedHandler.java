package com.github.tartaricacid.touhoulittlemaid.api.task;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface FeedHandler extends TaskHandler {

    boolean isFood(ItemStack stack, EntityPlayer owner);

    Trend getTrend(ItemStack stack, EntityPlayer owner);

    ItemStack feed(ItemStack stack, EntityPlayer owner);

    @Override
    default boolean canExecute(AbstractEntityMaid maid) {
        return true;
    }
}
