package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built;

import com.mojang.math.Vector3f;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;

public class GeoVertex {
    public final Vector3f position;
    public final float textureU;
    public final float textureV;

    public GeoVertex(float x, float y, float z) {
        this.position = new Vector3f(x, y, z);
        this.textureU = 0;
        this.textureV = 0;
    }

    public GeoVertex(double x, double y, double z) {
        this.position = new Vector3f((float) x, (float) y, (float) z);
        this.textureU = 0;
        this.textureV = 0;
    }

    public GeoVertex(Vector3f posIn, float texU, float texV) {
        this.position = new Vector3f(posIn.x(), posIn.y(), posIn.z());
        this.textureU = texU;
        this.textureV = texV;
    }

    public GeoVertex setTextureUV(float texU, float texV) {
        return new GeoVertex(this.position, texU, texV);
    }

    public GeoVertex setTextureUV(double[] array) {
        Validate.validIndex(ArrayUtils.toObject(array), 1);
        return new GeoVertex(this.position, (float) array[0], (float) array[1]);
    }
}