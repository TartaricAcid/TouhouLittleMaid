package com.github.tartaricacid.touhoulittlemaid.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PerspectiveBakedModel implements BakedModel {
    private final BakedModel bakedModel2d;
    private final BakedModel bakedModel3d;

    public PerspectiveBakedModel(BakedModel bakedModel2d, BakedModel bakedModel3d) {
        this.bakedModel2d = bakedModel2d;
        this.bakedModel3d = bakedModel3d;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
        return Collections.emptyList();
    }

    @Override
    public boolean usesBlockLight() {
        return bakedModel2d.usesBlockLight();
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.bakedModel2d.getParticleIcon();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return bakedModel2d.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return bakedModel2d.isGui3d();
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Nonnull
    @Override
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }

    @Override
    public BakedModel handlePerspective(ItemTransforms.TransformType type, PoseStack mat) {
        if (type == ItemTransforms.TransformType.GUI || type == ItemTransforms.TransformType.FIXED) {
            return bakedModel2d.handlePerspective(type, mat);
        } else {
            return bakedModel3d.handlePerspective(type, mat);
        }
    }
}
