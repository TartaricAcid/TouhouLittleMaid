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
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.MaidFishingHook;
import com.github.tartaricacid.touhoulittlemaid.tileentity.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.EntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
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
        EntityRenderers.register(EntityBroom.TYPE, EntityBroomRender::new);
        EntityRenderers.register(MaidFishingHook.TYPE, MaidFishingHookRenderer::new);

        EntityRenderers.register(EntityType.SLIME, EntityYukkuriSlimeRender::new);
        EntityRenderers.register(EntityType.MAGMA_CUBE, EntityMarisaYukkuriSlimeRender::new);
        EntityRenderers.register(EntityType.EXPERIENCE_ORB, ReplaceExperienceOrbRenderer::new);

        BlockEntityRenderers.register(TileEntityAltar.TYPE, TileEntityAltarRenderer::new);
        BlockEntityRenderers.register(TileEntityStatue.TYPE, TileEntityStatueRenderer::new);
        BlockEntityRenderers.register(TileEntityGarageKit.TYPE, TileEntityGarageKitRenderer::new);
        BlockEntityRenderers.register(TileEntityGomoku.TYPE, TileEntityGomokuRenderer::new);
        BlockEntityRenderers.register(TileEntityCChess.TYPE, TileEntityCChessRenderer::new);
        BlockEntityRenderers.register(TileEntityWChess.TYPE, TileEntityWChessRenderer::new);
        BlockEntityRenderers.register(TileEntityKeyboard.TYPE, TileEntityKeyboardRenderer::new);
        BlockEntityRenderers.register(TileEntityBookshelf.TYPE, TileEntityBookshelfRenderer::new);
        BlockEntityRenderers.register(TileEntityComputer.TYPE, TileEntityComputerRenderer::new);
        BlockEntityRenderers.register(TileEntityShrine.TYPE, TileEntityShrineRenderer::new);
        BlockEntityRenderers.register(TileEntityShrine.TYPE, TileEntityShrineRenderer::new);
        BlockEntityRenderers.register(TileEntityPicnicMat.TYPE, PicnicMatRender::new);
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(AltarModel.LAYER, AltarModel::createBodyLayer);
        event.registerLayerDefinition(DebugFloorModel.LAYER, DebugFloorModel::createBodyLayer);
        event.registerLayerDefinition(EntityBoxModel.LAYER, EntityBoxModel::createBodyLayer);
        event.registerLayerDefinition(EntityFairyModel.LAYER, EntityFairyModel::createBodyLayer);
        event.registerLayerDefinition(NewEntityFairyModel.LAYER, NewEntityFairyModel::createBodyLayer);
        event.registerLayerDefinition(BigBackpackModel.LAYER, BigBackpackModel::createBodyLayer);
        event.registerLayerDefinition(MiddleBackpackModel.LAYER, MiddleBackpackModel::createBodyLayer);
        event.registerLayerDefinition(SmallBackpackModel.LAYER, SmallBackpackModel::createBodyLayer);
        event.registerLayerDefinition(StatueBaseModel.LAYER, StatueBaseModel::createBodyLayer);
        event.registerLayerDefinition(EntityYukkuriModel.LAYER, EntityYukkuriModel::createBodyLayer);
        event.registerLayerDefinition(EntityMarisaYukkuriModel.LAYER, EntityMarisaYukkuriModel::createBodyLayer);
        event.registerLayerDefinition(GomokuModel.LAYER, GomokuModel::createBodyLayer);
        event.registerLayerDefinition(CChessModel.LAYER, CChessModel::createBodyLayer);
        event.registerLayerDefinition(WChessModel.LAYER, WChessModel::createBodyLayer);
        event.registerLayerDefinition(PieceModel.LAYER, PieceModel::createBodyLayer);
        event.registerLayerDefinition(WChessPiecesModel.LAYER, WChessPiecesModel::createBodyLayer);
        event.registerLayerDefinition(CraftingTableBackpackModel.LAYER, CraftingTableBackpackModel::createBodyLayer);
        event.registerLayerDefinition(EnderChestBackpackModel.LAYER, EnderChestBackpackModel::createBodyLayer);
        event.registerLayerDefinition(FurnaceBackpackModel.LAYER, FurnaceBackpackModel::createBodyLayer);
        event.registerLayerDefinition(TankBackpackModel.LAYER, TankBackpackModel::createBodyLayer);
        event.registerLayerDefinition(TombstoneModel.LAYER, TombstoneModel::createBodyLayer);
        event.registerLayerDefinition(KeyboardModel.LAYER, KeyboardModel::createBodyLayer);
        event.registerLayerDefinition(BookshelfModel.LAYER, BookshelfModel::createBodyLayer);
        event.registerLayerDefinition(ComputerModel.LAYER, ComputerModel::createBodyLayer);
        event.registerLayerDefinition(ShrineModel.LAYER, ShrineModel::createBodyLayer);
        event.registerLayerDefinition(MaidBannerModel.LAYER, MaidBannerModel::createBodyLayer);
        event.registerLayerDefinition(BroomModel.LAYER, BroomModel::createBodyLayer);
        event.registerLayerDefinition(PicnicBasketModel.LAYER, PicnicBasketModel::createBodyLayer);
        event.registerLayerDefinition(PicnicMatModel.LAYER, PicnicMatModel::createBodyLayer);
    }
}
