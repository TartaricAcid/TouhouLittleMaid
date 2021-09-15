package com.github.tartaricacid.touhoulittlemaid.client.gui.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.TaskButton;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.tartaricacid.touhoulittlemaid.inventory.MaidInventory;
import com.github.tartaricacid.touhoulittlemaid.item.BackpackLevel;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.MaidConfigMessage;
import com.github.tartaricacid.touhoulittlemaid.network.message.MaidTaskMessage;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.ToggleWidget;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

import static net.minecraft.client.gui.widget.button.Button.NO_TOOLTIP;

public class MaidInventoryGui extends ContainerScreen<MaidInventory> {
    private static final ResourceLocation BG = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_main.png");
    private static final ResourceLocation SIDE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_side.png");
    private static final ResourceLocation BACKPACK = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_backpack.png");
    private static final ResourceLocation BUTTON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_button.png");
    private static final ResourceLocation TASK = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_task.png");
    private static int TASK_PAGE = 0;
    private final EntityMaid maid;
    private ToggleWidget home;
    private ToggleWidget pick;
    private ToggleWidget ride;
    private ImageButton info;
    private ImageButton taskSwitch;
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

        this.addToggleButton();
        {
            this.addButton(new ImageButton(leftPos + 62, topPos + 14, 9, 9, 72, 42, 10, BUTTON, 256, 256,
                    (b) -> getMinecraft().setScreen(new MaidModelGui(maid)),
                    (b, m, x, y) -> renderTooltip(m, new TranslationTextComponent("gui.touhou_little_maid.button.skin"), x, y),
                    StringTextComponent.EMPTY));
            info = new ImageButton(leftPos + 8, topPos + 14, 9, 9, 72, 64, 10, BUTTON, 256, 256,
                    (b) -> {
                    }, NO_TOOLTIP, StringTextComponent.EMPTY);
            this.addButton(info);
        }
        this.addTaskButtons();
    }

    private void addTaskButtons() {
        ImageButton pageDown = new ImageButton(leftPos - 72, topPos + 9, 16, 13, 93, 0, 14, TASK, 256, 256,
                (b) -> {
                    // TODO: 2021/8/30 翻页功能 
                },
                (b, m, x, y) -> renderTooltip(m, new TranslationTextComponent("gui.touhou_little_maid.task.next_page"), x, y), StringTextComponent.EMPTY);

        ImageButton pageUp = new ImageButton(leftPos - 89, topPos + 9, 16, 13, 110, 0, 14, TASK, 256, 256,
                (b) -> {
                    // TODO: 2021/8/30 翻页功能
                },
                (b, m, x, y) -> renderTooltip(m, new TranslationTextComponent("gui.touhou_little_maid.task.previous_page"), x, y), StringTextComponent.EMPTY);

        ImageButton pageClose = new ImageButton(leftPos - 19, topPos + 9, 13, 13, 127, 0, 14, TASK, 256, 256,
                (b) -> {
                    taskListOpen = false;
                    init();
                },
                (b, m, x, y) -> renderTooltip(m, new TranslationTextComponent("gui.touhou_little_maid.task.close"), x, y), StringTextComponent.EMPTY);

        ImageButton taskSwitch = new ImageButton(leftPos + 4, topPos + 149, 71, 21, 0, 42, 22, BUTTON, 256, 256,
                (b) -> {
                    taskListOpen = !taskListOpen;
                    init();
                },
                (b, m, x, y) -> renderTooltip(m, new TranslationTextComponent("gui.touhou_little_maid.task.switch"), x, y), StringTextComponent.EMPTY);

        this.addButton(pageUp);
        this.addButton(pageDown);
        this.addButton(pageClose);
        this.addButton(taskSwitch);
        pageUp.visible = taskListOpen;
        pageDown.visible = taskListOpen;
        pageClose.visible = taskListOpen;

        List<IMaidTask> tasks = TaskManager.getTaskIndex();
        // 先判定首位是否超限
        if (TASK_PAGE * 12 >= tasks.size()) {
            TASK_PAGE = 0;
        }
        for (int i = 0; i < 12; i++) {
            int index = TASK_PAGE * 12 + i;
            if (index < tasks.size()) {
                final IMaidTask maidTask = tasks.get(index);
                TaskButton button = new TaskButton(maidTask, leftPos - 89, topPos + 23 + 19 * i,
                        83, 19, 93, 28, 20, TASK, 256, 256,
                        (b) -> NetworkHandler.CHANNEL.sendToServer(new MaidTaskMessage(maid.getId(), maidTask.getUid())),
                        (b, m, x, y) -> renderComponentTooltip(m, maidTask.getDescription(maid), x, y), StringTextComponent.EMPTY);
                this.addButton(button);
                button.visible = taskListOpen;
            }
        }
    }

    private void addToggleButton() {
        home = new ToggleWidget(leftPos + 9, topPos + 174, 20, 20, maid.isHomeModeEnable()) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                this.isStateTriggered = !this.isStateTriggered;
                NetworkHandler.CHANNEL.sendToServer(new MaidConfigMessage(maid.getId(), isStateTriggered, maid.isPickup(), maid.isRideable()));
            }
        };
        home.initTextureValues(0, 0, 21, 21, BUTTON);
        this.addButton(home);

        pick = new ToggleWidget(leftPos + 30, topPos + 174, 20, 20, maid.isPickup()) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                this.isStateTriggered = !this.isStateTriggered;
                NetworkHandler.CHANNEL.sendToServer(new MaidConfigMessage(maid.getId(), maid.isHomeModeEnable(), isStateTriggered, maid.isRideable()));
            }
        };
        pick.initTextureValues(42, 0, 21, 21, BUTTON);
        this.addButton(pick);

        ride = new ToggleWidget(leftPos + 51, topPos + 174, 20, 20, maid.isRideable()) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                this.isStateTriggered = !this.isStateTriggered;
                NetworkHandler.CHANNEL.sendToServer(new MaidConfigMessage(maid.getId(), maid.isHomeModeEnable(), maid.isPickup(), isStateTriggered));
            }
        };
        ride.initTextureValues(84, 0, 21, 21, BUTTON);
        this.addButton(ride);
    }

    @Override
    @SuppressWarnings("all")
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        {
            IMaidTask task = maid.getTask();
            itemRenderer.renderGuiItem(task.getIcon(), leftPos + 6, topPos + 151);
            List<IReorderingProcessor> splitTexts = font.split(task.getName(), 42);
            if (!splitTexts.isEmpty()) {
                font.draw(matrixStack, splitTexts.get(0), leftPos + 28, topPos + 155, 0x333333);
            }
        }
        renderTooltip(matrixStack, mouseX, mouseY);
        if (info.isHovered()) {
            renderMaidInfo(matrixStack, mouseX, mouseY);
        }
    }

    private void renderMaidInfo(MatrixStack matrixStack, int mouseX, int mouseY) {
        // TODO: 2021/8/30 显示详细信息
        renderComponentTooltip(matrixStack, Lists.newArrayList(
                new StringTextComponent("你的女仆当前很健康"),
                new StringTextComponent("- 血量：全满（+20）"),
                new StringTextComponent("- 心情：良好（+943）"),
                new StringTextComponent("- 饥饿状况：饱腹（+20）"),
                new StringTextComponent("- 好感度：高（+93）"),
                new StringTextComponent("- 护甲：高（+20）")
        ), mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(MatrixStack matrixStack, int x, int y) {
        super.renderTooltip(matrixStack, x, y);
        if (home.isHovered()) {
            renderComponentTooltip(matrixStack, Lists.newArrayList(
                    new TranslationTextComponent("gui.touhou_little_maid.button.home.desc"),
                    new TranslationTextComponent("gui.touhou_little_maid.button.home." + home.isStateTriggered())
            ), x, y);
        }
        if (pick.isHovered()) {
            renderComponentTooltip(matrixStack, Lists.newArrayList(
                    new TranslationTextComponent("gui.touhou_little_maid.button.pickup.desc"),
                    new TranslationTextComponent("gui.touhou_little_maid.button.pickup." + pick.isStateTriggered())
            ), x, y);
        }
        if (ride.isHovered()) {
            renderComponentTooltip(matrixStack, Lists.newArrayList(
                    new TranslationTextComponent("gui.touhou_little_maid.button.maid_riding_set.desc"),
                    new TranslationTextComponent("gui.touhou_little_maid.button.maid_riding_set." + ride.isStateTriggered())
            ), x, y);
        }
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int x, int y) {
    }

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

        this.drawBaseInfoGui(matrixStack);
        this.drawBackpackGui(matrixStack);

        if (taskListOpen) {
            getMinecraft().textureManager.bind(TASK);
            blit(matrixStack, leftPos - 93, topPos + 5, 0, 0, 92, 251);
        }
    }

    @SuppressWarnings("all")
    private void drawBaseInfoGui(MatrixStack matrixStack) {
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
            getMinecraft().font.draw(matrixStack, String.format("%d", maid.getArmorValue()), leftPos + 15, topPos + 125, TextFormatting.DARK_GRAY.getColor());
        }
        {
            getMinecraft().textureManager.bind(SIDE);
            blit(matrixStack, leftPos + 5, topPos + 135, 18, 0, 9, 9);
            blit(matrixStack, leftPos + 27, topPos + 135, 0, 9, 47, 9);
            double hunger = maid.getHunger() / 20.0;
            blit(matrixStack, leftPos + 29, topPos + 137, 2, 28, (int) (43 * hunger), 5);
            getMinecraft().font.draw(matrixStack, String.format("%d", maid.getHunger()), leftPos + 15, topPos + 136, TextFormatting.DARK_GRAY.getColor());
        }

        getMinecraft().textureManager.bind(SIDE);
        blit(matrixStack, leftPos + 94, topPos + 7, 107, 0, 149, 21);
        blit(matrixStack, leftPos + 94, topPos + 5, 107, 21, 24, 26);
        blit(matrixStack, leftPos + 98, topPos + 12, 107, 47, 16, 16);
    }

    private void drawBackpackGui(MatrixStack matrixStack) {
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
