package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.snapshot;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.processor.IBone;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.processor.PointData;

public class BoneTopLevelSnapshot extends BoneSnapshot {
    public final IBone bone;
    public final PointData cachedPointData = new PointData(); // 并行动画控制器需要缓存旋转参数

    public float mostRecentResetRotationTick = 0;
    public float mostRecentResetPositionTick = 0;
    public float mostRecentResetScaleTick = 0;
    public boolean isCurrentlyRunningRotationAnimation = true;
    public boolean isCurrentlyRunningPositionAnimation = true;
    public boolean isCurrentlyRunningScaleAnimation = true;

    public BoneTopLevelSnapshot(IBone bone) {
        super(bone.getName());
        this.rotationValueX = bone.getRotationX();
        this.rotationValueY = bone.getRotationY();
        this.rotationValueZ = bone.getRotationZ();

        this.positionOffsetX = bone.getPositionX();
        this.positionOffsetY = bone.getPositionY();
        this.positionOffsetZ = bone.getPositionZ();

        this.scaleValueX = bone.getScaleX();
        this.scaleValueY = bone.getScaleY();
        this.scaleValueZ = bone.getScaleZ();

        this.hidden = bone.isHidden();
        this.childrenHidden = bone.childBonesAreHiddenToo();

        this.bone = bone;
    }

    public BoneTopLevelSnapshot(IBone bone, boolean dontSaveRotations) {
        this(bone);
        if (dontSaveRotations) {
            this.rotationValueX = 0;
            this.rotationValueY = 0;
            this.rotationValueZ = 0;
        }
    }

    public void commit() {
        this.bone.setHidden(this.hidden, this.childrenHidden);

        this.bone.setRotationX(this.rotationValueX);
        this.bone.setRotationY(this.rotationValueY);
        this.bone.setRotationZ(this.rotationValueZ);

        this.bone.setPositionX(this.positionOffsetX);
        this.bone.setPositionY(this.positionOffsetY);
        this.bone.setPositionZ(this.positionOffsetZ);

        this.bone.setScaleX(this.scaleValueX);
        this.bone.setScaleY(this.scaleValueY);
        this.bone.setScaleZ(this.scaleValueZ);

        this.cachedPointData.rotationValueX = 0;
        this.cachedPointData.rotationValueY = 0;
        this.cachedPointData.rotationValueZ = 0;
    }
}
