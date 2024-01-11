package com.github.tartaricacid.touhoulittlemaid.client.event;


import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemKappaCompass;
import com.github.tartaricacid.touhoulittlemaid.util.RenderHelper;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Dist.CLIENT)
public class CompassRenderEvent {
    @SubscribeEvent
    public static void onRender(RenderWorldLastEvent event) {
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
        ResourceLocation dimension = ItemKappaCompass.getDimension(stack);
        if (dimension != null && !mc.player.level.dimension().location().equals(dimension)) {
            return;
        }
        BlockPos workPos = ItemKappaCompass.getPoint(Activity.WORK, stack);
        Vector3d camera = mc.gameRenderer.getMainCamera().getPosition().reverse();
        event.getMatrixStack().pushPose();
        event.getMatrixStack().translate(0, 1, 0);
        if (workPos != null) {
            Vector3d centerPos = camera.add(workPos.getX() + 0.5, workPos.getY() + 0.5, workPos.getZ() + 0.5);
            double radius = MaidConfig.MAID_WORK_RANGE.get() + 0.1;
            IVertexBuilder buffer = mc.renderBuffers().bufferSource().getBuffer(RenderType.LINES);
            RenderHelper.renderCylinder(event.getMatrixStack(), buffer, centerPos, radius, 16, 1.0F, 0, 0);
            Vector3d textPos = new Vector3d(workPos.getX() + 0.5, workPos.getY() + 2, workPos.getZ() + 0.5);
            String text = I18n.get("message.touhou_little_maid.kappa_compass.work_area");
            RenderHelper.renderFloatingText(event.getMatrixStack(), text, textPos.x, textPos.y, textPos.z, 0xff1111, 0.15f, true, -5, false);
            RenderHelper.renderFloatingText(event.getMatrixStack(), "▼", textPos.x, textPos.y, textPos.z, 0xff1111, 0.15f, true, 5, false);
        }

        BlockPos idlePos = ItemKappaCompass.getPoint(Activity.IDLE, stack);
        if (idlePos != null) {
            Vector3d centerPos = camera.add(idlePos.getX() + 0.5, idlePos.getY() + 0.5, idlePos.getZ() + 0.5);
            double radius = MaidConfig.MAID_IDLE_RANGE.get();
            IVertexBuilder buffer = mc.renderBuffers().bufferSource().getBuffer(RenderType.LINES);
            RenderHelper.renderCylinder(event.getMatrixStack(), buffer, centerPos, radius, 16, 0, 1.0F, 0);
            Vector3d textPos = new Vector3d(idlePos.getX() + 0.5, idlePos.getY() + 2, idlePos.getZ() + 0.5);
            if (idlePos.equals(workPos)) {
                textPos = textPos.add(0, 1, 0);
            } else if (workPos != null) {
                Vector3d prePos = camera.add(workPos.getX() + 0.5, workPos.getY() + 0.5, workPos.getZ() + 0.5);
                RenderHelper.renderLine(event.getMatrixStack(), buffer, centerPos, prePos, 1.0f, 1.0f, 1.0f);
            }
            String text = I18n.get("message.touhou_little_maid.kappa_compass.idle_area");
            RenderHelper.renderFloatingText(event.getMatrixStack(), text, textPos.x, textPos.y, textPos.z, 0x11ff11, 0.15f, true, -5, false);
            RenderHelper.renderFloatingText(event.getMatrixStack(), "▼", textPos.x, textPos.y, textPos.z, 0x11ff11, 0.15f, true, 5, false);
        }

        BlockPos resetPos = ItemKappaCompass.getPoint(Activity.REST, stack);
        if (resetPos != null) {
            Vector3d centerPos = camera.add(resetPos.getX() + 0.5, resetPos.getY() + 0.5, resetPos.getZ() + 0.5);
            double radius = MaidConfig.MAID_SLEEP_RANGE.get() - 0.1;
            IVertexBuilder buffer = mc.renderBuffers().bufferSource().getBuffer(RenderType.LINES);
            RenderHelper.renderCylinder(event.getMatrixStack(), buffer, centerPos, radius, 16, 0, 0, 1.0F);
            Vector3d textPos = new Vector3d(resetPos.getX() + 0.5, resetPos.getY() + 2, resetPos.getZ() + 0.5);
            if (resetPos.equals(idlePos)) {
                textPos = textPos.add(0, 2, 0);
            } else if (idlePos != null && workPos != null) {
                Vector3d prePos = camera.add(idlePos.getX() + 0.5, idlePos.getY() + 0.5, idlePos.getZ() + 0.5);
                RenderHelper.renderLine(event.getMatrixStack(), buffer, centerPos, prePos, 1.0f, 1.0f, 1.0f);
                prePos = camera.add(workPos.getX() + 0.5, workPos.getY() + 0.5, workPos.getZ() + 0.5);
                RenderHelper.renderLine(event.getMatrixStack(), buffer, centerPos, prePos, 1.0f, 1.0f, 1.0f);
            }
            String text = I18n.get("message.touhou_little_maid.kappa_compass.sleep_area");
            RenderHelper.renderFloatingText(event.getMatrixStack(), text, textPos.x, textPos.y, textPos.z, 0x1111ff, 0.15f, true, -5, false);
            RenderHelper.renderFloatingText(event.getMatrixStack(), "▼", textPos.x, textPos.y, textPos.z, 0x1111ff, 0.15f, true, 5, false);
        }
        event.getMatrixStack().popPose();
    }
}
