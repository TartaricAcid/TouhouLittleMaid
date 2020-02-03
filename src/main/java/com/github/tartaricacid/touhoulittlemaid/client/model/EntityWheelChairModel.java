package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * @author TartaricAcid
 * @date 2020/2/3 16:50
 **/
public class EntityWheelChairModel extends ModelBase {
    private final ModelRenderer whells;
    private final ModelRenderer whellFrontLeft;
    private final ModelRenderer bone7;
    private final ModelRenderer bone8;
    private final ModelRenderer bone9;
    private final ModelRenderer bone;
    private final ModelRenderer bone2;
    private final ModelRenderer bone3;
    private final ModelRenderer whellFrontRight;
    private final ModelRenderer bone23;
    private final ModelRenderer bone24;
    private final ModelRenderer bone25;
    private final ModelRenderer bone26;
    private final ModelRenderer bone27;
    private final ModelRenderer bone28;
    private final ModelRenderer whellRearLeft;
    private final ModelRenderer bone4;
    private final ModelRenderer bone5;
    private final ModelRenderer bone6;
    private final ModelRenderer bone10;
    private final ModelRenderer bone11;
    private final ModelRenderer bone12;
    private final ModelRenderer bone13;
    private final ModelRenderer bone14;
    private final ModelRenderer whellRearRight;
    private final ModelRenderer bone15;
    private final ModelRenderer bone16;
    private final ModelRenderer bone17;
    private final ModelRenderer bone18;
    private final ModelRenderer bone19;
    private final ModelRenderer bone20;
    private final ModelRenderer bone21;
    private final ModelRenderer bone22;
    private final ModelRenderer frames;
    private final ModelRenderer tilt;
    private final ModelRenderer bone30;

    public EntityWheelChairModel() {
        textureWidth = 64;
        textureHeight = 64;

        whells = new ModelRenderer(this);
        whells.setRotationPoint(0.0F, 24.0F, 0.0F);

        whellFrontLeft = new ModelRenderer(this);
        whellFrontLeft.setRotationPoint(7.0F, -1.9F, -6.0F);
        whells.addChild(whellFrontLeft);

        bone7 = new ModelRenderer(this);
        bone7.setRotationPoint(0.0F, 0.034F, 0.0F);
        whellFrontLeft.addChild(bone7);
        bone7.cubeList.add(new ModelBox(bone7, 29, 24, -0.501F, -1.0F, -0.25F, 1, 1, 0, 0.0F, false));

        bone8 = new ModelRenderer(this);
        bone8.setRotationPoint(0.0F, 0.034F, 0.0F);
        setRotationAngle(bone8, -2.0944F, 0.0F, 0.0F);
        whellFrontLeft.addChild(bone8);
        bone8.cubeList.add(new ModelBox(bone8, 29, 24, -0.501F, -1.0F, -0.25F, 1, 1, 0, 0.0F, false));

        bone9 = new ModelRenderer(this);
        bone9.setRotationPoint(0.0F, 0.034F, 0.0F);
        setRotationAngle(bone9, 2.0944F, 0.0F, 0.0F);
        whellFrontLeft.addChild(bone9);
        bone9.cubeList.add(new ModelBox(bone9, 29, 24, -0.501F, -1.0F, -0.25F, 1, 1, 0, 0.0F, false));

        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, 1.9F, 0.0F);
        whellFrontLeft.addChild(bone);
        bone.cubeList.add(new ModelBox(bone, 28, 42, -0.5F, -1.0F, -0.5F, 1, 1, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 12, 42, -0.5F, -3.7321F, -0.5F, 1, 1, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 42, -0.5F, -2.366F, -1.866F, 1, 1, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 41, 38, -0.5F, -2.366F, 0.866F, 1, 1, 1, 0.0F, false));

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(0.0F, 1.9F, 0.0F);
        setRotationAngle(bone2, -0.5236F, 0.0F, 0.0F);
        whellFrontLeft.addChild(bone2);
        bone2.cubeList.add(new ModelBox(bone2, 24, 42, -0.5F, -0.75F, -1.433F, 1, 1, 1, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 41, 35, -0.5F, -2.116F, -2.799F, 1, 1, 1, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 8, 42, -0.5F, -3.4821F, -1.433F, 1, 1, 1, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 41, 15, -0.5F, -2.116F, -0.067F, 1, 1, 1, 0.0F, false));

