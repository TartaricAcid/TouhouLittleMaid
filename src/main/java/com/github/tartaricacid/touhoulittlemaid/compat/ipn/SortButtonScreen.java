package com.github.tartaricacid.touhoulittlemaid.compat.ipn;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;

public class SortButtonScreen {
    private static final ResourceLocation SIDE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_side.png");
    private static final String IPN_ID = "inventoryprofilesnext";

    public static void renderBackground(MatrixStack matrixStack, int x, int y) {
        if (ModList.get().isLoaded(IPN_ID)) {
            Minecraft.getInstance().textureManager.bind(SIDE);
            AbstractGui.blit(matrixStack, x, y, 0, 73, 17, 48, 256, 256);
        }
    }
}
