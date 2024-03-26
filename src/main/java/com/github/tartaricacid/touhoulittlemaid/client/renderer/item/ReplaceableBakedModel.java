package com.github.tartaricacid.touhoulittlemaid.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
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
    public List<BakedQuad> getQuads(@Nullable BlockState pState, @Nullable Direction pDirection, RandomSource random) {
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
    public BakedModel applyTransform(ItemDisplayContext type, PoseStack mat, boolean applyLeftHandTransform) {
        if (isReplace.get()) {
            return this.replacedBakedModel.applyTransform(type, mat, applyLeftHandTransform);
        } else {
            return this.rawBakedModel.applyTransform(type, mat, applyLeftHandTransform);
        }
    }
}
