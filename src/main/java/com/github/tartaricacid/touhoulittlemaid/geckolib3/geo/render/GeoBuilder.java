package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.raw.pojo.Bone;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.raw.pojo.Cube;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.raw.pojo.ModelProperties;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.raw.tree.RawBoneGroup;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.raw.tree.RawGeometryTree;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoBone;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoCube;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.VectorUtils;
import com.mojang.math.Vector3f;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public class GeoBuilder implements IGeoBuilder {
    private static final IGeoBuilder DEFAULT_BUILDER = new GeoBuilder();

    public static IGeoBuilder getGeoBuilder() {
        return DEFAULT_BUILDER;
    }

    @Override
    public GeoModel constructGeoModel(RawGeometryTree geometryTree) {
        List<GeoBone> topLevelBones = new ArrayList<>();

        for (RawBoneGroup rawBone : geometryTree.topLevelBones.values()) {
            GeoBone bone = this.constructBone(rawBone, geometryTree.properties, 0);
            topLevelBones.add(bone);
        }

        return new GeoModel(topLevelBones, geometryTree.properties);
    }

    public GeoBone constructBone(RawBoneGroup bone, ModelProperties properties, int depth) {
        Bone rawBone = bone.selfBone;
        Vector3f rotation = VectorUtils.convertDoubleToFloat(VectorUtils.fromArray(rawBone.getRotation()));
        Vector3f pivot = VectorUtils.convertDoubleToFloat(VectorUtils.fromArray(rawBone.getPivot()));
        rotation.mul(-1, -1, 1);

        List<GeoCube> cubes = new ArrayList<>();
        if (!ArrayUtils.isEmpty(rawBone.getCubes())) {
            for (Cube cube : rawBone.getCubes()) {
                cubes.add(GeoCube.createFromPojoCube(cube, properties,
                        rawBone.getInflate() == null ? null : rawBone.getInflate() / 16, rawBone.getMirror()));
            }
        }

        List<GeoBone> children = new ArrayList<>();
        for (RawBoneGroup child : bone.children.values()) {
            children.add(constructBone(child, properties, depth + 1));
        }

        GeoBone geoBone = new GeoBone(children, rawBone.getName(),
                new Vector3f(-pivot.x(), pivot.y(), pivot.z()),
                new Vector3f((float) Math.toRadians(rotation.x()), (float) Math.toRadians(rotation.y()), (float) Math.toRadians(rotation.z())),
                cubes,
                rawBone.getMirror(),
                rawBone.getInflate(),
                rawBone.getNeverRender(),
                rawBone.getReset());

        for (GeoBone child : children) {
            child.setParent(geoBone);
        }

        return geoBone;
    }
}
