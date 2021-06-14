package com.github.tartaricacid.touhoulittlemaid.client.init;

import com.github.tartaricacid.touhoulittlemaid.client.model.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityChairRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class InitEntitiesRender {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent evt) {
        RenderingRegistry.registerEntityRenderingHandler(EntityMaid.TYPE, r -> new EntityMaidRenderer(r, new BedrockModel<>(), 0.5f));
        RenderingRegistry.registerEntityRenderingHandler(EntityChair.TYPE, r -> new EntityChairRenderer(r, new BedrockModel<>(), 0.5f));
    }
}
