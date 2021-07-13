package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.google.common.collect.Maps;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Map;

public class ItemMaidBackpack extends Item {
    private static final Map<Integer, ItemMaidBackpack> ITEM_BY_LEVEL = Maps.newHashMap();
    private final int level;

    public ItemMaidBackpack(int level) {
        super((new Properties()).tab(InitItems.MAIN_TAB).stacksTo(1));
        this.level = level;
        ITEM_BY_LEVEL.put(level, this);
    }

    public int getLevel() {
        return level;
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        if (target instanceof EntityMaid) {
            EntityMaid maid = (EntityMaid) target;
            int maidBackpackLevel = maid.getBackpackLevel();
            if (maid.isOwnedBy(playerIn) && level != maidBackpackLevel) {
                if (!maid.backpackNoDelay()) {
                    return ActionResultType.SUCCESS;
                }
                maid.setBackpackLevel(level);
                maid.setBackpackDelay();
                stack.shrink(1);
                ItemsUtil.giveItemToPlayer(playerIn, new ItemStack(ITEM_BY_LEVEL.get(maidBackpackLevel)));
                if (level < maidBackpackLevel) {
                    ItemStackHandler maidInv = maid.getMaidInv();
                    int startIndex = BackpackLevel.BACKPACK_CAPACITY_MAP.get(level);
                    int endIndex = BackpackLevel.BACKPACK_CAPACITY_MAP.get(maidBackpackLevel);
                    ItemsUtil.dropEntityItems(maid, maidInv, startIndex, endIndex);
                }
                return ActionResultType.SUCCESS;
            }
        }
        return super.interactLivingEntity(stack, playerIn, target, hand);
    }
}
