package com.github.tartaricacid.touhoulittlemaid.client.animation.script;

import com.github.tartaricacid.touhoulittlemaid.client.model.ModelRendererWithOffset;

public class ModelRendererWrapper {
    private ModelRendererWithOffset modelRenderer;

    public ModelRendererWrapper(ModelRendererWithOffset modelRenderer) {
        this.modelRenderer = modelRenderer;
    }

    public float getRotateAngleX() {
        return modelRenderer.xRot;
    }

    public void setRotateAngleX(float xRot) {
        modelRenderer.xRot = xRot;
    }

    public float getRotateAngleY() {
        return modelRenderer.yRot;
    }

    public void setRotateAngleY(float yRot) {
        modelRenderer.yRot = yRot;
    }

    public float getRotateAngleZ() {
        return modelRenderer.zRot;
    }

    public void setRotateAngleZ(float zRot) {
        modelRenderer.zRot = zRot;
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
        return modelRenderer.x;
    }

    public float getRotationPointY() {
        return modelRenderer.y;
    }

    public float getRotationPointZ() {
        return modelRenderer.z;
    }

    public boolean isHidden() {
        return !modelRenderer.visible;
    }

    public void setHidden(boolean hidden) {
        modelRenderer.visible = !hidden;
    }

    public ModelRendererWithOffset getModelRenderer() {
        return modelRenderer;
    }

    public void setModelRenderer(ModelRendererWithOffset modelRenderer) {
        this.modelRenderer = modelRenderer;
    }
}
