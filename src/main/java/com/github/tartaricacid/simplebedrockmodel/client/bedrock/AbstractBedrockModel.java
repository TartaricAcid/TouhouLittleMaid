package com.github.tartaricacid.simplebedrockmodel.client.bedrock;

import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockCube;
import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockCubeBox;
import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockCubePerFace;
import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockPart;
import com.github.tartaricacid.simplebedrockmodel.client.bedrock.pojo.*;
import com.github.tartaricacid.simplebedrockmodel.client.compat.sodium.SodiumBedrockCubeBox;
import com.github.tartaricacid.simplebedrockmodel.client.compat.sodium.SodiumBedrockCubePerFace;
import com.github.tartaricacid.simplebedrockmodel.client.compat.sodium.SodiumCompat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * 将基岩版实体模型文件读取为 Java 版的 net.minecraft.client.model.Model 模型，此类和 AbstractBedrockEntityModel 一样
 * <p>
 * 但由于 net.minecraft.client.model.EntityModel 和 net.minecraft.client.model.Model 是继承关系，无法复用，故重复代码
 */
@OnlyIn(Dist.CLIENT)
public abstract class AbstractBedrockModel extends Model implements BedrockModelProvider<AbstractBedrockModel> {
    /**
     * 存储 BedrockPart 的 HashMap
     */
    protected final HashMap<String, BedrockPart> modelMap = new HashMap<>();
    /**
     * 存储 Bones 的 HashMap，主要是给后面寻找父骨骼进行坐标转换用的
     */
    protected final HashMap<String, BonesItem> indexBones = new HashMap<>();
    /**
     * 哪些模型需要渲染。加载进父骨骼的子骨骼是不需要渲染的
     */
    protected final List<BedrockPart> shouldRender = new LinkedList<>();
    /**
     * 模型的 AABB
     */
    protected AABB renderBoundingBox;

