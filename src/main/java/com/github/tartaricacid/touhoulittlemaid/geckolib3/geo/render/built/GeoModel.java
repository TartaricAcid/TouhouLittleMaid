package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.raw.pojo.ModelProperties;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectLists;

import java.util.List;

public class GeoModel {
    private final List<GeoBone> topLevelBones;
    private final ModelProperties properties;

    public GeoModel(List<GeoBone> topLevelBones, ModelProperties properties) {
        this.topLevelBones = ObjectLists.unmodifiable(new ObjectArrayList<>(topLevelBones));
        this.properties = properties;
    }

    public List<GeoBone> topLevelBones() {
        return topLevelBones;
    }

    public ModelProperties properties() {
        return properties;
    }
}
