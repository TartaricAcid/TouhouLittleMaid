package com.github.tartaricacid.touhoulittlemaid.client.animation.script;

import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelRendererWrapper {
    private ModelRenderer modelRenderer;

    public ModelRendererWrapper(ModelRenderer modelRenderer) {
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
        // FIXME：可能新版本已经不存在此数值，需要进一步确认
        return 0;
    }

    public void setOffsetX(float offsetX) {
        // FIXME：可能新版本已经不存在此数值，需要进一步确认
    }

    public float getOffsetY() {
        // FIXME：可能新版本已经不存在此数值，需要进一步确认
        return 0;
    }

    public void setOffsetY(float offsetY) {
        // FIXME：可能新版本已经不存在此数值，需要进一步确认
    }

    public float getOffsetZ() {
        // FIXME：可能新版本已经不存在此数值，需要进一步确认
        return 0;
    }

    public void setOffsetZ(float offsetZ) {
        // FIXME：可能新版本已经不存在此数值，需要进一步确认
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

    public ModelRenderer getModelRenderer() {
        return modelRenderer;
    }

    public void setModelRenderer(ModelRenderer modelRenderer) {
        this.modelRenderer = modelRenderer;
    }
}
