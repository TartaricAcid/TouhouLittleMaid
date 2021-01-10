package com.github.tartaricacid.touhoulittlemaid.client.animation.script;

import net.minecraft.client.model.ModelRenderer;

public class ModelRendererWrapper {
    private ModelRenderer modelRenderer;

    public ModelRendererWrapper(ModelRenderer modelRenderer) {
        this.modelRenderer = modelRenderer;
    }

    public float getRotateAngleX() {
        return modelRenderer.rotateAngleX;
    }

    public void setRotateAngleX(float rotateAngleX) {
        modelRenderer.rotateAngleX = rotateAngleX;
    }

    public float getRotateAngleY() {
        return modelRenderer.rotateAngleY;
    }

    public void setRotateAngleY(float rotateAngleY) {
        modelRenderer.rotateAngleY = rotateAngleY;
    }

    public float getRotateAngleZ() {
        return modelRenderer.rotateAngleZ;
    }

    public void setRotateAngleZ(float rotateAngleZ) {
        modelRenderer.rotateAngleZ = rotateAngleZ;
    }

    public float getOffsetX() {
        return modelRenderer.offsetX;
    }

    public void setOffsetX(float offsetX) {
        modelRenderer.offsetX = offsetX;
    }

    public float getOffsetY() {
        return modelRenderer.offsetY;
    }

    public void setOffsetY(float offsetY) {
        modelRenderer.offsetY = offsetY;
    }

    public float getOffsetZ() {
        return modelRenderer.offsetZ;
    }

    public void setOffsetZ(float offsetZ) {
        modelRenderer.offsetZ = offsetZ;
    }

    public float getRotationPointX() {
        return modelRenderer.rotationPointX;
    }

    public float getRotationPointY() {
        return modelRenderer.rotationPointY;
    }

    public float getRotationPointZ() {
        return modelRenderer.rotationPointZ;
    }

    public boolean isHidden() {
        return modelRenderer.isHidden;
    }

    public void setHidden(boolean hidden) {
        modelRenderer.isHidden = hidden;
    }

    public ModelRenderer getModelRenderer() {
        return modelRenderer;
    }

    public void setModelRenderer(ModelRenderer modelRenderer) {
        this.modelRenderer = modelRenderer;
    }
}
