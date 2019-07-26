package com.github.tartaricacid.touhoulittlemaid.internal.task;

import com.github.tartaricacid.touhoulittlemaid.api.task.FeedHandler;
import com.github.tartaricacid.touhoulittlemaid.api.task.Trend;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

/**
 * 用于女仆在农场模式下的判定收割、种植行为的接口
 *
 * @author Snownee
 * @date 2019/7/25 21:41
 */
public class VanillaFeedHandler implements FeedHandler {
    @Override
    public boolean isFood(ItemStack stack, EntityPlayer owner) {
        // 牛奶桶也判定为食物，优先进行玩家负面效果剔除
        if (stack.getItem() == Items.MILK_BUCKET) {
            boolean flag = false;
            // 遍历玩家身上的效果
            // 如果存在一个正面效果，直接中断检索，不再进行喂食
            // 如果都是负面效果，只要有一个符合使用条件，可以进行喂食
            for (PotionEffect effect : owner.getActivePotionEffects()) {
                // 是负面效果
                if (effect.getPotion().isBadEffect()) {
                    // 此负面效果时长大于 60，而且牛奶桶可以剔除此效果
                    if (effect.getDuration() > 60 && effect.isCurativeItem(stack)) {
                        flag = true;
                    }
                    // 否则再遍历下一个 effect，进行尝试
                }
                // 如果是正面效果，直接返回 false，不再进行遍历
                else {
                    flag = false;
                    break;
                }
            }
            return flag;
        }

        // 如果是食物（生鸡肉除外，它有负面效果）
        if (stack.getItem() != Items.CHICKEN && stack.getItem() instanceof ItemFood) {
            ItemFood food = (ItemFood) stack.getItem();
            // 如果该食物没有药水属性，或者药水属性不是负面的，就可以喂食
            return food.potionId == null || !food.potionId.getPotion().isBadEffect();
        }
        return false;
    }

    @Override
    public Trend getTrend(ItemStack stack, EntityPlayer owner) {
        // 牛奶桶，属于 EXACT 属性食物
        if (stack.getItem() == Items.MILK_BUCKET) {
            return Trend.EXACT;
        }
        // 金苹果，而且在玩家血量小于一半的情况下，属于 EXACT 属性食物
        if (stack.getItem() == Items.GOLDEN_APPLE && owner.getHealth() * 2 < owner.getMaxHealth()) {
            return Trend.EXACT;
        }

        ItemFood food = (ItemFood) stack.getItem();
        // 食物能够恢复的饱食度
        int heal = food.getHealAmount(stack);
        // 当前玩家欠缺的饱食度
        int hunger = 20 - owner.getFoodStats().getFoodLevel();
        // 如果饱食度恰好，塞入 EXACT 分组
        // 否则依据大小分为 GOOD 和 BAD
        if (heal == hunger) {
            return Trend.EXACT;
        } else if (heal > hunger) {
            return Trend.GOOD;
        } else {
            return Trend.BAD;
        }
    }

    @Override
    public ItemStack feed(ItemStack stack, EntityPlayer owner) {
        // TODO: 2019/7/27 使用牛奶桶时释放音效
        return stack.getItem().onItemUseFinish(stack, owner.world, owner);
    }
}
