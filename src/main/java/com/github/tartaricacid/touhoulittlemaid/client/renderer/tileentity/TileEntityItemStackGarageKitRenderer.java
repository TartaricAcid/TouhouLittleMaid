package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.StatueBaseModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.ItemGarageKit;
import com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.concurrent.ExecutionException;

import static com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil.clearMaidDataResidue;

public class TileEntityItemStackGarageKitRenderer extends ItemStackTileEntityRenderer {
    private static final StatueBaseModel BASE_MODEL = new StatueBaseModel();
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/statue_base.png");

    @Override
    public void renderByItem(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStack.pushPose();
        matrixStack.scale(0.5f, 0.5f, 0.5f);
        matrixStack.translate(1, 0.5, 1);
        matrixStack.mulPose(Vector3f.ZN.rotationDegrees(180));
        IVertexBuilder buffer = bufferIn.getBuffer(RenderType.entityTranslucent(TEXTURE));
        BASE_MODEL.renderToBuffer(matrixStack, buffer, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.popPose();

        CompoundNBT data = ItemGarageKit.getMaidData(stack);
        World world = Minecraft.getInstance().level;
        if (data.isEmpty() || world == null) {
            return;
        }

        EntityType.byString(data.getString("id")).ifPresent(type -> {
                    try {
                        renderEntity(matrixStack, bufferIn, combinedLightIn, data, world, type);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    private void renderEntity(MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, CompoundNBT data, World world, EntityType<?> type) throws ExecutionException {
        Entity entity = EntityCacheUtil.ENTITY_CACHE.get(type, () -> {
            Entity e = type.create(world);
            if (e == null) {
                return new EntityMaid(world);
            } else {
                return e;
            }
        });

        float renderItemScale = 1;
        if (entity instanceof EntityMaid) {
            EntityMaid maid = (EntityMaid) entity;
            clearMaidDataResidue(maid, true);
            entity.load(data);
            if (data.contains(EntityMaid.MODEL_ID_TAG, Constants.NBT.TAG_STRING)) {
                String modelId = data.getString(EntityMaid.MODEL_ID_TAG);
                renderItemScale = CustomPackLoader.MAID_MODELS.getModelRenderItemScale(modelId);
            }
        }

        matrixStack.pushPose();
        matrixStack.scale(0.5f, 0.5f, 0.5f);
        matrixStack.scale(renderItemScale, renderItemScale, renderItemScale);
        matrixStack.translate(1, 0.21328125, 1);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180));
        EntityRendererManager render = Minecraft.getInstance().getEntityRenderDispatcher();
        boolean isShowHitBox = render.shouldRenderHitBoxes();
        render.setRenderHitBoxes(false);
        RenderSystem.runAsFancy(() -> {
            render.render(entity, 0, 0, 0, 0, 0,
                    matrixStack, bufferIn, combinedLightIn);
        });
        render.setRenderHitBoxes(isShowHitBox);
        matrixStack.popPose();
    }
}
