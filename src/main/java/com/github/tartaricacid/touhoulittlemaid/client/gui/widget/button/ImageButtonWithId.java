package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ImageButtonWithId extends ImageButton {
    private final int index;

    public ImageButtonWithId(int index, int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation resourceLocationIn, IPressable onPressIn) {
        this(index, xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, 256, 256, onPressIn);
    }

    public ImageButtonWithId(int index, int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation resourceLocationIn, int textureWidth, int textureHeight, IPressable onPressIn) {
        this(index, xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, textureWidth, textureHeight, onPressIn, StringTextComponent.EMPTY);
    }

    public ImageButtonWithId(int index, int x, int y, int width, int height, int xTexStart, int yTexStart, int yDiffText, ResourceLocation resourceLocation, int textureWidth, int textureHeight, IPressable onPress, ITextComponent title) {
        this(index, x, y, width, height, xTexStart, yTexStart, yDiffText, resourceLocation, textureWidth, textureHeight, onPress, NO_TOOLTIP, title);
    }

    public ImageButtonWithId(int index, int x, int y, int width, int height, int xTexStart, int yTexStart, int yDiffText, ResourceLocation resourceLocation, int textureWidth, int textureHeight, IPressable onPress, ITooltip tooltip, ITextComponent title) {
        super(x, y, width, height, xTexStart, yTexStart, yDiffText, resourceLocation, textureWidth, textureHeight, onPress, tooltip, title);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
