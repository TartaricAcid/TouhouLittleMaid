package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.client.render.MaidRenderState;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.SimpleBedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityStatue;
import com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
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

import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader.STATUE_BASE;
import static com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil.clearMaidDataResidue;

public class TileEntityStatueRenderer implements BlockEntityRenderer<TileEntityStatue> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/bedrock/block/statue_base.png");
    private final SimpleBedrockModel<Entity> BASE_MODEL;

    public TileEntityStatueRenderer(BlockEntityRendererProvider.Context context) {
        BASE_MODEL = BedrockModelLoader.getModel(STATUE_BASE);
    }

    @Override
    public void render(TileEntityStatue te, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (!te.isCoreBlock()) {
            return;
        }

        poseStack.pushPose();
        this.setTranslateAndPose(te, poseStack);
        poseStack.mulPose(Axis.ZN.rotationDegrees(180));
        VertexConsumer buffer = bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        BASE_MODEL.renderToBuffer(poseStack, buffer, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();

        CompoundTag data = te.getExtraMaidData();
        Level world = Minecraft.getInstance().level;
        if (data == null || world == null) {
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

    private void renderEntity(TileEntityStatue te, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, CompoundTag data, Level world, EntityType<?> type) throws ExecutionException {
        Entity entity;
        if (type.equals(InitEntities.MAID.get())) {
            long posId = te.getBlockPos().asLong();
            entity = EntityCacheUtil.STATUE_CACHE.get(posId, () -> new EntityMaid(world));
        } else {
            entity = EntityCacheUtil.ENTITY_CACHE.get(type, () -> {
                Entity e = type.create(world);
                return Objects.requireNonNullElseGet(e, () -> new EntityMaid(world));
            });
        }

        entity.load(data);
        if (entity instanceof EntityMaid maid) {
            clearMaidDataResidue(maid, true);
            maid.renderState = MaidRenderState.STATUE;
        }

        float size = te.getSize().getScale();
        float offset = 0;
        if (te.getSize() == TileEntityStatue.Size.MIDDLE) {
            offset = 1.0f / 4.0f;
        } else if (te.getSize() == TileEntityStatue.Size.BIG) {
            offset = 1.0f / 3.0f;
        }

        poseStack.pushPose();
        poseStack.scale(size, size, size);
        poseStack.translate(0.5 / size, 0.21328125, 0.5 / size);
        switch (te.getFacing()) {
            case EAST:
                poseStack.mulPose(Axis.YP.rotationDegrees(90));
                break;
            case WEST:
                poseStack.mulPose(Axis.YP.rotationDegrees(270));
                break;
            case SOUTH:
                break;
            case NORTH:
            default:
                poseStack.mulPose(Axis.YP.rotationDegrees(180));
                break;
        }
        EntityRenderDispatcher render = Minecraft.getInstance().getEntityRenderDispatcher();
        boolean isShowHitBox = render.shouldRenderHitBoxes();
        render.setRenderHitBoxes(false);
        render.render(entity, offset, 0, -offset, 0, 0,
                poseStack, bufferIn, combinedLightIn);
        render.setRenderHitBoxes(isShowHitBox);
        poseStack.popPose();
    }

    private void setTranslateAndPose(TileEntityStatue te, PoseStack poseStack) {
        float size = te.getSize().getScale();
        float offset = 0;
        if (te.getSize() == TileEntityStatue.Size.MIDDLE) {
            offset = 1.0f / 4.0f;
        } else if (te.getSize() == TileEntityStatue.Size.BIG) {
            offset = 1.0f / 3.0f;
        }

        switch (te.getFacing()) {
            case EAST:
                poseStack.translate(-offset * size, 0, -offset * size);
                break;
            case NORTH:
                poseStack.translate(-offset * size, 0, offset * size);
                break;
            case WEST:
                poseStack.translate(offset * size, 0, offset * size);
                break;
            case SOUTH:
                poseStack.translate(offset * size, 0, -offset * size);
                break;
            default:
                poseStack.translate(0, 0, 0);
        }
        poseStack.scale(size, size, size);
        poseStack.translate(0.5 / size, 1.5, 0.5 / size);
    }
}
