package com.github.tartaricacid.touhoulittlemaid.client.gui.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.ScheduleButton;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.TaskButton;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.tartaricacid.touhoulittlemaid.inventory.MaidInventory;
import com.github.tartaricacid.touhoulittlemaid.item.BackpackLevel;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.MaidConfigMessage;
import com.github.tartaricacid.touhoulittlemaid.network.message.MaidTaskMessage;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
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
import net.minecraft.util.text.*;

import java.util.List;

import static com.github.tartaricacid.touhoulittlemaid.util.GuiTools.NO_ACTION;

public class MaidInventoryGui extends ContainerScreen<MaidInventory> {
    private static final ResourceLocation BG = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_main.png");
    private static final ResourceLocation SIDE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_side.png");
    private static final ResourceLocation BACKPACK = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_backpack.png");
    private static final ResourceLocation BUTTON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_button.png");
    private static final ResourceLocation TASK = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_task.png");
    private static final int TASK_COUNT_PER_PAGE = 12;
    private static int TASK_PAGE = 0;
    private final EntityMaid maid;
    private ToggleWidget home;
    private ToggleWidget pick;
    private ToggleWidget ride;
    private ImageButton info;
    private ImageButton skin;
    private ImageButton pageDown;
    private ImageButton pageUp;
    private ImageButton pageClose;
    private ImageButton taskSwitch;
    private ScheduleButton scheduleButton;
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
        this.addStateButton();
        this.addTaskSwitchButton();
        this.addTaskControlButton();
        this.addTaskListButton();
        this.addScheduleButton();
        this.addHomeButton();
        this.addPickButton();
        this.addRideButton();
    }

    @Override
    @SuppressWarnings("all")
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.drawCurrentTaskText(matrixStack);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        matrixStack.translate(0, 0, -100);
        renderBackground(matrixStack);
        getMinecraft().textureManager.bind(BG);
        blit(matrixStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        this.drawMaidCharacter(x, y);
        this.drawBaseInfoGui(matrixStack);
        this.drawBackpackGui(matrixStack);
        this.drawTaskListBg(matrixStack);
    }

    @Override
    protected void renderTooltip(MatrixStack matrixStack, int x, int y) {
        super.renderTooltip(matrixStack, x, y);
        renderTransTooltip(home, matrixStack, x, y, "gui.touhou_little_maid.button.home");
        renderTransTooltip(pick, matrixStack, x, y, "gui.touhou_little_maid.button.pickup");
        renderTransTooltip(ride, matrixStack, x, y, "gui.touhou_little_maid.button.maid_riding_set");
        renderTransTooltip(skin, matrixStack, x, y, "gui.touhou_little_maid.button.skin");
        renderTransTooltip(pageUp, matrixStack, x, y, "gui.touhou_little_maid.task.next_page");
        renderTransTooltip(pageDown, matrixStack, x, y, "gui.touhou_little_maid.task.previous_page");
        renderTransTooltip(pageClose, matrixStack, x, y, "gui.touhou_little_maid.task.close");
        renderTransTooltip(taskSwitch, matrixStack, x, y, "gui.touhou_little_maid.task.switch");
        renderMaidInfo(matrixStack, x, y);
        renderScheduleInfo(matrixStack, x, y);
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int x, int y) {
        this.drawTaskPageCount(matrixStack);
    }

    private void addStateButton() {
        skin = new ImageButton(leftPos + 62, topPos + 14, 9, 9, 72, 43, 10, BUTTON, (b) -> getMinecraft().setScreen(new MaidModelGui(maid)));
        info = new ImageButton(leftPos + 8, topPos + 14, 9, 9, 72, 65, 10, BUTTON, NO_ACTION);
        this.addButton(skin);
        this.addButton(info);
    }

    private void addTaskControlButton() {
        pageDown = new ImageButton(leftPos - 72, topPos + 9, 16, 13, 93, 0, 14, TASK, (b) -> {
            List<IMaidTask> tasks = TaskManager.getTaskIndex();
            if (TASK_PAGE * TASK_COUNT_PER_PAGE + TASK_COUNT_PER_PAGE < tasks.size()) {
                TASK_PAGE++;
                init();
            }
        });
        pageUp = new ImageButton(leftPos - 89, topPos + 9, 16, 13, 110, 0, 14, TASK, (b) -> {
            if (TASK_PAGE > 0) {
                TASK_PAGE--;
                init();
            }
        });
        pageClose = new ImageButton(leftPos - 19, topPos + 9, 13, 13, 127, 0, 14, TASK, (b) -> {
            taskListOpen = false;
            init();
        });
        this.addButton(pageUp);
        this.addButton(pageDown);
        this.addButton(pageClose);
        pageUp.visible = taskListOpen;
        pageDown.visible = taskListOpen;
        pageClose.visible = taskListOpen;
    }

    private void addTaskListButton() {
        List<IMaidTask> tasks = TaskManager.getTaskIndex();
        if (TASK_PAGE * TASK_COUNT_PER_PAGE >= tasks.size()) {
            TASK_PAGE = 0;
        }
        for (int count = 0; count < TASK_COUNT_PER_PAGE; count++) {
            int index = TASK_PAGE * TASK_COUNT_PER_PAGE + count;
            if (index < tasks.size()) {
                drawPerTaskButton(tasks, count, index);
            }
        }
    }

    private void drawPerTaskButton(List<IMaidTask> tasks, int count, int index) {
        final IMaidTask maidTask = tasks.get(index);
        TaskButton button = new TaskButton(maidTask, leftPos - 89, topPos + 23 + 19 * count,
                83, 19, 93, 28, 20, TASK, 256, 256,
                (b) -> NetworkHandler.CHANNEL.sendToServer(new MaidTaskMessage(maid.getId(), maidTask.getUid())),
                (b, m, x, y) -> renderComponentTooltip(m, maidTask.getDescription(maid), x, y), StringTextComponent.EMPTY);
        this.addButton(button);
        button.visible = taskListOpen;
    }

    private void addScheduleButton() {
        scheduleButton = new ScheduleButton(leftPos + 9, topPos + 177, this);
        this.addButton(scheduleButton);
    }

    private void addTaskSwitchButton() {
        taskSwitch = new ImageButton(leftPos + 4, topPos + 149, 71, 21, 0, 42, 22, BUTTON, (b) -> {
            taskListOpen = !taskListOpen;
            init();
        });
        this.addButton(taskSwitch);
    }

    private void addRideButton() {
        ride = new ToggleWidget(leftPos + 51, topPos + 196, 20, 20, maid.isRideable()) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                this.isStateTriggered = !this.isStateTriggered;
                NetworkHandler.CHANNEL.sendToServer(new MaidConfigMessage(maid.getId(), maid.isHomeModeEnable(), maid.isPickup(), isStateTriggered, maid.getSchedule()));
            }
        };
        ride.initTextureValues(84, 0, 21, 21, BUTTON);
        this.addButton(ride);
    }

    private void addPickButton() {
        pick = new ToggleWidget(leftPos + 30, topPos + 196, 20, 20, maid.isPickup()) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                this.isStateTriggered = !this.isStateTriggered;
                NetworkHandler.CHANNEL.sendToServer(new MaidConfigMessage(maid.getId(), maid.isHomeModeEnable(), isStateTriggered, maid.isRideable(), maid.getSchedule()));
            }
        };
        pick.initTextureValues(42, 0, 21, 21, BUTTON);
        this.addButton(pick);
    }

    private void addHomeButton() {
        home = new ToggleWidget(leftPos + 9, topPos + 196, 20, 20, maid.isHomeModeEnable()) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                this.isStateTriggered = !this.isStateTriggered;
                NetworkHandler.CHANNEL.sendToServer(new MaidConfigMessage(maid.getId(), isStateTriggered, maid.isPickup(), maid.isRideable(), maid.getSchedule()));
            }
        };
        home.initTextureValues(0, 0, 21, 21, BUTTON);
        this.addButton(home);
    }

    private void drawTaskPageCount(MatrixStack matrixStack) {
        if (taskListOpen) {
            String text = String.format("%d/%d", TASK_PAGE + 1, TaskManager.getTaskIndex().size() / TASK_COUNT_PER_PAGE + 1);
            font.draw(matrixStack, text, -48, 12, 0x333333);
        }
    }

    private void drawCurrentTaskText(MatrixStack matrixStack) {
        IMaidTask task = maid.getTask();
        itemRenderer.renderGuiItem(task.getIcon(), leftPos + 6, topPos + 151);
        List<IReorderingProcessor> splitTexts = font.split(task.getName(), 42);
        if (!splitTexts.isEmpty()) {
            font.draw(matrixStack, splitTexts.get(0), leftPos + 28, topPos + 155, 0x333333);
        }
    }

    private void renderMaidInfo(MatrixStack matrixStack, int mouseX, int mouseY) {
        if (info.isHovered()) {
            List<ITextComponent> list = Lists.newArrayList();
            String prefix = "§a█\u0020";

            IFormattableTextComponent title = new StringTextComponent("")
                    .append(new TranslationTextComponent("tooltips.touhou_little_maid.info.title")
                            .withStyle(TextFormatting.GOLD, TextFormatting.UNDERLINE))
                    .append(new StringTextComponent("§r\u0020"));
            if (maid.isStruckByLightning()) {
                title.append(new StringTextComponent("❀").withStyle(TextFormatting.DARK_RED));
            }
            if (maid.isInvulnerable()) {
                title.append(new StringTextComponent("✟").withStyle(TextFormatting.BLUE));
            }
            list.add(title);

            if (maid.getOwner() != null) {
                list.add(new StringTextComponent(prefix).withStyle(TextFormatting.WHITE)
                        .append(new TranslationTextComponent("tooltips.touhou_little_maid.info.owner")
                                .append(":\u0020").withStyle(TextFormatting.AQUA))
                        .append(maid.getOwner().getDisplayName()));
            }
            CustomPackLoader.MAID_MODELS.getInfo(maid.getModelId()).ifPresent((info) -> list.add(new StringTextComponent(prefix)
                    .withStyle(TextFormatting.WHITE)
                    .append(new TranslationTextComponent("tooltips.touhou_little_maid.info.model_name")
                            .append(":\u0020").withStyle(TextFormatting.AQUA))
                    .append(ParseI18n.parse(info.getName()))));
            list.add(new StringTextComponent(prefix).withStyle(TextFormatting.WHITE)
                    .append(new TranslationTextComponent("tooltips.touhou_little_maid.info.experience")
                            .append(":\u0020").withStyle(TextFormatting.AQUA))
                    .append(String.valueOf(maid.getExperience())));
            list.add(new StringTextComponent(prefix).withStyle(TextFormatting.WHITE)
                    .append(new TranslationTextComponent("tooltips.touhou_little_maid.info.favorability")
                            .append(":\u0020").withStyle(TextFormatting.AQUA))
                    .append(String.valueOf(maid.getFavorability())));

            renderComponentTooltip(matrixStack, list, mouseX, mouseY);
        }
    }

    private void renderScheduleInfo(MatrixStack matrixStack, int mouseX, int mouseY) {
        if (scheduleButton.isHovered()) {
            renderComponentTooltip(matrixStack, scheduleButton.getTooltips(), mouseX, mouseY);
        }
    }

    private void drawMaidCharacter(int x, int y) {
        double scale = getMinecraft().getWindow().getGuiScale();
        RenderSystem.enableScissor((int) ((leftPos + 6) * scale), (int) ((topPos + 107 + 42) * scale),
                (int) (67 * scale), (int) (95 * scale));
        InventoryScreen.renderEntityInInventory(leftPos + 40, topPos + 100, 40, (leftPos + 40) - x, (topPos + 70 - 20) - y, maid);
        RenderSystem.disableScissor();
    }

    private void drawTaskListBg(MatrixStack matrixStack) {
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
        blit(matrixStack, leftPos + 6, topPos + 168, 0, 47, 67, 25);
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

    public EntityMaid getMaid() {
        return maid;
    }

    private void renderTransTooltip(ImageButton button, MatrixStack matrixStack, int x, int y, String key) {
        if (button.isHovered()) {
            renderTooltip(matrixStack, new TranslationTextComponent(key), x, y);
        }
    }

    private void renderTransTooltip(ToggleWidget button, MatrixStack matrixStack, int x, int y, String key) {
        if (button.isHovered()) {
            renderComponentTooltip(matrixStack, Lists.newArrayList(
                    new TranslationTextComponent(key + "." + button.isStateTriggered()),
                    new TranslationTextComponent(key + ".desc")
            ), x, y);
        }
    }
}
