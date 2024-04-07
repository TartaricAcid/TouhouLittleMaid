package com.github.tartaricacid.touhoulittlemaid.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class ReplaceableBakedModel implements BakedModel {
    private final BakedModel rawBakedModel;
    private final BakedModel replacedBakedModel;
    private final Supplier<Boolean> isReplace;

    public ReplaceableBakedModel(BakedModel rawBakedModel, BakedModel replacedBakedModel, Supplier<Boolean> isReplace) {
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
    public ItemOverrides getOverrides() {
        if (isReplace.get()) {
            return this.replacedBakedModel.getOverrides();
        } else {
            return this.rawBakedModel.getOverrides();
        }
    }

    @Override
    public BakedModel handlePerspective(ItemTransforms.TransformType cameraTransformType, PoseStack poseStack) {
        if (isReplace.get()) {
            return this.replacedBakedModel.handlePerspective(cameraTransformType, poseStack);
        } else {
            return this.rawBakedModel.handlePerspective(cameraTransformType, poseStack);
        }
    }
}
