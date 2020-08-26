package com.github.tartaricacid.touhoulittlemaid.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class PlayStationModel extends ModelBase {
    private final ModelRenderer nintendo;
    private final ModelRenderer bone2;
    private final ModelRenderer entertainment;
    private final ModelRenderer drawer;
    private final ModelRenderer tv;
    private final ModelRenderer bone16;
    private final ModelRenderer bone17;
    private final ModelRenderer cassette;
    private final ModelRenderer cassette3;
    private final ModelRenderer bone15;
    private final ModelRenderer bone14;
    private final ModelRenderer cassette2;
    private final ModelRenderer cushion;
    private final ModelRenderer controller;
    private final ModelRenderer line;
    private final ModelRenderer bone;
    private final ModelRenderer bone4;
    private final ModelRenderer bone3;
    private final ModelRenderer bone5;
    private final ModelRenderer bone6;
    private final ModelRenderer bone7;
    private final ModelRenderer bone8;
    private final ModelRenderer bone10;
    private final ModelRenderer bone11;
    private final ModelRenderer bone12;
    private final ModelRenderer bone13;
    private final ModelRenderer joypad;
    private final ModelRenderer button;

    public PlayStationModel() {
        textureWidth = 128;
        textureHeight = 128;

        nintendo = new ModelRenderer(this);
        nintendo.setRotationPoint(-11.0F, 23.0F, -9.0F);
        setRotationAngle(nintendo, 0.0F, 0.5236F, 0.0F);
        nintendo.cubeList.add(new ModelBox(nintendo, 60, 60, -3.0F, -1.0F, -4.0F, 6, 2, 8, 0.0F, false));
        nintendo.cubeList.add(new ModelBox(nintendo, 77, 9, -3.0F, -2.0F, 0.0F, 6, 1, 3, 0.0F, false));
        nintendo.cubeList.add(new ModelBox(nintendo, 0, 2, -2.5F, -2.25F, 1.5F, 1, 1, 1, 0.0F, false));
        nintendo.cubeList.add(new ModelBox(nintendo, 0, 0, 1.5F, -2.25F, 1.5F, 1, 1, 1, 0.0F, false));
        nintendo.cubeList.add(new ModelBox(nintendo, 77, 18, -3.0F, -2.0F, -4.0F, 6, 1, 1, 0.0F, false));
        nintendo.cubeList.add(new ModelBox(nintendo, 15, 77, -2.5F, -2.0F, -3.0F, 5, 1, 3, 0.0F, false));
        nintendo.cubeList.add(new ModelBox(nintendo, 0, 21, -2.0F, -4.0F, -2.0F, 4, 2, 1, 0.0F, false));
        nintendo.cubeList.add(new ModelBox(nintendo, 0, 33, -1.5F, -3.5F, -0.999F, 3, 2, 0, 0.0F, false));

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(0.0F, 0.0F, 0.0F);
        nintendo.addChild(bone2);
        setRotationAngle(bone2, -0.7854F, 0.0F, 0.0F);
        bone2.cubeList.add(new ModelBox(bone2, 0, 11, -2.5F, -3.54F, 0.9F, 5, 0, 1, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 35, 38, -3.0F, -3.5355F, 1.1213F, 6, 1, 1, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 65, 34, -3.0F, -3.5355F, 0.7071F, 6, 1, 1, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 5, 34, -1.0F, -2.8284F, -1.0F, 2, 1, 1, 0.0F, false));

        entertainment = new ModelRenderer(this);
        entertainment.setRotationPoint(0.0F, 24.0F, -30.0F);


        drawer = new ModelRenderer(this);
        drawer.setRotationPoint(0.0F, 0.0F, 0.0F);
        entertainment.addChild(drawer);
        drawer.cubeList.add(new ModelBox(drawer, 39, 14, 7.0F, -11.0F, -6.0F, 1, 11, 12, 0.0F, false));
        drawer.cubeList.add(new ModelBox(drawer, 38, 38, -8.0F, -11.0F, -6.0F, 1, 11, 12, 0.0F, false));
        drawer.cubeList.add(new ModelBox(drawer, 0, 26, -7.0F, -1.0F, -5.0F, 14, 1, 11, 0.0F, false));
        drawer.cubeList.add(new ModelBox(drawer, 0, 53, -7.0F, -11.0F, -6.0F, 14, 11, 1, 0.0F, false));
        drawer.cubeList.add(new ModelBox(drawer, 0, 14, -7.0F, -5.0F, -5.0F, 14, 1, 11, 0.0F, false));
        drawer.cubeList.add(new ModelBox(drawer, 0, 0, -8.5F, -12.0F, -6.5F, 17, 1, 13, 0.0F, false));

        tv = new ModelRenderer(this);
        tv.setRotationPoint(0.0F, -12.0F, -2.0F);
        entertainment.addChild(tv);
        setRotationAngle(tv, 0.0F, 3.1416F, 0.0F);
        tv.cubeList.add(new ModelBox(tv, 78, 78, -8.0F, -12.0F, -8.0F, 1, 12, 1, 0.0F, false));
        tv.cubeList.add(new ModelBox(tv, 74, 78, 7.0F, -12.0F, -8.0F, 1, 12, 1, 0.0F, false));
        tv.cubeList.add(new ModelBox(tv, 65, 32, -7.0F, -1.0F, -8.0F, 14, 1, 1, 0.0F, false));
        tv.cubeList.add(new ModelBox(tv, 65, 30, -7.0F, -12.0F, -8.0F, 14, 1, 1, 0.0F, false));
        tv.cubeList.add(new ModelBox(tv, 0, 65, -7.0F, -11.0F, -7.5F, 14, 10, 0, 0.0F, false));
        tv.cubeList.add(new ModelBox(tv, 0, 38, -8.0F, -12.0F, -7.0F, 16, 12, 3, 0.0F, false));
        tv.cubeList.add(new ModelBox(tv, 52, 37, -7.5F, -11.5F, -4.0F, 15, 11, 1, 0.0F, false));
        tv.cubeList.add(new ModelBox(tv, 30, 61, -7.0F, -11.0F, -3.0F, 14, 10, 1, 0.0F, false));
        tv.cubeList.add(new ModelBox(tv, 64, 49, -6.5F, -10.5F, -2.0F, 13, 9, 1, 0.0F, false));
        tv.cubeList.add(new ModelBox(tv, 65, 21, -6.0F, -10.0F, -1.0F, 12, 8, 1, 0.0F, false));
        tv.cubeList.add(new ModelBox(tv, 66, 70, -5.5F, -9.5F, 0.0F, 11, 7, 1, 0.0F, false));
        tv.cubeList.add(new ModelBox(tv, 71, 0, -5.0F, -9.0F, 1.0F, 10, 6, 1, 0.0F, false));
        tv.cubeList.add(new ModelBox(tv, 28, 72, -4.5F, -8.5F, 2.0F, 9, 5, 1, 0.0F, false));
        tv.cubeList.add(new ModelBox(tv, 0, 75, -4.0F, -8.0F, 3.0F, 8, 4, 1, 0.0F, false));
        tv.cubeList.add(new ModelBox(tv, 47, 0, -4.0F, -4.0F, -4.0F, 8, 4, 8, 0.0F, false));

        bone16 = new ModelRenderer(this);
        bone16.setRotationPoint(-4.0F, 0.0F, 4.0F);
        tv.addChild(bone16);
        setRotationAngle(bone16, 0.0F, 0.4538F, 0.0F);
        bone16.cubeList.add(new ModelBox(bone16, 52, 70, 0.4384F, -4.0F, -8.8988F, 3, 4, 8, 0.0F, true));

        bone17 = new ModelRenderer(this);
        bone17.setRotationPoint(4.0F, 0.0F, 4.0F);
        tv.addChild(bone17);
        setRotationAngle(bone17, 0.0F, -0.4538F, 0.0F);
        bone17.cubeList.add(new ModelBox(bone17, 52, 70, -3.4384F, -4.0F, -8.8988F, 3, 4, 8, 0.0F, false));

        cassette = new ModelRenderer(this);
        cassette.setRotationPoint(0.0F, 0.0F, 0.0F);
        entertainment.addChild(cassette);
        cassette.cubeList.add(new ModelBox(cassette, 0, 0, 6.0F, -8.0F, 2.0F, 1, 3, 4, 0.0F, false));
        cassette.cubeList.add(new ModelBox(cassette, 0, 14, 5.0F, -8.0F, 1.5F, 1, 3, 4, 0.0F, false));
        cassette.cubeList.add(new ModelBox(cassette, 0, 0, 4.0F, -8.0F, 1.75F, 1, 3, 4, 0.0F, false));
        cassette.cubeList.add(new ModelBox(cassette, 0, 14, 3.0F, -8.0F, 1.25F, 1, 3, 4, 0.0F, false));

        cassette3 = new ModelRenderer(this);
        cassette3.setRotationPoint(-1.0F, 0.0F, 0.0F);
        entertainment.addChild(cassette3);
        cassette3.cubeList.add(new ModelBox(cassette3, 41, 78, -6.0F, -6.0F, 1.0F, 3, 1, 4, 0.0F, false));
        cassette3.cubeList.add(new ModelBox(cassette3, 77, 13, -5.5F, -7.0F, 0.5F, 3, 1, 4, 0.0F, false));
        cassette3.cubeList.add(new ModelBox(cassette3, 41, 78, -5.0F, -8.0F, 1.5F, 3, 1, 4, 0.0F, false));
        cassette3.cubeList.add(new ModelBox(cassette3, 27, 78, -5.5F, -9.0F, 1.0F, 3, 1, 4, 0.0F, false));
        cassette3.cubeList.add(new ModelBox(cassette3, 77, 13, 1.5F, -10.0F, 2.0F, 3, 1, 4, 0.0F, false));

        bone15 = new ModelRenderer(this);
        bone15.setRotationPoint(0.0F, 0.0F, 0.0F);
        cassette3.addChild(bone15);
        setRotationAngle(bone15, 0.0F, -0.1745F, 0.0F);
        bone15.cubeList.add(new ModelBox(bone15, 41, 78, 0.5F, -9.0F, 2.0F, 3, 1, 4, 0.0F, false));

        bone14 = new ModelRenderer(this);
        bone14.setRotationPoint(0.0F, 0.0F, 0.0F);
        cassette3.addChild(bone14);
        setRotationAngle(bone14, 0.0F, 0.2618F, 0.0F);
        bone14.cubeList.add(new ModelBox(bone14, 27, 78, 2.5F, -9.0F, 3.0F, 3, 1, 4, 0.0F, false));

        cassette2 = new ModelRenderer(this);
        cassette2.setRotationPoint(-4.0F, 0.0F, 1.0F);
        entertainment.addChild(cassette2);
        setRotationAngle(cassette2, 0.0F, -0.2618F, 0.0F);
        cassette2.cubeList.add(new ModelBox(cassette2, 0, 26, 5.8262F, -8.0F, -1.5703F, 1, 3, 4, 0.0F, false));
        cassette2.cubeList.add(new ModelBox(cassette2, 0, 0, 4.8262F, -8.0F, -1.0703F, 1, 3, 4, 0.0F, false));
        cassette2.cubeList.add(new ModelBox(cassette2, 0, 14, 3.8262F, -8.0F, -0.8203F, 1, 3, 4, 0.0F, false));
        cassette2.cubeList.add(new ModelBox(cassette2, 0, 0, 2.8262F, -8.0F, -0.3203F, 1, 3, 4, 0.0F, false));

        cushion = new ModelRenderer(this);
        cushion.setRotationPoint(0.0F, 24.0F, 0.0F);
        cushion.cubeList.add(new ModelBox(cushion, 53, 12, -4.0F, -1.0F, -4.0F, 8, 1, 8, 0.0F, false));

        controller = new ModelRenderer(this);
        controller.setRotationPoint(0.0F, 20.0F, 0.0F);


        line = new ModelRenderer(this);
        line.setRotationPoint(0.0F, 4.0F, 0.0F);
        controller.addChild(line);


        bone = new ModelRenderer(this);
        bone.setRotationPoint(-1.0F, 0.0F, 0.0F);
        line.addChild(bone);
        setRotationAngle(bone, 0.5236F, 0.0F, 0.0F);
        bone.cubeList.add(new ModelBox(bone, 11, 11, 0.5F, -12.1394F, -1.5F, 1, 1, 0, 0.0F, false));

        bone4 = new ModelRenderer(this);
        bone4.setRotationPoint(-1.0F, 0.0F, 0.0F);
        line.addChild(bone4);
        setRotationAngle(bone4, 1.0472F, 0.0F, 0.0F);
        bone4.cubeList.add(new ModelBox(bone4, 10, 9, 0.5F, -12.263F, 4.7706F, 1, 1, 0, 0.0F, false));

        bone3 = new ModelRenderer(this);
        bone3.setRotationPoint(-1.0F, 0.0F, 0.0F);
        line.addChild(bone3);
        bone3.cubeList.add(new ModelBox(bone3, 7, 1, 0.5F, -10.263F, -9.2347F, 1, 0, 1, 0.0F, false));

        bone5 = new ModelRenderer(this);
        bone5.setRotationPoint(-1.0F, 0.0F, 0.0F);
        line.addChild(bone5);
        setRotationAngle(bone5, 0.5236F, 0.0F, 0.0F);
        bone5.cubeList.add(new ModelBox(bone5, 7, 0, 0.5F, -13.5054F, -3.866F, 1, 0, 1, 0.0F, false));

        bone6 = new ModelRenderer(this);
        bone6.setRotationPoint(-1.0F, 0.0F, 0.0F);
        line.addChild(bone6);
        setRotationAngle(bone6, 1.0472F, 0.0F, 0.0F);
        bone6.cubeList.add(new ModelBox(bone6, 5, 3, 0.5F, -13.629F, 2.4046F, 1, 0, 1, 0.0F, false));

        bone7 = new ModelRenderer(this);
        bone7.setRotationPoint(-1.0F, 0.0F, 0.0F);
        line.addChild(bone7);
        bone7.cubeList.add(new ModelBox(bone7, 10, 0, 0.5F, -8.897F, -10.6008F, 1, 7, 0, 0.0F, false));

        bone8 = new ModelRenderer(this);
        bone8.setRotationPoint(-1.0F, 0.0F, 0.0F);
        line.addChild(bone8);
        setRotationAngle(bone8, 0.5236F, 0.0F, 0.0F);
        bone8.cubeList.add(new ModelBox(bone8, 10, 10, 0.5F, -6.9432F, -8.232F, 1, 1, 0, 0.0F, false));

        bone10 = new ModelRenderer(this);
        bone10.setRotationPoint(-1.0F, 0.0F, 0.0F);
        line.addChild(bone10);
        setRotationAngle(bone10, 1.0472F, 0.0F, 0.0F);
        bone10.cubeList.add(new ModelBox(bone10, 9, 8, 0.5F, -9.2625F, -4.1577F, 1, 1, 0, 0.0F, false));

        bone11 = new ModelRenderer(this);
        bone11.setRotationPoint(-1.0F, 0.0F, 0.0F);
        line.addChild(bone11);
        bone11.cubeList.add(new ModelBox(bone11, 5, 2, 0.5F, -0.5306F, -9.2344F, 1, 0, 1, 0.0F, false));
        bone11.cubeList.add(new ModelBox(bone11, 0, 7, -5.866F, -0.5296F, -7.8684F, 6, 0, 1, 0.0F, false));

        bone12 = new ModelRenderer(this);
        bone12.setRotationPoint(-1.0F, 0.0F, 0.0F);
        line.addChild(bone12);
        setRotationAngle(bone12, 0.0F, -0.5236F, 0.0F);
        bone12.cubeList.add(new ModelBox(bone12, 5, 1, -3.8182F, -0.5296F, -7.8812F, 1, 0, 1, 0.0F, false));

        bone13 = new ModelRenderer(this);
        bone13.setRotationPoint(-1.0F, 0.0F, 0.0F);
        line.addChild(bone13);
        setRotationAngle(bone13, 0.0F, -1.0472F, 0.0F);
        bone13.cubeList.add(new ModelBox(bone13, 5, 0, -6.8812F, -0.5296F, -4.5502F, 1, 0, 1, 0.0F, false));

        joypad = new ModelRenderer(this);
        joypad.setRotationPoint(0.0F, 0.0F, 0.0F);
        controller.addChild(joypad);
        setRotationAngle(joypad, 0.3491F, 0.0F, 0.0F);
        joypad.cubeList.add(new ModelBox(joypad, 0, 8, -2.0F, -7.0F, -5.5F, 4, 2, 1, 0.0F, false));

        button = new ModelRenderer(this);
        button.setRotationPoint(-0.8333F, -0.25F, -0.2505F);
        joypad.addChild(button);
        button.cubeList.add(new ModelBox(button, 64, 2, 1.5833F, -6.5F, -4.9995F, 0, 1, 1, 0.0F, false));
        button.cubeList.add(new ModelBox(button, 64, 2, 1.0833F, -6.0F, -4.9995F, 1, 0, 1, 0.0F, false));
        button.cubeList.add(new ModelBox(button, 64, 2, 0.0833F, -5.5F, -4.9995F, 0, 0, 1, 0.0F, false));
        button.cubeList.add(new ModelBox(button, 64, 2, -0.9167F, -5.5F, -4.9995F, 0, 0, 1, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        nintendo.render(f5);
        entertainment.render(f5);
        cushion.render(f5);
        controller.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
