package com.github.tartaricacid.touhoulittlemaid.client.gui.skin;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.ModelItem;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Point4d;
import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.github.tartaricacid.touhoulittlemaid.client.gui.skin.AbstractSkinDetailsGui.BUTTON.*;

/**
 * @author TartaricAcid
 * @date 2019/8/3 19:58
 **/
@SideOnly(Side.CLIENT)
public abstract class AbstractSkinDetailsGui<T extends EntityLivingBase> extends GuiScreen {
    protected static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/skin_detail.png");
    /**
     * 部分缩放，旋转所限制的范围
     */
    private static final float SCALE_MAX = 360f;
    private static final float SCALE_MIN = 18f;
    private static final float PITCH_MAX = 90f;
    private static final float PITCH_MIN = -90f;
    private static ScheduledExecutorService timer;
    /**
     * 几个主界面的起始坐标和终点坐标
     */
    private static Point4d BACKGROUND_SIZE = new Point4d();
    private static Point4d BOTTOM_STATUS_BAR_SIZE = new Point4d();
    private static Point4d SIDE_MENU_SIZE = new Point4d();
    private static Point4d TOP_STATUS_BAR_SIZE = new Point4d();

    /**
     * 必须的部分变量
     */
    protected T sourceEntity;
    protected ModelItem modelItem;
    protected volatile T guiEntity;
    protected volatile boolean isEnableWalk = false;
    /**
     * 控制旋转、缩放和行走的变量
     */
    private float posX = 0;
    private float posY = -15;
    private float scale = 80;
    private float yaw = -45;
    private float pitch = -30;

    public AbstractSkinDetailsGui(T sourceEntity, T guiEntity, ModelItem modelItem) {
        this.sourceEntity = sourceEntity;
        this.guiEntity = guiEntity;
        this.modelItem = modelItem;
        // 启用模拟 tick 线程
        simulationTickRun();
    }

    /**
     * 初始化侧边栏按钮
     */
    abstract void initSideButton();

    /**
     * 处理侧边栏按钮的按下的事件
     *
     * @param button 按下的按键
     */
    abstract void actionSideButtonPerformed(GuiButton button);

    /**
     * 处理显示地面网格按钮的逻辑
     */
    abstract void applyFloorButtonLogic();

    /**
     * 处理返回按钮的逻辑
     */
    abstract void applyReturnButtonLogic();

    /**
     * 绘制侧边栏按钮上的文字
     */
    abstract void drawSideButtonText();

