package com.github.tartaricacid.touhoulittlemaid.client.gui.skin;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.HataSasimonoFlagModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.HataSasimonoFrameModel;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.SetMaidSasimonoCRC32;
import com.github.tartaricacid.touhoulittlemaid.proxy.ClientProxy;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import com.google.common.collect.Iterators;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.Iterator;

/**
 * @author TartaricAcid
 * @date 2019/8/15 22:02
 **/
@SideOnly(Side.CLIENT)
public class MaidHataSelect extends GuiScreen {
    private static final ModelBase HATA_SASIMONO_FLAG_MODEL = new HataSasimonoFlagModel();
    private static final ModelBase HATA_SASIMONO_FRAME_MODEL = new HataSasimonoFrameModel();
    private static final ResourceLocation FRAME_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/hata_sasimono.png");
    private EntityMaid maid;
    /**
     * 避免自动拆箱，自动装箱带来的微乎其微的性能问题？
     */
    private Long hataCrc32;
    private int index = 0;
    private boolean isShowSasimono;
    private GuiButton showHideButton;

    public MaidHataSelect(EntityMaid maid) {
        this.maid = maid;
        this.hataCrc32 = maid.getSasimonoCRC32();
        this.isShowSasimono = maid.isShowSasimono();
        for (Long crc32 : ClientProxy.HATA_NAME_MAP.keySet()) {
            if (maid.getSasimonoCRC32().equals(crc32)) {
                break;
            }
            index++;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawGradientRect(0, 0, this.width, this.height, 0xee101010, 0xee101010);
        drawTopListModel();
        drawBottomReferenceModel();
        if (isShowSasimono) {
            showHideButton.displayString = I18n.format("gui.touhou_little_maid.hata_select.display.true");
        } else {
            showHideButton.displayString = I18n.format("gui.touhou_little_maid.hata_select.display.false");
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        fontRenderer.drawString(String.format("%s%d/%d", TextFormatting.BOLD, index + 1, ClientProxy.HATA_NAME_MAP.size()),
                width / 2, height / 9, 0xFFFFFF);
    }

    private void drawTopListModel() {
        Iterator<Long> iterable = Iterators.cycle(ClientProxy.HATA_NAME_MAP.keySet());
        // 临时变量，用来渲染上部列表
        Long tmpCrc32;

        // 以防万一，保证里面至少有一个元素
        if (ClientProxy.HATA_NAME_MAP.keySet().size() < 1) {
            return;
        }

        // 依据索引跳过某些元素
        for (int j = 0; j < index; j++) {
            iterable.next();
        }

        // 获取第一个元素，为渲染下部图片所使用
        tmpCrc32 = hataCrc32 = iterable.next();

        // 绘制列表
        for (int i = 5; i < width - 29; i += 24) {
            GlStateManager.pushMatrix();
            GlStateManager.enableCull();
            GlStateManager.translate(i, height * 2 / 5, 100);
            GlStateManager.rotate(90, 0, 1, 0);
            GlStateManager.bindTexture(ClientProxy.HATA_NAME_MAP.get(tmpCrc32));
            HATA_SASIMONO_FLAG_MODEL.render(maid, 0, 0, 0, 0, 0, 2);
            mc.renderEngine.bindTexture(FRAME_TEXTURE);
            HATA_SASIMONO_FRAME_MODEL.render(maid, 0, 0, 0, 0, 0, 2);
            GlStateManager.disableCull();
            GlStateManager.popMatrix();
            // 临时变量获取下一个
            tmpCrc32 = iterable.next();
        }
    }

    /**
     * 绘制底部参考图片
     */
    private void drawBottomReferenceModel() {
        if (isShowSasimono) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(width / 4, height * 4 / 5, 100);
            drawHataModel(hataCrc32, 90);
            drawHataModel(hataCrc32, -90);
            GlStateManager.popMatrix();
        }
    }

    /**
     * 依据索引绘制旗帜
     *
     * @param crc32 旗帜索引
     * @param angle 角度，默认为 90、-90 度，用来控制旗帜渲染正反两面
     */
    private void drawHataModel(Long crc32, float angle) {
        GlStateManager.pushMatrix();
        GlStateManager.enableCull();
        GlStateManager.rotate(angle, 0, 1, 0);
        GlStateManager.bindTexture(ClientProxy.HATA_NAME_MAP.get(crc32));
        HATA_SASIMONO_FLAG_MODEL.render(maid, 0, 0, 0, 0, 0, 2);
        mc.renderEngine.bindTexture(FRAME_TEXTURE);
        HATA_SASIMONO_FRAME_MODEL.render(maid, 0, 0, 0, 0, 0, 2);
        GlStateManager.disableCull();
        GlStateManager.popMatrix();
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        // 鼠标滚轮改变大小
        if (Mouse.getEventDWheel() != 0) {
            index = Mouse.getEventDWheel() < 0 ? indexCycle(index + 1) : indexCycle(index - 1);
        }
    }

    /**
     * 循环索引，超过上限跳转到下限，低于下限，跳转到上限
     */
    private int indexCycle(int indexNext) {
        int size = ClientProxy.HATA_NAME_MAP.size();
        if (0 <= indexNext && indexNext < size) {
            return indexNext;
        } else if (indexNext < 0) {
            return size - 1;
        } else {
            return 0;
        }
    }

    @Override
    public void initGui() {
        this.addButton(new GuiButton(BUTTON.APPLY.ordinal(), width * 2 / 5, height * 4 / 5 - 36, 204, 20, I18n.format("gui.touhou_little_maid.hata_select.apply")));
        this.addButton(new GuiButton(BUTTON.CLOSE.ordinal(), width * 2 / 5, height * 4 / 5 - 12, 100, 20, I18n.format("gui.touhou_little_maid.hata_select.close")));
        showHideButton = new GuiButton(BUTTON.SHOW_HIDE.ordinal(), width * 2 / 5 + 104, height * 4 / 5 - 12, 100, 20, "");
        this.addButton(showHideButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == BUTTON.APPLY.ordinal()) {
            CommonProxy.INSTANCE.sendToServer(new SetMaidSasimonoCRC32(maid.getUniqueID(), hataCrc32, maid.isShowSasimono()));
            mc.addScheduledTask(() -> mc.displayGuiScreen(null));
        }
        if (button.id == BUTTON.CLOSE.ordinal()) {
            mc.addScheduledTask(() -> mc.displayGuiScreen(null));
        }
        if (button.id == BUTTON.SHOW_HIDE.ordinal()) {
            isShowSasimono = !isShowSasimono;
            CommonProxy.INSTANCE.sendToServer(new SetMaidSasimonoCRC32(maid.getUniqueID(), maid.getSasimonoCRC32(), isShowSasimono));
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    enum BUTTON {
        // 关闭按钮
        CLOSE,
        // 应用材质按钮
        APPLY,
        // 显示/隐藏按钮
        SHOW_HIDE
    }
}
