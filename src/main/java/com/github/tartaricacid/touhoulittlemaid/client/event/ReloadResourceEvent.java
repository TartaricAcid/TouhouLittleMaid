package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.resources.CustomModelLoader;
import com.github.tartaricacid.touhoulittlemaid.proxy.ClientProxy;
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
        CustomModelLoader.reloadModelPack(ClientProxy.MAID_MODEL);
        CustomModelLoader.reloadModelPack(ClientProxy.CHAIR_MODEL);
    }
}
