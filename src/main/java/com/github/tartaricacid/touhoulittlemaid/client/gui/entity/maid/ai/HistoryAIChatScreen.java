package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.UserPromptContexts;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.response.ResponseChat;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMMessage;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.Role;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.response.ToolCall;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.FlatColorButton;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.HistoryChatWidget;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.ai.ClearMaidAIDataMessage;
import com.google.common.collect.Lists;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HistoryAIChatScreen extends Screen {
    private static final MutableComponent HISTORY_TITLE = Component.translatable("gui.touhou_little_maid.button.maid_ai_chat_config.history_chat.title");
    private static final MutableComponent HISTORY_EMPTY = Component.translatable("gui.touhou_little_maid.button.maid_ai_chat_config.history_chat_is_empty");
    private static final MutableComponent SUMMARY_TITLE = Component.translatable("gui.touhou_little_maid.button.maid_ai_chat_config.history_chat.summary_title");
    private static final MutableComponent SUMMARY_EMPTY = Component.translatable("gui.touhou_little_maid.button.maid_ai_chat_config.history_chat.summary_empty");

    private static final int CHAT_TEXT_WIDTH = 140;
    private static final int TOOL_TEXT_WIDTH = 180;

    private static final int SUMMARY_WIDTH = 120;
    private static final float SUMMARY_TEXT_SCALE = 0.5f;
    private static final int SUMMARY_TOP = 24;
    private static final int RIGHT_COLUMN_BOTTOM_MARGIN = 24;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_GAP = 6;
    private static final int SUMMARY_BUTTON_GAP = 12;
    private static final int SUMMARY_MIN_HEIGHT = 48;

    private final EntityMaid maid;
    private final @Nullable Screen parent;
    private final ResourceLocation playerSkin;
    private final List<LLMMessage> history = Lists.newArrayList();
    private final List<Renderable> historyWidgets = Lists.newArrayList();

    private String summaryText = StringUtils.EMPTY;

    private double scroll = 0;
    private int maxHeight = 0;
    private int posX = 0;

    private int summaryTop = 0;
    private int summaryBottom = 0;
    private int historyTop = 0;
    private int historyBottom = 0;

    /**
     * 在渲染摘要时，缓存的一个变量，些许降低性能占用
     */
    private @Nullable List<String> linesCache = null;

    public HistoryAIChatScreen(EntityMaid maid) {
        this(null, maid);
    }

    public HistoryAIChatScreen(@Nullable Screen parent, EntityMaid maid) {
        super(Component.literal("Maid History AI Chat Screen"));
        this.parent = parent;
        this.maid = maid;
        this.playerSkin = this.getPlayerSkin();
        this.summaryText = maid.getAiChatManager().getCompressedSummary();
        this.transformMessage();
    }

    @Override
    protected void init() {
        this.clearWidgets();
        this.historyWidgets.clear();
        // 刷新时，重置缓存
        this.linesCache = null;

        this.posX = this.width / 2 - 75;

        this.summaryTop = SUMMARY_TOP;
        this.summaryBottom = this.summaryTop + this.getSummaryPanelHeight();
        this.historyTop = SUMMARY_TOP;
        this.historyBottom = this.height - 5;
        this.maxHeight = this.historyTop;

        for (LLMMessage message : this.history) {
            int lineHeight = this.addHistoryWidget(message, posX);
            maxHeight += lineHeight + 5;
        }
        this.addButtons();

        int visibleHeight = this.historyBottom - this.historyTop;
        int contentHeight = Math.max(0, this.maxHeight - this.historyTop);

        // 让滚动一开始就在中间
        if (contentHeight < visibleHeight) {
            this.scroll = (visibleHeight - contentHeight) / 2d;
        } else {
            double topMax = this.historyTop;
            double bottomMax = this.historyBottom;
            double scrollBottom = scroll + maxHeight;
            if (scroll > topMax) {
                scroll = topMax;
            }
            if (bottomMax > scrollBottom) {
                scroll = bottomMax - maxHeight;
            }
        }
    }

    private void addButtons() {
        MutableComponent clearName = Component.translatable("gui.touhou_little_maid.button.maid_ai_chat_config.clear_history_chat");
        MutableComponent clearMsg = Component.translatable("gui.touhou_little_maid.button.maid_ai_chat_config.clear_history_chat.confirm");
        this.addRenderableWidget(new FlatColorButton(this.getRightColumnLeft(), this.getClearButtonY(), SUMMARY_WIDTH, BUTTON_HEIGHT, clearName, button -> {
            this.getMinecraft().setScreen(new ConfirmScreen(yes -> {
                if (yes) {
                    this.history.clear();
                    this.historyWidgets.clear();
                    this.summaryText = StringUtils.EMPTY;
                    this.maid.getAiChatManager().clearAllChatMemory();
                    NetworkHandler.CHANNEL.sendToServer(new ClearMaidAIDataMessage(this.maid.getId()));
                    this.init();
                }
                this.getMinecraft().setScreen(this);
            }, clearName, clearMsg));
        }));
        this.addRenderableWidget(new FlatColorButton(this.getRightColumnLeft(), this.getBackButtonY(), SUMMARY_WIDTH, BUTTON_HEIGHT,
                CommonComponents.GUI_BACK, button -> this.onClose()));
    }

    private int addHistoryWidget(LLMMessage message, int posX) {
        boolean isTool = message.role() == Role.TOOL;
        boolean isLeft = message.role() != Role.USER;

        Component msg = this.getDisplayMessage(message);
        int lineHeight = this.getHistoryLineHeight(msg, isTool);

        // 工具消息
        if (isTool) {
            historyWidgets.add(new HistoryChatWidget(posX - TOOL_TEXT_WIDTH / 2, maxHeight,
                    TOOL_TEXT_WIDTH, lineHeight, msg, playerSkin, message.gameTime(), true, true));
            return lineHeight;
        }

        // 普通聊天消息
        int width = Math.min(font.width(msg), CHAT_TEXT_WIDTH) + 10;
        if (isLeft) {
            historyWidgets.add(new HistoryChatWidget(posX - 100, maxHeight,
                    width, lineHeight, msg, playerSkin, message.gameTime(), true, false));
        } else {
            historyWidgets.add(new HistoryChatWidget(posX + 100 - width, maxHeight,
                    width, lineHeight, msg, playerSkin, message.gameTime(), false, false));
        }
        return lineHeight;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);

        graphics.drawCenteredString(font, HISTORY_TITLE, posX + 210, 8, 0xFFFFFF);
        this.renderSummaryPanel(graphics);

        if (this.historyWidgets.isEmpty()) {
            List<FormattedCharSequence> split = font.split(HISTORY_EMPTY, 150);
            for (int i = 0; i < split.size(); i++) {
                int height = i * font.lineHeight;
                graphics.drawCenteredString(font, split.get(i), posX, this.historyTop + 15 + height, 0xff5555);
            }
        } else {
            graphics.enableScissor(posX - 128, this.historyTop, posX + 128, this.historyBottom);
            graphics.pose().pushPose();
            graphics.pose().translate(0, scroll, 0);
            for (Renderable renderable : this.historyWidgets) {
                renderable.render(graphics, mouseX, mouseY, partialTicks);
            }
            graphics.pose().popPose();
            graphics.disableScissor();
        }
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double delta) {
        if (delta != 0) {
            double topMax = this.historyTop;
            double bottomMax = this.historyBottom;
            double scrollBottom = scroll + maxHeight;
            if (delta < 0 && bottomMax < scrollBottom) {
                scroll += delta * 15;
            }
            if (0 < delta && scroll < topMax) {
                scroll += delta * 15;
            }
        }
        return super.mouseScrolled(pMouseX, pMouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            Screen screen = Objects.requireNonNullElse(this.parent, new AIChatScreen(this.maid));
            this.minecraft.setScreen(screen);
        }
    }

    private void transformMessage() {
        Deque<LLMMessage> deque = this.maid.getAiChatManager().getHistory().getDeque();
        deque.descendingIterator().forEachRemaining(message -> {
            if (message.role() == Role.USER) {
                this.history.add(message);
                return;
            }

            if (message.role() == Role.ASSISTANT) {
                // LLM 发起的工具调用信息，只显示工具名
                List<ToolCall> toolCalls = message.toolCalls();
                if (toolCalls != null && !toolCalls.isEmpty()) {
                    LLMMessage msg = new LLMMessage(Role.TOOL, this.getToolCallNames(toolCalls), message.gameTime());
                    this.history.add(msg);
                    return;
                }

                // 普通的 LLM 返回信息
                String text = message.message();
                if (StringUtils.isNotBlank(text)) {
                    String chatText = new ResponseChat(text).getChatText();
                    if (StringUtils.isNotBlank(chatText)) {
                        LLMMessage msg = new LLMMessage(Role.ASSISTANT, chatText, message.gameTime());
                        this.history.add(msg);
                    }
                }
            }

            // 自身发送给 LLM 的历史记录，不显示在聊天记录中
            // if (message.role() == Role.TOOL) {}
        });
    }

    private String getToolCallNames(List<ToolCall> toolCalls) {
        return toolCalls.stream()
                .map(tool -> tool.getFunction().getName())
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.joining(", "));
    }

    private Component getDisplayMessage(LLMMessage message) {
        if (message.role() == Role.TOOL) {
            if (StringUtils.isBlank(message.message())) {
                return Component.translatable("gui.touhou_little_maid.button.maid_ai_chat_config.history_chat.tool_call.generic");
            }
            return Component.translatable("gui.touhou_little_maid.button.maid_ai_chat_config.history_chat.tool_call.named", message.message());
        }

        // 需要剔除 user 的 context 部分
        if (message.role() == Role.USER) {
            String content = UserPromptContexts.removeContext(message.message());
            return Component.literal(content);
        }
        return Component.literal(message.message());
    }

    private int getHistoryLineHeight(Component message, boolean isTool) {
        if (isTool) {
            int lineCount = font.split(message, TOOL_TEXT_WIDTH).size();
            return lineCount * font.lineHeight / 5;
        } else {
            int lineCount = font.split(message, CHAT_TEXT_WIDTH).size();
            return 10 + lineCount * font.lineHeight;
        }
    }

    private void renderSummaryPanel(GuiGraphics graphics) {
        int left = this.getRightColumnLeft();
        int right = left + SUMMARY_WIDTH;

        graphics.fill(left, this.summaryTop, right, this.summaryBottom, 0xAA111111);
        graphics.fill(left, this.summaryTop, right, this.summaryTop + 1, 0x66FFFFFF);
        graphics.fill(left, this.summaryBottom - 1, right, this.summaryBottom, 0x66FFFFFF);

        graphics.drawCenteredString(font, SUMMARY_TITLE, left + SUMMARY_WIDTH / 2, this.summaryTop + 6, 0xFFFFFF);

        // 依据窗口大小，调整 summary 的显示内容
        if (this.linesCache == null) {
            Component content = StringUtils.isBlank(this.summaryText) ? SUMMARY_EMPTY : Component.literal(this.summaryText);
            this.linesCache = this.getSummaryDisplayLines(content);
        }

        // 渲染缩放字符大小的 summary
        graphics.pose().pushPose();
        graphics.pose().scale(SUMMARY_TEXT_SCALE, SUMMARY_TEXT_SCALE, 1);

        int color = StringUtils.isBlank(this.summaryText) ? 0x999999 : 0xDDDDDD;
        float x = (left + 6) / SUMMARY_TEXT_SCALE;
        float y = (this.summaryTop + 22) / SUMMARY_TEXT_SCALE;
        for (int i = 0; i < this.linesCache.size(); i++) {
            graphics.drawString(font, this.linesCache.get(i), (int) x, (int) (y + i * font.lineHeight), color, false);
        }

        graphics.pose().popPose();
    }

    private int getSummaryPanelHeight() {
        return Math.max(SUMMARY_MIN_HEIGHT, this.getClearButtonY() - SUMMARY_BUTTON_GAP - this.summaryTop);
    }

    private int getRightColumnLeft() {
        return this.posX + 150;
    }

    private int getClearButtonY() {
        return this.height - RIGHT_COLUMN_BOTTOM_MARGIN - BUTTON_HEIGHT * 2 - BUTTON_GAP;
    }

    private int getBackButtonY() {
        return this.height - RIGHT_COLUMN_BOTTOM_MARGIN - BUTTON_HEIGHT;
    }

    private List<String> getSummaryDisplayLines(Component content) {
        int logicalWidth = Math.max(1, (int) ((SUMMARY_WIDTH - 12) / SUMMARY_TEXT_SCALE));
        int maxLines = Math.max(1, (int) ((this.summaryBottom - this.summaryTop - 28) / (font.lineHeight * SUMMARY_TEXT_SCALE)));

        List<String> lines = Lists.newArrayList();
        String[] paragraphs = content.getString().replace("\r", StringUtils.EMPTY).split("\n", -1);

        boolean truncated = false;

        outer:
        for (String paragraph : paragraphs) {
            String remaining = paragraph;
            if (remaining.isEmpty()) {
                if (lines.size() >= maxLines) {
                    truncated = true;
                    break;
                }
                lines.add(StringUtils.EMPTY);
                continue;
            }

            while (!remaining.isEmpty()) {
                if (lines.size() >= maxLines) {
                    truncated = true;
                    break outer;
                }
                String part = font.plainSubstrByWidth(remaining, logicalWidth);
                if (part.isEmpty()) {
                    truncated = true;
                    break outer;
                }
                lines.add(part);
                remaining = remaining.substring(part.length()).trim();
            }
        }

        if (lines.isEmpty()) {
            lines.add(StringUtils.EMPTY);
        }

        if (truncated) {
            int lastIndex = lines.size() - 1;
            String ellipsis = "…";
            String last = lines.get(lastIndex);
            String clipped = font.plainSubstrByWidth(last, Math.max(1, logicalWidth - font.width(ellipsis)));
            lines.set(lastIndex, clipped + ellipsis);
        }
        return lines;
    }

    private ResourceLocation getPlayerSkin() {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) {
            return DefaultPlayerSkin.getDefaultSkin(Util.NIL_UUID);
        }
        return mc.getSkinManager().getInsecureSkinLocation(player.getGameProfile());
    }
}
