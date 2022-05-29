package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class ButtonWithId extends Button {
    private final Consumer<Integer> onClick;
    private final int id;

    public ButtonWithId(int id, int x, int y, int width, int height, Component title, Consumer<Integer> onClick) {
        super(x, y, width, height, title, (b) -> {
        });
        this.id = id;
        this.onClick = onClick;
    }

    @Override
    public void onPress() {
        super.onPress();
        this.onClick.accept(this.id);
    }
}
