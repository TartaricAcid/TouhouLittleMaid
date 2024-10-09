package com.github.tartaricacid.touhoulittlemaid.compat.aquaculture.client;

import com.github.tartaricacid.touhoulittlemaid.compat.aquaculture.entity.AquacultureFishingHook;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class AquacultureClientRegister {
    @SubscribeEvent
    public void onEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(AquacultureFishingHook.TYPE, AquacultureFishingHookRenderer::new);
    }
}
