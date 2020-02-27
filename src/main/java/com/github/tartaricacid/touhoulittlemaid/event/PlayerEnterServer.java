package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.SyncCustomSpellCardData;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

/**
 * @author TartaricAcid
 * @date 2019/12/1 16:51
 **/
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public class PlayerEnterServer {
    @SubscribeEvent
    public static void onPlayerEnterServer(PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.player;
            TouhouLittleMaid.LOGGER.info("Sending custom spell data to {}", player.getDisplayNameString());
            CommonProxy.INSTANCE.sendTo(new SyncCustomSpellCardData(), player);
        }
    }
}
