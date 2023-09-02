package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class DirectButton extends Button {
    private Direction direction = Direction.SOUTH;

    public DirectButton(int pX, int pY, int pWidth, int pHeight, Direction direction, Button.IPressable pOnPress) {
        super(pX, pY, pWidth, pHeight, StringTextComponent.EMPTY, pOnPress);
        this.direction = direction;
    }


    public Direction getDirection() {
        return direction;
    }

    @Override
    public void onPress() {
        this.direction = Direction.from2DDataValue((direction.get2DDataValue() + 1) % 4);
        this.onPress.onPress(this);
    }

    @Override
    public ITextComponent getMessage() {
        switch (direction) {
            case EAST:
                return new TranslationTextComponent("gui.touhou_little_maid.model_switcher.direction.east");
            case WEST:
                return new TranslationTextComponent("gui.touhou_little_maid.model_switcher.direction.west");
            case SOUTH:
                return new TranslationTextComponent("gui.touhou_little_maid.model_switcher.direction.south");
            default:
            case NORTH:
                return new TranslationTextComponent("gui.touhou_little_maid.model_switcher.direction.north");
        }
    }
}