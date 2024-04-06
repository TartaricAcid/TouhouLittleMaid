package com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.condition;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.ItemHakureiGohei;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.*;

public class InnerClassify {
    private static final String EMPTY = "";

    public static String doClassifyTest(String extraPre, EntityMaid maid, InteractionHand hand) {
        ItemStack itemInHand = maid.getItemInHand(hand);
        Item item = itemInHand.getItem();
        if (item instanceof ItemHakureiGohei) {
            return extraPre + "gohei";
        }
        if (item instanceof SwordItem) {
            return extraPre + "sword";
        }
        if (item instanceof AxeItem) {
            return extraPre + "axe";
        }
        if (item instanceof PickaxeItem) {
            return extraPre + "pickaxe";
        }
        if (item instanceof ShovelItem) {
            return extraPre + "shovel";
        }
        if (item instanceof HoeItem) {
            return extraPre + "hoe";
        }
        if (item instanceof ShieldItem) {
            return extraPre + "shield";
        }
        if (item instanceof ThrowablePotionItem) {
            return extraPre + "throwable_potion";
        }
        return EMPTY;
    }
}
