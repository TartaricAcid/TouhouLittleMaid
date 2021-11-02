package com.github.tartaricacid.touhoulittlemaid.client.model;

import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.Direction;

public class ModelFloatBox extends ModelRenderer.ModelBox {
    public ModelFloatBox(float texOffX, float texOffY, float x, float y, float z, float width, float height, float depth, float delta, boolean mirror, float texWidth, float texHeight) {
        super((int) texOffX, (int) texOffY, x, y, z, width, height, depth, delta, delta, delta, mirror, texWidth, texHeight);

        this.minX = x;
        this.minY = y;
        this.minZ = z;
        this.maxX = x + width;
        this.maxY = y + height;
        this.maxZ = z + depth;
        this.polygons = new ModelRenderer.TexturedQuad[6];

        float xEnd = x + width;
        float yEnd = y + height;
        float zEnd = z + depth;
        x = x - delta;
        y = y - delta;
        z = z - delta;
        xEnd = xEnd + delta;
        yEnd = yEnd + delta;
        zEnd = zEnd + delta;

        if (mirror) {
            float tmp = xEnd;
            xEnd = x;
            x = tmp;
        }

        ModelRenderer.PositionTextureVertex vertex1 = new ModelRenderer.PositionTextureVertex(x, y, z, 0.0F, 0.0F);
        ModelRenderer.PositionTextureVertex vertex2 = new ModelRenderer.PositionTextureVertex(xEnd, y, z, 0.0F, 8.0F);
        ModelRenderer.PositionTextureVertex vertex3 = new ModelRenderer.PositionTextureVertex(xEnd, yEnd, z, 8.0F, 8.0F);
        ModelRenderer.PositionTextureVertex vertex4 = new ModelRenderer.PositionTextureVertex(x, yEnd, z, 8.0F, 0.0F);
        ModelRenderer.PositionTextureVertex vertex5 = new ModelRenderer.PositionTextureVertex(x, y, zEnd, 0.0F, 0.0F);
        ModelRenderer.PositionTextureVertex vertex6 = new ModelRenderer.PositionTextureVertex(xEnd, y, zEnd, 0.0F, 8.0F);
        ModelRenderer.PositionTextureVertex vertex7 = new ModelRenderer.PositionTextureVertex(xEnd, yEnd, zEnd, 8.0F, 8.0F);
        ModelRenderer.PositionTextureVertex vertex8 = new ModelRenderer.PositionTextureVertex(x, yEnd, zEnd, 8.0F, 0.0F);

        int dx = (int) width;
        int dy = (int) height;
        int dz = (int) depth;

        float p1 = texOffX + dz;
        float p2 = texOffX + dz + dx;
        float p3 = texOffX + dz + dx + dx;
        float p4 = texOffX + dz + dx + dz;
        float p5 = texOffX + dz + dx + dz + dx;
        float p6 = texOffY + dz;
        float p7 = texOffY + dz + dy;
        float p8 = texOffY;
        float p9 = texOffX;

        this.polygons[2] = new ModelRenderer.TexturedQuad(new ModelRenderer.PositionTextureVertex[]{vertex6, vertex5, vertex1, vertex2}, p1, p8, p2, p6, texWidth, texHeight, mirror, Direction.DOWN);
        this.polygons[3] = new ModelRenderer.TexturedQuad(new ModelRenderer.PositionTextureVertex[]{vertex3, vertex4, vertex8, vertex7}, p2, p6, p3, p8, texWidth, texHeight, mirror, Direction.UP);
        this.polygons[1] = new ModelRenderer.TexturedQuad(new ModelRenderer.PositionTextureVertex[]{vertex1, vertex5, vertex8, vertex4}, p9, p6, p1, p7, texWidth, texHeight, mirror, Direction.WEST);
        this.polygons[4] = new ModelRenderer.TexturedQuad(new ModelRenderer.PositionTextureVertex[]{vertex2, vertex1, vertex4, vertex3}, p1, p6, p2, p7, texWidth, texHeight, mirror, Direction.NORTH);
        this.polygons[0] = new ModelRenderer.TexturedQuad(new ModelRenderer.PositionTextureVertex[]{vertex6, vertex2, vertex3, vertex7}, p2, p6, p4, p7, texWidth, texHeight, mirror, Direction.EAST);
        this.polygons[5] = new ModelRenderer.TexturedQuad(new ModelRenderer.PositionTextureVertex[]{vertex5, vertex6, vertex7, vertex8}, p4, p6, p5, p7, texWidth, texHeight, mirror, Direction.SOUTH);
    }
}
