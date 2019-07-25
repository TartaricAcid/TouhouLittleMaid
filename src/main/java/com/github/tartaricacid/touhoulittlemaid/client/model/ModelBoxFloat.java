package com.github.tartaricacid.touhoulittlemaid.client.model;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.util.math.Vec3d;

public class ModelBoxFloat extends ModelBox {

    public ModelBoxFloat(ModelRenderer renderer, int texU, int texV, float x, float y, float z, float dx, float dy, float dz, float delta) {
        this(renderer, texU, texV, x, y, z, dx, dy, dz, delta, renderer.mirror);
    }

    public ModelBoxFloat(ModelRenderer renderer, int texU, int texV, float x, float y, float z, float dx, float dy, float dz, float delta, boolean mirror) {
        super(renderer, texU, texV, x, y, z, (int) dx, (int) dy, (int) dz, delta, renderer.mirror);
        if (posX2 == x + dx && posY2 == y + dy && posZ2 == z + dz) {
            return;
        }
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

        if (mirror) {
            float f3 = f;
            f = x;
            x = f3;
        }

        this.vertexPositions[0].vector3D = new Vec3d(x, y, z);
        this.vertexPositions[1].vector3D = new Vec3d(f, y, z);
        this.vertexPositions[2].vector3D = new Vec3d(f, f1, z);
        this.vertexPositions[3].vector3D = new Vec3d(x, f1, z);
        this.vertexPositions[4].vector3D = new Vec3d(x, y, f2);
        this.vertexPositions[5].vector3D = new Vec3d(f, y, f2);
        this.vertexPositions[6].vector3D = new Vec3d(f, f1, f2);
        this.vertexPositions[7].vector3D = new Vec3d(x, f1, f2);
        int ddx = (int) Math.ceil(dx);
        int ddy = (int) Math.ceil(dy);
        int ddz = (int) Math.ceil(dz);
        this.quadList[0] = new TexturedQuad(new PositionTextureVertex[] { vertexPositions[5], vertexPositions[1], vertexPositions[2], vertexPositions[6] }, texU + ddz + ddx, texV + ddz, texU + ddz + ddx + ddz, texV + ddz + ddy, renderer.textureWidth, renderer.textureHeight);
        this.quadList[1] = new TexturedQuad(new PositionTextureVertex[] { vertexPositions[0], vertexPositions[4], vertexPositions[7], vertexPositions[3] }, texU, texV + ddz, texU + ddz, texV + ddz + ddy, renderer.textureWidth, renderer.textureHeight);
        this.quadList[2] = new TexturedQuad(new PositionTextureVertex[] { vertexPositions[5], vertexPositions[4], vertexPositions[0], vertexPositions[1] }, texU + ddz, texV, texU + ddz + ddx, texV + ddz, renderer.textureWidth, renderer.textureHeight);
        this.quadList[3] = new TexturedQuad(new PositionTextureVertex[] { vertexPositions[2], vertexPositions[3], vertexPositions[7], vertexPositions[6] }, texU + ddz + ddx, texV + ddz, texU + ddz + ddx + ddx, texV, renderer.textureWidth, renderer.textureHeight);
        this.quadList[4] = new TexturedQuad(new PositionTextureVertex[] { vertexPositions[1], vertexPositions[0], vertexPositions[3], vertexPositions[2] }, texU + ddz, texV + ddz, texU + ddz + ddx, texV + ddz + ddy, renderer.textureWidth, renderer.textureHeight);
        this.quadList[5] = new TexturedQuad(new PositionTextureVertex[] { vertexPositions[4], vertexPositions[5], vertexPositions[6], vertexPositions[7] }, texU + ddz + ddx + ddz, texV + ddz, texU + ddz + ddx + ddz + ddx, texV + ddz + ddy, renderer.textureWidth, renderer.textureHeight);

        if (mirror) {
            for (TexturedQuad texturedquad : this.quadList) {
                texturedquad.flipFace();
            }
        }
    }
}
