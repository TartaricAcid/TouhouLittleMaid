package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import net.minecraft.client.gui.widget.ToggleWidget;

public class WirelessIOSlotButton extends ToggleWidget {
    private final int index;
    private final boolean[] config;

    public WirelessIOSlotButton(int index, int xIn, int yIn, int widthIn, int heightIn, boolean[] config) {
        super(xIn, yIn, widthIn, heightIn, config[index]);
        this.index = index;
        this.config = config;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.isStateTriggered = !this.isStateTriggered;
        this.config[this.index] = this.isStateTriggered;
    }
}
