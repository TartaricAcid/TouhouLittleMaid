/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.snapshot;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.processor.IBone;


public class BoneSnapshot {
    public String name;
    public float scaleValueX;
    public float scaleValueY;
    public float scaleValueZ;
    public float positionOffsetX;
    public float positionOffsetY;
    public float positionOffsetZ;
    public float rotationValueX;
    public float rotationValueY;
    public float rotationValueZ;
    public float mostRecentResetRotationTick = 0;
    public float mostRecentResetPositionTick = 0;
    public float mostRecentResetScaleTick = 0;
    public boolean isCurrentlyRunningRotationAnimation = true;
    public boolean isCurrentlyRunningPositionAnimation = true;
    public boolean isCurrentlyRunningScaleAnimation = true;
    private IBone modelRenderer;

    public BoneSnapshot(IBone modelRenderer) {
        rotationValueX = modelRenderer.getRotationX();
        rotationValueY = modelRenderer.getRotationY();
        rotationValueZ = modelRenderer.getRotationZ();

        positionOffsetX = modelRenderer.getPositionX();
        positionOffsetY = modelRenderer.getPositionY();
        positionOffsetZ = modelRenderer.getPositionZ();

        scaleValueX = modelRenderer.getScaleX();
        scaleValueY = modelRenderer.getScaleY();
        scaleValueZ = modelRenderer.getScaleZ();

        this.modelRenderer = modelRenderer;
        this.name = modelRenderer.getName();
    }

    public BoneSnapshot(IBone modelRenderer, boolean dontSaveRotations) {
        if (dontSaveRotations) {
            rotationValueX = 0;
            rotationValueY = 0;
            rotationValueZ = 0;
        }

        rotationValueX = modelRenderer.getRotationX();
        rotationValueY = modelRenderer.getRotationY();
        rotationValueZ = modelRenderer.getRotationZ();

        positionOffsetX = modelRenderer.getPositionX();
        positionOffsetY = modelRenderer.getPositionY();
        positionOffsetZ = modelRenderer.getPositionZ();

        scaleValueX = modelRenderer.getScaleX();
        scaleValueY = modelRenderer.getScaleY();
        scaleValueZ = modelRenderer.getScaleZ();

        this.modelRenderer = modelRenderer;
        this.name = modelRenderer.getName();
    }

    public BoneSnapshot(BoneSnapshot snapshot) {
        scaleValueX = snapshot.scaleValueX;
        scaleValueY = snapshot.scaleValueY;
        scaleValueZ = snapshot.scaleValueZ;

        positionOffsetX = snapshot.positionOffsetX;
        positionOffsetY = snapshot.positionOffsetY;
        positionOffsetZ = snapshot.positionOffsetZ;

        rotationValueX = snapshot.rotationValueX;
        rotationValueY = snapshot.rotationValueY;
        rotationValueZ = snapshot.rotationValueZ;
        this.modelRenderer = snapshot.modelRenderer;
        this.name = snapshot.name;
    }

    @Override
        public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BoneSnapshot that = (BoneSnapshot) o;
        return name.equals(that.name);
    }

    @Override
        public int hashCode() {
        return name.hashCode();
    }
}
