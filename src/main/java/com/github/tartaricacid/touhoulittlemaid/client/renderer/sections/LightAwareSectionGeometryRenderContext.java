package com.github.tartaricacid.touhoulittlemaid.client.renderer.sections;

import com.github.tartaricacid.touhoulittlemaid.client.renderer.sections.cache.CachedEntityModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.sections.cache.RendererBakedModelsCache;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.sections.dynamic.DynamicChunkBuffers;
import com.github.tartaricacid.touhoulittlemaid.compat.sodium.SodiumCompat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Transformation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.event.AddSectionGeometryEvent;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.lighting.LightPipelineAwareModelBlockRenderer;
import net.neoforged.neoforge.client.model.pipeline.TransformingVertexPipeline;
import org.joml.Matrix4f;

/**
 * @author Argon4W
 */
@SuppressWarnings("UnstableApiUsage")
public class LightAwareSectionGeometryRenderContext implements SectionGeometryRenderContext {
    private final AddSectionGeometryEvent.SectionRenderingContext context;
    private final RendererBakedModelsCache cache;
    private final BlockPos pos;
    private final BlockEntity blockEntity;
    private final RandomSource randomSource;
    private final Transformation transformation;

    public LightAwareSectionGeometryRenderContext(AddSectionGeometryEvent.SectionRenderingContext context, RendererBakedModelsCache cache, BlockPos pos, BlockPos regionOrigin, BlockEntity blockEntity) {
        this.context = context;
        this.cache = cache;
        this.pos = pos;
        this.blockEntity = blockEntity;
        this.randomSource = RandomSource.createNewThreadLocalInstance();
        this.transformation = SodiumCompat.isInstalled() ?
                new Transformation(new Matrix4f(context.getPoseStack().last().pose())) :
                new Transformation(new Matrix4f(context.getPoseStack().last().pose()).translate(regionOrigin.getX(), regionOrigin.getY(), regionOrigin.getZ()));
    }

    @Override
    public void renderCachedModel(BakedModel model, PoseStack poseStack, RenderType renderType, int overlay, ModelData modelData) {
        renderCachedModel(model, context.getRegion().getBlockState(pos), poseStack, renderType, overlay, modelData);
    }

    @Override
    public void renderCachedModel(BakedModel model, BlockState blockState, PoseStack poseStack, RenderType renderType, int overlay, ModelData modelData) {
        LightPipelineAwareModelBlockRenderer.render(context.getOrCreateChunkBuffer(renderType), context.getQuadLighter(false), context.getRegion(), cache.getTransformedModel(model, poseStack), blockState, pos, context.getPoseStack(), false, randomSource, 42L, overlay, modelData, renderType);
    }

    @Override
    public void renderUncachedItem(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, int overlay) {
        renderUncachedItem(null, null, 42, itemStack, displayContext, leftHand, poseStack, overlay);
    }

    @Override
    public void renderUncachedItem(Level level, LivingEntity entity, int seed, ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, int overlay) {
        Minecraft.getInstance().getItemRenderer().render(itemStack, displayContext, leftHand, poseStack, getUncachedItemBufferSource(), getPackedLight(), overlay, Minecraft.getInstance().getItemRenderer().getModel(itemStack, level, entity, seed));
    }

    @Override
    public <E extends Entity> void renderCachedCutoutEntity(E entity, ResourceLocation cacheLocation, PoseStack poseStack) {
        renderCachedEntity(entity, cacheLocation, poseStack, getUncachedDynamicCutoutBufferSource(entity));
    }

    @Override
    public <E extends Entity> void renderCachedTranslucentEntity(E entity, ResourceLocation cacheLocation, PoseStack poseStack) {
        renderCachedEntity(entity, cacheLocation, poseStack, getUncachedDynamicTranslucentBufferSource(entity));
    }

    @Override
    public <E extends Entity> void renderCachedCutoutEntity(EntityType<? extends E> entityType, PoseStack poseStack) {
        renderCachedEntity(entityType, poseStack, getUncachedDynamicCutoutBufferSource(DynamicChunkBuffers.getEntityTextureResourceLocation(entityType)));
    }

    @Override
    public <E extends Entity> void renderCachedTranslucentEntity(EntityType<? extends E> entityType, PoseStack poseStack) {
        renderCachedEntity(entityType, poseStack, getUncachedDynamicTranslucentBufferSource(DynamicChunkBuffers.getEntityTextureResourceLocation(entityType)));
    }

    @Override
    public <E extends Entity> void renderCachedMultiEntity(E entity, ResourceLocation cacheLocation, PoseStack poseStack) {
        renderCachedEntity(entity, cacheLocation, poseStack, getUncachedDynamicMultiBufferSource(entity));
    }

