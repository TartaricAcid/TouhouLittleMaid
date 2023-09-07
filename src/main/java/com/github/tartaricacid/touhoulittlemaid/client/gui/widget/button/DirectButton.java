package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import net.minecraft.client.gui.components.Button;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class DirectButton extends Button {
    private Direction direction = Direction.SOUTH;

    public DirectButton(int pX, int pY, int pWidth, int pHeight, Direction direction, OnPress pOnPress) {
        super(pX, pY, pWidth, pHeight, Component.empty(), pOnPress);
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
                return Component.translatable("gui.touhou_little_maid.model_switcher.direction.east");
            case WEST:
                return Component.translatable("gui.touhou_little_maid.model_switcher.direction.west");
            case SOUTH:
                return Component.translatable("gui.touhou_little_maid.model_switcher.direction.south");
            default:
            case NORTH:
                return Component.translatable("gui.touhou_little_maid.model_switcher.direction.north");
        }
    }
}
