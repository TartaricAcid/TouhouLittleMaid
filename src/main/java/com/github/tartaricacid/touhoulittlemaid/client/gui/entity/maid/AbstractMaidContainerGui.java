package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.client.gui.ITooltipButton;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.model.MaidModelGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.sound.MaidSoundPackGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.*;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.compat.ipn.SortButtonScreen;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.MaidGomokuAI;
import com.github.tartaricacid.touhoulittlemaid.entity.favorability.FavorabilityManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.AbstractMaidContainer;
import com.github.tartaricacid.touhoulittlemaid.network.pack.MaidConfigPackage;
import com.github.tartaricacid.touhoulittlemaid.network.pack.MaidTaskPackage;
import com.github.tartaricacid.touhoulittlemaid.network.pack.RequestEffectPackage;
import com.github.tartaricacid.touhoulittlemaid.network.pack.SendEffectPackage;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static com.github.tartaricacid.touhoulittlemaid.util.GuiTools.NO_ACTION;

public abstract class AbstractMaidContainerGui<T extends AbstractMaidContainer> extends AbstractContainerScreen<T> {
    private static final ResourceLocation BG = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_main.png");
    private static final ResourceLocation SIDE = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_side.png");
    private static final ResourceLocation BUTTON = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_button.png");
    private static final ResourceLocation TASK = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_task.png");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("00");
    private static final int TASK_COUNT_PER_PAGE = 12;
    private static int TASK_PAGE = 0;
    @Nullable
    private final EntityMaid maid;
    private TouhouStateSwitchButton home;
    private TouhouStateSwitchButton pick;
    private TouhouStateSwitchButton ride;
    private TouhouImageButton info;
    private TouhouImageButton skin;
    private TouhouImageButton sound;
    private TouhouImageButton pageDown;
    private TouhouImageButton pageUp;
    private TouhouImageButton pageClose;
    private TouhouImageButton taskSwitch;
    private MaidDownloadButton modelDownload;
    private ScheduleButton<T> scheduleButton;
    private boolean taskListOpen;
    private int counterTime = 0;

    public AbstractMaidContainerGui(T screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageHeight = 256;
        this.imageWidth = 256;
        this.maid = menu.getMaid();
    }

    @Override
    protected void init() {
        super.init();
        // fixme: https://github.com/TartaricAcid/TouhouLittleMaid/issues/416
        // 临时修复，应该采用更好的办法！
        if (this.maid == null) {
            return;
        }
        this.clearWidgets();
        this.addHomeButton();
        this.addPickButton();
        this.addRideButton();
        this.addDownloadButton();
        this.addStateButton();
        this.addTaskSwitchButton();
        this.addTaskControlButton();
        this.addTaskListButton();
        this.addScheduleButton();
        this.addTabsButton();
    }

