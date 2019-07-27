package com.github.tartaricacid.touhoulittlemaid.client.gui.inventory;


import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.MaidMainContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.github.tartaricacid.touhoulittlemaid.network.MaidGuiHandler.GUI.MAIN;

@SideOnly(Side.CLIENT)
public class MaidMainGuiContainer extends AbstractMaidGuiContainer {
    public MaidMainGuiContainer(InventoryPlayer inventory, EntityMaid maid, int taskIndex) {
        super(new MaidMainContainer(inventory, maid, taskIndex), MAIN.getId());
    }

    @Override
    public void drawCustomScreen(int mouseX, int mouseY, float partialTicks) {
        int i = this.guiLeft;
        int j = this.guiTop;
        // 绘制文字
        fontRenderer.drawString(I18n.format("gui.touhou_little_maid.info.healthy", maid.getHealth(), maid.getMaxHealth()), i + 82, j + 10, 0x555555, false);
        fontRenderer.drawString(I18n.format("gui.touhou_little_maid.info.armor", maid.getEntityAttribute(SharedMonsterAttributes.ARMOR).getAttributeValue()), i + 82, j + 22, 0x555555, false);
        fontRenderer.drawString(I18n.format("gui.touhou_little_maid.info.attack", maid.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()), i + 82, j + 34, 0x555555, false);
        fontRenderer.drawString(I18n.format("gui.touhou_little_maid.info.experience", maid.getExp()), i + 82, j + 46, 0x555555, false);
    }

    @Override
    public void drawCustomBackground(int mouseX, int mouseY, float partialTicks) {
    }

    @Override
    public String getGuiName() {
        return "main";
    }
}
