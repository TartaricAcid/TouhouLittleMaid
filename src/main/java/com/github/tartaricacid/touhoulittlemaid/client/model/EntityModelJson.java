package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.github.tartaricacid.touhoulittlemaid.client.animation.pojo.AnimationItem;
import com.github.tartaricacid.touhoulittlemaid.client.animation.pojo.CustomAnimation;
import com.github.tartaricacid.touhoulittlemaid.client.animation.pojo.KeyFrameItem;
import com.github.tartaricacid.touhoulittlemaid.client.model.pojo.BonesItem;
import com.github.tartaricacid.touhoulittlemaid.client.model.pojo.CubesItem;
import com.github.tartaricacid.touhoulittlemaid.client.model.pojo.CustomModelPOJO;
import com.github.tartaricacid.touhoulittlemaid.config.GeneralConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityMarisaBroom;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySuitcase;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.proxy.ClientProxy;
import com.google.common.collect.Maps;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.*;

import static com.github.tartaricacid.touhoulittlemaid.client.animation.EnumAnimationType.ALWAYS;

/**
 * 通过解析基岩版 JSON 实体模型得到的 POJO，将其转换为 ModelBase 类
 *
 * @author TartaricAcid
 * @date 2019/7/9 14:18
 **/
@SideOnly(Side.CLIENT)
public class EntityModelJson extends ModelBase {
    public final AxisAlignedBB renderBoundingBox;
    private final int format;
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
    private List<ModelRenderer> shouldRender = new LinkedList<>();

    private HashMap<String, Float[]> cacheRotation = Maps.newHashMap();

