package com.github.tartaricacid.touhoulittlemaid.client.gui.skin;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.ModelItem;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityMarisaBroom;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.proxy.ClientProxy;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.GuiButtonToggle;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
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

import static com.github.tartaricacid.touhoulittlemaid.client.gui.skin.MaidSkinDetailsGui.BUTTON.*;

/**
 * @author TartaricAcid
 * @date 2019/8/3 19:58
 **/
@SideOnly(Side.CLIENT)
public class MaidSkinDetailsGui extends GuiScreen {
    private static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/skin_detail.png");

    /**
     * 相关物品实例，用于渲染实体持有效果，ARMOR_ITEM 仅为占位用物品
     */
    private static final ItemStack MAIN_HAND_SWORD = Items.DIAMOND_SWORD.getDefaultInstance();
    private static final ItemStack OFF_HAND_SHIELD = Items.SHIELD.getDefaultInstance();
    private static final ItemStack ARMOR_ITEM = Items.GOLDEN_APPLE.getDefaultInstance();

    /**
     * 部分缩放，旋转所限制的范围
     */
    private static final float SCALE_MAX = 360f;
    private static final float SCALE_MIN = 18f;
    private static final float PITCH_MAX = 90f;
    private static final float PITCH_MIN = -90f;

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
    private EntityMaid preMaid;
    private EntityMarisaBroom marisaBroom;
    private ModelItem modelItem;
    private volatile EntityMaid maid;
    private ScheduledExecutorService timer;

    /**
     * 控制旋转、缩放和行走的变量
     */
    private float posX = 0;
    private float posY = -15;
    private float scale = 80;
    private float yaw = -45;
    private float pitch = -30;
    private volatile boolean isEnableWalk = false;

    /**
     * 所有的 GuiButtonToggle 按钮
     */
    private GuiButtonToggle begButton;
    private GuiButtonToggle walkButton;
    private GuiButtonToggle sitButton;
    private GuiButtonToggle rideButton;
    private GuiButtonToggle rideBroomButton;
    private GuiButtonToggle helmetButton;
    private GuiButtonToggle chestPlateButton;
    private GuiButtonToggle leggingsButton;
    private GuiButtonToggle bootsButton;
    private GuiButtonToggle mainHandButton;
    private GuiButtonToggle offHandButton;

