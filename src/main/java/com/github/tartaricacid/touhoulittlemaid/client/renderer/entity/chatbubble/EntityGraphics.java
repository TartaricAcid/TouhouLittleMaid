package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.chatbubble;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class EntityGraphics extends GuiGraphics {
    private final EntityMaid maid;
    private final int packedLight;
    private final float partialTicks;

    public EntityGraphics(Minecraft minecraft, PoseStack pose, EntityMaid maid, int packedLight, float partialTicks) {
        super(minecraft, pose, minecraft.renderBuffers().bufferSource());
        this.maid = maid;
        this.packedLight = packedLight;
        this.partialTicks = partialTicks;
    }

    public EntityMaid getMaid() {
        return maid;
    }

    public int getPackedLight() {
        return packedLight;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    /**
     * 实体渲染不需要提交顶点
     */
    @Override
    public void flush() {
    }
}
