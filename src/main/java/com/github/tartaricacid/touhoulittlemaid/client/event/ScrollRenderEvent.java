package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemRedFoxScroll;
import com.github.tartaricacid.touhoulittlemaid.util.RenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Dist.CLIENT)
public class ScrollRenderEvent {
    @SubscribeEvent
    public static void onRenderWorldLastEvent(RenderLevelStageEvent event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (RenderLevelStageEvent.Stage.AFTER_PARTICLES.equals(event.getStage()) && player != null && player.getMainHandItem().is(InitItems.RED_FOX_SCROLL.get())) {
            Pair<String, BlockPos> info = ItemRedFoxScroll.getTrackInfo(player.getMainHandItem());
            if (info == null) {
                return;
            }
            String dimension = info.getLeft();
            Vec3 trackVec = new Vec3(info.getRight().getX(), info.getRight().getY(), info.getRight().getZ());
            if (!dimension.equals(player.level.dimension().location().toString())) {
                return;
            }
            Vec3 playerVec = player.position();
            double actualDistance = playerVec.distanceTo(trackVec);
            if (actualDistance < 5) {
                return;
            }
            double viewDistance = actualDistance;
            double maxRenderDistance = mc.options.renderDistance().get() * 16;
            if (actualDistance > maxRenderDistance) {
                Vec3 delta = trackVec.subtract(playerVec).normalize();
                trackVec = playerVec.add(delta.x * maxRenderDistance, delta.y * maxRenderDistance, delta.z * maxRenderDistance);
                viewDistance = maxRenderDistance;
            }
            float scale = 0.02f * (((float) viewDistance + 4.0f) / 3.0f);
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderHelper.renderFloatingText(event.getPoseStack(), Math.round(actualDistance) + " m", trackVec, 0xff1111, scale, -17);
            RenderHelper.renderFloatingText(event.getPoseStack(), "▼", trackVec, 0xff0000, scale * 1.2f, -5);
        }
    }
}
