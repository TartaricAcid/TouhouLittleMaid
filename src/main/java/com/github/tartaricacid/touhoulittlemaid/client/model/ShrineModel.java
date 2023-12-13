package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ShrineModel extends EntityModel<Entity> {
    private final ModelRenderer main;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer cube_r3;
    private final ModelRenderer cube_r4;
    private final ModelRenderer cube_r5;
    private final ModelRenderer cube_r6;
    private final ModelRenderer cube_r7;
    private final ModelRenderer bone7;
    private final ModelRenderer bone4;
    private final ModelRenderer bone5;
    private final ModelRenderer bone3;
    private final ModelRenderer bone6;

    public ShrineModel() {
        texWidth = 128;
        texHeight = 128;

        main = new ModelRenderer(this);
        main.setPos(0.0F, 24.0F, 0.0F);
        main.texOffs(0, 0).addBox(-7.5F, -5.0F, -7.5F, 15.0F, 5.0F, 15.0F, 0.0F, false);
        main.texOffs(0, 21).addBox(-6.0F, -10.0F, -6.0F, 12.0F, 1.0F, 12.0F, 0.0F, false);
        main.texOffs(60, 26).addBox(-3.5F, -19.0F, -3.5F, 7.0F, 1.0F, 7.0F, 0.0F, true);
        main.texOffs(73, 35).addBox(-3.5F, -17.5F, 2.5F, 7.0F, 6.0F, 1.0F, 0.0F, false);
        main.texOffs(31, 39).addBox(-5.0F, -10.5F, -5.0F, 10.0F, 1.0F, 10.0F, 0.0F, false);
        main.texOffs(42, 21).addBox(-0.5F, -23.75F, -3.75F, 1.0F, 4.0F, 1.0F, 0.0F, false);
        main.texOffs(0, 37).addBox(-0.5F, -23.75F, 2.75F, 1.0F, 4.0F, 1.0F, 0.0F, false);
        main.texOffs(98, 0).addBox(-0.5F, -24.5F, -7.0F, 1.0F, 1.0F, 14.0F, 0.0F, false);
        main.texOffs(21, 68).addBox(2.5F, -17.5F, -3.5F, 1.0F, 6.0F, 7.0F, 0.0F, true);
        main.texOffs(21, 68).addBox(-3.5F, -17.5F, -3.5F, 1.0F, 6.0F, 7.0F, 0.0F, false);
        main.texOffs(60, 26).addBox(-3.5F, -11.5F, -3.5F, 7.0F, 1.0F, 7.0F, 0.0F, false);
        main.texOffs(60, 26).addBox(-3.5F, -18.0F, -3.5F, 7.0F, 1.0F, 7.0F, 0.0F, false);

        cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(0.0F, -20.6005F, 3.0F);
        main.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, 0.0F, -0.7854F);
        cube_r1.texOffs(0, 0).addBox(-2.5F, -2.5F, -0.5F, 5.0F, 5.0F, 1.0F, 0.0F, true);
        cube_r1.texOffs(0, 8).addBox(-2.5F, -2.5F, -6.5F, 5.0F, 5.0F, 1.0F, 0.0F, false);

        cube_r2 = new ModelRenderer(this);
        cube_r2.setPos(0.0F, -16.2342F, 0.0F);
        main.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.3927F, 0.0F, 0.0F);
        cube_r2.texOffs(98, 0).addBox(-0.5F, -5.034F, 9.2477F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        cube_r3 = new ModelRenderer(this);
        cube_r3.setPos(0.0F, -16.2342F, 0.0F);
        main.addChild(cube_r3);
        setRotationAngle(cube_r3, -0.3927F, 0.0F, 0.0F);
        cube_r3.texOffs(98, 0).addBox(-0.5F, -5.034F, -11.2477F, 1.0F, 1.0F, 2.0F, 0.0F, true);

        cube_r4 = new ModelRenderer(this);
        cube_r4.setPos(0.0F, -16.2342F, 0.0F);
        main.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.0F, 0.0F, -0.3927F);
        cube_r4.texOffs(98, 0).addBox(-6.9399F, -5.7711F, -4.0F, 5.25F, 0.5F, 1.0F, 0.0F, true);
        cube_r4.texOffs(98, 0).addBox(-6.9399F, -5.7711F, 3.0F, 5.25F, 0.5F, 1.0F, 0.0F, false);
        cube_r4.texOffs(35, 21).addBox(-6.4781F, -5.6293F, -7.0F, 5.0F, 0.5F, 14.0F, 0.0F, false);

        cube_r5 = new ModelRenderer(this);
        cube_r5.setPos(0.0F, -16.2342F, 0.0F);
        main.addChild(cube_r5);
        setRotationAngle(cube_r5, 0.0F, 0.0F, 0.7854F);
        cube_r5.texOffs(98, 0).addBox(-6.0166F, -5.4559F, -4.0F, 0.5F, 5.0F, 1.0F, 0.0F, true);
        cube_r5.texOffs(98, 0).addBox(-6.0166F, -5.4559F, 3.0F, 0.5F, 5.0F, 1.0F, 0.0F, false);
        cube_r5.texOffs(33, 51).addBox(-5.8044F, -5.5973F, -7.0F, 0.5F, 5.0F, 14.0F, 0.0F, false);

        cube_r6 = new ModelRenderer(this);
        cube_r6.setPos(0.0F, -16.2342F, 0.0F);
        main.addChild(cube_r6);
        setRotationAngle(cube_r6, 0.0F, 0.0F, 0.3927F);
        cube_r6.texOffs(98, 0).addBox(1.6899F, -5.7711F, 3.0F, 5.25F, 0.5F, 1.0F, 0.0F, false);
        cube_r6.texOffs(98, 0).addBox(1.6899F, -5.7711F, -4.0F, 5.25F, 0.5F, 1.0F, 0.0F, false);
        cube_r6.texOffs(35, 21).addBox(1.4781F, -5.6293F, -7.0F, 5.0F, 0.5F, 14.0F, 0.0F, true);

        cube_r7 = new ModelRenderer(this);
        cube_r7.setPos(0.0F, -16.2342F, 0.0F);
        main.addChild(cube_r7);
        setRotationAngle(cube_r7, 0.0F, 0.0F, -0.7854F);
        cube_r7.texOffs(98, 0).addBox(5.5166F, -5.4559F, 3.0F, 0.5F, 5.0F, 1.0F, 0.0F, false);
        cube_r7.texOffs(98, 0).addBox(5.5166F, -5.4559F, -4.0F, 0.5F, 5.0F, 1.0F, 0.0F, false);
        cube_r7.texOffs(61, 13).addBox(0.5771F, -5.5771F, -3.5F, 5.0F, 5.0F, 1.0F, 0.0F, false);
        cube_r7.texOffs(61, 13).addBox(0.2235F, -5.2235F, 2.5F, 5.0F, 5.0F, 1.0F, 0.0F, false);
        cube_r7.texOffs(33, 51).addBox(5.3044F, -5.5973F, -7.0F, 0.5F, 5.0F, 14.0F, 0.0F, true);

        bone7 = new ModelRenderer(this);
        bone7.setPos(0.0F, -16.2342F, 0.0F);
        main.addChild(bone7);
        bone7.texOffs(110, 36).addBox(2.4833F, -1.7325F, -3.7F, 0.55F, 7.0F, 0.2F, 0.0F, false);
        bone7.texOffs(110, 36).addBox(-3.5F, 4.2508F, -3.7F, 7.0F, 0.55F, 0.2F, 0.0F, true);
        bone7.texOffs(110, 36).addBox(-3.0333F, -1.7325F, -3.7F, 0.55F, 7.0F, 0.2F, 0.0F, true);
        bone7.texOffs(110, 36).addBox(-3.5F, -1.2658F, -3.7F, 7.0F, 0.55F, 0.2F, 0.0F, true);
        bone7.texOffs(110, 36).addBox(3.5F, -0.3158F, -3.5F, 0.2F, 0.2F, 7.0F, 0.0F, false);
        bone7.texOffs(110, 36).addBox(3.5F, -1.7325F, 2.4833F, 0.2F, 7.0F, 0.55F, 0.0F, true);
        bone7.texOffs(110, 36).addBox(3.5F, 4.2508F, -3.5F, 0.2F, 0.55F, 7.0F, 0.0F, false);
        bone7.texOffs(110, 36).addBox(3.5F, 3.6508F, -3.5F, 0.2F, 0.2F, 7.0F, 0.0F, false);
        bone7.texOffs(110, 36).addBox(3.5F, -1.7325F, -3.0333F, 0.2F, 7.0F, 0.55F, 0.0F, false);
        bone7.texOffs(110, 36).addBox(3.5F, -1.2658F, -3.5F, 0.2F, 0.55F, 7.0F, 0.0F, false);
        bone7.texOffs(110, 36).addBox(3.5F, -1.7325F, -2.0833F, 0.2F, 7.0F, 0.2F, 0.0F, false);
        bone7.texOffs(110, 36).addBox(3.5F, -1.7325F, 1.8833F, 0.2F, 7.0F, 0.2F, 0.0F, false);
        bone7.texOffs(110, 36).addBox(-3.0333F, -1.7325F, 3.5F, 0.55F, 7.0F, 0.2F, 0.0F, false);
        bone7.texOffs(110, 36).addBox(-3.5F, -1.2658F, 3.5F, 7.0F, 0.55F, 0.2F, 0.0F, false);
        bone7.texOffs(110, 36).addBox(2.4833F, -1.7325F, 3.5F, 0.55F, 7.0F, 0.2F, 0.0F, false);
        bone7.texOffs(110, 36).addBox(1.8833F, -1.7325F, 3.5F, 0.2F, 7.0F, 0.2F, 0.0F, false);
        bone7.texOffs(110, 36).addBox(-3.5F, 4.2508F, 3.5F, 7.0F, 0.55F, 0.2F, 0.0F, false);
        bone7.texOffs(110, 36).addBox(-3.5F, 3.6508F, 3.5F, 7.0F, 0.2F, 0.2F, 0.0F, false);
        bone7.texOffs(110, 36).addBox(-3.5F, -0.3158F, 3.5F, 7.0F, 0.2F, 0.2F, 0.0F, false);
        bone7.texOffs(110, 36).addBox(-2.0833F, -1.7325F, 3.5F, 0.2F, 7.0F, 0.2F, 0.0F, false);
        bone7.texOffs(110, 36).addBox(-3.7F, -1.7325F, 2.4833F, 0.2F, 7.0F, 0.55F, 0.0F, false);
        bone7.texOffs(110, 36).addBox(-3.7F, -1.2658F, -3.5F, 0.2F, 0.55F, 7.0F, 0.0F, true);
        bone7.texOffs(110, 36).addBox(-3.7F, -0.3158F, -3.5F, 0.2F, 0.2F, 7.0F, 0.0F, true);
        bone7.texOffs(110, 36).addBox(-3.7F, -1.7325F, -3.0333F, 0.2F, 7.0F, 0.55F, 0.0F, false);
        bone7.texOffs(110, 36).addBox(-3.7F, 4.2508F, -3.5F, 0.2F, 0.55F, 7.0F, 0.0F, true);
        bone7.texOffs(110, 36).addBox(-3.7F, 3.6508F, -3.5F, 0.2F, 0.2F, 7.0F, 0.0F, true);
        bone7.texOffs(110, 36).addBox(-3.7F, -1.7325F, -2.0833F, 0.2F, 7.0F, 0.2F, 0.0F, false);
        bone7.texOffs(110, 36).addBox(-3.7F, -1.7325F, 1.8833F, 0.2F, 7.0F, 0.2F, 0.0F, false);

        bone4 = new ModelRenderer(this);
        bone4.setPos(-0.25F, -16.2342F, -0.25F);
        main.addChild(bone4);
        bone4.texOffs(32, 36).addBox(-6.75F, 7.9842F, 6.5F, 14.0F, 1.0F, 0.5F, -0.1F, false);
        bone4.texOffs(0, 35).addBox(3.25F, 7.9842F, -6.5F, 4.0F, 1.0F, 0.5F, -0.1F, false);
        bone4.texOffs(0, 35).addBox(-6.75F, 7.9842F, -6.5F, 4.0F, 1.0F, 0.5F, -0.1F, false);
        bone4.texOffs(60, 10).addBox(6.5F, 7.9842F, -6.75F, 0.5F, 1.0F, 14.0F, -0.1F, false);
        bone4.texOffs(58, 37).addBox(-6.5F, 7.9842F, -6.75F, 0.5F, 1.0F, 14.0F, -0.1F, false);

        bone5 = new ModelRenderer(this);
        bone5.setPos(0.25F, 0.0F, 0.25F);
        bone4.addChild(bone5);
        bone5.texOffs(62, 0).addBox(-6.75F, 7.4842F, -6.75F, 0.5F, 7.0F, 0.5F, 0.1F, false);
        bone5.texOffs(62, 0).addBox(-6.75F, 7.4842F, -5.75F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(-6.75F, 7.4842F, -4.75F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(-6.75F, 7.4842F, -3.75F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(-6.75F, 7.4842F, -2.75F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(-6.75F, 7.4842F, -1.75F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(-6.75F, 7.4842F, -0.75F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(-6.75F, 7.4842F, 0.25F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(-6.75F, 7.4842F, 1.25F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(-6.75F, 7.4842F, 2.25F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(-6.75F, 7.4842F, 3.25F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(-6.75F, 7.4842F, 4.25F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(-6.75F, 7.4842F, 5.25F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(6.25F, 7.4842F, 5.25F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(6.25F, 7.4842F, 4.25F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(6.25F, 7.4842F, 3.25F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(6.25F, 7.4842F, 2.25F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(6.25F, 7.4842F, 1.25F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(6.25F, 7.4842F, 0.25F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(6.25F, 7.4842F, -0.75F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(6.25F, 7.4842F, -1.75F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(6.25F, 7.4842F, -2.75F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(6.25F, 7.4842F, -3.75F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(6.25F, 7.4842F, -4.75F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(6.25F, 7.4842F, -5.75F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(6.25F, 7.4842F, -6.75F, 0.5F, 7.0F, 0.5F, 0.1F, false);
        bone5.texOffs(62, 0).addBox(-6.75F, 7.4842F, 6.25F, 0.5F, 7.0F, 0.5F, 0.1F, false);
        bone5.texOffs(62, 0).addBox(-5.75F, 7.4842F, 6.25F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(-4.75F, 7.4842F, 6.25F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(-3.75F, 7.4842F, 6.25F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(-2.75F, 7.4842F, 6.25F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(-1.75F, 7.4842F, 6.25F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(-0.75F, 7.4842F, 6.25F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(0.25F, 7.4842F, 6.25F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(1.25F, 7.4842F, 6.25F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(2.25F, 7.4842F, 6.25F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(3.25F, 7.4842F, 6.25F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(4.25F, 7.4842F, 6.25F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(5.25F, 7.4842F, 6.25F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(6.25F, 7.4842F, 6.25F, 0.5F, 7.0F, 0.5F, 0.1F, false);
        bone5.texOffs(62, 0).addBox(-6.75F, 7.4842F, -6.75F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(-5.75F, 7.4842F, -6.75F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(-4.75F, 7.4842F, -6.75F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(-3.75F, 7.4842F, -6.75F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(3.25F, 7.4842F, -6.75F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(4.25F, 7.4842F, -6.75F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(5.25F, 7.4842F, -6.75F, 0.5F, 7.0F, 0.5F, 0.0F, false);
        bone5.texOffs(62, 0).addBox(6.25F, 7.4842F, -6.75F, 0.5F, 7.0F, 0.5F, 0.0F, false);

        bone3 = new ModelRenderer(this);
        bone3.setPos(0.0F, -1.0F, 0.0F);
        main.addChild(bone3);
        bone3.texOffs(48, 56).addBox(-5.5F, -8.0F, -5.5F, 3.0F, 4.0F, 3.0F, 0.0F, false);
        bone3.texOffs(33, 51).addBox(2.5F, -8.0F, -5.5F, 3.0F, 4.0F, 3.0F, 0.0F, false);
        bone3.texOffs(0, 35).addBox(-5.5F, -8.0F, -3.5F, 11.0F, 4.0F, 9.0F, 0.0F, false);

        bone6 = new ModelRenderer(this);
        bone6.setPos(0.0F, -16.2342F, 0.0F);
        main.addChild(bone6);
        bone6.texOffs(0, 68).addBox(3.0F, 4.7342F, -4.5F, 1.0F, 1.0F, 9.0F, 0.0F, false);
        bone6.texOffs(48, 53).addBox(-4.5F, 4.7342F, -4.0F, 9.0F, 1.0F, 1.0F, 0.0F, false);
        bone6.texOffs(37, 21).addBox(3.0F, -4.5158F, -4.0F, 1.0F, 10.0F, 1.0F, 0.0F, false);
        bone6.texOffs(48, 53).addBox(-4.5F, 4.7342F, 3.0F, 9.0F, 1.0F, 1.0F, 0.0F, false);
        bone6.texOffs(0, 68).addBox(-4.0F, 4.7342F, -4.5F, 1.0F, 1.0F, 9.0F, 0.0F, false);
        bone6.texOffs(37, 21).addBox(-4.0F, -4.5158F, -4.0F, 1.0F, 10.0F, 1.0F, 0.0F, false);
        bone6.texOffs(37, 21).addBox(3.0F, -4.5158F, 3.0F, 1.0F, 10.0F, 1.0F, 0.0F, false);
        bone6.texOffs(37, 21).addBox(-4.0F, -4.5158F, 3.0F, 1.0F, 10.0F, 1.0F, 0.0F, false);
        bone6.texOffs(46, 10).addBox(-4.5F, -3.7658F, 3.0F, 9.0F, 1.0F, 1.0F, 0.0F, false);
        bone6.texOffs(65, 53).addBox(-4.0F, -3.7658F, -6.5F, 1.0F, 1.0F, 13.0F, 0.0F, false);
        bone6.texOffs(65, 53).addBox(3.0F, -3.7658F, -6.5F, 1.0F, 1.0F, 13.0F, 0.0F, false);
        bone6.texOffs(46, 10).addBox(-4.5F, -3.7658F, -4.0F, 9.0F, 1.0F, 1.0F, 0.0F, false);
        bone6.texOffs(46, 0).addBox(-4.5F, -1.7658F, -4.5F, 9.0F, 0.5F, 9.0F, 0.0F, false);
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