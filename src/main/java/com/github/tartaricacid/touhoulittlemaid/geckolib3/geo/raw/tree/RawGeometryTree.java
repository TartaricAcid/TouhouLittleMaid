package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.raw.tree;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.raw.pojo.Bone;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.raw.pojo.MinecraftGeometry;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.raw.pojo.ModelProperties;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.raw.pojo.RawGeoModel;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RawGeometryTree {
    public Map<String, RawBoneGroup> topLevelBones = new Object2ObjectOpenHashMap<>();
    public ModelProperties properties;

    public static RawGeometryTree parseHierarchy(RawGeoModel model) {
        RawGeometryTree hierarchy = new RawGeometryTree();
        MinecraftGeometry geometry = model.getMinecraftGeometry()[0];
        hierarchy.properties = geometry.getProperties();
        List<Bone> bones = new ObjectArrayList<>(geometry.getBones());
        int index = bones.size() - 1;
        while (true) {
            Bone bone = bones.get(index);
            if (!hasParent(bone)) {
                hierarchy.topLevelBones.put(bone.getName(), new RawBoneGroup(bone));
                bones.remove(bone);
            } else {
                RawBoneGroup groupFromHierarchy = getGroupFromHierarchy(hierarchy, bone.getParent());
                if (groupFromHierarchy != null) {
                    groupFromHierarchy.children.put(bone.getName(), new RawBoneGroup(bone));
                    bones.remove(bone);
                }
            }
            if (index == 0) {
                index = bones.size() - 1;
                if (index == -1) {
                    break;
                }
            } else {
                index--;
            }
        }
        return hierarchy;
    }

    public static boolean hasParent(Bone bone) {
        return bone.getParent() != null;
    }

    public static RawBoneGroup getGroupFromHierarchy(RawGeometryTree hierarchy, String bone) {
        HashMap<String, RawBoneGroup> flatList = Maps.newHashMap();
        for (RawBoneGroup group : hierarchy.topLevelBones.values()) {
            flatList.put(group.selfBone.getName(), group);
            traverse(flatList, group);
        }
        return flatList.get(bone);
    }

    public static void traverse(HashMap<String, RawBoneGroup> flatList, RawBoneGroup group) {
        for (RawBoneGroup child : group.children.values()) {
            flatList.put(child.selfBone.getName(), child);
            traverse(flatList, child);
        }
    }
}
