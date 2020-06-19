package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.proxy.ClientProxy;
import com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author TartaricAcid
 * @date 2019/7/27 20:22
 **/
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Side.CLIENT)
public class ChangeDimEvent {
    private static int dimAt;

    @SubscribeEvent
    public static void onChangeDim(EntityJoinWorldEvent event) {
        if (event.getWorld().isRemote && event.getEntity() == Minecraft.getMinecraft().player) {
            int dim = event.getEntity().world.provider.getDimension();
            if (dim != dimAt) {
                dimAt = dim;
                EntityCacheUtil.ENTITY_CACHE.invalidateAll();
            }
        }
    }
}
