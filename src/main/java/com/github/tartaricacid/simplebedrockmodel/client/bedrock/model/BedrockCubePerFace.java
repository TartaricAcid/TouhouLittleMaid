package com.github.tartaricacid.simplebedrockmodel.client.bedrock.model;


import com.github.tartaricacid.simplebedrockmodel.client.bedrock.pojo.FaceItem;
import com.github.tartaricacid.simplebedrockmodel.client.bedrock.pojo.FaceUVsItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.core.Direction;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class BedrockCubePerFace implements BedrockCube {
    protected static final Vector3f[] VERTICES = new Vector3f[8];
    protected static final Vector3f EDGE_X = new Vector3f();
    protected static final Vector3f EDGE_Y = new Vector3f();
    protected static final Vector3f EDGE_Z = new Vector3f();
    protected final float width;
    protected final float height;
    protected final float depth;
    protected final float x;
    protected final float y;
    protected final float z;
    protected final float[][] uvs = new float[6][4];

    static {
        for (int i = 0; i < VERTICES.length; i++) {
            VERTICES[i] = new Vector3f();
        }
    }

    public BedrockCubePerFace(float x, float y, float z, float width, float height, float depth, float delta, float texWidth, float texHeight, FaceUVsItem faces) {
        this.x = (x - delta) / 16.0f;
        this.y = (y - delta) / 16.0f;
        this.z = (z - delta) / 16.0f;
        this.width = (width + delta * 2) / 16.0f;
        this.height = (height + delta * 2) / 16.0f;
        this.depth = (depth + delta * 2) / 16.0f;

        for (Direction direction : Direction.values()) {
            fillUV(direction, faces, texWidth, texHeight);
        }
    }

    private static boolean equalZero(float[] uvSize) {
        return Math.abs(uvSize[0]) < 1e-9 && Math.abs(uvSize[1]) < 1e-9;
    }

    private void fillUV(Direction direction, FaceUVsItem faces, float texWidth, float texHeight) {
        FaceItem face = faces.getFace(direction);
        if (face == null) {
            return;
        }
        if (equalZero(face.getUvSize())) {
            return;
        }
        uvs[direction.ordinal()][0] = face.getUv()[0] / texWidth;
        uvs[direction.ordinal()][1] = (face.getUv()[0] + face.getUvSize()[0]) / texWidth;
        uvs[direction.ordinal()][2] = face.getUv()[1] / texHeight;
        uvs[direction.ordinal()][3] = (face.getUv()[1] + face.getUvSize()[1]) / texHeight;
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
                    r, g, b, a, uvs[i][1], uvs[i][2], overlay, lightmap, normals[i].x, normals[i].y, normals[i].z);

            consumer.vertex(VERTICES[VERTEX_ORDER[i][1]].x, VERTICES[VERTEX_ORDER[i][1]].y, VERTICES[VERTEX_ORDER[i][1]].z,
                    r, g, b, a, uvs[i][0], uvs[i][2], overlay, lightmap, normals[i].x, normals[i].y, normals[i].z);

            consumer.vertex(VERTICES[VERTEX_ORDER[i][2]].x, VERTICES[VERTEX_ORDER[i][2]].y, VERTICES[VERTEX_ORDER[i][2]].z,
                    r, g, b, a, uvs[i][0], uvs[i][3], overlay, lightmap, normals[i].x, normals[i].y, normals[i].z);

            consumer.vertex(VERTICES[VERTEX_ORDER[i][3]].x, VERTICES[VERTEX_ORDER[i][3]].y, VERTICES[VERTEX_ORDER[i][3]].z,
                    r, g, b, a, uvs[i][1], uvs[i][3], overlay, lightmap, normals[i].x, normals[i].y, normals[i].z);
        }
    }
}