    /**
     * 绘制侧边栏按钮的文本提示
     *
     * @param mouseX 鼠标 x 坐标
     * @param mouseY 鼠标 y 坐标
     */
    abstract void drawSideButtonTooltips(int mouseX, int mouseY);

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // 各个区域大小
        BACKGROUND_SIZE.set(0, 0, width, height);
        BOTTOM_STATUS_BAR_SIZE.set(0, height - 16, width, height);
        SIDE_MENU_SIZE.set(0, 0, 132, height);
        TOP_STATUS_BAR_SIZE.set(0, 0, width, 15);
        // 绘制灰色默认背景
        // 原版默认最大 zLevel 为 -1000，低于此会导致此矩形无法显示
        this.drawGradientRect(BACKGROUND_SIZE, 0xfe17191d, -1000);
        // 绘制实体
        drawEntity((width + 132) / 2, height / 2);
        // 绘制参考轴线
        drawDirections();
        // 绘制底部状态栏
        drawBottomStatusBar();
        // 绘制左边侧
        drawSideMenu();
        // 绘制顶部状态栏
        drawTopStatusBar();
        // 绘制按钮
        super.drawScreen(mouseX, mouseY, partialTicks);
        // 绘制按钮文字
        drawSideButtonText();
        // 绘制顶部按键的文本提示
        drawButtonTooltips(mouseX, mouseY);
    }

    /**
     * 创建一个模拟 tick 的定时线程
     */
    private void simulationTickRun() {
        timer = new ScheduledThreadPoolExecutor(1, new BasicThreadFactory.Builder()
                .namingPattern("skin-details-gui-simulation-tick-schedule").daemon(true).build());
        timer.scheduleAtFixedRate(() -> {
            // tick 时间增加，用于一些永远进行的动画
            guiEntity.ticksExisted += 1;
            // 行走的实现
            guiEntity.prevLimbSwingAmount = guiEntity.limbSwingAmount;
            // 依据 isEnableWalk 设置速度（0 ~ 1）
            float speed = isEnableWalk ? 0.5f : 0;
            // 来自原版的行走相关数据
            guiEntity.limbSwingAmount += (speed - guiEntity.limbSwingAmount) * 0.4F;
            guiEntity.limbSwing += guiEntity.limbSwingAmount;
        }, 0, 50, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onGuiClosed() {
        // 关闭模拟线程
        timer.shutdown();
    }

    /**
     * 绘制右下角的参考轴线
     */
    private void drawDirections() {
        GlStateManager.pushMatrix();
        GlStateManager.translate(width - 16, height - 32, -20);
        GlStateManager.rotate(pitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180, 0.0F, 1.0F, 0.0F);
        OpenGlHelper.renderDirections(12);
        GlStateManager.popMatrix();
    }

    /**
     * 绘制底部状态栏
     */
    private void drawBottomStatusBar() {
        // 背景
        this.drawGradientRect(BOTTOM_STATUS_BAR_SIZE, 0xfe282c34, zLevel);
        // 文字绘制
        String name = String.format("%s%s %s", TextFormatting.BOLD, "\u2714", ParseI18n.parse(modelItem.getName()));
        String info = String.format("%s%d FPS %.2f%%", TextFormatting.BOLD, Minecraft.getDebugFPS(), scale * 100 / 80);
        fontRenderer.drawString(name, 136, this.height - 12, 0xcacad4, false);
        fontRenderer.drawString(info, this.width - fontRenderer.getStringWidth(info) - 4, this.height - 12, 0xcacad4, false);
    }

    /**
     * 绘制边侧栏
     */
    private void drawSideMenu() {
        this.drawGradientRect(SIDE_MENU_SIZE, 0xfe21252b, zLevel);
    }

    /**
     * 绘制顶部状态栏
     */
    private void drawTopStatusBar() {
        this.drawGradientRect(TOP_STATUS_BAR_SIZE, 0xfe282c34, zLevel);
        String title = String.format("%s%s", TextFormatting.BOLD, TouhouLittleMaid.MOD_NAME);
        fontRenderer.drawString(title, 6, 3, 0xcacad4, false);
    }

    @Override
    public void initGui() {
        buttonList.clear();

        // 顶部按钮
        GuiButtonImage closeButton = new GuiButtonImage(CLOSE.getIndex(), width - 15, 0, 15, 15,
                0, 24, 15, BUTTON_TEXTURE);
        GuiButtonImage floorButton = new GuiButtonImage(SHOW_FLOOR.getIndex(), width - 30, 0, 15, 15,
                30, 24, 15, BUTTON_TEXTURE);
        GuiButtonImage returnButton = new GuiButtonImage(RETURN.getIndex(), width - 45, 0, 15, 15,
                15, 24, 15, BUTTON_TEXTURE);
        addButton(closeButton);
        addButton(floorButton);
        addButton(returnButton);

        initSideButton();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (BUTTON.getButtonFromIndex(button.id)) {
            case CLOSE:
                applyCloseButtonLogic();
                return;
            case SHOW_FLOOR:
                applyFloorButtonLogic();
                return;
            case RETURN:
                applyReturnButtonLogic();
                return;
            case OTHER:
            default:
                actionSideButtonPerformed(button);
        }
    }

    protected void applyCloseButtonLogic() {
        mc.addScheduledTask(() -> mc.displayGuiScreen(null));
    }


    /**
     * 绘制顶部按钮的文本提示
     */
    private void drawButtonTooltips(int mouseX, int mouseY) {
        boolean isInWidthRange;
        boolean isInHeightRange;

        isInWidthRange = width - 15 < mouseX && mouseX < width;
        isInHeightRange = 0 < mouseY && mouseY < 15;
        if (isInWidthRange && isInHeightRange) {
            drawHoveringText(I18n.format("gui.touhou_little_maid.skin_details.close"), mouseX + 16, mouseY + 24);
        }

        isInWidthRange = width - 30 < mouseX && mouseX < width - 15;
        if (isInWidthRange && isInHeightRange) {
            drawHoveringText(I18n.format("gui.touhou_little_maid.skin_details.floor"), mouseX + 16, mouseY + 24);
        }

        isInWidthRange = width - 45 < mouseX && mouseX < width - 30;
        if (isInWidthRange && isInHeightRange) {
            drawHoveringText(I18n.format("gui.touhou_little_maid.skin_details.return"), mouseX + 16, mouseY + 24);
        }

        drawSideButtonTooltips(mouseX, mouseY);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        int mouseX = Mouse.getX() * width / mc.displayWidth;
        int mouseY = height - Mouse.getY() * height / mc.displayHeight - 1;
        int mouseDX = Mouse.getDX() * width / mc.displayWidth * 3;
        int mouseDY = -Mouse.getDY() * height / mc.displayHeight * 3;

        // 如果不在范围内，不执行任何鼠标操作
        boolean isInWidthRange = 132 < mouseX && mouseX < width - 1;
        boolean isInHeightRange = 15 < mouseY && mouseY < height - 16;
        if (!(isInWidthRange && isInHeightRange)) {
            return;
        }

        // 鼠标滚轮改变大小
        if (Mouse.getEventDWheel() != 0) {
            changeScaleValue(Mouse.getEventDWheel() * 0.07f);
        }
        // 鼠标左键拖动旋转
        if (Mouse.isButtonDown(0)) {
            yaw += mouseDX;
            changePitchValue(mouseDY);
        }
        // 鼠标右键移动位置
        if (Mouse.isButtonDown(1)) {
            posX += mouseDX;
            posY += mouseDY;
        }
    }

    /**
     * 更改 Scale 的数值
     *
     * @param amount 增加或者减少的数值
     */
    private void changeScaleValue(float amount) {
        float tmp = scale + amount * scale / 80;
        if (tmp > SCALE_MAX) {
            scale = SCALE_MAX;
        } else if (tmp < SCALE_MIN) {
            scale = SCALE_MIN;
        } else {
            scale = tmp;
        }
    }

    /**
     * 更改 Pitch 的数值
     *
     * @param amount 增加或者减少的数值
     */
    private void changePitchValue(float amount) {
        if (pitch - amount > PITCH_MAX) {
            pitch = 90;
        } else if (pitch - amount < PITCH_MIN) {
            pitch = -90;
        } else {
            pitch = pitch - amount;
        }
    }

    protected void drawEntityPre(RenderManager rendermanager, int middleWidth, int middleHeight) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(posX + middleWidth, posY + middleHeight, -500);
        GlStateManager.rotate(pitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.scale(-scale, scale, scale);
        RenderHelper.enableStandardItemLighting();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
    }

    protected void drawEntityPost(RenderManager rendermanager) {
        rendermanager.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    /**
     * 绘制实体部分
     */
    protected void drawEntity(int middleWidth, int middleHeight) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        drawEntityPre(rendermanager, middleWidth, middleHeight);
        rendermanager.renderEntity(guiEntity, 0.0D, -1.0D, 0.0D, 0.0F, 0.0f, true);
        drawEntityPost(rendermanager);
    }

    /**
     * 依据原版绘制矩形修改的方法，此方法额外可修改 zLevel
     */
    private void drawGradientRect(Point4d size, int color, double zLevel) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(size.z, size.y, zLevel).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(size.x, size.y, zLevel).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(size.x, size.w, zLevel).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(size.z, size.w, zLevel).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    enum BUTTON {
        // 关闭按钮
        CLOSE(943),
        // 显示地面
        SHOW_FLOOR(944),
        // 返回按钮
        RETURN(945),
        // 其他按钮，把决定权交给子类，所以里面的数值是不用的
        OTHER(-1);

        private int index;

        BUTTON(int index) {
            this.index = index;
        }

        /**
         * 通过序号获取对应的 BUTTON 枚举类型
         */
        static BUTTON getButtonFromIndex(int index) {
            for (BUTTON button : BUTTON.values()) {
                if (button.getIndex() == index) {
                    return button;
                }
            }
            return OTHER;
        }

        public int getIndex() {
            return index;
        }
    }
}
