package com.github.tartaricacid.touhoulittlemaid.client.gui.skin;

import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.ModelItem;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.proxy.ClientProxy;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.lwjgl.input.Mouse;

import java.io.IOException;

/**
 * @author TartaricAcid
 * @date 2019/8/3 19:58
 **/
public class MaidSkinDetailsGui extends GuiScreen {
    private static final float SCALE_MAX = 180f;
    private static final float SCALE_MIN = 18f;
    private static final float PITCH_MAX = 90f;
    private static final float PITCH_MIN = -90f;

    private ModelItem modelItem;
    private EntityMaid maid;
    private long zeroSystemTime;
    private float posX = 0;
    private float posY = -15;
    private float scale = 80;
    private float yaw = -45;
    private float pitch = -30;

    public MaidSkinDetailsGui(World world, ResourceLocation modelId) {
        this.maid = new EntityMaid(world);
        this.modelItem = ClientProxy.ID_INFO_MAP.get(modelId.toString());
        this.maid.setModelId(modelId.toString());
        this.maid.isDebugFloorOpen = true;
        this.zeroSystemTime = Minecraft.getSystemTime();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // 因为日常动画是通过 ticksExisted 这个数据作为自变量的
        // zeroSystemTime 作用是减小这个数值
        // 因为骨骼动画中大量使用了 MathHelper 这个类，但是它无法处理非常大的数
        maid.ticksExisted = (int) (Minecraft.getSystemTime() - zeroSystemTime) / 50;
        // 绘制灰色默认背景
        // 原版默认最大 zLevel 为 -1000，低于此会导致此矩形无法显示
        this.drawGradientRect(0, 0, this.width, this.height, 0xfe17191d, 0xfe17191d, -1000);
        // 绘制实体，中心点稍微偏右侧，在（3/5，1/2）处
        drawEntity(width * 3 / 5, height / 2);
        // 绘制底部状态栏
        drawBottomStatusBar();
        // 绘制左边侧
        drawSideMenu();
        // 绘制顶部状态栏
        drawTopStatusBar();
        // 绘制按钮
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /**
     * 绘制底部状态栏
     */
    private void drawBottomStatusBar() {
        // 背景
        this.drawGradientRect(0, this.height - 16, this.width, this.height, 0xfe282c34, 0xfe282c34);
        // 文字绘制
        String name = String.format("%s%s %s", TextFormatting.BOLD, "\u2714", ParseI18n.parse(modelItem.getName()));
        String info = String.format("%s%d FPS %.2f%%", TextFormatting.BOLD, Minecraft.getDebugFPS(), scale * 100 / 80);
        fontRenderer.drawString(name, (int) (this.width * 0.236076f) + 4, this.height - 12, 0xcacad4, false);
        fontRenderer.drawString(info, this.width - fontRenderer.getStringWidth(info) - 4, this.height - 12, 0xcacad4, false);
    }

    /**
     * 绘制边侧栏
     */
    private void drawSideMenu() {
        this.drawGradientRect(0, 0, (int) (this.width * 0.236076f), this.height, 0xfe21252b, 0xfe21252b);
    }

    /**
     * 绘制顶部状态栏
     */
    private void drawTopStatusBar() {
        this.drawGradientRect(0, 0, this.width, 16, 0xfe282c34, 0xfe282c34);
        String title = String.format("%sTouhou Little Maid", TextFormatting.BOLD);
        fontRenderer.drawString(title, 6, 4, 0xcacad4, false);
    }

    @Override
    public void initGui() {
        // TODO: 2019/8/5 按钮添加
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        // TODO: 2019/8/5 功能性按钮启用
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        // 鼠标滚轮改变大小
        if (Mouse.getEventDWheel() != 0) {
            changeScaleValue(Mouse.getEventDWheel() * 0.07f);
        }

        // 鼠标左键拖动旋转
        if (Mouse.isButtonDown(0)) {
            if (Mouse.getEventDX() != 0) {
                yaw += Mouse.getEventDX() * 0.4f;
            }
            if (Mouse.getEventDY() != 0) {
                changePitchValue(Mouse.getEventDY() * 0.4f);
            }
        }

        // 鼠标右键移动位置
        if (Mouse.isButtonDown(1)) {
            if (Mouse.getEventDX() != 0) {
                posX += Mouse.getEventDX() * 0.4f;
            }
            if (Mouse.getEventDY() != 0) {
                posY -= Mouse.getEventDY() * 0.4f;
            }
        }
    }

    /**
     * 更改 Scale 的数值
     *
     * @param amount 增加或者减少的数值
     */
    private void changeScaleValue(float amount) {
        if (scale + amount > SCALE_MAX) {
            scale = SCALE_MAX;
        } else if (scale + amount < SCALE_MIN) {
            scale = SCALE_MIN;
        } else {
            scale = scale + amount;
        }
    }

    /**
     * 更改 Pitch 的数值
     *
     * @param amount 增加或者减少的数值
     */
    private void changePitchValue(float amount) {
        if (pitch + amount > PITCH_MAX) {
            pitch = 90;
        } else if (pitch + amount < PITCH_MIN) {
            pitch = -90;
        } else {
            pitch = pitch + amount;
        }
    }

    /**
     * 绘制实体部分
     */
    private void drawEntity(int middleWidth, int middleHeight) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(posX + middleWidth, posY + middleHeight, -500);
        GlStateManager.rotate(pitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.scale(-scale, scale, scale);
        RenderHelper.enableStandardItemLighting();
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntity(maid, 0.0D, -1.0D, 0.0D, 0.0F, 0.0f, true);
        rendermanager.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    /**
     * 依据原版绘制矩形修改的方法，此方法额外可修改 zLevel
     */
    private void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor, double zLevel) {
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double) right, (double) top, zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double) left, (double) top, zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double) left, (double) bottom, zLevel).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos((double) right, (double) bottom, zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
}
