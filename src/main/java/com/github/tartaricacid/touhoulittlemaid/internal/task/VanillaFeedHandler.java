package com.github.tartaricacid.touhoulittlemaid.internal.task;

import com.github.tartaricacid.touhoulittlemaid.api.task.FeedHandler;
import com.github.tartaricacid.touhoulittlemaid.api.task.Trend;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

public class VanillaFeedHandler implements FeedHandler {

    @Override
    public boolean isFood(ItemStack stack, EntityPlayer owner) {
        if (stack.getItem() == Items.MILK_BUCKET) {
            boolean flag = false;
            for (PotionEffect effect : owner.getActivePotionEffects()) {
                if (effect.getPotion().isBadEffect()) {
                    if (effect.getDuration() > 60 && effect.isCurativeItem(stack)) {
                        flag = true;
                    }
                }
                else {
                    flag = false;
                    break;
                }
            }
            return flag;
        }
        if (stack.getItem() != Items.CHICKEN && stack.getItem() instanceof ItemFood) {
            ItemFood food = (ItemFood) stack.getItem();
            if (food.potionId != null && food.potionId.getPotion().isBadEffect()) {
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public Trend getTrend(ItemStack stack, EntityPlayer owner) {
        if (stack.getItem() == Items.MILK_BUCKET) {
            return Trend.EXACT;
        }
        if (stack.getItem() == Items.GOLDEN_APPLE && owner.getHealth() * 2 < owner.getMaxHealth()) {
            return Trend.EXACT;
        }
        ItemFood food = (ItemFood) stack.getItem();
        int heal = food.getHealAmount(stack);
        int hunger = 20 - owner.getFoodStats().getFoodLevel();
        if (heal == hunger) {
            return Trend.EXACT;
        }
        else if (heal > hunger) {
            return Trend.GOOD;
        }
        else {
            return Trend.BAD;
        }
    }

    @Override
    public ItemStack feed(ItemStack stack, EntityPlayer owner) {
        return stack.getItem().onItemUseFinish(stack, owner.world, owner);
    }

}
