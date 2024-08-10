package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import java.util.List;

public class WirelessIOSlotButton extends TouhouStateSwitchButton {
    private final int index;
    private final List<Boolean> config;

    public WirelessIOSlotButton(int index, int xIn, int yIn, int widthIn, int heightIn, List<Boolean> config) {
        super(xIn, yIn, widthIn, heightIn, config.get(index));
        this.index = index;
        this.config = config;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.isStateTriggered = !this.isStateTriggered;
        this.config.set(this.index, this.isStateTriggered);
    }
}
