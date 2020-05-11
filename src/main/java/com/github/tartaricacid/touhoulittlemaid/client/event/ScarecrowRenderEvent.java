package com.github.tartaricacid.touhoulittlemaid.client.event;


import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityScarecrow;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.util.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_ZERO;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Side.CLIENT)
public class ScarecrowRenderEvent {
    private static final int RANGE = 10;

    @SubscribeEvent
    public static void onRender(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;
        if (player.getHeldItemMainhand().getItem() == MaidItems.SCARECROW && mc.objectMouseOver != null) {
            if (mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK
                    && mc.objectMouseOver.getBlockPos() != null) {
                renderSphereDisplay(mc, mc.objectMouseOver.getBlockPos());
                return;
            }

            if (mc.objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY
                    && mc.objectMouseOver.entityHit instanceof EntityScarecrow) {
                renderSphereDisplay(mc, mc.objectMouseOver.entityHit.getPosition());
            }
        }
    }

    private static void renderSphereDisplay(Minecraft mc, BlockPos pos) {
        Vec3d vec3d = new Vec3d(-TileEntityRendererDispatcher.staticPlayerX,
                -TileEntityRendererDispatcher.staticPlayerY + mc.player.getEyeHeight() - 0.1,
                -TileEntityRendererDispatcher.staticPlayerZ);

        GlStateManager.enableBlend();
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
        GlStateManager.disableTexture2D();
        GlStateManager.resetColor();

        GlStateManager.pushMatrix();
        GlStateManager.translate(vec3d.x, vec3d.y, vec3d.z);
        GlStateManager.color(1, 0, 0, 1);
        GlStateManager.translate(pos.getX() + 0.5, pos.getY() - 0.75, pos.getZ() + 0.5);
        GlStateManager.scale(RANGE, RANGE, RANGE);
        GlStateManager.rotate(90, 1.0F, 0.0F, 0.0F);
        GlStateManager.glLineWidth(2.0F);
        GlStateManager.callList(RenderHelper.SPHERE_LINE);
        GlStateManager.popMatrix();

        GlStateManager.resetColor();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}
