package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.google.common.collect.Maps;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Map;
import java.util.Optional;

import static com.github.tartaricacid.touhoulittlemaid.item.MaidGroup.MAIN_TAB;

public class ItemMaidBackpack extends Item {
    private static final Map<Integer, ItemMaidBackpack> ITEM_BY_LEVEL = Maps.newHashMap();
    private final int level;

    public ItemMaidBackpack(int level) {
        super((new Properties()).tab(MAIN_TAB).stacksTo(1));
        this.level = level;
        ITEM_BY_LEVEL.put(level, this);
    }

    public static Optional<ItemMaidBackpack> getInstance(int level) {
        return Optional.ofNullable(ITEM_BY_LEVEL.get(level));
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
                if (maid.backpackHasDelay()) {
                    return ActionResultType.SUCCESS;
                }
                maid.setBackpackLevel(level);
                maid.setBackpackDelay();
                for (Item backpack : ITEM_BY_LEVEL.values()) {
                    playerIn.getCooldowns().addCooldown(backpack, 20);
                }
                stack.shrink(1);
                target.playSound(SoundEvents.HORSE_SADDLE, 0.5F, 1.0F);
                getInstance(maidBackpackLevel).ifPresent(backpack -> ItemHandlerHelper.giveItemToPlayer(playerIn, backpack.getDefaultInstance()));
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
