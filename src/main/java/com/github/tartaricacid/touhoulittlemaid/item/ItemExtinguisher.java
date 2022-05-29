package com.github.tartaricacid.touhoulittlemaid.item;

import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static com.github.tartaricacid.touhoulittlemaid.item.MaidGroup.MAIN_TAB;

public class ItemExtinguisher extends Item {
    public ItemExtinguisher() {
        super((new Properties()).tab(MAIN_TAB).durability(128).setNoRepair());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        if (worldIn.isClientSide) {
            playerIn.sendMessage(new TranslatableComponent("message.touhou_little_maid.extinguisher.player_cannot_use"), Util.NIL_UUID);
        }
        return super.use(worldIn, playerIn, handIn);
    }
}
