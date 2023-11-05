/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.manager;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.controller.AnimationController;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.processor.IBone;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.snapshot.BoneSnapshot;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class AnimationData {
    private final Map<String, AnimationController> animationControllers = new Object2ObjectLinkedOpenHashMap<>();
    public double tick;
    public boolean isFirstTick = true;
    public double startTick = -1;
    public boolean shouldPlayWhilePaused = false;
    private Map<String, Pair<IBone, BoneSnapshot>> boneSnapshotCollection;
    private double resetTickLength = 1;

    public AnimationData() {
        boneSnapshotCollection = new Object2ObjectOpenHashMap<>();
    }

    public AnimationController addAnimationController(AnimationController value) {
        return this.animationControllers.put(value.getName(), value);
    }

    public Map<String, Pair<IBone, BoneSnapshot>> getBoneSnapshotCollection() {
        return boneSnapshotCollection;
    }

    public void setBoneSnapshotCollection(HashMap<String, Pair<IBone, BoneSnapshot>> boneSnapshotCollection) {
        this.boneSnapshotCollection = boneSnapshotCollection;
    }

    public void clearSnapshotCache() {
        this.boneSnapshotCollection = new HashMap<>();
    }

    public double getResetSpeed() {
        return resetTickLength;
    }

    /**
     * 这是任何没有动画的骨骼恢复到其初始位置所需的时间
     *
     * @param resetTickLength 重置时所需的 tick。不能为负数
     */
    public void setResetSpeedInTicks(double resetTickLength) {
        this.resetTickLength = resetTickLength < 0 ? 0 : resetTickLength;
    }

    public Map<String, AnimationController> getAnimationControllers() {
        return animationControllers;
    }
}
