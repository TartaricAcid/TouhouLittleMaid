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
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ConfirmOpenLinkScreen;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

public class ModelDownloadGui extends Screen {
    private static final ResourceLocation BG = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/download_background.png");
    private static final String PACK_FILE_SUFFIX = ".zip";
    private final Map<Long, String> crc32Infos = Maps.newHashMap();
    private final List<DownloadInfo> showInfos = Lists.newArrayList();
    private Condition condition = Condition.ALL;
    private TextFieldWidget textField;
    private boolean needReload = false;
    private int selectIndex = -1;
    private int currentPage;
    private int x;
    private int y;

    public ModelDownloadGui() {
        super(new StringTextComponent("New Model Pack Download GUI"));
        this.getCrc32Infos();
        this.checkDownloadInfo();
    }

    @Override
    protected void init() {
        this.getMinecraft().keyboardHandler.setSendRepeatsToGui(true);
        this.buttons.clear();
        this.children.clear();
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
        textField = new TextFieldWidget(getMinecraft().font, x + 273, y + 78, 144, 16, StringTextComponent.EMPTY);
        textField.setTextColor(0xF3EFE0);
        textField.setFocus(focus);
        textField.setValue(textCache);
        textField.moveCursorToEnd();
        this.addWidget(this.textField);
    }

    private void addPackHandleButtons() {
        if (0 <= this.selectIndex && this.selectIndex < this.showInfos.size()) {
            DownloadInfo info = this.showInfos.get(this.selectIndex);
            this.addButton(new FlatColorButton(x + 272, y + 50, 20, 20, StringTextComponent.EMPTY, b -> openPackWebsite(info))
                    .setTooltips("gui.touhou_little_maid.resources_download.open_link"));
            this.addButton(new GuiDownloadButton(x + 294, y + 50, 102, 20, info, b -> {
                if (info.getStatus() == DownloadStatus.NOT_DOWNLOAD) {
                    info.setStatus(DownloadStatus.DOWNLOADING);
                    InfoGetManager.downloadResourcesPack(info);
                    this.init();
                } else if (info.getStatus() == DownloadStatus.NEED_UPDATE) {
                    this.updatePack(info);
                }
            }));
            this.addButton(new FlatColorButton(x + 398, y + 50, 20, 20, StringTextComponent.EMPTY, b -> deletePack(info))
                    .setTooltips("gui.touhou_little_maid.resources_download.delete"));
        }
    }

