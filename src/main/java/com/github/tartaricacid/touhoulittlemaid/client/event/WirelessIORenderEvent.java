package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.bauble.WirelessIOBauble;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemWirelessIO;
import com.github.tartaricacid.touhoulittlemaid.util.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.github.tartaricacid.touhoulittlemaid.client.event.KappaCompassRenderEvent.drawNameplate;
import static org.lwjgl.opengl.GL11.*;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Side.CLIENT)
public class WirelessIORenderEvent {
    @SubscribeEvent
    public static void onRender(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        ItemStack mainStack = mc.player.getHeldItemMainhand();
        if (mainStack.getItem() != MaidItems.WIRELESS_IO) {
            return;
        }
        BlockPos pos = ItemWirelessIO.getBindingPos(mainStack);
        if (pos == null) {
            return;
        }

        Vec3d playerVec3d = new Vec3d(-TileEntityRendererDispatcher.staticPlayerX,
                -TileEntityRendererDispatcher.staticPlayerY + mc.player.getEyeHeight() - 0.1,
                -TileEntityRendererDispatcher.staticPlayerZ);
        float yaw = TileEntityRendererDispatcher.instance.entityYaw;
        float pitch = TileEntityRendererDispatcher.instance.entityPitch;

        GlStateManager.enableBlend();
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
        GlStateManager.disableTexture2D();
        GlStateManager.resetColor();

        GlStateManager.pushMatrix();
        GlStateManager.translate(playerVec3d.x, playerVec3d.y, playerVec3d.z);
        GlStateManager.color(1, 0, 0, 1);
        GlStateManager.translate(pos.getX() + 0.5, pos.getY() - 0.75, pos.getZ() + 0.5);
        GlStateManager.scale(WirelessIOBauble.MAX_DISTANCE, WirelessIOBauble.MAX_DISTANCE, WirelessIOBauble.MAX_DISTANCE);
        GlStateManager.rotate(90, 1.0F, 0.0F, 0.0F);
        GlStateManager.glLineWidth(2.0F);
        GlStateManager.callList(RenderHelper.SPHERE_LINE);
        GlStateManager.popMatrix();
        GlStateManager.resetColor();

        GlStateManager.pushMatrix();
        GlStateManager.translate(playerVec3d.x, playerVec3d.y, playerVec3d.z);
        GlStateManager.translate(pos.getX(), pos.getY() - 1.5, pos.getZ());
        GlStateManager.glLineWidth(5.0F);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        for (int i = 0; i < 2; i++) {
            bufferbuilder.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos(i, 0, 1).color(1.0F, 1.0F, 0.0F, 1).endVertex();
            bufferbuilder.pos(i, 0, 0).color(1.0F, 1.0F, 0.0F, 1).endVertex();
            bufferbuilder.pos(i, 1, 0).color(1.0F, 1.0F, 0.0F, 1).endVertex();
            bufferbuilder.pos(i, 1, 1).color(1.0F, 1.0F, 0.0F, 1).endVertex();
            bufferbuilder.pos(i, 0, 1).color(1.0F, 1.0F, 0.0F, 1).endVertex();
            tessellator.draw();
        }
        for (int i = 0; i < 2; i++) {
            bufferbuilder.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos(0, i, 1).color(1.0F, 1.0F, 0.0F, 1).endVertex();
            bufferbuilder.pos(0, i, 0).color(1.0F, 1.0F, 0.0F, 1).endVertex();
            bufferbuilder.pos(1, i, 0).color(1.0F, 1.0F, 0.0F, 1).endVertex();
            bufferbuilder.pos(1, i, 1).color(1.0F, 1.0F, 0.0F, 1).endVertex();
            bufferbuilder.pos(0, i, 1).color(1.0F, 1.0F, 0.0F, 1).endVertex();
            tessellator.draw();
        }
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(playerVec3d.x, playerVec3d.y, playerVec3d.z);
        drawNameplate(mc.fontRenderer, String.format("§l§a[%d, %d, %d]",
                pos.getX(), pos.getY(), pos.getZ()),
                pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f, 0,
                yaw, pitch, 0.05f);
        GlStateManager.popMatrix();

        GlStateManager.resetColor();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}
