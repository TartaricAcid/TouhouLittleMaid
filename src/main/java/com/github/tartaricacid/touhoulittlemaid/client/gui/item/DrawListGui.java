package com.github.tartaricacid.touhoulittlemaid.client.gui.item;

import com.github.tartaricacid.touhoulittlemaid.client.resources.CustomResourcesLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.draw.DrawManger;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;

import java.util.Optional;

public class DrawListGui extends GuiScrollingList {
    private DrawConfigGui parentGui;

    public DrawListGui(DrawConfigGui parentGui) {
        super(parentGui.mc, parentGui.width, parentGui.height, 90, parentGui.height,
                0, 12, parentGui.width, parentGui.height);
        this.parentGui = parentGui;
        setHeaderInfo(true, 15);
    }

    @Override
    protected int getSize() {
        return parentGui.modelDrawInfoList.size();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {
        DrawManger.ModelDrawInfo info = parentGui.modelDrawInfoList.get(index);
        Optional<MaidModelInfo> modelInfo = CustomResourcesLoader.MAID_MODEL.getInfo(info.getModelId());
        if (modelInfo.isPresent()) {
            parentGui.setSelectIndex(index);
            parentGui.setModelItem(modelInfo.get());
        }
    }

    @Override
    protected void drawHeader(int entryRight, int relativeY, Tessellator tess) {
        parentGui.getFontRenderer().drawString("序号", 10, relativeY, 0xffffffff);
        parentGui.getFontRenderer().drawString("皮肤名称", 40, relativeY, 0xffffffff);
        parentGui.getFontRenderer().drawString("奖池", parentGui.width - 100, relativeY, 0xffffffff);
        parentGui.getFontRenderer().drawString("权重", parentGui.width - 50, relativeY, 0xffffffff);
    }

    @Override
    protected boolean isSelected(int index) {
        return index == parentGui.getSelectIndex();
    }

    @Override
    protected void drawBackground() {
        Gui.drawRect(0, 89, parentGui.width, 90, 0xff566270);
        Gui.drawRect(0, 90, parentGui.width, parentGui.height, 0xff383A3F);
    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
        DrawManger.ModelDrawInfo info = parentGui.modelDrawInfoList.get(slotIdx);
        Optional<MaidModelInfo> modelInfo = CustomResourcesLoader.MAID_MODEL.getInfo(info.getModelId());
        if (modelInfo.isPresent()) {
            String name = ParseI18n.parse(modelInfo.get().getName());
            parentGui.getFontRenderer().drawString(String.valueOf(slotIdx), 10, slotTop, 0xffffffff);
            parentGui.getFontRenderer().drawString(name, 40, slotTop, 0xffffffff);
            parentGui.getFontRenderer().drawString(info.getLevel().getFormatText(), parentGui.width - 100, slotTop, 0xffffffff);
            parentGui.getFontRenderer().drawString(String.valueOf(info.getWeight()), parentGui.width - 50, slotTop, 0xffffffff);
            if (slotIdx % 2 == 0) {
                Gui.drawRect(0, slotTop - 2, parentGui.width, slotTop + 10, 0x22566270);
            }
        }
    }
}
