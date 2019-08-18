package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layers;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
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
 * @date 2019/8/5 19:47
 * <p>
 * 用于渲染皮肤详情页面的扫帚效果
 **/
@SideOnly(Side.CLIENT)
public class LayerMaidDebugBroom implements LayerRenderer<EntityMaid> {
    private static final ResourceLocation BROOM_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/marisa_broom.png");
    private ModelBase modelBase;

    public LayerMaidDebugBroom(ModelBase modelBase) {
        this.modelBase = modelBase;
    }

    @Override
    public void doRenderLayer(@Nonnull EntityMaid maid, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (maid.isDebugBroomShow) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.rotate(-12f, 1, 0, 0);
            GlStateManager.translate(0, -0.2, 0.2);
            Minecraft.getMinecraft().renderEngine.bindTexture(BROOM_TEXTURE);
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