package com.github.tartaricacid.touhoulittlemaid.client.gui.inventory;


import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.favorability.Level;
import com.github.tartaricacid.touhoulittlemaid.inventory.MaidMainContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;

import static com.github.tartaricacid.touhoulittlemaid.network.MaidGuiHandler.MAIN_GUI.MAIN;

@SideOnly(Side.CLIENT)
public class MaidMainGuiContainer extends AbstractMaidGuiContainer {
    public MaidMainGuiContainer(InventoryPlayer inventory, EntityMaid maid, int taskIndex) {
        super(new MaidMainContainer(inventory, maid, taskIndex), MAIN.getId());
    }

    @Override
    public void drawCustomScreen(int mouseX, int mouseY, float partialTicks) {
        int i = this.guiLeft;
        int j = this.guiTop;
        GlStateManager.pushMatrix();
        mc.renderEngine.bindTexture(BACKGROUND);
        GlStateManager.disableLighting();
        drawTexturedModalRect(i + 80, j + 10, 178, 93, 9, 9);
        drawTexturedModalRect(i + 80, j + 22, 188, 93, 9, 9);
        drawTexturedModalRect(i + 105, j + 10, 178, 104, 64, 9);
        drawTexturedModalRect(i + 105, j + 22, 178, 104, 64, 9);
        drawTexturedModalRect(i + 107, j + 12, 180, 114, (int) (64 * maid.getHealth() / maid.getMaxHealth()), 5);ResourceLocation icon = Level.getLevelByCount(maid.getFavorability()).getIcon();
        mc.renderEngine.bindTexture(icon);
        drawModalRectWithCustomSizedTexture(i + 150, j + 39,
                0, 0, 16, 16, 16, 16);
        double armorNum = maid.getEntityAttribute(SharedMonsterAttributes.ARMOR).getAttributeValue();
        drawTexturedModalRect(i + 107, j + 24, 180, 120, (int) (64 * (armorNum % 20.00000000001) / 20), 5);
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
        // 绘制文字
        fontRenderer.drawString(String.valueOf(Math.round(maid.getHealth())), i + 91, j + 11, 0x555555, false);
        fontRenderer.drawString(String.valueOf(Math.round(armorNum)), i + 91, j + 23, 0x555555, false);
        String favStr = I18n.format("gui.touhou_little_maid.info.favorability", maid.getFavorability());
        fontRenderer.drawString(favStr, i + 80, j + 35, 0x555555, false);
        fontRenderer.drawString(I18n.format("gui.touhou_little_maid.info.experience", maid.getExp()), i + 80, j + 46, 0x555555, false);
    }

    @Override
    public void drawCustomBackground(int mouseX, int mouseY, float partialTicks) {
    }

    @Override
    public void drawCustomTooltips(int mouseX, int mouseY, float partialTicks) {
    }

    @Override
    public int getRenderPotionStartXOffset() {
        return 0;
    }

    @Override
    public String getGuiName() {
        return MAIN.name().toLowerCase(Locale.US);
    }
}
