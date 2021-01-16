package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layers;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityModelJson;
import com.github.tartaricacid.touhoulittlemaid.client.model.MaidBackpackBigModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.MaidBackpackMiddleModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.MaidBackpackSmallModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRender;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * @author TartaricAcid
 * @date 2020/1/17 21:55
 **/
public class LayerMaidBackpack implements LayerRenderer<EntityMaid> {
    private static final ResourceLocation SMALL = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_backpack_small.png");
    private static final ResourceLocation MIDDLE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_backpack_middle.png");
    private static final ResourceLocation BIG = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_backpack_big.png");
    private final EntityMaidRender renderer;
    private ModelBase smallModel = new MaidBackpackSmallModel();
    private ModelBase middleModel = new MaidBackpackMiddleModel();
    private ModelBase bigModel = new MaidBackpackBigModel();

    public LayerMaidBackpack(EntityMaidRender renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(@Nonnull EntityMaid entityMaid, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        // 非渲染判定
        if (!renderer.getMainInfo().isShowBackpack() || entityMaid.isSleep()) {
            return;
        }

        // 渲染
        EntityModelJson mainModel = (EntityModelJson) this.renderer.getMainModel();
        // 稍微缩放，避免整数倍的 z-flight
        GlStateManager.scale(1.01, 1.01, 1.01);
        // [-13, 41, 15]
        if (mainModel.hasBackpackPositioningModel()) {
            ModelRenderer renderer = mainModel.getBackpackPositioningModel();
            GlStateManager.translate(renderer.rotationPointX * 0.0625, 0.0625 * (renderer.rotationPointY - 23 + 8),
                    0.0625 * (renderer.rotationPointZ + 4));
        } else {
            GlStateManager.translate(0, -0.5, 0.25);
        }
        switch (entityMaid.getBackLevel()) {
            default:
            case EMPTY:
                return;
            case SMALL:
                Minecraft.getMinecraft().renderEngine.bindTexture(SMALL);
                smallModel.render(entityMaid, 0, 0, 0, 0, 0, 0.0625f);
                return;
            case MIDDLE:
                Minecraft.getMinecraft().renderEngine.bindTexture(MIDDLE);
                middleModel.render(entityMaid, 0, 0, 0, 0, 0, 0.0625f);
                return;
            case BIG:
                Minecraft.getMinecraft().renderEngine.bindTexture(BIG);
                bigModel.render(entityMaid, 0, 0, 0, 0, 0, 0.0625f);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
