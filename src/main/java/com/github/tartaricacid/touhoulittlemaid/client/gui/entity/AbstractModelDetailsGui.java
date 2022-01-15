package com.github.tartaricacid.touhoulittlemaid.client.gui.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.IModelInfo;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.sun.javafx.geom.Vec4d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;

public abstract class AbstractModelDetailsGui<T extends LivingEntity, E extends IModelInfo> extends Screen {
    protected static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/skin_detail.png");

    private static final float SCALE_MAX = 360f;
    private static final float SCALE_MIN = 18f;
    private static final float PITCH_MAX = 90f;
    private static final float PITCH_MIN = -90f;

    private static Vec4d BACKGROUND_SIZE;
    private static Vec4d BOTTOM_STATUS_BAR_SIZE;
    private static Vec4d SIDE_MENU_SIZE;
    private static Vec4d TOP_STATUS_BAR_SIZE;

    protected T sourceEntity;
    protected volatile T guiEntity;
    protected E modelInfo;

    private float posX = 0;
    private float posY = 25;
    private float scale = 80;
    private float yaw = 160;
    private float pitch = 0;

    public AbstractModelDetailsGui(T sourceEntity, @Nullable T guiEntity, E modelInfo) {
        super(new StringTextComponent("Custom Model Details GUI"));
        this.sourceEntity = sourceEntity;
        this.guiEntity = guiEntity;
        this.modelInfo = modelInfo;
    }

    abstract protected void applyReturnButtonLogic();

    @Override
    protected void init() {
        this.buttons.clear();
        this.children.clear();

        BACKGROUND_SIZE = new Vec4d(0, 0, width, height);
        BOTTOM_STATUS_BAR_SIZE = new Vec4d(0, height - 16, width, height);
        SIDE_MENU_SIZE = new Vec4d(0, 0, 132, height);
        TOP_STATUS_BAR_SIZE = new Vec4d(0, 0, width, 15);

        ImageButton closeButton = new ImageButton(width - 15, 0, 15, 15,
                0, 24, 15, BUTTON_TEXTURE, b -> Minecraft.getInstance().setScreen(null));
        ImageButton floorButton = new ImageButton(width - 30, 0, 15, 15,
                30, 24, 15, BUTTON_TEXTURE, b -> {
        });
        ImageButton returnButton = new ImageButton(width - 45, 0, 15, 15,
                15, 24, 15, BUTTON_TEXTURE, b -> applyReturnButtonLogic());
        addButton(closeButton);
        addButton(floorButton);
        addButton(returnButton);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        if (minecraft == null) {
            return;
        }

        this.fillGradient(stack, BACKGROUND_SIZE, 0xfe17191d, -1000);
        renderEntity((width + 132) / 2, height / 2);

        RenderSystem.pushMatrix();
        RenderSystem.translatef(width - 16, height - 32, -20);
        RenderSystem.rotatef(-pitch, 1.0F, 0.0F, 0.0F);
        RenderSystem.rotatef(yaw, 0.0F, 1.0F, 0.0F);
        RenderSystem.rotatef(180, 0.0F, 1.0F, 0.0F);
        RenderSystem.renderCrosshair(10);
        RenderSystem.popMatrix();

        this.fillGradient(stack, BOTTOM_STATUS_BAR_SIZE, 0xfe282c34);
        String name = String.format("%s %s", "\u2714", I18n.get(ParseI18n.getI18nKey(modelInfo.getName())));
        String info = String.format("%d FPS %.2f%%", Minecraft.fps, scale * 100 / 80);
        drawString(stack, font, name, 136, this.height - 12, 0xcacad4);
        drawString(stack, font, info, this.width - font.width(info) - 4, this.height - 12, 0xcacad4);

        this.fillGradient(stack, SIDE_MENU_SIZE, 0xfe21252b);
        this.fillGradient(stack, TOP_STATUS_BAR_SIZE, 0xfe282c34);
        drawString(stack, font, getTitle(), 6, 4, 0xffaaaaaa);

        super.render(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        // 如果不在范围内，不执行任何鼠标操作
        boolean isInWidthRange = 132 < mouseX && mouseX < width - 1;
        boolean isInHeightRange = 15 < mouseY && mouseY < height - 16;
        boolean isInRange = isInWidthRange && isInHeightRange;
        if (minecraft == null || !isInRange) {
            return false;
        }

        // 鼠标左键拖动旋转
        if (button == 0) {
            yaw += dragX;
            changePitchValue((float) -dragY);
        }
        // 鼠标右键移动位置
        if (button == 1) {
            posX += dragX;
            posY += dragY;
        }
        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        boolean isInWidthRange = 132 < mouseX && mouseX < width - 1;
        boolean isInHeightRange = 15 < mouseY && mouseY < height - 16;
        boolean isInRange = isInWidthRange && isInHeightRange;
        if (minecraft == null || !isInRange) {
            return false;
        }
        if (delta != 0) {
            changeScaleValue((float) delta * 0.07f);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    private void changePitchValue(float amount) {
        if (pitch - amount > PITCH_MAX) {
            pitch = 90;
        } else if (pitch - amount < PITCH_MIN) {
            pitch = -90;
        } else {
            pitch = pitch - amount;
        }
    }

    private void changeScaleValue(float amount) {
        float tmp = scale + amount * scale;
        scale = MathHelper.clamp(tmp, SCALE_MIN, SCALE_MAX);
    }


    private void renderEntity(int middleWidth, int middleHeight) {
        RenderSystem.pushMatrix();
        RenderSystem.translatef(posX + middleWidth, posY + middleHeight, -550);
        RenderSystem.scalef(1.0F, 1.0F, -1.0F);
        MatrixStack matrixstack = new MatrixStack();
        matrixstack.translate(0, 0, -500);
        matrixstack.scale(scale, scale, scale);
        Quaternion zRot = Vector3f.ZP.rotationDegrees(-180.0F);
        Quaternion yRot = Vector3f.YP.rotationDegrees(yaw);
        Quaternion xRot = Vector3f.XP.rotationDegrees(pitch);
        yRot.mul(xRot);
        zRot.mul(yRot);
        matrixstack.mulPose(zRot);
        EntityRendererManager manager = Minecraft.getInstance().getEntityRenderDispatcher();
        xRot.conj();
        manager.overrideCameraOrientation(xRot);
        manager.setRenderShadow(false);
        IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> {
            manager.render(guiEntity, 0, -0.5, 0, 0, 1, matrixstack, buffer, 15728880);
        });
        buffer.endBatch();
        manager.setRenderShadow(true);
        RenderSystem.popMatrix();
    }

    private void fillGradient(MatrixStack matrixStack, Vec4d vec4d, int color) {
        fillGradient(matrixStack, (int) vec4d.x, (int) vec4d.y, (int) vec4d.z, (int) vec4d.w, color, color);
    }

    private void fillGradient(MatrixStack matrixStack, Vec4d vec4d, int color, int zLevel) {
        int blitOffset = getBlitOffset();
        setBlitOffset(zLevel);
        fillGradient(matrixStack, vec4d, color);
        setBlitOffset(blitOffset);
    }
}
