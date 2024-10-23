package com.github.tartaricacid.touhoulittlemaid.client.renderer.sections;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;

/**
 * @author Argon4W
 */
public interface SectionGeometryRenderContext {
    void renderCachedModel(BakedModel model, PoseStack poseStack, RenderType renderType, int overlay, ModelData modelData);

    void renderCachedModel(BakedModel model, BlockState blockState, PoseStack poseStack, RenderType renderType, int overlay, ModelData modelData);

    void renderUncachedItem(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, int overlay);

    void renderUncachedItem(Level level, LivingEntity entity, int seed, ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, int overlay);

    <E extends Entity> void renderCachedCutoutEntity(E entity, ResourceLocation cacheLocation, PoseStack poseStack);

    <E extends Entity> void renderCachedTranslucentEntity(E entity, ResourceLocation cacheLocation, PoseStack poseStack);

    <E extends Entity> void renderCachedCutoutEntity(EntityType<? extends E> entityType, PoseStack poseStack);

    <E extends Entity> void renderCachedTranslucentEntity(EntityType<? extends E> entityType, PoseStack poseStack);

    <E extends Entity> void renderCachedMultiEntity(E entity, ResourceLocation cacheLocation, PoseStack poseStack);

    <E extends Entity> void renderCachedMultiEntity(EntityType<? extends E> entityType, PoseStack poseStack);

    <E extends Entity> void renderCachedEntity(E entity, ResourceLocation cacheLocation, PoseStack poseStack);

    <E extends Entity> void renderCachedEntity(EntityType<? extends E> entityType, PoseStack poseStack);

    <E extends Entity> void renderUncachedCutoutEntity(E entity, double x, double y, double z, float rotationYaw, float partialTicks, PoseStack poseStack);

    <E extends Entity> void renderUncachedTranslucentEntity(E entity, double x, double y, double z, float rotationYaw, float partialTicks, PoseStack poseStack);

    <E extends Entity> void renderUncachedCutoutEntity(EntityType<? extends E> entityType, double x, double y, double z, float rotationYaw, float partialTicks, PoseStack poseStack);

    <E extends Entity> void renderUncachedTranslucentEntity(EntityType<? extends E> entityType, double x, double y, double z, float rotationYaw, float partialTicks, PoseStack poseStack);

    <E extends Entity> void renderUncachedMultiEntity(E entity, double x, double y, double z, float rotationYaw, float partialTicks, PoseStack poseStack);

    <E extends Entity> void renderUncachedMultiEntity(EntityType<? extends E> entityType, double x, double y, double z, float rotationYaw, float partialTicks, PoseStack poseStack);

    <E extends Entity> void renderUncachedEntity(E entity, double x, double y, double z, float rotationYaw, float partialTicks, PoseStack poseStack);

    <E extends Entity> void renderUncachedEntity(EntityType<? extends E> entityType, double x, double y, double z, float rotationYaw, float partialTicks, PoseStack poseStack);

    int getPackedLight();

    MultiBufferSource getUncachedBufferSource();

    MultiBufferSource getUncachedItemBufferSource();

    <E extends Entity> MultiBufferSource getUncachedDynamicCutoutBufferSource(E entity);

    <E extends Entity> MultiBufferSource getUncachedDynamicTranslucentBufferSource(E entity);

    <E extends Entity> MultiBufferSource getUncachedDynamicAllSingleBufferSource(E entity);

    MultiBufferSource getUncachedDynamicCutoutBufferSource(ResourceLocation textureResourceLocation);

    MultiBufferSource getUncachedDynamicTranslucentBufferSource(ResourceLocation textureResourceLocation);

    MultiBufferSource getUncachedDynamicAllSingleBufferSource(ResourceLocation textureResourceLocation);

    <E extends Entity> MultiBufferSource getUncachedDynamicMultiBufferSource(E entity);

    <E extends Entity> MultiBufferSource getUncachedDynamicBufferSource(E entity);

    MultiBufferSource getUncachedDynamicMultiBufferSource(ResourceLocation textureResourceLocation);

    MultiBufferSource getUncachedDynamicBufferSource(ResourceLocation textureResourceLocation);
}
