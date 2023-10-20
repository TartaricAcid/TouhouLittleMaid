package com.github.tartaricacid.touhoulittlemaid.client.init;

import com.github.tartaricacid.touhoulittlemaid.client.model.*;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.*;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity.TileEntityAltarRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity.TileEntityGarageKitRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity.TileEntityStatueRenderer;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBox;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityExtinguishingAgent;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPowerPoint;
import com.github.tartaricacid.touhoulittlemaid.entity.monster.EntityFairy;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.EntityDanmaku;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.EntityThrowPowerPoint;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityAltar;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGarageKit;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityStatue;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class InitEntitiesRender {
    @SubscribeEvent
    public static void onEntityRenderers(EntityRenderersEvent.RegisterRenderers evt) {
        EntityRenderers.register(EntityMaid.TYPE, EntityMaidRenderer::new);
        EntityRenderers.register(EntityChair.TYPE, EntityChairRenderer::new);
        EntityRenderers.register(EntityFairy.TYPE, EntityFairyRenderer::new);
        EntityRenderers.register(EntityDanmaku.TYPE, EntityDanmakuRenderer::new);
        EntityRenderers.register(EntityPowerPoint.TYPE, EntityPowerPointRenderer::new);
        EntityRenderers.register(EntityExtinguishingAgent.TYPE, EntityExtinguishingAgentRenderer::new);
        EntityRenderers.register(EntityBox.TYPE, EntityBoxRender::new);
        EntityRenderers.register(EntityThrowPowerPoint.TYPE, ThrownItemRenderer::new);

        EntityRenderers.register(EntityType.SLIME, EntityYukkuriSlimeRender::new);
        EntityRenderers.register(EntityType.EXPERIENCE_ORB, ReplaceExperienceOrbRenderer::new);

        BlockEntityRenderers.register(TileEntityAltar.TYPE, TileEntityAltarRenderer::new);
        BlockEntityRenderers.register(TileEntityStatue.TYPE, TileEntityStatueRenderer::new);
        BlockEntityRenderers.register(TileEntityGarageKit.TYPE, TileEntityGarageKitRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(AltarModel.LAYER, AltarModel::createBodyLayer);
        event.registerLayerDefinition(DebugFloorModel.LAYER, DebugFloorModel::createBodyLayer);
        event.registerLayerDefinition(EntityBoxModel.LAYER, EntityBoxModel::createBodyLayer);
        event.registerLayerDefinition(EntityFairyModel.LAYER, EntityFairyModel::createBodyLayer);
        event.registerLayerDefinition(MaidBackpackBigModel.LAYER, MaidBackpackBigModel::createBodyLayer);
        event.registerLayerDefinition(MaidBackpackMiddleModel.LAYER, MaidBackpackMiddleModel::createBodyLayer);
        event.registerLayerDefinition(MaidBackpackSmallModel.LAYER, MaidBackpackSmallModel::createBodyLayer);
        event.registerLayerDefinition(StatueBaseModel.LAYER, StatueBaseModel::createBodyLayer);
        event.registerLayerDefinition(EntityYukkuriModel.LAYER, EntityYukkuriModel::createBodyLayer);
    }
}