    @Override
    @SuppressWarnings("all")
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        // fixme: https://github.com/TartaricAcid/TouhouLittleMaid/issues/416
        // 临时修复，应该采用更好的办法！
        if (this.maid == null) {
            return;
        }
        drawModInfo(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.drawEffectInfo(graphics);
        this.drawCurrentTaskText(graphics);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    // 增加一些额外信息，通过截图就能方便作者检查错误
    @SuppressWarnings("all")
    private void drawModInfo(GuiGraphics graphics) {
        String minecraftVersion = SharedConstants.getCurrentVersion().getName();
        String modVersion = ModList.get().getModFileById(TouhouLittleMaid.MOD_ID).versionString();
        String debugInfo = String.format("%s-%s", minecraftVersion, modVersion);
        graphics.drawCenteredString(font, debugInfo, leftPos + 80 / 2, topPos - 4, ChatFormatting.GRAY.getColor());
    }

    @SuppressWarnings("all")
    private void drawEffectInfo(GuiGraphics graphics) {
        if (taskListOpen) {
            return;
        }
        List<SendEffectPackage.EffectData> effects = maid.getEffects();
        if (!effects.isEmpty()) {
            int yOffset = 5;
            for (SendEffectPackage.EffectData effect : effects) {
                MutableComponent text = Component.translatable(effect.descriptionId);
                if (effect.amplifier >= 1 && effect.amplifier <= 9) {
                    MutableComponent levelText = Component.translatable("enchantment.level." + (effect.amplifier + 1));
                    text = text.append(CommonComponents.SPACE).append(levelText);
                }
                String duration;
                if (effect.duration == -1) {
                    duration = I18n.get("effect.duration.infinite");
                } else {
                    duration = StringUtil.formatTickDuration(effect.duration, 20);
                }
                text = text.append(CommonComponents.SPACE).append(duration);
                graphics.drawString(font, text, leftPos - font.width(text) - 3, topPos + yOffset + 5, getPotionColor(effect.category));
                yOffset += 10;
            }
        }
    }

    @SuppressWarnings("all")
    private int getPotionColor(int category) {
        switch (category) {
            case 0:
                return ChatFormatting.GREEN.getColor();
            case 1:
                return ChatFormatting.RED.getColor();
            default:
                return ChatFormatting.BLUE.getColor();
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
        graphics.pose().translate(0, 0, -100);
        renderBackground(graphics, x, y, partialTicks);
        graphics.blit(BG, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        SortButtonScreen.renderBackground(graphics, leftPos + 249, topPos + 166);
        this.drawMaidCharacter(graphics, x, y);
        this.drawBaseInfoGui(graphics);
        this.drawTaskListBg(graphics);
    }

    @Override
    protected void renderTooltip(GuiGraphics graphics, int x, int y) {
        super.renderTooltip(graphics, x, y);
        renderTransTooltip(home, graphics, x, y, "gui.touhou_little_maid.button.home");
        renderTransTooltip(pick, graphics, x, y, "gui.touhou_little_maid.button.pickup");
        renderTransTooltip(ride, graphics, x, y, "gui.touhou_little_maid.button.maid_riding_set");
        renderTransTooltip(modelDownload, graphics, x, y, "gui.touhou_little_maid.button.model_download");
        renderTransTooltip(skin, graphics, x, y, "gui.touhou_little_maid.button.skin");
        renderTransTooltip(sound, graphics, x, y, "gui.touhou_little_maid.button.sound");
        renderTransTooltip(pageUp, graphics, x, y, "gui.touhou_little_maid.task.previous_page");
        renderTransTooltip(pageDown, graphics, x, y, "gui.touhou_little_maid.task.next_page");
        renderTransTooltip(pageClose, graphics, x, y, "gui.touhou_little_maid.task.close");
        renderTransTooltip(taskSwitch, graphics, x, y, "gui.touhou_little_maid.task.switch");
        renderMaidInfo(graphics, x, y);
        renderScheduleInfo(graphics, x, y);
        renderTaskButtonInfo(graphics, x, y);
        modelDownload.renderExtraTips(graphics);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int x, int y) {
        this.drawTaskPageCount(graphics);
    }

    private void addStateButton() {
        skin = new TouhouImageButton(leftPos + 62, topPos + 14, 9, 9, 72, 43, 10, BUTTON, (b) -> getMinecraft().setScreen(new MaidModelGui(maid)));
        sound = new TouhouImageButton(leftPos + 52, topPos + 14, 9, 9, 144, 43, 10, BUTTON, (b) -> getMinecraft().setScreen(new MaidSoundPackGui(maid)));
        info = new TouhouImageButton(leftPos + 8, topPos + 14, 9, 9, 72, 65, 10, BUTTON, NO_ACTION);
        this.addRenderableWidget(skin);
        this.addRenderableWidget(sound);
        this.addRenderableWidget(info);
    }

    private void addTaskControlButton() {
        pageDown = new TouhouImageButton(leftPos - 72, topPos + 9, 16, 13, 93, 0, 14, TASK, (b) -> {
            List<IMaidTask> tasks = TaskManager.getTaskIndex();
            if (TASK_PAGE * TASK_COUNT_PER_PAGE + TASK_COUNT_PER_PAGE < tasks.size()) {
                TASK_PAGE++;
                init();
            }
        });
        pageUp = new TouhouImageButton(leftPos - 89, topPos + 9, 16, 13, 110, 0, 14, TASK, (b) -> {
            if (TASK_PAGE > 0) {
                TASK_PAGE--;
                init();
            }
        });
        pageClose = new TouhouImageButton(leftPos - 19, topPos + 9, 13, 13, 127, 0, 14, TASK, (b) -> {
            taskListOpen = false;
            init();
        });
        this.addRenderableWidget(pageUp);
        this.addRenderableWidget(pageDown);
        this.addRenderableWidget(pageClose);
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
        boolean enable = maidTask.isEnable(maid);
        TaskButton button = new TaskButton(maidTask, enable, leftPos - 89, topPos + 23 + 19 * count,
                83, 19, 93, 28, 20, TASK, 256, 256,
                (b) -> {
                    if (enable) {
                        taskButtonPressed(maidTask, true);
                    }
                },
                getTaskTooltips(maidTask), Component.empty());
        this.addRenderableWidget(button);
        button.visible = taskListOpen;
    }

    // 用于开放切换任务时对当前 GUI 的操作
    protected void taskButtonPressed(IMaidTask maidTask, boolean enable) {
        if (maid != null) {
            PacketDistributor.sendToServer(new MaidTaskPackage(maid.getId(), maidTask.getUid()));
        }
    }

    private List<Component> getTaskTooltips(IMaidTask maidTask) {
        List<Component> desc = ParseI18n.keysToTrans(maidTask.getDescription(maid), ChatFormatting.GRAY);
        if (!desc.isEmpty()) {
            desc.addFirst(Component.translatable("task.touhou_little_maid.desc.title").withStyle(ChatFormatting.GOLD));
        }
        if (!maidTask.isEnable(maid)) {
            List<Pair<String, Predicate<EntityMaid>>> enableConditionDesc = maidTask.getEnableConditionDesc(maid);
            // 强制显示启用条件提示
            desc.add(Component.literal(" "));
            desc.add(Component.translatable("task.touhou_little_maid.desc.enable_condition").withStyle(ChatFormatting.GOLD));

            for (Pair<String, Predicate<EntityMaid>> line : enableConditionDesc) {
                MutableComponent prefix = Component.literal("- ");
                String key = String.format("task.%s.%s.enable_condition.%s", maidTask.getUid().getNamespace(), maidTask.getUid().getPath(), line.getFirst());
                MutableComponent condition = Component.translatable(key);
                if (line.getSecond().test(maid)) {
                    condition.withStyle(ChatFormatting.GREEN);
                } else {
                    condition.withStyle(ChatFormatting.RED);
                }
                desc.add(prefix.append(condition));
            }
        }
        List<Pair<String, Predicate<EntityMaid>>> conditions = maidTask.getConditionDescription(maid);
        if (!conditions.isEmpty()) {
            desc.add(Component.literal(" "));
            desc.add(Component.translatable("task.touhou_little_maid.desc.condition").withStyle(ChatFormatting.GOLD));
        }
        for (Pair<String, Predicate<EntityMaid>> line : conditions) {
            MutableComponent prefix = Component.literal("- ");
            String key = String.format("task.%s.%s.condition.%s", maidTask.getUid().getNamespace(), maidTask.getUid().getPath(), line.getFirst());
            MutableComponent condition = Component.translatable(key);
            if (line.getSecond().test(maid)) {
                condition.withStyle(ChatFormatting.GREEN);
            } else {
                condition.withStyle(ChatFormatting.RED);
            }
            desc.add(prefix.append(condition));
        }
        if (this.getMinecraft().options.advancedItemTooltips) {
            desc.add(CommonComponents.SPACE);
            desc.add(Component.translatable("task.touhou_little_maid.advanced.id", maidTask.getUid().getPath()).withStyle(ChatFormatting.DARK_GRAY));
        }
        return desc;
    }

    private void addScheduleButton() {
        scheduleButton = new ScheduleButton<>(leftPos + 9, topPos + 187, this);
        this.addRenderableWidget(scheduleButton);
    }

    private void addTabsButton() {
        MaidTabs<T> maidTabs = new MaidTabs<>(maid.getId(), leftPos, topPos);
        MaidTabButton[] tabs = maidTabs.getTabs(this);
        for (MaidTabButton button : tabs) {
            this.addRenderableWidget(button);
        }
    }

    private void addTaskSwitchButton() {
        taskSwitch = new TouhouImageButton(leftPos + 4, topPos + 159, 71, 21, 0, 42, 22, BUTTON, (b) -> {
            taskListOpen = !taskListOpen;
            init();
        });
        this.addRenderableWidget(taskSwitch);
    }

    private void addRideButton() {
        ride = new TouhouStateSwitchButton(leftPos + 51, topPos + 206, 20, 20, maid.isRideable()) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                this.isStateTriggered = !this.isStateTriggered;
                PacketDistributor.sendToServer(new MaidConfigPackage(maid.getId(), maid.isHomeModeEnable(), maid.isPickup(), isStateTriggered, maid.getSchedule()));
            }
        };
        ride.initTextureValues(84, 0, 21, 21, BUTTON);
        this.addRenderableWidget(ride);
    }

    private void addPickButton() {
        pick = new TouhouStateSwitchButton(leftPos + 30, topPos + 206, 20, 20, maid.isPickup()) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                this.isStateTriggered = !this.isStateTriggered;
                PacketDistributor.sendToServer(new MaidConfigPackage(maid.getId(), maid.isHomeModeEnable(), isStateTriggered, maid.isRideable(), maid.getSchedule()));
            }
        };
        pick.initTextureValues(42, 0, 21, 21, BUTTON);
        this.addRenderableWidget(pick);
    }

    private void addHomeButton() {
        home = new TouhouStateSwitchButton(leftPos + 9, topPos + 206, 20, 20, maid.isHomeModeEnable()) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                this.isStateTriggered = !this.isStateTriggered;
                PacketDistributor.sendToServer(new MaidConfigPackage(maid.getId(), isStateTriggered, maid.isPickup(), maid.isRideable(), maid.getSchedule()));
            }
        };
        home.initTextureValues(0, 0, 21, 21, BUTTON);
        this.addRenderableWidget(home);
    }

    private void addDownloadButton() {
        modelDownload = new MaidDownloadButton(leftPos + 20, topPos + 230, BUTTON);
        this.addRenderableWidget(modelDownload);
    }

    private void drawTaskPageCount(GuiGraphics graphics) {
        if (taskListOpen) {
            String text = String.format("%d/%d", TASK_PAGE + 1, (TaskManager.getTaskIndex().size() - 1) / TASK_COUNT_PER_PAGE + 1);
            graphics.drawString(font, text, -48, 12, 0x333333, false);
        }
    }

    private void drawCurrentTaskText(GuiGraphics graphics) {
        IMaidTask task = maid.getTask();
        graphics.renderItem(task.getIcon(), leftPos + 6, topPos + 161);
        List<FormattedCharSequence> splitTexts = font.split(task.getName(), 42);
        if (!splitTexts.isEmpty()) {
            graphics.drawString(font, splitTexts.getFirst(), leftPos + 28, topPos + 165, 0x333333, false);
        }
    }

    private void renderMaidInfo(GuiGraphics graphics, int mouseX, int mouseY) {
        if (info.isHovered()) {
            List<Component> list = Lists.newArrayList();
            String prefix = "§a█ ";

            MutableComponent title = Component.literal("")
                    .append(Component.translatable("tooltips.touhou_little_maid.info.title")
                            .withStyle(ChatFormatting.GOLD, ChatFormatting.UNDERLINE))
                    .append(Component.literal("§r "));
            if (maid.isStruckByLightning()) {
                title.append(Component.literal("❀").withStyle(ChatFormatting.DARK_RED));
            }
            if (maid.isInvulnerable()) {
                title.append(Component.literal("✟").withStyle(ChatFormatting.BLUE));
            }
            list.add(title);

            if (maid.getOwner() != null) {
                list.add(Component.literal(prefix).withStyle(ChatFormatting.WHITE)
                        .append(Component.translatable("tooltips.touhou_little_maid.info.owner")
                                .append(": ").withStyle(ChatFormatting.AQUA))
                        .append(maid.getOwner().getDisplayName()));
            }
            CustomPackLoader.MAID_MODELS.getInfo(maid.getModelId()).ifPresent((info) -> list.add(Component.literal(prefix)
                    .withStyle(ChatFormatting.WHITE)
                    .append(Component.translatable("tooltips.touhou_little_maid.info.model_name")
                            .append(": ").withStyle(ChatFormatting.AQUA))
                    .append(ParseI18n.parse(info.getName()))));
            list.add(Component.literal(prefix).withStyle(ChatFormatting.WHITE)
                    .append(Component.translatable("tooltips.touhou_little_maid.info.experience")
                            .append(": ").withStyle(ChatFormatting.AQUA))
                    .append(String.valueOf(maid.getExperience())));
            list.add(Component.literal(prefix).withStyle(ChatFormatting.WHITE)
                    .append(Component.translatable("tooltips.touhou_little_maid.info.favorability")
                            .append(": ").withStyle(ChatFormatting.AQUA))
                    .append(String.valueOf(maid.getFavorability())));
            list.add(Component.literal(prefix).withStyle(ChatFormatting.WHITE)
                    .append(Component.translatable("block.touhou_little_maid.gomoku")
                            .append(": ").withStyle(ChatFormatting.AQUA))
                    .append(Component.translatable("tooltips.touhou_little_maid.info.game_skill.gomoku", MaidGomokuAI.getMaidCount(maid), MaidGomokuAI.getRank(maid))));

            graphics.renderComponentTooltip(font, list, mouseX, mouseY);
        }
    }

    private void renderScheduleInfo(GuiGraphics graphics, int mouseX, int mouseY) {
        if (scheduleButton.isHovered()) {
            graphics.renderComponentTooltip(font, scheduleButton.getTooltips(), mouseX, mouseY);
        }
    }

    private void renderTaskButtonInfo(GuiGraphics graphics, int x, int y) {
        this.renderables.stream().filter(b -> b instanceof ITooltipButton).forEach(b -> {
            ITooltipButton tooltipButton = (ITooltipButton) b;
            if (tooltipButton.isHovered()) {
                tooltipButton.renderTooltip(graphics, getMinecraft(), x, y);
            }
        });
    }

    private void drawMaidCharacter(GuiGraphics graphics, int x, int y) {
        double scale = getMinecraft().getWindow().getGuiScale();
        RenderSystem.enableScissor((int) ((leftPos + 6) * scale), (int) ((topPos + 107 + 42) * scale),
                (int) (67 * scale), (int) (95 * scale));
        InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, leftPos + 40, topPos + 100, 40, (leftPos + 40) - x, (topPos + 70 - 20) - y, maid);
        RenderSystem.disableScissor();
    }

    private void drawTaskListBg(GuiGraphics graphics) {
        if (taskListOpen) {
            graphics.blit(TASK, leftPos - 93, topPos + 5, 0, 0, 92, 251);
        }
    }

    @SuppressWarnings("all")
    private void drawBaseInfoGui(GuiGraphics graphics) {
        graphics.pose().translate(0, 0, 200);
        {
            graphics.blit(SIDE, leftPos + 53, topPos + 113, 0, 0, 9, 9);
            graphics.blit(SIDE, leftPos + 5, topPos + 113, 0, 9, 47, 9);
            double hp = maid.getHealth() / maid.getMaxHealth();
            graphics.blit(SIDE, leftPos + 7, topPos + 115, 2, 18, (int) (43 * hp), 5);
            drawNumberScale(graphics, maid.getHealth(), leftPos + 63, topPos + 114);
        }
        {
            graphics.blit(SIDE, leftPos + 53, topPos + 124, 9, 0, 9, 9);
            graphics.blit(SIDE, leftPos + 5, topPos + 124, 0, 9, 47, 9);
            double armor = maid.getAttributeValue(Attributes.ARMOR) / 20;
            graphics.blit(SIDE, leftPos + 7, topPos + 126, 2, 23, (int) (43 * armor), 5);
            drawNumberScale(graphics, maid.getArmorValue(), leftPos + 63, topPos + 125);
        }
        {
            graphics.blit(SIDE, leftPos + 53, topPos + 135, 18, 0, 9, 9);
            graphics.blit(SIDE, leftPos + 5, topPos + 135, 0, 9, 47, 9);

            int exp = maid.getExperience();
            int count = exp / 120;
            double percent = (exp % 120) / 120.0;
            graphics.blit(SIDE, leftPos + 7, topPos + 137, 2, 28, (int) (43 * percent), 5);
            drawNumberScale(graphics, count, leftPos + 63, topPos + 136);
        }
        {
            graphics.blit(SIDE, leftPos + 53, topPos + 146, 27, 0, 9, 9);
            graphics.blit(SIDE, leftPos + 5, topPos + 146, 0, 9, 47, 9);
            FavorabilityManager manager = maid.getFavorabilityManager();
            double percent = manager.getLevelPercent();
            graphics.blit(SIDE, leftPos + 7, topPos + 148, 2, 33, (int) (43 * percent), 5);
            drawNumberScale(graphics, manager.getLevel(), leftPos + 63, topPos + 147);
        }

        graphics.blit(SIDE, leftPos + 94, topPos + 7, 107, 0, 149, 21);
        graphics.blit(SIDE, leftPos + 6, topPos + 178, 0, 47, 67, 25);
    }

    @SuppressWarnings("all")
    private void drawNumberScale(GuiGraphics graphics, double value, int posX, int posY) {
        String text = DECIMAL_FORMAT.format(value);
        graphics.pose().pushPose();
        graphics.pose().scale(0.5f, 0.5f, 1);
        graphics.drawString(font, text, posX * 2, posY * 2 + font.lineHeight / 2, ChatFormatting.DARK_GRAY.getColor(), false);
        graphics.pose().popPose();
    }

    @Override
    protected void containerTick() {
        counterTime += 1;
        if (counterTime % 20 == 0 && maid != null) {
            PacketDistributor.sendToServer(new RequestEffectPackage(maid.getId()));
        }
    }

    public boolean isTaskListOpen() {
        return taskListOpen;
    }

    public int[] getTaskListAreas() {
        return new int[]{leftPos - 93, topPos + 5, 92, 251};
    }

    public EntityMaid getMaid() {
        return maid;
    }

    private void renderTransTooltip(TouhouImageButton button, GuiGraphics graphics, int x, int y, String key) {
        if (button.isHovered()) {
            graphics.renderComponentTooltip(font, Collections.singletonList(Component.translatable(key)), x, y);
        }
    }

    private void renderTransTooltip(TouhouStateSwitchButton button, GuiGraphics graphics, int x, int y, String key) {
        if (button.isHovered()) {
            graphics.renderComponentTooltip(font, Lists.newArrayList(
                    Component.translatable(key + "." + button.isStateTriggered()),
                    Component.translatable(key + ".desc")
            ), x, y);
        }
    }
}
