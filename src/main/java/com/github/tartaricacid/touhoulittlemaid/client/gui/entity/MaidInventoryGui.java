package com.github.tartaricacid.touhoulittlemaid.client.gui.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.inventory.MaidInventory;
import com.github.tartaricacid.touhoulittlemaid.item.BackpackLevel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.ToggleWidget;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class MaidInventoryGui extends ContainerScreen<MaidInventory> {
    private static final ResourceLocation BG = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_main.png");
    private static final ResourceLocation SIDE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_side.png");
    private static final ResourceLocation BACKPACK = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_backpack.png");
    private static final ResourceLocation BUTTON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_button.png");
    private static final ResourceLocation TASK = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_task.png");
    private final EntityMaid maid;
    private boolean taskListOpen;

    public MaidInventoryGui(MaidInventory screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageHeight = 256;
        this.imageWidth = 256;
        this.maid = menu.getMaid();
    }

    @Override
    protected void init() {
        super.init();
        this.buttons.clear();
        this.children.clear();

        ToggleWidget home = new ToggleWidget(leftPos + 9, topPos + 174, 20, 20, false);
        home.initTextureValues(0, 0, 21, 21, BUTTON);
        this.addButton(home);

        ToggleWidget pick = new ToggleWidget(leftPos + 30, topPos + 174, 20, 20, false);
        pick.initTextureValues(42, 0, 21, 21, BUTTON);
        this.addButton(pick);

        ToggleWidget hat = new ToggleWidget(leftPos + 51, topPos + 174, 20, 20, false);
        hat.initTextureValues(84, 0, 21, 21, BUTTON);
        this.addButton(hat);

        ToggleWidget ride = new ToggleWidget(leftPos + 9, topPos + 195, 20, 20, false);
        ride.initTextureValues(126, 0, 21, 21, BUTTON);
        this.addButton(ride);

        ToggleWidget tog1 = new ToggleWidget(leftPos + 30, topPos + 195, 20, 20, false);
        tog1.initTextureValues(168, 0, 21, 21, BUTTON);
        this.addButton(tog1);

        ToggleWidget tog2 = new ToggleWidget(leftPos + 51, topPos + 195, 20, 20, false);
        tog2.initTextureValues(210, 0, 21, 21, BUTTON);
        this.addButton(tog2);

        this.addButton(new ImageButton(leftPos + 62, topPos + 14, 9, 9, 72, 42, 10, BUTTON, 256, 256,
                (b) -> getMinecraft().setScreen(new MaidModelGui(maid)),
                (b, m, x, y) -> renderTooltip(m, new TranslationTextComponent("gui.touhou_little_maid.button.skin"), x, y),
                StringTextComponent.EMPTY));

        ImageButton pageDown = new ImageButton(leftPos - 72, topPos + 9, 16, 13, 93, 0, 14, TASK, 256, 256,
                (b) -> {
                },
                (b, m, x, y) -> renderTooltip(m, new StringTextComponent("下一页"), x, y), StringTextComponent.EMPTY);

        ImageButton pageUp = new ImageButton(leftPos - 89, topPos + 9, 16, 13, 110, 0, 14, TASK, 256, 256,
                (b) -> {
                },
                (b, m, x, y) -> renderTooltip(m, new StringTextComponent("上一页"), x, y), StringTextComponent.EMPTY);

        ImageButton pageClose = new ImageButton(leftPos - 19, topPos + 9, 13, 13, 127, 0, 14, TASK, 256, 256,
                (b) -> {
                    taskListOpen = false;
                    init();
                },
                (b, m, x, y) -> renderTooltip(m, new StringTextComponent("关闭"), x, y), StringTextComponent.EMPTY);

        this.addButton(pageUp);
        this.addButton(pageDown);
        this.addButton(pageClose);

        this.addButton(new ImageButton(leftPos + 4, topPos + 149, 71, 21, 0, 42, 22, BUTTON, 256, 256,
                (b) -> {
                    taskListOpen = !taskListOpen;
                    init();
                },
                (b, m, x, y) -> renderTooltip(m, new StringTextComponent("xxx"), x, y), StringTextComponent.EMPTY));

        pageUp.visible = taskListOpen;
        pageDown.visible = taskListOpen;
        pageClose.visible = taskListOpen;
    }

    @Override
    @SuppressWarnings("all")
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        {
            IMaidTask task = maid.getTask();
            itemRenderer.renderGuiItem(task.getIcon(), leftPos + 6, topPos + 151);
        }
        renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int x, int y) {
    }

    @SuppressWarnings("all")
    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        matrixStack.translate(0, 0, -100);
        renderBackground(matrixStack);
        getMinecraft().textureManager.bind(BG);
        blit(matrixStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        double scale = getMinecraft().getWindow().getGuiScale();
        RenderSystem.enableScissor((int) ((leftPos + 6) * scale), (int) ((topPos + 107 + 42) * scale),
                (int) (67 * scale), (int) (95 * scale));
        InventoryScreen.renderEntityInInventory(leftPos + 40, topPos + 100, 40, (leftPos + 40) - x, (topPos + 70 - 20) - y, maid);
        RenderSystem.disableScissor();

        matrixStack.translate(0, 0, 200);
        {
            getMinecraft().textureManager.bind(SIDE);
            blit(matrixStack, leftPos + 5, topPos + 113, 0, 0, 9, 9);
            blit(matrixStack, leftPos + 27, topPos + 113, 0, 9, 47, 9);
            double hp = maid.getHealth() / maid.getMaxHealth();
            blit(matrixStack, leftPos + 29, topPos + 115, 2, 18, (int) (43 * hp), 5);
            getMinecraft().font.draw(matrixStack, String.format("%.0f", maid.getHealth()), leftPos + 15, topPos + 114, TextFormatting.DARK_GRAY.getColor());
        }
        {
            getMinecraft().textureManager.bind(SIDE);
            blit(matrixStack, leftPos + 5, topPos + 124, 9, 0, 9, 9);
            blit(matrixStack, leftPos + 27, topPos + 124, 0, 9, 47, 9);
            double armor = maid.getAttributeValue(Attributes.ARMOR) / 20;
            blit(matrixStack, leftPos + 29, topPos + 126, 2, 23, (int) (43 * armor), 5);
            getMinecraft().font.draw(matrixStack, String.format("%.0f", maid.getAttributeValue(Attributes.ARMOR)), leftPos + 15, topPos + 125, TextFormatting.DARK_GRAY.getColor());
        }
        {
            getMinecraft().textureManager.bind(SIDE);
            blit(matrixStack, leftPos + 5, topPos + 135, 18, 0, 9, 9);
            blit(matrixStack, leftPos + 27, topPos + 135, 0, 9, 47, 9);
            double hunger = maid.getHunger() / 20;
            blit(matrixStack, leftPos + 29, topPos + 137, 2, 28, (int) (43 * hunger), 5);
            getMinecraft().font.draw(matrixStack, String.format("%d", maid.getHunger()), leftPos + 15, topPos + 136, TextFormatting.DARK_GRAY.getColor());
        }

        getMinecraft().textureManager.bind(SIDE);
        blit(matrixStack, leftPos + 94, topPos + 7, 107, 0, 149, 21);
        blit(matrixStack, leftPos + 94, topPos + 5, 107, 21, 24, 26);
        blit(matrixStack, leftPos + 98, topPos + 12, 107, 47, 16, 16);

        getMinecraft().textureManager.bind(BACKPACK);
        blit(matrixStack, leftPos + 85, topPos + 36, 0, 0, 165, 122);

        int level = maid.getBackpackLevel();
        if (level < BackpackLevel.SMALL) {
            fill(matrixStack, leftPos + 142, topPos + 58, leftPos + 250, topPos + 76, 0xaa222222);
            blit(matrixStack, leftPos + 190, topPos + 62, 165, 0, 11, 11);
        }
        if (level < BackpackLevel.MIDDLE) {
            fill(matrixStack, leftPos + 142, topPos + 81, leftPos + 250, topPos + 117, 0xaa222222);
            blit(matrixStack, leftPos + 190, topPos + 92, 165, 0, 11, 11);
        }
        if (level < BackpackLevel.BIG) {
            fill(matrixStack, leftPos + 142, topPos + 122, leftPos + 250, topPos + 158, 0xaa222222);
            blit(matrixStack, leftPos + 190, topPos + 133, 165, 0, 11, 11);
        }

        if (taskListOpen) {
            getMinecraft().textureManager.bind(TASK);
            blit(matrixStack, leftPos - 93, topPos + 5, 0, 0, 92, 251);
        }
    }

    @Override
    public int getGuiLeft() {
        if (taskListOpen) {
            return leftPos - 93;
        }
        return leftPos;
    }

    @Override
    public int getXSize() {
        if (taskListOpen) {
            return imageWidth + 93;
        }
        return imageWidth;
    }
}