        bone3 = new ModelRenderer(this);
        bone3.setRotationPoint(0.0F, 1.9F, 0.0F);
        setRotationAngle(bone3, -1.0472F, 0.0F, 0.0F);
        whellFrontLeft.addChild(bone3);
        bone3.cubeList.add(new ModelBox(bone3, 18, 42, -0.5F, -0.067F, -2.116F, 1, 1, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 41, 13, -0.5F, -1.433F, -3.482F, 1, 1, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 4, 42, -0.5F, -2.799F, -2.116F, 1, 1, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 40, 41, -0.5F, -1.433F, -0.75F, 1, 1, 1, 0.0F, false));

        whellFrontRight = new ModelRenderer(this);
        whellFrontRight.setRotationPoint(-7.0F, -1.9F, -6.0F);
        whells.addChild(whellFrontRight);

        bone23 = new ModelRenderer(this);
        bone23.setRotationPoint(0.0F, 0.034F, 0.0F);
        whellFrontRight.addChild(bone23);
        bone23.cubeList.add(new ModelBox(bone23, 29, 24, -0.499F, -1.0F, -0.25F, 1, 1, 0, 0.0F, false));

        bone24 = new ModelRenderer(this);
        bone24.setRotationPoint(0.0F, 0.034F, 0.0F);
        setRotationAngle(bone24, -2.0944F, 0.0F, 0.0F);
        whellFrontRight.addChild(bone24);
        bone24.cubeList.add(new ModelBox(bone24, 29, 24, -0.499F, -1.0F, -0.25F, 1, 1, 0, 0.0F, false));

        bone25 = new ModelRenderer(this);
        bone25.setRotationPoint(0.0F, 0.034F, 0.0F);
        setRotationAngle(bone25, 2.0944F, 0.0F, 0.0F);
        whellFrontRight.addChild(bone25);
        bone25.cubeList.add(new ModelBox(bone25, 29, 24, -0.499F, -1.0F, -0.25F, 1, 1, 0, 0.0F, false));

        bone26 = new ModelRenderer(this);
        bone26.setRotationPoint(0.0F, 1.9F, 0.0F);
        whellFrontRight.addChild(bone26);
        bone26.cubeList.add(new ModelBox(bone26, 28, 42, -0.5F, -1.0F, -0.5F, 1, 1, 1, 0.0F, false));
        bone26.cubeList.add(new ModelBox(bone26, 12, 42, -0.5F, -3.7321F, -0.5F, 1, 1, 1, 0.0F, false));
        bone26.cubeList.add(new ModelBox(bone26, 0, 42, -0.5F, -2.366F, -1.866F, 1, 1, 1, 0.0F, false));
        bone26.cubeList.add(new ModelBox(bone26, 41, 38, -0.5F, -2.366F, 0.866F, 1, 1, 1, 0.0F, false));

        bone27 = new ModelRenderer(this);
        bone27.setRotationPoint(0.0F, 1.9F, 0.0F);
        setRotationAngle(bone27, -0.5236F, 0.0F, 0.0F);
        whellFrontRight.addChild(bone27);
        bone27.cubeList.add(new ModelBox(bone27, 24, 42, -0.5F, -0.75F, -1.433F, 1, 1, 1, 0.0F, false));
        bone27.cubeList.add(new ModelBox(bone27, 41, 35, -0.5F, -2.116F, -2.799F, 1, 1, 1, 0.0F, false));
        bone27.cubeList.add(new ModelBox(bone27, 8, 42, -0.5F, -3.4821F, -1.433F, 1, 1, 1, 0.0F, false));
        bone27.cubeList.add(new ModelBox(bone27, 41, 15, -0.5F, -2.116F, -0.067F, 1, 1, 1, 0.0F, false));

