package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.animation.inner.InnerAnimation;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resource.GeckoModelLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resource.models.PlayerMaidModels;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.TextureAtlasStitchedEvent;
import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.TimeUnit;


@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class ReloadResourceEvent {
    public static final ResourceLocation BLOCK_ATLAS_TEXTURE = ResourceLocation.withDefaultNamespace("textures/atlas/blocks.png");

    @SubscribeEvent
    public static void onTextureStitchEventPost(TextureAtlasStitchedEvent event) {
        if (BLOCK_ATLAS_TEXTURE.equals(event.getAtlas().location())) {
            reloadAllPack();
        }
    }

    public static void reloadAllPack() {
        StopWatch watch = StopWatch.createStarted();
        {
            GeckoModelLoader.reload();
            InnerAnimation.init();
            CustomPackLoader.reloadPacks();
            PlayerMaidModels.reload();
        }
        watch.stop();
        TouhouLittleMaid.LOGGER.info("Model loading time: {} ms", watch.getTime(TimeUnit.MICROSECONDS) / 1000.0);
    }
}
