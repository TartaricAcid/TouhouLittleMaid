package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.sections.dynamic.DynamicChunkBuffers;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.sections.events.ReloadDynamicChunkBufferEvent;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class DynamicChunkBufferEvent {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/cchess.png");
    private static final ResourceLocation PIECES_TEXTURE = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/cchess_pieces.png");

    @SubscribeEvent
    public static void doDynamicChunkBufferEvent(ReloadDynamicChunkBufferEvent event) {
        DynamicChunkBuffers.markCutoutChunkBuffer(TEXTURE, RenderType.entityCutoutNoCull(TEXTURE));
        DynamicChunkBuffers.markCutoutChunkBuffer(PIECES_TEXTURE, RenderType.entityCutoutNoCull(PIECES_TEXTURE));
    }
}
