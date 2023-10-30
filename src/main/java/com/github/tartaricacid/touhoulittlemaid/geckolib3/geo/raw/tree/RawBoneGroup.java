package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.raw.tree;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.raw.pojo.Bone;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.Map;

public class RawBoneGroup {
    public Map<String, RawBoneGroup> children = new Object2ObjectOpenHashMap<>();
    public Bone selfBone;

    public RawBoneGroup(Bone bone) {
        this.selfBone = bone;
    }
}