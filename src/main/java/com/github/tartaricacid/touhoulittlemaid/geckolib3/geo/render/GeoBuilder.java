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
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GeoBuilder implements IGeoBuilder {
    private static final Map<String, IGeoBuilder> MODDED_GEO_BUILDERS = new Object2ObjectOpenHashMap<>();
    private static final IGeoBuilder DEFAULT_BUILDER = new GeoBuilder();
    private static final String LEFT_HAND_LOCATOR = "LeftHandLocator";
    private static final String RIGHT_HAND_LOCATOR = "RightHandLocator";
    private static final String LEFT_WAIST_LOCATOR = "LeftWaistLocator";
    private static final String RIGHT_WAIST_LOCATOR = "RightWaistLocator";
    private static final String BACKPACK_LOCATOR_NAME = "BackpackLocator";
    private static final String TAC_PISTOL_LOCATOR_NAME = "PistolLocator";
    private static final String TAC_RIFLE_LOCATOR_NAME = "RifleLocator";
    private static final String HEAD_NAME = "Head";

    public static void registerGeoBuilder(String modid, IGeoBuilder builder) {
        MODDED_GEO_BUILDERS.put(modid, builder);
    }

    public static IGeoBuilder getGeoBuilder(String modid) {
        IGeoBuilder builder = MODDED_GEO_BUILDERS.get(modid);
        return builder == null ? DEFAULT_BUILDER : builder;
    }

    @Override
    public GeoModel constructGeoModel(RawGeometryTree geometryTree) {
        GeoModel model = new GeoModel();
        model.properties = geometryTree.properties;
        for (RawBoneGroup rawBone : geometryTree.topLevelBones.values()) {
            model.topLevelBones.add(this.constructBone(rawBone, geometryTree.properties, null));
        }
        model.getBone(LEFT_HAND_LOCATOR).ifPresent(b -> {
            getBoneParent(b, model.leftHandBones);
            Collections.reverse(model.leftHandBones);
        });
        model.getBone(RIGHT_HAND_LOCATOR).ifPresent(b -> {
            getBoneParent(b, model.rightHandBones);
            Collections.reverse(model.rightHandBones);
        });
        model.getBone(BACKPACK_LOCATOR_NAME).ifPresent(b -> {
            getBoneParent(b, model.backpackBones);
            Collections.reverse(model.backpackBones);
        });
        model.getBone(LEFT_WAIST_LOCATOR).ifPresent(b -> {
            getBoneParent(b, model.leftWaistBones);
            Collections.reverse(model.leftWaistBones);
        });
        model.getBone(RIGHT_WAIST_LOCATOR).ifPresent(b -> {
            getBoneParent(b, model.rightWaistBones);
            Collections.reverse(model.rightWaistBones);
        });
        model.getBone(TAC_PISTOL_LOCATOR_NAME).ifPresent(b -> {
            getBoneParent(b, model.tacPistolBones);
            Collections.reverse(model.tacPistolBones);
        });
        model.getBone(TAC_RIFLE_LOCATOR_NAME).ifPresent(b -> {
            getBoneParent(b, model.tacRifleBones);
            Collections.reverse(model.tacRifleBones);
        });
        model.getBone(HEAD_NAME).ifPresent(b -> {
            model.head = b;
            getBoneParent(b, model.headBones);
            Collections.reverse(model.headBones);
        });
        return model;
    }

    @Override
    public GeoBone constructBone(RawBoneGroup bone, ModelProperties properties, GeoBone parent) {
        GeoBone geoBone = new GeoBone();

        Bone rawBone = bone.selfBone;
        Vector3f rotation = VectorUtils.convertDoubleToFloat(VectorUtils.fromArray(rawBone.getRotation()));
        Vector3f pivot = VectorUtils.convertDoubleToFloat(VectorUtils.fromArray(rawBone.getPivot()));
        rotation.mul(-1, -1, 1);

        geoBone.mirror = rawBone.getMirror();
        geoBone.dontRender = rawBone.getNeverRender();
        geoBone.reset = rawBone.getReset();
        geoBone.inflate = rawBone.getInflate();
        geoBone.parent = parent;
        geoBone.setModelRendererName(rawBone.getName());

        geoBone.setRotationX((float) Math.toRadians(rotation.x()));
        geoBone.setRotationY((float) Math.toRadians(rotation.y()));
        geoBone.setRotationZ((float) Math.toRadians(rotation.z()));

        geoBone.rotationPointX = -pivot.x();
        geoBone.rotationPointY = pivot.y();
        geoBone.rotationPointZ = pivot.z();

        if (!ArrayUtils.isEmpty(rawBone.getCubes())) {
            for (Cube cube : rawBone.getCubes()) {
                geoBone.childCubes.add(GeoCube.createFromPojoCube(cube, properties,
                        geoBone.inflate == null ? null : geoBone.inflate / 16, geoBone.mirror));
            }
        }

        for (RawBoneGroup child : bone.children.values()) {
            geoBone.childBones.add(constructBone(child, properties, geoBone));
        }

        return geoBone;
    }

    private void getBoneParent(GeoBone bone, List<GeoBone> boneList) {
        boneList.add(bone);
        if (bone.parent != null) {
            getBoneParent(bone.parent, boneList);
        }
    }
}