        bone28 = new ModelRenderer(this);
        bone28.setRotationPoint(0.0F, 1.9F, 0.0F);
        setRotationAngle(bone28, -1.0472F, 0.0F, 0.0F);
        whellFrontRight.addChild(bone28);
        bone28.cubeList.add(new ModelBox(bone28, 18, 42, -0.5F, -0.067F, -2.116F, 1, 1, 1, 0.0F, false));
        bone28.cubeList.add(new ModelBox(bone28, 41, 13, -0.5F, -1.433F, -3.482F, 1, 1, 1, 0.0F, false));
        bone28.cubeList.add(new ModelBox(bone28, 4, 42, -0.5F, -2.799F, -2.116F, 1, 1, 1, 0.0F, false));
        bone28.cubeList.add(new ModelBox(bone28, 40, 41, -0.5F, -1.433F, -0.75F, 1, 1, 1, 0.0F, false));

        whellRearLeft = new ModelRenderer(this);
        whellRearLeft.setRotationPoint(7.0F, -3.8F, 4.0F);
        whells.addChild(whellRearLeft);

        bone4 = new ModelRenderer(this);
        bone4.setRotationPoint(0.0F, 3.8F, 0.0F);
        whellRearLeft.addChild(bone4);
        bone4.cubeList.add(new ModelBox(bone4, 32, 37, -0.5F, -1.0F, -1.0F, 1, 1, 2, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 22, 37, -0.5F, -7.4641F, -1.0F, 1, 1, 2, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 21, 40, -0.5F, -4.7321F, -3.7321F, 1, 2, 1, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 39, 27, -0.5F, -4.7321F, 2.7321F, 1, 2, 1, 0.0F, false));

        bone5 = new ModelRenderer(this);
        bone5.setRotationPoint(0.0F, 3.8F, 0.0F);
        setRotationAngle(bone5, -0.5236F, 0.0F, 0.0F);
        whellRearLeft.addChild(bone5);
        bone5.cubeList.add(new ModelBox(bone5, 36, 4, -0.5F, -0.5F, -2.866F, 1, 1, 2, 0.0F, false));
        bone5.cubeList.add(new ModelBox(bone5, 39, 24, -0.5F, -4.2321F, -5.5981F, 1, 2, 1, 0.0F, false));
        bone5.cubeList.add(new ModelBox(bone5, 12, 36, -0.5F, -6.9641F, -2.866F, 1, 1, 2, 0.0F, false));
        bone5.cubeList.add(new ModelBox(bone5, 39, 7, -0.5F, -4.2321F, 0.866F, 1, 2, 1, 0.0F, false));

        bone6 = new ModelRenderer(this);
        bone6.setRotationPoint(0.0F, 3.8F, 0.0F);
        setRotationAngle(bone6, -1.0472F, 0.0F, 0.0F);
        whellRearLeft.addChild(bone6);
        bone6.cubeList.add(new ModelBox(bone6, 6, 36, -0.5F, 0.866F, -4.2321F, 1, 1, 2, 0.0F, false));
        bone6.cubeList.add(new ModelBox(bone6, 37, 39, -0.5F, -2.866F, -6.9641F, 1, 2, 1, 0.0F, false));
        bone6.cubeList.add(new ModelBox(bone6, 0, 36, -0.5F, -5.5981F, -4.2321F, 1, 1, 2, 0.0F, false));
        bone6.cubeList.add(new ModelBox(bone6, 27, 39, -0.5F, -2.866F, -0.5F, 1, 2, 1, 0.0F, false));

        bone10 = new ModelRenderer(this);
        bone10.setRotationPoint(0.0F, 0.0679F, 0.0F);
        whellRearLeft.addChild(bone10);
        bone10.cubeList.add(new ModelBox(bone10, 28, 22, -0.501F, -3.0F, -0.5F, 1, 3, 1, 0.0F, false));

        bone11 = new ModelRenderer(this);
        bone11.setRotationPoint(0.0F, 0.0679F, 0.0F);
        setRotationAngle(bone11, -1.2566F, 0.0F, 0.0F);
        whellRearLeft.addChild(bone11);
        bone11.cubeList.add(new ModelBox(bone11, 28, 22, -0.501F, -3.0F, -0.5F, 1, 3, 1, 0.0F, false));

        bone12 = new ModelRenderer(this);
        bone12.setRotationPoint(0.0F, 0.0679F, 0.0F);
        setRotationAngle(bone12, -2.5133F, 0.0F, 0.0F);
        whellRearLeft.addChild(bone12);
        bone12.cubeList.add(new ModelBox(bone12, 28, 22, -0.501F, -3.0F, -0.5F, 1, 3, 1, 0.0F, false));

