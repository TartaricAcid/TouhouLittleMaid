package com.github.tartaricacid.touhoulittlemaid.client.event;


import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemKappaCompass;
import com.github.tartaricacid.touhoulittlemaid.util.RenderHelper;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
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
                return;
            }
            if (!ItemKappaCompass.hasKappaCompassData(stack)) {
                return;
            }
            BlockPos workPos = ItemKappaCompass.getPoint(Activity.WORK, stack);
            if (workPos != null) {
                Vec3 camera = event.getCamera().getPosition().reverse();
                Vec3 centerPos = camera.add(workPos.getX(), workPos.getY(), workPos.getZ());
                int radius = MaidConfig.MAID_WORK_RANGE.get();

                VertexConsumer buffer = mc.renderBuffers().bufferSource().getBuffer(RenderType.LINES);
                RenderHelper.renderCylinder(event.getPoseStack(), buffer, centerPos.add(0, 2, 0), radius, 32, 1.0F, 0, 0);
                RenderHelper.renderCylinder(event.getPoseStack(), buffer, centerPos.add(0, 1, 0), radius, 32, 1.0F, 0, 0);
                RenderHelper.renderCylinder(event.getPoseStack(), buffer, centerPos, radius, 32, 1.0F, 0, 0);

                RenderHelper.renderFloatingText(event.getPoseStack(), "工作区域", new Vec3(workPos.getX(), workPos.getY(), workPos.getZ()), 0xff1111, 0.2f, -10);
                RenderHelper.renderFloatingText(event.getPoseStack(), "▼", new Vec3(workPos.getX(), workPos.getY(), workPos.getZ()), 0xff1111, 0.2f, 0);
            }

            BlockPos idlePos = ItemKappaCompass.getPoint(Activity.IDLE, stack);
            if (idlePos != null) {
                Vec3 position = event.getCamera().getPosition().reverse();
                Vec3 pos = position.add(idlePos.getX(), idlePos.getY(), idlePos.getZ());
                int radius = MaidConfig.MAID_IDLE_RANGE.get();
                VertexConsumer buffer = mc.renderBuffers().bufferSource().getBuffer(RenderType.LINES);
                RenderHelper.renderCylinder(event.getPoseStack(), buffer, pos.add(0, 2, 0), radius, 32, 0, 1.0F, 0);
                RenderHelper.renderCylinder(event.getPoseStack(), buffer, pos.add(0, 1, 0), radius, 32, 0, 1.0F, 0);
                RenderHelper.renderCylinder(event.getPoseStack(), buffer, pos, radius, 32, 0, 1.0F, 0);

                RenderHelper.renderFloatingText(event.getPoseStack(), "休息区域", new Vec3(idlePos.getX(), idlePos.getY(), idlePos.getZ()), 0x11ff11, 0.15f, -10);
                RenderHelper.renderFloatingText(event.getPoseStack(), "▼", new Vec3(idlePos.getX(), idlePos.getY(), idlePos.getZ()), 0x11ff11, 0.15f, 0);
            }

            BlockPos resetPos = ItemKappaCompass.getPoint(Activity.REST, stack);
            if (resetPos != null) {
                Vec3 position = event.getCamera().getPosition().reverse();
                Vec3 pos = position.add(resetPos.getX(), resetPos.getY(), resetPos.getZ());
                double radius = MaidConfig.MAID_SLEEP_RANGE.get() - 0.2;
                VertexConsumer buffer = mc.renderBuffers().bufferSource().getBuffer(RenderType.LINES);
                RenderHelper.renderCylinder(event.getPoseStack(), buffer, pos.add(0, 2, 0), radius, 32, 0, 0, 1.0F);
                RenderHelper.renderCylinder(event.getPoseStack(), buffer, pos.add(0, 1, 0), radius, 32, 0, 0, 1.0F);
                RenderHelper.renderCylinder(event.getPoseStack(), buffer, pos, radius, 32, 0, 0, 1.0F);

                RenderHelper.renderFloatingText(event.getPoseStack(), "睡觉区域", new Vec3(resetPos.getX(), resetPos.getY(), resetPos.getZ()), 0x1111ff, 0.1f, -10);
                RenderHelper.renderFloatingText(event.getPoseStack(), "▼", new Vec3(resetPos.getX(), resetPos.getY(), resetPos.getZ()), 0x1111ff, 0.1f, 0);
            }
        }
    }
}
