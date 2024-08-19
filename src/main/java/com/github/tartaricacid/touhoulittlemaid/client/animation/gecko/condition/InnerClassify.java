package com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.condition;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.item.ItemHakureiGohei;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.*;

public class InnerClassify {
    private static final String EMPTY = "";

	public static String doClassifyTest(String extraPre, IMaid maid, InteractionHand hand) {
		ItemStack itemInHand = maid.asEntity().getItemInHand(hand);
        Item item = itemInHand.getItem();
        return switch (item) {
            case ItemHakureiGohei ignored -> extraPre + "gohei";
            case SwordItem ignored -> extraPre + "sword";
            case AxeItem ignored -> extraPre + "axe";
            case PickaxeItem ignored -> extraPre + "pickaxe";
            case ShovelItem ignored -> extraPre + "shovel";
            case HoeItem ignored -> extraPre + "hoe";
            case ShieldItem ignored -> extraPre + "shield";
            case ThrowablePotionItem ignored -> extraPre + "throwable_potion";
            default -> EMPTY;
        };
    }
}
