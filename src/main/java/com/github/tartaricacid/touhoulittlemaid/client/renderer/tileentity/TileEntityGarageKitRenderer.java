package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.StatueBaseModel;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGarageKit;
import com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.concurrent.ExecutionException;

import static com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil.clearMaidDataResidue;

public class TileEntityGarageKitRenderer implements BlockEntityRenderer<TileEntityGarageKit> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/statue_base.png");
    private final StatueBaseModel BASE_MODEL;

    public TileEntityGarageKitRenderer(BlockEntityRendererProvider.Context context) {
        BASE_MODEL = new StatueBaseModel(context.bakeLayer(StatueBaseModel.LAYER));
    }

    @Override
    public void render(TileEntityGarageKit te, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        poseStack.pushPose();
        poseStack.scale(0.5f, 0.5f, 0.5f);
        poseStack.translate(1, 0.5, 1);
        poseStack.mulPose(Vector3f.ZN.rotationDegrees(180));
        VertexConsumer buffer = bufferIn.getBuffer(RenderType.entityTranslucent(TEXTURE));
        BASE_MODEL.renderToBuffer(poseStack, buffer, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();

        CompoundTag data = te.getExtraData();
        Level world = Minecraft.getInstance().level;
        if (data.isEmpty() || world == null) {
            return;
        }

        EntityType.byString(data.getString("id")).ifPresent(type -> {
                    try {
                        renderEntity(te, poseStack, bufferIn, combinedLightIn, data, world, type);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    private void renderEntity(TileEntityGarageKit te, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, CompoundTag data, Level world, EntityType<?> type) throws ExecutionException {
        Entity entity = EntityCacheUtil.ENTITY_CACHE.get(type, () -> {
            Entity e = type.create(world);
            if (e == null) {
                return new EntityMaid(world);
            } else {
                return e;
            }
        });

        entity.load(data);
        if (entity instanceof EntityMaid) {
            EntityMaid maid = (EntityMaid) entity;
            clearMaidDataResidue(maid, true);
        }

        poseStack.pushPose();
        poseStack.scale(0.5f, 0.5f, 0.5f);
        poseStack.translate(1, 0.21328125, 1);
        switch (te.getFacing()) {
            case EAST:
                poseStack.mulPose(Vector3f.YP.rotationDegrees(90));
                break;
            case WEST:
                poseStack.mulPose(Vector3f.YP.rotationDegrees(270));
                break;
            case SOUTH:
                break;
            case NORTH:
            default:
                poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
                break;
        }
        EntityRenderDispatcher render = Minecraft.getInstance().getEntityRenderDispatcher();
        boolean isShowHitBox = render.shouldRenderHitBoxes();
        render.setRenderHitBoxes(false);
        render.render(entity, 0, 0, 0, 0, 0,
                poseStack, bufferIn, combinedLightIn);
        render.setRenderHitBoxes(isShowHitBox);
        poseStack.popPose();
    }
}