    @Override
    public <E extends Entity> void renderCachedMultiEntity(EntityType<? extends E> entityType, PoseStack poseStack) {
        renderCachedEntity(entityType, poseStack, getUncachedDynamicMultiBufferSource(DynamicChunkBuffers.getEntityTextureResourceLocation(entityType)));
    }

    @Override
    public <E extends Entity> void renderCachedEntity(E entity, ResourceLocation cacheLocation, PoseStack poseStack) {
        renderCachedEntity(entity, cacheLocation, poseStack, getUncachedDynamicBufferSource(entity));
    }

    @Override
    public <E extends Entity> void renderCachedEntity(EntityType<? extends E> entityType, PoseStack poseStack) {
        renderCachedEntity(entityType, poseStack, getUncachedDynamicBufferSource(DynamicChunkBuffers.getEntityTextureResourceLocation(entityType)));
    }

    public <E extends Entity> void renderCachedEntity(E entity, ResourceLocation cacheLocation, PoseStack poseStack, MultiBufferSource bufferSource) {
        renderCachedEntity(cache.getEntityModel(entity, cacheLocation), poseStack, bufferSource);
    }

    public <E extends Entity> void renderCachedEntity(EntityType<? extends E> entityType, PoseStack poseStack, MultiBufferSource bufferSource) {
        renderCachedEntity(cache.getEntityModel(entityType, blockEntity.getLevel()), poseStack, bufferSource);
    }

    public void renderCachedEntity(CachedEntityModel model, PoseStack poseStack, MultiBufferSource bufferSource) {
        model.cachedQuads().keySet().forEach(SodiumCompat.isInstalled() ?
                renderType -> LightPipelineAwareModelBlockRenderer.render(bufferSource.getBuffer(renderType), context.getQuadLighter(true), context.getRegion(), cache.getTransformedModel(model, poseStack), context.getRegion().getBlockState(pos), pos, new PoseStack(), false, randomSource, 42L, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, renderType) :
                renderType -> Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(new PoseStack().last(), bufferSource.getBuffer(renderType), context.getRegion().getBlockState(pos), cache.getTransformedModel(model, poseStack), 1.0f, 1.0f, 1.0f, getPackedLight(), OverlayTexture.NO_OVERLAY, ModelData.EMPTY, renderType));
    }

    @Override
    public <E extends Entity> void renderUncachedCutoutEntity(E entity, double x, double y, double z, float rotationYaw, float partialTicks, PoseStack poseStack) {
        Minecraft.getInstance().getEntityRenderDispatcher().render(entity, x, y, z, rotationYaw, partialTicks, poseStack, getUncachedDynamicCutoutBufferSource(entity), getPackedLight());
    }

    @Override
    public <E extends Entity> void renderUncachedTranslucentEntity(E entity, double x, double y, double z, float rotationYaw, float partialTicks, PoseStack poseStack) {
        Minecraft.getInstance().getEntityRenderDispatcher().render(entity, x, y, z, rotationYaw, partialTicks, poseStack, getUncachedDynamicTranslucentBufferSource(entity), getPackedLight());
    }

    @Override
    public <E extends Entity> void renderUncachedCutoutEntity(EntityType<? extends E> entityType, double x, double y, double z, float rotationYaw, float partialTicks, PoseStack poseStack) {
        renderUncachedCutoutEntity(entityType.create(blockEntity.getLevel()), x, y, z, rotationYaw, partialTicks, poseStack);
    }

    @Override
    public <E extends Entity> void renderUncachedTranslucentEntity(EntityType<? extends E> entityType, double x, double y, double z, float rotationYaw, float partialTicks, PoseStack poseStack) {
        renderUncachedTranslucentEntity(entityType.create(blockEntity.getLevel()), x, y, z, rotationYaw, partialTicks, poseStack);
    }

    @Override
    public <E extends Entity> void renderUncachedMultiEntity(E entity, double x, double y, double z, float rotationYaw, float partialTicks, PoseStack poseStack) {
        Minecraft.getInstance().getEntityRenderDispatcher().render(entity, x, y, z, rotationYaw, partialTicks, poseStack, getUncachedDynamicMultiBufferSource(entity), getPackedLight());
    }

    @Override
    public <E extends Entity> void renderUncachedMultiEntity(EntityType<? extends E> entityType, double x, double y, double z, float rotationYaw, float partialTicks, PoseStack poseStack) {
        renderUncachedMultiEntity(entityType.create(blockEntity.getLevel()), x, y, z, rotationYaw, partialTicks, poseStack);
    }

