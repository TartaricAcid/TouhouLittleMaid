package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class TaskButton extends Button {
    private final IMaidTask task;
    private final ResourceLocation resourceLocation;
    private final int xTexStart;
    private final int yTexStart;
    private final int yDiffTex;
    private final int textureWidth;
    private final int textureHeight;
    private final List<Component> tooltips;

    public TaskButton(IMaidTask task, int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation resourceLocationIn, OnPress onPressIn) {
        this(task, xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, 256, 256, onPressIn);
    }

    public TaskButton(IMaidTask task, int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation resourceLocationIn, int textureWidth, int textureHeight, OnPress onPressIn) {
        this(task, xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, textureWidth, textureHeight, onPressIn, Component.empty());
    }

    public TaskButton(IMaidTask task, int x, int y, int width, int height, int xTexStart, int yTexStart, int yDiffText, ResourceLocation resourceLocation, int textureWidth, int textureHeight, OnPress onPress, Component title) {
        this(task, x, y, width, height, xTexStart, yTexStart, yDiffText, resourceLocation, textureWidth, textureHeight, onPress, Collections.emptyList(), title);
    }

    public TaskButton(IMaidTask task, int x, int y, int width, int height, int xTexStart, int yTexStart, int yDiffText, ResourceLocation resourceLocation, int textureWidth, int textureHeight, OnPress onPress, List<Component> tooltips, Component title) {
        super(x, y, width, height, title, onPress, Supplier::get);
        this.task = task;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.xTexStart = xTexStart;
        this.yTexStart = yTexStart;
        this.yDiffTex = yDiffText;
        this.resourceLocation = resourceLocation;
        this.tooltips = tooltips;
    }

    public IMaidTask getTask() {
        return task;
    }

    @Override
    @SuppressWarnings("all")
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        int i = this.yTexStart;
        if (this.isHoveredOrFocused()) {
            i += this.yDiffTex;
        }
        RenderSystem.enableDepthTest();
        graphics.blit(this.resourceLocation, this.getX(), this.getY(), (float) this.xTexStart, (float) i, this.width, this.height, this.textureWidth, this.textureHeight);
        graphics.renderItem(task.getIcon(), this.getX() + 2, this.getY() + 2);
        graphics.drawString(minecraft.font, task.getName(), this.getX() + 23, this.getY() + 6, 0x333333, false);
        if (this.isHovered()) {
            this.renderToolTip(graphics, minecraft, mouseX, mouseY);
        }
    }

    private void renderToolTip(GuiGraphics graphics, Minecraft mc, int mouseX, int mouseY) {
        if (!this.tooltips.isEmpty()) {
            graphics.renderComponentTooltip(mc.font, this.tooltips, mouseX, mouseY);
        }
    }
}
