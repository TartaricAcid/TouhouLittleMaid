package com.github.tartaricacid.touhoulittlemaid.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author TartaricAcid
 */
@SideOnly(Side.CLIENT)
public class ScarecrowModel extends ModelBase {
    private final ModelRenderer head;
    private final ModelRenderer ahoge;
    private final ModelRenderer armRight;
    private final ModelRenderer armLeft;
    private final ModelRenderer body;
    private final ModelRenderer bone;

    public ScarecrowModel() {
        textureWidth = 128;
        textureHeight = 128;

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, 6.0F, 1.0F);
        head.cubeList.add(new ModelBox(head, 0, 36, -4.0F, -8.0F, -5.0F, 8, 8, 8, 0.0F, false));
        head.cubeList.add(new ModelBox(head, 28, 28, -4.0F, -8.0F, -5.0F, 8, 8, 8, 0.25F, false));
        head.cubeList.add(new ModelBox(head, 0, 24, -4.5F, -8.5F, -5.5F, 9, 3, 9, 0.1F, false));
        head.cubeList.add(new ModelBox(head, 0, 0, -5.5F, -6.25F, -6.5F, 11, 1, 11, 0.0F, false));

        ahoge = new ModelRenderer(this);
        ahoge.setRotationPoint(0.0F, -8.0F, -2.55F);
        head.addChild(ahoge);
        ahoge.cubeList.add(new ModelBox(ahoge, 0, 24, -2.5F, -3.5F, -0.2F, 4, 4, 0, 0.0F, false));

        armRight = new ModelRenderer(this);
        armRight.setRotationPoint(-3.0F, 6.5F, 0.0F);
        setRotationAngle(armRight, 0.0F, 0.0F, 1.5708F);
        armRight.cubeList.add(new ModelBox(armRight, 0, 12, 0.3063F, 1.4226F, -1.0F, 2, 8, 2, 0.0F, false));
        armRight.cubeList.add(new ModelBox(armRight, 20, 52, -0.1937F, 0.4226F, -1.5F, 3, 6, 3, 0.0F, false));

        armLeft = new ModelRenderer(this);
        armLeft.setRotationPoint(3.0F, 6.5F, 0.0F);
        setRotationAngle(armLeft, 0.0F, 0.0F, -1.5708F);
        armLeft.cubeList.add(new ModelBox(armLeft, 8, 52, -2.8063F, 0.4226F, -1.5F, 3, 6, 3, 0.0F, false));
        armLeft.cubeList.add(new ModelBox(armLeft, 0, 0, -2.3063F, 1.4226F, -1.0F, 2, 8, 2, 0.0F, false));

        body = new ModelRenderer(this);
        body.setRotationPoint(0.0F, 13.5F, 0.0F);
        body.cubeList.add(new ModelBox(body, 32, 44, -3.0F, -7.499F, -3.0F, 6, 7, 6, 0.0F, false));
        body.cubeList.add(new ModelBox(body, 25, 57, -3.5F, -7.999F, -3.5F, 7, 7, 7, -0.25F, false));
        body.cubeList.add(new ModelBox(body, 33, 19, -3.5F, -1.499F, -3.5F, 7, 2, 7, 0.0F, false));
        body.cubeList.add(new ModelBox(body, 37, 5, -3.5F, 0.201F, -3.5F, 7, 7, 7, -0.2F, false));
        body.cubeList.add(new ModelBox(body, 0, 82, -6.0F, -6.299F, -4.5F, 12, 7, 1, 0.0F, false));
        body.cubeList.add(new ModelBox(body, 0, 12, -5.0F, 8.501F, -5.0F, 10, 2, 10, 0.0F, false));
        body.cubeList.add(new ModelBox(body, 0, 52, -1.0F, -17.499F, -1.0F, 2, 26, 2, 0.0F, false));

        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, -7.799F, 0.0F);
        setRotationAngle(bone, 0.3491F, 0.0F, 0.0F);
        body.addChild(bone);
        bone.cubeList.add(new ModelBox(bone, 59, 0, -4.5F, 0.0F, -4.7F, 9, 1, 10, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        head.render(f5);
        armRight.render(f5);
        armLeft.render(f5);
        body.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
