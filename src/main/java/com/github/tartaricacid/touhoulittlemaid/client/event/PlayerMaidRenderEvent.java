package com.github.tartaricacid.touhoulittlemaid.client.event;


import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.RenderMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.client.resources.PlayerMaidResources;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Side.CLIENT)
public class PlayerMaidRenderEvent {
    @SubscribeEvent
    public static void onRenderMaid(RenderMaidEvent event) {
        EntityMaid maid = event.getMaid();
        String name = maid.getCustomNameTag();
        if (name.startsWith("=>")) {
            RenderMaidEvent.ModelData data = event.getModelData();
            data.setModel(PlayerMaidResources.getPlayerMaidModel());
            data.setAnimations(PlayerMaidResources.getPlayerMaidAnimations());
            data.setInfo(PlayerMaidResources.getPlayerMaidInfo(name.substring(2)));
            event.setCanceled(true);
        }
    }
}
