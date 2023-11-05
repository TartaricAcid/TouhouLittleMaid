package com.github.tartaricacid.touhoulittlemaid.client.gui.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.download.InfoGetManager;
import com.github.tartaricacid.touhoulittlemaid.client.download.pojo.DownloadInfo;
import com.github.tartaricacid.touhoulittlemaid.client.download.pojo.DownloadStatus;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.ButtonWithId;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.GuiDownloadButton;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.ImageButtonWithId;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import java.util.List;

public class ModelDownloadGui extends Screen {
    private static final ResourceLocation BG = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/download_background.png");
    private static float scroll;
    private static int startIndex;
    private static int index;
    private static int currentPage;
    private List<Long> crc32List = Lists.newArrayList();
    private List<DownloadInfo> showInfoList;

    public ModelDownloadGui(@Nullable List<DownloadInfo> showInfoList) {
        super(new StringTextComponent("Model Pack Download GUI"));
        this.showInfoList = showInfoList;
        if (showInfoList != null && !showInfoList.isEmpty()) {
            getCrc32Info();
            checkDownloadInfo();
            calculationStartIndex();
        }
    }

    public static int getCurrentPage() {
        return currentPage;
    }

    @Override
    protected void init() {
        this.buttons.clear();
        this.children.clear();

        int startX = this.width / 2 - 194;
        int startY = this.height / 2 - 79;

        if (showInfoList == null || showInfoList.isEmpty()) {
            addButton(new Button(5, 5, 60, 20, new TranslationTextComponent("gui.touhou_little_maid.resources_download.reload"), (b) -> {
            }));
            return;
        }

        for (int i = startIndex; i < 9 + startIndex; i++) {
            if (i < showInfoList.size()) {
                DownloadInfo info = showInfoList.get(i);
                ButtonWithId button = new ButtonWithId(i, startX, startY + 21 * (i - startIndex), 171, 20, new TranslationTextComponent(info.getName()), (id) -> {
                    index = id;
                    this.init();
                });
                if (i == index) {
                    button.active = false;
                }
                addButton(button);
            }
        }

        addButton(new GuiDownloadButton(startX + 184, startY + 165, 200, 20, showInfoList.get(index)));
        for (int i = 0; i < 4; i++) {
            addButton(new ImageButtonWithId(i, startX - 1 + (28 * i),
                    startY - 30, 25, 25, 480, 0, 0, BG, (b) -> {
                int pageIndex = ((ImageButtonWithId) b).getIndex();
                if (minecraft != null && currentPage != pageIndex) {
                    currentPage = pageIndex;
                    startIndex = 0;
                    scroll = 0;
                    DownloadInfo.TypeEnum typeEnum = DownloadInfo.TypeEnum.getTypeByIndex(pageIndex - 1);
                    List<DownloadInfo> downloadInfos = InfoGetManager.getTypedDownloadInfoList(typeEnum);
                    minecraft.setScreen(new ModelDownloadGui(downloadInfos));
                }
            }));
        }

        this.addButton(new ImageButton(startX + 194 - 21, startY + 1, 8, 10, 128, 224, 10, BG, 484, 256, b -> {
            this.mouseScrolled(0, 0, 1);
        }));
        this.addButton(new ImageButton(startX + 194 - 21, startY + 2 + 175, 8, 10, 136, 224, 10, BG, 484, 256, b -> {
            this.mouseScrolled(0, 0, -1);
        }));

        addButton(new Button(startX + 287, startY - 30, 100, 20, new TranslationTextComponent("spectatorMenu.close"), (b) -> {
            if (minecraft != null) {
                minecraft.setScreen(null);
            }
        }));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        int middleX = this.width / 2;
        int middleY = this.height / 2;

        renderBackground(matrixStack);
        if (minecraft == null || showInfoList == null || showInfoList.isEmpty()) {
            drawCenteredString(matrixStack, font, new TranslationTextComponent("gui.touhou_little_maid.resources_download.fail.1"), this.width / 2, this.height / 2 - 15, 0xfffff);
            drawCenteredString(matrixStack, font, new TranslationTextComponent("gui.touhou_little_maid.resources_download.fail.2"), this.width / 2, this.height / 2, 0xfffff);
        } else {
            minecraft.getTextureManager().bind(BG);
            blit(matrixStack, middleX - 200, middleY - 85, 0, 0, 400, 200, 484, 256);
            blit(matrixStack, middleX - 19, middleY - 66 + (int) (147 * scroll), 400, 0, 4, 15, 484, 256);
            for (int i = 0; i < 4; i++) {
                blit(matrixStack, middleX - 196 + (28 * i), middleY - 110, 456, 194, 28, 25, 484, 256);
            }
            blit(matrixStack, middleX - 196 + (28 * currentPage), middleY - 113, 456, 224, 28, 32, 484, 256);
            for (int i = 0; i < 4; i++) {
                blit(matrixStack, middleX - 192 + (28 * i), middleY - 106, 20, 20, i * 32, 224, 32, 32, 484, 256);
            }
            addResInfo(matrixStack, showInfoList.get(index));
        }
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        boolean inRangeX = (middleX - 196) <= mouseX && mouseX < (middleX - 196 + 28 * 4);
        boolean inRangeY = (middleY - 110) <= mouseY && mouseY < (middleY - 110 + 25);
        if (inRangeX && inRangeY) {
            int index = (mouseX - (middleX - 196)) / 28;
            renderTooltip(matrixStack, new TranslationTextComponent("gui.touhou_little_maid.resources_download.tab." + index), mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (delta != 0) {
            if (showInfoList.size() <= 9) {
                return true;
            }
            float num = delta < 0 ? 1.0f : -1.0f;
            scroll = MathHelper.clamp(scroll + num / (showInfoList.size() - 9), 0, 1);
            startIndex = startIndex + (int) num;
            if (startIndex < 0) {
                startIndex = 0;
            }
            if (startIndex > showInfoList.size() - 9) {
                startIndex = showInfoList.size() - 9;
            }
            this.init();
            return true;
        }
        return false;
    }

    private void addResInfo(MatrixStack matrixStack, DownloadInfo info) {
        int startX = this.width / 2 - 7;
        int startY = this.height / 2 - 73;

        TranslationTextComponent name = new TranslationTextComponent(info.getName());
        drawString(matrixStack, font, name, startX, startY, 0xFFAA00);
        drawString(matrixStack, font, info.getVersion(), startX + font.width(name) + 4, startY, 0x55FF55);
        drawString(matrixStack, font, getI18nFormatFileSize(info.getFormatFileSize()), startX, startY + 12, 0x3A8FB7);
        drawString(matrixStack, font, getI18nFormatFileTime(info.getFormatData()), startX, startY + 24, 0x55FFFF);
        drawString(matrixStack, font, getI18nFormatAuthor(info.getAuthor()), startX, startY + 36, 0x00896C);
        drawString(matrixStack, font, getI18nFormatLicense(info.getLicense()), startX, startY + 48, 0xF7D94C);
        font.drawWordWrap(new TranslationTextComponent(info.getDesc()), startX, startY + 72, 195, 0x7a7a7a);
    }

    private void getCrc32Info() {
        try {
            Files.walkFileTree(CustomPackLoader.PACK_FOLDER, EnumSet.noneOf(FileVisitOption.class), 1, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.getFileName().toString().endsWith(".zip")) {
                        crc32List.add(FileUtils.checksumCRC32(file.toFile()));
                    }
                    return super.visitFile(file, attrs);
                }
            });
            Files.walkFileTree(Minecraft.getInstance().gameDirectory.toPath().resolve("resourcepacks"), EnumSet.noneOf(FileVisitOption.class), 1, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.getFileName().toString().endsWith(".zip")) {
                        crc32List.add(FileUtils.checksumCRC32(file.toFile()));
                    }
                    return super.visitFile(file, attrs);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkDownloadInfo() {
        for (DownloadInfo info : showInfoList) {
            if (info.getStatus() == DownloadStatus.DOWNLOADING) {
                continue;
            }
            info.setStatus(DownloadStatus.NOT_DOWNLOAD);
            for (Long crc32 : crc32List) {
                if (crc32.equals(info.getChecksum())) {
                    info.setStatus(DownloadStatus.DOWNLOADED);
                    break;
                }
            }
        }
    }

    private void calculationStartIndex() {
        int size = showInfoList.size();
        index = MathHelper.clamp(index, 0, size - 1);
        if (size - 9 > 0 && startIndex > size - 9) {
            startIndex = size - 9;
        }
    }

    private String getI18nFormatAuthor(List<String> authors) {
        String str = String.join(I18n.get("gui.touhou_little_maid.resources_download.author.delimiter"), authors);
        return I18n.get("gui.touhou_little_maid.resources_download.author", str);
    }

    private String getI18nFormatFileSize(String size) {
        return I18n.get("gui.touhou_little_maid.resources_download.file_size", size);
    }

    private String getI18nFormatFileTime(String time) {
        return I18n.get("gui.touhou_little_maid.resources_download.upload_time", time);
    }

    private String getI18nFormatLicense(String license) {
        return I18n.get("gui.touhou_little_maid.resources_download.license", license);
    }
}
