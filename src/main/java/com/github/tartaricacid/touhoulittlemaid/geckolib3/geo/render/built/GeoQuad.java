package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built;

import com.mojang.math.Vector3f;
import net.minecraft.core.Direction;

public class GeoQuad {
    public final Vector3f normal;
    public GeoVertex[] vertices;
    public Direction direction;

    public GeoQuad(GeoVertex[] verticesIn, float u1, float v1, float uSize, float vSize, float texWidth,
                   float texHeight, Boolean mirrorIn, Direction directionIn) {
        this.direction = directionIn;
        this.vertices = verticesIn;

        float u2 = u1 + uSize;
        float v2 = v1 + vSize;

        u1 /= texWidth;
        u2 /= texWidth;
        v1 /= texHeight;
        v2 /= texHeight;

        if (mirrorIn != null && mirrorIn) {
            vertices[0] = verticesIn[0].setTextureUV(u1, v1);
            vertices[1] = verticesIn[1].setTextureUV(u2, v1);
            vertices[2] = verticesIn[2].setTextureUV(u2, v2);
            vertices[3] = verticesIn[3].setTextureUV(u1, v2);
        } else {
            vertices[0] = verticesIn[0].setTextureUV(u2, v1);
            vertices[1] = verticesIn[1].setTextureUV(u1, v1);
            vertices[2] = verticesIn[2].setTextureUV(u1, v2);
            vertices[3] = verticesIn[3].setTextureUV(u2, v2);
        }

        this.normal = directionIn.step();
        if (mirrorIn != null && mirrorIn) {
            this.normal.mul(-1.0F, 1.0F, 1.0F);
        }
    }

    public GeoQuad(GeoVertex[] verticesIn, double[] uvCoords, double[] uvSize, float texWidth, float texHeight,
                   Boolean mirrorIn, Direction directionIn) {
        this(verticesIn, (float) uvCoords[0], (float) uvCoords[1], (float) uvSize[0], (float) uvSize[1], texWidth,
                texHeight, mirrorIn, directionIn);
    }
}
