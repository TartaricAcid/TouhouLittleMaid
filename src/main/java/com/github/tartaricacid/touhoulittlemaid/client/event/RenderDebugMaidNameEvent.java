package com.github.tartaricacid.touhoulittlemaid.client.event;


import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Side.CLIENT)
public class RenderDebugMaidNameEvent {
    @SubscribeEvent
    public static void onRenderName(RenderLivingEvent.Specials.Post<EntityMaid> event) {
        if (event.getEntity() instanceof EntityMaid) {
            EntityMaid entityIn = (EntityMaid) event.getEntity();
            RenderManager renderManager = event.getRenderer().getRenderManager();
            double distanceSq1 = entityIn.getDistanceSq(renderManager.renderViewEntity);
            if (distanceSq1 <= 32 * 32) {
                boolean sneaking = entityIn.isSneaking();
                float viewY = renderManager.playerViewY;
                float viewX = renderManager.playerViewX;
                boolean thirdPersonView = renderManager.options.thirdPersonView == 2;
                float yOffset = entityIn.height + 0.8F - (sneaking ? 0.25F : 0.0F);
                EntityRenderer.drawNameplate(event.getRenderer().getFontRendererFromRenderManager(),
                        String.format("好感度：%d", entityIn.getFavorability()),
                        (float) event.getX(), (float) event.getY() + yOffset, (float) event.getZ(),
                        0, viewY, viewX, thirdPersonView, sneaking);
            }
        }
    }
}
