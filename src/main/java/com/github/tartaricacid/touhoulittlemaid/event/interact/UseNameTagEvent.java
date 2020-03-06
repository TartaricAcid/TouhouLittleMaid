package com.github.tartaricacid.touhoulittlemaid.event.interact;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.client.gui.item.NameTagGui;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public class UseNameTagEvent {
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onInteractClient(InteractMaidEvent event) {
        ItemStack itemstack = event.getStack();
        EntityPlayer player = event.getPlayer();
        AbstractEntityMaid maid = event.getMaid();

        if (!itemstack.hasDisplayName() && player.getHeldItemMainhand().getItem() == Items.NAME_TAG
                && player.equals(maid.getOwner())) {
            Minecraft.getMinecraft().displayGuiScreen(new NameTagGui((EntityMaid) maid));
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public static void onInteractServer(InteractMaidEvent event) {
        ItemStack itemstack = event.getStack();
        EntityPlayer player = event.getPlayer();
        AbstractEntityMaid maid = event.getMaid();

        if (!itemstack.hasDisplayName() && player.getHeldItemMainhand().getItem() == Items.NAME_TAG
                && player.equals(maid.getOwner())) {
            event.setCanceled(true);
        }
    }
}
