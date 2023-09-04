package com.github.tartaricacid.touhoulittlemaid.client.model.bedrock;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public interface BedrockCube {
    void compile(PoseStack.Pose pose, VertexConsumer consumer, int texU, int texV, float red, float green, float blue, float alpha);
}
