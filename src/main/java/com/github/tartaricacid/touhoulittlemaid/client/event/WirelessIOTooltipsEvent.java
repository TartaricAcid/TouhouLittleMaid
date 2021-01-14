package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemWirelessIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Side.CLIENT)
public class WirelessIOTooltipsEvent {
    private static final ResourceLocation BG = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/wireless_io_tooltips.png");

    @SubscribeEvent
    public static void onRenderTooltip(RenderTooltipEvent.PostText event) {
        ItemStack stack = event.getStack();
        if (stack.getItem() == MaidItems.WIRELESS_IO) {
            int x = event.getX();
            int y = event.getY();
            Minecraft mc = Minecraft.getMinecraft();
            IItemHandler handler = ItemWirelessIO.getFilterList(stack);
            int offset = 0;

            RenderHelper.enableGUIStandardItemLighting();
            for (int i = 0; i < 9; ++i) {
                ItemStack stackIn = handler.getStackInSlot(i);
                if (!stackIn.isEmpty()) {
                    mc.getRenderItem().renderItemAndEffectIntoGUI(stackIn, x + offset, y + 12);
                    offset += 16;
                }
            }
            RenderHelper.disableStandardItemLighting();
        }
    }
}
