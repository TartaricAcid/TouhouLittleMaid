package com.github.tartaricacid.touhoulittlemaid.event.maid;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.event.api.InteractMaidEvent;
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
    @SubscribeEvent
    public static void onInteract(InteractMaidEvent event) {
        ItemStack itemstack = event.getStack();
        PlayerEntity player = event.getPlayer();
        EntityMaid maid = event.getMaid();
        World world = event.getWorld();

        // WIKI 上说附魔之瓶会掉落 3-11 的经验
        // 那么我们就让其消耗 12 点经验获得一个附魔之瓶吧
        int costNum = 12;
        if (itemstack.getItem() == Items.GLASS_BOTTLE && maid.getExperience() / costNum > 0) {
            maid.setExperience(maid.getExperience() - costNum);
            itemstack.shrink(1);
            if (!world.isClientSide) {
                InventoryHelper.dropItemStack(world, player.getX(), player.getY(), player.getZ(), new ItemStack(Items.EXPERIENCE_BOTTLE));
            }
            maid.playSound(SoundEvents.ITEM_PICKUP, 0.2F, ((world.random.nextFloat() - world.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            event.setCanceled(true);
        }
    }
}
