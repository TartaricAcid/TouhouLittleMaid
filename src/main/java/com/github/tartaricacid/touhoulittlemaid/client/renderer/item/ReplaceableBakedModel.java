package com.github.tartaricacid.touhoulittlemaid.client.renderer.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class ReplaceableBakedModel implements IBakedModel {
    private final IBakedModel rawBakedModel;
    private final IBakedModel replacedBakedModel;
    private final Supplier<Boolean> isReplace;

    public ReplaceableBakedModel(IBakedModel rawBakedModel, IBakedModel replacedBakedModel, Supplier<Boolean> isReplace) {
        this.rawBakedModel = rawBakedModel;
        this.replacedBakedModel = replacedBakedModel;
        this.isReplace = isReplace;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState pState, @Nullable Direction pDirection, Random random) {
        return Collections.emptyList();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return rawBakedModel.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return rawBakedModel.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return rawBakedModel.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return rawBakedModel.getParticleIcon();
    }

    @Override
    @Nonnull
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.EMPTY;
    }

    @Override
    public IBakedModel handlePerspective(ItemCameraTransforms.TransformType type, MatrixStack mat) {
        if (isReplace.get()) {
            return this.replacedBakedModel.handlePerspective(type, mat);
        } else {
            return this.rawBakedModel.handlePerspective(type, mat);
        }
    }
}
