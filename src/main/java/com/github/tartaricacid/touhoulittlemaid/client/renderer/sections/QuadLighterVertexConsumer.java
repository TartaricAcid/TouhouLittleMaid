package com.github.tartaricacid.touhoulittlemaid.client.renderer.sections;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.client.event.AddSectionGeometryEvent;
import net.neoforged.neoforge.client.model.IQuadTransformer;
import net.neoforged.neoforge.client.model.QuadTransformers;
import net.neoforged.neoforge.client.model.lighting.QuadLighter;
import org.jetbrains.annotations.NotNull;

/**
 * @author Argon4W
 */
public class QuadLighterVertexConsumer implements VertexConsumer {
    private final VertexConsumer vertexConsumer;
    private final AddSectionGeometryEvent.SectionRenderingContext context;
    private final BlockPos pos;

    public QuadLighterVertexConsumer(VertexConsumer vertexConsumer, AddSectionGeometryEvent.SectionRenderingContext context, BlockPos pos) {
        this.vertexConsumer = vertexConsumer;
        this.context = context;
        this.pos = pos;
    }

    public QuadLighterVertexConsumer(AddSectionGeometryEvent.SectionRenderingContext context, BlockPos pos) {
        this.vertexConsumer = context.getOrCreateChunkBuffer(RenderType.translucent());
        this.context = context;
        this.pos = pos;
    }

    @NotNull
    @Override
    public VertexConsumer addVertex(float x, float y, float z) {
        return vertexConsumer.addVertex(x, y, z);
    }

    @NotNull
    @Override
    public VertexConsumer setColor(int red, int green, int blue, int alpha) {
        return vertexConsumer.setColor(red, green, blue, alpha);
    }

    @NotNull
    @Override
    public VertexConsumer setUv(float u, float v) {
        return vertexConsumer.setUv(u, v);
    }

    @NotNull
    @Override
    public VertexConsumer setUv1(int u, int v) {
        return vertexConsumer.setUv1(u, v);
    }

    @NotNull
    @Override
    public VertexConsumer setUv2(int u, int v) {
        return vertexConsumer.setUv2(u, v);
    }

    @NotNull
    @Override
    public VertexConsumer setNormal(float normalX, float normalY, float normalZ) {
        return vertexConsumer.setNormal(normalX, normalY, normalZ);
    }

    @Override
    public void putBulkData(@NotNull PoseStack.Pose pose, @NotNull BakedQuad bakedQuad, float red, float green, float blue, float alpha, int packedLight, int packedOverlay) {
        putBulkData(pose, bakedQuad, red, green, blue, alpha, packedLight, packedOverlay, true);
    }

    @Override
    public void putBulkData(@NotNull PoseStack.Pose pose, @NotNull BakedQuad bakedQuad, float red, float green, float blue, float alpha, int packedLight, int packedOverlay, boolean readExistingColor) {
        IQuadTransformer transformer = QuadTransformers.applying(new Transformation(pose.pose()));
        QuadLighter lighter = context.getQuadLighter(false);
        BakedQuad quad = transformer.process(bakedQuad);

        lighter.setup(context.getRegion(), pos, context.getRegion().getBlockState(pos));
        lighter.computeLightingForQuad(quad);

        vertexConsumer.putBulkData(context.getPoseStack().last(), quad, lighter.getComputedBrightness(), red, green, blue, alpha, lighter.getComputedLightmap(), packedOverlay, readExistingColor);
        lighter.reset();
    }
}
