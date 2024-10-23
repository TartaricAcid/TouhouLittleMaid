package com.github.tartaricacid.touhoulittlemaid.client.renderer.sections.cache;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.client.model.IQuadTransformer;
import net.neoforged.neoforge.client.textures.UnitTextureAtlasSprite;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Argon4W
 */
public class QuadListBakingVertexConsumer implements VertexConsumer {
    private static final int QUAD_DATA_SIZE = IQuadTransformer.STRIDE * 4;

    private final List<BakedQuad> quads;
    private int tintIndex;
    private Direction direction;
    private TextureAtlasSprite sprite;
    private boolean shade;
    private boolean hasAmbientOcclusion;
    private int vertexIndex;
    private boolean building;
    private final Map<VertexFormatElement, Integer> elementOffsets;
    private final int[] quadData;

    public QuadListBakingVertexConsumer(RenderType renderType) {
        this.quads = new ArrayList<>();
        this.tintIndex = -1;
        this.direction = Direction.DOWN;
        this.sprite = UnitTextureAtlasSprite.INSTANCE;
        this.shade = true;
        this.hasAmbientOcclusion = true;
        this.vertexIndex = 0;
        this.building = false;
        this.elementOffsets = Util.make(new IdentityHashMap<>(), map -> DefaultVertexFormat.BLOCK.getElements().forEach(element -> map.put(element, DefaultVertexFormat.BLOCK.getOffset(element) / 4)));
        this.quadData = new int[QUAD_DATA_SIZE];
    }

    @NotNull
    @Override
    public VertexConsumer addVertex(float x, float y, float z) {
        if (building) {
            if (++vertexIndex == 4) {
                quads.add(bakeQuad());
            }
        }

        building = true;
        int offset = vertexIndex * IQuadTransformer.STRIDE + IQuadTransformer.POSITION;
        quadData[offset] = Float.floatToRawIntBits(x);
        quadData[offset + 1] = Float.floatToRawIntBits(y);
        quadData[offset + 2] = Float.floatToRawIntBits(z);
        return this;
    }

    @NotNull
    @Override
    public VertexConsumer setNormal(float x, float y, float z) {
        int offset = vertexIndex * IQuadTransformer.STRIDE + IQuadTransformer.NORMAL;
        quadData[offset] = ((int) (x * 127.0f) & 0xFF) | (((int) (y * 127.0f) & 0xFF) << 8) | (((int) (z * 127.0f) & 0xFF) << 16);
        return this;
    }

    @NotNull
    @Override
    public VertexConsumer setColor(int r, int g, int b, int a) {
        int offset = vertexIndex * IQuadTransformer.STRIDE + IQuadTransformer.COLOR;
        quadData[offset] = ((a & 0xFF) << 24) | ((b & 0xFF) << 16) | ((g & 0xFF) << 8) | (r & 0xFF);
        return this;
    }

    @NotNull
    @Override
    public VertexConsumer setUv(float u, float v) {
        int offset = vertexIndex * IQuadTransformer.STRIDE + IQuadTransformer.UV0;
        quadData[offset] = Float.floatToRawIntBits(u);
        quadData[offset + 1] = Float.floatToRawIntBits(v);
        return this;
    }

    @NotNull
    @Override
    public VertexConsumer setUv1(int u, int v) {
        if (IQuadTransformer.UV1 >= 0) { // Vanilla doesn't support this, but it may be added by a 3rd party
            int offset = vertexIndex * IQuadTransformer.STRIDE + IQuadTransformer.UV1;
            quadData[offset] = (u & 0xFFFF) | ((v & 0xFFFF) << 16);
        }
        return this;
    }

    @NotNull
    @Override
    public VertexConsumer setUv2(int u, int v) {
        int offset = vertexIndex * IQuadTransformer.STRIDE + IQuadTransformer.UV2;
        quadData[offset] = (u & 0xFFFF) | ((v & 0xFFFF) << 16);
        return this;
    }

    @NotNull
    @Override
    public VertexConsumer misc(VertexFormatElement element, int... rawData) {
        Integer baseOffset = elementOffsets.get(element);
        if (baseOffset != null) {
            int offset = vertexIndex * IQuadTransformer.STRIDE + baseOffset;
            System.arraycopy(rawData, 0, quadData, offset, rawData.length);
        }
        return this;
    }

    public BakedQuad bakeQuad() {
        BakedQuad quad = new BakedQuad(quadData.clone(), tintIndex, direction, sprite, shade, hasAmbientOcclusion);
        vertexIndex = 0;
        building = false;
        Arrays.fill(quadData, 0);

        return quad;
    }

    public List<BakedQuad> getQuads() {
        return quads;
    }

    public void setTintIndex(int tintIndex) {
        this.tintIndex = tintIndex;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setSprite(TextureAtlasSprite sprite) {
        this.sprite = sprite;
    }

    public void setShade(boolean shade) {
        this.shade = shade;
    }

    public void setHasAmbientOcclusion(boolean hasAmbientOcclusion) {
        this.hasAmbientOcclusion = hasAmbientOcclusion;
    }
}
