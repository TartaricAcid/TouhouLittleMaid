package com.github.tartaricacid.touhoulittlemaid.client.event;


import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.RenderMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.client.resources.ModelData;
import com.github.tartaricacid.touhoulittlemaid.client.resources.PlayerMaidResources;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import static com.github.tartaricacid.touhoulittlemaid.client.resources.CustomResourcesLoader.MAID_MODEL;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Side.CLIENT)
public class PlayerMaidRenderEvent {
    /**
     * EMCAScript 6 箭头函数表达式风格的前缀，不错吧
     */
    private static final String PLAYER_NAME_PREFIX = "=>";

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRenderPlayerNamedMaid(RenderMaidEvent event) {
        String nameTag = event.getMaid().getCustomNameTag();
        if (StringUtils.isNotBlank(nameTag) && nameTag.startsWith(PLAYER_NAME_PREFIX)) {
            ModelData data = event.getModelData();
            String name = nameTag.substring(2);
            data.setModel(PlayerMaidResources.getPlayerMaidModel(name));
            data.setAnimations(PlayerMaidResources.getPlayerMaidAnimations());
            data.setInfo(PlayerMaidResources.getPlayerMaidInfo(name));
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onRenderEncryptNamedMaid(RenderMaidEvent event) {
        String name = event.getMaid().getCustomNameTag();
        if (StringUtils.isNotBlank(name)) {
            MAID_MODEL.getEasterEggEncryptTagModel(DigestUtils.sha1Hex(name)).ifPresent(data -> modelDataSet(event, data));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderNormalNamedMaid(RenderMaidEvent event) {
        String name = event.getMaid().getCustomNameTag();
        if (StringUtils.isNotBlank(name)) {
            MAID_MODEL.getEasterEggNormalTagModel(name).ifPresent(data -> modelDataSet(event, data));
        }
    }

    private static void modelDataSet(RenderMaidEvent event, ModelData data) {
        ModelData rawData = event.getModelData();
        rawData.setModel(data.getModel());
        rawData.setInfo(data.getInfo());
        if (data.getAnimations() != null && !data.getAnimations().isEmpty()) {
            rawData.setAnimations(data.getAnimations());
        }
        event.setCanceled(true);
    }
}
