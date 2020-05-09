package com.github.tartaricacid.touhoulittlemaid.client.model;

import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * @author TartaricAcid
 * @date 2019/8/7 23:49
 **/
@SideOnly(Side.CLIENT)
public class TexturedQuadFloat extends TexturedQuad {
    public TexturedQuadFloat(PositionTextureVertex[] vertices, float texcoordU1, float texcoordV1, float texcoordU2, float texcoordV2, float textureWidth, float textureHeight) {
        super(vertices, (int) texcoordU1, (int) texcoordV1, (int) texcoordU2, (int) texcoordV2, textureWidth, textureHeight);
    }
}