        bone13 = new ModelRenderer(this);
        bone13.setRotationPoint(0.0F, 0.0679F, 0.0F);
        setRotationAngle(bone13, 2.5133F, 0.0F, 0.0F);
        whellRearLeft.addChild(bone13);
        bone13.cubeList.add(new ModelBox(bone13, 28, 22, -0.501F, -3.0F, -0.5F, 1, 3, 1, 0.0F, false));

        bone14 = new ModelRenderer(this);
        bone14.setRotationPoint(0.0F, 0.0679F, 0.0F);
        setRotationAngle(bone14, 1.2566F, 0.0F, 0.0F);
        whellRearLeft.addChild(bone14);
        bone14.cubeList.add(new ModelBox(bone14, 28, 22, -0.501F, -3.0F, -0.5F, 1, 3, 1, 0.0F, false));

        whellRearRight = new ModelRenderer(this);
        whellRearRight.setRotationPoint(-7.0F, -3.8F, 4.0F);
        whells.addChild(whellRearRight);

        bone15 = new ModelRenderer(this);
        bone15.setRotationPoint(0.0F, 3.8F, 0.0F);
        whellRearRight.addChild(bone15);
        bone15.cubeList.add(new ModelBox(bone15, 32, 37, -0.5F, -1.0F, -1.0F, 1, 1, 2, 0.0F, false));
        bone15.cubeList.add(new ModelBox(bone15, 22, 37, -0.5F, -7.4641F, -1.0F, 1, 1, 2, 0.0F, false));
        bone15.cubeList.add(new ModelBox(bone15, 21, 40, -0.5F, -4.7321F, -3.7321F, 1, 2, 1, 0.0F, false));
        bone15.cubeList.add(new ModelBox(bone15, 39, 27, -0.5F, -4.7321F, 2.7321F, 1, 2, 1, 0.0F, false));

        bone16 = new ModelRenderer(this);
        bone16.setRotationPoint(0.0F, 3.8F, 0.0F);
        setRotationAngle(bone16, -0.5236F, 0.0F, 0.0F);
        whellRearRight.addChild(bone16);
        bone16.cubeList.add(new ModelBox(bone16, 36, 4, -0.5F, -0.5F, -2.866F, 1, 1, 2, 0.0F, false));
        bone16.cubeList.add(new ModelBox(bone16, 39, 24, -0.5F, -4.2321F, -5.5981F, 1, 2, 1, 0.0F, false));
        bone16.cubeList.add(new ModelBox(bone16, 12, 36, -0.5F, -6.9641F, -2.866F, 1, 1, 2, 0.0F, false));
        bone16.cubeList.add(new ModelBox(bone16, 39, 7, -0.5F, -4.2321F, 0.866F, 1, 2, 1, 0.0F, false));

        bone17 = new ModelRenderer(this);
        bone17.setRotationPoint(0.0F, 3.8F, 0.0F);
        setRotationAngle(bone17, -1.0472F, 0.0F, 0.0F);
        whellRearRight.addChild(bone17);
        bone17.cubeList.add(new ModelBox(bone17, 6, 36, -0.5F, 0.866F, -4.2321F, 1, 1, 2, 0.0F, false));
        bone17.cubeList.add(new ModelBox(bone17, 37, 39, -0.5F, -2.866F, -6.9641F, 1, 2, 1, 0.0F, false));
        bone17.cubeList.add(new ModelBox(bone17, 0, 36, -0.5F, -5.5981F, -4.2321F, 1, 1, 2, 0.0F, false));
        bone17.cubeList.add(new ModelBox(bone17, 27, 39, -0.5F, -2.866F, -0.5F, 1, 2, 1, 0.0F, false));

        bone18 = new ModelRenderer(this);
        bone18.setRotationPoint(0.0F, 0.0679F, 0.0F);
        whellRearRight.addChild(bone18);
        bone18.cubeList.add(new ModelBox(bone18, 28, 22, -0.499F, -3.0F, -0.5F, 1, 3, 1, 0.0F, false));

