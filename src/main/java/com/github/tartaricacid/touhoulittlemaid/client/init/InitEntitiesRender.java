package com.github.tartaricacid.touhoulittlemaid.client.init;

import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.*;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityExtinguishingAgent;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPowerPoint;
import com.github.tartaricacid.touhoulittlemaid.entity.monster.EntityFairy;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.EntityDanmaku;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class InitEntitiesRender {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent evt) {
        RenderingRegistry.registerEntityRenderingHandler(EntityMaid.TYPE, EntityMaidRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityChair.TYPE, EntityChairRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityFairy.TYPE, EntityFairyRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityDanmaku.TYPE, EntityDanmakuRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityPowerPoint.TYPE, EntityPowerPointRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityExtinguishingAgent.TYPE, EntityExtinguishingAgentRenderer::new);
    }
}
