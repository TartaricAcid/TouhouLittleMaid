package com.github.tartaricacid.touhoulittlemaid.client.init;

import com.github.tartaricacid.touhoulittlemaid.client.model.*;
import com.github.tartaricacid.touhoulittlemaid.client.model.backpack.*;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.*;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity.*;
import com.github.tartaricacid.touhoulittlemaid.entity.item.*;
import com.github.tartaricacid.touhoulittlemaid.entity.monster.EntityFairy;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.EntityDanmaku;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.EntityThrowPowerPoint;
import com.github.tartaricacid.touhoulittlemaid.tileentity.*;
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
        EntityRenderers.register(EntityTombstone.TYPE, EntityTombstoneRenderer::new);
        EntityRenderers.register(EntitySit.TYPE, EntitySitRenderer::new);

        EntityRenderers.register(EntityType.SLIME, EntityYukkuriSlimeRender::new);
        EntityRenderers.register(EntityType.EXPERIENCE_ORB, ReplaceExperienceOrbRenderer::new);

        BlockEntityRenderers.register(TileEntityAltar.TYPE, TileEntityAltarRenderer::new);
        BlockEntityRenderers.register(TileEntityStatue.TYPE, TileEntityStatueRenderer::new);
        BlockEntityRenderers.register(TileEntityGarageKit.TYPE, TileEntityGarageKitRenderer::new);
        BlockEntityRenderers.register(TileEntityGomoku.TYPE, TileEntityGomokuRenderer::new);
        BlockEntityRenderers.register(TileEntityKeyboard.TYPE, TileEntityKeyboardRenderer::new);
        BlockEntityRenderers.register(TileEntityBookshelf.TYPE, TileEntityBookshelfRenderer::new);
        BlockEntityRenderers.register(TileEntityComputer.TYPE, TileEntityComputerRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(AltarModel.LAYER, AltarModel::createBodyLayer);
        event.registerLayerDefinition(DebugFloorModel.LAYER, DebugFloorModel::createBodyLayer);
        event.registerLayerDefinition(EntityBoxModel.LAYER, EntityBoxModel::createBodyLayer);
        event.registerLayerDefinition(EntityFairyModel.LAYER, EntityFairyModel::createBodyLayer);
        event.registerLayerDefinition(BigBackpackModel.LAYER, BigBackpackModel::createBodyLayer);
        event.registerLayerDefinition(MiddleBackpackModel.LAYER, MiddleBackpackModel::createBodyLayer);
        event.registerLayerDefinition(SmallBackpackModel.LAYER, SmallBackpackModel::createBodyLayer);
        event.registerLayerDefinition(StatueBaseModel.LAYER, StatueBaseModel::createBodyLayer);
        event.registerLayerDefinition(EntityYukkuriModel.LAYER, EntityYukkuriModel::createBodyLayer);
        event.registerLayerDefinition(GomokuModel.LAYER, GomokuModel::createBodyLayer);
        event.registerLayerDefinition(PieceModel.LAYER, PieceModel::createBodyLayer);
        event.registerLayerDefinition(CraftingTableBackpackModel.LAYER, CraftingTableBackpackModel::createBodyLayer);
        event.registerLayerDefinition(EnderChestBackpackModel.LAYER, EnderChestBackpackModel::createBodyLayer);
        event.registerLayerDefinition(FurnaceBackpackModel.LAYER, FurnaceBackpackModel::createBodyLayer);
        event.registerLayerDefinition(TombstoneModel.LAYER, TombstoneModel::createBodyLayer);
        event.registerLayerDefinition(KeyboardModel.LAYER, KeyboardModel::createBodyLayer);
        event.registerLayerDefinition(BookshelfModel.LAYER, BookshelfModel::createBodyLayer);
        event.registerLayerDefinition(ComputerModel.LAYER, ComputerModel::createBodyLayer);
    }
}
