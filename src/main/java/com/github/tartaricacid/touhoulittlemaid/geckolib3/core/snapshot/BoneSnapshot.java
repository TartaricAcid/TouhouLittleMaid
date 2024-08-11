/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.snapshot;

public class BoneSnapshot {
    public final String name;
    public float scaleValueX;
    public float scaleValueY;
    public float scaleValueZ;
    public float positionOffsetX;
    public float positionOffsetY;
    public float positionOffsetZ;
    public float rotationValueX;
    public float rotationValueY;
    public float rotationValueZ;

    public boolean hidden;
    public boolean childrenHidden;

    protected BoneSnapshot(String name) {
        this.name = name;
    }

    public BoneSnapshot(BoneSnapshot snapshot) {
        copyFrom(snapshot);
        this.name = snapshot.name;
    }

    public void copyFrom(BoneSnapshot snapshot) {
        this.scaleValueX = snapshot.scaleValueX;
        this.scaleValueY = snapshot.scaleValueY;
        this.scaleValueZ = snapshot.scaleValueZ;

        this.positionOffsetX = snapshot.positionOffsetX;
        this.positionOffsetY = snapshot.positionOffsetY;
        this.positionOffsetZ = snapshot.positionOffsetZ;

        this.rotationValueX = snapshot.rotationValueX;
        this.rotationValueY = snapshot.rotationValueY;
        this.rotationValueZ = snapshot.rotationValueZ;

        this.hidden = snapshot.hidden;
        this.childrenHidden = snapshot.childrenHidden;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof BoneSnapshot that) {
            return this.name.equals(that.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
