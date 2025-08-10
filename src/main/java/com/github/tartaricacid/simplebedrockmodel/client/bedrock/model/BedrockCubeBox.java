package com.github.tartaricacid.simplebedrockmodel.client.bedrock.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class BedrockCubeBox implements BedrockCube {
    protected static final Vector3f[] VERTICES = new Vector3f[8];
    protected static final Vector3f EDGE_X = new Vector3f();
    protected static final Vector3f EDGE_Y = new Vector3f();
    protected static final Vector3f EDGE_Z = new Vector3f();
    protected static final int[][] UV_ORDER_NO_MIRROR = new int[][]{
            {1, 2, 6, 7},
            {2, 3, 7, 6},
            {1, 2, 7, 8},
            {4, 5, 7, 8},
            {0, 1, 7, 8},
            {2, 4, 7, 8},
    };
    protected static final int[][] UV_ORDER_MIRRORED = new int[][]{
            {2, 1, 6, 7},
            {3, 2, 7, 6},
            {2, 1, 7, 8},
            {5, 4, 7, 8},
            {4, 2, 7, 8},
            {1, 0, 7, 8},
    };
    protected final float width;
    protected final float height;
    protected final float depth;
    protected final float x;
    protected final float y;
    protected final float z;
    protected final float[] uvs;
    protected final int[][] uvOrder;

    static {
        for (int i = 0; i < VERTICES.length; i++) {
            VERTICES[i] = new Vector3f();
        }
    }

    public BedrockCubeBox(float texOffX, float texOffY, float x, float y, float z, float width, float height, float depth, float delta, boolean mirror, float texWidth, float texHeight) {
        this.x = (x - delta) / 16.0f;
        this.y = (y - delta) / 16.0f;
        this.z = (z - delta) / 16.0f;
        this.width = (width + delta * 2) / 16.0f;
        this.height = (height + delta * 2) / 16.0f;
        this.depth = (depth + delta * 2) / 16.0f;

        float dx = Mth.floor(width);
        float dy = Mth.floor(height);
        float dz = Mth.floor(depth);

        float scaleU = 1.0f / texWidth;
        float scaleV = 1.0f / texHeight;

        this.uvs = new float[9];
        this.uvs[0] = scaleU * texOffX;
        this.uvs[1] = scaleU * (texOffX + dz);
        this.uvs[2] = scaleU * (texOffX + dz + dx);
        this.uvs[3] = scaleU * (texOffX + dz + dx + dx);
        this.uvs[4] = scaleU * (texOffX + dz + dx + dz);
        this.uvs[5] = scaleU * (texOffX + dz + dx + dz + dx);
        this.uvs[6] = scaleV * texOffY;
        this.uvs[7] = scaleV * (texOffY + dz);
        this.uvs[8] = scaleV * (texOffY + dz + dy);

        this.uvOrder = mirror ? UV_ORDER_MIRRORED : UV_ORDER_NO_MIRROR;
    }

    protected void prepareVertices(Matrix4f pose) {
        EDGE_X.set(pose.m00(), pose.m01(), pose.m02()).mul(width);
        EDGE_Y.set(pose.m10(), pose.m11(), pose.m12()).mul(height);
        EDGE_Z.set(pose.m20(), pose.m21(), pose.m22()).mul(depth);
        VERTICES[VERTEX_X1_Y1_Z1].set(x, y, z).mulPosition(pose);
        VERTICES[VERTEX_X1_Y1_Z1].add(EDGE_X, VERTICES[VERTEX_X2_Y1_Z1]);
        VERTICES[VERTEX_X2_Y1_Z1].add(EDGE_Y, VERTICES[VERTEX_X2_Y2_Z1]);
        VERTICES[VERTEX_X1_Y1_Z1].add(EDGE_Y, VERTICES[VERTEX_X1_Y2_Z1]);
        VERTICES[VERTEX_X1_Y1_Z1].add(EDGE_Z, VERTICES[VERTEX_X1_Y1_Z2]);
        VERTICES[VERTEX_X2_Y1_Z1].add(EDGE_Z, VERTICES[VERTEX_X2_Y1_Z2]);
        VERTICES[VERTEX_X2_Y2_Z1].add(EDGE_Z, VERTICES[VERTEX_X2_Y2_Z2]);
        VERTICES[VERTEX_X1_Y2_Z1].add(EDGE_Z, VERTICES[VERTEX_X1_Y2_Z2]);
    }

    @Override
    public void compile(PoseStack.Pose pose, Vector3f[] normals, VertexConsumer consumer, int lightmap, int overlay, float r, float g, float b, float a) {
        Matrix4f matrix4f = pose.pose();
        prepareVertices(matrix4f);

        for (int i = 0; i < NUM_CUBE_FACES; i++) {
            consumer.vertex(VERTICES[VERTEX_ORDER[i][0]].x, VERTICES[VERTEX_ORDER[i][0]].y, VERTICES[VERTEX_ORDER[i][0]].z,
                    r, g, b, a, uvs[uvOrder[i][1]], uvs[uvOrder[i][2]], overlay, lightmap, normals[i].x, normals[i].y, normals[i].z);

            consumer.vertex(VERTICES[VERTEX_ORDER[i][1]].x, VERTICES[VERTEX_ORDER[i][1]].y, VERTICES[VERTEX_ORDER[i][1]].z,
                    r, g, b, a, uvs[uvOrder[i][0]], uvs[uvOrder[i][2]], overlay, lightmap, normals[i].x, normals[i].y, normals[i].z);

            consumer.vertex(VERTICES[VERTEX_ORDER[i][2]].x, VERTICES[VERTEX_ORDER[i][2]].y, VERTICES[VERTEX_ORDER[i][2]].z,
                    r, g, b, a, uvs[uvOrder[i][0]], uvs[uvOrder[i][3]], overlay, lightmap, normals[i].x, normals[i].y, normals[i].z);

            consumer.vertex(VERTICES[VERTEX_ORDER[i][3]].x, VERTICES[VERTEX_ORDER[i][3]].y, VERTICES[VERTEX_ORDER[i][3]].z,
                    r, g, b, a, uvs[uvOrder[i][1]], uvs[uvOrder[i][3]], overlay, lightmap, normals[i].x, normals[i].y, normals[i].z);
        }
    }
}
