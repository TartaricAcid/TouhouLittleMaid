package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author TartaricAcid
 * @date 2019/10/18 20:01
 **/
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Side.CLIENT)
public class MaidSpawnEggTooltipEvent {
    private static final ResourceLocation MAID_ID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "entity.passive.maid");

    @SubscribeEvent
    public static void onRenderTooltip(ItemTooltipEvent event) {
        if (event.getItemStack().getItem() == Items.SPAWN_EGG) {
            if (MAID_ID.equals(ItemMonsterPlacer.getNamedIdFrom(event.getItemStack()))) {
                event.getToolTip().add(I18n.format("tooltips.touhou_little_maid.maid_spawn_egg.desc.1"));
                event.getToolTip().add(I18n.format("tooltips.touhou_little_maid.maid_spawn_egg.desc.2"));
            }
        }
    }
}
