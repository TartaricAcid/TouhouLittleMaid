package com.github.tartaricacid.touhoulittlemaid.client.renderer.sections.dynamic;

import com.mojang.blaze3d.vertex.VertexConsumer;
import org.jetbrains.annotations.NotNull;

/**
 * @author Argon4W
 */
public class DoNothingVertexConsumer implements VertexConsumer {
    @NotNull
    @Override
    public VertexConsumer addVertex(float x, float y, float z) {
        return this;
    }

    @NotNull
    @Override
    public VertexConsumer setColor(int red, int green, int blue, int alpha) {
        return this;
    }

    @NotNull
    @Override
    public VertexConsumer setUv(float u, float v) {
        return this;
    }

    @NotNull
    @Override
    public VertexConsumer setUv1(int u, int v) {
        return this;
    }

    @NotNull
    @Override
    public VertexConsumer setUv2(int u, int v) {
        return this;
    }

    @NotNull
    @Override
    public VertexConsumer setNormal(float normalX, float normalY, float normalZ) {
        return this;
    }
}
