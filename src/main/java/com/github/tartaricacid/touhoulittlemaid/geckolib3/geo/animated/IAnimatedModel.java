package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.processor.IBaseBone;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public interface IAnimatedModel<AB extends IBaseBone> {
    Map<String, ? extends IBaseBone> bones();
    List<? extends IBaseBone> topLevelBones();
    List<? extends IBaseBone> leftHandBones();
    List<? extends IBaseBone> rightHandBones();
    List<? extends IBaseBone> leftWaistBones();
    List<? extends IBaseBone> rightWaistBones();
    List<? extends IBaseBone> backpackBones();
    List<? extends IBaseBone> tacPistolBones();
    List<? extends IBaseBone> tacRifleBones();
    List<? extends IBaseBone> headBones();
    @Nullable
    AB head();
    @Nullable
    AB hat();
    @Nullable
    AB leftArm();
    @Nullable
    AB rightArm();

}
