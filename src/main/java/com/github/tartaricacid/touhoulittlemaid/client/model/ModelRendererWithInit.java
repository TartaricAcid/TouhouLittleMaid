package com.github.tartaricacid.touhoulittlemaid.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelRendererWithInit extends ModelRenderer {
    private float initRotX;
    private float initRotY;
    private float initRotZ;

    public ModelRendererWithInit(ModelBase model, String boxNameIn) {
        super(model, boxNameIn);
    }

    public ModelRendererWithInit(ModelBase model) {
        super(model);
    }

    public ModelRendererWithInit(ModelBase model, int texOffX, int texOffY) {
        super(model, texOffX, texOffY);
    }

    void setInitRotationAngle(float x, float y, float z) {
        this.initRotX = x;
        this.initRotY = y;
        this.initRotZ = z;
    }

    public float getInitRotX() {
        return initRotX;
    }

    public float getInitRotY() {
        return initRotY;
    }

    public float getInitRotZ() {
        return initRotZ;
    }
}
