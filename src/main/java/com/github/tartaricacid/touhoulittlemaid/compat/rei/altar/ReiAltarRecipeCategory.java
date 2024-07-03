package com.github.tartaricacid.touhoulittlemaid.compat.rei.altar;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.compat.rei.MaidREIClientPlugin;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.client.gui.GuiComponent.blit;

public class ReiAltarRecipeCategory implements DisplayCategory<ReiAltarRecipeDisplay> {
    private static final MutableComponent TITLE = Component.translatable("jei.touhou_little_maid.altar_craft.title");
    private static final ResourceLocation ALTAR_ICON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/altar_icon.png");
    private static final ResourceLocation POWER_ICON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/power_point.png");
    private final Renderer icon;

    public ReiAltarRecipeCategory() {
        this.icon = new Renderer() {
            @Override
            public void render(PoseStack matrices, Rectangle bounds, int mouseX, int mouseY, float delta) {
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, ALTAR_ICON);
                blit(matrices, bounds.x, bounds.y, 0, 0, 16, 16, 16, 16);
            }

            @Override
            public int getZ() {
                return 100;
            }

            @Override
            public void setZ(int z) {
            }
        };
    }

    @Override
    public CategoryIdentifier<? extends ReiAltarRecipeDisplay> getCategoryIdentifier() {
        return MaidREIClientPlugin.ALTAR;
    }

    @Override
    public Component getTitle() {
        return TITLE;
    }

    @Override
    public Renderer getIcon() {
        return icon;
    }

    @Override
    public int getDisplayHeight() {
        return 125;
    }

    @Override
    public int getDisplayWidth(ReiAltarRecipeDisplay display) {
        return 160;
    }

    @Override
    public List<Widget> setupDisplay(ReiAltarRecipeDisplay display, Rectangle bounds) {
        int darkGray = 0x555555;
        Font font = Minecraft.getInstance().font;
        String result = I18n.get("jei.touhou_little_maid.altar_craft.result", I18n.get(display.getLangKey()));
        int startX = bounds.x;
        int startY = bounds.y + 5;

        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createSlot(new Point(startX + 40, startY + 35)).entries(getInput(display.getInputEntries(), 0)).markInput());
        widgets.add(Widgets.createSlot(new Point(startX + 40, startY + 55)).entries(getInput(display.getInputEntries(), 1)).markInput());
        widgets.add(Widgets.createSlot(new Point(startX + 60, startY + 15)).entries(getInput(display.getInputEntries(), 2)).markInput());
        widgets.add(Widgets.createSlot(new Point(startX + 80, startY + 15)).entries(getInput(display.getInputEntries(), 3)).markInput());
        widgets.add(Widgets.createSlot(new Point(startX + 100, startY + 35)).entries(getInput(display.getInputEntries(), 4)).markInput());
        widgets.add(Widgets.createSlot(new Point(startX + 100, startY + 55)).entries(getInput(display.getInputEntries(), 5)).markInput());
        widgets.add(Widgets.createSlot(new Point(startX + 140 - 5, startY + 5)).entries(display.getOutputEntries().get(0)).markOutput());

        widgets.add(Widgets.createTexturedWidget(POWER_ICON, startX + 72, startY + 38, 32, 0, 16, 16, 64, 64));
        widgets.add(Widgets.withTranslate(Widgets.createDrawableWidget((helper, matrices, mouseX, mouseY, v) -> {
            font.draw(matrices, result, 0, 0, darkGray);
        }), startX + (int) ((bounds.getWidth() - font.width(result)) / 2.0f), startY + 85, 0));

        widgets.add(Widgets.withTranslate(Widgets.createDrawableWidget((helper, matrices, mouseX, mouseY, v) -> {
            font.draw(matrices, String.format("×%.2f", display.getPowerCost()), 0, 0, darkGray);
        }), startX + 65, startY + 55, 0));

        return widgets;
    }

    private EntryIngredient getInput(List<EntryIngredient> inputs, int index) {
        if (index < inputs.size()) {
            return inputs.get(index);
        }
        return EntryIngredient.empty();
    }
}