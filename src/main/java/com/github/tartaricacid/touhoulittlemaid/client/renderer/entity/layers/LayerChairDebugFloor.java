package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layers;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.DebugFloorModel;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * @author TartaricAcid
 * @date 2019/8/4 22:45
 * <p>
 * 用于渲染皮肤详情页面的地面网格
 **/
@SideOnly(Side.CLIENT)
public class LayerChairDebugFloor implements LayerRenderer<EntityChair> {
    private static final ResourceLocation FLOOR_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/debug_floor.png");
    private ModelBase modelBase;

    public LayerChairDebugFloor() {
        this.modelBase = new DebugFloorModel();
    }

    @Override
    public void doRenderLayer(@Nonnull EntityChair chair, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (chair.isDebugFloorOpen) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.color(255, 255, 255, 255);
            GlStateManager.translate(0, 0.00001, 0);
            Minecraft.getMinecraft().renderEngine.bindTexture(FLOOR_TEXTURE);
            modelBase.render(chair, 0, 0, 0, 0, 0, 0.0625f);
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
