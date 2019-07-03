package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Side.CLIENT)
public class KappaCompassRenderEvent {
    @SubscribeEvent
    public static void renderBlockOverlayEvent(RenderBlockOverlayEvent event) {
        ItemStack stack = event.getPlayer().getHeldItemMainhand();
        if (!stack.isEmpty() && stack.getItem() == MaidItems.KAPPA_COMPASS && stack.hasTagCompound() && stack.getTagCompound().hasKey("Pos")) {
            int[] i = stack.getTagCompound().getIntArray("Pos");
            BlockPos targetPos = new BlockPos(i[0], i[1], i[2]);
            // TODO：渲染指定坐标方块
        }
    }
}
