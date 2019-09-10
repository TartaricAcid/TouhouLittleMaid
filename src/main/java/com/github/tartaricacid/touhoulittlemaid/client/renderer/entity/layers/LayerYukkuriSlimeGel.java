package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layers;

import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityYukkuriSlimeRender;
import com.github.tartaricacid.touhoulittlemaid.config.GeneralConfig;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.monster.EntitySlime;

import javax.annotation.Nonnull;

/**
 * @author TartaricAcid
 * @date 2019/9/10 17:40
 **/
public class LayerYukkuriSlimeGel implements LayerRenderer<EntitySlime> {
    private final EntityYukkuriSlimeRender slimeRenderer;
    private final ModelBase slimeModel = new ModelSlime(0);

    public LayerYukkuriSlimeGel(EntityYukkuriSlimeRender slimeRendererIn) {
        this.slimeRenderer = slimeRendererIn;
    }

    @Override
    public void doRenderLayer(@Nonnull EntitySlime entitySlime, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!entitySlime.isInvisible() && !GeneralConfig.VANILLA_CONFIG.changeSlimeModel) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableNormalize();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.slimeModel.setModelAttributes(this.slimeRenderer.getMainModel());
            this.slimeModel.render(entitySlime, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GlStateManager.disableBlend();
            GlStateManager.disableNormalize();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}
