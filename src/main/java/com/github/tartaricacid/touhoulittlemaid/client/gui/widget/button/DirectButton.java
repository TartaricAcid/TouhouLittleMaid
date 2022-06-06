package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import net.minecraft.client.gui.components.Button;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class DirectButton extends Button {
    private Direction direction = Direction.SOUTH;

    public DirectButton(int pX, int pY, int pWidth, int pHeight, Direction direction, OnPress pOnPress) {
        super(pX, pY, pWidth, pHeight, TextComponent.EMPTY, pOnPress);
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
    public Component getMessage() {
        switch (direction) {
            case EAST:
                return new TranslatableComponent("gui.touhou_little_maid.model_switcher.direction.east");
            case WEST:
                return new TranslatableComponent("gui.touhou_little_maid.model_switcher.direction.west");
            case SOUTH:
                return new TranslatableComponent("gui.touhou_little_maid.model_switcher.direction.south");
            default:
            case NORTH:
                return new TranslatableComponent("gui.touhou_little_maid.model_switcher.direction.north");
        }
    }
}
