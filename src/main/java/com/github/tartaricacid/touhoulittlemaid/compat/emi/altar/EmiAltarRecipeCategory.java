package com.github.tartaricacid.touhoulittlemaid.compat.emi.altar;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

/**
 * 用来渲染祭坛图标
 */
public class EmiAltarRecipeCategory extends EmiRecipeCategory {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "altar");
    private static final ResourceLocation ALTAR_ICON = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/gui/altar_icon.png");

    public EmiAltarRecipeCategory() {
        super(ID, (draw, x, y, delta) -> {
        });
    }

    @Override
    public void render(GuiGraphics draw, int x, int y, float delta) {
        draw.blit(ALTAR_ICON, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public void renderSimplified(GuiGraphics draw, int x, int y, float delta) {
        draw.blit(ALTAR_ICON, x, y, 0, 0, 16, 16, 16, 16);
    }
}