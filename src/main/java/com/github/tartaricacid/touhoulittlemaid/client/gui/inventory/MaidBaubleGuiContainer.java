package com.github.tartaricacid.touhoulittlemaid.client.gui.inventory;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MaidBaubleGuiContainer extends AbstractMaidGuiContainer {
    private static final ResourceLocation STORAGE_TEX = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/inventory_storage.png");

    public MaidBaubleGuiContainer(Container inventorySlotsIn, EntityMaid entityMaid) {
        super(inventorySlotsIn, entityMaid, 3);
    }

    @Override
    public void drawCustomScreen(int mouseX, int mouseY, float partialTicks) {
    }

    @Override
    public void drawCustomBackground(int mouseX, int mouseY, float partialTicks) {
        mc.getTextureManager().bindTexture(STORAGE_TEX);
        drawTexturedModalRect(guiLeft + 79, guiTop + 7, 0, 0, 72, 36);
    }
}
