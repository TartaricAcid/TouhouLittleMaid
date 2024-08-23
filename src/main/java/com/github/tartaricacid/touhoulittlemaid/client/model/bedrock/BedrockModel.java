package com.github.tartaricacid.touhoulittlemaid.client.model.bedrock;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.animation.CustomJsAnimationManger;
import com.github.tartaricacid.touhoulittlemaid.client.animation.inner.IAnimation;
import com.github.tartaricacid.touhoulittlemaid.client.animation.script.EntityChairWrapper;
import com.github.tartaricacid.touhoulittlemaid.client.animation.script.EntityMaidWrapper;
import com.github.tartaricacid.touhoulittlemaid.client.animation.script.ModelRendererWrapper;
import com.github.tartaricacid.touhoulittlemaid.client.model.AbstractModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.BedrockVersion;
import com.github.tartaricacid.touhoulittlemaid.client.model.pojo.*;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.script.Invocable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class BedrockModel<T extends LivingEntity> extends AbstractModel<T> {
    /**
     * 存储 ModelRender 子模型的 HashMap
     */
    protected final HashMap<String, ModelRendererWrapper> modelMap = new HashMap<>();
    /**
     * 存储 Bones 的 HashMap，主要是给后面寻找父骨骼进行坐标转换用的
     */
    private final HashMap<String, BonesItem> indexBones = new HashMap<>();
    /**
     * 哪些模型需要渲染。加载进父骨骼的子骨骼是不需要渲染的
     */
    private final List<BedrockPart> shouldRender = new LinkedList<>();
    /**
     * 用于自定义动画的变量
     */
    private final EntityMaidWrapper entityMaidWrapper = new EntityMaidWrapper();
    private final EntityChairWrapper entityChairWrapper = new EntityChairWrapper();
    private AABB renderBoundingBox;
    private List<Object> animations = Lists.newArrayList();

    public BedrockModel() {
        super(RenderType::entityTranslucent);
        renderBoundingBox = new AABB(-1, 0, -1, 1, 2, 1);
    }

    public BedrockModel(BedrockModelPOJO pojo, BedrockVersion version) {
        super(RenderType::entityTranslucent);
        if (version == BedrockVersion.LEGACY) {
            loadLegacyModel(pojo);
        }
        if (version == BedrockVersion.NEW) {
            loadNewModel(pojo);
        }
    }

    protected void loadNewModel(BedrockModelPOJO pojo) {
        assert pojo.getGeometryModelNew() != null;
        pojo.getGeometryModelNew().deco();

        Description description = pojo.getGeometryModelNew().getDescription();
        // 材质的长度、宽度
        int texWidth = description.getTextureWidth();
        int texHeight = description.getTextureHeight();

        List<Float> offset = description.getVisibleBoundsOffset();
        float offsetX = offset.get(0);
        float offsetY = offset.get(1);
        float offsetZ = offset.get(2);
        float width = description.getVisibleBoundsWidth() / 2.0f;
        float height = description.getVisibleBoundsHeight() / 2.0f;
        renderBoundingBox = new AABB(offsetX - width, offsetY - height, offsetZ - width, offsetX + width, offsetY + height, offsetZ + width);

        // 往 indexBones 里面注入数据，为后续坐标转换做参考
        for (BonesItem bones : pojo.getGeometryModelNew().getBones()) {
            // 塞索引，这是给后面坐标转换用的
            indexBones.put(bones.getName(), bones);
            // 塞入新建的空 BedrockPart 实例
            // 因为后面添加 parent 需要，所以先塞空对象，然后二次遍历再进行数据存储
            modelMap.put(bones.getName(), new ModelRendererWrapper(new BedrockPart()));
        }

        // 开始往 ModelRenderer 实例里面塞数据
        for (BonesItem bones : pojo.getGeometryModelNew().getBones()) {
            // 骨骼名称，注意因为后面动画的需要，头部、手部、腿部等骨骼命名必须是固定死的
            String name = bones.getName();
            // 旋转点，可能为空
            @Nullable List<Float> rotation = bones.getRotation();
            // 父骨骼的名称，可能为空
            @Nullable String parent = bones.getParent();
            // 塞进 HashMap 里面的模型对象
            BedrockPart model = modelMap.get(name).getModelRenderer();

            // 镜像参数
            model.mirror = bones.isMirror();

            // 旋转点
            model.setPos(convertPivot(bones, 0), convertPivot(bones, 1), convertPivot(bones, 2));

            // Nullable 检查，设置旋转角度
            if (rotation != null) {
                setRotationAngle(model, convertRotation(rotation.get(0)), convertRotation(rotation.get(1)), convertRotation(rotation.get(2)));
            }

            // Null 检查，进行父骨骼绑定
            if (parent != null) {
                modelMap.get(parent).getModelRenderer().addChild(model);
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
                List<Float> uv = cube.getUv();
                @Nullable FaceUVsItem faceUv = cube.getFaceUv();
                List<Float> size = cube.getSize();
                @Nullable List<Float> cubeRotation = cube.getRotation();
                boolean mirror = cube.isMirror();
                float inflate = cube.getInflate();

                // 当做普通 cube 存入
                if (cubeRotation == null) {
                    if (faceUv == null) {
                        model.cubes.add(new BedrockCubeBox(uv.get(0), uv.get(1),
                                convertOrigin(bones, cube, 0), convertOrigin(bones, cube, 1), convertOrigin(bones, cube, 2),
                                size.get(0), size.get(1), size.get(2), inflate, mirror,
                                texWidth, texHeight));
                    } else {
                        model.cubes.add(new BedrockCubePerFace(
                                convertOrigin(bones, cube, 0), convertOrigin(bones, cube, 1), convertOrigin(bones, cube, 2),
                                size.get(0), size.get(1), size.get(2), inflate,
                                texWidth, texHeight, faceUv));
                    }
                }
                // 创建 Cube ModelRender
                else {
                    BedrockPart cubeRenderer = new BedrockPart();
                    cubeRenderer.setPos(convertPivot(bones, cube, 0), convertPivot(bones, cube, 1), convertPivot(bones, cube, 2));
                    setRotationAngle(cubeRenderer, convertRotation(cubeRotation.get(0)), convertRotation(cubeRotation.get(1)), convertRotation(cubeRotation.get(2)));
                    if (faceUv == null) {
                        cubeRenderer.cubes.add(new BedrockCubeBox(uv.get(0), uv.get(1),
                                convertOrigin(cube, 0), convertOrigin(cube, 1), convertOrigin(cube, 2),
                                size.get(0), size.get(1), size.get(2), inflate, mirror,
                                texWidth, texHeight));
                    } else {
                        cubeRenderer.cubes.add(new BedrockCubePerFace(
                                convertOrigin(cube, 0), convertOrigin(cube, 1), convertOrigin(cube, 2),
                                size.get(0), size.get(1), size.get(2), inflate,
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

        List<Float> offset = pojo.getGeometryModelLegacy().getVisibleBoundsOffset();
        float offsetX = offset.get(0);
        float offsetY = offset.get(1);
        float offsetZ = offset.get(2);
        float width = pojo.getGeometryModelLegacy().getVisibleBoundsWidth() / 2.0f;
        float height = pojo.getGeometryModelLegacy().getVisibleBoundsHeight() / 2.0f;
        renderBoundingBox = new AABB(offsetX - width, offsetY - height, offsetZ - width, offsetX + width, offsetY + height, offsetZ + width);

        // 往 indexBones 里面注入数据，为后续坐标转换做参考
        for (BonesItem bones : pojo.getGeometryModelLegacy().getBones()) {
            // 塞索引，这是给后面坐标转换用的
            indexBones.put(bones.getName(), bones);
            // 塞入新建的空 ModelRenderer 实例
            // 因为后面添加 parent 需要，所以先塞空对象，然后二次遍历再进行数据存储
            modelMap.put(bones.getName(), new ModelRendererWrapper(new BedrockPart()));
        }

        // 开始往 ModelRenderer 实例里面塞数据
        for (BonesItem bones : pojo.getGeometryModelLegacy().getBones()) {
            // 骨骼名称，注意因为后面动画的需要，头部、手部、腿部等骨骼命名必须是固定死的
            String name = bones.getName();
            // 旋转点，可能为空
            @Nullable List<Float> rotation = bones.getRotation();
            // 父骨骼的名称，可能为空
            @Nullable String parent = bones.getParent();
            // 塞进 HashMap 里面的模型对象
            BedrockPart model = modelMap.get(name).getModelRenderer();

            // 镜像参数
            model.mirror = bones.isMirror();

            // 旋转点
            model.setPos(convertPivot(bones, 0), convertPivot(bones, 1), convertPivot(bones, 2));

            // Nullable 检查，设置旋转角度
            if (rotation != null) {
                setRotationAngle(model, convertRotation(rotation.get(0)), convertRotation(rotation.get(1)), convertRotation(rotation.get(2)));
            }

            // Null 检查，进行父骨骼绑定
            if (parent != null) {
                modelMap.get(parent).getModelRenderer().addChild(model);
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
                List<Float> uv = cube.getUv();
                List<Float> size = cube.getSize();
                boolean mirror = cube.isMirror();
                float inflate = cube.getInflate();

                model.cubes.add(new BedrockCubeBox(uv.get(0), uv.get(1),
                        convertOrigin(bones, cube, 0), convertOrigin(bones, cube, 1), convertOrigin(bones, cube, 2),
                        size.get(0), size.get(1), size.get(2), inflate, mirror,
                        texWidth, texHeight));
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (animations != null) {
            Invocable invocable = (Invocable) CustomJsAnimationManger.NASHORN;
            if (entityIn instanceof Mob mob) {
                IMaid maid = IMaid.convert(mob);
                if (maid != null) {
                    setupMaidAnim(maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, invocable);
                }
                return;
            }
            if (entityIn instanceof EntityChair) {
                setupChairAnim((EntityChair) entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, invocable);
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        for (BedrockPart model : shouldRender) {
            model.render(poseStack, buffer, packedLight, packedOverlay);
        }
    }


    @SuppressWarnings("unchecked")
    private void setupMaidAnim(IMaid entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, Invocable invocable) {
        try {
            for (Object animation : animations) {
                if (animation instanceof IAnimation) {
                    ((IAnimation<Mob>) animation).setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 0, entityIn.asEntity(), modelMap);
                } else {
                    entityMaidWrapper.setData(entityIn, attackTime, riding);
                    invocable.invokeMethod(animation, "animation", entityMaidWrapper, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 0.0625f, modelMap);
                    entityMaidWrapper.clearData();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            CustomPackLoader.MAID_MODELS.removeAnimation(entityIn.getModelId());
        }
    }

    @SuppressWarnings("unchecked")
    private void setupChairAnim(EntityChair entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, Invocable invocable) {
        try {
            for (Object animation : animations) {
                if (animation instanceof IAnimation) {
                    ((IAnimation<EntityChair>) animation).setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 0, entityIn, modelMap);
                } else {
                    entityChairWrapper.setData(entityIn);
                    invocable.invokeMethod(animation, "animation", entityChairWrapper, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 0.0625f, modelMap);
                    entityChairWrapper.clearData();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            CustomPackLoader.CHAIR_MODELS.removeAnimation(entityIn.getModelId());
        }
    }

    private void setRotationAngle(BedrockPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
        modelRenderer.setInitRotationAngle(x, y, z);
    }

    public void translateToHand(HumanoidArm sideIn, PoseStack poseStack) {
        BedrockPart arm = getArm(sideIn);
        if (arm != null) {
            arm.translateAndRotate(poseStack);
        }
    }

    public boolean hasBackpackPositioningModel() {
        return modelMap.get("backpackPositioningBone") != null;
    }

    public BedrockPart getBackpackPositioningModel() {
        return modelMap.get("backpackPositioningBone").getModelRenderer();
    }

    @Nullable
    private BedrockPart getArm(HumanoidArm sideIn) {
        return sideIn == HumanoidArm.LEFT ? modelMap.get("armLeft").getModelRenderer() : modelMap.get("armRight").getModelRenderer();
    }

    public boolean hasHead() {
        return modelMap.containsKey("head");
    }

    public BedrockPart getHead() {
        return modelMap.get("head").getModelRenderer();
    }

    public boolean hasLeftArm() {
        return modelMap.containsKey("armLeft");
    }

    public boolean hasRightArm() {
        return modelMap.containsKey("armRight");
    }

    public boolean hasArmPositioningModel(HumanoidArm side) {
        ModelRendererWrapper arm = (side == HumanoidArm.LEFT ? modelMap.get("armLeftPositioningBone") : modelMap.get("armRightPositioningBone"));
        return arm != null;
    }

    public void translateToPositioningHand(HumanoidArm sideIn, PoseStack poseStack) {
        BedrockPart arm = (sideIn == HumanoidArm.LEFT ? modelMap.get("armLeftPositioningBone").getModelRenderer() : modelMap.get("armRightPositioningBone").getModelRenderer());
        if (arm != null) {
            arm.translateAndRotate(poseStack);
        }
    }

    public boolean hasWaistPositioningModel(HumanoidArm side) {
        ModelRendererWrapper waist = (side == HumanoidArm.LEFT ? modelMap.get("waistLeftPositioningBone") : modelMap.get("waistRightPositioningBone"));
        return waist != null;
    }

    public void translateToPositioningWaist(HumanoidArm sideIn, PoseStack poseStack) {
        BedrockPart waist = (sideIn == HumanoidArm.LEFT ? modelMap.get("waistLeftPositioningBone").getModelRenderer() : modelMap.get("waistRightPositioningBone").getModelRenderer());
        if (waist != null) {
            waist.translateAndRotate(poseStack);
        }
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
    private float convertPivot(BonesItem bones, int index) {
        if (bones.getParent() != null) {
            if (index == 1) {
                return indexBones.get(bones.getParent()).getPivot().get(index) - bones.getPivot().get(index);
            } else {
                return bones.getPivot().get(index) - indexBones.get(bones.getParent()).getPivot().get(index);
            }
        } else {
            if (index == 1) {
                return 24 - bones.getPivot().get(index);
            } else {
                return bones.getPivot().get(index);
            }
        }
    }

    private float convertPivot(BonesItem parent, CubesItem cube, int index) {
        assert cube.getPivot() != null;
        if (index == 1) {
            return parent.getPivot().get(index) - cube.getPivot().get(index);
        } else {
            return cube.getPivot().get(index) - parent.getPivot().get(index);
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
    private float convertOrigin(BonesItem bone, CubesItem cube, int index) {
        if (index == 1) {
            return bone.getPivot().get(index) - cube.getOrigin().get(index) - cube.getSize().get(index);
        } else {
            return cube.getOrigin().get(index) - bone.getPivot().get(index);
        }
    }

    private float convertOrigin(CubesItem cube, int index) {
        assert cube.getPivot() != null;
        if (index == 1) {
            return cube.getPivot().get(index) - cube.getOrigin().get(index) - cube.getSize().get(index);
        } else {
            return cube.getOrigin().get(index) - cube.getPivot().get(index);
        }
    }

    /**
     * 基岩版用的是度，Java 版用的是弧度，这个转换很简单
     */
    private float convertRotation(float degree) {
        return (float) (degree * Math.PI / 180);
    }

    public void setAnimations(@Nullable List<Object> animations) {
        this.animations = animations;
    }

    public AABB getRenderBoundingBox() {
        return renderBoundingBox;
    }
}
