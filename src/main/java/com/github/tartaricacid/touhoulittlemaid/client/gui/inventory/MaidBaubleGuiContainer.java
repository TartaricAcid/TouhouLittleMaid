package com.github.tartaricacid.touhoulittlemaid.client.gui.inventory;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.MaidBaubleContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;

import static com.github.tartaricacid.touhoulittlemaid.network.MaidGuiHandler.MAIN_GUI.BAUBLE;

@SideOnly(Side.CLIENT)
public class MaidBaubleGuiContainer extends AbstractMaidGuiContainer {
    private static final ResourceLocation STORAGE_TEX = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/inventory_storage.png");

    public MaidBaubleGuiContainer(InventoryPlayer inventory, EntityMaid maid, int taskIndex) {
        super(new MaidBaubleContainer(inventory, maid, taskIndex), BAUBLE.getId());
    }

    @Override
    public void drawCustomScreen(int mouseX, int mouseY, float partialTicks) {
    }

    @Override
    public void drawCustomBackground(int mouseX, int mouseY, float partialTicks) {
        mc.getTextureManager().bindTexture(STORAGE_TEX);
        drawTexturedModalRect(guiLeft + 79, guiTop + 7, 0, 7, 72, 36);
    }

    @Override
    public int getRenderPotionStartXOffset() {
        return 0;
    }

    @Override
    public void drawCustomTooltips(int mouseX, int mouseY, float partialTicks) {
    }

    @Override
    public String getGuiName() {
        return BAUBLE.name().toLowerCase(Locale.US);
    }
}