    public AbstractBedrockModel(InputStream stream) {
        super(RenderType::entityCutoutNoCull);
        BedrockModelPOJO pojo = BedrockModelUtil.GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), BedrockModelPOJO.class);
        if (BedrockVersion.isLegacyVersion(pojo)) {
            loadLegacyModel(pojo);
        }
        if (BedrockVersion.isNewVersion(pojo)) {
            loadNewModel(pojo);
        }
    }

    public AbstractBedrockModel(BedrockModelPOJO pojo) throws InvalidVersionSpecificationException {
        this(pojo, BedrockVersion.getVersion(pojo));
    }

    public AbstractBedrockModel(BedrockModelPOJO pojo, BedrockVersion version) {
        super(RenderType::entityCutoutNoCull);
        if (version == BedrockVersion.LEGACY) {
            loadLegacyModel(pojo);
        }
        if (version == BedrockVersion.NEW) {
            loadNewModel(pojo);
        }
    }

    public AbstractBedrockModel() {
        super(RenderType::entityCutoutNoCull);
        renderBoundingBox = new AABB(-1, 0, -1, 1, 2, 1);
    }

    protected void loadNewModel(BedrockModelPOJO pojo) {
        assert pojo.getGeometryModelNew() != null;
        pojo.getGeometryModelNew().deco();

        Description description = pojo.getGeometryModelNew().getDescription();
        // 材质的长度、宽度
        int texWidth = description.getTextureWidth();
        int texHeight = description.getTextureHeight();

        float[] offset = description.getVisibleBoundsOffset();
        float offsetX = offset[0];
        float offsetY = offset[1];
        float offsetZ = offset[2];
        float width = description.getVisibleBoundsWidth() / 2.0f;
        float height = description.getVisibleBoundsHeight() / 2.0f;
        renderBoundingBox = new AABB(offsetX - width, offsetY - height, offsetZ - width, offsetX + width, offsetY + height, offsetZ + width);

        // 往 indexBones 里面注入数据，为后续坐标转换做参考
        for (BonesItem bones : pojo.getGeometryModelNew().getBones()) {
            // 塞索引，这是给后面坐标转换用的
            indexBones.put(bones.getName(), bones);
            // 塞入新建的空 BedrockPart 实例
            // 因为后面添加 parent 需要，所以先塞空对象，然后二次遍历再进行数据存储
            modelMap.put(bones.getName(), new BedrockPart());
        }

        // 开始往 ModelRenderer 实例里面塞数据
        for (BonesItem bones : pojo.getGeometryModelNew().getBones()) {
            // 骨骼名称，注意因为后面动画的需要，头部、手部、腿部等骨骼命名必须是固定死的
            String name = bones.getName();
            // 旋转点，可能为空
            @Nullable float[] rotation = bones.getRotation();
            // 父骨骼的名称，可能为空
            @Nullable String parent = bones.getParent();
            // 塞进 HashMap 里面的模型对象
            BedrockPart model = modelMap.get(name);

            // 镜像参数
            model.mirror = bones.isMirror();

            // 旋转点
            model.setPos(convertPivot(bones, 0), convertPivot(bones, 1), convertPivot(bones, 2));

            // Nullable 检查，设置旋转角度
            if (rotation != null) {
                setRotationAngle(model, convertRotation(rotation[0]), convertRotation(rotation[1]), convertRotation(rotation[2]));
            }

            // Null 检查，进行父骨骼绑定
            if (parent != null) {
                modelMap.get(parent).addChild(model);
            } else {
                // 没有父骨骼的模型才进行渲染
                shouldRender.add(model);
            }

            // 我的天，Cubes 还能为空……
            if (bones.getCubes() == null) {
                continue;
            }

            // 塞入 Cube List
            for (CubesItem cube : bones.getCubes()) {
                float[] uv = cube.getUv();
                @Nullable FaceUVsItem faceUv = cube.getFaceUv();
                float[] size = cube.getSize();
                @Nullable float[] cubeRotation = cube.getRotation();
                boolean mirror = cube.isMirror();
                float inflate = cube.getInflate();

                // 当做普通 cube 存入
                if (cubeRotation == null) {
                    if (faceUv == null) {
                        model.cubes.add(createCubeBox(uv[0], uv[1],
                                convertOrigin(bones, cube, 0), convertOrigin(bones, cube, 1), convertOrigin(bones, cube, 2),
                                size[0], size[1], size[2], inflate, mirror,
                                texWidth, texHeight));
                    } else {
                        model.cubes.add(createCubePerFace(
                                convertOrigin(bones, cube, 0), convertOrigin(bones, cube, 1), convertOrigin(bones, cube, 2),
                                size[0], size[1], size[2], inflate,
                                texWidth, texHeight, faceUv));
                    }
                }
                // 创建 Cube ModelRender
                else {
                    BedrockPart cubeRenderer = new BedrockPart();
                    cubeRenderer.setPos(convertPivot(bones, cube, 0), convertPivot(bones, cube, 1), convertPivot(bones, cube, 2));
                    setRotationAngle(cubeRenderer, convertRotation(cubeRotation[0]), convertRotation(cubeRotation[1]), convertRotation(cubeRotation[2]));
                    if (faceUv == null) {
                        cubeRenderer.cubes.add(createCubeBox(uv[0], uv[1],
                                convertOrigin(cube, 0), convertOrigin(cube, 1), convertOrigin(cube, 2),
                                size[0], size[1], size[2], inflate, mirror,
                                texWidth, texHeight));
                    } else {
                        cubeRenderer.cubes.add(createCubePerFace(
                                convertOrigin(cube, 0), convertOrigin(cube, 1), convertOrigin(cube, 2),
                                size[0], size[1], size[2], inflate,
                                texWidth, texHeight, faceUv));
                    }

                    // 添加进父骨骼中
                    model.addChild(cubeRenderer);
                }
            }
        }
    }

    protected void loadLegacyModel(BedrockModelPOJO pojo) {
        assert pojo.getGeometryModelLegacy() != null;
        pojo.getGeometryModelLegacy().deco();

        // 材质的长度、宽度
        int texWidth = pojo.getGeometryModelLegacy().getTextureWidth();
        int texHeight = pojo.getGeometryModelLegacy().getTextureHeight();

        float[] offset = pojo.getGeometryModelLegacy().getVisibleBoundsOffset();
        float offsetX = offset[0];
        float offsetY = offset[1];
        float offsetZ = offset[2];
        float width = pojo.getGeometryModelLegacy().getVisibleBoundsWidth() / 2.0f;
        float height = pojo.getGeometryModelLegacy().getVisibleBoundsHeight() / 2.0f;
        renderBoundingBox = new AABB(offsetX - width, offsetY - height, offsetZ - width, offsetX + width, offsetY + height, offsetZ + width);

        // 往 indexBones 里面注入数据，为后续坐标转换做参考
        for (BonesItem bones : pojo.getGeometryModelLegacy().getBones()) {
            // 塞索引，这是给后面坐标转换用的
            indexBones.put(bones.getName(), bones);
            // 塞入新建的空 ModelRenderer 实例
            // 因为后面添加 parent 需要，所以先塞空对象，然后二次遍历再进行数据存储
            modelMap.put(bones.getName(), new BedrockPart());
        }

        // 开始往 ModelRenderer 实例里面塞数据
        for (BonesItem bones : pojo.getGeometryModelLegacy().getBones()) {
            // 骨骼名称，注意因为后面动画的需要，头部、手部、腿部等骨骼命名必须是固定死的
            String name = bones.getName();
            // 旋转点，可能为空
            @Nullable float[] rotation = bones.getRotation();
            // 父骨骼的名称，可能为空
            @Nullable String parent = bones.getParent();
            // 塞进 HashMap 里面的模型对象
            BedrockPart model = modelMap.get(name);

            // 镜像参数
            model.mirror = bones.isMirror();

            // 旋转点
            model.setPos(convertPivot(bones, 0), convertPivot(bones, 1), convertPivot(bones, 2));

            // Nullable 检查，设置旋转角度
            if (rotation != null) {
                setRotationAngle(model, convertRotation(rotation[0]), convertRotation(rotation[1]), convertRotation(rotation[2]));
            }

            // Null 检查，进行父骨骼绑定
            if (parent != null) {
                modelMap.get(parent).addChild(model);
            } else {
                // 没有父骨骼的模型才进行渲染
                shouldRender.add(model);
            }

            // 我的天，Cubes 还能为空……
            if (bones.getCubes() == null) {
                continue;
            }

            // 塞入 Cube List
            for (CubesItem cube : bones.getCubes()) {
                float[] uv = cube.getUv();
                float[] size = cube.getSize();
                boolean mirror = cube.isMirror();
                float inflate = cube.getInflate();

                model.cubes.add(new BedrockCubeBox(uv[0], uv[1],
                        convertOrigin(bones, cube, 0), convertOrigin(bones, cube, 1), convertOrigin(bones, cube, 2),
                        size[0], size[1], size[2], inflate, mirror,
                        texWidth, texHeight));
            }
        }
    }

    protected BedrockCube createCubeBox(float texOffX, float texOffY, float x, float y, float z, float width, float height, float depth,
                                        float delta, boolean mirror, float texWidth, float texHeight) {
        if (SodiumCompat.isSodiumInstalled()) {
            return new SodiumBedrockCubeBox(texOffX, texOffY, x, y, z, width, height, depth, delta, mirror, texWidth, texHeight);
        }
        return new BedrockCubeBox(texOffX, texOffY, x, y, z, width, height, depth, delta, mirror, texWidth, texHeight);
    }

    protected BedrockCube createCubePerFace(float x, float y, float z, float width, float height, float depth, float delta,
                                            float texWidth, float texHeight, FaceUVsItem faces) {
        if (SodiumCompat.isSodiumInstalled()) {
            return new SodiumBedrockCubePerFace(x, y, z, width, height, depth, delta, texWidth, texHeight, faces);
        }
        return new BedrockCubePerFace(x, y, z, width, height, depth, delta, texWidth, texHeight, faces);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        for (BedrockPart model : shouldRender) {
            model.render(poseStack, buffer, packedLight, packedOverlay);
        }
    }

    @Override
    public AABB getRenderBoundingBox() {
        return renderBoundingBox;
    }

    @Override
    public HashMap<String, BedrockPart> getModelMap() {
        return modelMap;
    }

    protected void setRotationAngle(BedrockPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
        modelRenderer.setInitRotationAngle(x, y, z);
    }

    /**
     * 基岩版的旋转中心计算方式和 Java 版不太一样，需要进行转换
     * <p>
     * 如果有父模型
     * <li>x，z 方向：本模型坐标 - 父模型坐标
     * <li>y 方向：父模型坐标 - 本模型坐标
     * <p>
     * 如果没有父模型
     * <li>x，z 方向不变
     * <li>y 方向：24 - 本模型坐标
     *
     * @param index 是 xyz 的哪一个，x 是 0，y 是 1，z 是 2
     */
    protected float convertPivot(BonesItem bones, int index) {
        if (bones.getParent() != null) {
            if (index == 1) {
                return indexBones.get(bones.getParent()).getPivot()[index] - bones.getPivot()[index];
            } else {
                return bones.getPivot()[index] - indexBones.get(bones.getParent()).getPivot()[index];
            }
        } else {
            if (index == 1) {
                return 24 - bones.getPivot()[index];
            } else {
                return bones.getPivot()[index];
            }
        }
    }

    protected float convertPivot(BonesItem parent, CubesItem cube, int index) {
        assert cube.getPivot() != null;
        if (index == 1) {
            return parent.getPivot()[index] - cube.getPivot()[index];
        } else {
            return cube.getPivot()[index] - parent.getPivot()[index];
        }
    }

    /**
     * 基岩版和 Java 版本的方块起始坐标也不一致，Java 是相对坐标，而且 y 值方向不一致。
     * 基岩版是绝对坐标，而且 y 方向朝上。
     * 其实两者规律很简单，但是我找了一下午，才明白咋回事。
     * <li>如果是 x，z 轴，那么只需要方块起始坐标减去旋转点坐标
     * <li>如果是 y 轴，旋转点坐标减去方块起始坐标，再减去方块的 y 长度
     *
     * @param index 是 xyz 的哪一个，x 是 0，y 是 1，z 是 2
     */
    protected float convertOrigin(BonesItem bone, CubesItem cube, int index) {
        if (index == 1) {
            return bone.getPivot()[index] - cube.getOrigin()[index] - cube.getSize()[index];
        } else {
            return cube.getOrigin()[index] - bone.getPivot()[index];
        }
    }

    protected float convertOrigin(CubesItem cube, int index) {
        assert cube.getPivot() != null;
        if (index == 1) {
            return cube.getPivot()[index] - cube.getOrigin()[index] - cube.getSize()[index];
        } else {
            return cube.getOrigin()[index] - cube.getPivot()[index];
        }
    }

    /**
     * 基岩版用的是度，Java 版用的是弧度，这个转换很简单
     */
    protected float convertRotation(float degree) {
        return (float) (degree * Math.PI / 180);
    }
}
