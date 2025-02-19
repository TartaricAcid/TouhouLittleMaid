package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.processor.IBaseBone;

import java.util.Collections;
import java.util.List;

public interface IAnimatedModel {
    default List<? extends IBaseBone> leftHandBones() {
        return Collections.emptyList();
    }

    default List<? extends IBaseBone> rightHandBones() {
        return Collections.emptyList();
    }

    default List<? extends IBaseBone> leftWaistBones() {
        return Collections.emptyList();
    }

    default List<? extends IBaseBone> rightWaistBones() {
        return Collections.emptyList();
    }

    default List<? extends IBaseBone> backpackBones() {
        return Collections.emptyList();
    }

    default List<? extends IBaseBone> tacPistolBones() {
        return Collections.emptyList();
    }

    default List<? extends IBaseBone> tacRifleBones() {
        return Collections.emptyList();
    }

    default List<? extends IBaseBone> headBones() {
        return Collections.emptyList();
    }
}
