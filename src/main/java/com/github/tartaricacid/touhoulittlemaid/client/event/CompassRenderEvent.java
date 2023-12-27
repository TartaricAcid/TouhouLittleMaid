package com.github.tartaricacid.touhoulittlemaid.client.event;


import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemKappaCompass;
import com.github.tartaricacid.touhoulittlemaid.util.RenderHelper;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Dist.CLIENT)
public class CompassRenderEvent {
    @SubscribeEvent
    public static void onRender(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) {
                return;
            }
            ItemStack stack = mc.player.getMainHandItem();
            if (stack.getItem() != InitItems.KAPPA_COMPASS.get()) {
                stack = mc.player.getOffhandItem();
                if (stack.getItem() != InitItems.KAPPA_COMPASS.get()) {
                    return;
                }
            }
            if (!ItemKappaCompass.hasKappaCompassData(stack)) {
                return;
            }
            BlockPos workPos = ItemKappaCompass.getPoint(Activity.WORK, stack);
            Vec3 camera = event.getCamera().getPosition().reverse();
            event.getPoseStack().pushPose();
            event.getPoseStack().translate(0, 1, 0);
            if (workPos != null) {
                Vec3 centerPos = camera.add(workPos.getX() + 0.5, workPos.getY() + 0.5, workPos.getZ() + 0.5);
                double radius = MaidConfig.MAID_WORK_RANGE.get() + 0.1;
                VertexConsumer buffer = mc.renderBuffers().bufferSource().getBuffer(RenderType.LINES);
                RenderHelper.renderCylinder(event.getPoseStack(), buffer, centerPos, radius, 16, 1.0F, 0, 0);
                Vec3 textPos = new Vec3(workPos.getX() + 0.5, workPos.getY() + 0.5, workPos.getZ() + 0.5);
                String text = I18n.get("message.touhou_little_maid.kappa_compass.work_area");
                RenderHelper.renderFloatingText(event.getPoseStack(), text, textPos, 0xff1111, 0.15f, -5);
                RenderHelper.renderFloatingText(event.getPoseStack(), "▼", textPos, 0xff1111, 0.15f, 5);
            }

            BlockPos idlePos = ItemKappaCompass.getPoint(Activity.IDLE, stack);
            if (idlePos != null) {
                Vec3 centerPos = camera.add(idlePos.getX() + 0.5, idlePos.getY() + 0.5, idlePos.getZ() + 0.5);
                double radius = MaidConfig.MAID_IDLE_RANGE.get();
                VertexConsumer buffer = mc.renderBuffers().bufferSource().getBuffer(RenderType.LINES);
                RenderHelper.renderCylinder(event.getPoseStack(), buffer, centerPos, radius, 16, 0, 1.0F, 0);
                Vec3 textPos = new Vec3(idlePos.getX() + 0.5, idlePos.getY() + 0.5, idlePos.getZ() + 0.5);
                if (idlePos.equals(workPos)) {
                    textPos = textPos.add(0, 1, 0);
                } else if (workPos != null) {
                    Vec3 prePos = camera.add(workPos.getX() + 0.5, workPos.getY() + 0.5, workPos.getZ() + 0.5);
                    RenderHelper.renderLine(event.getPoseStack(), buffer, centerPos, prePos, 1.0f, 1.0f, 1.0f);
                }
                String text = I18n.get("message.touhou_little_maid.kappa_compass.idle_area");
                RenderHelper.renderFloatingText(event.getPoseStack(), text, textPos, 0x11ff11, 0.15f, -5);
                RenderHelper.renderFloatingText(event.getPoseStack(), "▼", textPos, 0x11ff11, 0.15f, 5);
            }

            BlockPos resetPos = ItemKappaCompass.getPoint(Activity.REST, stack);
            if (resetPos != null) {
                Vec3 centerPos = camera.add(resetPos.getX() + 0.5, resetPos.getY() + 0.5, resetPos.getZ() + 0.5);
                double radius = MaidConfig.MAID_SLEEP_RANGE.get() - 0.1;
                VertexConsumer buffer = mc.renderBuffers().bufferSource().getBuffer(RenderType.LINES);
                RenderHelper.renderCylinder(event.getPoseStack(), buffer, centerPos, radius, 16, 0, 0, 1.0F);
                Vec3 textPos = new Vec3(resetPos.getX() + 0.5, resetPos.getY() + 0.5, resetPos.getZ() + 0.5);
                if (resetPos.equals(idlePos)) {
                    textPos = textPos.add(0, 2, 0);
                } else if (idlePos != null) {
                    Vec3 prePos = camera.add(idlePos.getX() + 0.5, idlePos.getY() + 0.5, idlePos.getZ() + 0.5);
                    RenderHelper.renderLine(event.getPoseStack(), buffer, centerPos, prePos, 1.0f, 1.0f, 1.0f);
                }
                String text = I18n.get("message.touhou_little_maid.kappa_compass.sleep_area");
                RenderHelper.renderFloatingText(event.getPoseStack(), text, textPos, 0x1111ff, 0.15f, -5);
                RenderHelper.renderFloatingText(event.getPoseStack(), "▼", textPos, 0x1111ff, 0.15f, 5);
            }
            event.getPoseStack().popPose();
        }
    }
}
