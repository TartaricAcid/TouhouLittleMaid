package com.github.tartaricacid.touhoulittlemaid.compat.emi.altar;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

import static net.minecraft.client.gui.GuiComponent.blit;

/**
 * 用来渲染祭坛图标
 */
public class EmiAltarRecipeCategory extends EmiRecipeCategory {
    public static final ResourceLocation ID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "altar");
    private static final ResourceLocation ALTAR_ICON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/altar_icon.png");

    public EmiAltarRecipeCategory() {
        super(ID, (draw, x, y, delta) -> {
        });
    }


    @Override
    public void render(PoseStack matrices, int x, int y, float delta) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, ALTAR_ICON);
        blit(matrices, x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public void renderSimplified(PoseStack matrices, int x, int y, float delta) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, ALTAR_ICON);
        blit(matrices, x, y, 0, 0, 16, 16, 16, 16);
    }
}