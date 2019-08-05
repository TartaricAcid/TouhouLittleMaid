package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author TartaricAcid
 * @date 2019/7/5 22:57
 **/
@SideOnly(Side.CLIENT)
public class EntityMarisaBroomModel extends ModelBase {
    private final ModelRenderer all;
    private final ModelRenderer head;
    private final ModelRenderer middle;
    private final ModelRenderer bone;
    private final ModelRenderer left;
    private final ModelRenderer right;
    private final ModelRenderer handle;

    public EntityMarisaBroomModel() {
        textureWidth = 128;
        textureHeight = 64;

        all = new ModelRenderer(this);
        all.setRotationPoint(0.0F, 21.0F, 4.0F);

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, 0.0F, 2.0F);
        all.addChild(head);

        middle = new ModelRenderer(this);
        middle.setRotationPoint(0.0F, 0.0F, 0.0F);
        head.addChild(middle);

        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, 0.0F, 0.0F);
        middle.addChild(bone);
        bone.cubeList.add(new ModelBox(bone, 0, 0, -3.0F, -1.5F, -1.0F, 6, 3, 12, 0.0F, false));

        left = new ModelRenderer(this);
        left.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(left, 0.0F, 0.4363F, 0.0F);
        head.addChild(left);
        left.cubeList.add(new ModelBox(left, 37, 0, -3.0F, -1.5F, 0.0F, 6, 3, 11, 0.0F, false));

        right = new ModelRenderer(this);
        right.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(right, 0.0F, -0.4363F, 0.0F);
        head.addChild(right);
        right.cubeList.add(new ModelBox(right, 37, 0, -3.0F, -1.5F, 0.0F, 6, 3, 11, 0.0F, false));

        handle = new ModelRenderer(this);
        handle.setRotationPoint(0.0F, 0.0F, 1.0F);
        all.addChild(handle);
        handle.cubeList.add(new ModelBox(handle, 0, 17, -1.0F, -1.0F, -28.0F, 2, 2, 32, 0.0F, false));
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        all.render(scale);
    }

    private void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks,
                                  float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        this.all.rotateAngleX = headPitch * 0.017453292F * 0.3F;
        // 当骑着女仆时，设置一个 12 度的仰角
        if (entityIn.getRidingEntity() instanceof EntityMaid) {
            this.all.rotateAngleX -= 12f * (float) Math.PI / 180;
        }
    }
}
