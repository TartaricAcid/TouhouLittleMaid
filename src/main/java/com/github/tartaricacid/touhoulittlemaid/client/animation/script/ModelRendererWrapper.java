package com.github.tartaricacid.touhoulittlemaid.client.animation.script;

import com.github.tartaricacid.touhoulittlemaid.api.animation.IModelRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.model.ModelRendererWithInit;

public final class ModelRendererWrapper implements IModelRenderer {
    private final ModelRendererWithInit modelRenderer;

    public ModelRendererWrapper(ModelRendererWithInit modelRenderer) {
        this.modelRenderer = modelRenderer;
    }

    public ModelRendererWithInit getModelRenderer() {
        return modelRenderer;
    }

    @Override
    public float getRotateAngleX() {
        return modelRenderer.rotateAngleX;
    }

    @Override
    public void setRotateAngleX(float rotateAngleX) {
        modelRenderer.rotateAngleX = rotateAngleX;
    }

    @Override
    public float getRotateAngleY() {
        return modelRenderer.rotateAngleY;
    }

    @Override
    public void setRotateAngleY(float rotateAngleY) {
        modelRenderer.rotateAngleY = rotateAngleY;
    }

    @Override
    public float getRotateAngleZ() {
        return modelRenderer.rotateAngleZ;
    }

    @Override
    public void setRotateAngleZ(float rotateAngleZ) {
        modelRenderer.rotateAngleZ = rotateAngleZ;
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
        return modelRenderer.rotationPointX;
    }

    @Override
    public float getRotationPointY() {
        return modelRenderer.rotationPointY;
    }

    @Override
    public float getRotationPointZ() {
        return modelRenderer.rotationPointZ;
    }

    @Override
    public boolean isHidden() {
        return modelRenderer.isHidden;
    }

    @Override
    public void setHidden(boolean hidden) {
        modelRenderer.isHidden = hidden;
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
