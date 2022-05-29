package com.github.tartaricacid.touhoulittlemaid.client.model.bedrock;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class BedrockPart {
    public final ObjectList<BedrockCube> cubes = new ObjectArrayList<>();
    private final ObjectList<BedrockPart> children = new ObjectArrayList<>();
    public float x;
    public float y;
    public float z;
    public float xRot;
    public float yRot;
    public float zRot;
    public float offsetX;
    public float offsetY;
    public float offsetZ;
    public boolean visible = true;
    public boolean mirror;
    private float initRotX;
    private float initRotY;
    private float initRotZ;

    public void setPos(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int texU, int texV) {
        this.render(poseStack, consumer, texU, texV, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int texU, int texV, float red, float green, float blue, float alpha) {
        if (this.visible) {
            if (!this.cubes.isEmpty() || !this.children.isEmpty()) {
                poseStack.pushPose();
                poseStack.translate(this.offsetX, this.offsetY, this.offsetZ);
                this.translateAndRotate(poseStack);
                this.compile(poseStack.last(), consumer, texU, texV, red, green, blue, alpha);

                for (BedrockPart part : this.children) {
                    part.render(poseStack, consumer, texU, texV, red, green, blue, alpha);
                }

                poseStack.popPose();
            }
        }
    }

    public void translateAndRotate(PoseStack poseStack) {
        poseStack.translate((this.x / 16.0F), (this.y / 16.0F), (this.z / 16.0F));
        if (this.zRot != 0.0F) {
            poseStack.mulPose(Vector3f.ZP.rotation(this.zRot));
        }
        if (this.yRot != 0.0F) {
            poseStack.mulPose(Vector3f.YP.rotation(this.yRot));
        }
        if (this.xRot != 0.0F) {
            poseStack.mulPose(Vector3f.XP.rotation(this.xRot));
        }

    }

    private void compile(PoseStack.Pose pose, VertexConsumer consumer, int texU, int texV, float red, float green, float blue, float alpha) {
        for (BedrockCube bedrockCube : this.cubes) {
            bedrockCube.compile(pose, consumer, texU, texV, red, green, blue, alpha);
        }
    }

    public BedrockCube getRandomCube(Random random) {
        return this.cubes.get(random.nextInt(this.cubes.size()));
    }

    public boolean isEmpty() {
        return this.cubes.isEmpty();
    }

    public void setInitRotationAngle(float x, float y, float z) {
        this.initRotX = x;
        this.initRotY = y;
        this.initRotZ = z;
    }

    public float getInitRotX() {
        return initRotX;
    }

    public float getInitRotY() {
        return initRotY;
    }

    public float getInitRotZ() {
        return initRotZ;
    }

    public void addChild(BedrockPart model) {
        this.children.add(model);
    }
}
