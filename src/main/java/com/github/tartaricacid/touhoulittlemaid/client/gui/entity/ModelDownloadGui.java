package com.github.tartaricacid.touhoulittlemaid.client.gui.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.download.InfoGetManager;
import com.github.tartaricacid.touhoulittlemaid.client.download.pojo.DownloadInfo;
import com.github.tartaricacid.touhoulittlemaid.client.download.pojo.DownloadStatus;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.FlatColorButton;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.PackInfoButton;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ModelDownloadGui extends Screen {
    private static final ResourceLocation BG = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/download_background.png");
    private static final String PACK_FILE_SUFFIX = ".zip";
    private final List<Long> crc32Infos = Lists.newArrayList();
    private final List<DownloadInfo> showInfos = Lists.newArrayList();
    private int currentPage;
    private Condition condition = Condition.ALL;
    private int selectIndex = -1;
    private int x;
    private int y;
    private EditBox textField;

    public ModelDownloadGui() {
        super(Component.literal("New Model Pack Download GUI"));
        this.getCrc32Infos();
        this.checkDownloadInfo();
    }

    @Override
    protected void init() {
        this.clearWidgets();
        this.x = (width - 420) / 2;
        this.y = (height - 235) / 2;
        this.initShowInfos();
        this.addBaseButtons();
        this.addPackButtons();
        this.addPageButtons();
        this.addPackHandleButtons();
        this.addSearchBox();
    }

    private void addSearchBox() {
        String textCache = textField == null ? "" : textField.getValue();
        boolean focus = textField != null && textField.isFocused();
        textField = new EditBox(getMinecraft().font, x + 273, y + 78, 144, 16, Component.empty());
        textField.setTextColor(0xF3EFE0);
        textField.setFocused(focus);
        textField.setValue(textCache);
        textField.moveCursorToEnd();
        this.addWidget(this.textField);
    }

    private void addPackHandleButtons() {
        if (0 <= this.selectIndex && this.selectIndex < this.showInfos.size()) {
            DownloadInfo info = this.showInfos.get(this.selectIndex);
            this.addRenderableWidget(new FlatColorButton(x + 272, y + 50, 20, 20, Component.empty(), b -> {
            }).setTooltips("gui.touhou_little_maid.resources_download.open_link"));
            this.addRenderableWidget(new FlatColorButton(x + 294, y + 50, 102, 20, getStatueText(info), b -> {
            }));
            this.addRenderableWidget(new FlatColorButton(x + 398, y + 50, 20, 20, Component.empty(), b -> {
            }).setTooltips("gui.touhou_little_maid.resources_download.delete"));
        }
    }

    private void addPageButtons() {
        this.addRenderableWidget(new FlatColorButton(x, y + 218, 40, 17, Component.literal("<"), b -> {
            if (this.currentPage > 0) {
                this.currentPage--;
                this.init();
            }
        }));
        this.addRenderableWidget(new FlatColorButton(x + 228, y + 218, 40, 17, Component.literal(">"), b -> {
            if ((this.currentPage * 4 + 4) <= this.showInfos.size()) {
                this.currentPage++;
                this.init();
            }
        }));
    }

    private void addPackButtons() {
        int startIndex = currentPage * 4;
        for (int i = startIndex; i < startIndex + 4; i++) {
            if (i >= this.showInfos.size()) {
                break;
            }
            DownloadInfo info = this.showInfos.get(i);
            int yOffset = y + 26 + (i - startIndex) * 48;
            final int tmp = i;
            PackInfoButton button = new PackInfoButton(x, yOffset, info, b -> {
                this.selectIndex = tmp;
                this.init();
            });
            if (this.selectIndex == i) {
                button.setSelect(true);
            }
            this.addRenderableWidget(button);
        }
    }

    private void addBaseButtons() {
        this.addRenderableWidget(new FlatColorButton(x + 69, y + 2, 20, 20, Component.empty(), b -> setCondition(Condition.ALL))
                .setTooltips("gui.touhou_little_maid.resources_download.all"));
        this.addRenderableWidget(new FlatColorButton(x + 91, y + 2, 20, 20, Component.empty(), b -> setCondition(Condition.MAID))
                .setTooltips("gui.touhou_little_maid.resources_download.maid"));
        this.addRenderableWidget(new FlatColorButton(x + 113, y + 2, 20, 20, Component.empty(), b -> setCondition(Condition.CHAIR))
                .setTooltips("gui.touhou_little_maid.resources_download.chair"));
        this.addRenderableWidget(new FlatColorButton(x + 135, y + 2, 20, 20, Component.empty(), b -> setCondition(Condition.SOUND))
                .setTooltips("gui.touhou_little_maid.resources_download.sound"));
        this.addRenderableWidget(new FlatColorButton(x + 157, y + 2, 20, 20, Component.empty(), b -> setCondition(Condition.DOWNLOADED))
                .setTooltips("gui.touhou_little_maid.resources_download.downloaded"));
        this.addRenderableWidget(new FlatColorButton(x + 179, y + 2, 20, 20, Component.empty(), b -> setCondition(Condition.NOT_DOWNLOAD))
                .setTooltips("gui.touhou_little_maid.resources_download.not_download"));
        this.addRenderableWidget(new FlatColorButton(x + 400, y + 2, 20, 20, Component.empty(), b -> this.getMinecraft().setScreen(null))
                .setTooltips("gui.touhou_little_maid.skin.button.close"));
        this.addRenderableWidget(new FlatColorButton(x + 270, y + 218, 150, 17, Component.translatable("gui.touhou_little_maid.resources_download.open_folder"),
                b -> Util.getPlatform().openFile(CustomPackLoader.PACK_FOLDER.toFile())));
    }

    private void initShowInfos() {
        this.showInfos.clear();

        switch (this.condition) {
            default -> this.showInfos.addAll(InfoGetManager.DOWNLOAD_INFO_LIST_ALL);
            case MAID -> this.showInfos.addAll(InfoGetManager.getTypedDownloadInfoList(DownloadInfo.TypeEnum.MAID));
            case CHAIR -> this.showInfos.addAll(InfoGetManager.getTypedDownloadInfoList(DownloadInfo.TypeEnum.CHAIR));
            case SOUND -> this.showInfos.addAll(InfoGetManager.getTypedDownloadInfoList(DownloadInfo.TypeEnum.SOUND));
            case DOWNLOADED ->
                    this.showInfos.addAll(InfoGetManager.DOWNLOAD_INFO_LIST_ALL.stream().filter(info -> info.getStatus() == DownloadStatus.DOWNLOADED).toList());
            case NOT_DOWNLOAD ->
                    this.showInfos.addAll(InfoGetManager.DOWNLOAD_INFO_LIST_ALL.stream().filter(info -> info.getStatus() != DownloadStatus.DOWNLOADED).toList());
        }

        if (textField != null && StringUtils.isNotBlank(textField.getValue())) {
            String search = this.textField.getValue().toLowerCase(Locale.US);
            this.showInfos.removeIf(info -> info.getKeyWord().stream().noneMatch(keyword -> keyword.contains(search)));
        }
    }

    @NotNull
    private static MutableComponent getStatueText(DownloadInfo info) {
        MutableComponent text;
        switch (info.getStatus()) {
            default -> text = Component.translatable("gui.touhou_little_maid.resources_download.not_download");
            case DOWNLOADED -> text = Component.translatable("gui.touhou_little_maid.resources_download.downloaded");
            case DOWNLOADING -> text = Component.translatable("gui.touhou_little_maid.resources_download.downloading");
            case NEED_UPDATE -> text = Component.translatable("gui.touhou_little_maid.resources_download.need_update");
        }
        return text;
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBase(graphics);
        this.renderSearchBox(graphics, pMouseX, pMouseY, pPartialTick);
        this.renderPageNumber(graphics);
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
        this.renderBaseButtons(graphics);
        this.renderPackHandleButtons(graphics);
        this.renderables.stream().filter(b -> b instanceof FlatColorButton).forEach(b -> ((FlatColorButton) b).renderToolTip(graphics, this, pMouseX, pMouseY));
    }

    private void renderPageNumber(GuiGraphics graphics) {
        int maxPage = (this.showInfos.size() - 1) / 4;
        String pageInfo = String.format("%d/%d", currentPage + 1, maxPage + 1);
        graphics.drawString(font, pageInfo, x + 134 - font.width(pageInfo) / 2, y + 227 - font.lineHeight / 2, 0xF3EFE0);
    }

    private void renderPackHandleButtons(GuiGraphics graphics) {
        if (0 <= this.selectIndex && this.selectIndex < this.showInfos.size()) {
            DownloadInfo info = this.showInfos.get(this.selectIndex);
            graphics.drawCenteredString(font, Component.translatable(info.getName()), x + 345, y + 34, ChatFormatting.WHITE.getColor());
            graphics.blit(BG, x + 400, y + 52, 0, 16, 16, 16);
            graphics.blit(BG, x + 274, y + 52, 16, 16, 16, 16);
        }
    }

    private void renderBaseButtons(GuiGraphics graphics) {
        graphics.blit(BG, x + 69 + 2, y + 4, 0, 0, 16, 16);
        graphics.blit(BG, x + 91 + 2, y + 4, 16, 0, 16, 16);
        graphics.blit(BG, x + 113 + 2, y + 4, 32, 0, 16, 16);
        graphics.blit(BG, x + 135 + 2, y + 4, 48, 0, 16, 16);
        graphics.blit(BG, x + 157 + 2, y + 4, 64, 0, 16, 16);
        graphics.blit(BG, x + 179 + 2, y + 4, 80, 0, 16, 16);
        graphics.blit(BG, x + 402, y + 4, 32, 16, 16, 16);
    }

    private void renderSearchBox(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        graphics.drawString(font, Component.translatable("gui.touhou_little_maid.resources_download.hot_search"), x + 274, y + 102, ChatFormatting.WHITE.getColor());
        textField.render(graphics, pMouseX, pMouseY, pPartialTick);
        if (textField.getValue().isEmpty() && !textField.isFocused()) {
            graphics.drawString(font, Component.translatable("gui.touhou_little_maid.resources_download.search").withStyle(ChatFormatting.ITALIC), x + 277, y + 83, 0x777777);
        }
    }

    private void renderBase(GuiGraphics graphics) {
        graphics.fillGradient(0, 0, this.width, this.height, 0xe2_000000, 0xe2_000000);
        graphics.fillGradient(x + 270, y + 26, x + 420, y + 72, 0xff_232221, 0xff_232221);
        graphics.fillGradient(x + 270, y + 74, x + 420, y + 216, 0xff_232221, 0xff_232221);
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        String value = this.textField.getValue();
        super.resize(minecraft, width, height);
        this.textField.setValue(value);
    }

    @Override
    public void tick() {
        this.textField.tick();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.textField.mouseClicked(mouseX, mouseY, button)) {
            this.setFocused(this.textField);
            return true;
        } else if (this.textField.isFocused()) {
            this.textField.setFocused(false);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (textField == null) {
            return false;
        }
        String perText = this.textField.getValue();
        if (this.textField.charTyped(codePoint, modifiers)) {
            if (!Objects.equals(perText, this.textField.getValue())) {
                this.currentPage = 0;
                this.init();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean hasKeyCode = InputConstants.getKey(keyCode, scanCode).getNumericKeyValue().isPresent();
        String preText = this.textField.getValue();
        if (hasKeyCode) {
            return true;
        }
        if (this.textField.keyPressed(keyCode, scanCode, modifiers)) {
            if (!Objects.equals(preText, this.textField.getValue())) {
                this.currentPage = 0;
                this.init();
            }
            return true;
        } else {
            return this.textField.isFocused() && this.textField.isVisible() && keyCode != 256 || super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    @Override
    protected void insertText(String text, boolean overwrite) {
        if (overwrite) {
            this.textField.setValue(text);
        } else {
            this.textField.insertText(text);
        }
    }

    private void getCrc32Infos() {
        try {
            Files.walkFileTree(CustomPackLoader.PACK_FOLDER, EnumSet.noneOf(FileVisitOption.class), 1, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.getFileName().toString().endsWith(PACK_FILE_SUFFIX)) {
                        crc32Infos.add(FileUtils.checksumCRC32(file.toFile()));
                    }
                    return super.visitFile(file, attrs);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkDownloadInfo() {
        for (DownloadInfo info : InfoGetManager.DOWNLOAD_INFO_LIST_ALL) {
            if (info.getStatus() == DownloadStatus.DOWNLOADING) {
                continue;
            }
            info.setStatus(DownloadStatus.NOT_DOWNLOAD);
            for (Long crc32 : this.crc32Infos) {
                if (crc32.equals(info.getChecksum())) {
                    info.setStatus(DownloadStatus.DOWNLOADED);
                    break;
                }
            }
        }
    }

    private void setCondition(Condition condition) {
        if (this.condition != condition) {
            this.condition = condition;
            this.currentPage = 0;
            this.init();
        }
    }

    private enum Condition {
        ALL, MAID, CHAIR, SOUND, DOWNLOADED, NOT_DOWNLOAD
    }
}
