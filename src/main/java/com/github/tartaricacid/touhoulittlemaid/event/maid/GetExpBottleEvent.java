package com.github.tartaricacid.touhoulittlemaid.event.maid;

import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public final class GetExpBottleEvent {
    /**
     * WIKI 上说附魔之瓶会掉落 3-11 的经验 <br>
     * 那么我们就让其消耗 12 点经验获得一个附魔之瓶吧
     */
    private static final int PER_BOTTLE_XP = 12;

    @SubscribeEvent
    public static void onInteract(InteractMaidEvent event) {
        ItemStack itemstack = event.getStack();
        PlayerEntity player = event.getPlayer();
        EntityMaid maid = event.getMaid();
        World world = event.getWorld();

        if (itemstack.getItem() == Items.GLASS_BOTTLE) {
            int count = maid.getExperience() / PER_BOTTLE_XP;
            if (count <= 0) {
                return;
            }
            if (player.isDiscrete()) {
                count = Math.min(count, itemstack.getCount());
            } else {
                count = 1;
            }
            int costNum = PER_BOTTLE_XP * count;
            maid.setExperience(maid.getExperience() - costNum);
            itemstack.shrink(count);
            if (!world.isClientSide) {
                ItemStack xpBottles = new ItemStack(Items.EXPERIENCE_BOTTLE, count);
                InventoryHelper.dropItemStack(world, player.getX(), player.getY(), player.getZ(), xpBottles);
            }
            maid.playSound(SoundEvents.ITEM_PICKUP, 0.2F, ((world.random.nextFloat() - world.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            event.setCanceled(true);
        }
    }
}
