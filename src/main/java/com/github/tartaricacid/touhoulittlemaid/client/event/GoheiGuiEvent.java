package com.github.tartaricacid.touhoulittlemaid.client.event;


import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.DanmakuColor;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.DanmakuType;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemHakureiGohei;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.GoheiModeMessage;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Side.CLIENT)
public class GoheiGuiEvent {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/danmaku.png");
    private static int timer = 0;
    private static int color = 0;
    private static boolean show = false;

    /**
     * 当 Alt 键摁下时，更改布尔值，判定 GUI 是否显示
     */
    @SubscribeEvent
    public static void onAltKeyInput(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;

        if (!show && mc.inGameHasFocus && Keyboard.isKeyDown(Keyboard.KEY_LMENU) && player.getHeldItemMainhand().getItem() == MaidItems.HAKUREI_GOHEI) {
            show = true;
        } else if (show && !Keyboard.isKeyDown(Keyboard.KEY_LMENU)) {
            show = false;
        }
    }

    @SubscribeEvent
    public static void onMouseDwheelInput(MouseEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;

        if (show && mc.inGameHasFocus && event.getDwheel() != 0 && player.getHeldItemMainhand().getItem() == MaidItems.HAKUREI_GOHEI) {
            CommonProxy.INSTANCE.sendToServer(new GoheiModeMessage(event.getDwheel() < 0));
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (show && event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayerSP player = mc.player;

            if (player.getHeldItemMainhand().getItem() != MaidItems.HAKUREI_GOHEI ||
                    !(MaidItems.HAKUREI_GOHEI instanceof ItemHakureiGohei)) {
                return;
            }

            ItemHakureiGohei item = (ItemHakureiGohei) MaidItems.HAKUREI_GOHEI;
            ItemStack stack = player.getHeldItemMainhand();

            // 获取相关数据
            DanmakuType type = item.getGoheiMode(stack);
            // 开始迭代变化色彩
            if (timer == 60) {
                color = (color >= DanmakuColor.getLength()) ? 0 : (color + 1);
                timer = 0;
            }
            timer += 1;

            // 材质宽度
            int w = 224;
            // 材质长度
            int l = 320;

            // 依据类型颜色开始定位材质位置（材质块都是 32 * 32 大小）
            double pStartU = 32 * color;
            double pStartV = 32 * type.getIndex();

            float sx = event.getResolution().getScaledWidth();
            float sy = event.getResolution().getScaledHeight();

            String text = I18n.format("tooltips.touhou_little_maid.hakurei_gohei.desc",
                    I18n.format("danmaku_type.touhou_little_maid." + item.getGoheiMode(stack).getName()));

            mc.fontRenderer.drawString(text, (sx - mc.fontRenderer.getStringWidth(text)) / 2, sy * 0.30f, 0xffffff, true);

            GlStateManager.pushMatrix();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.alphaFunc(GL11.GL_GREATER, 0f);

            GlStateManager.translate(sx / 2, sy * 0.50, 0);
            GlStateManager.rotate(180F, 0.0F, 1.0F, 0.0F);

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufbuilder = tessellator.getBuffer();

            bufbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            mc.getTextureManager().bindTexture(TEXTURE);

            bufbuilder.pos(-25, 25, 0).tex((pStartU + 0) / w, (pStartV + 32) / l).endVertex();
            bufbuilder.pos(-25, -25, 0).tex((pStartU + 0) / w, (pStartV + 0) / l).endVertex();
            bufbuilder.pos(25, -25, 0).tex((pStartU + 32) / w, (pStartV + 0) / l).endVertex();
            bufbuilder.pos(25, 25, 0).tex((pStartU + 32) / w, (pStartV + 32) / l).endVertex();
            tessellator.draw();

            GlStateManager.disableBlend();
            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
        }
    }
}
