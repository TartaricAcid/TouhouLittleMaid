package com.github.tartaricacid.touhoulittlemaid.client.gui.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.download.InfoGetManager;
import com.github.tartaricacid.touhoulittlemaid.client.download.pojo.DownloadInfo;
import com.github.tartaricacid.touhoulittlemaid.client.download.pojo.DownloadStatus;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.FlatColorButton;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.GuiDownloadButton;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.PackInfoButton;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class ModelDownloadGui extends Screen {
    private static final ResourceLocation BG = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/download_background.png");
    private static final String PACK_FILE_SUFFIX = ".zip";
    private final Map<Long, String> crc32Infos = Maps.newHashMap();
    private final List<DownloadInfo> showInfos = Lists.newArrayList();
    private Condition condition = Condition.ALL;
    private EditBox textField;
    private boolean needReload = false;
    private int selectIndex = -1;
    private int currentPage;
    private int x;
    private int y;

    public ModelDownloadGui() {
        super(Component.literal("New Model Pack Download GUI"));
        this.getCrc32Infos();
        this.checkDownloadInfo();
    }

    @Override
    protected void init() {
        this.clearWidgets();
        this.x = (width - 420) / 2;
        this.y = (height - 240) / 2;
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
        textField.setFocus(focus);
        textField.setValue(textCache);
        textField.moveCursorToEnd();
        this.addWidget(this.textField);
    }

    private void addPackHandleButtons() {
        if (0 <= this.selectIndex && this.selectIndex < this.showInfos.size()) {
            DownloadInfo info = this.showInfos.get(this.selectIndex);
            this.addRenderableWidget(new FlatColorButton(x + 272, y + 50, 20, 20, Component.empty(), b -> openPackWebsite(info))
                    .setTooltips("gui.touhou_little_maid.resources_download.open_link"));
            this.addRenderableWidget(new GuiDownloadButton(x + 294, y + 50, 102, 20, info, b -> {
                if (info.getStatus() == DownloadStatus.NOT_DOWNLOAD) {
                    info.setStatus(DownloadStatus.DOWNLOADING);
                    InfoGetManager.downloadResourcesPack(info);
                    this.init();
                } else if (info.getStatus() == DownloadStatus.NEED_UPDATE) {
                    this.updatePack(info);
                }
            }));
            this.addRenderableWidget(new FlatColorButton(x + 398, y + 50, 20, 20, Component.empty(), b -> deletePack(info))
                    .setTooltips("gui.touhou_little_maid.resources_download.delete"));
        }
    }

    private void addPageButtons() {
        this.addRenderableWidget(new FlatColorButton(x, y + 218, 40, 20, Component.literal("<"), b -> {
            if (this.currentPage > 0) {
                this.currentPage--;
                this.init();
            }
        }));
        this.addRenderableWidget(new FlatColorButton(x + 228, y + 218, 40, 20, Component.literal(">"), b -> {
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
        int i = 0;
        for (Condition c : Condition.values()) {
            int width = 52;
            int xPos = x + (width + 2) * i;
            String key = c.name().toLowerCase(Locale.US);
            String nameKey = "gui.touhou_little_maid.resources_download." + key;
            String descKey = nameKey + ".tips";
            Button button = new Button(xPos, y + 3, width, 20, Component.translatable(nameKey), b -> setCondition(c),
                    (b, matrixStack, x, y) -> this.renderTooltip(matrixStack, Component.translatable(descKey), x, y));
            if (this.condition.equals(c)) {
                button.active = false;
            }
            this.addRenderableWidget(button);
            i++;
        }
        this.addRenderableWidget(new FlatColorButton(x + 400, y + 2, 20, 20, Component.empty(),
                b -> this.getMinecraft().setScreen(null)).setTooltips("gui.touhou_little_maid.skin.button.close"));
        this.addRenderableWidget(new Button(x + 270, y + 218, 150, 20,
                Component.translatable("gui.touhou_little_maid.resources_download.open_folder"),
                b -> Util.getPlatform().openFile(CustomPackLoader.PACK_FOLDER.toFile())));
    }

    private void initShowInfos() {
        this.showInfos.clear();

        switch (this.condition) {
            default -> this.showInfos.addAll(InfoGetManager.DOWNLOAD_INFO_LIST_ALL);
            case MAID -> this.showInfos.addAll(InfoGetManager.getTypedDownloadInfoList(DownloadInfo.TypeEnum.MAID));
            case CHAIR -> this.showInfos.addAll(InfoGetManager.getTypedDownloadInfoList(DownloadInfo.TypeEnum.CHAIR));
            case SOUND -> this.showInfos.addAll(InfoGetManager.getTypedDownloadInfoList(DownloadInfo.TypeEnum.SOUND));
            case UPDATE -> this.showInfos.addAll(InfoGetManager.DOWNLOAD_INFO_LIST_ALL.stream()
                    .filter(info -> info.getStatus() == DownloadStatus.NEED_UPDATE).toList());
        }

        if (textField != null && StringUtils.isNotBlank(textField.getValue())) {
            String search = this.textField.getValue().toLowerCase(Locale.US);
            this.showInfos.removeIf(info -> !info.getKeyword().contains(search));
        }
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float pPartialTick) {
        this.renderBase(poseStack);
        this.renderSearchBox(poseStack, mouseX, mouseY, pPartialTick);
        this.renderPageNumber(poseStack);
        super.render(poseStack, mouseX, mouseY, pPartialTick);
        this.renderBaseButtons(poseStack);
        this.renderPackHandleButtons(poseStack);
        this.renderNoDataTips(poseStack);
        this.renderables.stream().filter(b -> b instanceof FlatColorButton).forEach(b -> ((FlatColorButton) b).renderToolTip(poseStack, this, mouseX, mouseY));
    }

    private void renderNoDataTips(PoseStack poseStack) {
        if (!InfoGetManager.DOWNLOAD_INFO_LIST_ALL.isEmpty()) {
            return;
        }
        List<FormattedCharSequence> split = font.split(Component.translatable("gui.touhou_little_maid.resources_download.fail"), 200);
        int yOffset = y + 100;
        for (FormattedCharSequence sequence : split) {
            drawCenteredString(poseStack, font, sequence, x + 134, yOffset, ChatFormatting.RED.getColor());
            yOffset += 12;
        }
    }

    private void renderPageNumber(PoseStack poseStack) {
        int maxPage = (this.showInfos.size() - 1) / 4;
        String pageInfo = String.format("%d/%d", currentPage + 1, maxPage + 1);
        drawString(poseStack, font, pageInfo, x + 134 - font.width(pageInfo) / 2, y + 227 - font.lineHeight / 2, 0xF3EFE0);
    }

    private void renderPackHandleButtons(PoseStack poseStack) {
        if (0 <= this.selectIndex && this.selectIndex < this.showInfos.size()) {
            DownloadInfo info = this.showInfos.get(this.selectIndex);
            drawCenteredString(poseStack, font, Component.translatable(info.getName()), x + 345, y + 34, 0xffffff);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, BG);
            blit(poseStack, x + 400, y + 52, 0, 16, 16, 16);
            blit(poseStack, x + 274, y + 52, 16, 16, 16, 16);
        }
    }

    private void renderBaseButtons(PoseStack poseStack) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BG);
        blit(poseStack, x + 402, y + 4, 32, 16, 16, 16);
    }

    private void renderSearchBox(PoseStack poseStack, int pMouseX, int pMouseY, float pPartialTick) {
        drawString(poseStack, font, Component.translatable("gui.touhou_little_maid.resources_download.hot_search"), x + 274, y + 102, 0xffffff);
        font.drawWordWrap(Component.translatable("gui.touhou_little_maid.resources_download.hot_search_key"), x + 274, y + 115, 146, ChatFormatting.GRAY.getColor());
        textField.render(poseStack, pMouseX, pMouseY, pPartialTick);
        if (textField.getValue().isEmpty() && !textField.isFocused()) {
            drawString(poseStack, font, Component.translatable("gui.touhou_little_maid.resources_download.search").withStyle(ChatFormatting.ITALIC), x + 277, y + 83, 0x777777);
        }
    }

    private void renderBase(PoseStack poseStack) {
        fillGradient(poseStack, 0, 0, this.width, this.height, 0xe2_000000, 0xe2_000000);
        fillGradient(poseStack, x + 270, y + 26, x + 420, y + 72, 0xff_232221, 0xff_232221);
        fillGradient(poseStack, x + 270, y + 74, x + 420, y + 216, 0xff_232221, 0xff_232221);
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
            this.textField.setFocus(false);
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

    @Override
    public void onClose() {
        if (this.needReload && this.getMinecraft().player != null) {
            this.getMinecraft().gui.setTitle(Component.translatable("gui.touhou_little_maid.resources_download.need_reload.title"));
            this.getMinecraft().gui.setSubtitle(Component.translatable("gui.touhou_little_maid.resources_download.need_reload.subtitle"));
            this.getMinecraft().player.sendSystemMessage(Component.translatable("gui.touhou_little_maid.resources_download.need_reload.subtitle"));
        }
        super.onClose();
    }

    private void getCrc32Infos() {
        this.crc32Infos.clear();
        try {
            Files.walkFileTree(CustomPackLoader.PACK_FOLDER, EnumSet.noneOf(FileVisitOption.class), 1, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.getFileName().toString().endsWith(PACK_FILE_SUFFIX)) {
                        crc32Infos.put(FileUtils.checksumCRC32(file.toFile()), file.toFile().getName());
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
            if (info.getStatus() != DownloadStatus.DOWNLOADING) {
                info.setStatus(DownloadStatus.NOT_DOWNLOAD);
            }
            for (Long crc32 : this.crc32Infos.keySet()) {
                if (crc32.equals(info.getChecksum())) {
                    info.setStatus(DownloadStatus.DOWNLOADED);
                    break;
                }
                if (info.getOldVersion().contains(crc32)) {
                    info.setStatus(DownloadStatus.NEED_UPDATE);
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

    private void openPackWebsite(DownloadInfo info) {
        String website = info.getWebsite();
        if (StringUtils.isNotBlank(website)) {
            this.getMinecraft().setScreen(new ConfirmLinkScreen(yes -> {
                if (yes) {
                    Util.getPlatform().openUri(website);
                }
                this.getMinecraft().setScreen(this);
            }, website, false));
        }
    }

    private void deletePack(DownloadInfo info) {
        Set<String> deleteFiles = this.getDeleteFiles(info);
        if (info.getStatus() == DownloadStatus.DOWNLOADED || info.getStatus() == DownloadStatus.NEED_UPDATE) {
            this.getMinecraft().setScreen(new ConfirmScreen(yes -> this.deleteFilesAndReload(yes, deleteFiles),
                    Component.translatable("gui.touhou_little_maid.resources_download.delete.confirm"),
                    Component.translatable(info.getName())));
        }
    }

    private void updatePack(DownloadInfo info) {
        Set<String> deleteFiles = this.getDeleteFiles(info);
        this.deleteFiles(deleteFiles);
        info.setStatus(DownloadStatus.DOWNLOADING);
        InfoGetManager.downloadResourcesPack(info);
        this.needReload = true;
        this.getCrc32Infos();
        this.checkDownloadInfo();
        this.init();
    }

    @NotNull
    private Set<String> getDeleteFiles(DownloadInfo info) {
        Set<String> deleteFiles = Sets.newHashSet();
        deleteFiles.add(info.getFileName());
        info.getOldVersion().forEach(version -> {
            if (crc32Infos.containsKey(version)) {
                deleteFiles.add(crc32Infos.get(version));
            }
        });
        return deleteFiles;
    }

    private void deleteFiles(Set<String> deleteFiles) {
        for (String fileName : deleteFiles) {
            try {
                Path file = CustomPackLoader.PACK_FOLDER.resolve(fileName);
                if (Files.isRegularFile(file)) {
                    Files.delete(file);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteFilesAndReload(boolean yes, Set<String> deleteFiles) {
        if (yes) {
            this.deleteFiles(deleteFiles);
            this.needReload = true;
            this.getCrc32Infos();
            this.checkDownloadInfo();
            this.init();
        }
        this.getMinecraft().setScreen(this);
    }

    public enum Condition {
        ALL, MAID, CHAIR, SOUND, UPDATE
    }
}
