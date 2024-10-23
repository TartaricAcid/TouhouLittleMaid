package com.github.tartaricacid.touhoulittlemaid.client.renderer.sections.cache;

import com.github.tartaricacid.touhoulittlemaid.compat.iris.IrisCompat;
import com.github.tartaricacid.touhoulittlemaid.util.EntryStreams;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.common.util.TriState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Argon4W
 */
@SuppressWarnings("deprecation")
public record CachedEntityModel(Map<RenderType, List<BakedQuad>> cachedQuads) implements BakedModel {
    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType) {
        return side == null ? cachedQuads.getOrDefault(renderType, List.of()) : List.of();
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, @NotNull RandomSource random) {
        throw new IllegalStateException("Should not call deprecated getQuads from CachedEntityModel");
    }

    @Override
    public boolean useAmbientOcclusion() {
        throw new IllegalStateException("Should not call legacy useAmbientOcclusion from CachedEntityModel");
    }

    @Override
    public boolean isGui3d() {
        throw new IllegalStateException("Should not call isGui3d from CachedEntityModel");
    }

    @Override
    public boolean usesBlockLight() {
        throw new IllegalStateException("Should not call usesBlockLight from CachedEntityModel");
    }

    @Override
    public boolean isCustomRenderer() {
        throw new IllegalStateException("Should not call isCustomRenderer from CachedEntityModel");
    }

    @NotNull
    @Override
    public TextureAtlasSprite getParticleIcon() {
        throw new IllegalStateException("Should not call getParticleIcon from CachedEntityModel");
    }

    @NotNull
    @Override
    public ItemOverrides getOverrides() {
        throw new IllegalStateException("Should not call getOverrides from CachedEntityModel");
    }

    @NotNull
    @Override
    public ItemTransforms getTransforms() {
        throw new IllegalStateException("Should not call deprecated getTransforms from CachedEntityModel");
    }

    @NotNull
    @Override
    public TriState useAmbientOcclusion(@NotNull BlockState state, @NotNull ModelData data, @NotNull RenderType renderType) {
        throw new IllegalStateException("Should not call getOverrides from CachedEntityModel");
    }

    @NotNull
    @Override
    public BakedModel applyTransform(@NotNull ItemDisplayContext transformType, @NotNull PoseStack poseStack, boolean applyLeftHandTransform) {
        throw new IllegalStateException("Should not call getOverrides from CachedEntityModel");
    }

    @NotNull
    @Override
    public ModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData modelData) {
        throw new IllegalStateException("Should not call getOverrides from CachedEntityModel");
    }

    @NotNull
    @Override
    public TextureAtlasSprite getParticleIcon(@NotNull ModelData data) {
        throw new IllegalStateException("Should not call getOverrides from CachedEntityModel");
    }

    @NotNull
    @Override
    public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
        throw new IllegalStateException("Should not call getOverrides from CachedEntityModel");
    }

    @NotNull
    @Override
    public List<RenderType> getRenderTypes(@NotNull ItemStack itemStack, boolean fabulous) {
        throw new IllegalStateException("Should not call getOverrides from CachedEntityModel");
    }

    @NotNull
    @Override
    public List<BakedModel> getRenderPasses(@NotNull ItemStack itemStack, boolean fabulous) {
        throw new IllegalStateException("Should not call getOverrides from CachedEntityModel");
    }

    public static <E extends Entity> CachedEntityModel create(EntityType<? extends E> entityType, Level level) {
        return create(entityType.create(level));
    }

    public static <E extends Entity> CachedEntityModel create(E entity) {
        return new CachedEntityModel(Util.make(new HashMap<RenderType, QuadListBakingVertexConsumer>(), builder -> Minecraft.getInstance().getEntityRenderDispatcher().render(entity, 0, 0, 0, 0, 0, new PoseStack(), IrisCompat.getRenderType(builder), 0)).entrySet().stream().map(EntryStreams.mapEntryValue(QuadListBakingVertexConsumer::getQuads)).collect(EntryStreams.collect()));
    }
}
