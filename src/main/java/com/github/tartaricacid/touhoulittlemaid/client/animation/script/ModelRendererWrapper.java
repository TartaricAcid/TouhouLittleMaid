package com.github.tartaricacid.touhoulittlemaid.client.animation.script;

import com.github.tartaricacid.touhoulittlemaid.api.animation.IModelRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.model.ModelRendererWithOffset;

public class ModelRendererWrapper implements IModelRenderer {
    private final ModelRendererWithOffset modelRenderer;

    public ModelRendererWrapper(ModelRendererWithOffset modelRenderer) {
        this.modelRenderer = modelRenderer;
    }

    public ModelRendererWithOffset getModelRenderer() {
        return modelRenderer;
    }

    @Override
    public float getRotateAngleX() {
        return modelRenderer.xRot;
    }

    @Override
    public void setRotateAngleX(float xRot) {
        modelRenderer.xRot = xRot;
    }

    @Override
    public float getRotateAngleY() {
        return modelRenderer.yRot;
    }

    @Override
    public void setRotateAngleY(float yRot) {
        modelRenderer.yRot = yRot;
    }

    @Override
    public float getRotateAngleZ() {
        return modelRenderer.zRot;
    }

    @Override
    public void setRotateAngleZ(float zRot) {
        modelRenderer.zRot = zRot;
    }

    @Override
    public float getOffsetX() {
        return modelRenderer.offsetX;
    }

    @Override
    public void setOffsetX(float offsetX) {
        modelRenderer.offsetX = offsetX;
    }

    @Override
    public float getOffsetY() {
        return modelRenderer.offsetY;
    }

    @Override
    public void setOffsetY(float offsetY) {
        modelRenderer.offsetY = offsetY;
    }

    @Override
    public float getOffsetZ() {
        return modelRenderer.offsetZ;
    }

    @Override
    public void setOffsetZ(float offsetZ) {
        modelRenderer.offsetZ = offsetZ;
    }

    @Override
    public float getRotationPointX() {
        return modelRenderer.x;
    }

    @Override
    public float getRotationPointY() {
        return modelRenderer.y;
    }

    @Override
    public float getRotationPointZ() {
        return modelRenderer.z;
    }

    @Override
    public boolean isHidden() {
        return !modelRenderer.visible;
    }

    @Override
    public void setHidden(boolean hidden) {
        modelRenderer.visible = !hidden;
    }

    @Override
    public float getInitRotateAngleX() {
        return modelRenderer.getInitRotX();
    }

    @Override
    public float getInitRotateAngleY() {
        return modelRenderer.getInitRotY();
    }

    @Override
    public float getInitRotateAngleZ() {
        return modelRenderer.getInitRotZ();
    }
}
