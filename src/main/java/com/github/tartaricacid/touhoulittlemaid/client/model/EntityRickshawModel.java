package com.github.tartaricacid.touhoulittlemaid.client.model;


import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * @author TartaricAcid
 * @date 2020/2/3 16:52
 **/
public class EntityRickshawModel extends ModelBase {
    private final ModelRenderer main;
    private final ModelRenderer wheel;
    private final ModelRenderer wheelRight;
    private final ModelRenderer section;
    private final ModelRenderer bone;
    private final ModelRenderer bone2;
    private final ModelRenderer bone3;
    private final ModelRenderer bone4;
    private final ModelRenderer bone5;
    private final ModelRenderer bone6;
    private final ModelRenderer section2;
    private final ModelRenderer bone7;
    private final ModelRenderer bone8;
    private final ModelRenderer bone9;
    private final ModelRenderer bone10;
    private final ModelRenderer bone11;
    private final ModelRenderer bone12;
    private final ModelRenderer section3;
    private final ModelRenderer bone13;
    private final ModelRenderer bone14;
    private final ModelRenderer bone15;
    private final ModelRenderer bone16;
    private final ModelRenderer bone17;
    private final ModelRenderer bone18;
    private final ModelRenderer section4;
    private final ModelRenderer bone19;
    private final ModelRenderer bone20;
    private final ModelRenderer bone21;
    private final ModelRenderer bone22;
    private final ModelRenderer bone23;
    private final ModelRenderer bone24;
    private final ModelRenderer wheelLeft;
    private final ModelRenderer section5;
    private final ModelRenderer bone25;
    private final ModelRenderer bone26;
    private final ModelRenderer bone27;
    private final ModelRenderer bone28;
    private final ModelRenderer bone29;
    private final ModelRenderer bone30;
    private final ModelRenderer section6;
    private final ModelRenderer bone31;
    private final ModelRenderer bone32;
    private final ModelRenderer bone34;
    private final ModelRenderer bone37;
    private final ModelRenderer bone38;
    private final ModelRenderer bone39;
    private final ModelRenderer section7;
    private final ModelRenderer bone40;
    private final ModelRenderer bone41;
    private final ModelRenderer bone42;
    private final ModelRenderer bone43;
    private final ModelRenderer bone44;
    private final ModelRenderer bone45;
    private final ModelRenderer section8;
    private final ModelRenderer bone46;
    private final ModelRenderer bone47;
    private final ModelRenderer bone48;
    private final ModelRenderer bone49;
    private final ModelRenderer bone50;
    private final ModelRenderer bone51;
    private final ModelRenderer body;
    private final ModelRenderer upper;
    private final ModelRenderer awning;
    private final ModelRenderer triangle;
    private final ModelRenderer bone35;
    private final ModelRenderer bone33;
    private final ModelRenderer triangle2;
    private final ModelRenderer bone36;
    private final ModelRenderer bone52;
    private final ModelRenderer triangle3;
    private final ModelRenderer bone53;
    private final ModelRenderer bone55;
    private final ModelRenderer bone54;
    private final ModelRenderer awning2;
    private final ModelRenderer triangle4;
    private final ModelRenderer bone56;
    private final ModelRenderer bone57;
    private final ModelRenderer triangle5;
    private final ModelRenderer bone58;
    private final ModelRenderer bone59;
    private final ModelRenderer triangle6;
    private final ModelRenderer bone60;
    private final ModelRenderer bone61;
    private final ModelRenderer bone62;
    private final ModelRenderer bone63;
    private final ModelRenderer cart;