    public EntityModelJson(CustomModelPOJO pojo, int format) {
        this.format = format;
        // 材质的长度、宽度
        textureWidth = pojo.getGeometryModel().getTexturewidth();
        textureHeight = pojo.getGeometryModel().getTextureheight();

        List<Float> offset = pojo.getGeometryModel().getVisibleBoundsOffset();
        float offsetX = offset.get(0);
        float offsetY = offset.get(1);
        float offsetZ = offset.get(2);
        float width = pojo.getGeometryModel().getVisibleBoundsWidth() / 2;
        float height = pojo.getGeometryModel().getVisibleBoundsHeight() / 2;
        renderBoundingBox = new AxisAlignedBB(offsetX - width, offsetY - height, offsetZ - width, offsetX + width, offsetY + height, offsetZ + width);

        // 往 indexBones 里面注入数据，为后续坐标转换做参考
        for (BonesItem bones : pojo.getGeometryModel().getBones()) {
            // 塞索引，这是给后面坐标转换用的
            indexBones.put(bones.getName(), bones);
            // 塞入新建的空 ModelRenderer 实例
            // 因为后面添加 parent 需要，所以先塞空对象，然后二次遍历再进行数据存储
            modelMap.put(bones.getName(), new ModelRenderer(this));
        }

        // 开始往 ModelRenderer 实例里面塞数据
        for (BonesItem bones : pojo.getGeometryModel().getBones()) {
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
                // 为自定义动画缓存的参数
                cacheRotation.put(bones.getName(), new Float[]{model.rotateAngleX, model.rotateAngleY, model.rotateAngleZ});
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
                List<Float> size = cubes.getSize();
                boolean mirror = cubes.isMirror();
                float inflate = cubes.getInflate();

                model.cubeList.add(new ModelBoxFloat(model, uv.get(0), uv.get(1),
                        convertOrigin(bones, cubes, 0), convertOrigin(bones, cubes, 1), convertOrigin(bones, cubes, 2),
                        size.get(0), size.get(1), size.get(2), inflate, mirror));
            }
        }
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        // 如果为 1，开启 cull 功能
        if (format >= 1) {
            GlStateManager.enableCull();
        }
        for (ModelRenderer model : shouldRender) {
            model.render(scale);
        }
        if (format >= 1) {
            GlStateManager.disableCull();
        }
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks,
                                  float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        if (entityIn instanceof EntityMaid) {
            setMaidRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, (EntityMaid) entityIn);
            return;
        }
        if (entityIn instanceof EntityChair) {
            setChairRotationAndOffset((EntityChair) entityIn, ageInTicks);
        }
    }

    private void setChairRotationAndOffset(EntityChair entityIn, float ageInTicks) {
        Optional<CustomAnimation> alwaysAnimation = ClientProxy.CHAIR_MODEL.getAnimation(entityIn.getModelId(), ALWAYS);
        if (alwaysAnimation.isPresent()) {
            float currentTime = ageInTicks / 20;
            for (AnimationItem item : alwaysAnimation.get().getAnimations().values()) {
                applyCustomAnimation(item, currentTime);
            }
        }
    }

    private void applyCustomAnimation(AnimationItem item, float currentTime) {
        if (item.isLoop()) {
            currentTime = currentTime % item.getAnimationLength();
        }
        for (String bone : item.getBones().keySet()) {
            KeyFrameItem keyFrame = item.getBones().get(bone);
            if (modelMap.containsKey(bone)) {
                ModelRenderer renderer = modelMap.get(bone);
                HashMap<Float, Float[]> rotationList = keyFrame.getRotationList();
                HashMap<Float, Float[]> positionList = keyFrame.getPositionList();
                if (rotationList != null && rotationList.size() > 0) {
                    Float[] rotation = linearInterpolation(rotationList, currentTime);
                    Float[] rotationPre = cacheRotation.get(bone);
                    renderer.rotateAngleX = rotation[0] * 0.017453292F - rotationPre[0];
                    renderer.rotateAngleY = rotation[1] * 0.017453292F - rotationPre[1];
                    renderer.rotateAngleZ = rotation[2] * 0.017453292F - rotationPre[2];
                }
                if (positionList != null && positionList.size() > 0) {
                    Float[] position = linearInterpolation(positionList, currentTime);
                    renderer.offsetX = position[0] * 0.0625f;
                    renderer.offsetY = position[1] * 0.0625f;
                    renderer.offsetZ = position[2] * 0.0625f;
                }
            }
        }
    }

    private Float[] linearInterpolation(HashMap<Float, Float[]> keyFrame, float currentTime) {
        Iterator<Float> iterator = keyFrame.keySet().iterator();
        // 因为 keyFrame 必然有至少一个元素，直接调用 next
        Float nextTime = iterator.next();
        for (float preTime : keyFrame.keySet()) {
            // 当前处于第一关键帧的前面，或者最后关键帧的后面
            // 直接返回最近的关键帧
            if (currentTime <= preTime || !iterator.hasNext()) {
                return keyFrame.get(preTime);
            }
            // 下一个关键帧
            nextTime = iterator.next();
            // 当前时间处于两个关键帧之间，就进行插值计算
            if (currentTime <= nextTime) {
                Float[] pre = keyFrame.get(preTime);
                Float[] next = keyFrame.get(nextTime);
                float coefficient = (nextTime - preTime) / currentTime;
                return new Float[]{(next[0] - pre[0]) / coefficient + pre[0], (next[1] - pre[1]) / coefficient + pre[1], (next[2] - pre[2]) / coefficient + pre[2]};
            }
        }
        return new Float[]{0f, 0f, 0f};
    }


    private void setMaidRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks,
                                       float netHeadYaw, float headPitch, float scaleFactor, EntityMaid entityMaid) {
        // 头部
        ModelRenderer head = modelMap.get("head");
        // 腿部
        ModelRenderer legLeft = modelMap.get("legLeft");
        ModelRenderer legRight = modelMap.get("legRight");
        // 手部
        ModelRenderer armLeft = modelMap.get("armLeft");
        ModelRenderer armRight = modelMap.get("armRight");
        // 翅膀
        ModelRenderer wingLeft = modelMap.get("wingLeft");
        ModelRenderer wingRight = modelMap.get("wingRight");
        // 呆毛
        ModelRenderer ahoge = modelMap.get("ahoge");
        // 眨眼
        ModelRenderer blink = modelMap.get("blink");
        // 尾巴
        ModelRenderer tail = modelMap.get("tail");
        // 浮动部件
        ModelRenderer sinFloat = modelMap.get("sinFloat");
        ModelRenderer cosFloat = modelMap.get("cosFloat");
        ModelRenderer negativeSinFloat = modelMap.get("-sinFloat");
        ModelRenderer negativeCosFloat = modelMap.get("-cosFloat");
        // 因为新版本 block bench 软件不支持 - 字符，故改为 _ 字符
        ModelRenderer negativeSinFloat2 = modelMap.get("_sinFloat");
        ModelRenderer negativeCosFloat2 = modelMap.get("_cosFloat");
        // 护甲
        ModelRenderer helmet = modelMap.get("helmet");
        ModelRenderer hat = modelMap.get("hat");
        ModelRenderer chestPlate = modelMap.get("chestPlate");
        ModelRenderer chestPlateLeft = modelMap.get("chestPlateLeft");
        ModelRenderer chestPlateMiddle = modelMap.get("chestPlateMiddle");
        ModelRenderer chestPlateRight = modelMap.get("chestPlateRight");
        ModelRenderer leggings = modelMap.get("leggings");
        ModelRenderer leggingsLeft = modelMap.get("leggingsLeft");
        ModelRenderer leggingsMiddle = modelMap.get("leggingsMiddle");
        ModelRenderer leggingsRight = modelMap.get("leggingsRight");
        ModelRenderer bootsLeft = modelMap.get("bootsLeft");
        ModelRenderer bootsRight = modelMap.get("bootsRight");
        // 反向隐藏护甲
        ModelRenderer reverseHelmet = modelMap.get("-helmet");
        ModelRenderer reverseHat = modelMap.get("-hat");
        ModelRenderer reverseChestPlate = modelMap.get("-chestPlate");
        ModelRenderer reverseChestPlateLeft = modelMap.get("-chestPlateLeft");
        ModelRenderer reverseChestPlateMiddle = modelMap.get("-chestPlateMiddle");
        ModelRenderer reverseChestPlateRight = modelMap.get("-chestPlateRight");
        ModelRenderer reverseLeggings = modelMap.get("-leggings");
        ModelRenderer reverseLeggingsLeft = modelMap.get("-leggingsLeft");
        ModelRenderer reverseLeggingsMiddle = modelMap.get("-leggingsMiddle");
        ModelRenderer reverseLeggingsRight = modelMap.get("-leggingsRight");
        ModelRenderer reverseBootsLeft = modelMap.get("-bootsLeft");
        ModelRenderer reverseBootsRight = modelMap.get("-bootsRight");
        // 因为新版本 block bench 软件不支持 - 字符，故改为 _ 字符
        ModelRenderer reverseHelmet2 = modelMap.get("_helmet");
        ModelRenderer reverseHat2 = modelMap.get("_hat");
        ModelRenderer reverseChestPlate2 = modelMap.get("_chestPlate");
        ModelRenderer reverseChestPlateLeft2 = modelMap.get("_chestPlateLeft");
        ModelRenderer reverseChestPlateMiddle2 = modelMap.get("_chestPlateMiddle");
        ModelRenderer reverseChestPlateRight2 = modelMap.get("_chestPlateRight");
        ModelRenderer reverseLeggings2 = modelMap.get("_leggings");
        ModelRenderer reverseLeggingsLeft2 = modelMap.get("_leggingsLeft");
        ModelRenderer reverseLeggingsMiddle2 = modelMap.get("_leggingsMiddle");
        ModelRenderer reverseLeggingsRight2 = modelMap.get("_leggingsRight");
        ModelRenderer reverseBootsLeft2 = modelMap.get("_bootsLeft");
        ModelRenderer reverseBootsRight2 = modelMap.get("_bootsRight");

        // 动画和姿势
        headAnimation(head, netHeadYaw, headPitch);
        legAnimation(legLeft, legRight, limbSwing, limbSwingAmount);
        armAnimation(entityMaid, armLeft, armRight, limbSwing, limbSwingAmount, ageInTicks);
        wingAnimation(wingLeft, wingRight, ageInTicks);
        blinkAnimation(blink, ageInTicks);
        tailAnimation(tail, ageInTicks);
        floatAnimation(sinFloat, cosFloat, negativeSinFloat, negativeCosFloat, negativeSinFloat2, negativeCosFloat2, ageInTicks);
        renderArmor(entityMaid, helmet, hat, chestPlate, chestPlateLeft, chestPlateMiddle, chestPlateRight,
                leggings, leggingsLeft, leggingsMiddle, leggingsRight, bootsLeft, bootsRight);
        renderReverseArmor(entityMaid, reverseHelmet, reverseHat,
                reverseChestPlate, reverseChestPlateLeft, reverseChestPlateMiddle, reverseChestPlateRight,
                reverseLeggings, reverseLeggingsLeft, reverseLeggingsMiddle, reverseLeggingsRight,
                reverseBootsLeft, reverseBootsRight);
        renderReverseArmor(entityMaid, reverseHelmet2, reverseHat2,
                reverseChestPlate2, reverseChestPlateLeft2, reverseChestPlateMiddle2, reverseChestPlateRight2,
                reverseLeggings2, reverseLeggingsLeft2, reverseLeggingsMiddle2, reverseLeggingsRight2,
                reverseBootsLeft2, reverseBootsRight2);
        beggingPosture(entityMaid, head, ahoge, ageInTicks);
        swingingArmsPosture(entityMaid, armLeft, armRight);

        // 头部复位
        if (head != null) {
            head.offsetY = 0;
        }

        // 因为三者不允许同时存在，所以需要 if 判定
        if (entityMaid.getControllingPassenger() instanceof EntityMarisaBroom || entityMaid.isDebugBroomShow) {
            // 坐在扫帚上时，应用待命的动作
            ridingBroomPosture(head, armLeft, armRight, legLeft, legRight);
        } else if (entityMaid.isRiding()) {
            ridingPosture(legLeft, legRight);
        } else if (entityMaid.isSitting()) {
            sittingPosture(armLeft, armRight, legLeft, legRight);
        }
    }

    private void headAnimation(@Nullable ModelRenderer head, float netHeadYaw, float headPitch) {
        if (head != null) {
            head.rotateAngleX = headPitch * 0.017453292F;
            head.rotateAngleY = netHeadYaw * 0.017453292F;
        }
    }

    private void legAnimation(@Nullable ModelRenderer legLeft, @Nullable ModelRenderer legRight, float limbSwing, float limbSwingAmount) {
        // 左脚右脚的运动
        if (legLeft != null) {
            legLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.67f) * 0.3f * limbSwingAmount;
            legLeft.rotateAngleZ = 0f;
        }
        if (legRight != null) {
            legRight.rotateAngleX = -MathHelper.cos(limbSwing * 0.67f) * 0.3f * limbSwingAmount;
            legRight.rotateAngleZ = 0f;
        }
    }

    private void armAnimation(EntityMaid entityMaid, @Nullable ModelRenderer armLeft, @Nullable ModelRenderer armRight,
                              float limbSwing, float limbSwingAmount, float ageInTicks) {
        // 使用动画的数据预处理，来自 ModelBiped
        float f1 = 1.0F - this.swingProgress;
        f1 = f1 * f1;
        f1 = f1 * f1;
        f1 = 1.0F - f1;
        float f2 = MathHelper.sin(f1 * (float) Math.PI);
        float f3 = MathHelper.sin(this.swingProgress * (float) Math.PI) * -0.7F * 0.75F;

        if (armLeft != null) {
            // 左手右手的运动（这一处还有一个功能，即对数据进行归位）
            if (entityMaid.getControllingPassenger() instanceof EntitySuitcase) {
                armLeft.rotateAngleX = 0.5f;
                armLeft.rotateAngleY = 0f;
                armLeft.rotateAngleZ = -0.395f;
            } else {
                armLeft.rotateAngleX = -MathHelper.cos(limbSwing * 0.67f) * 0.7F * limbSwingAmount;
                armLeft.rotateAngleY = 0f;
                armLeft.rotateAngleZ = MathHelper.cos(ageInTicks * 0.05f) * 0.05f - 0.4f;

                // 手部使用动画
                if (swingProgress > 0.0F && getSwingingHand(entityMaid) == EnumHandSide.LEFT) {
                    armLeft.rotateAngleX = (float) ((double) armLeft.rotateAngleX - ((double) f2 * 1.2D + (double) f3));
                    armLeft.rotateAngleZ += MathHelper.sin(this.swingProgress * (float) Math.PI) * -0.4F;
                }
            }
        }

        if (armRight != null) {
            // 左手右手的运动（这一处还有一个功能，即对数据进行归位）
            armRight.rotateAngleX = MathHelper.cos(limbSwing * 0.67f) * 0.7F * limbSwingAmount;
            armRight.rotateAngleY = 0f;
            armRight.rotateAngleZ = -MathHelper.cos(ageInTicks * 0.05f) * 0.05f + 0.4f;

            // 手部使用动画
            if (swingProgress > 0.0F && getSwingingHand(entityMaid) == EnumHandSide.RIGHT) {
                armRight.rotateAngleX = (float) ((double) armRight.rotateAngleX - ((double) f2 * 1.2D + (double) f3));
                armRight.rotateAngleZ += MathHelper.sin(this.swingProgress * (float) Math.PI) * -0.4F;
            }
        }
    }

    private void wingAnimation(@Nullable ModelRenderer wingLeft, @Nullable ModelRenderer wingRight, float ageInTicks) {
        if (wingLeft != null) {
            wingLeft.rotateAngleY = -MathHelper.cos(ageInTicks * 0.3f) * 0.2f + 1.0f;
        }
        if (wingRight != null) {
            wingRight.rotateAngleY = MathHelper.cos(ageInTicks * 0.3f) * 0.2f - 1.0f;
        }
    }

    private void blinkAnimation(@Nullable ModelRenderer blink, float ageInTicks) {
        if (blink != null) {
            float remainder = ageInTicks % 60;
            // 0-10 显示眨眼贴图
            blink.isHidden = !(55 < remainder && remainder < 60);
        }
    }

    private void tailAnimation(@Nullable ModelRenderer tail, float ageInTicks) {
        if (tail != null) {
            tail.rotateAngleZ = MathHelper.cos(ageInTicks * 0.2f) * 0.1f;
            tail.rotateAngleX = MathHelper.sin(ageInTicks * 0.2f) * 0.05f;
        }
    }

    private void floatAnimation(@Nullable ModelRenderer sinFloat, @Nullable ModelRenderer cosFloat,
                                @Nullable ModelRenderer negativeSinFloat, @Nullable ModelRenderer negativeCosFloat,
                                @Nullable ModelRenderer negativeSinFloat2, @Nullable ModelRenderer negativeCosFloat2,
                                float ageInTicks) {
        if (sinFloat != null) {
            sinFloat.offsetY = MathHelper.sin(ageInTicks * 0.1f) * 0.05f;
        }
        if (cosFloat != null) {
            cosFloat.offsetY = MathHelper.cos(ageInTicks * 0.1f) * 0.05f;
        }
        if (negativeSinFloat != null) {
            negativeSinFloat.offsetY = -MathHelper.sin(ageInTicks * 0.1f) * 0.05f;
        }
        if (negativeSinFloat2 != null) {
            negativeSinFloat2.offsetY = -MathHelper.sin(ageInTicks * 0.1f) * 0.05f;
        }
        if (negativeCosFloat != null) {
            negativeCosFloat.offsetY = -MathHelper.cos(ageInTicks * 0.1f) * 0.05f;
        }
        if (negativeCosFloat2 != null) {
            negativeCosFloat2.offsetY = -MathHelper.cos(ageInTicks * 0.1f) * 0.05f;
        }
    }

    private void renderArmor(EntityMaid entityMaid, @Nullable ModelRenderer helmet, @Nullable ModelRenderer hat,
                             @Nullable ModelRenderer chestPlate, @Nullable ModelRenderer chestPlateLeft, @Nullable ModelRenderer chestPlateMiddle, @Nullable ModelRenderer chestPlateRight,
                             @Nullable ModelRenderer leggings, @Nullable ModelRenderer leggingsLeft, @Nullable ModelRenderer leggingsMiddle, @Nullable ModelRenderer leggingsRight,
                             @Nullable ModelRenderer bootsLeft, @Nullable ModelRenderer bootsRight) {
        // 护甲部分渲染
        if (helmet != null) {
            helmet.isHidden = entityMaid.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty();
        }
        if (hat != null) {
            hat.isHidden = !GeneralConfig.MAID_CONFIG.maidAlwaysShowHat && entityMaid.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty();
        }
        if (chestPlate != null) {
            chestPlate.isHidden = entityMaid.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty();
        }
        if (chestPlateLeft != null) {
            chestPlateLeft.isHidden = entityMaid.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty();
        }
        if (chestPlateMiddle != null) {
            chestPlateMiddle.isHidden = entityMaid.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty();
        }
        if (chestPlateRight != null) {
            chestPlateRight.isHidden = entityMaid.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty();
        }
        if (leggings != null) {
            leggings.isHidden = entityMaid.getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty();
        }
        if (leggingsLeft != null) {
            leggingsLeft.isHidden = entityMaid.getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty();
        }
        if (leggingsMiddle != null) {
            leggingsMiddle.isHidden = entityMaid.getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty();
        }
        if (leggingsRight != null) {
            leggingsRight.isHidden = entityMaid.getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty();
        }
        if (bootsLeft != null) {
            bootsLeft.isHidden = entityMaid.getItemStackFromSlot(EntityEquipmentSlot.FEET).isEmpty();
        }
        if (bootsRight != null) {
            bootsRight.isHidden = entityMaid.getItemStackFromSlot(EntityEquipmentSlot.FEET).isEmpty();
        }
    }

    private void renderReverseArmor(EntityMaid entityMaid, @Nullable ModelRenderer reverseHelmet, @Nullable ModelRenderer reverseHat,
                                    @Nullable ModelRenderer reverseChestPlate, @Nullable ModelRenderer reverseChestPlateLeft, @Nullable ModelRenderer reverseChestPlateMiddle, @Nullable ModelRenderer reverseChestPlateRight,
                                    @Nullable ModelRenderer reverseLeggings, @Nullable ModelRenderer reverseLeggingsLeft, @Nullable ModelRenderer reverseLeggingsMiddle, @Nullable ModelRenderer reverseLeggingsRight,
                                    @Nullable ModelRenderer reverseBootsLeft, @Nullable ModelRenderer reverseBootsRight) {
        // 护甲部分渲染
        if (reverseHelmet != null) {
            reverseHelmet.isHidden = !entityMaid.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty();
        }
        if (reverseHat != null) {
            reverseHat.isHidden = GeneralConfig.MAID_CONFIG.maidAlwaysShowHat || !entityMaid.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty();
        }
        if (reverseChestPlate != null) {
            reverseChestPlate.isHidden = !entityMaid.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty();
        }
        if (reverseChestPlateLeft != null) {
            reverseChestPlateLeft.isHidden = !entityMaid.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty();
        }
        if (reverseChestPlateMiddle != null) {
            reverseChestPlateMiddle.isHidden = !entityMaid.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty();
        }
        if (reverseChestPlateRight != null) {
            reverseChestPlateRight.isHidden = !entityMaid.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty();
        }
        if (reverseLeggings != null) {
            reverseLeggings.isHidden = !entityMaid.getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty();
        }
        if (reverseLeggingsLeft != null) {
            reverseLeggingsLeft.isHidden = !entityMaid.getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty();
        }
        if (reverseLeggingsMiddle != null) {
            reverseLeggingsMiddle.isHidden = !entityMaid.getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty();
        }
        if (reverseLeggingsRight != null) {
            reverseLeggingsRight.isHidden = !entityMaid.getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty();
        }
        if (reverseBootsLeft != null) {
            reverseBootsLeft.isHidden = !entityMaid.getItemStackFromSlot(EntityEquipmentSlot.FEET).isEmpty();
        }
        if (reverseBootsRight != null) {
            reverseBootsRight.isHidden = !entityMaid.getItemStackFromSlot(EntityEquipmentSlot.FEET).isEmpty();
        }
    }

    private void beggingPosture(EntityMaid entityMaid, @Nullable ModelRenderer head, @Nullable ModelRenderer ahoge, float ageInTicks) {
        if (entityMaid.isBegging()) {
            if (head != null) {
                head.rotateAngleZ = 0.139f;
            }
            if (ahoge != null) {
                ahoge.rotateAngleX = MathHelper.cos(ageInTicks * 1.0f) * 0.05f;
                ahoge.rotateAngleZ = MathHelper.sin(ageInTicks * 1.0f) * 0.05f;
            }
        } else {
            if (head != null) {
                head.rotateAngleZ = 0f;
            }
            if (ahoge != null) {
                ahoge.rotateAngleZ = 0f;
            }
        }
    }

    private void swingingArmsPosture(EntityMaid entityMaid, @Nullable ModelRenderer armLeft, @Nullable ModelRenderer armRight) {
        if (entityMaid.isSwingingArms()) {
            if (armLeft != null) {
                armLeft.rotateAngleX = -1.396f;
                armLeft.rotateAngleY = 0.785f;
            }
            if (armRight != null) {
                armRight.rotateAngleX = -1.396f;
                armRight.rotateAngleY = -0.174f;
            }
        }
    }

    private void sittingPosture(@Nullable ModelRenderer armLeft, @Nullable ModelRenderer armRight,
                                @Nullable ModelRenderer legLeft, @Nullable ModelRenderer legRight) {
        if (armLeft != null) {
            armLeft.rotateAngleX = -0.798f;
            armLeft.rotateAngleZ = 0.274f;
        }
        if (armRight != null) {
            armRight.rotateAngleX = -0.798f;
            armRight.rotateAngleZ = -0.274f;
        }
        if (legLeft != null) {
            legLeft.rotateAngleX = -0.960f;
            legLeft.rotateAngleZ = -0.523f;
        }
        if (legRight != null) {
            legRight.rotateAngleX = -0.960f;
            legRight.rotateAngleZ = 0.523f;
        }
        GlStateManager.translate(0, 0.3f, 0);
    }

    private void ridingBroomPosture(@Nullable ModelRenderer head, @Nullable ModelRenderer armLeft, @Nullable ModelRenderer armRight,
                                    @Nullable ModelRenderer legLeft, @Nullable ModelRenderer legRight) {
        sittingPosture(armLeft, armRight, legLeft, legRight);
        if (head != null) {
            head.rotateAngleX -= 30f * (float) Math.PI / 180;
            head.offsetY = 0.0625f;
        }
        GlStateManager.rotate(30, 1, 0, 0);
        GlStateManager.translate(0, -0.4, -0.3);
    }

    private void ridingPosture(@Nullable ModelRenderer legLeft, @Nullable ModelRenderer legRight) {
        if (legLeft != null) {
            legLeft.rotateAngleX = -0.960f;
            legLeft.rotateAngleZ = -0.523f;
        }
        if (legRight != null) {
            legRight.rotateAngleX = -0.960f;
            legRight.rotateAngleZ = 0.523f;
        }

        GlStateManager.translate(0, 0.3f, 0);
    }

    private void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public boolean hasBackpackPositioningModel() {
        return modelMap.get("backpackPositioningBone") != null;
    }

    public ModelRenderer getBackpackPositioningModel() {
        return modelMap.get("backpackPositioningBone");
    }

    public boolean hasArmPositioningModel(EnumHandSide side) {
        ModelRenderer arm = (side == EnumHandSide.LEFT ? modelMap.get("armLeftPositioningBone") : modelMap.get("armRightPositioningBone"));
        return arm != null;
    }

    public void postRenderArmPositioningModel(float scale, EnumHandSide side) {
        ModelRenderer arm = (side == EnumHandSide.LEFT ? modelMap.get("armLeftPositioningBone") : modelMap.get("armRightPositioningBone"));
        if (arm != null) {
            arm.postRender(scale);
        }
    }

    public void postRenderArm(float scale, EnumHandSide side) {
        ModelRenderer arm = (side == EnumHandSide.LEFT ? modelMap.get("armLeft") : modelMap.get("armRight"));
        if (arm != null) {
            arm.postRender(scale);
        }
    }

    public void postRenderCustomHead(float scale) {
        ModelRenderer customHead = modelMap.get("head");
        if (customHead != null) {
            customHead.postRender(scale);
        }
    }

    /**
     * 获取现在使用的是主手还是副手<br>
     * 如果是主手，那就返回右边
     */
    private EnumHandSide getSwingingHand(EntityMaid entityIn) {
        return entityIn.swingingHand == EnumHand.MAIN_HAND ? EnumHandSide.RIGHT : EnumHandSide.LEFT;
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

    /**
     * 基岩版和 Java 版本的方块起始坐标也不一致，Java 是相对坐标，而且 y 值方向不一致。
     * 基岩版是绝对坐标，而且 y 方向朝上。
     * 其实两者规律很简单，但是我找了一下午，才明白咋回事。
     * <li>如果是 x，z 轴，那么只需要方块起始坐标减去旋转点坐标
     * <li>如果是 y 轴，旋转点坐标减去方块起始坐标，再减去方块的 y 长度
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
