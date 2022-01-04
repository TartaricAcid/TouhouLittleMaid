package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.StatueBaseModel;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGarageKit;
import com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;

import java.util.concurrent.ExecutionException;

import static com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil.clearMaidDataResidue;

public class TileEntityGarageKitRenderer extends TileEntityRenderer<TileEntityGarageKit> {
    private static final StatueBaseModel BASE_MODEL = new StatueBaseModel();
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/statue_base.png");

    public TileEntityGarageKitRenderer(TileEntityRendererDispatcher render) {
        super(render);
    }

    @Override
    public void render(TileEntityGarageKit te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStack.pushPose();
        matrixStack.scale(0.5f, 0.5f, 0.5f);
        matrixStack.translate(1, 0.5, 1);
        matrixStack.mulPose(Vector3f.ZN.rotationDegrees(180));
        IVertexBuilder buffer = bufferIn.getBuffer(RenderType.entityTranslucent(TEXTURE));
        BASE_MODEL.renderToBuffer(matrixStack, buffer, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.popPose();

        CompoundNBT data = te.getExtraData();
        World world = Minecraft.getInstance().level;
        if (data.isEmpty() || world == null) {
            return;
        }

        EntityType.byString(data.getString("id")).ifPresent(type -> {
                    try {
                        renderEntity(te, matrixStack, bufferIn, combinedLightIn, data, world, type);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    private void renderEntity(TileEntityGarageKit te, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, CompoundNBT data, World world, EntityType<?> type) throws ExecutionException {
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

        matrixStack.pushPose();
        matrixStack.scale(0.5f, 0.5f, 0.5f);
        matrixStack.translate(1, 0.21328125, 1);
        switch (te.getFacing()) {
            case EAST:
                matrixStack.mulPose(Vector3f.YP.rotationDegrees(90));
                break;
            case WEST:
                matrixStack.mulPose(Vector3f.YP.rotationDegrees(270));
                break;
            case SOUTH:
                break;
            case NORTH:
            default:
                matrixStack.mulPose(Vector3f.YP.rotationDegrees(180));
                break;
        }
        EntityRendererManager render = Minecraft.getInstance().getEntityRenderDispatcher();
        boolean isShowHitBox = render.shouldRenderHitBoxes();
        render.setRenderHitBoxes(false);
        render.render(entity, 0, 0, 0, 0, 0,
                matrixStack, bufferIn, combinedLightIn);
        render.setRenderHitBoxes(isShowHitBox);
        matrixStack.popPose();
    }
}
