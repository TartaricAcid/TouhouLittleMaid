package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layers;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

/**
 * @author TartaricAcid
 * @date 2019/8/4 22:45
 **/
public class LayerMaidDebugFloor implements LayerRenderer<EntityMaid> {
    private static final ResourceLocation FLOOR_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/debug_floor.png");
    private ModelBase modelBase;

    public LayerMaidDebugFloor(ModelBase modelBase) {
        this.modelBase = modelBase;
    }

    @Override
    public void doRenderLayer(EntityMaid maid, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (maid.isDebugFloorOpen) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.color(255, 255, 255, 255);
            GlStateManager.translate(0, 1.3175, 0);
            GlStateManager.scale(0.123, 0.123, 0.123);
            Minecraft.getMinecraft().renderEngine.bindTexture(FLOOR_TEXTURE);
            modelBase.render(maid, 0, 0, 0, 0, 0, 0.0625f);
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
