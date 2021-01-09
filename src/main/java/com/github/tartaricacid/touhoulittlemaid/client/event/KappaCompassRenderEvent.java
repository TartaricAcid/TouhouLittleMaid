package com.github.tartaricacid.touhoulittlemaid.client.event;


import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemKappaCompass;
import com.github.tartaricacid.touhoulittlemaid.util.RenderHelper;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.lwjgl.opengl.GL11.*;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Side.CLIENT)
public class KappaCompassRenderEvent {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/particle/flag.png");
    private static List<BlockPos> TMP_DISPLAY_POS = Lists.newArrayList();
    private static ItemKappaCompass.Mode TMP_DISPLAY_MODE = ItemKappaCompass.Mode.NONE;
    private static long TIME;

    @SubscribeEvent
    public static void onRender(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        if (Minecraft.getSystemTime() <= (TIME + TimeUnit.SECONDS.toMillis(9))
                && TMP_DISPLAY_POS.size() > 0 && TMP_DISPLAY_MODE != ItemKappaCompass.Mode.NONE) {
            renderWorldPos(mc, TMP_DISPLAY_POS, TMP_DISPLAY_MODE);
        }

        ItemStack mainStack = mc.player.getHeldItemMainhand();
        ItemStack offStack = mc.player.getHeldItemOffhand();
        if (mainStack.getItem() != MaidItems.KAPPA_COMPASS && offStack.getItem() != MaidItems.KAPPA_COMPASS) {
            return;
        }
        List<BlockPos> blockPosList;
        ItemKappaCompass.Mode mode;

        blockPosList = ItemKappaCompass.getPos(mainStack);
        mode = ItemKappaCompass.getMode(mainStack);
        if (blockPosList.size() == 0 || mode == ItemKappaCompass.Mode.NONE) {
            blockPosList = ItemKappaCompass.getPos(offStack);
            mode = ItemKappaCompass.getMode(offStack);
            if (blockPosList.size() == 0 || mode == ItemKappaCompass.Mode.NONE) {
                return;
            }
        }

        renderWorldPos(mc, blockPosList, mode);
    }

    public static void setTmpDisplay(List<BlockPos> tmpPos, ItemKappaCompass.Mode tmpMode) {
        TMP_DISPLAY_POS = tmpPos;
        TMP_DISPLAY_MODE = tmpMode;
        TIME = Minecraft.getSystemTime();
    }

    private static void renderWorldPos(Minecraft mc, List<BlockPos> blockPosList, ItemKappaCompass.Mode mode) {
        Vec3d playerVec3d = new Vec3d(-TileEntityRendererDispatcher.staticPlayerX,
                -TileEntityRendererDispatcher.staticPlayerY + 1.5,
                -TileEntityRendererDispatcher.staticPlayerZ);
        float yaw = TileEntityRendererDispatcher.instance.entityYaw;
        float pitch = TileEntityRendererDispatcher.instance.entityPitch;
        int size = blockPosList.size();

        GlStateManager.enableBlend();
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
        GlStateManager.disableTexture2D();
        GlStateManager.resetColor();
        {
            switch (mode) {
                case SINGLE_POINT:
                    List<BlockPos> singlePos = Collections.singletonList(blockPosList.get(size - 1));
                    drawRangeLine(playerVec3d, blockPosList, 8, 0xffaaaa00);
                    drawRangeLine(playerVec3d, blockPosList, 16, 0xffaa0000);
                    drawPosInfo(mc, playerVec3d, singlePos, yaw, pitch);
                    renderFlag(playerVec3d, yaw, pitch, singlePos);
                    break;
                case MULTI_POINT_CLOSURE:
                    drawPathLine(playerVec3d, blockPosList);
                    drawClosurePathLine(playerVec3d, blockPosList.get(0), blockPosList.get(size - 1));
                    if (size < ItemKappaCompass.MAX_POINT_NUM) {
                        drawRangeLine(playerVec3d, blockPosList, 15, 0xff555500);
                    }
                    drawPosInfo(mc, playerVec3d, blockPosList, yaw, pitch);
                    renderFlag(playerVec3d, yaw, pitch, blockPosList);
                    break;
                case MULTI_POINT_REENTRY:
                    drawPathLine(playerVec3d, blockPosList);
                    if (size < ItemKappaCompass.MAX_POINT_NUM) {
                        drawRangeLine(playerVec3d, blockPosList, 15, 0xff555500);
                    }
                    drawPosInfo(mc, playerVec3d, blockPosList, yaw, pitch);
                    renderFlag(playerVec3d, yaw, pitch, blockPosList);
                    break;
                case SET_RANGE:
                    List<BlockPos> posListTmp = Lists.newArrayList();
                    if (size == 1) {
                        drawRangeLine(playerVec3d, blockPosList, 15, 0xff555500);
                        posListTmp.add(blockPosList.get(size - 1));
                    }
                    if (size >= 2) {
                        posListTmp.add(blockPosList.get(size - 1));
                        posListTmp.add(blockPosList.get(size - 2));
                        drawCubeArea(playerVec3d, posListTmp.get(0), posListTmp.get(1));
                    }
                    drawPosInfo(mc, playerVec3d, posListTmp, yaw, pitch);
                    renderFlag(playerVec3d, yaw, pitch, posListTmp);
                    break;
                default:
                    break;
            }
        }
        GlStateManager.resetColor();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    private static void drawPosInfo(Minecraft mc, Vec3d vec3d, List<BlockPos> blockPosList, float yaw, float pitch) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(vec3d.x, vec3d.y, vec3d.z);
        for (int i = 0; i < blockPosList.size(); i++) {
            BlockPos pos = blockPosList.get(i);
            drawNameplate(mc.fontRenderer, String.format("§l§a[%d, %d, %d]",
                    pos.getX(), pos.getY(), pos.getZ()),
                    pos.getX() + 0.5f, pos.getY() - 0.25f, pos.getZ() + 0.5f, 0,
                    yaw, pitch, 0.05f);
            drawNameplate(mc.fontRenderer, String.valueOf((char) (i + 65)),
                    pos.getX() + 0.5f, pos.getY() - 0.25f, pos.getZ() + 0.5f, -10,
                    yaw, pitch, 0.1f);
        }
        GlStateManager.popMatrix();
    }

