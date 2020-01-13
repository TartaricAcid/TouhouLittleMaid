package com.github.tartaricacid.touhoulittlemaid.client.gui;

import com.github.tartaricacid.touhoulittlemaid.client.download.InfoGetManager;
import com.github.tartaricacid.touhoulittlemaid.client.download.pojo.DownloadInfo;
import com.github.tartaricacid.touhoulittlemaid.client.download.pojo.DownloadStatus;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import java.util.List;

/**
 * @author TartaricAcid
 * @date 2020/1/12 15:35
 **/
@SideOnly(Side.CLIENT)
public class ResourcesDownloadGui extends GuiScreen {
    private static int SCROLL;
    private List<Long> crc32List = Lists.newArrayList();

    public ResourcesDownloadGui() {
        getCrc32Info();
        checkDownloadInfo();
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        for (int i = 0; i < InfoGetManager.DOWNLOAD_INFO_LIST.size(); i++) {
            DownloadInfo info = InfoGetManager.DOWNLOAD_INFO_LIST.get(i);
            addButton(new GuiDownloadButton(i, this.width - 80, i * 75 + 60 - SCROLL, 60, 20, info));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        for (int i = 0; i < InfoGetManager.DOWNLOAD_INFO_LIST.size(); i++) {
            addResInfo(i, InfoGetManager.DOWNLOAD_INFO_LIST.get(i));
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void addResInfo(int index, DownloadInfo info) {
        drawGradientRect(10, index * 75 + 20 - SCROLL, this.width - 10, index * 75 + 90 - SCROLL, 0xf0101010, 0xf0101010);
        drawString(fontRenderer, info.getName(), 15, index * 75 + 25 - SCROLL, 0xFFAA00);
        drawString(fontRenderer, info.getVersion(), 20 + fontRenderer.getStringWidth(info.getName()), index * 75 + 25 - SCROLL, 0x55FF55);
        drawString(fontRenderer, getI18nFormatAuthor(info.getAuthor()), 15, index * 75 + 38 - SCROLL, 0x55FFFF);
        fontRenderer.drawSplitString(info.getDesc(), 15, index * 75 + 51 - SCROLL, this.width - 100, 0x6a6a6a);
        String fileSize = getI18nFormatFileSize(info.getFormatFileSize());
        drawString(fontRenderer, fileSize, this.width - fontRenderer.getStringWidth(fileSize) - 20, index * 75 + 45 - SCROLL, 0x6a6a6a);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id >= 0 && button.id < InfoGetManager.DOWNLOAD_INFO_LIST.size()) {
            DownloadInfo info = InfoGetManager.DOWNLOAD_INFO_LIST.get(button.id);
            if (DownloadStatus.canDownload(info.getStatus())) {
                InfoGetManager.downloadResourcesPack(info);
            }
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        // 鼠标滚轮改变大小
        if (Mouse.getEventDWheel() != 0) {
            // 小于四个就不要滚动了
            if (InfoGetManager.DOWNLOAD_INFO_LIST.size() < 4) {
                return;
            }
            SCROLL += (Mouse.getEventDWheel() < 0 ? 20 : -20);
            if (SCROLL < 0) {
                SCROLL = 0;
            }
            if (SCROLL > InfoGetManager.DOWNLOAD_INFO_LIST.size() * 75 - this.height + 75) {
                SCROLL = InfoGetManager.DOWNLOAD_INFO_LIST.size() * 75 - this.height + 75;
            }
            this.initGui();
        }
    }

    private void getCrc32Info() {
        try {
            Path path = Minecraft.getMinecraft().gameDir.toPath().resolve("resourcepacks");
            Files.walkFileTree(path, EnumSet.noneOf(FileVisitOption.class), 1, new SimpleFileVisitor<Path>() {
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
        for (DownloadInfo info : InfoGetManager.DOWNLOAD_INFO_LIST) {
            // 如果当前状态为下载中，直接返回
            if (info.getStatus() == DownloadStatus.DOWNLOADING) {
                return;
            }

            // 已下载或需要更新
            for (Long crc32 : crc32List) {
                // 检查已下载
                if (crc32.equals(info.getChecksum())) {
                    info.setStatus(DownloadStatus.DOWNLOADED);
                    return;
                }
                // 检查需更新
                if (info.getHistory().contains(crc32)) {
                    info.setStatus(DownloadStatus.NEED_UPDATE);
                    return;
                }
            }

            info.setStatus(DownloadStatus.NOT_DOWNLOAD);
        }
    }

    private String getI18nFormatAuthor(List<String> authors) {
        String str = String.join(I18n.format("gui.touhou_little_maid.resources_download.author.delimiter"), authors);
        return I18n.format("gui.touhou_little_maid.resources_download.author", str);
    }

    private String getI18nFormatFileSize(String size) {
        return I18n.format("gui.touhou_little_maid.resources_download.file_size", size);
    }
}
