package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.sections.dynamic.DynamicChunkBuffers;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.sections.events.ReloadDynamicChunkBufferEvent;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity.PicnicMatRender;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity.TileEntityCChessRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class DynamicChunkBufferEvent {
    @SubscribeEvent
    public static void doDynamicChunkBufferEvent(ReloadDynamicChunkBufferEvent event) {
        DynamicChunkBuffers.markCutoutChunkBuffer(TileEntityCChessRenderer.TEXTURE, TileEntityCChessRenderer.CCHESS_RENDER_TYPE);
        DynamicChunkBuffers.markCutoutChunkBuffer(TileEntityCChessRenderer.PIECES_TEXTURE, TileEntityCChessRenderer.CCHESS_PIECES_RENDER_TYPE);
        DynamicChunkBuffers.markCutoutChunkBuffer(PicnicMatRender.TEXTURE, PicnicMatRender.RENDER_TYPE);
    }
}
