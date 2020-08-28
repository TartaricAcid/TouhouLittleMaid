package com.github.tartaricacid.touhoulittlemaid.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class BookDeskModel extends ModelBase {
    private final ModelRenderer bone;
    private final ModelRenderer bone4;
    private final ModelRenderer bone3;
    private final ModelRenderer book11;
    private final ModelRenderer book12;
    private final ModelRenderer book13;
    private final ModelRenderer book14;
    private final ModelRenderer book15;
    private final ModelRenderer book16;
    private final ModelRenderer book17;
    private final ModelRenderer book18;
    private final ModelRenderer book19;
    private final ModelRenderer book20;
    private final ModelRenderer bone2;
    private final ModelRenderer book;
    private final ModelRenderer book10;
    private final ModelRenderer book7;
    private final ModelRenderer book8;
    private final ModelRenderer book9;
    private final ModelRenderer book2;
    private final ModelRenderer book3;
    private final ModelRenderer book4;
    private final ModelRenderer book5;
    private final ModelRenderer book6;
    private final ModelRenderer bone5;
    private final ModelRenderer bone6;
    private final ModelRenderer bone7;
    private final ModelRenderer book21;
    private final ModelRenderer book22;
    private final ModelRenderer book24;
    private final ModelRenderer book27;
    private final ModelRenderer book30;
    private final ModelRenderer bone8;
    private final ModelRenderer bone10;
    private final ModelRenderer bone9;
    private final ModelRenderer bone11;
    private final ModelRenderer book23;
    private final ModelRenderer book25;
    private final ModelRenderer book26;
    private final ModelRenderer book28;
    private final ModelRenderer book29;
    private final ModelRenderer bone12;
    private final ModelRenderer bone13;
    private final ModelRenderer bone14;
    private final ModelRenderer bone15;
    private final ModelRenderer bone16;
    private final ModelRenderer side;
    private final ModelRenderer bone17;
    private final ModelRenderer bone18;
    private final ModelRenderer side2;
    private final ModelRenderer bone19;
    private final ModelRenderer bone20;

    public BookDeskModel() {
        textureWidth = 256;
        textureHeight = 256;

        bone = new ModelRenderer(this);
        bone.setRotationPoint(-4.0F, 24.0F, 0.0F);
        bone.cubeList.add(new ModelBox(bone, 45, 109, -22.0F, -15.0F, 5.0F, 2, 15, 2, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 92, 108, -22.0F, -15.0F, -22.0F, 2, 15, 2, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 71, 108, 20.0F, -15.0F, -22.0F, 2, 15, 2, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 14, 108, 20.0F, -15.0F, -10.0F, 2, 15, 2, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 37, 99, -11.0F, -15.0F, 5.0F, 2, 15, 2, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 0, -24.0F, -17.0F, -24.0F, 48, 2, 18, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 20, -24.0F, -17.0F, -6.0F, 17, 2, 15, 0.0F, false));

        bone4 = new ModelRenderer(this);
        bone4.setRotationPoint(-20.0F, 7.1F, -7.7F);
        setRotationAngle(bone4, 0.0F, 1.5708F, 0.0F);


        bone3 = new ModelRenderer(this);
        bone3.setRotationPoint(23.7F, 17.0F, 17.6F);
        bone4.addChild(bone3);


        book11 = new ModelRenderer(this);
        book11.setRotationPoint(-19.0F, -17.1F, -18.2F);
        bone3.addChild(book11);
        book11.cubeList.add(new ModelBox(book11, 121, 67, 0.3F, -6.6F, -2.2F, 1, 7, 5, -0.4F, false));
        book11.cubeList.add(new ModelBox(book11, 60, 120, -0.7F, -6.6F, -2.2F, 1, 7, 5, -0.4F, false));
        book11.cubeList.add(new ModelBox(book11, 23, 99, -0.75F, -6.6F, -2.1F, 2, 7, 5, -0.5F, false));
        book11.cubeList.add(new ModelBox(book11, 41, 126, -0.7F, -6.6F, 2.0F, 2, 7, 1, -0.4F, false));

        book12 = new ModelRenderer(this);
        book12.setRotationPoint(-14.2F, -17.1F, -18.2F);
        bone3.addChild(book12);
        book12.cubeList.add(new ModelBox(book12, 119, 20, 0.3F, -6.6F, -2.2F, 1, 7, 5, -0.4F, false));
        book12.cubeList.add(new ModelBox(book12, 29, 118, -0.7F, -6.6F, -2.2F, 1, 7, 5, -0.4F, false));
        book12.cubeList.add(new ModelBox(book12, 80, 98, -0.75F, -6.6F, -2.1F, 2, 7, 5, -0.5F, false));
        book12.cubeList.add(new ModelBox(book12, 95, 41, -0.7F, -6.6F, 2.0F, 2, 7, 1, -0.4F, false));

        book13 = new ModelRenderer(this);
        book13.setRotationPoint(-21.2F, -17.1F, -18.2F);
        bone3.addChild(book13);
        book13.cubeList.add(new ModelBox(book13, 131, 26, 0.12F, -5.6F, -1.68F, 1, 6, 4, -0.4F, false));
        book13.cubeList.add(new ModelBox(book13, 131, 16, -0.88F, -5.6F, -1.68F, 1, 6, 4, -0.4F, false));
        book13.cubeList.add(new ModelBox(book13, 19, 126, -0.9F, -5.58F, -1.58F, 2, 6, 4, -0.5F, false));
        book13.cubeList.add(new ModelBox(book13, 100, 128, -0.88F, -5.6F, 1.53F, 2, 6, 1, -0.4F, false));

        book14 = new ModelRenderer(this);
        book14.setRotationPoint(-17.6F, -17.1F, -18.2F);
        bone3.addChild(book14);
        book14.cubeList.add(new ModelBox(book14, 131, 131, 0.12F, -5.6F, -1.68F, 1, 6, 4, -0.4F, false));
        book14.cubeList.add(new ModelBox(book14, 115, 131, -0.88F, -5.6F, -1.68F, 1, 6, 4, -0.4F, false));
        book14.cubeList.add(new ModelBox(book14, 125, 89, -0.9F, -5.58F, -1.58F, 2, 6, 4, -0.5F, false));
        book14.cubeList.add(new ModelBox(book14, 36, 116, -0.88F, -5.6F, 1.53F, 2, 6, 1, -0.4F, false));

        book15 = new ModelRenderer(this);
        book15.setRotationPoint(-22.4F, -17.1F, -18.2F);
        bone3.addChild(book15);
        book15.cubeList.add(new ModelBox(book15, 94, 131, 0.12F, -5.6F, -1.68F, 1, 6, 4, -0.4F, false));
        book15.cubeList.add(new ModelBox(book15, 130, 0, -0.88F, -5.6F, -1.68F, 1, 6, 4, -0.4F, false));
        book15.cubeList.add(new ModelBox(book15, 86, 125, -0.9F, -5.58F, -1.58F, 2, 6, 4, -0.5F, false));
        book15.cubeList.add(new ModelBox(book15, 108, 63, -0.88F, -5.6F, 1.53F, 2, 6, 1, -0.4F, false));

        book16 = new ModelRenderer(this);
        book16.setRotationPoint(-20.2F, -17.1F, -18.2F);
        bone3.addChild(book16);
        book16.cubeList.add(new ModelBox(book16, 117, 39, 0.3F, -6.6F, -2.2F, 1, 7, 5, -0.4F, false));
        book16.cubeList.add(new ModelBox(book16, 116, 102, -0.7F, -6.6F, -2.2F, 1, 7, 5, -0.4F, false));
        book16.cubeList.add(new ModelBox(book16, 59, 98, -0.75F, -6.6F, -2.1F, 2, 7, 5, -0.5F, false));
        book16.cubeList.add(new ModelBox(book16, 90, 80, -0.7F, -6.6F, 2.0F, 2, 7, 1, -0.4F, false));

        book17 = new ModelRenderer(this);
        book17.setRotationPoint(-16.6F, -17.1F, -18.2F);
        bone3.addChild(book17);
        book17.cubeList.add(new ModelBox(book17, 116, 55, 0.3F, -6.6F, -2.2F, 1, 7, 5, -0.4F, false));
        book17.cubeList.add(new ModelBox(book17, 116, 116, -0.7F, -6.6F, -2.2F, 1, 7, 5, -0.4F, false));
        book17.cubeList.add(new ModelBox(book17, 97, 75, -0.75F, -6.6F, -2.1F, 2, 7, 5, -0.5F, false));
        book17.cubeList.add(new ModelBox(book17, 87, 34, -0.7F, -6.6F, 2.0F, 2, 7, 1, -0.4F, false));

        book18 = new ModelRenderer(this);
        book18.setRotationPoint(-15.6F, -17.1F, -18.3F);
        bone3.addChild(book18);
        book18.cubeList.add(new ModelBox(book18, 82, 82, 0.48F, -7.6F, -2.72F, 1, 8, 6, -0.4F, false));
        book18.cubeList.add(new ModelBox(book18, 62, 82, -0.52F, -7.6F, -2.72F, 1, 8, 6, -0.4F, false));
        book18.cubeList.add(new ModelBox(book18, 48, 59, -0.6F, -7.62F, -2.62F, 2, 8, 6, -0.5F, false));
        book18.cubeList.add(new ModelBox(book18, 125, 135, -0.52F, -7.6F, 2.47F, 2, 8, 1, -0.4F, false));

        book19 = new ModelRenderer(this);
        book19.setRotationPoint(-23.8F, -17.1F, -18.3F);
        bone3.addChild(book19);
        book19.cubeList.add(new ModelBox(book19, 80, 58, 0.48F, -7.6F, -2.72F, 1, 8, 6, -0.4F, false));
        book19.cubeList.add(new ModelBox(book19, 8, 79, -0.52F, -7.6F, -2.72F, 1, 8, 6, -0.4F, false));
        book19.cubeList.add(new ModelBox(book19, 16, 59, -0.6F, -7.62F, -2.62F, 2, 8, 6, -0.5F, false));
        book19.cubeList.add(new ModelBox(book19, 104, 135, -0.52F, -7.6F, 2.47F, 2, 8, 1, -0.4F, false));

        book20 = new ModelRenderer(this);
        book20.setRotationPoint(-13.2F, -17.1F, -18.3F);
        bone3.addChild(book20);
        book20.cubeList.add(new ModelBox(book20, 74, 74, 0.48F, -7.6F, -2.72F, 1, 8, 6, -0.4F, false));
        book20.cubeList.add(new ModelBox(book20, 54, 73, -0.52F, -7.6F, -2.72F, 1, 8, 6, -0.4F, false));
        book20.cubeList.add(new ModelBox(book20, 32, 57, -0.6F, -7.62F, -2.62F, 2, 8, 6, -0.5F, false));
        book20.cubeList.add(new ModelBox(book20, 84, 135, -0.52F, -7.6F, 2.47F, 2, 8, 1, -0.4F, false));

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(11.9F, 17.0F, 17.6F);
        bone4.addChild(bone2);


        book = new ModelRenderer(this);
        book.setRotationPoint(-19.0F, -17.1F, -18.2F);
        bone2.addChild(book);
        book.cubeList.add(new ModelBox(book, 124, 46, 0.3F, -6.6F, -2.2F, 1, 7, 5, -0.4F, false));
        book.cubeList.add(new ModelBox(book, 124, 32, -0.7F, -6.6F, -2.2F, 1, 7, 5, -0.4F, false));
        book.cubeList.add(new ModelBox(book, 103, 36, -0.75F, -6.6F, -2.1F, 2, 7, 5, -0.5F, false));
        book.cubeList.add(new ModelBox(book, 137, 95, -0.7F, -6.6F, 2.0F, 2, 7, 1, -0.4F, false));

        book10 = new ModelRenderer(this);
        book10.setRotationPoint(-14.2F, -17.1F, -18.2F);
        bone2.addChild(book10);
        book10.cubeList.add(new ModelBox(book10, 48, 122, 0.3F, -6.6F, -2.2F, 1, 7, 5, -0.4F, false));
        book10.cubeList.add(new ModelBox(book10, 7, 122, -0.7F, -6.6F, -2.2F, 1, 7, 5, -0.4F, false));
        book10.cubeList.add(new ModelBox(book10, 100, 100, -0.75F, -6.6F, -2.1F, 2, 7, 5, -0.5F, false));
        book10.cubeList.add(new ModelBox(book10, 135, 109, -0.7F, -6.6F, 2.0F, 2, 7, 1, -0.4F, false));

        book7 = new ModelRenderer(this);
        book7.setRotationPoint(-21.2F, -17.1F, -18.2F);
        bone2.addChild(book7);
        book7.cubeList.add(new ModelBox(book7, 43, 134, 0.12F, -5.6F, -1.68F, 1, 6, 4, -0.4F, false));
        book7.cubeList.add(new ModelBox(book7, 10, 134, -0.88F, -5.6F, -1.68F, 1, 6, 4, -0.4F, false));
        book7.cubeList.add(new ModelBox(book7, 31, 130, -0.9F, -5.58F, -1.58F, 2, 6, 4, -0.5F, false));
        book7.cubeList.add(new ModelBox(book7, 26, 139, -0.88F, -5.6F, 1.53F, 2, 6, 1, -0.4F, false));

        book8 = new ModelRenderer(this);
        book8.setRotationPoint(-17.6F, -17.1F, -18.2F);
        bone2.addChild(book8);
        book8.cubeList.add(new ModelBox(book8, 133, 68, 0.12F, -5.6F, -1.68F, 1, 6, 4, -0.4F, false));
        book8.cubeList.add(new ModelBox(book8, 132, 40, -0.88F, -5.6F, -1.68F, 1, 6, 4, -0.4F, false));
        book8.cubeList.add(new ModelBox(book8, 128, 99, -0.9F, -5.58F, -1.58F, 2, 6, 4, -0.5F, false));
        book8.cubeList.add(new ModelBox(book8, 138, 36, -0.88F, -5.6F, 1.53F, 2, 6, 1, -0.4F, false));

        book9 = new ModelRenderer(this);
        book9.setRotationPoint(-22.4F, -17.1F, -18.2F);
        bone2.addChild(book9);
        book9.cubeList.add(new ModelBox(book9, 56, 132, 0.12F, -5.6F, -1.68F, 1, 6, 4, -0.4F, false));
        book9.cubeList.add(new ModelBox(book9, 131, 117, -0.88F, -5.6F, -1.68F, 1, 6, 4, -0.4F, false));
        book9.cubeList.add(new ModelBox(book9, 128, 58, -0.9F, -5.58F, -1.58F, 2, 6, 4, -0.5F, false));
        book9.cubeList.add(new ModelBox(book9, 137, 127, -0.88F, -5.6F, 1.53F, 2, 6, 1, -0.4F, false));

        book2 = new ModelRenderer(this);
        book2.setRotationPoint(-20.2F, -17.1F, -18.2F);
        bone2.addChild(book2);
        book2.cubeList.add(new ModelBox(book2, 123, 109, 0.3F, -6.6F, -2.2F, 1, 7, 5, -0.4F, false));
        book2.cubeList.add(new ModelBox(book2, 123, 123, -0.7F, -6.6F, -2.2F, 1, 7, 5, -0.4F, false));
        book2.cubeList.add(new ModelBox(book2, 103, 20, -0.75F, -6.6F, -2.1F, 2, 7, 5, -0.5F, false));
        book2.cubeList.add(new ModelBox(book2, 137, 87, -0.7F, -6.6F, 2.0F, 2, 7, 1, -0.4F, false));

        book3 = new ModelRenderer(this);
        book3.setRotationPoint(-16.6F, -17.1F, -18.2F);
        bone2.addChild(book3);
        book3.cubeList.add(new ModelBox(book3, 107, 123, 0.3F, -6.6F, -2.2F, 1, 7, 5, -0.4F, false));
        book3.cubeList.add(new ModelBox(book3, 74, 122, -0.7F, -6.6F, -2.2F, 1, 7, 5, -0.4F, false));
        book3.cubeList.add(new ModelBox(book3, 0, 103, -0.75F, -6.6F, -2.1F, 2, 7, 5, -0.5F, false));
        book3.cubeList.add(new ModelBox(book3, 137, 10, -0.7F, -6.6F, 2.0F, 2, 7, 1, -0.4F, false));

        book4 = new ModelRenderer(this);
        book4.setRotationPoint(-15.6F, -17.1F, -18.3F);
        bone2.addChild(book4);
        book4.cubeList.add(new ModelBox(book4, 88, 66, 0.48F, -7.6F, -2.72F, 1, 8, 6, -0.4F, false));
        book4.cubeList.add(new ModelBox(book4, 0, 87, -0.52F, -7.6F, -2.72F, 1, 8, 6, -0.4F, false));
        book4.cubeList.add(new ModelBox(book4, 69, 23, -0.6F, -7.62F, -2.62F, 2, 8, 6, -0.5F, false));
        book4.cubeList.add(new ModelBox(book4, 136, 78, -0.52F, -7.6F, 2.47F, 2, 8, 1, -0.4F, false));

        book5 = new ModelRenderer(this);
        book5.setRotationPoint(-23.8F, -17.1F, -18.3F);
        bone2.addChild(book5);
        book5.cubeList.add(new ModelBox(book5, 86, 43, 0.48F, -7.6F, -2.72F, 1, 8, 6, -0.4F, false));
        book5.cubeList.add(new ModelBox(book5, 85, 20, -0.52F, -7.6F, -2.72F, 1, 8, 6, -0.4F, false));
        book5.cubeList.add(new ModelBox(book5, 64, 64, -0.6F, -7.62F, -2.62F, 2, 8, 6, -0.5F, false));
        book5.cubeList.add(new ModelBox(book5, 136, 50, -0.52F, -7.6F, 2.47F, 2, 8, 1, -0.4F, false));

        book6 = new ModelRenderer(this);
        book6.setRotationPoint(-13.2F, -17.1F, -18.3F);
        bone2.addChild(book6);
        book6.cubeList.add(new ModelBox(book6, 36, 85, 0.48F, -7.6F, -2.72F, 1, 8, 6, -0.4F, false));
        book6.cubeList.add(new ModelBox(book6, 22, 85, -0.52F, -7.6F, -2.72F, 1, 8, 6, -0.4F, false));
        book6.cubeList.add(new ModelBox(book6, 60, 43, -0.6F, -7.62F, -2.62F, 2, 8, 6, -0.5F, false));
        book6.cubeList.add(new ModelBox(book6, 20, 136, -0.52F, -7.6F, 2.47F, 2, 8, 1, -0.4F, false));

        bone5 = new ModelRenderer(this);
        bone5.setRotationPoint(0.3F, 0.2F, 0.0F);
        bone4.addChild(bone5);
        bone5.cubeList.add(new ModelBox(bone5, 100, 87, -15.0F, -1.0F, -3.5F, 3, 1, 6, -0.3F, false));
        bone5.cubeList.add(new ModelBox(bone5, 0, 85, -13.0F, -6.0F, -2.9F, 1, 6, 2, -0.3F, false));
        bone5.cubeList.add(new ModelBox(bone5, 22, 83, -13.0F, -6.0F, -0.1F, 1, 6, 2, -0.3F, false));
        bone5.cubeList.add(new ModelBox(bone5, 8, 20, -13.0F, -6.0F, -1.5F, 1, 2, 2, -0.3F, false));

        bone6 = new ModelRenderer(this);
        bone6.setRotationPoint(-0.8F, 0.2F, 0.0F);
        bone4.addChild(bone6);
        bone6.cubeList.add(new ModelBox(bone6, 96, 64, 12.0F, -1.0F, -3.5F, 3, 1, 6, -0.3F, false));
        bone6.cubeList.add(new ModelBox(bone6, 82, 72, 12.0F, -6.0F, -2.9F, 1, 6, 2, -0.3F, false));
        bone6.cubeList.add(new ModelBox(bone6, 79, 20, 12.0F, -6.0F, -0.1F, 1, 6, 2, -0.3F, false));
        bone6.cubeList.add(new ModelBox(bone6, 0, 20, 12.0F, -6.0F, -1.5F, 1, 2, 2, -0.3F, false));

        bone7 = new ModelRenderer(this);
        bone7.setRotationPoint(8.2F, 1.3F, -17.2F);
        setRotationAngle(bone7, 0.1745F, 0.0F, 1.5708F);


        book21 = new ModelRenderer(this);
        book21.setRotationPoint(2.5875F, -3.1F, 2.575F);
        bone7.addChild(book21);
        setRotationAngle(book21, 0.1745F, 0.0F, 0.0F);
        book21.cubeList.add(new ModelBox(book21, 100, 116, 0.0125F, -3.5F, -3.075F, 1, 7, 5, -0.4F, false));
        book21.cubeList.add(new ModelBox(book21, 0, 115, -0.9875F, -3.5F, -3.075F, 1, 7, 5, -0.4F, false));
        book21.cubeList.add(new ModelBox(book21, 45, 97, -1.0375F, -3.5F, -2.975F, 2, 7, 5, -0.5F, false));
        book21.cubeList.add(new ModelBox(book21, 78, 49, -0.9875F, -3.5F, 1.125F, 2, 7, 1, -0.4F, false));

        book22 = new ModelRenderer(this);
        book22.setRotationPoint(3.7875F, -3.1F, 2.575F);
        bone7.addChild(book22);
        setRotationAngle(book22, -0.0785F, 0.0F, 0.0F);
        book22.cubeList.add(new ModelBox(book22, 114, 0, 0.0125F, -3.5F, -3.075F, 1, 7, 5, -0.4F, false));
        book22.cubeList.add(new ModelBox(book22, 113, 90, -0.9875F, -3.5F, -3.075F, 1, 7, 5, -0.4F, false));
        book22.cubeList.add(new ModelBox(book22, 9, 96, -1.0375F, -3.5F, -2.975F, 2, 7, 5, -0.5F, false));
        book22.cubeList.add(new ModelBox(book22, 50, 47, -0.9875F, -3.5F, 1.125F, 2, 7, 1, -0.4F, false));

        book24 = new ModelRenderer(this);
        book24.setRotationPoint(0.215F, -2.595F, 2.4725F);
        bone7.addChild(book24);
        setRotationAngle(book24, 0.1309F, 0.0F, 0.0F);
        book24.cubeList.add(new ModelBox(book24, 68, 130, 0.005F, -3.005F, -2.4525F, 1, 6, 4, -0.4F, false));
        book24.cubeList.add(new ModelBox(book24, 0, 130, -0.995F, -3.005F, -2.4525F, 1, 6, 4, -0.4F, false));
        book24.cubeList.add(new ModelBox(book24, 124, 79, -1.015F, -2.985F, -2.3525F, 2, 6, 4, -0.5F, false));
        book24.cubeList.add(new ModelBox(book24, 88, 57, -0.995F, -3.005F, 0.7575F, 2, 6, 1, -0.4F, false));

        book27 = new ModelRenderer(this);
        book27.setRotationPoint(1.3875F, -3.1F, 2.575F);
        bone7.addChild(book27);
        setRotationAngle(book27, -0.1745F, 0.0F, 0.0F);
        book27.cubeList.add(new ModelBox(book27, 112, 78, 0.0125F, -3.5F, -3.075F, 1, 7, 5, -0.4F, false));
        book27.cubeList.add(new ModelBox(book27, 112, 27, -0.9875F, -3.5F, -3.075F, 1, 7, 5, -0.4F, false));
        book27.cubeList.add(new ModelBox(book27, 95, 52, -1.0375F, -3.5F, -2.975F, 2, 7, 5, -0.5F, false));
        book27.cubeList.add(new ModelBox(book27, 49, 20, -0.9875F, -3.5F, 1.125F, 2, 7, 1, -0.4F, false));

        book30 = new ModelRenderer(this);
        book30.setRotationPoint(4.96F, -3.605F, 2.5775F);
        bone7.addChild(book30);
        setRotationAngle(book30, 0.0873F, 0.0F, 0.0F);
        book30.cubeList.add(new ModelBox(book30, 40, 71, 0.02F, -3.995F, -3.6975F, 1, 8, 6, -0.4F, false));
        book30.cubeList.add(new ModelBox(book30, 26, 71, -0.98F, -3.995F, -3.6975F, 1, 8, 6, -0.4F, false));
        book30.cubeList.add(new ModelBox(book30, 0, 57, -1.06F, -4.015F, -3.5975F, 2, 8, 6, -0.5F, false));
        book30.cubeList.add(new ModelBox(book30, 78, 134, -0.98F, -3.995F, 1.4925F, 2, 8, 1, -0.4F, false));

        bone8 = new ModelRenderer(this);
        bone8.setRotationPoint(1.5F, 24.4F, -1.0F);


        bone10 = new ModelRenderer(this);
        bone10.setRotationPoint(-1.2F, -17.75F, -11.0F);
        bone8.addChild(bone10);
        setRotationAngle(bone10, 0.0F, 0.0F, -0.0436F);
        bone10.cubeList.add(new ModelBox(bone10, 0, 48, -0.4F, -0.25F, -4.0F, 5, 1, 8, -0.3F, false));
        bone10.cubeList.add(new ModelBox(bone10, 32, 47, -0.6F, -1.25F, -4.0F, 5, 2, 8, -0.5F, false));

        bone9 = new ModelRenderer(this);
        bone9.setRotationPoint(-1.4F, -17.75F, -11.0F);
        bone8.addChild(bone9);
        setRotationAngle(bone9, 0.0F, 0.0F, 0.0436F);
        bone9.cubeList.add(new ModelBox(bone9, 30, 37, -4.4F, -1.25F, -4.0F, 5, 2, 8, -0.5F, false));
        bone9.cubeList.add(new ModelBox(bone9, 49, 20, -4.6F, -0.25F, -4.0F, 5, 1, 8, -0.3F, false));

        bone11 = new ModelRenderer(this);
        bone11.setRotationPoint(-14.8F, 1.3F, -13.2F);
        setRotationAngle(bone11, 1.1781F, 0.0F, 1.5708F);


        book23 = new ModelRenderer(this);
        book23.setRotationPoint(2.5875F, -3.1F, 2.575F);
        bone11.addChild(book23);
        setRotationAngle(book23, 0.0873F, 0.0F, 0.0F);
        book23.cubeList.add(new ModelBox(book23, 22, 111, 0.0125F, -3.5F, -3.075F, 1, 7, 5, -0.4F, false));
        book23.cubeList.add(new ModelBox(book23, 79, 110, -0.9875F, -3.5F, -3.075F, 1, 7, 5, -0.4F, false));
        book23.cubeList.add(new ModelBox(book23, 94, 29, -1.0375F, -3.5F, -2.975F, 2, 7, 5, -0.5F, false));
        book23.cubeList.add(new ModelBox(book23, 18, 48, -0.9875F, -3.5F, 1.125F, 2, 7, 1, -0.4F, false));

        book25 = new ModelRenderer(this);
        book25.setRotationPoint(3.7875F, -3.1F, 2.575F);
        bone11.addChild(book25);
        setRotationAngle(book25, -0.0785F, 0.0F, 0.0F);
        book25.cubeList.add(new ModelBox(book25, 53, 110, 0.0125F, -3.5F, -3.075F, 1, 7, 5, -0.4F, false));
        book25.cubeList.add(new ModelBox(book25, 109, 66, -0.9875F, -3.5F, -3.075F, 1, 7, 5, -0.4F, false));
        book25.cubeList.add(new ModelBox(book25, 91, 91, -1.0375F, -3.5F, -2.975F, 2, 7, 5, -0.5F, false));
        book25.cubeList.add(new ModelBox(book25, 0, 48, -0.9875F, -3.5F, 1.125F, 2, 7, 1, -0.4F, false));

        book26 = new ModelRenderer(this);
        book26.setRotationPoint(0.215F, -2.595F, 2.4725F);
        bone11.addChild(book26);
        setRotationAngle(book26, 0.1309F, 0.0F, 0.0F);
        book26.cubeList.add(new ModelBox(book26, 16, 73, 0.005F, -3.005F, -2.4525F, 1, 6, 4, -0.4F, false));
        book26.cubeList.add(new ModelBox(book26, 0, 37, -0.995F, -3.005F, -2.4525F, 1, 6, 4, -0.4F, false));
        book26.cubeList.add(new ModelBox(book26, 122, 8, -1.015F, -2.985F, -2.3525F, 2, 6, 4, -0.5F, false));
        book26.cubeList.add(new ModelBox(book26, 32, 48, -0.995F, -3.005F, 0.7575F, 2, 6, 1, -0.4F, false));

        book28 = new ModelRenderer(this);
        book28.setRotationPoint(1.3875F, -3.1F, 2.575F);
        bone11.addChild(book28);
        setRotationAngle(book28, -0.0436F, 0.0F, 0.0F);
        book28.cubeList.add(new ModelBox(book28, 109, 48, 0.0125F, -3.5F, -3.075F, 1, 7, 5, -0.4F, false));
        book28.cubeList.add(new ModelBox(book28, 109, 109, -0.9875F, -3.5F, -3.075F, 1, 7, 5, -0.4F, false));
        book28.cubeList.add(new ModelBox(book28, 71, 91, -1.0375F, -3.5F, -2.975F, 2, 7, 5, -0.5F, false));
        book28.cubeList.add(new ModelBox(book28, 30, 37, -0.9875F, -3.5F, 1.125F, 2, 7, 1, -0.4F, false));

        book29 = new ModelRenderer(this);
        book29.setRotationPoint(4.96F, -3.605F, 2.5775F);
        bone11.addChild(book29);
        setRotationAngle(book29, -0.1309F, 0.0F, 0.0F);
        book29.cubeList.add(new ModelBox(book29, 0, 71, 0.02F, -3.995F, -3.6975F, 1, 8, 6, -0.4F, false));
        book29.cubeList.add(new ModelBox(book29, 0, 20, -0.98F, -3.995F, -3.6975F, 1, 8, 6, -0.4F, false));
        book29.cubeList.add(new ModelBox(book29, 0, 0, -1.06F, -4.015F, -3.5975F, 2, 8, 6, -0.5F, false));
        book29.cubeList.add(new ModelBox(book29, 65, 110, -0.98F, -3.995F, 1.4925F, 2, 8, 1, -0.4F, false));

        bone12 = new ModelRenderer(this);
        bone12.setRotationPoint(0.0F, 24.4F, 1.0F);
        bone12.cubeList.add(new ModelBox(bone12, 49, 29, -4.0F, -18.0F, -23.0F, 6, 1, 4, 0.0F, false));

        bone13 = new ModelRenderer(this);
        bone13.setRotationPoint(-1.0F, -17.5F, -22.0F);
        bone12.addChild(bone13);
        setRotationAngle(bone13, -0.1745F, 0.0F, 0.0F);
        bone13.cubeList.add(new ModelBox(bone13, 50, 85, -0.5F, -10.5F, -0.5F, 1, 10, 1, 0.0F, false));

        bone14 = new ModelRenderer(this);
        bone14.setRotationPoint(-1.5F, -9.5521F, 0.0954F);
        bone13.addChild(bone14);
        setRotationAngle(bone14, 0.3491F, 0.0F, 0.0F);
        bone14.cubeList.add(new ModelBox(bone14, 50, 88, 1.0F, -1.0F, -0.3F, 1, 1, 8, -0.1F, false));

        bone15 = new ModelRenderer(this);
        bone15.setRotationPoint(1.5F, -0.0009F, 8.3607F);
        bone14.addChild(bone15);
        setRotationAngle(bone15, -0.3054F, 0.0F, 0.0F);
        bone15.cubeList.add(new ModelBox(bone15, 58, 59, -4.5F, -1.25F, -1.5F, 9, 2, 3, -0.5F, false));
        bone15.cubeList.add(new ModelBox(bone15, 48, 37, -4.5F, -1.25F, -1.5F, 9, 3, 3, -0.8F, false));

        bone16 = new ModelRenderer(this);
        bone16.setRotationPoint(0.0F, 24.0F, 0.0F);
        bone16.cubeList.add(new ModelBox(bone16, 0, 37, -5.0F, -7.0F, -5.0F, 10, 1, 10, 0.0F, false));
        bone16.cubeList.add(new ModelBox(bone16, 70, 43, -5.0F, -17.3F, 5.4142F, 10, 5, 1, 0.0F, false));
        bone16.cubeList.add(new ModelBox(bone16, 48, 43, -3.5F, -1.5F, -0.5F, 7, 1, 1, 0.0F, false));
        bone16.cubeList.add(new ModelBox(bone16, 67, 20, -3.0F, -16.8284F, 6.4142F, 6, 1, 1, 0.0F, false));
        bone16.cubeList.add(new ModelBox(bone16, 0, 16, -3.5F, -6.0F, 3.5F, 7, 1, 1, 0.0F, false));
        bone16.cubeList.add(new ModelBox(bone16, 0, 14, -3.5F, -13.8F, 6.4142F, 7, 1, 1, 0.0F, false));

        side = new ModelRenderer(this);
        side.setRotationPoint(3.0F, 0.0F, -3.0F);
        bone16.addChild(side);
        side.cubeList.add(new ModelBox(side, 10, 0, -7.4142F, -5.0F, 2.0F, 1, 4, 2, 0.0F, false));
        side.cubeList.add(new ModelBox(side, 48, 48, -7.4142F, -6.0F, -1.0F, 1, 1, 10, 0.0F, false));
        side.cubeList.add(new ModelBox(side, 70, 78, -7.4142F, -15.4142F, 9.4142F, 1, 9, 1, 0.0F, false));
        side.cubeList.add(new ModelBox(side, 77, 34, -7.415F, -1.5F, -1.0F, 1, 1, 8, 0.0F, false));
        side.cubeList.add(new ModelBox(side, 0, 57, -7.4142F, -1.0F, -1.5F, 1, 1, 1, 0.0F, false));
        side.cubeList.add(new ModelBox(side, 8, 24, -7.4142F, -1.0F, 6.5F, 1, 1, 1, 0.0F, false));

        bone17 = new ModelRenderer(this);
        bone17.setRotationPoint(0.0F, 0.0F, -3.0F);
        side.addChild(bone17);
        setRotationAngle(bone17, 0.0F, 0.0F, 0.7854F);
        bone17.cubeList.add(new ModelBox(bone17, 56, 45, -16.1421F, -7.6569F, 12.4142F, 1, 2, 1, 0.0F, false));

        bone18 = new ModelRenderer(this);
        bone18.setRotationPoint(0.0F, 0.0F, -3.0F);
        side.addChild(bone18);
        setRotationAngle(bone18, -0.7854F, 0.0F, 0.0F);
        bone18.cubeList.add(new ModelBox(bone18, 49, 29, -7.4142F, -14.0208F, 3.9497F, 1, 2, 1, 0.0F, false));

        side2 = new ModelRenderer(this);
        side2.setRotationPoint(-3.0F, 0.0F, -3.0F);
        bone16.addChild(side2);
        side2.cubeList.add(new ModelBox(side2, 0, 0, 6.4142F, -5.0F, 2.0F, 1, 4, 2, 0.0F, false));
        side2.cubeList.add(new ModelBox(side2, 16, 48, 6.4142F, -6.0F, -1.0F, 1, 1, 10, 0.0F, false));
        side2.cubeList.add(new ModelBox(side2, 28, 48, 6.4142F, -15.4142F, 9.4142F, 1, 9, 1, 0.0F, false));
        side2.cubeList.add(new ModelBox(side2, 68, 49, 6.415F, -1.5F, -1.0F, 1, 1, 8, 0.0F, false));
        side2.cubeList.add(new ModelBox(side2, 0, 24, 6.4142F, -1.0F, -1.5F, 1, 1, 1, 0.0F, false));
        side2.cubeList.add(new ModelBox(side2, 14, 0, 6.4142F, -1.0F, 6.5F, 1, 1, 1, 0.0F, false));

        bone19 = new ModelRenderer(this);
        bone19.setRotationPoint(0.0F, 0.0F, -3.0F);
        side2.addChild(bone19);
        setRotationAngle(bone19, 0.0F, 0.0F, -0.7854F);
        bone19.cubeList.add(new ModelBox(bone19, 6, 37, 15.1421F, -7.6569F, 12.4142F, 1, 2, 1, 0.0F, false));

        bone20 = new ModelRenderer(this);
        bone20.setRotationPoint(0.0F, 0.0F, -3.0F);
        side2.addChild(bone20);
        setRotationAngle(bone20, -0.7854F, 0.0F, 0.0F);
        bone20.cubeList.add(new ModelBox(bone20, 0, 37, 6.4142F, -14.0208F, 3.9497F, 1, 2, 1, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        bone.render(f5);
        bone4.render(f5);
        bone7.render(f5);
        bone8.render(f5);
        bone11.render(f5);
        bone12.render(f5);
        bone16.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}