        bone19 = new ModelRenderer(this);
        bone19.setRotationPoint(0.0F, 0.0679F, 0.0F);
        setRotationAngle(bone19, -1.2566F, 0.0F, 0.0F);
        whellRearRight.addChild(bone19);
        bone19.cubeList.add(new ModelBox(bone19, 28, 22, -0.499F, -3.0F, -0.5F, 1, 3, 1, 0.0F, false));

        bone20 = new ModelRenderer(this);
        bone20.setRotationPoint(0.0F, 0.0679F, 0.0F);
        setRotationAngle(bone20, -2.5133F, 0.0F, 0.0F);
        whellRearRight.addChild(bone20);
        bone20.cubeList.add(new ModelBox(bone20, 28, 22, -0.499F, -3.0F, -0.5F, 1, 3, 1, 0.0F, false));

        bone21 = new ModelRenderer(this);
        bone21.setRotationPoint(0.0F, 0.0679F, 0.0F);
        setRotationAngle(bone21, 2.5133F, 0.0F, 0.0F);
        whellRearRight.addChild(bone21);
        bone21.cubeList.add(new ModelBox(bone21, 28, 22, -0.499F, -3.0F, -0.5F, 1, 3, 1, 0.0F, false));

        bone22 = new ModelRenderer(this);
        bone22.setRotationPoint(0.0F, 0.0679F, 0.0F);
        setRotationAngle(bone22, 1.2566F, 0.0F, 0.0F);
        whellRearRight.addChild(bone22);
        bone22.cubeList.add(new ModelBox(bone22, 28, 22, -0.499F, -3.0F, -0.5F, 1, 3, 1, 0.0F, false));

        frames = new ModelRenderer(this);
        frames.setRotationPoint(0.0F, 24.0F, 0.0F);
        frames.cubeList.add(new ModelBox(frames, 0, 11, -8.5F, -5.232F, -6.5F, 17, 1, 1, 0.0F, false));
        frames.cubeList.add(new ModelBox(frames, 27, 0, -6.5F, -4.232F, 3.5F, 13, 1, 1, 0.0F, false));
        frames.cubeList.add(new ModelBox(frames, 22, 22, -6.5F, -5.232F, -5.5F, 1, 1, 10, 0.0F, false));
        frames.cubeList.add(new ModelBox(frames, 22, 22, 5.5F, -5.232F, -5.5F, 1, 1, 10, 0.0F, true));
        frames.cubeList.add(new ModelBox(frames, 22, 33, -6.5F, -7.232F, -7.5F, 1, 1, 3, 0.0F, false));
        frames.cubeList.add(new ModelBox(frames, 22, 33, 5.5F, -7.232F, -7.5F, 1, 1, 3, 0.0F, true));
        frames.cubeList.add(new ModelBox(frames, 0, 33, -6.5F, -3.0709F, -9.5864F, 2, 1, 1, 0.0F, false));
        frames.cubeList.add(new ModelBox(frames, 0, 33, 4.5F, -3.0709F, -9.5864F, 2, 1, 1, 0.0F, true));
        frames.cubeList.add(new ModelBox(frames, 27, 4, -4.5F, -3.0709F, -10.5864F, 3, 1, 3, 0.0F, false));
        frames.cubeList.add(new ModelBox(frames, 27, 4, 1.5F, -3.0709F, -10.5864F, 3, 1, 3, 0.0F, true));
        frames.cubeList.add(new ModelBox(frames, 22, 28, -6.5F, -9.9305F, -6.7899F, 1, 1, 3, 0.0F, false));
        frames.cubeList.add(new ModelBox(frames, 22, 28, 5.5F, -9.9305F, -6.7899F, 1, 1, 3, 0.0F, true));
        frames.cubeList.add(new ModelBox(frames, 22, 22, -8.5F, -4.232F, -6.5F, 1, 3, 1, 0.0F, false));
        frames.cubeList.add(new ModelBox(frames, 22, 22, -6.5F, -4.232F, -6.5F, 1, 3, 1, 0.0F, false));
        frames.cubeList.add(new ModelBox(frames, 22, 22, 7.5F, -4.232F, -6.5F, 1, 3, 1, 0.0F, true));
        frames.cubeList.add(new ModelBox(frames, 22, 22, 5.5F, -4.232F, -6.5F, 1, 3, 1, 0.0F, true));
        frames.cubeList.add(new ModelBox(frames, 22, 13, -3.5F, -8.232F, 2.5F, 7, 4, 5, 0.0F, false));
        frames.cubeList.add(new ModelBox(frames, 0, 0, -4.5F, -9.6706F, -3.9649F, 9, 2, 9, 0.0F, false));
        frames.cubeList.add(new ModelBox(frames, 22, 22, -5.5F, -8.6706F, -3.9649F, 1, 1, 4, 0.0F, false));
        frames.cubeList.add(new ModelBox(frames, 22, 22, 4.5F, -8.6706F, -3.9649F, 1, 1, 4, 0.0F, true));
        frames.cubeList.add(new ModelBox(frames, 22, 22, -5.5F, -8.6706F, 1.0351F, 1, 1, 4, 0.0F, false));
        frames.cubeList.add(new ModelBox(frames, 22, 22, 4.5F, -8.6706F, 1.0351F, 1, 1, 4, 0.0F, true));
        frames.cubeList.add(new ModelBox(frames, 4, 0, -5.5F, -12.6706F, 0.0351F, 1, 5, 1, 0.0F, false));
        frames.cubeList.add(new ModelBox(frames, 4, 0, 4.5F, -12.6706F, 0.0351F, 1, 5, 1, 0.0F, true));
        frames.cubeList.add(new ModelBox(frames, 0, 28, -6.5F, -13.6706F, -4.9649F, 2, 1, 7, 0.0F, false));
        frames.cubeList.add(new ModelBox(frames, 0, 28, 4.5F, -13.6706F, -4.9649F, 2, 1, 7, 0.0F, false));