    public MaidSkinDetailsGui(EntityMaid preMaid, ResourceLocation modelId) {
        this.preMaid = preMaid;
        this.maid = new EntityMaid(preMaid.world);
        this.marisaBroom = new EntityMarisaBroom(preMaid.world);
        this.modelItem = ClientProxy.ID_INFO_MAP.get(modelId.toString());
        this.maid.setModelId(modelId.toString());
        this.maid.isDebugFloorOpen = true;
        // 启用模拟 tick 线程
        simulationTickRun();
    }

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
        drawEntity((width - 132) / 2 + 132, height / 2);
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
        drawButtonText();
    }

    /**
     * 创建一个模拟 tick 的定时线程
     */
    private void simulationTickRun() {
        timer = new ScheduledThreadPoolExecutor(1, new BasicThreadFactory.Builder()
                .namingPattern("skin-details-gui-simulation-tick-schedule").daemon(true).build());
        timer.scheduleAtFixedRate(() -> {
            // tick 时间增加，用于一些永远进行的动画
            maid.ticksExisted += 1;
            // 行走的实现
            maid.prevLimbSwingAmount = maid.limbSwingAmount;
            // 依据 isEnableWalk 设置速度（0 ~ 1）
            float speed = isEnableWalk ? 0.5f : 0;
            // 来自原版的行走相关数据
            maid.limbSwingAmount += (speed - maid.limbSwingAmount) * 0.4F;
            maid.limbSwing += maid.limbSwingAmount;
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
        GuiButtonImage closeButton = new GuiButtonImage(CLOSE.ordinal(), width - 15, 0, 15, 15,
                0, 24, 15, BUTTON_TEXTURE);
        GuiButtonImage returnButton = new GuiButtonImage(RETURN.ordinal(), width - 30, 0, 15, 15,
                15, 24, 15, BUTTON_TEXTURE);
        addButton(closeButton);
        addButton(returnButton);

        // 所有的侧边栏按钮
        begButton = new GuiButtonToggle(BEG.ordinal(), 2, 17, 128, 12, maid.isBegging());
        begButton.initTextureValues(0, 0, 128, 12, BUTTON_TEXTURE);
        walkButton = new GuiButtonToggle(WALK.ordinal(), 2, 17 + 13, 128, 12, isEnableWalk);
        walkButton.initTextureValues(0, 0, 128, 12, BUTTON_TEXTURE);
        sitButton = new GuiButtonToggle(SIT.ordinal(), 2, 17 + 13 * 2, 128, 12, maid.isSitting());
        sitButton.initTextureValues(0, 0, 128, 12, BUTTON_TEXTURE);
        rideButton = new GuiButtonToggle(RIDE.ordinal(), 2, 17 + 13 * 3, 128, 12, maid.isRiding());
        rideButton.initTextureValues(0, 0, 128, 12, BUTTON_TEXTURE);
        rideBroomButton = new GuiButtonToggle(RIDE_BROOM.ordinal(), 2, 17 + 13 * 4, 128, 12, maid.isDebugBroomShow);
        rideBroomButton.initTextureValues(0, 0, 128, 12, BUTTON_TEXTURE);
        helmetButton = new GuiButtonToggle(HELMET.ordinal(), 2, 17 + 13 * 5, 128, 12, !maid.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty());
        helmetButton.initTextureValues(0, 0, 128, 12, BUTTON_TEXTURE);
        chestPlateButton = new GuiButtonToggle(CHEST_PLATE.ordinal(), 2, 17 + 13 * 6, 128, 12, !maid.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty());
        chestPlateButton.initTextureValues(0, 0, 128, 12, BUTTON_TEXTURE);
        leggingsButton = new GuiButtonToggle(LEGGINGS.ordinal(), 2, 17 + 13 * 7, 128, 12, !maid.getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty());
        leggingsButton.initTextureValues(0, 0, 128, 12, BUTTON_TEXTURE);
        bootsButton = new GuiButtonToggle(BOOTS.ordinal(), 2, 17 + 13 * 8, 128, 12, !maid.getItemStackFromSlot(EntityEquipmentSlot.FEET).isEmpty());
        bootsButton.initTextureValues(0, 0, 128, 12, BUTTON_TEXTURE);
        mainHandButton = new GuiButtonToggle(MAIN_HAND.ordinal(), 2, 17 + 13 * 9, 128, 12, !maid.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).isEmpty());
        mainHandButton.initTextureValues(0, 0, 128, 12, BUTTON_TEXTURE);
        offHandButton = new GuiButtonToggle(OFF_HAND.ordinal(), 2, 17 + 13 * 10, 128, 12, !maid.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND).isEmpty());
        offHandButton.initTextureValues(0, 0, 128, 12, BUTTON_TEXTURE);
        addButton(begButton);
        addButton(walkButton);
        addButton(sitButton);
        addButton(rideButton);
        addButton(rideBroomButton);
        addButton(helmetButton);
        addButton(chestPlateButton);
        addButton(leggingsButton);
        addButton(bootsButton);
        addButton(mainHandButton);
        addButton(offHandButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (BUTTON.getButtonFromOrdinal(button.id)) {
            case CLOSE:
                applyCloseButtonLogic();
                return;
            case RETURN:
                applyReturnButtonLogic();
                return;
            case BEG:
                applyBegButtonLogic();
                return;
            case WALK:
                applyWalkButtonLogic();
                return;
            case SIT:
                applySitButtonLogic();
                return;
            case RIDE:
                applyRideButtonLogic();
                return;
            case RIDE_BROOM:
                applyRideBroomButtonLogic();
                return;
            case HELMET:
                applyEquipmentButtonLogic(EntityEquipmentSlot.HEAD);
                return;
            case CHEST_PLATE:
                applyEquipmentButtonLogic(EntityEquipmentSlot.CHEST);
                return;
            case LEGGINGS:
                applyEquipmentButtonLogic(EntityEquipmentSlot.LEGS);
                return;
            case BOOTS:
                applyEquipmentButtonLogic(EntityEquipmentSlot.FEET);
                return;
            case MAIN_HAND:
                applyEquipmentButtonLogic(EntityEquipmentSlot.MAINHAND);
                return;
            case OFF_HAND:
                applyEquipmentButtonLogic(EntityEquipmentSlot.OFFHAND);
                return;
            default:
                applyCloseButtonLogic();
        }
    }

    private void applyCloseButtonLogic() {
        mc.addScheduledTask(() -> mc.displayGuiScreen(null));
    }

    private void applyReturnButtonLogic() {
        mc.addScheduledTask(() -> mc.displayGuiScreen(new MaidSkinGui(preMaid)));
    }

    private void applyBegButtonLogic() {
        if (maid.isBegging()) {
            begButton.setStateTriggered(false);
            maid.setBegging(false);
        } else {
            begButton.setStateTriggered(true);
            maid.setBegging(true);
        }
    }

    private void applyWalkButtonLogic() {
        if (isEnableWalk) {
            isEnableWalk = false;
            walkButton.setStateTriggered(false);
        } else {
            applyConflictReset(WALK);
            isEnableWalk = true;
            walkButton.setStateTriggered(true);
        }
    }

    private void applySitButtonLogic() {
        if (maid.isSitting()) {
            maid.setSitting(false);
            sitButton.setStateTriggered(false);
        } else {
            // 不兼容的动作列表需要归位
            applyConflictReset(SIT);
            maid.setSitting(true);
            sitButton.setStateTriggered(true);
        }
    }

    private void applyRideButtonLogic() {
        if (maid.isRiding()) {
            maid.dismountRidingEntity();
            rideButton.setStateTriggered(false);
        } else {
            // 不兼容的动作列表需要归位
            applyConflictReset(RIDE);
            maid.startRiding(marisaBroom);
            rideButton.setStateTriggered(true);
        }
    }

    private void applyRideBroomButtonLogic() {
        if (maid.getControllingPassenger() != null) {
            maid.isDebugBroomShow = false;
            rideBroomButton.setStateTriggered(false);
        } else {
            // 不兼容的动作列表需要归位
            applyConflictReset(RIDE_BROOM);
            maid.isDebugBroomShow = true;
            rideBroomButton.setStateTriggered(true);
        }
    }

    private void applyEquipmentButtonLogic(EntityEquipmentSlot slot) {
        if (!maid.getItemStackFromSlot(slot).isEmpty()) {
            maid.setItemStackToSlot(slot, ItemStack.EMPTY);
            setEquipmentStateTriggered(slot, false);
        } else {
            if (slot == EntityEquipmentSlot.MAINHAND) {
                maid.setItemStackToSlot(slot, MAIN_HAND_SWORD);
            } else if (slot == EntityEquipmentSlot.OFFHAND) {
                maid.setItemStackToSlot(slot, OFF_HAND_SHIELD);
            } else {
                maid.setItemStackToSlot(slot, ARMOR_ITEM);
            }
            setEquipmentStateTriggered(slot, true);
        }
    }

    private void setEquipmentStateTriggered(EntityEquipmentSlot slot, boolean state) {
        switch (slot) {
            case HEAD:
                helmetButton.setStateTriggered(state);
                return;
            case CHEST:
                chestPlateButton.setStateTriggered(state);
                return;
            case LEGS:
                leggingsButton.setStateTriggered(state);
                return;
            case FEET:
                bootsButton.setStateTriggered(state);
                return;
            case MAINHAND:
                mainHandButton.setStateTriggered(state);
                return;
            case OFFHAND:
                offHandButton.setStateTriggered(state);
                return;
            default:
        }
    }

    /**
     * 用于处理一些互相冲突的按钮，会将冲突的按键进行重置
     *
     * @param button 你当前按下的按键
     */
    private void applyConflictReset(BUTTON button) {
        if (button != WALK) {
            isEnableWalk = false;
            walkButton.setStateTriggered(false);
        }
        if (button != SIT) {
            maid.setSitting(false);
            sitButton.setStateTriggered(false);
        }
        if (button != RIDE) {
            maid.dismountRidingEntity();
            rideButton.setStateTriggered(false);
        }
        if (button != RIDE_BROOM) {
            maid.isDebugBroomShow = false;
            rideBroomButton.setStateTriggered(false);
        }
    }

    /**
     * 绘制按钮上的文字
     */
    private void drawButtonText() {
        fontRenderer.drawString(I18n.format("gui.touhou_little_maid.skin_details.beg"), 16, 19, 0xcacad4);
        fontRenderer.drawString(I18n.format("gui.touhou_little_maid.skin_details.walk"), 16, 19 + 13, 0xcacad4);
        fontRenderer.drawString(I18n.format("gui.touhou_little_maid.skin_details.sit"), 16, 19 + 13 * 2, 0xcacad4);
        fontRenderer.drawString(I18n.format("gui.touhou_little_maid.skin_details.ride"), 16, 19 + 13 * 3, 0xcacad4);
        fontRenderer.drawString(I18n.format("gui.touhou_little_maid.skin_details.ride_broom"), 16, 19 + 13 * 4, 0xcacad4);
        fontRenderer.drawString(I18n.format("gui.touhou_little_maid.skin_details.helmet"), 16, 19 + 13 * 5, 0xcacad4);
        fontRenderer.drawString(I18n.format("gui.touhou_little_maid.skin_details.chest_plate"), 16, 19 + 13 * 6, 0xcacad4);
        fontRenderer.drawString(I18n.format("gui.touhou_little_maid.skin_details.leggings"), 16, 19 + 13 * 7, 0xcacad4);
        fontRenderer.drawString(I18n.format("gui.touhou_little_maid.skin_details.boots"), 16, 19 + 13 * 8, 0xcacad4);
        fontRenderer.drawString(I18n.format("gui.touhou_little_maid.skin_details.main_hand"), 16, 19 + 13 * 9, 0xcacad4);
        fontRenderer.drawString(I18n.format("gui.touhou_little_maid.skin_details.off_hand"), 16, 19 + 13 * 10, 0xcacad4);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        // 如果不在范围内，不执行任何鼠标操作
        int mouseX = Mouse.getX() * width / mc.displayWidth;
        int mouseY = height - Mouse.getY() * height / mc.displayHeight - 1;
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
        CLOSE,
        // 返回按钮
        RETURN,
        // 祈求动画按钮
        BEG,
        // 行走
        WALK,
        // 待命
        SIT,
        // 普通骑乘
        RIDE,
        // 扫帚骑乘
        RIDE_BROOM,
        // 头盔
        HELMET,
        // 胸甲
        CHEST_PLATE,
        // 护腿
        LEGGINGS,
        // 靴子
        BOOTS,
        // 主手持有物品
        MAIN_HAND,
        // 副手持有物品
        OFF_HAND;

        /**
         * 通过序号获取对应的 BUTTON 枚举类型
         */
        static BUTTON getButtonFromOrdinal(int ordinal) {
            for (BUTTON button : BUTTON.values()) {
                if (button.ordinal() == ordinal) {
                    return button;
                }
            }
            return CLOSE;
        }
    }
}
