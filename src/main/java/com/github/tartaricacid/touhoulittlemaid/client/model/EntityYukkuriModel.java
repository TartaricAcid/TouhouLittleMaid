package com.github.tartaricacid.touhoulittlemaid.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


/**
 * @author TartaricAcid
 * @date 2019/8/30 14:50
 **/
@SideOnly(Side.CLIENT)
public class EntityYukkuriModel extends ModelBase {
    private final ModelRenderer bone;
    private final ModelRenderer bone2;
    private final ModelRenderer bone3;
    private final ModelRenderer bone4;
    private final ModelRenderer bone5;
    private final ModelRenderer bone6;
    private final ModelRenderer bone7;
    private final ModelRenderer bone8;

    public EntityYukkuriModel() {
        textureWidth = 64;
        textureHeight = 64;

        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, 24.0F, 0.0F);
        bone.cubeList.add(new ModelBox(bone, 0, 18, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 0, -4.5F, -8.5F, -4.5F, 9, 9, 9, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 38, 0, 3.0F, -3.75F, -5.0F, 2, 4, 2, -0.35F, true));
        bone.cubeList.add(new ModelBox(bone, 38, 0, -5.0F, -3.75F, -5.0F, 2, 4, 2, -0.35F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 0, -1.5F, -11.0F, -2.0F, 2, 3, 0, 0.0F, false));

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(0.0F, -6.5F, 4.0F);
        setRotationAngle(bone2, 0.0F, -0.1745F, -0.4363F);
        bone.addChild(bone2);
        bone2.cubeList.add(new ModelBox(bone2, 48, 14, -0.8195F, -2.0F, 0.1061F, 7, 4, 1, -0.2F, false));

        bone3 = new ModelRenderer(this);
        bone3.setRotationPoint(0.0F, -6.5F, 4.0F);
        setRotationAngle(bone3, 0.0F, 0.1745F, 0.4363F);
        bone.addChild(bone3);
        bone3.cubeList.add(new ModelBox(bone3, 48, 14, -6.1805F, -2.0F, 0.1061F, 7, 4, 1, -0.2F, true));

        bone4 = new ModelRenderer(this);
        bone4.setRotationPoint(0.0F, -6.5F, 4.0F);
        setRotationAngle(bone4, 0.0F, -0.1745F, 2.7053F);
        bone.addChild(bone4);
        bone4.cubeList.add(new ModelBox(bone4, 50, 20, -0.3195F, -2.0F, 0.1061F, 6, 4, 1, -0.3F, false));

        bone5 = new ModelRenderer(this);
        bone5.setRotationPoint(0.0F, -6.5F, 4.0F);
        setRotationAngle(bone5, 0.0F, 0.1745F, -2.7053F);
        bone.addChild(bone5);
        bone5.cubeList.add(new ModelBox(bone5, 50, 20, -5.6805F, -2.0F, 0.1061F, 6, 4, 1, -0.3F, true));

        bone6 = new ModelRenderer(this);
        bone6.setRotationPoint(3.0F, -3.0F, 1.0F);
        setRotationAngle(bone6, 0.0F, 0.0F, -0.2618F);
        bone.addChild(bone6);
        bone6.cubeList.add(new ModelBox(bone6, 48, 0, 0.1F, -3.5F, -3.3F, 2, 7, 6, 0.0F, false));

        bone7 = new ModelRenderer(this);
        bone7.setRotationPoint(-3.0F, -3.0F, 1.0F);
        setRotationAngle(bone7, 0.0F, 0.0F, 0.2618F);
        bone.addChild(bone7);
        bone7.cubeList.add(new ModelBox(bone7, 48, 0, -2.1F, -3.5F, -3.3F, 2, 7, 6, 0.0F, true));

        bone8 = new ModelRenderer(this);
        bone8.setRotationPoint(0.0F, -8.0F, 6.0F);
        setRotationAngle(bone8, 0.1745F, 0.0F, 0.0F);
        bone.addChild(bone8);
        bone8.cubeList.add(new ModelBox(bone8, 54, 35, -2.0F, -0.6042F, -1.0909F, 4, 9, 1, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        bone.render(f5);
    }
    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}