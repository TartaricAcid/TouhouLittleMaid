package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layers;

import com.github.tartaricacid.touhoulittlemaid.client.model.HeadCubeModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.ZunHeadCubeModel;
import com.github.tartaricacid.touhoulittlemaid.client.resources.PlayerMaidResources;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityScarecrow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;

/**
 * @author TartaricAcid
 */
public class LayerScarecrowAnnex implements LayerRenderer<EntityScarecrow> {
    private static HeadCubeModel HEAD_CUBE;
    private static ZunHeadCubeModel ZUN_HEAD_CUBE;

    public LayerScarecrowAnnex() {
        HEAD_CUBE = new HeadCubeModel();
        ZUN_HEAD_CUBE = new ZunHeadCubeModel();
    }

    @Override
    public void doRenderLayer(@Nonnull EntityScarecrow scarecrow, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (StringUtils.isNotBlank(scarecrow.getCustomNameTag())) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.color(255, 255, 255, 255);
            GlStateManager.translate(0, 0, 0);
            Minecraft.getMinecraft().renderEngine.bindTexture(PlayerMaidResources.getPlayerSkin(scarecrow.getCustomNameTag()));
            if (scarecrow.isSpecial()) {
                ZUN_HEAD_CUBE.render(scarecrow, 0, 0, 0, 0, 0, 0.0625f);
            } else {
                HEAD_CUBE.render(scarecrow, 0, 0, 0, 0, 0, 0.0625f);
            }
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
        if (StringUtils.isNotBlank(scarecrow.getText()) && !scarecrow.isSpecial()) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 0, -0.285);
            GlStateManager.scale(0.01F, 0.01F, 0.01F);
            GlStateManager.glNormal3f(0.0F, 0.0F, -0.01F);
            GlStateManager.depthMask(false);
            FontRenderer fontrenderer = Minecraft.getMinecraft().fontRenderer;
            String s = scarecrow.getText();
            s = s.substring(0, Math.min(s.length(), 28));
            fontrenderer.drawSplitString(s, -32, 49, 70, 0);
            GlStateManager.depthMask(true);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
