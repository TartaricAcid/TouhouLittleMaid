package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ComputerModel extends EntityModel<Entity> {
    private final ModelRenderer main;
    private final ModelRenderer group;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer cube_r3;
    private final ModelRenderer cube_r4;
    private final ModelRenderer group8;
    private final ModelRenderer cube_r5;
    private final ModelRenderer group3;
    private final ModelRenderer cube_r6;
    private final ModelRenderer bone;
    private final ModelRenderer cube_r7;
    private final ModelRenderer cube_r8;
    private final ModelRenderer cube_r9;
    private final ModelRenderer cube_r10;
    private final ModelRenderer bone2;
    private final ModelRenderer cube_r11;
    private final ModelRenderer cube_r12;
    private final ModelRenderer cube_r13;
    private final ModelRenderer cube_r14;
    private final ModelRenderer group2;
    private final ModelRenderer cube_r15;
    private final ModelRenderer cube_r16;
    private final ModelRenderer cube_r17;
    private final ModelRenderer cube_r18;
    private final ModelRenderer cube_r19;
    private final ModelRenderer cube_r20;

    public ComputerModel() {
        texWidth = 256;
        texHeight = 256;

        main = new ModelRenderer(this);
        main.setPos(0.0F, 8.0F, -6.0F);


        group = new ModelRenderer(this);
        group.setPos(8.0F, 0.0F, -8.0F);
        main.addChild(group);
        group.texOffs(0, 0).addBox(-27.0F, -8.0F, -15.0F, 38.0F, 1.0F, 16.0F, 0.0F, false);
        group.texOffs(0, 0).addBox(9.0F, -19.0F, -13.0F, 1.0F, 11.0F, 5.0F, 0.0F, false);
        group.texOffs(100, 119).addBox(-1.0F, -19.0F, -14.0F, 11.0F, 11.0F, 1.0F, 0.0F, false);
        group.texOffs(92, 6).addBox(-2.0F, -20.0F, -15.0F, 13.0F, 1.0F, 8.0F, 0.0F, false);
        group.texOffs(112, 88).addBox(5.0F, -24.0F, -14.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);
        group.texOffs(16, 63).addBox(5.5F, -30.0F, -13.5F, 3.0F, 6.0F, 3.0F, 0.0F, false);
        group.texOffs(136, 52).addBox(-17.0F, -8.5F, -14.5F, 6.0F, 1.0F, 5.0F, 0.0F, false);
        group.texOffs(46, 63).addBox(-15.0F, -9.25F, -13.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        group.texOffs(124, 15).addBox(-20.0F, -17.25F, -13.5F, 12.0F, 8.0F, 1.0F, 0.0F, false);
        group.texOffs(0, 56).addBox(-23.0F, 13.9F, -14.0F, 30.0F, 1.0F, 6.0F, 0.0F, false);
        group.texOffs(46, 65).addBox(-23.0F, -4.0F, -11.0F, 30.0F, 1.0F, 1.0F, 0.0F, false);
        group.texOffs(109, 17).addBox(6.0F, -2.0F, -14.0F, 1.0F, 1.0F, 13.0F, 0.0F, false);
        group.texOffs(16, 63).addBox(-23.0F, -2.0F, -14.0F, 1.0F, 1.0F, 13.0F, 0.0F, false);
        group.texOffs(30, 63).addBox(-24.0F, -7.0F, -14.0F, 1.0F, 23.0F, 14.0F, 0.0F, false);
        group.texOffs(105, 33).addBox(-30.0F, 3.0F, -14.0F, 6.0F, 13.0F, 11.0F, 0.0F, false);
        group.texOffs(0, 63).addBox(7.0F, -7.0F, -14.0F, 1.0F, 23.0F, 14.0F, 0.0F, false);
        group.texOffs(0, 32).addBox(-24.0F, -7.0F, -15.0F, 32.0F, 23.0F, 1.0F, 0.0F, false);

        cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(-1.0F, -9.75F, -11.5F);
        group.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, -0.3927F, 0.0F);
        cube_r1.texOffs(60, 120).addBox(-7.0F, -7.5F, 0.8F, 12.0F, 8.0F, 1.0F, 0.0F, false);
        cube_r1.texOffs(52, 63).addBox(-2.0F, 0.5F, 0.8F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r1.texOffs(102, 135).addBox(-4.0F, 1.25F, -0.2F, 6.0F, 1.0F, 5.0F, 0.0F, false);

        cube_r2 = new ModelRenderer(this);
        cube_r2.setPos(6.8915F, -13.25F, -5.8238F);
        group.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0F, 0.7854F, 0.0F);
        cube_r2.texOffs(112, 68).addBox(-1.3F, -4.0F, -4.3F, 1.0F, 8.0F, 12.0F, 0.0F, false);

        cube_r3 = new ModelRenderer(this);
        cube_r3.setPos(1.2346F, -8.75F, -8.6522F);
        group.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.0F, 0.7854F, 0.0F);
        cube_r3.texOffs(37, 67).addBox(0.7F, -0.5F, 6.7F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        cube_r3.texOffs(128, 34).addBox(-2.3F, 0.25F, 4.7F, 5.0F, 1.0F, 6.0F, 0.0F, false);

        cube_r4 = new ModelRenderer(this);
        cube_r4.setPos(-18.5733F, -8.2667F, -4.3254F);
        group.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.0F, 0.3927F, 0.0F);
        cube_r4.texOffs(110, 57).addBox(-3.75F, -0.0333F, -6.0833F, 8.0F, 1.0F, 10.0F, 0.0F, false);
        cube_r4.texOffs(31, 63).addBox(-2.5F, -0.9833F, -2.8333F, 2.0F, 1.0F, 3.0F, 0.0F, false);
        cube_r4.texOffs(0, 25).addBox(-3.25F, -0.4833F, 1.4167F, 5.0F, 1.0F, 2.0F, 0.0F, false);

        group8 = new ModelRenderer(this);
        group8.setPos(0.2458F, -4.9736F, -2.1984F);
        main.addChild(group8);
        group8.texOffs(0, 17).addBox(-15.2458F, 1.9736F, -14.0516F, 30.0F, 1.0F, 14.0F, 0.0F, false);

        cube_r5 = new ModelRenderer(this);
        cube_r5.setPos(0.0F, 0.0F, -4.0F);
        group8.addChild(cube_r5);
        setRotationAngle(cube_r5, -0.3927F, 0.0F, 0.0F);
        cube_r5.texOffs(0, 0).addBox(-2.9903F, -0.6223F, -0.9946F, 1.0F, 1.0F, 1.0F, -0.3F, false);
        cube_r5.texOffs(0, 0).addBox(-2.9903F, -0.6223F, -0.5946F, 1.0F, 1.0F, 1.0F, -0.3F, false);
        cube_r5.texOffs(0, 2).addBox(-2.8903F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(7.7597F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(7.7597F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(6.9597F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(6.1597F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 28).addBox(0.3597F, -0.5223F, 1.5554F, 6.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-0.4903F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-1.2903F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-2.0653F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-2.8653F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(7.3597F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(6.5597F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(5.7597F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(4.9597F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(4.1597F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(2.5597F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(1.7597F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(0.9597F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(3.3597F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(0.1597F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-0.6403F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-1.4403F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-2.3153F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-2.8903F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(7.3597F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(7.7597F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(7.7597F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-2.6403F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-1.8403F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-1.0403F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-0.2403F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(0.5597F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(1.3597F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(2.1597F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(2.9597F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(3.7597F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(4.5597F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(5.3597F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(6.1597F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(6.9597F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-2.2403F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-1.4403F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-0.6403F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(0.1597F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(0.9597F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(1.7597F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(2.5597F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(3.3597F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(4.1597F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(4.9597F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(5.7597F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(6.5597F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(7.3597F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-9.2403F, -0.5223F, 1.0554F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(12, 5).addBox(-9.2403F, -0.5223F, -0.4446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-7.2403F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-9.2403F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-6.8403F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-7.6403F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-8.4403F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-9.2403F, -0.5223F, -1.6446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-6.8403F, -0.5223F, -1.6446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(12, 7).addBox(-7.6403F, -0.5223F, -1.6446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(12, 7).addBox(-8.4403F, -0.5223F, -1.6446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-9.2403F, -0.5223F, 0.7554F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-6.8403F, -0.5223F, 0.7554F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-7.6403F, -0.5223F, 0.7554F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-8.4403F, -0.5223F, 0.7554F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(12, 5).addBox(-9.2403F, -0.5223F, -0.0446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-6.8403F, -0.5223F, -0.0446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-7.6403F, -0.5223F, -0.0446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-8.4403F, -0.5223F, -0.0446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(12, 5).addBox(-9.2403F, -0.5223F, -0.8446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-6.8403F, -0.5223F, -0.8446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-7.6403F, -0.5223F, -0.8446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-8.4403F, -0.5223F, -0.8446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-4.9403F, -0.5223F, 0.7554F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-4.1403F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-4.9403F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-5.7403F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-4.1403F, -0.5223F, -0.6446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-4.9403F, -0.5223F, -0.6446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-5.7403F, -0.5223F, -0.6446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-4.1403F, -0.5223F, -1.4446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-4.9403F, -0.5223F, -1.4446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-5.7403F, -0.5223F, -1.4446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-2.9403F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-2.6403F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-1.8403F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-1.0403F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-0.2403F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(1.3597F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(0.5597F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(2.1597F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(2.9597F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(3.7597F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(4.5597F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(5.3597F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(6.1597F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(6.9597F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(7.7597F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-5.7403F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-4.9403F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-4.1403F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-2.9403F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-2.1403F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-1.3403F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(-0.5403F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(0.5597F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(1.3597F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(2.1597F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(2.9597F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(4.0597F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(4.8597F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(5.6597F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(6.4597F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(0, 2).addBox(7.7597F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        cube_r5.texOffs(126, 6).addBox(-7.2403F, 1.0777F, -1.9446F, 13.0F, 2.0F, 1.0F, 0.0F, false);
        cube_r5.texOffs(92, 0).addBox(-9.2403F, 0.0777F, -2.4446F, 18.0F, 1.0F, 5.0F, 0.0F, false);

        group3 = new ModelRenderer(this);
        group3.setPos(0.0F, -8.72F, 27.3872F);
        main.addChild(group3);


        cube_r6 = new ModelRenderer(this);
        cube_r6.setPos(0.0F, 0.72F, 0.8002F);
        group3.addChild(cube_r6);
        setRotationAngle(cube_r6, 0.0F, 0.7854F, 0.0F);
        cube_r6.texOffs(104, 67).addBox(-1.0F, -24.0F, -1.1F, 2.0F, 48.0F, 2.0F, 0.0F, false);

        bone = new ModelRenderer(this);
        bone.setPos(-0.2885F, 20.72F, 0.0499F);
        group3.addChild(bone);
        setRotationAngle(bone, 0.0F, -0.3927F, 0.0F);


        cube_r7 = new ModelRenderer(this);
        cube_r7.setPos(-1.0F, 21.0F, -8.0F);
        bone.addChild(cube_r7);
        setRotationAngle(cube_r7, 0.0F, 0.3927F, 0.0F);
        cube_r7.texOffs(132, 88).addBox(-11.8876F, -24.5F, 7.424F, 10.0F, 2.0F, 2.0F, -0.25F, false);
        cube_r7.texOffs(132, 96).addBox(-11.8876F, -63.0F, 7.424F, 10.0F, 2.0F, 2.0F, -0.25F, false);
        cube_r7.texOffs(0, 100).addBox(-11.8876F, -62.0F, 7.924F, 10.0F, 38.0F, 1.0F, 0.0F, false);

        cube_r8 = new ModelRenderer(this);
        cube_r8.setPos(-12.9957F, 21.0F, 2.4185F);
        bone.addChild(cube_r8);
        setRotationAngle(cube_r8, 0.0F, -0.3927F, 0.0F);
        cube_r8.texOffs(132, 92).addBox(-5.0F, -24.5F, -1.0F, 10.0F, 2.0F, 2.0F, -0.25F, false);
        cube_r8.texOffs(133, 0).addBox(-5.0F, -63.0F, -1.0F, 10.0F, 2.0F, 2.0F, -0.25F, false);
        cube_r8.texOffs(22, 100).addBox(-5.0F, -62.0F, -0.5F, 10.0F, 38.0F, 1.0F, 0.0F, false);

        cube_r9 = new ModelRenderer(this);
        cube_r9.setPos(-17.8064F, -20.0F, 0.9671F);
        bone.addChild(cube_r9);
        setRotationAngle(cube_r9, 0.0F, -0.3927F, 0.0F);
        cube_r9.texOffs(44, 100).addBox(-1.0F, -24.0F, -1.5F, 2.0F, 48.0F, 2.0F, 0.0F, false);

        cube_r10 = new ModelRenderer(this);
        cube_r10.setPos(-1.0F, -20.0F, -8.0F);
        bone.addChild(cube_r10);
        setRotationAngle(cube_r10, 0.0F, 0.7854F, 0.0F);
        cube_r10.texOffs(52, 100).addBox(-15.3978F, -24.0F, 2.6955F, 2.0F, 48.0F, 2.0F, 0.0F, false);

        bone2 = new ModelRenderer(this);
        bone2.setPos(0.2885F, 20.72F, 0.0499F);
        group3.addChild(bone2);
        setRotationAngle(bone2, 0.0F, 0.3927F, 0.0F);


        cube_r11 = new ModelRenderer(this);
        cube_r11.setPos(1.0F, 21.0F, -8.0F);
        bone2.addChild(cube_r11);
        setRotationAngle(cube_r11, 0.0F, -0.3927F, 0.0F);
        cube_r11.texOffs(132, 88).addBox(1.8876F, -24.5F, 7.424F, 10.0F, 2.0F, 2.0F, -0.25F, true);
        cube_r11.texOffs(132, 96).addBox(1.8876F, -63.0F, 7.424F, 10.0F, 2.0F, 2.0F, -0.25F, true);
        cube_r11.texOffs(60, 67).addBox(1.8876F, -62.0F, 7.924F, 10.0F, 38.0F, 1.0F, 0.0F, false);

        cube_r12 = new ModelRenderer(this);
        cube_r12.setPos(12.9957F, 21.0F, 2.4185F);
        bone2.addChild(cube_r12);
        setRotationAngle(cube_r12, 0.0F, 0.3927F, 0.0F);
        cube_r12.texOffs(132, 92).addBox(-5.0F, -24.5F, -1.0F, 10.0F, 2.0F, 2.0F, -0.25F, true);
        cube_r12.texOffs(133, 0).addBox(-5.0F, -63.0F, -1.0F, 10.0F, 2.0F, 2.0F, -0.25F, true);
        cube_r12.texOffs(82, 67).addBox(-5.0F, -62.0F, -0.5F, 10.0F, 38.0F, 1.0F, 0.0F, false);

        cube_r13 = new ModelRenderer(this);
        cube_r13.setPos(17.8064F, -20.0F, 0.9671F);
        bone2.addChild(cube_r13);
        setRotationAngle(cube_r13, 0.0F, 0.3927F, 0.0F);
        cube_r13.texOffs(44, 100).addBox(-1.0F, -24.0F, -1.5F, 2.0F, 48.0F, 2.0F, 0.0F, true);

        cube_r14 = new ModelRenderer(this);
        cube_r14.setPos(1.0F, -20.0F, -8.0F);
        bone2.addChild(cube_r14);
        setRotationAngle(cube_r14, 0.0F, -0.7854F, 0.0F);
        cube_r14.texOffs(52, 100).addBox(13.3978F, -24.0F, 2.6955F, 2.0F, 48.0F, 2.0F, 0.0F, true);

        group2 = new ModelRenderer(this);
        group2.setPos(-8.0F, 0.0F, -8.0F);
        main.addChild(group2);
        group2.texOffs(137, 114).addBox(6.0F, 11.9F, 12.0F, 4.0F, 3.0F, 4.0F, 0.0F, false);
        group2.texOffs(104, 57).addBox(6.0F, 6.9F, 12.0F, 4.0F, 2.0F, 4.0F, 0.0F, false);
        group2.texOffs(58, 49).addBox(0.0F, 1.9F, 6.0F, 16.0F, 2.0F, 14.0F, 0.0F, false);
        group2.texOffs(74, 17).addBox(1.0F, 3.9F, 9.0F, 14.0F, 3.0F, 10.0F, 0.0F, false);
        group2.texOffs(126, 9).addBox(4.0F, 10.363F, 1.3275F, 8.0F, 1.0F, 4.0F, 0.0F, false);
        group2.texOffs(66, 32).addBox(1.0F, 0.9F, 7.0F, 14.0F, 1.0F, 11.0F, 0.0F, false);
        group2.texOffs(84, 119).addBox(16.0F, -0.1F, 8.0F, 2.0F, 4.0F, 12.0F, 0.0F, false);
        group2.texOffs(116, 88).addBox(-2.0F, -0.1F, 8.0F, 2.0F, 4.0F, 12.0F, 0.0F, false);
        group2.texOffs(115, 122).addBox(-1.5F, -2.2075F, 9.6691F, 1.0F, 4.0F, 9.0F, 0.0F, false);
        group2.texOffs(128, 24).addBox(-2.0F, -3.2075F, 9.6691F, 2.0F, 1.0F, 9.0F, 0.0F, false);
        group2.texOffs(126, 127).addBox(16.0F, -3.2075F, 9.6691F, 2.0F, 1.0F, 9.0F, 0.0F, false);
        group2.texOffs(126, 114).addBox(16.5F, -2.2075F, 9.6691F, 1.0F, 4.0F, 9.0F, 0.0F, false);
        group2.texOffs(126, 68).addBox(3.0F, -14.1866F, 22.5922F, 10.0F, 7.0F, 2.0F, 0.0F, false);
        group2.texOffs(136, 58).addBox(4.0F, -13.1866F, 21.5922F, 8.0F, 5.0F, 1.0F, 0.0F, false);
        group2.texOffs(66, 44).addBox(0.0F, 12.9F, 13.0F, 16.0F, 2.0F, 2.0F, 0.0F, false);
        group2.texOffs(96, 101).addBox(7.0F, 12.9F, 6.0F, 2.0F, 2.0F, 16.0F, 0.0F, false);
        group2.texOffs(66, 37).addBox(7.0F, 8.9F, 13.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
        group2.texOffs(66, 58).addBox(7.5F, 13.9F, 21.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        group2.texOffs(66, 54).addBox(7.5F, 13.9F, 5.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        group2.texOffs(31, 67).addBox(-1.0F, 13.9F, 13.5F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        group2.texOffs(38, 63).addBox(15.0F, 13.9F, 13.5F, 2.0F, 2.0F, 1.0F, 0.0F, false);

        cube_r15 = new ModelRenderer(this);
        cube_r15.setPos(8.0F, -14.1537F, 22.771F);
        group2.addChild(cube_r15);
        setRotationAngle(cube_r15, -0.3927F, 0.0F, 0.0F);
        cube_r15.texOffs(80, 135).addBox(-4.0F, -2.0F, -1.3F, 8.0F, 3.0F, 3.0F, 0.0F, false);

        cube_r16 = new ModelRenderer(this);
        cube_r16.setPos(13.0F, -10.6866F, 24.5922F);
        group2.addChild(cube_r16);
        setRotationAngle(cube_r16, 0.0F, 0.3927F, 0.0F);
        cube_r16.texOffs(0, 17).addBox(0.0F, -3.5F, -2.0F, 5.0F, 6.0F, 2.0F, 0.0F, false);
        cube_r16.texOffs(7, 0).addBox(1.0F, -2.5F, -3.0F, 3.0F, 4.0F, 1.0F, 0.0F, false);

        cube_r17 = new ModelRenderer(this);
        cube_r17.setPos(3.0F, -10.6866F, 24.5922F);
        group2.addChild(cube_r17);
        setRotationAngle(cube_r17, 0.0F, -0.3927F, 0.0F);
        cube_r17.texOffs(66, 32).addBox(-4.0F, -2.5F, -3.0F, 3.0F, 4.0F, 1.0F, 0.0F, false);
        cube_r17.texOffs(0, 63).addBox(-5.0F, -3.5F, -2.0F, 5.0F, 6.0F, 2.0F, 0.0F, false);

        cube_r18 = new ModelRenderer(this);
        cube_r18.setPos(8.0F, 3.9F, 20.0F);
        group2.addChild(cube_r18);
        setRotationAngle(cube_r18, -0.3927F, 0.0F, 0.0F);
        cube_r18.texOffs(116, 104).addBox(-7.0F, -11.5F, -3.0F, 14.0F, 9.0F, 1.0F, 0.0F, false);
        cube_r18.texOffs(120, 137).addBox(-10.0F, -11.0F, -4.0F, 2.0F, 11.0F, 4.0F, 0.0F, false);
        cube_r18.texOffs(132, 137).addBox(8.0F, -11.0F, -4.0F, 2.0F, 11.0F, 4.0F, 0.0F, false);
        cube_r18.texOffs(60, 106).addBox(-8.0F, -12.0F, -2.0F, 16.0F, 12.0F, 2.0F, 0.0F, false);

        cube_r19 = new ModelRenderer(this);
        cube_r19.setPos(8.0F, 5.2F, 17.1F);
        group2.addChild(cube_r19);
        setRotationAngle(cube_r19, -0.3927F, 0.0F, 0.0F);
        cube_r19.texOffs(66, 48).addBox(8.5F, -4.0F, -9.7F, 1.0F, 4.0F, 2.0F, 0.0F, false);
        cube_r19.texOffs(0, 56).addBox(-9.5F, -4.0F, -9.7F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        cube_r20 = new ModelRenderer(this);
        cube_r20.setPos(8.0F, 1.9F, 6.0F);
        group2.addChild(cube_r20);
        setRotationAngle(cube_r20, -0.3927F, 0.0F, 0.0F);
        cube_r20.texOffs(60, 129).addBox(-4.0F, 0.0F, 1.0F, 8.0F, 9.0F, 2.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        main.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}