package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.StatueBaseModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.ItemGarageKit;
import com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent.MODEL_ID_TAG_NAME;
import static com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil.clearMaidDataResidue;

public class TileEntityItemStackGarageKitRenderer extends BlockEntityWithoutLevelRenderer {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/statue_base.png");
    private static StatueBaseModel BASE_MODEL;

    public TileEntityItemStackGarageKitRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);
        BASE_MODEL = new StatueBaseModel(modelSet.bakeLayer(StatueBaseModel.LAYER));
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        poseStack.pushPose();
        poseStack.scale(0.5f, 0.5f, 0.5f);
        poseStack.translate(1, 0.5, 1);
        poseStack.mulPose(Axis.ZN.rotationDegrees(180));
        VertexConsumer buffer = bufferIn.getBuffer(RenderType.entityTranslucent(TEXTURE));
        BASE_MODEL.renderToBuffer(poseStack, buffer, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();

        CustomData data = ItemGarageKit.getMaidData(stack);
        Level world = Minecraft.getInstance().level;
        if (data.isEmpty() || world == null) {
            return;
        }

        EntityType.byString(data.read(Codec.STRING.fieldOf("id")).getOrThrow()).ifPresent(type -> {
                    try {
                        renderEntity(poseStack, bufferIn, combinedLightIn, data.copyTag(), world, type);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    private void renderEntity(PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, CompoundTag data, Level world, EntityType<?> type) throws ExecutionException {
        Entity entity = EntityCacheUtil.ENTITY_CACHE.get(type, () -> {
            Entity e = type.create(world);
            return Objects.requireNonNullElseGet(e, () -> new EntityMaid(world));
        });

        float renderItemScale = 1;
        entity.load(data);
        if (entity instanceof EntityMaid) {
            EntityMaid maid = (EntityMaid) entity;
            clearMaidDataResidue(maid, true);
            if (data.contains(MODEL_ID_TAG_NAME, Tag.TAG_STRING)) {
                String modelId = data.getString(MODEL_ID_TAG_NAME);
                renderItemScale = CustomPackLoader.MAID_MODELS.getModelRenderItemScale(modelId);
            }
        }

        poseStack.pushPose();
        poseStack.scale(0.5f, 0.5f, 0.5f);
        poseStack.scale(renderItemScale, renderItemScale, renderItemScale);
        poseStack.translate(1, 0.21328125, 1);
        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        EntityRenderDispatcher render = Minecraft.getInstance().getEntityRenderDispatcher();
        boolean isShowHitBox = render.shouldRenderHitBoxes();
        render.setRenderHitBoxes(false);
        RenderSystem.runAsFancy(() -> {
            render.render(entity, 0, 0, 0, 0, 0,
                    poseStack, bufferIn, combinedLightIn);
        });
        render.setRenderHitBoxes(isShowHitBox);
        poseStack.popPose();
    }
}
