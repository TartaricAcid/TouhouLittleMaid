package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.resources.CustomResourcesLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resources.PlayerMaidResources;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author TartaricAcid
 * @date 2019/7/14 19:53
 **/
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Side.CLIENT)
public class ReloadResourceEvent {
    @SubscribeEvent
    public static void onTextureStitchEvent(TextureStitchEvent.Post event) {
        CustomResourcesLoader.reloadResources();
        PlayerMaidResources.reloadResources();
    }
}
