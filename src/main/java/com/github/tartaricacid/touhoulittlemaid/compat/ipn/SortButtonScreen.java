package com.github.tartaricacid.touhoulittlemaid.compat.ipn;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;

public class SortButtonScreen {
    private static final ResourceLocation SIDE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_side.png");
    private static final String IPN_ID = "inventoryprofilesnext";

    public static void renderBackground(PoseStack matrixStack, int x, int y) {
        if (ModList.get().isLoaded(IPN_ID)) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, SIDE);
            RenderSystem.enableDepthTest();
            GuiComponent.blit(matrixStack, x, y, 0, 73, 17, 48, 256, 256);
        }
    }
}
