package com.github.tartaricacid.touhoulittlemaid.client.model;

import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.Direction;

public class ModelFloatBox extends ModelRenderer.ModelBox {
    public ModelFloatBox(float texOffX, float texOffY, float x, float y, float z, float width, float height, float depth, float deltaX, float deltaY, float deltaZ, boolean mirorIn, float texWidth, float texHeight) {
        super((int) texOffX, (int) texOffY, x, y, z, width, height, depth, deltaX, deltaY, deltaZ, mirorIn, texWidth, texHeight);

        this.minX = x;
        this.minY = y;
        this.minZ = z;
        this.maxX = x + width;
        this.maxY = y + height;
        this.maxZ = z + depth;
        this.polygons = new ModelRenderer.TexturedQuad[6];

        float f = x + width;
        float f1 = y + height;
        float f2 = z + depth;
        x = x - deltaX;
        y = y - deltaY;
        z = z - deltaZ;
        f = f + deltaX;
        f1 = f1 + deltaY;
        f2 = f2 + deltaZ;

        if (mirorIn) {
            float f3 = f;
            f = x;
            x = f3;
        }

        ModelRenderer.PositionTextureVertex vertex1 = new ModelRenderer.PositionTextureVertex(x, y, z, 0.0F, 0.0F);
        ModelRenderer.PositionTextureVertex vertex2 = new ModelRenderer.PositionTextureVertex(f, y, z, 0.0F, 8.0F);
        ModelRenderer.PositionTextureVertex vertex3 = new ModelRenderer.PositionTextureVertex(f, f1, z, 8.0F, 8.0F);
        ModelRenderer.PositionTextureVertex vertex4 = new ModelRenderer.PositionTextureVertex(x, f1, z, 8.0F, 0.0F);
        ModelRenderer.PositionTextureVertex vertex5 = new ModelRenderer.PositionTextureVertex(x, y, f2, 0.0F, 0.0F);
        ModelRenderer.PositionTextureVertex vertex6 = new ModelRenderer.PositionTextureVertex(f, y, f2, 0.0F, 8.0F);
        ModelRenderer.PositionTextureVertex vertex7 = new ModelRenderer.PositionTextureVertex(f, f1, f2, 8.0F, 8.0F);
        ModelRenderer.PositionTextureVertex vertex8 = new ModelRenderer.PositionTextureVertex(x, f1, f2, 8.0F, 0.0F);

        int dx = (int) width;
        int dy = (int) height;
        int dz = (int) depth;

        float f5 = texOffX + dz;
        float f6 = texOffX + dz + dx;
        float f7 = texOffX + dz + dx + dx;
        float f8 = texOffX + dz + dx + dz;
        float f9 = texOffX + dz + dx + dz + dx;
        float f11 = texOffY + dz;
        float f12 = texOffY + dz + dy;

        this.polygons[2] = new ModelRenderer.TexturedQuad(new ModelRenderer.PositionTextureVertex[]{vertex6, vertex5, vertex1, vertex2}, f5, texOffY, f6, f11, texWidth, texHeight, mirorIn, Direction.DOWN);
        this.polygons[3] = new ModelRenderer.TexturedQuad(new ModelRenderer.PositionTextureVertex[]{vertex3, vertex4, vertex8, vertex7}, f6, f11, f7, texOffY, texWidth, texHeight, mirorIn, Direction.UP);
        this.polygons[1] = new ModelRenderer.TexturedQuad(new ModelRenderer.PositionTextureVertex[]{vertex1, vertex5, vertex8, vertex4}, texOffX, f11, f5, f12, texWidth, texHeight, mirorIn, Direction.WEST);
        this.polygons[4] = new ModelRenderer.TexturedQuad(new ModelRenderer.PositionTextureVertex[]{vertex2, vertex1, vertex4, vertex3}, f5, f11, f6, f12, texWidth, texHeight, mirorIn, Direction.NORTH);
        this.polygons[0] = new ModelRenderer.TexturedQuad(new ModelRenderer.PositionTextureVertex[]{vertex6, vertex2, vertex3, vertex7}, f6, f11, f8, f12, texWidth, texHeight, mirorIn, Direction.EAST);
        this.polygons[5] = new ModelRenderer.TexturedQuad(new ModelRenderer.PositionTextureVertex[]{vertex5, vertex6, vertex7, vertex8}, f8, f11, f9, f12, texWidth, texHeight, mirorIn, Direction.SOUTH);
    }
}
