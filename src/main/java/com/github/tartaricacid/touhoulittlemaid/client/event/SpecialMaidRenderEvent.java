package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.client.RenderMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.client.model.EasterEggModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.models.MaidModels;
import com.github.tartaricacid.touhoulittlemaid.client.resource.models.PlayerMaidModels;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;

import static com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader.MAID_MODELS;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Dist.CLIENT)
public final class SpecialMaidRenderEvent {
    public static final String EASTER_EGG_MODEL = "touhou_little_maid:easter_egg_model";
    /**
     * EMCAScript 6 箭头函数表达式风格的前缀，不错吧
     */
    private static final String PLAYER_NAME_PREFIX = "=>";

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRenderPlayerNamedMaid(RenderMaidEvent event) {
        Component customName = event.getMaid().getCustomName();
        if (customName == null) {
            return;
        }
        String name = customName.getString();
        if (StringUtils.isNotBlank(name) && name.startsWith(PLAYER_NAME_PREFIX)) {
            String playerName = name.substring(2);
            MaidModels.ModelData data = event.getModelData();
            data.setModel(PlayerMaidModels.getPlayerMaidModel(playerName));
            data.setAnimations(PlayerMaidModels.getPlayerMaidAnimations());
            data.setInfo(PlayerMaidModels.getPlayerMaidInfo(playerName));
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onRenderEncryptNamedMaid(RenderMaidEvent event) {
        Component customName = event.getMaid().getCustomName();
        if (customName == null) {
            return;
        }
        String name = customName.getString();
        if (StringUtils.isNotBlank(name)) {
            MAID_MODELS.getEasterEggEncryptTagModel(DigestUtils.sha1Hex(name)).ifPresent(data -> modelDataSet(event, data));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onRenderNormalNamedMaid(RenderMaidEvent event) {
        Component customName = event.getMaid().getCustomName();
        if (customName == null) {
            return;
        }
        String name = customName.getString();
        if (StringUtils.isNotBlank(name)) {
            MAID_MODELS.getEasterEggNormalTagModel(name).ifPresent(data -> modelDataSet(event, data));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderEasterEggModel(RenderMaidEvent event) {
        String id = event.getMaid().getModelId();
        if (EASTER_EGG_MODEL.equals(id)) {
            MaidModels.ModelData data = event.getModelData();
            data.setModel(EasterEggModel.getInstance());
            data.setAnimations(Collections.emptyList());
            data.setInfo(EasterEggModel.getInfo());
            event.setCanceled(true);
        }
    }

    private static void modelDataSet(RenderMaidEvent event, MaidModels.ModelData data) {
        MaidModels.ModelData rawData = event.getModelData();
        rawData.setModel(data.getModel());
        rawData.setInfo(data.getInfo());
        if (data.getAnimations() != null && !data.getAnimations().isEmpty()) {
            rawData.setAnimations(data.getAnimations());
        }
        event.setCanceled(true);
    }
}
