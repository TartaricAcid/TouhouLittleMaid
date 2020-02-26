package com.github.tartaricacid.touhoulittlemaid.event.interact;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public final class GetExpBottleEvent {
    @SubscribeEvent
    public static void onInteract(InteractMaidEvent event) {
        ItemStack itemstack = event.getStack();
        EntityPlayer player = event.getPlayer();
        AbstractEntityMaid maid = event.getMaid();
        World world = event.getWorld();

        // WIKI 上说附魔之瓶会掉落 3-11 的经验
        // 那么我们就让其消耗 12 点经验获得一个附魔之瓶吧
        int costNum = 12;
        if (itemstack.getItem() == Items.GLASS_BOTTLE && maid.getExp() / costNum > 0) {
            maid.setExp(maid.getExp() - costNum);
            itemstack.shrink(1);
            if (!world.isRemote) {
                InventoryHelper.spawnItemStack(world, player.posX, player.posY, player.posZ, new ItemStack(Items.EXPERIENCE_BOTTLE));
            }
            maid.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.2F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            event.setCanceled(true);
        }
    }
}
