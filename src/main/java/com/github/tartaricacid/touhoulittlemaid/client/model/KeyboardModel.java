package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class KeyboardModel extends EntityModel<Entity> {
    private final ModelRenderer main;
    private final ModelRenderer bone;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer cube_r3;
    private final ModelRenderer cube_r4;
    private final ModelRenderer cube_r5;
    private final ModelRenderer cube_r6;
    private final ModelRenderer bone3;
    private final ModelRenderer bone4;
    private final ModelRenderer bone5;
    private final ModelRenderer bone2;
    private final ModelRenderer group;
    private final ModelRenderer group2;
    private final ModelRenderer bone6;

    public KeyboardModel() {
        texWidth = 128;
        texHeight = 128;

        main = new ModelRenderer(this);
        main.setPos(0.0F, 8.0F, 7.0F);


        bone = new ModelRenderer(this);
        bone.setPos(8.0F, 0.0F, -11.0F);
        main.addChild(bone);
        bone.texOffs(0, 0).addBox(-29.0F, 1.0F, 12.0F, 42.0F, 1.0F, 15.0F, 0.0F, false);
        bone.texOffs(0, 16).addBox(-29.0F, -1.0F, 18.0F, 42.0F, 2.0F, 9.0F, 0.0F, false);
        bone.texOffs(46, 71).addBox(-12.0F, -1.1F, 22.0F, 8.0F, 2.0F, 3.0F, 0.0F, false);
        bone.texOffs(29, 32).addBox(-9.0F, -1.1F, 20.5F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        bone.texOffs(29, 29).addBox(-9.0F, -1.1F, 19.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        bone.texOffs(18, 76).addBox(-29.0F, -0.4142F, 13.4142F, 4.0F, 2.0F, 5.0F, 0.0F, false);
        bone.texOffs(0, 75).addBox(9.0F, -0.4142F, 13.4142F, 4.0F, 2.0F, 5.0F, 0.0F, false);
        bone.texOffs(44, 47).addBox(-20.0F, 2.0F, 22.0F, 24.0F, 1.0F, 4.0F, 0.0F, false);
        bone.texOffs(0, 27).addBox(-19.0F, 2.0F, 17.0F, 2.0F, 1.0F, 5.0F, 0.0F, false);
        bone.texOffs(0, 27).addBox(1.0F, 2.0F, 17.0F, 2.0F, 1.0F, 5.0F, 0.0F, true);
        bone.texOffs(0, 11).addBox(10.0F, 15.0F, 22.0F, 3.0F, 1.0F, 3.0F, 0.0F, false);
        bone.texOffs(0, 11).addBox(-29.0F, 15.0F, 23.0F, 3.0F, 1.0F, 3.0F, 0.0F, false);
        bone.texOffs(44, 47).addBox(-20.0F, 2.0F, 13.0F, 24.0F, 1.0F, 4.0F, 0.0F, false);
        bone.texOffs(0, 11).addBox(-29.0F, 15.0F, 13.0F, 3.0F, 1.0F, 3.0F, 0.0F, false);
        bone.texOffs(0, 27).addBox(8.5F, -5.0F, -15.0F, 7.0F, 21.0F, 15.0F, 0.0F, false);
        bone.texOffs(44, 29).addBox(7.5F, -6.0F, -16.0F, 8.0F, 1.0F, 17.0F, 0.0F, false);
        bone.texOffs(60, 63).addBox(9.5F, -7.0F, -9.25F, 5.0F, 1.0F, 7.0F, 0.0F, false);
        bone.texOffs(0, 0).addBox(11.5F, -14.0F, -7.75F, 2.0F, 7.0F, 4.0F, 0.0F, false);
        bone.texOffs(9, 30).addBox(10.25F, -11.0F, -6.25F, 2.0F, 1.0F, 1.0F, -0.3F, false);
        bone.texOffs(29, 35).addBox(10.75F, -11.0F, -6.25F, 1.0F, 1.0F, 1.0F, -0.2F, false);
        bone.texOffs(0, 11).addBox(10.0F, 15.0F, 14.0F, 3.0F, 1.0F, 3.0F, 0.0F, false);

        cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(11.25F, -10.5F, -5.75F);
        bone.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.7854F, 0.0F, 0.0F);
        cube_r1.texOffs(0, 30).addBox(-0.5F, 1.7F, -0.5F, 1.0F, 1.0F, 1.0F, -0.275F, false);
        cube_r1.texOffs(0, 0).addBox(-0.5F, -4.5F, -0.5F, 1.0F, 2.0F, 1.0F, -0.4F, false);
        cube_r1.texOffs(8, 33).addBox(-0.5F, -5.5F, -0.5F, 1.0F, 8.0F, 1.0F, -0.45F, false);

        cube_r2 = new ModelRenderer(this);
        cube_r2.setPos(-8.0F, 1.5F, 20.5F);
        bone.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0F, 0.0F, 0.3927F);
        cube_r2.texOffs(48, 76).addBox(-7.0F, 4.5F, -4.5F, 1.0F, 1.0F, 7.0F, 0.0F, false);
        cube_r2.texOffs(48, 76).addBox(17.0F, 4.5F, -4.5F, 1.0F, 1.0F, 7.0F, 0.0F, false);
        cube_r2.texOffs(29, 27).addBox(-10.0F, 4.5F, -5.5F, 34.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r2.texOffs(29, 27).addBox(-10.0F, 4.5F, 2.5F, 34.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r3 = new ModelRenderer(this);
        cube_r3.setPos(-8.0F, 1.5F, 20.5F);
        bone.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.0F, 0.0F, -0.3927F);
        cube_r3.texOffs(35, 71).addBox(6.0F, 4.5F, -5.5F, 1.0F, 1.0F, 9.0F, 0.0F, false);
        cube_r3.texOffs(35, 71).addBox(-18.0F, 4.5F, -5.5F, 1.0F, 1.0F, 9.0F, 0.0F, false);
        cube_r3.texOffs(29, 27).addBox(-24.0F, 4.5F, -6.5F, 34.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r3.texOffs(29, 27).addBox(-24.0F, 4.5F, 3.5F, 34.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r4 = new ModelRenderer(this);
        cube_r4.setPos(-8.0F, 6.9F, 19.5F);
        bone.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.0F, 0.0F, -0.7854F);
        cube_r4.texOffs(18, 63).addBox(-0.5F, -0.5F, -6.0F, 1.0F, 1.0F, 12.0F, -0.2F, false);

        cube_r5 = new ModelRenderer(this);
        cube_r5.setPos(11.0F, 0.2929F, 13.4142F);
        bone.addChild(cube_r5);
        setRotationAngle(cube_r5, 0.7854F, 0.0F, 0.0F);
        cube_r5.texOffs(44, 42).addBox(-2.0F, -0.5F, -1.5F, 4.0F, 1.0F, 2.0F, 0.0F, false);
        cube_r5.texOffs(32, 71).addBox(-40.0F, -0.5F, -1.5F, 4.0F, 1.0F, 2.0F, 0.0F, false);

        cube_r6 = new ModelRenderer(this);
        cube_r6.setPos(-8.0F, -1.0F, 26.5F);
        bone.addChild(cube_r6);
        setRotationAngle(cube_r6, -0.3927F, 0.0F, 0.0F);
        cube_r6.texOffs(0, 63).addBox(-7.0F, -10.5F, -1.0F, 14.0F, 11.0F, 1.0F, -0.475F, false);
        cube_r6.texOffs(29, 39).addBox(-6.0F, -0.4F, -1.4F, 12.0F, 1.0F, 2.0F, -0.4F, false);
        cube_r6.texOffs(44, 52).addBox(-9.0F, -9.0F, -0.5F, 18.0F, 10.0F, 1.0F, -0.3F, false);

        bone3 = new ModelRenderer(this);
        bone3.setPos(-8.0F, 1.5F, 19.5F);
        bone.addChild(bone3);
        bone3.texOffs(8, 0).addBox(-5.7F, -2.85F, 1.0F, 2.0F, 2.0F, 1.0F, -0.25F, false);
        bone3.texOffs(8, 0).addBox(-3.7F, -2.85F, 1.0F, 2.0F, 2.0F, 1.0F, -0.25F, false);
        bone3.texOffs(8, 0).addBox(1.7F, -2.85F, 1.0F, 2.0F, 2.0F, 1.0F, -0.25F, false);
        bone3.texOffs(8, 0).addBox(3.7F, -2.85F, 1.0F, 2.0F, 2.0F, 1.0F, -0.25F, false);

        bone4 = new ModelRenderer(this);
        bone4.setPos(-8.0F, 1.5F, 19.5F);
        bone.addChild(bone4);
        bone4.texOffs(9, 11).addBox(-5.7F, -2.85F, 0.25F, 2.0F, 2.0F, 1.0F, -0.25F, false);
        bone4.texOffs(9, 11).addBox(-3.7F, -2.85F, 0.25F, 2.0F, 2.0F, 1.0F, -0.25F, false);
        bone4.texOffs(9, 11).addBox(1.7F, -2.85F, 0.25F, 2.0F, 2.0F, 1.0F, -0.25F, false);
        bone4.texOffs(9, 11).addBox(3.7F, -2.85F, 0.25F, 2.0F, 2.0F, 1.0F, -0.25F, false);

        bone5 = new ModelRenderer(this);
        bone5.setPos(-8.0F, 1.5F, 19.5F);
        bone.addChild(bone5);
        bone5.texOffs(9, 27).addBox(-5.7F, -2.85F, -0.5F, 2.0F, 2.0F, 1.0F, -0.25F, false);
        bone5.texOffs(9, 27).addBox(-3.7F, -2.85F, -0.5F, 2.0F, 2.0F, 1.0F, -0.25F, false);
        bone5.texOffs(9, 27).addBox(1.7F, -2.85F, -0.5F, 2.0F, 2.0F, 1.0F, -0.25F, false);
        bone5.texOffs(9, 27).addBox(3.7F, -2.85F, -0.5F, 2.0F, 2.0F, 1.0F, -0.25F, false);

        bone2 = new ModelRenderer(this);
        bone2.setPos(-8.0F, 1.5F, 19.5F);
        bone.addChild(bone2);
        bone2.texOffs(0, 27).addBox(-7.7F, -2.85F, 4.0F, 1.0F, 2.0F, 1.0F, -0.25F, false);
        bone2.texOffs(0, 27).addBox(-7.7F, -2.85F, 3.25F, 1.0F, 2.0F, 1.0F, -0.25F, false);
        bone2.texOffs(0, 27).addBox(-7.7F, -2.85F, 2.5F, 1.0F, 2.0F, 1.0F, -0.25F, false);
        bone2.texOffs(0, 27).addBox(-6.7F, -2.85F, 4.0F, 1.0F, 2.0F, 1.0F, -0.25F, false);
        bone2.texOffs(0, 27).addBox(-6.7F, -2.85F, 3.25F, 1.0F, 2.0F, 1.0F, -0.25F, false);
        bone2.texOffs(0, 27).addBox(-6.7F, -2.85F, 2.5F, 1.0F, 2.0F, 1.0F, -0.25F, false);
        bone2.texOffs(0, 27).addBox(-5.7F, -2.85F, 4.0F, 1.0F, 2.0F, 1.0F, -0.25F, false);
        bone2.texOffs(0, 27).addBox(-5.7F, -2.85F, 3.25F, 1.0F, 2.0F, 1.0F, -0.25F, false);
        bone2.texOffs(0, 27).addBox(-5.7F, -2.85F, 2.5F, 1.0F, 2.0F, 1.0F, -0.25F, false);
        bone2.texOffs(0, 27).addBox(4.7F, -2.85F, 4.0F, 1.0F, 2.0F, 1.0F, -0.25F, false);
        bone2.texOffs(0, 27).addBox(-7.7F, -2.85F, 1.0F, 1.0F, 2.0F, 1.0F, -0.25F, false);
        bone2.texOffs(0, 27).addBox(-7.7F, -2.85F, 0.25F, 1.0F, 2.0F, 1.0F, -0.25F, false);
        bone2.texOffs(0, 27).addBox(-7.7F, -2.85F, -0.5F, 1.0F, 2.0F, 1.0F, -0.25F, false);
        bone2.texOffs(0, 27).addBox(-6.7F, -2.85F, 1.0F, 1.0F, 2.0F, 1.0F, -0.25F, false);
        bone2.texOffs(0, 27).addBox(-6.7F, -2.85F, 0.25F, 1.0F, 2.0F, 1.0F, -0.25F, false);
        bone2.texOffs(0, 27).addBox(-6.7F, -2.85F, -0.5F, 1.0F, 2.0F, 1.0F, -0.25F, false);
        bone2.texOffs(0, 27).addBox(4.7F, -2.85F, 3.25F, 1.0F, 2.0F, 1.0F, -0.25F, false);
        bone2.texOffs(0, 27).addBox(4.7F, -2.85F, 2.5F, 1.0F, 2.0F, 1.0F, -0.25F, false);
        bone2.texOffs(0, 27).addBox(5.7F, -2.85F, 4.0F, 1.0F, 2.0F, 1.0F, -0.25F, false);
        bone2.texOffs(0, 27).addBox(5.7F, -2.85F, 3.25F, 1.0F, 2.0F, 1.0F, -0.25F, false);
        bone2.texOffs(0, 27).addBox(5.7F, -2.85F, 2.5F, 1.0F, 2.0F, 1.0F, -0.25F, false);
        bone2.texOffs(0, 27).addBox(6.7F, -2.85F, 4.0F, 1.0F, 2.0F, 1.0F, -0.25F, false);
        bone2.texOffs(0, 27).addBox(6.7F, -2.85F, 3.25F, 1.0F, 2.0F, 1.0F, -0.25F, false);
        bone2.texOffs(0, 27).addBox(6.7F, -2.85F, 2.5F, 1.0F, 2.0F, 1.0F, -0.25F, false);
        bone2.texOffs(0, 27).addBox(5.7F, -2.85F, 1.0F, 1.0F, 2.0F, 1.0F, -0.25F, false);
        bone2.texOffs(0, 27).addBox(5.7F, -2.85F, 0.25F, 1.0F, 2.0F, 1.0F, -0.25F, false);
        bone2.texOffs(0, 27).addBox(5.7F, -2.85F, -0.5F, 1.0F, 2.0F, 1.0F, -0.25F, false);
        bone2.texOffs(0, 27).addBox(6.7F, -2.85F, -0.5F, 1.0F, 2.0F, 1.0F, -0.25F, false);
        bone2.texOffs(0, 27).addBox(6.7F, -2.85F, 0.25F, 1.0F, 2.0F, 1.0F, -0.25F, false);
        bone2.texOffs(0, 27).addBox(6.7F, -2.85F, 1.0F, 1.0F, 2.0F, 1.0F, -0.25F, false);

        group = new ModelRenderer(this);
        group.setPos(-8.0F, 1.2F, 21.4F);
        bone.addChild(group);
        group.texOffs(60, 71).addBox(-14.0F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, -0.175F, false);
        group.texOffs(60, 71).addBox(-10.4F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, -0.175F, false);
        group.texOffs(60, 71).addBox(-13.1F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, -0.175F, false);
        group.texOffs(60, 71).addBox(-12.2F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, -0.175F, false);
        group.texOffs(60, 71).addBox(-4.1F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, -0.175F, false);
        group.texOffs(60, 71).addBox(-3.2F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, -0.175F, false);
        group.texOffs(60, 71).addBox(-5.9F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, -0.175F, false);
        group.texOffs(60, 71).addBox(-6.8F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, -0.175F, false);
        group.texOffs(60, 71).addBox(-7.7F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, -0.175F, false);
        group.texOffs(60, 71).addBox(-9.5F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, -0.175F, false);
        group.texOffs(60, 71).addBox(-1.4F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, -0.175F, false);
        group.texOffs(60, 71).addBox(0.4F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, -0.175F, false);
        group.texOffs(60, 71).addBox(-0.5F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, -0.175F, false);
        group.texOffs(60, 71).addBox(2.2F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, -0.175F, false);
        group.texOffs(60, 71).addBox(3.1F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, -0.175F, false);
        group.texOffs(60, 71).addBox(4.9F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, -0.175F, false);
        group.texOffs(60, 71).addBox(5.8F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, -0.175F, false);
        group.texOffs(60, 71).addBox(6.7F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, -0.175F, false);
        group.texOffs(60, 71).addBox(9.4F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, -0.175F, false);
        group.texOffs(60, 71).addBox(8.5F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, -0.175F, false);
        group.texOffs(60, 71).addBox(11.2F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, -0.175F, false);
        group.texOffs(60, 71).addBox(-16.7F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, -0.175F, false);
        group.texOffs(60, 71).addBox(-15.8F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, -0.175F, false);
        group.texOffs(60, 71).addBox(15.7F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, -0.175F, false);
        group.texOffs(60, 71).addBox(14.8F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, -0.175F, false);
        group.texOffs(60, 71).addBox(13.0F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, -0.175F, false);
        group.texOffs(60, 71).addBox(12.1F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, -0.175F, false);

        group2 = new ModelRenderer(this);
        group2.setPos(-8.0F, 1.2F, 21.4F);
        bone.addChild(group2);
        group2.texOffs(70, 72).addBox(16.15F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(-14.45F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(-11.75F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(-10.85F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(-13.55F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(-12.65F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(-4.55F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(-3.65F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(-6.35F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(-5.45F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(-7.25F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(-8.15F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(-9.05F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(-9.95F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(-2.75F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(-1.85F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(-0.05F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(-0.95F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(1.75F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(0.85F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(3.55F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(2.65F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(4.45F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(5.35F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(6.25F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(7.15F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(8.95F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(8.05F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(10.75F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(9.85F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(-17.15F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(-16.25F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(-15.35F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(15.25F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(14.35F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(13.45F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(12.55F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);
        group2.texOffs(70, 72).addBox(11.65F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, -0.075F, false);

        bone6 = new ModelRenderer(this);
        bone6.setPos(0.0F, 0.0F, 0.0F);
        main.addChild(bone6);
        bone6.texOffs(32, 63).addBox(-3.5F, 6.0F, -10.5F, 7.0F, 1.0F, 7.0F, 0.0F, false);
        bone6.texOffs(29, 29).addBox(-4.0F, 7.0F, -11.0F, 8.0F, 2.0F, 8.0F, 0.0F, false);
        bone6.texOffs(0, 33).addBox(-4.0F, 9.0F, -11.0F, 2.0F, 7.0F, 2.0F, 0.0F, false);
        bone6.texOffs(0, 16).addBox(-4.0F, 9.0F, -5.0F, 2.0F, 7.0F, 2.0F, 0.0F, false);
        bone6.texOffs(77, 38).addBox(-3.5F, 11.0F, -9.0F, 1.0F, 2.0F, 4.0F, 0.0F, false);
        bone6.texOffs(31, 76).addBox(-2.0F, 13.0F, -10.5F, 4.0F, 2.0F, 1.0F, 0.0F, false);
        bone6.texOffs(80, 61).addBox(2.0F, 9.0F, -11.0F, 2.0F, 7.0F, 2.0F, 0.0F, false);
        bone6.texOffs(13, 76).addBox(-2.0F, 13.0F, -4.5F, 4.0F, 2.0F, 1.0F, 0.0F, false);
        bone6.texOffs(77, 29).addBox(2.0F, 9.0F, -5.0F, 2.0F, 7.0F, 2.0F, 0.0F, false);
        bone6.texOffs(53, 63).addBox(2.5F, 11.0F, -9.0F, 1.0F, 2.0F, 4.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        //previously the render function, render code was moved to a method below
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