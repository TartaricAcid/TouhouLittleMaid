package com.github.tartaricacid.touhoulittlemaid.client.gui.button;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class MaidStorageGuiButton extends GuiButton {
    protected static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation(TouhouLittleMaid.MOD_NAME, "textures/gui/storage_button.png");

    public MaidStorageGuiButton(int x, int y) {
        super(1, x, y, 18, 18, "");
    }
}
