package com.github.tartaricacid.touhoulittlemaid.client.gui;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.download.InfoGetManager;
import com.github.tartaricacid.touhoulittlemaid.client.download.pojo.DownloadInfo;
import com.github.tartaricacid.touhoulittlemaid.client.download.pojo.DownloadStatus;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
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
    private static final ResourceLocation BG = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/download_background.png");
    private static float scroll;
    private static int startIndex;
    private static int index;
    private List<Long> crc32List = Lists.newArrayList();

    public ResourcesDownloadGui() {
        if (InfoGetManager.DOWNLOAD_INFO_LIST != null && InfoGetManager.DOWNLOAD_INFO_LIST.size() != 0) {
            getCrc32Info();
            checkDownloadInfo();
            calculationStartIndex();
        }
    }

    @Override
    public void initGui() {
        this.buttonList.clear();

        int startX = this.width / 2 - 194;
        int startY = this.height / 2 - 94;

        if (InfoGetManager.DOWNLOAD_INFO_LIST == null || InfoGetManager.DOWNLOAD_INFO_LIST.size() == 0) {
            addButton(new GuiButton(-1, 5, 5, 60, 20, I18n.format("gui.touhou_little_maid.resources_download.reload")));
            return;
        }

        for (int i = startIndex; i < 9 + startIndex; i++) {
            if (i < InfoGetManager.DOWNLOAD_INFO_LIST.size()) {
                DownloadInfo info = InfoGetManager.DOWNLOAD_INFO_LIST.get(i);
                GuiButton button = new GuiButton(i - startIndex, startX, startY + 21 * (i - startIndex), 171, 20, I18n.format(info.getName()));
                if (i == index) {
                    button.enabled = false;
                }
                addButton(button);
            }
        }
        addButton(new GuiDownloadButton(9, startX + 184, startY + 165, 200, 20, InfoGetManager.DOWNLOAD_INFO_LIST.get(index)));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // 中心点
        int middleX = this.width / 2;
        int middleY = this.height / 2;

        drawDefaultBackground();
        if (InfoGetManager.DOWNLOAD_INFO_LIST == null || InfoGetManager.DOWNLOAD_INFO_LIST.size() == 0) {
            drawCenteredString(fontRenderer, I18n.format("gui.touhou_little_maid.resources_download.fail.1"), this.width / 2, this.height / 2 - 15, 0xfffff);
            drawCenteredString(fontRenderer, I18n.format("gui.touhou_little_maid.resources_download.fail.2"), this.width / 2, this.height / 2, 0xfffff);
        } else {
            mc.renderEngine.bindTexture(BG);
            drawModalRectWithCustomSizedTexture(middleX - 200, middleY - 100, 0, 0, 400, 256, 484, 256);
            drawModalRectWithCustomSizedTexture(middleX - 19, middleY - 92 + (int) (169 * scroll), 400, 0, 4, 15, 484, 256);
            addResInfo(InfoGetManager.DOWNLOAD_INFO_LIST.get(index));
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void addResInfo(DownloadInfo info) {
        int startX = this.width / 2 - 7;
        int startY = this.height / 2 - 88;

        String name = I18n.format(info.getName());
        drawString(fontRenderer, name, startX, startY, 0xFFAA00);
        drawString(fontRenderer, info.getVersion(), startX + fontRenderer.getStringWidth(name) + 4, startY, 0x55FF55);
        drawString(fontRenderer, getI18nFormatFileSize(info.getFormatFileSize()), startX, startY + 12, 0x55FFFF);
        drawString(fontRenderer, getI18nFormatFileTime(info.getFormatData()), startX, startY + 24, 0x55FFFF);
        drawString(fontRenderer, getI18nFormatAuthor(info.getAuthor()), startX, startY + 36, 0x55FFFF);
        fontRenderer.drawSplitString(I18n.format(info.getDesc()), startX, startY + 60, 195, 0x6a6a6a);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == -1) {
            InfoGetManager.checkInfoJsonFile();
            mc.player.sendMessage(new TextComponentTranslation("message.touhou_little_maid.resources_download.reload"));
            mc.addScheduledTask(() -> mc.displayGuiScreen(null));
            return;
        }
        if (button.id >= 0 && button.id < 9) {
            index = button.id + startIndex;
            this.initGui();
            return;
        }
        if (button.id == 9) {
            DownloadInfo info = InfoGetManager.DOWNLOAD_INFO_LIST.get(index);
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
            // 小于等于 ⑨ 个就不要滚动了
            if (InfoGetManager.DOWNLOAD_INFO_LIST.size() <= 9) {
                return;
            }
            float num = Mouse.getEventDWheel() < 0 ? 1.0f : -1.0f;
            scroll = MathHelper.clamp(scroll + num / (InfoGetManager.DOWNLOAD_INFO_LIST.size() - 9), 0, 1);
            startIndex = startIndex + (int) num;
            if (startIndex < 0) {
                startIndex = 0;
            }
            if (startIndex > InfoGetManager.DOWNLOAD_INFO_LIST.size() - 9) {
                startIndex = InfoGetManager.DOWNLOAD_INFO_LIST.size() - 9;
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
                continue;
            }

            // 先设置为未下载
            info.setStatus(DownloadStatus.NOT_DOWNLOAD);

            // 已下载或需要更新
            for (Long crc32 : crc32List) {
                // 检查已下载
                if (crc32.equals(info.getChecksum())) {
                    info.setStatus(DownloadStatus.DOWNLOADED);
                    break;
                }
            }
        }
    }

    private void calculationStartIndex() {
        int size = InfoGetManager.DOWNLOAD_INFO_LIST.size();
        index = MathHelper.clamp(index, 0, size - 1);
        if (size - 9 > 0 && startIndex > size - 9) {
            startIndex = size - 9;
        }
    }

    private String getI18nFormatAuthor(List<String> authors) {
        String str = String.join(I18n.format("gui.touhou_little_maid.resources_download.author.delimiter"), authors);
        return I18n.format("gui.touhou_little_maid.resources_download.author", str);
    }

    private String getI18nFormatFileSize(String size) {
        return I18n.format("gui.touhou_little_maid.resources_download.file_size", size);
    }

    private String getI18nFormatFileTime(String time) {
        return I18n.format("gui.touhou_little_maid.resources_download.upload_time", time);
    }
}