package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemWirelessIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.fml.client.config.GuiUtils;
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

            if (y < 70) {
                y = 70;
            }
            y = y - 70;
            x = x - 80;

            Minecraft mc = Minecraft.getMinecraft();
            IItemHandler handler = ItemWirelessIO.getFilterList(stack);

            GlStateManager.color(1, 1, 1);
            mc.getTextureManager().bindTexture(BG);
            GuiUtils.drawTexturedModalRect(x, y, 0, 0, 68, 668, 300);

            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.translate(0, 0, 300);
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 3; ++j) {
                    ItemStack stackIn = handler.getStackInSlot(j * 3 + i);
                    mc.getRenderItem().renderItemAndEffectIntoGUI(stackIn, x + 8 + 18 * i, y + 8 + 18 * j);
                }
            }
            RenderHelper.disableStandardItemLighting();
        }
    }
}
