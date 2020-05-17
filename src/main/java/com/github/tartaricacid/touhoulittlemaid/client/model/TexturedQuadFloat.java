package com.github.tartaricacid.touhoulittlemaid.client.model;

import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author TartaricAcid
 * @date 2019/8/7 23:49
 **/
@SideOnly(Side.CLIENT)
public class TexturedQuadFloat extends TexturedQuad {
    public TexturedQuadFloat(PositionTextureVertex[] vertices, float texcoordU1, float texcoordV1, float texcoordU2, float texcoordV2, float textureWidth, float textureHeight) {
        super(vertices, (int) texcoordU1, (int) texcoordV1, (int) texcoordU2, (int) texcoordV2, textureWidth, textureHeight);
        vertices[0] = vertices[0].setTexturePosition(texcoordU2 / textureWidth, texcoordV1 / textureHeight);
        vertices[1] = vertices[1].setTexturePosition(texcoordU1 / textureWidth, texcoordV1 / textureHeight);
        vertices[2] = vertices[2].setTexturePosition(texcoordU1 / textureWidth, texcoordV2 / textureHeight);
        vertices[3] = vertices[3].setTexturePosition(texcoordU2 / textureWidth, texcoordV2 / textureHeight);
    }
}