    @Override
    public <E extends Entity> void renderUncachedEntity(E entity, double x, double y, double z, float rotationYaw, float partialTicks, PoseStack poseStack) {
        Minecraft.getInstance().getEntityRenderDispatcher().render(entity, x, y, z, rotationYaw, partialTicks, poseStack, getUncachedDynamicBufferSource(entity), getPackedLight());
    }

    @Override
    public <E extends Entity> void renderUncachedEntity(EntityType<? extends E> entityType, double x, double y, double z, float rotationYaw, float partialTicks, PoseStack poseStack) {
        renderUncachedEntity(entityType.create(blockEntity.getLevel()), x, y, z, rotationYaw, partialTicks, poseStack);
    }

    @Override
    public int getPackedLight() {
        return LightTexture.pack(context.getRegion().getBrightness(LightLayer.BLOCK, pos), context.getRegion().getBrightness(LightLayer.SKY, pos));
    }

    @Override
    public MultiBufferSource getUncachedBufferSource() {
        return renderType -> new QuadLighterVertexConsumer(context.getOrCreateChunkBuffer(renderType), context, pos);
    }

    @Override
    public MultiBufferSource getUncachedItemBufferSource() {
        return SodiumCompat.isInstalled() ? pRenderType -> new QuadLighterVertexConsumer(context, pos) : ignored -> new TransformingVertexPipeline(context.getOrCreateChunkBuffer(Sheets.translucentItemSheet()), transformation);
    }

    @Override
    public <E extends Entity> MultiBufferSource getUncachedDynamicCutoutBufferSource(E entity) {
        return getUncachedDynamicCutoutBufferSource(DynamicChunkBuffers.getEntityTextureResourceLocation(entity));
    }

    @Override
    public <E extends Entity> MultiBufferSource getUncachedDynamicTranslucentBufferSource(E entity) {
        return getUncachedDynamicTranslucentBufferSource(DynamicChunkBuffers.getEntityTextureResourceLocation(entity));
    }

    @Override
    public <E extends Entity> MultiBufferSource getUncachedDynamicAllSingleBufferSource(E entity) {
        return getUncachedDynamicAllSingleBufferSource(DynamicChunkBuffers.getEntityTextureResourceLocation(entity));
    }

    @Override
    public MultiBufferSource getUncachedDynamicCutoutBufferSource(ResourceLocation textureResourceLocation) {
        return ignored -> new TransformingVertexPipeline(context.getOrCreateChunkBuffer(DynamicChunkBuffers.DYNAMIC_CUTOUT_LAYERS.get(textureResourceLocation)), transformation);
    }

    @Override
    public MultiBufferSource getUncachedDynamicTranslucentBufferSource(ResourceLocation textureResourceLocation) {
        return ignored -> new TransformingVertexPipeline(context.getOrCreateChunkBuffer(DynamicChunkBuffers.DYNAMIC_TRANSLUCENT_LAYERS.get(textureResourceLocation)), transformation);
    }

    @Override
    public MultiBufferSource getUncachedDynamicAllSingleBufferSource(ResourceLocation textureResourceLocation) {
        return DynamicChunkBuffers.DYNAMIC_TRANSLUCENT_LAYERS.containsKey(textureResourceLocation) ? getUncachedDynamicTranslucentBufferSource(textureResourceLocation) : getUncachedDynamicCutoutBufferSource(textureResourceLocation);
    }

    @Override
    public <E extends Entity> MultiBufferSource getUncachedDynamicMultiBufferSource(E entity) {
        return getUncachedDynamicMultiBufferSource(DynamicChunkBuffers.getEntityTextureResourceLocation(entity));
    }

    @Override
    public <E extends Entity> MultiBufferSource getUncachedDynamicBufferSource(E entity) {
        return getUncachedDynamicBufferSource(DynamicChunkBuffers.getEntityTextureResourceLocation(entity));
    }

    @Override
    public MultiBufferSource getUncachedDynamicMultiBufferSource(ResourceLocation textureResourceLocation) {
        return renderType -> new TransformingVertexPipeline(context.getOrCreateChunkBuffer(DynamicChunkBuffers.DYNAMIC_MULTI_LAYERS.get(textureResourceLocation).apply(renderType)), transformation);
    }

    @Override
    public MultiBufferSource getUncachedDynamicBufferSource(ResourceLocation textureResourceLocation) {
        return DynamicChunkBuffers.DYNAMIC_MULTI_LAYERS.containsKey(textureResourceLocation) ? getUncachedDynamicMultiBufferSource(textureResourceLocation) : getUncachedDynamicAllSingleBufferSource(textureResourceLocation);
    }
}