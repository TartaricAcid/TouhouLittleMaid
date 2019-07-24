package com.github.tartaricacid.touhoulittlemaid.client.gui.inventory;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.MaidInventoryContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MaidBaubleGuiContainer extends AbstractMaidGuiContainer {
    private static final ResourceLocation STORAGE_TEX = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/inventory_storage.png");

    public MaidBaubleGuiContainer(InventoryPlayer inventory, EntityMaid maid, int taskIndex) {
        super(new MaidInventoryContainer(inventory, maid, taskIndex), BUTTON.BAUBLE.getGuiId());
    }

    @Override
    public void drawCustomScreen(int mouseX, int mouseY, float partialTicks) {
    }

    @Override
    public void drawCustomBackground(int mouseX, int mouseY, float partialTicks) {
        mc.getTextureManager().bindTexture(STORAGE_TEX);
        drawTexturedModalRect(guiLeft + 79, guiTop + 7, 0, 0, 72, 36);
    }

    @Override
    public String getGuiName() {
        return "bauble";
    }
}
