package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.github.tartaricacid.touhoulittlemaid.client.model.pojo.FaceItem;
import com.github.tartaricacid.touhoulittlemaid.client.model.pojo.FaceUVsItem;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

public class ModelBoxFaceFloat extends ModelBox {
    public ModelBoxFaceFloat(ModelRenderer renderer, float x, float y, float z, float dx, float dy, float dz, float delta, FaceUVsItem faces) {
        super(renderer, 0, 0, x, y, z, (int) dx, (int) dy, (int) dz, delta, false);
        this.posX2 = x + dx;
        this.posY2 = y + dy;
        this.posZ2 = z + dz;
        float f = x + dx;
        float f1 = y + dy;
        float f2 = z + dz;
        x = x - delta;
        y = y - delta;
        z = z - delta;
        f = f + delta;
        f1 = f1 + delta;
        f2 = f2 + delta;

        this.vertexPositions[0].vector3D = new Vec3d(x, y, z);
        this.vertexPositions[1].vector3D = new Vec3d(f, y, z);
        this.vertexPositions[2].vector3D = new Vec3d(f, f1, z);
        this.vertexPositions[3].vector3D = new Vec3d(x, f1, z);
        this.vertexPositions[4].vector3D = new Vec3d(x, y, f2);
        this.vertexPositions[5].vector3D = new Vec3d(f, y, f2);
        this.vertexPositions[6].vector3D = new Vec3d(f, f1, f2);
        this.vertexPositions[7].vector3D = new Vec3d(x, f1, f2);

        this.quadList[0] = getTexturedQuad(new PositionTextureVertex[]{vertexPositions[5], vertexPositions[1], vertexPositions[2], vertexPositions[6]}, renderer.textureWidth, renderer.textureHeight, EnumFacing.EAST, faces);
        this.quadList[1] = getTexturedQuad(new PositionTextureVertex[]{vertexPositions[0], vertexPositions[4], vertexPositions[7], vertexPositions[3]}, renderer.textureWidth, renderer.textureHeight, EnumFacing.WEST, faces);
        this.quadList[2] = getTexturedQuad(new PositionTextureVertex[]{vertexPositions[5], vertexPositions[4], vertexPositions[0], vertexPositions[1]}, renderer.textureWidth, renderer.textureHeight, EnumFacing.DOWN, faces);
        this.quadList[3] = getTexturedQuad(new PositionTextureVertex[]{vertexPositions[2], vertexPositions[3], vertexPositions[7], vertexPositions[6]}, renderer.textureWidth, renderer.textureHeight, EnumFacing.UP, faces);
        this.quadList[4] = getTexturedQuad(new PositionTextureVertex[]{vertexPositions[1], vertexPositions[0], vertexPositions[3], vertexPositions[2]}, renderer.textureWidth, renderer.textureHeight, EnumFacing.NORTH, faces);
        this.quadList[5] = getTexturedQuad(new PositionTextureVertex[]{vertexPositions[4], vertexPositions[5], vertexPositions[6], vertexPositions[7]}, renderer.textureWidth, renderer.textureHeight, EnumFacing.SOUTH, faces);
    }

    private TexturedQuadFloat getTexturedQuad(PositionTextureVertex[] positionsIn, float texWidth, float texHeight, EnumFacing direction, FaceUVsItem faces) {
        FaceItem face = faces.getFace(direction);
        float u1 = face.getUv()[0];
        float v1 = face.getUv()[1];
        float u2 = u1 + face.getUvSize()[0];
        float v2 = v1 + face.getUvSize()[1];
        return new TexturedQuadFloat(positionsIn, u1, v1, u2, v2, texWidth, texHeight);
    }
}
