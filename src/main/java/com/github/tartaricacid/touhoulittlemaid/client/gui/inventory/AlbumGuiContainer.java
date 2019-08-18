package com.github.tartaricacid.touhoulittlemaid.client.gui.inventory;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.AlbumContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * @author TartaricAcid
 * @date 2019/8/18 22:42
 **/
public class AlbumGuiContainer extends GuiContainer {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/album.png");
    private ItemStack album;

    public AlbumGuiContainer(IInventory playerInventory, ItemStack album) {
        super(new AlbumContainer(playerInventory, album));
        this.album = album;
        this.xSize = 256;
        this.ySize = 214;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        super.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.drawDefaultBackground();
        // 绘制主背景
        mc.getTextureManager().bindTexture(BACKGROUND);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, 256, 214);
    }
}
