package com.github.tartaricacid.touhoulittlemaid.client.renderer.item;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.List;
import java.util.function.Supplier;

@SideOnly(Side.CLIENT)
public class ReplaceableBakedModel implements IBakedModel {
    private IBakedModel rawBakedModel;
    private IBakedModel replacedBakedModel;
    private Supplier<Boolean> isReplace;

    public ReplaceableBakedModel(IBakedModel rawBakedModel, IBakedModel replacedBakedModel, Supplier<Boolean> isReplace) {
        this.rawBakedModel = rawBakedModel;
        this.replacedBakedModel = replacedBakedModel;
        this.isReplace = isReplace;
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        return ImmutableList.of();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return rawBakedModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return rawBakedModel.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Nonnull
    @Override
    public TextureAtlasSprite getParticleTexture() {
        return rawBakedModel.getParticleTexture();
    }

    @Nonnull
    @Override
    public ItemOverrideList getOverrides() {
        return new ItemOverrideList(ImmutableList.of());
    }

    @Nonnull
    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(@Nonnull ItemCameraTransforms.TransformType type) {
        if (isReplace.get()) {
            return this.replacedBakedModel.handlePerspective(type);
        } else {
            return this.rawBakedModel.handlePerspective(type);
        }
    }
}
