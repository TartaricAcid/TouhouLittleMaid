package com.github.tartaricacid.touhoulittlemaid.debug.target;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.VisibleForDebug;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@VisibleForDebug
@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME, modid = TouhouLittleMaid.MOD_ID, value = Dist.CLIENT)
public class DebugClientRenderEvent {
    @SubscribeEvent
    public static void onRender(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES && TouhouLittleMaid.DEBUG) {
            MultiBufferSource.BufferSource bufferSource = event.getLevelRenderer().renderBuffers.bufferSource();
            Minecraft.getInstance().debugRenderer.pathfindingRenderer.render(event.getPoseStack(),
                    bufferSource,
                    event.getCamera().getPosition().x,
                    event.getCamera().getPosition().y,
                    event.getCamera().getPosition().z);
            bufferSource.endBatch();
        }
    }
}
