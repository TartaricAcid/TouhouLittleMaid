package com.github.tartaricacid.touhoulittlemaid.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class BookDeskModel extends ModelBase {
    private final ModelRenderer bone;
    private final ModelRenderer side;
    private final ModelRenderer side2;
    private final ModelRenderer bone2;
    private final ModelRenderer side3;
    private final ModelRenderer bone3;
    private final ModelRenderer bone4;
    private final ModelRenderer side4;
    private final ModelRenderer bone5;
    private final ModelRenderer bone6;
    private final ModelRenderer bone7;
    private final ModelRenderer bone8;

    public BookDeskModel() {
        textureWidth = 128;
        textureHeight = 128;

        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, 24.0F, 9.0F);
        bone.cubeList.add(new ModelBox(bone, 0, 0, -13.0F, -16.0F, -5.0F, 26, 1, 10, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 11, -10.5F, -12.0F, -4.5F, 21, 1, 9, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 30, 21, -11.5F, -2.0F, 0.5F, 23, 1, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 30, 23, -10.5F, -15.0F, 3.5F, 21, 3, 1, 0.0F, false));

        side = new ModelRenderer(this);
        side.setRotationPoint(3.0F, 0.0F, 3.0F);
        bone.addChild(side);
        side.cubeList.add(new ModelBox(side, 0, 43, -14.5F, -15.0F, -7.5F, 1, 4, 9, 0.0F, false));
        side.cubeList.add(new ModelBox(side, 48, 61, -15.5F, -15.0F, -7.0F, 1, 13, 2, 0.0F, false));
        side.cubeList.add(new ModelBox(side, 42, 61, -15.5F, -15.0F, -1.0F, 1, 13, 2, 0.0F, false));
        side.cubeList.add(new ModelBox(side, 42, 27, -15.5F, -2.0F, -8.0F, 1, 1, 10, 0.0F, false));
        side.cubeList.add(new ModelBox(side, 4, 28, -15.5F, -1.0F, 1.0F, 1, 1, 2, 0.0F, false));
        side.cubeList.add(new ModelBox(side, 0, 27, -15.5F, -1.0F, -9.0F, 1, 1, 2, 0.0F, false));

        side2 = new ModelRenderer(this);
        side2.setRotationPoint(-3.0F, 0.0F, 3.0F);
        bone.addChild(side2);
        side2.cubeList.add(new ModelBox(side2, 25, 41, 13.5F, -15.0F, -7.5F, 1, 4, 9, 0.0F, false));
        side2.cubeList.add(new ModelBox(side2, 36, 61, 14.5F, -15.0F, -7.0F, 1, 13, 2, 0.0F, false));
        side2.cubeList.add(new ModelBox(side2, 30, 61, 14.5F, -15.0F, -1.0F, 1, 13, 2, 0.0F, false));
        side2.cubeList.add(new ModelBox(side2, 12, 33, 14.5F, -2.0F, -8.0F, 1, 1, 10, 0.0F, false));
        side2.cubeList.add(new ModelBox(side2, 4, 25, 14.5F, -1.0F, 1.0F, 1, 1, 2, 0.0F, false));
        side2.cubeList.add(new ModelBox(side2, 0, 17, 14.5F, -1.0F, -9.0F, 1, 1, 2, 0.0F, false));

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(0.0F, 24.0F, 0.0F);
        bone2.cubeList.add(new ModelBox(bone2, 0, 21, -5.0F, -7.0F, -5.0F, 10, 1, 10, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 36, 41, -5.0F, -17.3F, -6.4142F, 10, 5, 1, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 24, 34, -3.5F, -1.5F, -0.5F, 7, 1, 1, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 24, 36, -3.0F, -16.8284F, -7.4142F, 6, 1, 1, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 24, 32, -3.5F, -6.0F, -4.5F, 7, 1, 1, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 30, 27, -3.5F, -13.8F, -7.4142F, 7, 1, 1, 0.0F, false));

        side3 = new ModelRenderer(this);
        side3.setRotationPoint(3.0F, 0.0F, 3.0F);
        bone2.addChild(side3);
        side3.cubeList.add(new ModelBox(side3, 0, 21, -7.4142F, -5.0F, -4.0F, 1, 4, 2, 0.0F, false));
        side3.cubeList.add(new ModelBox(side3, 0, 32, -7.4142F, -6.0F, -9.0F, 1, 1, 10, 0.0F, false));
        side3.cubeList.add(new ModelBox(side3, 4, 0, -7.4142F, -15.4142F, -10.4142F, 1, 9, 1, 0.0F, false));
        side3.cubeList.add(new ModelBox(side3, 12, 61, -7.415F, -1.5F, -7.0F, 1, 1, 8, 0.0F, false));
        side3.cubeList.add(new ModelBox(side3, 16, 32, -7.4142F, -1.0F, 0.5F, 1, 1, 1, 0.0F, false));
        side3.cubeList.add(new ModelBox(side3, 12, 32, -7.4142F, -1.0F, -7.5F, 1, 1, 1, 0.0F, false));

        bone3 = new ModelRenderer(this);
        bone3.setRotationPoint(0.0F, 0.0F, 3.0F);
        side3.addChild(bone3);
        setRotationAngle(bone3, 0.0F, 0.0F, 0.7854F);
        bone3.cubeList.add(new ModelBox(bone3, 4, 32, -16.1421F, -7.6569F, -13.4142F, 1, 2, 1, 0.0F, false));

        bone4 = new ModelRenderer(this);
        bone4.setRotationPoint(0.0F, 0.0F, 3.0F);
        side3.addChild(bone4);
        setRotationAngle(bone4, 0.7854F, 0.0F, 0.0F);
        bone4.cubeList.add(new ModelBox(bone4, 0, 32, -7.4142F, -14.0208F, -4.9497F, 1, 2, 1, 0.0F, false));

        side4 = new ModelRenderer(this);
        side4.setRotationPoint(-3.0F, 0.0F, 3.0F);
        bone2.addChild(side4);
        side4.cubeList.add(new ModelBox(side4, 0, 11, 6.4142F, -5.0F, -4.0F, 1, 4, 2, 0.0F, false));
        side4.cubeList.add(new ModelBox(side4, 30, 30, 6.4142F, -6.0F, -9.0F, 1, 1, 10, 0.0F, false));
        side4.cubeList.add(new ModelBox(side4, 0, 0, 6.4142F, -15.4142F, -10.4142F, 1, 9, 1, 0.0F, false));
        side4.cubeList.add(new ModelBox(side4, 59, 41, 6.415F, -1.5F, -7.0F, 1, 1, 8, 0.0F, false));
        side4.cubeList.add(new ModelBox(side4, 30, 29, 6.4142F, -1.0F, 0.5F, 1, 1, 1, 0.0F, false));
        side4.cubeList.add(new ModelBox(side4, 4, 11, 6.4142F, -1.0F, -7.5F, 1, 1, 1, 0.0F, false));

        bone5 = new ModelRenderer(this);
        bone5.setRotationPoint(0.0F, 0.0F, 3.0F);
        side4.addChild(bone5);
        setRotationAngle(bone5, 0.0F, 0.0F, -0.7854F);
        bone5.cubeList.add(new ModelBox(bone5, 6, 21, 15.1421F, -7.6569F, -13.4142F, 1, 2, 1, 0.0F, false));

        bone6 = new ModelRenderer(this);
        bone6.setRotationPoint(0.0F, 0.0F, 3.0F);
        side4.addChild(bone6);
        setRotationAngle(bone6, 0.7854F, 0.0F, 0.0F);
        bone6.cubeList.add(new ModelBox(bone6, 5, 16, 6.4142F, -14.0208F, -4.9497F, 1, 2, 1, 0.0F, false));

        bone7 = new ModelRenderer(this);
        bone7.setRotationPoint(-7.0F, 6.0F, 9.0F);
        setRotationAngle(bone7, 0.0F, 0.2182F, 0.0F);
        bone7.cubeList.add(new ModelBox(bone7, 58, 34, -2.0F, 1.0F, -3.0F, 4, 1, 6, 0.0F, false));
        bone7.cubeList.add(new ModelBox(bone7, 0, 56, -2.0F, 0.0F, -3.0F, 4, 1, 6, 0.0F, false));
        bone7.cubeList.add(new ModelBox(bone7, 54, 27, -2.0F, -1.0F, -3.0F, 4, 1, 6, 0.0F, false));
        bone7.cubeList.add(new ModelBox(bone7, 54, 54, -2.0F, -2.0F, -3.0F, 4, 1, 6, 0.0F, false));

        bone8 = new ModelRenderer(this);
        bone8.setRotationPoint(7.0F, 6.0F, 9.0F);
        setRotationAngle(bone8, 0.0F, -0.0436F, 0.0F);
        bone8.cubeList.add(new ModelBox(bone8, 34, 54, -2.0F, 1.0F, -3.0F, 4, 1, 6, 0.0F, false));
        bone8.cubeList.add(new ModelBox(bone8, 14, 54, -2.0F, 0.0F, -3.0F, 4, 1, 6, 0.0F, false));
        bone8.cubeList.add(new ModelBox(bone8, 51, 11, -2.0F, -1.0F, -3.0F, 4, 1, 6, 0.0F, false));
        bone8.cubeList.add(new ModelBox(bone8, 45, 47, -2.0F, -2.0F, -3.0F, 4, 1, 6, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        bone.render(f5);
        bone2.render(f5);
        bone7.render(f5);
        bone8.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