    private static void drawRangeLine(Vec3d vec3d, List<BlockPos> blockPosList, float range, int color) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        GlStateManager.pushMatrix();
        BlockPos posLast = blockPosList.get(blockPosList.size() - 1);
        GlStateManager.translate(vec3d.x, vec3d.y, vec3d.z);
        GlStateManager.color(red, green, blue, alpha);
        GlStateManager.translate(posLast.getX() + 0.5, posLast.getY() - 0.75, posLast.getZ() + 0.5);
        GlStateManager.scale(range, range, range);
        GlStateManager.rotate(90, 1.0F, 0.0F, 0.0F);
        GlStateManager.glLineWidth(2.0F);
        GlStateManager.callList(RenderHelper.SPHERE_LINE);
        GlStateManager.popMatrix();
        GlStateManager.resetColor();
    }

    private static void drawClosurePathLine(Vec3d vec3d, BlockPos startPos, BlockPos endPos) {
        if (startPos.equals(endPos)) {
            return;
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(vec3d.x, vec3d.y, vec3d.z);
        GlStateManager.rotate(360, 0.0F, 0.0F, 1.0F);
        GlStateManager.glLineWidth(3.0F);
        boolean isFar = startPos.distanceSq(endPos) > Math.pow(ItemKappaCompass.MAX_DISTANCE, 2);
        if (isFar) {
            GlStateManager.color(1, 1, 1, 255);
            glLineStipple(5, (short) 0b101010101010101);
            glEnable(GL_LINE_STIPPLE);
        } else {
            GlStateManager.color(255, 0, 0, 255);
        }
        GlStateManager.glBegin(GL11.GL_LINE_STRIP);
        GL11.glVertex3d(startPos.getX() + 0.5, startPos.getY() - 0.75, startPos.getZ() + 0.5);
        GL11.glVertex3d(endPos.getX() + 0.5, endPos.getY() - 0.75, endPos.getZ() + 0.5);
        GlStateManager.glEnd();
        if (isFar) {
            glDisable(GL_LINE_STIPPLE);
        }
        GlStateManager.popMatrix();
    }

    private static void drawCubeArea(Vec3d vec3d, BlockPos pos1, BlockPos pos2) {
        if (pos1.equals(pos2)) {
            return;
        }

        BlockPos distancePos = new BlockPos(
                Math.abs(pos1.getX() - pos2.getX()) + 1,
                Math.abs(pos1.getY() - pos2.getY()) + 1,
                Math.abs(pos1.getZ() - pos2.getZ()) + 1
        );

        BlockPos originPos = new BlockPos(
                Math.min(pos1.getX(), pos2.getX()),
                Math.min(pos1.getY(), pos2.getY()),
                Math.min(pos1.getZ(), pos2.getZ())
        );

        GlStateManager.pushMatrix();
        GlStateManager.translate(vec3d.x, vec3d.y, vec3d.z);
        GlStateManager.translate(originPos.getX(), originPos.getY() - 1.5, originPos.getZ());
        GlStateManager.glLineWidth(3.0F);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        for (int i = 0; i <= distancePos.getX(); i++) {
            bufferbuilder.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos(i, 0, distancePos.getZ()).color(1.0F, 0.0F, 0.0F, 1).endVertex();
            bufferbuilder.pos(i, 0, 0).color(1.0F, 0.0F, 0.0F, 1).endVertex();
            bufferbuilder.pos(i, distancePos.getY(), 0).color(1.0F, 0.0F, 0.0F, 1).endVertex();
            bufferbuilder.pos(i, distancePos.getY(), distancePos.getZ()).color(1.0F, 0.0F, 0.0F, 1).endVertex();
            bufferbuilder.pos(i, 0, distancePos.getZ()).color(1.0F, 0.0F, 0.0F, 1).endVertex();
            tessellator.draw();
        }

        for (int i = 0; i <= distancePos.getY(); i++) {
            bufferbuilder.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos(0, i, distancePos.getZ()).color(1.0F, 0.0F, 0.0F, 1).endVertex();
            bufferbuilder.pos(0, i, 0).color(1.0F, 0.0F, 0.0F, 1).endVertex();
            bufferbuilder.pos(distancePos.getX(), i, 0).color(1.0F, 0.0F, 0.0F, 1).endVertex();
            bufferbuilder.pos(distancePos.getX(), i, distancePos.getZ()).color(1.0F, 0.0F, 0.0F, 1).endVertex();
            bufferbuilder.pos(0, i, distancePos.getZ()).color(1.0F, 0.0F, 0.0F, 1).endVertex();
            tessellator.draw();
        }

        for (int i = 0; i <= distancePos.getZ(); i++) {
            bufferbuilder.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos(0, distancePos.getY(), i).color(1.0F, 0.0F, 0.0F, 1).endVertex();
            bufferbuilder.pos(0, 0, i).color(1.0F, 0.0F, 0.0F, 1).endVertex();
            bufferbuilder.pos(distancePos.getX(), 0, i).color(1.0F, 0.0F, 0.0F, 1).endVertex();
            bufferbuilder.pos(distancePos.getX(), distancePos.getY(), i).color(1.0F, 0.0F, 0.0F, 1).endVertex();
            bufferbuilder.pos(0, distancePos.getY(), i).color(1.0F, 0.0F, 0.0F, 1).endVertex();
            tessellator.draw();
        }

        GlStateManager.popMatrix();
    }

    private static void drawPathLine(Vec3d vec3d, List<BlockPos> blockPosList) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(vec3d.x, vec3d.y, vec3d.z);
        GlStateManager.rotate(360, 0.0F, 0.0F, 1.0F);
        GlStateManager.color(255, 0, 0, 255);
        GlStateManager.glLineWidth(3.0F);
        GlStateManager.glBegin(GL11.GL_LINE_STRIP);
        for (BlockPos pos : blockPosList) {
            GL11.glVertex3d(pos.getX() + 0.5, pos.getY() - 0.75, pos.getZ() + 0.5);
        }
        GlStateManager.glEnd();
        GlStateManager.popMatrix();
    }

    public static void renderFlag(Vec3d vec3d, float yaw, float pitch, List<BlockPos> blockPosList) {
        for (BlockPos pos : blockPosList) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(vec3d.x, vec3d.y, vec3d.z);
            GlStateManager.translate(pos.getX() + 0.5f, pos.getY() - 1f, pos.getZ() + 0.5f);
            GlStateManager.rotate(-yaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(pitch, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(180, 0.0F, 0.0F, 1.0F);
            Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            buffer.pos(0.25, 0.25, 0).tex(1, 1).endVertex();
            buffer.pos(0.25, -0.25, 0).tex(1, 0).endVertex();
            buffer.pos(-0.25, -0.25, 0).tex(0, 0).endVertex();
            buffer.pos(-0.25, 0.25, 0).tex(0, 1).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
        }
    }

    public static void drawNameplate(FontRenderer fontRendererIn, String str, float x, float y, float z, int verticalShift, float viewerYaw, float viewerPitch, float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-viewerYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(viewerPitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-scale, -scale, scale);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        int i = fontRendererIn.getStringWidth(str) / 2;
        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(-i - 1, -1 + verticalShift, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        bufferbuilder.pos(-i - 1, 8 + verticalShift, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        bufferbuilder.pos(i + 1, 8 + verticalShift, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        bufferbuilder.pos(i + 1, -1 + verticalShift, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();

        fontRendererIn.drawString(str, -fontRendererIn.getStringWidth(str) / 2, verticalShift, 0x20ffffff);
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        fontRendererIn.drawString(str, -fontRendererIn.getStringWidth(str) / 2, verticalShift, 0xffffffff);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }
}
