package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layers;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.HataSasimonoFlagModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.HataSasimonoFrameModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRender;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * @author TartaricAcid
 * @date 2019/8/13 17:43
 **/
public class LayerHataSasimono implements LayerRenderer<EntityMaid> {
    private static final ResourceLocation FRAME_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/hata_sasimono.png");
    private final EntityMaidRender renderer;
    private ModelBase modelBase;
    private ModelBase modelFlag;

    public LayerHataSasimono(EntityMaidRender renderer) {
        this.modelBase = new HataSasimonoFrameModel();
        this.modelFlag = new HataSasimonoFlagModel();
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(@Nonnull EntityMaid maid, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (renderer.getMainInfo().isShowHata() && maid.hasSasimono() && maid.isShowSasimono()) {
            int texture;

            // 先判定 size
            if (ClientProxy.HATA_NAME_MAP.size() < 1) {
                return;
            }

            // 判定此索引是否存在
            if (ClientProxy.HATA_NAME_MAP.get(maid.getSasimonoCRC32()) == null) {
                texture = ClientProxy.HATA_NAME_MAP.values().stream().findFirst().get();
            } else {
                texture = ClientProxy.HATA_NAME_MAP.get(maid.getSasimonoCRC32());
            }

            // 渲染
            Minecraft.getMinecraft().renderEngine.bindTexture(FRAME_TEXTURE);
            modelBase.render(maid, 0, 0, 0, 0, 0, 0.0625f);
            GlStateManager.pushMatrix();
            GlStateManager.enableCull();
            GlStateManager.bindTexture(texture);
            modelFlag.render(maid, 0, 0, 0, 0, 0, 0.0625f);
            GlStateManager.disableCull();
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
