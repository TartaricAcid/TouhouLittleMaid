//package com.github.tartaricacid.touhoulittlemaid.compat.slashblade;
//
//import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
//import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoBone;
//import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoModel;
//import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.RenderUtils;
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.math.Axis;
//import mods.flammpfeil.slashblade.capability.slashblade.CapabilitySlashBlade;
//import mods.flammpfeil.slashblade.client.renderer.model.BladeModelManager;
//import mods.flammpfeil.slashblade.client.renderer.model.obj.WavefrontObject;
//import mods.flammpfeil.slashblade.client.renderer.util.BladeRenderState;
//import mods.flammpfeil.slashblade.item.ItemSlashBlade;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.HumanoidArm;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.Mob;
//import net.minecraft.world.item.ItemStack;
//import net.neoforged.api.distmarker.Dist;
//import net.neoforged.api.distmarker.OnlyIn;
//
//@OnlyIn(Dist.CLIENT)
//public class SlashBladeRender {
//    private static final ResourceLocation RESOURCE_DEFAULT_MODEL = new ResourceLocation("slashblade", "model/blade.obj");
//    private static final ResourceLocation RESOURCE_DEFAULT_TEXTURE = new ResourceLocation("slashblade", "model/blade.png");
//
//    public static void renderSlashBlade(PoseStack matrixStack, MultiBufferSource bufferIn, int lightIn, ItemStack stack) {
//        if (stack.isEmpty()) {
//            return;
//        }
//        stack.getCapability(CapabilitySlashBlade.BLADESTATE).ifPresent(bladeState -> {
//            ResourceLocation texture = bladeState.getTexture().orElse(RESOURCE_DEFAULT_TEXTURE);
//            WavefrontObject obj = BladeModelManager.getInstance().getModel(bladeState.getModel().orElse(null));
//            String part;
//            if (bladeState.isBroken()) {
//                part = "blade_damaged";
//            } else {
//                part = "blade";
//            }
//            BladeRenderState.renderOverrided(stack, obj, part, texture, matrixStack, bufferIn, lightIn);
//            BladeRenderState.renderOverridedLuminous(stack, obj, part + "_luminous", texture, matrixStack, bufferIn, lightIn);
//            BladeRenderState.renderOverrided(stack, obj, "sheath", texture, matrixStack, bufferIn, lightIn);
//            BladeRenderState.renderOverridedLuminous(stack, obj, "sheath_luminous", texture, matrixStack, bufferIn, lightIn);
//        });
//    }
//
//    public static void renderMaidBackSlashBlade(PoseStack matrixStack, MultiBufferSource bufferIn, int lightIn, ItemStack stack) {
//        matrixStack.translate(0.9, -0.25, 0.05);
//        matrixStack.mulPose(Axis.ZN.rotationDegrees(15));
//        matrixStack.scale(0.007F, 0.007F, 0.007F);
//        renderSlashBlade(matrixStack, bufferIn, lightIn, stack);
//    }
//
//    public static void renderGeckoMaidBackSlashBlade(PoseStack matrixStack, MultiBufferSource bufferIn, int lightIn, ItemStack stack) {
//        matrixStack.translate(1.25, -0.25, 0);
//        matrixStack.mulPose(Axis.ZN.rotationDegrees(15));
//        matrixStack.scale(0.01F, 0.01F, 0.01F);
//        renderSlashBlade(matrixStack, bufferIn, lightIn, stack);
//    }
//
//    public static void renderMaidMainhandSlashBlade(Mob maid, BedrockModel<Mob> model, PoseStack matrixStack, MultiBufferSource bufferIn, int lightIn, ItemStack stack, float partialTicks) {
//        if (stack.getItem() instanceof ItemSlashBlade) {
//            matrixStack.pushPose();
//            // 主手的刀渲染在左边
//            if (model.hasWaistPositioningModel(HumanoidArm.LEFT)) {
//                model.translateToPositioningWaist(HumanoidArm.LEFT, matrixStack);
//            } else {
//                matrixStack.translate(0.25, 0.85, 0);
//                matrixStack.mulPose(Axis.XP.rotationDegrees(-20));
//            }
//            matrixStack.translate(0, 0, -0.5);
//            matrixStack.scale(0.007F, 0.007F, 0.007F);
//            matrixStack.mulPose(Axis.YP.rotationDegrees(90));
//            if (stack.isEmpty()) {
//                return;
//            }
//            stack.getCapability(CapabilitySlashBlade.BLADESTATE).ifPresent(bladeState -> {
//                ResourceLocation texture = bladeState.getTexture().orElse(RESOURCE_DEFAULT_TEXTURE);
//                WavefrontObject obj = BladeModelManager.getInstance().getModel(bladeState.getModel().orElse(RESOURCE_DEFAULT_MODEL));
//                String part;
//                if (bladeState.isBroken()) {
//                    part = "blade_damaged";
//                } else {
//                    part = "blade";
//                }
//                BladeRenderState.renderOverrided(stack, obj, "sheath", texture, matrixStack, bufferIn, lightIn);
//                BladeRenderState.renderOverridedLuminous(stack, obj, "sheath_luminous", texture, matrixStack, bufferIn, lightIn);
//                long time = maid.level.getGameTime() - bladeState.getLastActionTime();
//                if (time < 5) {
//                    float i = time + partialTicks;
//                    matrixStack.translate(0, 0, -0.5 / 0.007);
//                    matrixStack.mulPose(Axis.YP.rotationDegrees(60 + i * 48));
//                    matrixStack.mulPose(Axis.XP.rotationDegrees(90));
//                }
//                BladeRenderState.renderOverrided(stack, obj, part, texture, matrixStack, bufferIn, lightIn);
//                BladeRenderState.renderOverridedLuminous(stack, obj, part + "_luminous", texture, matrixStack, bufferIn, lightIn);
//            });
//            matrixStack.popPose();
//        }
//    }
//
//    public static void renderMaidMainhandSlashBlade(LivingEntity maid, GeoModel model, PoseStack matrixStack, MultiBufferSource bufferIn, int lightIn, ItemStack stack, float partialTicks) {
//        if (stack.getItem() instanceof ItemSlashBlade) {
//            matrixStack.pushPose();
//            // 主手的刀渲染在左边
//            if (!model.leftWaistBones.isEmpty()) {
//                translateToWaist(HumanoidArm.LEFT, matrixStack, model);
//            } else {
//                matrixStack.translate(-0.25, 1.25, 0);
//                matrixStack.mulPose(Axis.XP.rotationDegrees(20));
//            }
//            matrixStack.translate(0, 0, -0.7);
//            matrixStack.scale(0.01F, 0.01F, 0.01F);
//            matrixStack.mulPose(Axis.YP.rotationDegrees(-90));
//            matrixStack.mulPose(Axis.ZP.rotationDegrees(180));
//            if (stack.isEmpty()) {
//                return;
//            }
//            stack.getCapability(CapabilitySlashBlade.BLADESTATE).ifPresent(bladeState -> {
//                ResourceLocation texture = bladeState.getTexture().orElse(RESOURCE_DEFAULT_TEXTURE);
//                WavefrontObject obj = BladeModelManager.getInstance().getModel(bladeState.getModel().orElse(RESOURCE_DEFAULT_MODEL));
//                String part;
//                if (bladeState.isBroken()) {
//                    part = "blade_damaged";
//                } else {
//                    part = "blade";
//                }
//                BladeRenderState.renderOverrided(stack, obj, "sheath", texture, matrixStack, bufferIn, lightIn);
//                BladeRenderState.renderOverridedLuminous(stack, obj, "sheath_luminous", texture, matrixStack, bufferIn, lightIn);
//                long time = maid.level.getGameTime() - bladeState.getLastActionTime();
//                if (time < 5) {
//                    float i = time + partialTicks;
//                    matrixStack.translate(0, 0, -0.5 / 0.007);
//                    matrixStack.mulPose(Axis.YP.rotationDegrees(60 + i * 48));
//                    matrixStack.mulPose(Axis.XP.rotationDegrees(90));
//                }
//                BladeRenderState.renderOverrided(stack, obj, part, texture, matrixStack, bufferIn, lightIn);
//                BladeRenderState.renderOverridedLuminous(stack, obj, part + "_luminous", texture, matrixStack, bufferIn, lightIn);
//            });
//            matrixStack.popPose();
//        }
//    }
//
//    public static void renderMaidOffhandSlashBlade(BedrockModel<Mob> model, PoseStack matrixStack, MultiBufferSource bufferIn, int lightIn, ItemStack stack) {
//        if (stack.getItem() instanceof ItemSlashBlade) {
//            matrixStack.pushPose();
//            // 副手的刀渲染在右边
//            if (model.hasWaistPositioningModel(HumanoidArm.RIGHT)) {
//                model.translateToPositioningWaist(HumanoidArm.RIGHT, matrixStack);
//            } else {
//                matrixStack.translate(-0.25, 0.85, 0);
//                matrixStack.mulPose(Axis.XP.rotationDegrees(-5));
//            }
//            matrixStack.translate(0, 0, -0.5);
//            matrixStack.scale(0.007F, 0.007F, 0.007F);
//            matrixStack.mulPose(Axis.YP.rotationDegrees(90));
//            SlashBladeRender.renderSlashBlade(matrixStack, bufferIn, lightIn, stack);
//            matrixStack.popPose();
//        }
//    }
//
//    public static void renderMaidOffhandSlashBlade(GeoModel model, PoseStack matrixStack, MultiBufferSource bufferIn, int lightIn, ItemStack stack) {
//        if (stack.getItem() instanceof ItemSlashBlade) {
//            matrixStack.pushPose();
//            // 副手的刀渲染在右边
//            if (!model.rightWaistBones.isEmpty()) {
//                translateToWaist(HumanoidArm.RIGHT, matrixStack, model);
//            } else {
//                matrixStack.translate(0.25, 1.25, 0);
//                matrixStack.mulPose(Axis.XP.rotationDegrees(5));
//            }
//            matrixStack.translate(0, 0, -0.7);
//            matrixStack.scale(0.01F, 0.01F, 0.01F);
//            matrixStack.mulPose(Axis.YP.rotationDegrees(-90));
//            matrixStack.mulPose(Axis.ZP.rotationDegrees(180));
//            SlashBladeRender.renderSlashBlade(matrixStack, bufferIn, lightIn, stack);
//            matrixStack.popPose();
//        }
//    }
//
//    private static void translateToWaist(HumanoidArm arm, PoseStack poseStack, GeoModel geoModel) {
//        if (arm == HumanoidArm.LEFT) {
//            int size = geoModel.leftWaistBones.size();
//            for (int i = 0; i < size - 1; i++) {
//                RenderUtils.prepMatrixForBone(poseStack, geoModel.leftWaistBones.get(i));
//            }
//            GeoBone lastBone = geoModel.leftWaistBones.get(size - 1);
//            RenderUtils.translateMatrixToBone(poseStack, lastBone);
//            RenderUtils.translateToPivotPoint(poseStack, lastBone);
//            RenderUtils.rotateMatrixAroundBone(poseStack, lastBone);
//            RenderUtils.scaleMatrixForBone(poseStack, lastBone);
//        } else {
//            int size = geoModel.rightWaistBones.size();
//            for (int i = 0; i < size - 1; i++) {
//                RenderUtils.prepMatrixForBone(poseStack, geoModel.rightWaistBones.get(i));
//            }
//            GeoBone lastBone = geoModel.rightWaistBones.get(size - 1);
//            RenderUtils.translateMatrixToBone(poseStack, lastBone);
//            RenderUtils.translateToPivotPoint(poseStack, lastBone);
//            RenderUtils.rotateMatrixAroundBone(poseStack, lastBone);
//            RenderUtils.scaleMatrixForBone(poseStack, lastBone);
//        }
//    }
//}
