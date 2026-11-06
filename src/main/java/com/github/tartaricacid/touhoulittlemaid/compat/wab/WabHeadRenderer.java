package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

/**
 * 头顶信息渲染：把 WabHeadSync 推下来的两行数值画在女仆头顶，billboard 正对玩家
 * （与女仆朝向无关，镜头怎么转它都面向你）。技术照抄 EntityTombstoneRenderer 的名牌：
 * translate 到头顶 → mulPose(cameraOrientation) → scale(-0.025) → drawInBatch。
 * 位置抬高过原版名牌，两层不打架。
 */
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT)
public final class WabHeadRenderer {
    private static final double MAX_DISTANCE_SQR = 48 * 48;

    private WabHeadRenderer() {
    }

    @SubscribeEvent
    public static void onRenderMaid(RenderLivingEvent.Post<?, ?> event) {
        if (!(event.getEntity() instanceof EntityMaid maid) || maid.isInvisible()) return;
        if (!WabConfig.showAboveMaid()) return;
        WabClientHeads.Entry entry = WabClientHeads.get(maid.getUUID());
        if (entry == null) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.player.distanceToSqr(maid) > MAX_DISTANCE_SQR) return;

        Font font = mc.font;
        PoseStack pose = event.getPoseStack();
        MultiBufferSource buffer = event.getMultiBufferSource();
        int light = event.getPackedLight();

        pose.pushPose();
        pose.translate(0, maid.getBbHeight() + 0.75F, 0);
        pose.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
        pose.scale(-0.025F, -0.025F, 0.025F);
        Matrix4f matrix = pose.last().pose();
        drawLine(font, buffer, matrix, entry.l1(), 0, 0xFFFFFFFF, light);
        drawLine(font, buffer, matrix, entry.l2(), 10, 0xFFFFE08A, light);
        pose.popPose();
    }

    private static void drawLine(Font font, MultiBufferSource buffer, Matrix4f matrix,
                                 String text, float y, int color, int light) {
        if (text == null || text.isEmpty()) return;
        font.drawInBatch(text, -font.width(text) / 2.0F, y, color, false, matrix, buffer,
                Font.DisplayMode.NORMAL, 0, light);
    }
}
