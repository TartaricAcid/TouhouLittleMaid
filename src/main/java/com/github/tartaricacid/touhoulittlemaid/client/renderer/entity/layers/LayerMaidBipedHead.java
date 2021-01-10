package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layers;

import com.github.tartaricacid.touhoulittlemaid.client.model.EntityModelJson;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRender;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;

import javax.annotation.ParametersAreNonnullByDefault;

public class LayerMaidBipedHead extends LayerArmorBase<ModelBiped> {
    private final EntityMaidRender maidRender;

    public LayerMaidBipedHead(EntityMaidRender maidRender) {
        super(maidRender);
        this.maidRender = maidRender;
    }

    @Override
    protected void initArmor() {
        this.modelLeggings = new ModelBiped(0.5F);
        this.modelArmor = new ModelBiped(1.0F);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void doRenderLayer(EntityLivingBase livingBase, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        EntityModelJson modelJson = (EntityModelJson) this.maidRender.getMainModel();
        if (maidRender.getMainInfo().isShowCustomHead() && modelJson.hasHead() && livingBase instanceof EntityMaid) {
            EntityMaid maid = (EntityMaid) livingBase;
            if (!maid.isShowHelmet()) {
                return;
            }
            ModelRenderer head = modelJson.getHead();
            GlStateManager.pushMatrix();
            GlStateManager.translate(head.rotationPointX * 0.0625, head.rotationPointY * 0.0625, head.rotationPointZ * 0.0625);
            if (maid.isBegging()) {
                GlStateManager.rotate(0.139f * 180 / (float) Math.PI, 0, 0, 1);
            }
            renderArmorLayer(livingBase, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.HEAD);
            GlStateManager.popMatrix();
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void setModelSlotVisible(ModelBiped modelBiped, EntityEquipmentSlot slotIn) {
        modelBiped.setVisible(false);
        if (slotIn == EntityEquipmentSlot.HEAD) {
            modelBiped.bipedHead.showModel = true;
            modelBiped.bipedHeadwear.showModel = true;
        }
    }
}
