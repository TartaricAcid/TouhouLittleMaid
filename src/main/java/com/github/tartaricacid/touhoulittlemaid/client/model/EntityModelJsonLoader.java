package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.github.tartaricacid.touhoulittlemaid.client.model.pojo.BonesItem;
import com.github.tartaricacid.touhoulittlemaid.client.model.pojo.CubesItem;
import com.github.tartaricacid.touhoulittlemaid.client.model.pojo.CustomModelPOJO;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 通过解析基岩版 JSON 实体模型得到的 POJO，将其转换为 ModelBase 类
 *
 * @author TartaricAcid
 * @date 2019/7/9 14:18
 **/
public class EntityModelJsonLoader extends ModelBase {
    /**
     * 存储 ModelRender 子模型的 HashMap
     */
    private HashMap<String, ModelRenderer> modelMap = new HashMap<>();
    /**
     * 存储 Bones 的 HashMap，主要是给后面寻找父骨骼进行坐标转换用的
     */
    private HashMap<String, BonesItem> indexBones = new HashMap<>();
    /**
     * 哪些模型需要渲染。加载进父骨骼的子骨骼是不需要渲染的
     */
    private List<ModelRenderer> shouldRender = new ArrayList<>();

    public EntityModelJsonLoader(CustomModelPOJO pojo) {
        // 材质的长度、宽度
        textureWidth = pojo.getGeometryEntityMaidModel().getTexturewidth();
        textureHeight = pojo.getGeometryEntityMaidModel().getTextureheight();

        // 往 indexBones 里面注入数据，为后续坐标转换做参考
        for (BonesItem bones : pojo.getGeometryEntityMaidModel().getBones()) {
            // 塞索引，这是给后面坐标转换用的
            indexBones.put(bones.getName(), bones);
            // 塞入新建的空 ModelRenderer 实例
            // 因为后面添加 parent 需要，所以先塞空对象，然后二次遍历再进行数据存储
            modelMap.put(bones.getName(), new ModelRenderer(this));
        }

        // 开始往 ModelRenderer 实例里面塞数据
        for (BonesItem bones : pojo.getGeometryEntityMaidModel().getBones()) {
            // 骨骼名称，注意因为后面动画的需要，头部、手部、腿部等骨骼命名必须是固定死的
            String name = bones.getName();
            // 旋转点，可能为空
            @Nullable List<Float> rotation = bones.getRotation();
            // 父骨骼的名称，可能为空
            @Nullable String parent = bones.getParent();
            // 塞进 HashMap 里面的模型对象
            ModelRenderer model = modelMap.get(name);

            // 旋转点
            model.setRotationPoint(convertPivot(bones, 0), convertPivot(bones, 1), convertPivot(bones, 2));

            // Nullable 检查，设置旋转角度
            if (rotation != null) {
                setRotationAngle(model, convertRotation(rotation.get(0)), convertRotation(rotation.get(1)), convertRotation(rotation.get(2)));
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
            for (CubesItem cubes : bones.getCubes()) {
                List<Integer> uv = cubes.getUv();
                List<Integer> size = cubes.getSize();
                boolean mirror = cubes.isMirror();

                model.cubeList.add(new ModelBox(model, uv.get(0), uv.get(1),
                        convertOrigin(bones, cubes, 0), convertOrigin(bones, cubes, 1), convertOrigin(bones, cubes, 2),
                        size.get(0), size.get(1), size.get(2), 0, mirror));
            }
        }
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        for (ModelRenderer model : shouldRender) {
            model.render(scale);
        }
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks,
                                  float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        ModelRenderer head = modelMap.get("head");
        ModelRenderer legLeft = modelMap.get("legLeft");
        ModelRenderer legRight = modelMap.get("legRight");
        ModelRenderer armLeft = modelMap.get("armLeft");
        ModelRenderer armRight = modelMap.get("armRight");

        // TODO：更加全面的 null 值检查策略
        if (head != null && legLeft != null && legRight != null && armLeft != null && armRight != null) {
            head.rotateAngleX = headPitch / 45f / (float) Math.PI;
            head.rotateAngleY = netHeadYaw / 45f / (float) Math.PI;

            // 左脚右脚的运动
            legLeft.rotateAngleX = MathHelper.cos(limbSwing * 1.2F) * 0.5F * limbSwingAmount;
            legRight.rotateAngleX = -MathHelper.cos(limbSwing * 1.2F) * 0.5F * limbSwingAmount;
            legLeft.rotateAngleZ = 0f;
            legRight.rotateAngleZ = 0f;

            // 左手右手的运动（这一处还有一个功能，即对数据进行归位）
            armLeft.rotateAngleX = -MathHelper.cos(limbSwing * 1.2F) * 1F * limbSwingAmount;
            armRight.rotateAngleX = MathHelper.cos(limbSwing * 1.2F) * 1F * limbSwingAmount;
            armLeft.rotateAngleY = 0f;
            armRight.rotateAngleY = 0f;
            armLeft.rotateAngleZ = MathHelper.cos(ageInTicks * 0.05f) * 0.05f - 0.4f;
            armRight.rotateAngleZ = -MathHelper.cos(ageInTicks * 0.05f) * 0.05f + 0.4f;

            EntityMaid entityMaid = (EntityMaid) entityIn;
            if (entityMaid.isRiding()) {
                legLeft.rotateAngleX = -0.960f;
                legLeft.rotateAngleZ = -0.523f;
                legRight.rotateAngleX = -0.960f;
                legRight.rotateAngleZ = 0.523f;

                GlStateManager.translate(0, 0.3f, 0);
            } else if (entityMaid.isSitting()) {
                armLeft.rotateAngleX = -0.798f;
                armLeft.rotateAngleZ = 0.274f;
                armRight.rotateAngleX = -0.798f;
                armRight.rotateAngleZ = -0.274f;

                legLeft.rotateAngleX = -0.960f;
                legLeft.rotateAngleZ = -0.523f;
                legRight.rotateAngleX = -0.960f;
                legRight.rotateAngleZ = 0.523f;

                GlStateManager.translate(0, 0.3f, 0);
            }

            if (entityMaid.isBegging()) {
                head.rotateAngleZ = 0.139f;
            } else {
                head.rotateAngleZ = 0f;
            }

            if (entityMaid.isSwingingArms()) {
                armLeft.rotateAngleX = -1.396f;
                armLeft.rotateAngleY = 0.785f;
                armRight.rotateAngleX = -1.396f;
                armRight.rotateAngleY = -0.174f;
            }
        }
    }

    private void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public void postRenderArm(float scale, EnumHandSide side) {
        this.getArmForSide(side).postRender(scale);
    }

    private ModelRenderer getArmForSide(EnumHandSide side) {
        // TODO：null 检查
        return side == EnumHandSide.LEFT ? modelMap.get("armLeft") : modelMap.get("armRight");
    }

    /**
     * 基岩版的旋转中心计算方式和 Java 版不太一样，需要进行转换<br>
     * <br>
     * 如果有父模型<br>
     * <li>x，z 方向：本模型坐标 - 父模型坐标</li>
     * <li>y 方向：父模型坐标 - 本模型坐标</li>
     * <br>
     * 如果没有父模型
     * <li>x，z 方向不变</li>
     * <li>y 方向：24 - 本模型坐标</li>
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

    /**
     * 基岩版和 Java 版本的方块起始坐标也不一致，Java 是相对坐标，而且 y 值方向不一致<br>
     * 基岩版是绝对坐标，而且 y 方向朝上<br>
     * 其实两者规律很简单，但是我找了一下午，才明白咋回事<br>
     * <li>如果是 x，z 轴，那么只需要方块起始坐标减去旋转点坐标</li>
     * <li>如果是 y 轴，旋转点坐标减去方块起始坐标，再减去方块的 y 长度</li>
     *
     * @param index 是 xyz 的哪一个，x 是 0，y 是 1，z 是 2
     */
    private float convertOrigin(BonesItem bones, CubesItem cubes, int index) {
        if (index == 1) {
            return bones.getPivot().get(index) - cubes.getOrigin().get(index) - cubes.getSize().get(index);
        } else {
            return cubes.getOrigin().get(index) - bones.getPivot().get(index);
        }
    }

    /**
     * 基岩版用的是度，Java 版用的是弧度，这个转换很简单
     */
    private float convertRotation(float degree) {
        return (float) (degree * Math.PI / 180);
    }
}