    private void addPageButtons() {
        this.addButton(new FlatColorButton(x, y + 218, 40, 20, new StringTextComponent("<"), b -> {
            if (this.currentPage > 0) {
                this.currentPage--;
                this.init();
            }
        }));
        this.addButton(new FlatColorButton(x + 228, y + 218, 40, 20, new StringTextComponent(">"), b -> {
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
            this.addButton(button);
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
            Button button = new Button(xPos, y + 3, width, 20, new TranslationTextComponent(nameKey), b -> setCondition(c),
                    (b, matrixStack, x, y) -> this.renderTooltip(matrixStack, new TranslationTextComponent(descKey), x, y));
            if (this.condition.equals(c)) {
                button.active = false;
            }
            this.addButton(button);
            i++;
        }
        this.addButton(new FlatColorButton(x + 400, y + 2, 20, 20, StringTextComponent.EMPTY,
                b -> this.getMinecraft().setScreen(null)).setTooltips("gui.touhou_little_maid.skin.button.close"));
        this.addButton(new Button(x + 270, y + 218, 150, 20,
                new TranslationTextComponent("gui.touhou_little_maid.resources_download.open_folder"),
                b -> Util.getPlatform().openFile(CustomPackLoader.PACK_FOLDER.toFile())));
    }

    private void initShowInfos() {
        this.showInfos.clear();

        switch (this.condition) {
            default:
                this.showInfos.addAll(InfoGetManager.DOWNLOAD_INFO_LIST_ALL);
                break;
            case MAID:
                this.showInfos.addAll(InfoGetManager.getTypedDownloadInfoList(DownloadInfo.TypeEnum.MAID));
                break;
            case CHAIR:
                this.showInfos.addAll(InfoGetManager.getTypedDownloadInfoList(DownloadInfo.TypeEnum.CHAIR));
                break;
            case SOUND:
                this.showInfos.addAll(InfoGetManager.getTypedDownloadInfoList(DownloadInfo.TypeEnum.SOUND));
                break;
            case UPDATE:
                this.showInfos.addAll(InfoGetManager.DOWNLOAD_INFO_LIST_ALL.stream()
                        .filter(info -> info.getStatus() == DownloadStatus.NEED_UPDATE).collect(Collectors.toList()));
                break;
        }

        if (textField != null && StringUtils.isNotBlank(textField.getValue())) {
            String search = this.textField.getValue().toLowerCase(Locale.US);
            this.showInfos.removeIf(info -> !info.getKeyword().contains(search));
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float pPartialTick) {
        this.renderBase(matrixStack);
        this.renderSearchBox(matrixStack, mouseX, mouseY, pPartialTick);
        this.renderPageNumber(matrixStack);
        super.render(matrixStack, mouseX, mouseY, pPartialTick);
        this.renderBaseButtons(matrixStack);
        this.renderPackHandleButtons(matrixStack);
        this.renderNoDataTips(matrixStack);
        this.buttons.stream().filter(b -> b instanceof FlatColorButton).forEach(b -> ((FlatColorButton) b).renderToolTip(matrixStack, this, mouseX, mouseY));
    }

    private void renderNoDataTips(MatrixStack matrixStack) {
        if (!InfoGetManager.DOWNLOAD_INFO_LIST_ALL.isEmpty()) {
            return;
        }
        List<IReorderingProcessor> split = font.split(new TranslationTextComponent("gui.touhou_little_maid.resources_download.fail"), 200);
        int yOffset = y + 100;
        for (IReorderingProcessor sequence : split) {
            font.drawShadow(matrixStack, sequence, (float) (x + 134 - font.width(sequence) / 2), yOffset, TextFormatting.RED.getColor());
            yOffset += 12;
        }
    }

    private void renderPageNumber(MatrixStack matrixStack) {
        int maxPage = (this.showInfos.size() - 1) / 4;
        String pageInfo = String.format("%d/%d", currentPage + 1, maxPage + 1);
        drawString(matrixStack, font, pageInfo, x + 134 - font.width(pageInfo) / 2, y + 227 - font.lineHeight / 2, 0xF3EFE0);
    }

    private void renderPackHandleButtons(MatrixStack matrixStack) {
        if (0 <= this.selectIndex && this.selectIndex < this.showInfos.size()) {
            DownloadInfo info = this.showInfos.get(this.selectIndex);
            drawCenteredString(matrixStack, font, new TranslationTextComponent(info.getName()), x + 345, y + 34, 0xffffff);
            Minecraft.getInstance().getTextureManager().bind(BG);
            blit(matrixStack, x + 400, y + 52, 0, 16, 16, 16);
            blit(matrixStack, x + 274, y + 52, 16, 16, 16, 16);
        }
    }

    private void renderBaseButtons(MatrixStack matrixStack) {
        Minecraft.getInstance().getTextureManager().bind(BG);
        blit(matrixStack, x + 402, y + 4, 32, 16, 16, 16);
    }

    private void renderSearchBox(MatrixStack matrixStack, int pMouseX, int pMouseY, float pPartialTick) {
        drawString(matrixStack, font, new TranslationTextComponent("gui.touhou_little_maid.resources_download.hot_search"), x + 274, y + 102, 0xffffff);
        font.drawWordWrap(new TranslationTextComponent("gui.touhou_little_maid.resources_download.hot_search_key"), x + 274, y + 115, 146, TextFormatting.GRAY.getColor());
        textField.render(matrixStack, pMouseX, pMouseY, pPartialTick);
        if (textField.getValue().isEmpty() && !textField.isFocused()) {
            drawString(matrixStack, font, new TranslationTextComponent("gui.touhou_little_maid.resources_download.search").withStyle(TextFormatting.ITALIC), x + 277, y + 83, 0x777777);
        }
    }

    private void renderBase(MatrixStack matrixStack) {
        fillGradient(matrixStack, 0, 0, this.width, this.height, 0xe2_000000, 0xe2_000000);
        fillGradient(matrixStack, x + 270, y + 26, x + 420, y + 72, 0xff_232221, 0xff_232221);
        fillGradient(matrixStack, x + 270, y + 74, x + 420, y + 216, 0xff_232221, 0xff_232221);
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
        boolean hasKeyCode = InputMappings.getKey(keyCode, scanCode).getNumericKeyValue().isPresent();
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
            this.getMinecraft().gui.setTitles(new TranslationTextComponent("gui.touhou_little_maid.resources_download.need_reload.title"),
                    null, 10, 70, 20);
            this.getMinecraft().gui.setTitles(null, new TranslationTextComponent("gui.touhou_little_maid.resources_download.need_reload.subtitle"),
                    10, 70, 20);
            this.getMinecraft().player.sendMessage(new TranslationTextComponent("gui.touhou_little_maid.resources_download.need_reload.subtitle"), Util.NIL_UUID);
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
            this.getMinecraft().setScreen(new ConfirmOpenLinkScreen(yes -> {
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
                    new TranslationTextComponent("gui.touhou_little_maid.resources_download.delete.confirm"),
                    new TranslationTextComponent(info.getName())));
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

    @Nonnull
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
