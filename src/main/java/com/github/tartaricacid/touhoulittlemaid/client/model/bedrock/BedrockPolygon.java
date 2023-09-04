package com.github.tartaricacid.touhoulittlemaid.client.model.bedrock;

import net.minecraft.core.Direction;
import org.joml.Vector3f;

public class BedrockPolygon {
    public final BedrockVertex[] vertices;
    public final Vector3f normal;

    public BedrockPolygon(BedrockVertex[] vertices, float u1, float v1, float u2, float v2, float texWidth, float texHeight, boolean mirror, Direction direction) {
        this.vertices = vertices;
        float f = 0.0F / texWidth;
        float f1 = 0.0F / texHeight;
        vertices[0] = vertices[0].remap(u2 / texWidth - f, v1 / texHeight + f1);
        vertices[1] = vertices[1].remap(u1 / texWidth + f, v1 / texHeight + f1);
        vertices[2] = vertices[2].remap(u1 / texWidth + f, v2 / texHeight - f1);
        vertices[3] = vertices[3].remap(u2 / texWidth - f, v2 / texHeight - f1);
        if (mirror) {
            int i = vertices.length;
            for (int j = 0; j < i / 2; ++j) {
                BedrockVertex bedrockVertex = vertices[j];
                vertices[j] = vertices[i - 1 - j];
                vertices[i - 1 - j] = bedrockVertex;
            }
        }
        this.normal = direction.step();
        if (mirror) {
            this.normal.mul(-1.0F, 1.0F, 1.0F);
        }
    }
}
