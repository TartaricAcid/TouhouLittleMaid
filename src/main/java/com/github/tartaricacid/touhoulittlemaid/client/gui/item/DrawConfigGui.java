package com.github.tartaricacid.touhoulittlemaid.client.gui.item;

import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.config.GeneralConfig;
import com.github.tartaricacid.touhoulittlemaid.draw.DrawManger;
import com.github.tartaricacid.touhoulittlemaid.draw.SendToServerDrawMessage;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil.clearMaidDataResidue;

public class DrawConfigGui extends GuiScreen {
    private static final String ENTITY_ID = "touhou_little_maid:entity.passive.maid";
    public List<DrawManger.ModelDrawInfo> modelDrawInfoList;
    private boolean isEditPool = false;
    private MaidModelInfo modelItem;
    private DrawListGui drawList;
    private int selectIndex;

    public DrawConfigGui(List<DrawManger.ModelDrawInfo> modelDrawInfoList) {
        this.modelDrawInfoList = modelDrawInfoList;
    }

    @Override
    public void initGui() {
        drawList = new DrawListGui(this);
        drawList.elementClicked(0, false);
        buttonList.add(new GuiButton(0, 100, 20, 30, 20, "N"));
        buttonList.add(new GuiButton(1, 132, 20, 30, 20, "R"));
        buttonList.add(new GuiButton(2, 164, 20, 30, 20, "SR"));
        buttonList.add(new GuiButton(3, 196, 20, 30, 20, "SSR"));
        buttonList.add(new GuiButton(4, 228, 20, 30, 20, "UR"));

        buttonList.add(new GuiButton(5, 100, 60, 30, 20, "-100"));
        buttonList.add(new GuiButton(6, 132, 60, 30, 20, "-10"));
        buttonList.add(new GuiButton(7, 164, 60, 30, 20, "-1"));
        buttonList.add(new GuiButton(8, 196, 60, 30, 20, "+1"));
        buttonList.add(new GuiButton(9, 228, 60, 30, 20, "+10"));
        buttonList.add(new GuiButton(10, 260, 60, 30, 20, "+100"));

        buttonList.add(new GuiButton(11, width - 90, 20, 80, 20, "Save"));
        buttonList.add(new GuiButton(12, width - 90, 45, 80, 20, "Save & Reload"));

        buttonList.add(new GuiButton(13, 260, 20, 30, 20, "Edit"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawGradientRect(0, 0, this.width, this.height, 0xff1F2124, 0xff1F2124);
        if (modelItem != null) {
            EntityMaid maid;
            try {
                maid = (EntityMaid) EntityCacheUtil.ENTITY_CACHE.get(ENTITY_ID, () -> {
                    Entity e = EntityList.createEntityByIDFromName(new ResourceLocation(ENTITY_ID), mc.world);
                    if (e == null) {
                        return new EntityMaid(mc.world);
                    } else {
                        return e;
                    }
                });
            } catch (ExecutionException | ClassCastException e) {
                e.printStackTrace();
                return;
            }
            clearMaidDataResidue(maid, true);
            maid.setModelId(modelItem.getModelId().toString());
            GuiInventory.drawEntityOnScreen(50, 80, (int) (40 * modelItem.getRenderItemScale()), -mouseX + 50, -mouseY + 40, maid);
        }
        if (!isEditPool) {
            drawList.drawScreen(mouseX, mouseY, partialTicks);
        } else {
            String text = String.format(
                    "§7奖池概率列举如下，你可通过修改服务器配置文件夹下的 §4§nTouhouLittleMaid.cfg§7 的 §4§ngashaponconfig§7 选项来修改奖池概率：\n\n" +
                            "§b§lN 等级权重： %d\n" +
                            "§a§lS 等级权重： %d\n" +
                            "§c§lSR 等级权重： %d\n" +
                            "§5§lSSR 等级权重： %d\n" +
                            "§6§lUR 等级权重： %d\n\n" +
                            "§9§n【再次点击 Edit 按钮返回】",
                    GeneralConfig.GASHAPON_CONFIG.gashaponWeights1, GeneralConfig.GASHAPON_CONFIG.gashaponWeights2,
                    GeneralConfig.GASHAPON_CONFIG.gashaponWeights3, GeneralConfig.GASHAPON_CONFIG.gashaponWeights4,
                    GeneralConfig.GASHAPON_CONFIG.gashaponWeights5);
            fontRenderer.drawSplitString(text, 50, 110, width - 100, 0xffffffff);
        }
        if (selectIndex >= 0 && selectIndex < modelDrawInfoList.size()) {
            fontRenderer.drawString("奖池： " + modelDrawInfoList.get(selectIndex).getLevel().getFormatText(), 100, 5, 0xffffffff);
            fontRenderer.drawString("权重： " + modelDrawInfoList.get(selectIndex).getWeight(), 100, 45, 0xffffffff);
            super.drawScreen(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        DrawManger.ModelDrawInfo info = modelDrawInfoList.get(selectIndex);
        switch (button.id) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                info.setLevel(DrawManger.Level.values()[button.id]);
                return;
            case 5:
                info.setWeight(MathHelper.clamp(info.getWeight() - 100, 0, Integer.MAX_VALUE));
                return;
            case 6:
                info.setWeight(MathHelper.clamp(info.getWeight() - 10, 0, Integer.MAX_VALUE));
                return;
            case 7:
                info.setWeight(MathHelper.clamp(info.getWeight() - 1, 0, Integer.MAX_VALUE));
                return;
            case 8:
                info.setWeight(MathHelper.clamp(info.getWeight() + 1, 0, Integer.MAX_VALUE));
                return;
            case 9:
                info.setWeight(MathHelper.clamp(info.getWeight() + 10, 0, Integer.MAX_VALUE));
                return;
            case 10:
                info.setWeight(MathHelper.clamp(info.getWeight() + 100, 0, Integer.MAX_VALUE));
                return;
            case 11:
                CommonProxy.INSTANCE.sendToServer(new SendToServerDrawMessage(modelDrawInfoList, false));
                return;
            case 12:
                CommonProxy.INSTANCE.sendToServer(new SendToServerDrawMessage(modelDrawInfoList, true));
                return;
            case 13:
                isEditPool = !isEditPool;
            default:
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        if (!isEditPool) {
            int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
            int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
            drawList.handleMouseInput(mouseX, mouseY);
        }
        super.handleMouseInput();
    }

    public FontRenderer getFontRenderer() {
        return fontRenderer;
    }

    public int getSelectIndex() {
        return selectIndex;
    }

    public void setSelectIndex(int index) {
        selectIndex = MathHelper.clamp(index, 0, modelDrawInfoList.size() - 1);
    }

    public void setModelItem(MaidModelInfo modelItem) {
        this.modelItem = modelItem;
    }
}