        tilt = new ModelRenderer(this);
        tilt.setRotationPoint(0.5F, 24.0F, 6.5F);
        setRotationAngle(tilt, -0.3491F, 0.0F, 0.0F);
        tilt.cubeList.add(new ModelBox(tilt, 30, 33, -6.0F, -8.2927F, -4.3268F, 1, 5, 1, 0.0F, false));
        tilt.cubeList.add(new ModelBox(tilt, 30, 33, 4.0F, -8.2927F, -4.3268F, 1, 5, 1, 0.0F, true));
        tilt.cubeList.add(new ModelBox(tilt, 0, 27, -7.0F, -5.8122F, -13.0658F, 1, 5, 1, 0.0F, false));
        tilt.cubeList.add(new ModelBox(tilt, 0, 27, 5.0F, -5.8122F, -13.0658F, 1, 5, 1, 0.0F, true));
        tilt.cubeList.add(new ModelBox(tilt, 0, 0, -7.0F, -4.7862F, -15.8848F, 1, 8, 1, 0.0F, false));
        tilt.cubeList.add(new ModelBox(tilt, 0, 0, 5.0F, -4.7862F, -15.8848F, 1, 8, 1, 0.0F, true));
        tilt.cubeList.add(new ModelBox(tilt, 0, 13, -5.0F, -19.7069F, -5.0F, 9, 13, 1, 0.0F, false));
        tilt.cubeList.add(new ModelBox(tilt, 27, 2, -4.5F, -20.7069F, -5.3268F, 8, 1, 1, 0.0F, false));
        tilt.cubeList.add(new ModelBox(tilt, 18, 28, -6.0F, -19.2927F, -5.3268F, 1, 12, 1, 0.0F, false));
        tilt.cubeList.add(new ModelBox(tilt, 18, 28, 4.0F, -19.2927F, -5.3268F, 1, 12, 1, 0.0F, false));

        bone30 = new ModelRenderer(this);
        bone30.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone30, 0.0F, 0.0F, -0.7854F);
        tilt.addChild(bone30);
        bone30.cubeList.add(new ModelBox(bone30, 4, 6, 16.1776F, -12.1065F, -5.3268F, 1, 2, 1, 0.0F, false));
        bone30.cubeList.add(new ModelBox(bone30, 36, 37, 9.3994F, -17.8847F, -5.3268F, 2, 1, 1, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        whells.render(f5);
        frames.render(f5);
        tilt.render(f5);
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
            whellFrontLeft.rotateAngleX = maid.limbSwing;
            whellFrontRight.rotateAngleX = maid.limbSwing;
            whellRearLeft.rotateAngleX = maid.limbSwing;
            whellRearRight.rotateAngleX = maid.limbSwing;
        }
    }
}