    public EntityRickshawModel() {
        textureWidth = 128;
        textureHeight = 128;

        main = new ModelRenderer(this);
        main.setRotationPoint(0.0F, 24.0F, 0.0F);

        wheel = new ModelRenderer(this);
        wheel.setRotationPoint(0.0F, -7.6F, 0.0F);
        main.addChild(wheel);
        wheel.cubeList.add(new ModelBox(wheel, 32, 21, -10.0F, -0.4958F, -0.5F, 20, 1, 1, 0.0F, false));

        wheelRight = new ModelRenderer(this);
        wheelRight.setRotationPoint(-9.0F, 7.6F, 0.0F);
        wheel.addChild(wheelRight);

        section = new ModelRenderer(this);
        section.setRotationPoint(0.0F, 0.0F, 0.0F);
        wheelRight.addChild(section);

        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, 0.0F, 0.0F);
        section.addChild(bone);
        bone.cubeList.add(new ModelBox(bone, 4, 4, -0.5F, -1.0F, -1.0F, 1, 1, 2, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 31, -0.5F, -7.0F, -0.5F, 1, 6, 1, 0.0F, false));

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone2, -0.2618F, 0.0F, 0.0F);
        section.addChild(bone2);
        bone2.cubeList.add(new ModelBox(bone2, 4, 4, -0.5F, -0.7412F, -2.9659F, 1, 1, 2, 0.0F, false));

        bone3 = new ModelRenderer(this);
        bone3.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone3, -0.5236F, 0.0F, 0.0F);
        section.addChild(bone3);
        bone3.cubeList.add(new ModelBox(bone3, 4, 4, -0.5F, 0.0176F, -4.7979F, 1, 1, 2, 0.0F, false));

        bone4 = new ModelRenderer(this);
        bone4.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone4, -0.7854F, 0.0F, 0.0F);
        section.addChild(bone4);
        bone4.cubeList.add(new ModelBox(bone4, 4, 4, -0.5F, 1.2247F, -6.371F, 1, 1, 2, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 0, 31, -0.5F, -4.7753F, -5.871F, 1, 6, 1, 0.0F, false));

        bone5 = new ModelRenderer(this);
        bone5.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone5, -1.0472F, 0.0F, 0.0F);
        section.addChild(bone5);
        bone5.cubeList.add(new ModelBox(bone5, 4, 4, -0.5F, 2.7979F, -7.5781F, 1, 1, 2, 0.0F, false));

        bone6 = new ModelRenderer(this);
        bone6.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone6, -1.309F, 0.0F, 0.0F);
        section.addChild(bone6);
        bone6.cubeList.add(new ModelBox(bone6, 4, 4, -0.5F, 4.6298F, -8.3369F, 1, 1, 2, 0.0F, false));

        section2 = new ModelRenderer(this);
        section2.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(section2, -1.5708F, 0.0F, 0.0F);
        wheelRight.addChild(section2);

        bone7 = new ModelRenderer(this);
        bone7.setRotationPoint(0.0F, 0.0F, 0.0F);
        section2.addChild(bone7);
        bone7.cubeList.add(new ModelBox(bone7, 4, 4, -0.5F, 6.5957F, -8.5958F, 1, 1, 2, 0.0F, false));
        bone7.cubeList.add(new ModelBox(bone7, 0, 31, -0.5F, 0.5957F, -8.0958F, 1, 6, 1, 0.0F, false));

        bone8 = new ModelRenderer(this);
        bone8.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone8, -0.2618F, 0.0F, 0.0F);
        section2.addChild(bone8);
        bone8.cubeList.add(new ModelBox(bone8, 4, 4, -0.5F, 8.5617F, -8.3369F, 1, 1, 2, 0.0F, false));

        bone9 = new ModelRenderer(this);
        bone9.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone9, -0.5236F, 0.0F, 0.0F);
        section2.addChild(bone9);
        bone9.cubeList.add(new ModelBox(bone9, 4, 4, -0.5F, 10.3936F, -7.5781F, 1, 1, 2, 0.0F, false));

        bone10 = new ModelRenderer(this);
        bone10.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone10, -0.7854F, 0.0F, 0.0F);
        section2.addChild(bone10);
        bone10.cubeList.add(new ModelBox(bone10, 4, 4, -0.5F, 11.9668F, -6.371F, 1, 1, 2, 0.0F, false));
        bone10.cubeList.add(new ModelBox(bone10, 0, 31, -0.5F, 5.9668F, -5.871F, 1, 6, 1, 0.0F, false));

        bone11 = new ModelRenderer(this);
        bone11.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone11, -1.0472F, 0.0F, 0.0F);
        section2.addChild(bone11);
        bone11.cubeList.add(new ModelBox(bone11, 4, 4, -0.5F, 13.1739F, -4.7979F, 1, 1, 2, 0.0F, false));

        bone12 = new ModelRenderer(this);
        bone12.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone12, -1.309F, 0.0F, 0.0F);
        section2.addChild(bone12);
        bone12.cubeList.add(new ModelBox(bone12, 4, 4, -0.5F, 13.9327F, -2.9659F, 1, 1, 2, 0.0F, false));

        section3 = new ModelRenderer(this);
        section3.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(section3, 3.1416F, 0.0F, 0.0F);
        wheelRight.addChild(section3);

        bone13 = new ModelRenderer(this);
        bone13.setRotationPoint(0.0F, 0.0F, 0.0F);
        section3.addChild(bone13);
        bone13.cubeList.add(new ModelBox(bone13, 4, 4, -0.5F, 14.1915F, -1.0F, 1, 1, 2, 0.0F, false));
        bone13.cubeList.add(new ModelBox(bone13, 0, 31, -0.5F, 8.1915F, -0.5F, 1, 6, 1, 0.0F, false));

        bone14 = new ModelRenderer(this);
        bone14.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone14, -0.2618F, 0.0F, 0.0F);
        section3.addChild(bone14);
        bone14.cubeList.add(new ModelBox(bone14, 4, 4, -0.5F, 13.9327F, 0.9659F, 1, 1, 2, 0.0F, false));

        bone15 = new ModelRenderer(this);
        bone15.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone15, -0.5236F, 0.0F, 0.0F);
        section3.addChild(bone15);
        bone15.cubeList.add(new ModelBox(bone15, 4, 4, -0.5F, 13.1739F, 2.7979F, 1, 1, 2, 0.0F, false));

        bone16 = new ModelRenderer(this);
        bone16.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone16, -0.7854F, 0.0F, 0.0F);
        section3.addChild(bone16);
        bone16.cubeList.add(new ModelBox(bone16, 4, 4, -0.5F, 11.9668F, 4.371F, 1, 1, 2, 0.0F, false));
        bone16.cubeList.add(new ModelBox(bone16, 0, 31, -0.5F, 5.9668F, 4.871F, 1, 6, 1, 0.0F, false));

        bone17 = new ModelRenderer(this);
        bone17.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone17, -1.0472F, 0.0F, 0.0F);
        section3.addChild(bone17);
        bone17.cubeList.add(new ModelBox(bone17, 4, 4, -0.5F, 10.3936F, 5.5781F, 1, 1, 2, 0.0F, false));

        bone18 = new ModelRenderer(this);
        bone18.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone18, -1.309F, 0.0F, 0.0F);
        section3.addChild(bone18);
        bone18.cubeList.add(new ModelBox(bone18, 4, 4, -0.5F, 8.5617F, 6.3369F, 1, 1, 2, 0.0F, false));

        section4 = new ModelRenderer(this);
        section4.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(section4, 1.5708F, 0.0F, 0.0F);
        wheelRight.addChild(section4);

        bone19 = new ModelRenderer(this);
        bone19.setRotationPoint(0.0F, 0.0F, 0.0F);
        section4.addChild(bone19);
        bone19.cubeList.add(new ModelBox(bone19, 4, 4, -0.5F, 6.5957F, 6.5957F, 1, 1, 2, 0.0F, false));
        bone19.cubeList.add(new ModelBox(bone19, 0, 31, -0.5F, 0.5957F, 7.0957F, 1, 6, 1, 0.0F, false));

        bone20 = new ModelRenderer(this);
        bone20.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone20, -0.2618F, 0.0F, 0.0F);
        section4.addChild(bone20);
        bone20.cubeList.add(new ModelBox(bone20, 4, 4, -0.5F, 4.6298F, 6.3369F, 1, 1, 2, 0.0F, false));

        bone21 = new ModelRenderer(this);
        bone21.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone21, -0.5236F, 0.0F, 0.0F);
        section4.addChild(bone21);
        bone21.cubeList.add(new ModelBox(bone21, 4, 4, -0.5F, 2.7979F, 5.5781F, 1, 1, 2, 0.0F, false));

        bone22 = new ModelRenderer(this);
        bone22.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone22, -0.7854F, 0.0F, 0.0F);
        section4.addChild(bone22);
        bone22.cubeList.add(new ModelBox(bone22, 4, 4, -0.5F, 1.2247F, 4.371F, 1, 1, 2, 0.0F, false));
        bone22.cubeList.add(new ModelBox(bone22, 0, 31, -0.5F, -4.7753F, 4.871F, 1, 6, 1, 0.0F, false));

        bone23 = new ModelRenderer(this);
        bone23.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone23, -1.0472F, 0.0F, 0.0F);
        section4.addChild(bone23);
        bone23.cubeList.add(new ModelBox(bone23, 4, 4, -0.5F, 0.0176F, 2.7979F, 1, 1, 2, 0.0F, false));

        bone24 = new ModelRenderer(this);
        bone24.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone24, -1.309F, 0.0F, 0.0F);
        section4.addChild(bone24);
        bone24.cubeList.add(new ModelBox(bone24, 4, 4, -0.5F, -0.7412F, 0.9659F, 1, 1, 2, 0.0F, false));

        wheelLeft = new ModelRenderer(this);
        wheelLeft.setRotationPoint(9.0F, 7.6F, 0.0F);
        wheel.addChild(wheelLeft);

        section5 = new ModelRenderer(this);
        section5.setRotationPoint(0.0F, 0.0F, 0.0F);
        wheelLeft.addChild(section5);

        bone25 = new ModelRenderer(this);
        bone25.setRotationPoint(0.0F, 0.0F, 0.0F);
        section5.addChild(bone25);
        bone25.cubeList.add(new ModelBox(bone25, 4, 4, -0.5F, -1.0F, -1.0F, 1, 1, 2, 0.0F, false));
        bone25.cubeList.add(new ModelBox(bone25, 0, 31, -0.5F, -7.0F, -0.5F, 1, 6, 1, 0.0F, false));

        bone26 = new ModelRenderer(this);
        bone26.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone26, -0.2618F, 0.0F, 0.0F);
        section5.addChild(bone26);
        bone26.cubeList.add(new ModelBox(bone26, 4, 4, -0.5F, -0.7412F, -2.9659F, 1, 1, 2, 0.0F, false));

        bone27 = new ModelRenderer(this);
        bone27.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone27, -0.5236F, 0.0F, 0.0F);
        section5.addChild(bone27);
        bone27.cubeList.add(new ModelBox(bone27, 4, 4, -0.5F, 0.0176F, -4.7979F, 1, 1, 2, 0.0F, false));

        bone28 = new ModelRenderer(this);
        bone28.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone28, -0.7854F, 0.0F, 0.0F);
        section5.addChild(bone28);
        bone28.cubeList.add(new ModelBox(bone28, 4, 4, -0.5F, 1.2247F, -6.371F, 1, 1, 2, 0.0F, false));
        bone28.cubeList.add(new ModelBox(bone28, 0, 31, -0.5F, -4.7753F, -5.871F, 1, 6, 1, 0.0F, false));

        bone29 = new ModelRenderer(this);
        bone29.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone29, -1.0472F, 0.0F, 0.0F);
        section5.addChild(bone29);
        bone29.cubeList.add(new ModelBox(bone29, 4, 4, -0.5F, 2.7979F, -7.5781F, 1, 1, 2, 0.0F, false));

        bone30 = new ModelRenderer(this);
        bone30.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone30, -1.309F, 0.0F, 0.0F);
        section5.addChild(bone30);
        bone30.cubeList.add(new ModelBox(bone30, 4, 4, -0.5F, 4.6298F, -8.3369F, 1, 1, 2, 0.0F, false));

        section6 = new ModelRenderer(this);
        section6.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(section6, -1.5708F, 0.0F, 0.0F);
        wheelLeft.addChild(section6);

        bone31 = new ModelRenderer(this);
        bone31.setRotationPoint(0.0F, 0.0F, 0.0F);
        section6.addChild(bone31);
        bone31.cubeList.add(new ModelBox(bone31, 4, 4, -0.5F, 6.5957F, -8.5958F, 1, 1, 2, 0.0F, false));
        bone31.cubeList.add(new ModelBox(bone31, 0, 31, -0.5F, 0.5957F, -8.0958F, 1, 6, 1, 0.0F, false));

        bone32 = new ModelRenderer(this);
        bone32.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone32, -0.2618F, 0.0F, 0.0F);
        section6.addChild(bone32);
        bone32.cubeList.add(new ModelBox(bone32, 4, 4, -0.5F, 8.5617F, -8.3369F, 1, 1, 2, 0.0F, false));

        bone34 = new ModelRenderer(this);
        bone34.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone34, -0.5236F, 0.0F, 0.0F);
        section6.addChild(bone34);
        bone34.cubeList.add(new ModelBox(bone34, 4, 4, -0.5F, 10.3936F, -7.5781F, 1, 1, 2, 0.0F, false));

        bone37 = new ModelRenderer(this);
        bone37.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone37, -0.7854F, 0.0F, 0.0F);
        section6.addChild(bone37);
        bone37.cubeList.add(new ModelBox(bone37, 4, 4, -0.5F, 11.9668F, -6.371F, 1, 1, 2, 0.0F, false));
        bone37.cubeList.add(new ModelBox(bone37, 0, 31, -0.5F, 5.9668F, -5.871F, 1, 6, 1, 0.0F, false));

        bone38 = new ModelRenderer(this);
        bone38.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone38, -1.0472F, 0.0F, 0.0F);
        section6.addChild(bone38);
        bone38.cubeList.add(new ModelBox(bone38, 4, 4, -0.5F, 13.1739F, -4.7979F, 1, 1, 2, 0.0F, false));

        bone39 = new ModelRenderer(this);
        bone39.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone39, -1.309F, 0.0F, 0.0F);
        section6.addChild(bone39);
        bone39.cubeList.add(new ModelBox(bone39, 4, 4, -0.5F, 13.9327F, -2.9659F, 1, 1, 2, 0.0F, false));

        section7 = new ModelRenderer(this);
        section7.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(section7, 3.1416F, 0.0F, 0.0F);
        wheelLeft.addChild(section7);

        bone40 = new ModelRenderer(this);
        bone40.setRotationPoint(0.0F, 0.0F, 0.0F);
        section7.addChild(bone40);
        bone40.cubeList.add(new ModelBox(bone40, 4, 4, -0.5F, 14.1915F, -1.0F, 1, 1, 2, 0.0F, false));
        bone40.cubeList.add(new ModelBox(bone40, 0, 31, -0.5F, 8.1915F, -0.5F, 1, 6, 1, 0.0F, false));

        bone41 = new ModelRenderer(this);
        bone41.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone41, -0.2618F, 0.0F, 0.0F);
        section7.addChild(bone41);
        bone41.cubeList.add(new ModelBox(bone41, 4, 4, -0.5F, 13.9327F, 0.9659F, 1, 1, 2, 0.0F, false));

        bone42 = new ModelRenderer(this);
        bone42.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone42, -0.5236F, 0.0F, 0.0F);
        section7.addChild(bone42);
        bone42.cubeList.add(new ModelBox(bone42, 4, 4, -0.5F, 13.1739F, 2.7979F, 1, 1, 2, 0.0F, false));

        bone43 = new ModelRenderer(this);
        bone43.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone43, -0.7854F, 0.0F, 0.0F);
        section7.addChild(bone43);
        bone43.cubeList.add(new ModelBox(bone43, 4, 4, -0.5F, 11.9668F, 4.371F, 1, 1, 2, 0.0F, false));
        bone43.cubeList.add(new ModelBox(bone43, 0, 31, -0.5F, 5.9668F, 4.871F, 1, 6, 1, 0.0F, false));

        bone44 = new ModelRenderer(this);
        bone44.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone44, -1.0472F, 0.0F, 0.0F);
        section7.addChild(bone44);
        bone44.cubeList.add(new ModelBox(bone44, 4, 4, -0.5F, 10.3936F, 5.5781F, 1, 1, 2, 0.0F, false));

        bone45 = new ModelRenderer(this);
        bone45.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone45, -1.309F, 0.0F, 0.0F);
        section7.addChild(bone45);
        bone45.cubeList.add(new ModelBox(bone45, 4, 4, -0.5F, 8.5617F, 6.3369F, 1, 1, 2, 0.0F, false));

        section8 = new ModelRenderer(this);
        section8.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(section8, 1.5708F, 0.0F, 0.0F);
        wheelLeft.addChild(section8);

        bone46 = new ModelRenderer(this);
        bone46.setRotationPoint(0.0F, 0.0F, 0.0F);
        section8.addChild(bone46);
        bone46.cubeList.add(new ModelBox(bone46, 4, 4, -0.5F, 6.5957F, 6.5957F, 1, 1, 2, 0.0F, false));
        bone46.cubeList.add(new ModelBox(bone46, 0, 31, -0.5F, 0.5957F, 7.0957F, 1, 6, 1, 0.0F, false));

        bone47 = new ModelRenderer(this);
        bone47.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone47, -0.2618F, 0.0F, 0.0F);
        section8.addChild(bone47);
        bone47.cubeList.add(new ModelBox(bone47, 4, 4, -0.5F, 4.6298F, 6.3369F, 1, 1, 2, 0.0F, false));

        bone48 = new ModelRenderer(this);
        bone48.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone48, -0.5236F, 0.0F, 0.0F);
        section8.addChild(bone48);
        bone48.cubeList.add(new ModelBox(bone48, 4, 4, -0.5F, 2.7979F, 5.5781F, 1, 1, 2, 0.0F, false));

        bone49 = new ModelRenderer(this);
        bone49.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone49, -0.7854F, 0.0F, 0.0F);
        section8.addChild(bone49);
        bone49.cubeList.add(new ModelBox(bone49, 4, 4, -0.5F, 1.2247F, 4.371F, 1, 1, 2, 0.0F, false));
        bone49.cubeList.add(new ModelBox(bone49, 0, 31, -0.5F, -4.7753F, 4.871F, 1, 6, 1, 0.0F, false));

        bone50 = new ModelRenderer(this);
        bone50.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone50, -1.0472F, 0.0F, 0.0F);
        section8.addChild(bone50);
        bone50.cubeList.add(new ModelBox(bone50, 4, 4, -0.5F, 0.0176F, 2.7979F, 1, 1, 2, 0.0F, false));

        bone51 = new ModelRenderer(this);
        bone51.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone51, -1.309F, 0.0F, 0.0F);
        section8.addChild(bone51);
        bone51.cubeList.add(new ModelBox(bone51, 4, 4, -0.5F, -0.7412F, 0.9659F, 1, 1, 2, 0.0F, false));

        body = new ModelRenderer(this);
        body.setRotationPoint(0.0F, -7.6F, 0.0F);
        main.addChild(body);

        upper = new ModelRenderer(this);
        upper.setRotationPoint(0.0F, 4.6F, -1.0F);
        body.addChild(upper);

        awning = new ModelRenderer(this);
        awning.setRotationPoint(-8.0F, -17.0F, -7.0F);
        upper.addChild(awning);

        triangle = new ModelRenderer(this);
        triangle.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(triangle, -1.2566F, 0.0F, 0.0F);
        awning.addChild(triangle);

        bone35 = new ModelRenderer(this);
        bone35.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone35, -0.3142F, 0.0F, 0.0F);
        triangle.addChild(bone35);
        bone35.cubeList.add(new ModelBox(bone35, 20, 4, -1.0F, -16.0F, -0.5F, 1, 16, 1, 0.0F, false));
        bone35.cubeList.add(new ModelBox(bone35, 32, 23, 0.0F, -16.0F, -0.5F, 16, 1, 1, 0.0F, false));

        bone33 = new ModelRenderer(this);
        bone33.setRotationPoint(0.0F, 0.0F, 0.0F);
        triangle.addChild(bone33);
        bone33.cubeList.add(new ModelBox(bone33, 0, 0, -0.5F, -15.0F, -4.5F, 0, 15, 9, 0.0F, false));
        bone33.cubeList.add(new ModelBox(bone33, 23, 0, -0.5F, -15.0F, -4.5F, 17, 0, 9, 0.0F, false));

        triangle2 = new ModelRenderer(this);
        triangle2.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(triangle2, -0.6283F, 0.0F, 0.0F);
        awning.addChild(triangle2);

        bone36 = new ModelRenderer(this);
        bone36.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone36, -0.3142F, 0.0F, 0.0F);
        triangle2.addChild(bone36);
        bone36.cubeList.add(new ModelBox(bone36, 20, 4, -1.0F, -16.0F, -0.5F, 1, 16, 1, 0.0F, false));
        bone36.cubeList.add(new ModelBox(bone36, 32, 23, 0.0F, -16.0F, -0.5F, 16, 1, 1, 0.0F, false));

        bone52 = new ModelRenderer(this);
        bone52.setRotationPoint(0.0F, 0.0F, 0.0F);
        triangle2.addChild(bone52);
        bone52.cubeList.add(new ModelBox(bone52, 0, 0, -0.5F, -15.0F, -4.5F, 0, 15, 9, 0.0F, false));
        bone52.cubeList.add(new ModelBox(bone52, 23, 0, -0.5F, -15.0F, -4.5F, 17, 0, 9, 0.0F, false));

        triangle3 = new ModelRenderer(this);
        triangle3.setRotationPoint(0.0F, 0.0F, 0.0F);
        awning.addChild(triangle3);

        bone53 = new ModelRenderer(this);
        bone53.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone53, -0.3142F, 0.0F, 0.0F);
        triangle3.addChild(bone53);
        bone53.cubeList.add(new ModelBox(bone53, 20, 4, -1.0F, -16.0F, -0.5F, 1, 16, 1, 0.0F, false));
        bone53.cubeList.add(new ModelBox(bone53, 32, 23, 0.0F, -16.0F, -0.5F, 16, 1, 1, 0.0F, false));

        bone55 = new ModelRenderer(this);
        bone55.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone55, 0.3142F, 0.0F, 0.0F);
        triangle3.addChild(bone55);
        bone55.cubeList.add(new ModelBox(bone55, 20, 4, -1.0F, -16.0F, -0.5F, 1, 16, 1, 0.0F, false));
        bone55.cubeList.add(new ModelBox(bone55, 32, 23, 0.0F, -16.0F, -0.5F, 16, 1, 1, 0.0F, false));

        bone54 = new ModelRenderer(this);
        bone54.setRotationPoint(0.0F, 0.0F, 0.0F);
        triangle3.addChild(bone54);
        bone54.cubeList.add(new ModelBox(bone54, 0, 0, -0.5F, -15.0F, -4.5F, 0, 15, 9, 0.0F, false));
        bone54.cubeList.add(new ModelBox(bone54, 23, 0, -0.501F, -15.0F, -4.5F, 17, 0, 9, 0.0F, false));

        awning2 = new ModelRenderer(this);
        awning2.setRotationPoint(9.0F, -17.0F, -7.0F);
        upper.addChild(awning2);

        triangle4 = new ModelRenderer(this);
        triangle4.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(triangle4, -1.2566F, 0.0F, 0.0F);
        awning2.addChild(triangle4);

        bone56 = new ModelRenderer(this);
        bone56.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone56, -0.3142F, 0.0F, 0.0F);
        triangle4.addChild(bone56);
        bone56.cubeList.add(new ModelBox(bone56, 20, 4, -1.0F, -16.0F, -0.5F, 1, 16, 1, 0.0F, false));

        bone57 = new ModelRenderer(this);
        bone57.setRotationPoint(0.0F, 0.0F, 0.0F);
        triangle4.addChild(bone57);
        bone57.cubeList.add(new ModelBox(bone57, 0, 0, -0.5F, -15.0F, -4.5F, 0, 15, 9, 0.0F, false));

        triangle5 = new ModelRenderer(this);
        triangle5.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(triangle5, -0.6283F, 0.0F, 0.0F);
        awning2.addChild(triangle5);

        bone58 = new ModelRenderer(this);
        bone58.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone58, -0.3142F, 0.0F, 0.0F);
        triangle5.addChild(bone58);
        bone58.cubeList.add(new ModelBox(bone58, 20, 4, -1.0F, -16.0F, -0.5F, 1, 16, 1, 0.0F, false));

        bone59 = new ModelRenderer(this);
        bone59.setRotationPoint(0.0F, 0.0F, 0.0F);
        triangle5.addChild(bone59);
        bone59.cubeList.add(new ModelBox(bone59, 0, 0, -0.5F, -15.0F, -4.5F, 0, 15, 9, 0.0F, false));

        triangle6 = new ModelRenderer(this);
        triangle6.setRotationPoint(0.0F, 0.0F, 0.0F);
        awning2.addChild(triangle6);

        bone60 = new ModelRenderer(this);
        bone60.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone60, -0.3142F, 0.0F, 0.0F);
        triangle6.addChild(bone60);
        bone60.cubeList.add(new ModelBox(bone60, 20, 4, -1.0F, -16.0F, -0.5F, 1, 16, 1, 0.0F, false));

        bone61 = new ModelRenderer(this);
        bone61.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone61, 0.3142F, 0.0F, 0.0F);
        triangle6.addChild(bone61);
        bone61.cubeList.add(new ModelBox(bone61, 20, 4, -1.0F, -16.0F, -0.5F, 1, 16, 1, 0.0F, false));

        bone62 = new ModelRenderer(this);
        bone62.setRotationPoint(0.0F, 0.0F, 0.0F);
        triangle6.addChild(bone62);
        bone62.cubeList.add(new ModelBox(bone62, 0, 0, -0.5F, -15.0F, -4.5F, 0, 15, 9, 0.0F, false));

        bone63 = new ModelRenderer(this);
        bone63.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone63, 0.7854F, 0.0F, 0.0F);
        upper.addChild(bone63);
        bone63.cubeList.add(new ModelBox(bone63, 24, 4, -9.0F, -17.1976F, 6.6256F, 1, 7, 1, 0.0F, false));
        bone63.cubeList.add(new ModelBox(bone63, 24, 4, 8.0F, -17.1976F, 6.6256F, 1, 7, 1, 0.0F, false));

        cart = new ModelRenderer(this);
        cart.setRotationPoint(0.0F, 9.6F, 0.0F);
        body.addChild(cart);
        cart.cubeList.add(new ModelBox(cart, 32, 9, -7.0F, -15.0F, -4.0F, 14, 2, 10, 0.0F, false));
        cart.cubeList.add(new ModelBox(cart, 8, 14, -8.0F, -18.0F, -4.0F, 1, 5, 10, 0.0F, true));
        cart.cubeList.add(new ModelBox(cart, 32, 45, -8.0F, -13.0F, -7.0F, 1, 7, 8, 0.0F, true));
        cart.cubeList.add(new ModelBox(cart, 32, 45, 7.0F, -13.0F, -7.0F, 1, 7, 8, 0.0F, false));
        cart.cubeList.add(new ModelBox(cart, 0, 24, -8.0F, -11.0F, -8.0F, 1, 5, 1, 0.0F, true));
        cart.cubeList.add(new ModelBox(cart, 0, 24, 7.0F, -11.0F, -8.0F, 1, 5, 1, 0.0F, false));
        cart.cubeList.add(new ModelBox(cart, 0, 4, -8.0F, -10.0F, -9.0F, 1, 4, 1, 0.0F, true));
        cart.cubeList.add(new ModelBox(cart, 0, 4, 7.0F, -10.0F, -9.0F, 1, 4, 1, 0.0F, false));
        cart.cubeList.add(new ModelBox(cart, 26, 12, -8.5F, -8.0F, -13.0F, 1, 6, 1, 0.0F, true));
        cart.cubeList.add(new ModelBox(cart, 26, 12, 7.5F, -8.0F, -13.0F, 1, 6, 1, 0.0F, false));
        cart.cubeList.add(new ModelBox(cart, 0, 0, -8.5F, -8.0F, -43.0F, 1, 1, 30, 0.0F, false));
        cart.cubeList.add(new ModelBox(cart, 0, 0, 7.5F, -8.0F, -43.0F, 1, 1, 30, 0.0F, false));
        cart.cubeList.add(new ModelBox(cart, 20, 17, -8.0F, -9.0F, -13.0F, 1, 3, 4, 0.0F, true));
        cart.cubeList.add(new ModelBox(cart, 20, 17, 7.0F, -9.0F, -13.0F, 1, 3, 4, 0.0F, false));
        cart.cubeList.add(new ModelBox(cart, 0, 0, -7.0F, -9.0F, -13.0F, 14, 3, 1, 0.0F, false));
        cart.cubeList.add(new ModelBox(cart, 32, 25, -7.5F, -8.0F, -40.0F, 15, 1, 1, 0.0F, false));
        cart.cubeList.add(new ModelBox(cart, 8, 14, 7.0F, -18.0F, -4.0F, 1, 5, 10, 0.0F, false));
        cart.cubeList.add(new ModelBox(cart, 0, 45, -7.0F, -23.0F, 4.0F, 14, 8, 2, 0.0F, false));
        cart.cubeList.add(new ModelBox(cart, 0, 31, -7.0F, -7.0F, -12.0F, 14, 1, 13, 0.0F, false));
        cart.cubeList.add(new ModelBox(cart, 41, 31, -7.0F, -13.0F, -4.0F, 14, 6, 5, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        main.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        if (entityIn.getRidingEntity() instanceof AbstractEntityMaid) {
            AbstractEntityMaid maid = (AbstractEntityMaid) entityIn.getRidingEntity();
            body.rotateAngleX = -15 * 0.01745329251f;
            wheel.rotateAngleX = maid.limbSwing;
        } else {
            body.rotateAngleX = 0;
        }
        main.rotateAngleY = -netHeadYaw * 0.01745329251f / 8;
    }
}
