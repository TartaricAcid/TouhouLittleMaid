package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoModel;
import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AnimatedGeoModel {
    private final GeoModel geoModel;
    private final List<AnimatedGeoBone> topLevelBones;
    private final Map<String, AnimatedGeoBone> bones;

    private final List<AnimatedGeoBone> leftHandBones;
    private final List<AnimatedGeoBone> rightHandBones;
    private final List<AnimatedGeoBone> leftWaistBones;
    private final List<AnimatedGeoBone> rightWaistBones;
    private final List<AnimatedGeoBone> backpackBones;
    private final List<AnimatedGeoBone> tacPistolBones;
    private final List<AnimatedGeoBone> tacRifleBones;
    private final List<AnimatedGeoBone> headBones;

    @Nullable
    private final AnimatedGeoBone head;

    public AnimatedGeoModel(GeoModel model) {
        geoModel = model;

        var bones = new Object2ObjectOpenHashMap<String, AnimatedGeoBone>();
        this.topLevelBones = ObjectLists.unmodifiable(new ObjectArrayList<>(model.topLevelBones().stream().map(b -> new AnimatedGeoBone(b, bones)).toList()));
        this.bones = Object2ObjectMaps.unmodifiable(bones);

        this.leftHandBones = getLocatorHierarchy("LeftHandLocator");
        this.rightHandBones = getLocatorHierarchy("RightHandLocator");
        this.leftWaistBones = getLocatorHierarchy("LeftWaistLocator");
        this.rightWaistBones = getLocatorHierarchy("RightWaistLocator");
        this.backpackBones = getLocatorHierarchy("BackpackLocator");
        this.tacPistolBones = getLocatorHierarchy("PistolLocator");
        this.tacRifleBones = getLocatorHierarchy("RifleLocator");
        this.headBones = getLocatorHierarchy("Head");

        this.head = bones.get("Head");
    }

    private List<AnimatedGeoBone> getLocatorHierarchy(String locatorName) {
        var bone = this.bones.get(locatorName);
        if (bone == null) {
            return ObjectLists.emptyList();
        }

        var list = new ObjectArrayList<AnimatedGeoBone>();
        while (true) {
            list.add(bone);
            if (bone.geoBone().parent() != null) {
                bone = Objects.requireNonNull(this.bones.get(bone.geoBone().parent().name()));
            } else {
                break;
            }
        }

        Collections.reverse(list);
        return ObjectLists.unmodifiable(list);
    }

    public GeoModel geoModel() {
        return geoModel;
    }

    public List<AnimatedGeoBone> topLevelBones() {
        return topLevelBones;
    }

    public Map<String, AnimatedGeoBone> bones() {
        return bones;
    }

    public List<AnimatedGeoBone> leftHandBones() {
        return leftHandBones;
    }

    public List<AnimatedGeoBone> rightHandBones() {
        return rightHandBones;
    }

    public List<AnimatedGeoBone> leftWaistBones() {
        return leftWaistBones;
    }

    public List<AnimatedGeoBone> rightWaistBones() {
        return rightWaistBones;
    }

    public List<AnimatedGeoBone> backpackBones() {
        return backpackBones;
    }

    public List<AnimatedGeoBone> tacPistolBones() {
        return tacPistolBones;
    }

    public List<AnimatedGeoBone> tacRifleBones() {
        return tacRifleBones;
    }

    public List<AnimatedGeoBone> headBones() {
        return headBones;
    }

    @Nullable
    public AnimatedGeoBone head() {
        return head;
    }
}
