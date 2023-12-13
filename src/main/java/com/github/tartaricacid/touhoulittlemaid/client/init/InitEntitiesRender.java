package com.github.tartaricacid.touhoulittlemaid.client.init;

import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.*;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity.*;
import com.github.tartaricacid.touhoulittlemaid.entity.item.*;
import com.github.tartaricacid.touhoulittlemaid.entity.monster.EntityFairy;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.EntityDanmaku;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.EntityThrowPowerPoint;
import com.github.tartaricacid.touhoulittlemaid.tileentity.*;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class InitEntitiesRender {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent evt) {
        ItemRenderer itemRenderer = evt.getMinecraftSupplier().get().getItemRenderer();
        RenderingRegistry.registerEntityRenderingHandler(EntityMaid.TYPE, EntityMaidRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityChair.TYPE, EntityChairRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityFairy.TYPE, EntityFairyRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityDanmaku.TYPE, EntityDanmakuRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityPowerPoint.TYPE, EntityPowerPointRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityExtinguishingAgent.TYPE, EntityExtinguishingAgentRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBox.TYPE, EntityBoxRender::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityThrowPowerPoint.TYPE, (manager) -> new SpriteRenderer<>(manager, itemRenderer));

        RenderingRegistry.registerEntityRenderingHandler(EntityTombstone.TYPE, EntityTombstoneRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntitySit.TYPE, EntitySitRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityType.SLIME, EntityYukkuriSlimeRender::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityType.EXPERIENCE_ORB, ReplaceExperienceOrbRenderer::new);

        ClientRegistry.bindTileEntityRenderer(TileEntityAltar.TYPE, TileEntityAltarRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityStatue.TYPE, TileEntityStatueRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityGarageKit.TYPE, TileEntityGarageKitRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityGomoku.TYPE, TileEntityGomokuRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityKeyboard.TYPE, TileEntityKeyboardRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityBookshelf.TYPE, TileEntityBookshelfRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityComputer.TYPE, TileEntityComputerRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityShrine.TYPE, TileEntityShrineRenderer::new);
    }
}
