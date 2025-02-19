package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.processor.IBaseBone;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface IAnimatedModel<AB extends IBaseBone> {
    default Map<String, ? extends IBaseBone> bones() {
        return Collections.emptyMap();
    }

    default List<? extends IBaseBone> topLevelBones() {
        return Collections.emptyList();
    }

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

    @Nullable
    default AB head() {
        return null;
    }

    @Nullable
    default AB hat() {
        return null;
    }

    @Nullable
    default AB leftArm() {
        return null;
    }

    @Nullable
    default AB rightArm() {
        return null;
    }

}
