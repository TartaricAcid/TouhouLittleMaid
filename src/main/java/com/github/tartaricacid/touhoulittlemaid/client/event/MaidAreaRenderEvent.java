package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.SchedulePos;
import com.github.tartaricacid.touhoulittlemaid.util.RenderHelper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import javax.annotation.Nullable;
import java.util.concurrent.TimeUnit;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Dist.CLIENT)
public class MaidAreaRenderEvent {
    private static final Cache<Integer, SchedulePos> CACHE = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS).build();

    @SubscribeEvent
    public static void onRender(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null || mc.level == null) {
                return;
            }
            Vec3 camera = event.getCamera().getPosition().reverse();
            PoseStack poseStack = event.getPoseStack();
            for (int id : CACHE.asMap().keySet()) {
                SchedulePos pos = CACHE.getIfPresent(id);
                if (pos == null) {
                    continue;
                }
                Entity entity = mc.level.getEntity(id);
                if (!(entity instanceof EntityMaid maid)) {
                    return;
                }
                ResourceLocation dimension = pos.getDimension();
                if (mc.player.level.dimension().location().equals(dimension)) {
                    renderPos(pos.getWorkPos(), pos.getIdlePos(), pos.getSleepPos(), camera, poseStack, mc, maid, mc.player);
                }
            }
        }
    }

    private static void renderPos(@Nullable BlockPos workPos, @Nullable BlockPos idlePos, @Nullable BlockPos resetPos, Vec3 camera, PoseStack poseStack, Minecraft mc, EntityMaid maid, Player player) {
        poseStack.pushPose();
        poseStack.translate(0, 1, 0);

        BlockPos restrictCenter = maid.getRestrictCenter();
        Vec3 restrictPos = camera.add(restrictCenter.getX() + 0.5, restrictCenter.getY() + 0.5, restrictCenter.getZ() + 0.5);
        if (!maid.isHomeModeEnable()) {
            restrictPos = camera.add(player.position());
        }
        Vec3 maidPos = camera.add(maid.position());
        RenderHelper.renderLine(poseStack, mc.renderBuffers().bufferSource().getBuffer(RenderType.LINES), restrictPos, maidPos, 1.0f, 0.2f, 0.2f);
        AABB aabb = maid.getBoundingBox().move(0, -1, 0).move(camera);
        DebugRenderer.renderFilledBox(poseStack, mc.renderBuffers().bufferSource(), aabb, 0.8F, 0.8F, 0.2F, 0.75F);

        if (workPos != null) {
            Vec3 centerPos = camera.add(workPos.getX() + 0.5, workPos.getY() + 0.5, workPos.getZ() + 0.5);
            double radius = MaidConfig.MAID_WORK_RANGE.get() + 0.1;
            VertexConsumer buffer = mc.renderBuffers().bufferSource().getBuffer(RenderType.LINES);
            RenderHelper.renderCylinder(poseStack, buffer, centerPos, radius, 16, 1.0F, 0, 0);

            Vec3 textPos = new Vec3(workPos.getX() + 0.5, workPos.getY() + 2, workPos.getZ() + 0.5);
            String text = I18n.get("message.touhou_little_maid.kappa_compass.work_area");
            RenderHelper.renderFloatingText(poseStack, text, textPos.x, textPos.y, textPos.z, 0xff1111, 0.15f, true, -5, false);
            RenderHelper.renderFloatingText(poseStack, "▼", textPos.x, textPos.y, textPos.z, 0xff1111, 0.15f, true, 5, false);
        }

        if (idlePos != null) {
            Vec3 centerPos = camera.add(idlePos.getX() + 0.5, idlePos.getY() + 0.5, idlePos.getZ() + 0.5);
            double radius = MaidConfig.MAID_IDLE_RANGE.get();
            VertexConsumer buffer = mc.renderBuffers().bufferSource().getBuffer(RenderType.LINES);
            RenderHelper.renderCylinder(poseStack, buffer, centerPos, radius, 16, 0, 1.0F, 0);
            Vec3 textPos = new Vec3(idlePos.getX() + 0.5, idlePos.getY() + 2, idlePos.getZ() + 0.5);
            if (idlePos.equals(workPos)) {
                textPos = textPos.add(0, 1, 0);
            } else if (workPos != null) {
                Vec3 prePos = camera.add(workPos.getX() + 0.5, workPos.getY() + 0.5, workPos.getZ() + 0.5);
                RenderHelper.renderLine(poseStack, buffer, centerPos, prePos, 1.0f, 1.0f, 1.0f);
            }
            String text = I18n.get("message.touhou_little_maid.kappa_compass.idle_area");
            RenderHelper.renderFloatingText(poseStack, text, textPos.x, textPos.y, textPos.z, 0x11ff11, 0.15f, true, -5, false);
            RenderHelper.renderFloatingText(poseStack, "▼", textPos.x, textPos.y, textPos.z, 0x11ff11, 0.15f, true, 5, false);
        }

        if (resetPos != null) {
            Vec3 centerPos = camera.add(resetPos.getX() + 0.5, resetPos.getY() + 0.5, resetPos.getZ() + 0.5);
            double radius = MaidConfig.MAID_SLEEP_RANGE.get() - 0.1;
            VertexConsumer buffer = mc.renderBuffers().bufferSource().getBuffer(RenderType.LINES);
            RenderHelper.renderCylinder(poseStack, buffer, centerPos, radius, 16, 0, 0, 1.0F);
            Vec3 textPos = new Vec3(resetPos.getX() + 0.5, resetPos.getY() + 2, resetPos.getZ() + 0.5);
            if (resetPos.equals(idlePos)) {
                textPos = textPos.add(0, 2, 0);
            } else if (idlePos != null && workPos != null) {
                Vec3 prePos = camera.add(idlePos.getX() + 0.5, idlePos.getY() + 0.5, idlePos.getZ() + 0.5);
                RenderHelper.renderLine(poseStack, buffer, centerPos, prePos, 1.0f, 1.0f, 1.0f);
                prePos = camera.add(workPos.getX() + 0.5, workPos.getY() + 0.5, workPos.getZ() + 0.5);
                RenderHelper.renderLine(poseStack, buffer, centerPos, prePos, 1.0f, 1.0f, 1.0f);
            }
            String text = I18n.get("message.touhou_little_maid.kappa_compass.sleep_area");
            RenderHelper.renderFloatingText(poseStack, text, textPos.x, textPos.y, textPos.z, 0x1111ff, 0.15f, true, -5, false);
            RenderHelper.renderFloatingText(poseStack, "▼", textPos.x, textPos.y, textPos.z, 0x1111ff, 0.15f, true, 5, false);
        }

        poseStack.popPose();
    }

    public static void addSchedulePos(int id, SchedulePos pos) {
        CACHE.put(id, pos);
    }
}
