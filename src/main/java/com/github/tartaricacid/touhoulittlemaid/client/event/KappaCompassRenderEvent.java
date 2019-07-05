package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.init.ParticleEnum;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Side.CLIENT)
public class KappaCompassRenderEvent {
    @SubscribeEvent
    public static void renderBlockOverlayEvent(RenderGameOverlayEvent event) {
        int[] i = MaidItems.KAPPA_COMPASS.getPos(Minecraft.getMinecraft().player.getHeldItemMainhand());
        if (i != null) {
            Minecraft.getMinecraft().effectRenderer.spawnEffectParticle(ParticleEnum.FLAG.getId(),
                    (double) i[0] + 0.5, (double) i[1] + 0.5, (double) i[2] + 0.5, 0, 0, 0);
        }
    }
}
