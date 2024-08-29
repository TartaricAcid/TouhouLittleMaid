package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.download.ClientPackDownloadManager;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = TouhouLittleMaid.MOD_ID)
public class ClientPackDownloadEvent {
    private static final String CONFIG_NAME = "touhou_little_maid-server.toml";

    /**
     * 玩家进入服务端，或者服务端自动重置配置时，会触发此方法
     */
    @SubscribeEvent
    public static void onReloadingConfig(ModConfigEvent.Reloading event) {
        String fileName = event.getConfig().getFileName();
        if (FMLEnvironment.dist == Dist.CLIENT && CONFIG_NAME.equals(fileName)) {
            // 先清理，把服务端文件从内存中全清干净
            CustomPackLoader.reloadPacks();
            // 然后尝试加载服务端需要下载的文件
            ClientPackDownloadManager.downloadClientPack();
        }
    }
}
