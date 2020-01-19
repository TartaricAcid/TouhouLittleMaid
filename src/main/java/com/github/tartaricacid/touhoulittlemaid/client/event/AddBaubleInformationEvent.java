package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidAPI;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author TartaricAcid
 * @date 2020/1/5 17:27
 **/

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Side.CLIENT)
public class AddBaubleInformationEvent {
    @SubscribeEvent
    public static void onRenderTooltips(ItemTooltipEvent event) {
        if (LittleMaidAPI.getBauble(event.getItemStack()) != null) {
            event.getToolTip().add(I18n.format("tooltips.touhou_little_maid.bauble.desc"));
        }
    }
}
