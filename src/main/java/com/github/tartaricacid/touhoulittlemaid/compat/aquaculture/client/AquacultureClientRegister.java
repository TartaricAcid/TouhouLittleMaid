package com.github.tartaricacid.touhoulittlemaid.compat.aquaculture.client;

import com.github.tartaricacid.touhoulittlemaid.compat.aquaculture.entity.AquacultureFishingHook;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@OnlyIn(Dist.CLIENT)
public class AquacultureClientRegister {
    @SubscribeEvent
    public void onEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(AquacultureFishingHook.TYPE, AquacultureFishingHookRenderer::new);
    }
}
