package com.github.tartaricacid.simplebedrockmodel.client.compat.sodium;

import net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter;
import net.caffeinemc.mods.sodium.api.vertex.format.common.ModelVertex;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public interface ISodiumVertexWriter {
    int STRIDE = ModelVertex.STRIDE;
    int SIZE = 6 * 4;
    long SCRATCH_BUFFER = MemoryUtil.nmemAlignedAlloc(64, SIZE * STRIDE);

    int[] NORMALS = new int[6];

    default void emitVertex(long ptr, float x, float y, float z, int color, float u, float v, int packedOverlay, int packedLight, int normal) {
        ModelVertex.write(ptr, x, y, z, color, u, v, packedOverlay, packedLight, normal);
    }

    default void flush(VertexBufferWriter writer, int vertexCount) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            writer.push(stack, SCRATCH_BUFFER, vertexCount, ModelVertex.FORMAT);
        }
    }

    default int packNormal(float x, float y, float z) {
        int normX = (int) (x * 127.0f) & 255;
        int normY = (int) (y * 127.0f) & 255;
        int normZ = (int) (z * 127.0f) & 255;

        return (normZ << 16) | (normY << 8) | normX;
    }

    default void prepareNormals(Vector3f[] normals) {
        for (int i = 0; i < normals.length; i++) {
            NORMALS[i] = packNormal(normals[i].x, normals[i].y, normals[i].z);
        }
    }
}
