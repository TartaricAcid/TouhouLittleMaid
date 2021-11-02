package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.github.tartaricacid.touhoulittlemaid.client.model.pojo.FaceItem;
import com.github.tartaricacid.touhoulittlemaid.client.model.pojo.FaceUVsItem;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.Direction;

public class ModelFaceFloatBox extends ModelRenderer.ModelBox {
    public ModelFaceFloatBox(float x, float y, float z, float width, float height, float depth, float delta, float texWidth, float texHeight, FaceUVsItem faces) {
        super(0, 0, x, y, z, width, height, depth, delta, delta, delta, false, texWidth, texHeight);

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

        ModelRenderer.PositionTextureVertex vertex1 = new ModelRenderer.PositionTextureVertex(x, y, z, 0.0F, 0.0F);
        ModelRenderer.PositionTextureVertex vertex2 = new ModelRenderer.PositionTextureVertex(xEnd, y, z, 0.0F, 8.0F);
        ModelRenderer.PositionTextureVertex vertex3 = new ModelRenderer.PositionTextureVertex(xEnd, yEnd, z, 8.0F, 8.0F);
        ModelRenderer.PositionTextureVertex vertex4 = new ModelRenderer.PositionTextureVertex(x, yEnd, z, 8.0F, 0.0F);
        ModelRenderer.PositionTextureVertex vertex5 = new ModelRenderer.PositionTextureVertex(x, y, zEnd, 0.0F, 0.0F);
        ModelRenderer.PositionTextureVertex vertex6 = new ModelRenderer.PositionTextureVertex(xEnd, y, zEnd, 0.0F, 8.0F);
        ModelRenderer.PositionTextureVertex vertex7 = new ModelRenderer.PositionTextureVertex(xEnd, yEnd, zEnd, 8.0F, 8.0F);
        ModelRenderer.PositionTextureVertex vertex8 = new ModelRenderer.PositionTextureVertex(x, yEnd, zEnd, 8.0F, 0.0F);

        this.polygons[2] = getTexturedQuad(new ModelRenderer.PositionTextureVertex[]{vertex6, vertex5, vertex1, vertex2}, texWidth, texHeight, Direction.DOWN, faces);
        this.polygons[3] = getTexturedQuad(new ModelRenderer.PositionTextureVertex[]{vertex3, vertex4, vertex8, vertex7}, texWidth, texHeight, Direction.UP, faces);
        this.polygons[1] = getTexturedQuad(new ModelRenderer.PositionTextureVertex[]{vertex1, vertex5, vertex8, vertex4}, texWidth, texHeight, Direction.WEST, faces);
        this.polygons[4] = getTexturedQuad(new ModelRenderer.PositionTextureVertex[]{vertex2, vertex1, vertex4, vertex3}, texWidth, texHeight, Direction.NORTH, faces);
        this.polygons[0] = getTexturedQuad(new ModelRenderer.PositionTextureVertex[]{vertex6, vertex2, vertex3, vertex7}, texWidth, texHeight, Direction.EAST, faces);
        this.polygons[5] = getTexturedQuad(new ModelRenderer.PositionTextureVertex[]{vertex5, vertex6, vertex7, vertex8}, texWidth, texHeight, Direction.SOUTH, faces);
    }

    private ModelRenderer.TexturedQuad getTexturedQuad(ModelRenderer.PositionTextureVertex[] positionsIn, float texWidth, float texHeight, Direction direction, FaceUVsItem faces) {
        FaceItem face = faces.getFace(direction);
        float u1 = face.getUv()[0];
        float v1 = face.getUv()[1];
        float u2 = u1 + face.getUvSize()[0];
        float v2 = v1 + face.getUvSize()[1];
        return new ModelRenderer.TexturedQuad(positionsIn, u1, v1, u2, v2, texWidth, texHeight, false, direction);
    }
}
