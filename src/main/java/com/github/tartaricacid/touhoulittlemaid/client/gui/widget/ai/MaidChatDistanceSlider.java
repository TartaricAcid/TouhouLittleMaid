package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.ai;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.settings.AIChatSettingsHubScreen.SharedState;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class MaidChatDistanceSlider extends AbstractSliderButton {
    private final SharedState state;

    public MaidChatDistanceSlider(int x, int y, int width, int height, SharedState state) {
        super(x, y, width, height, Component.empty(), toSliderValueStatic(state.maidCanChatDistance));
        this.state = state;
        this.updateMessage();
    }

    private static double toSliderValueStatic(int distance) {
        int clamped = Mth.clamp(distance, 1, 80);
        return (double) (clamped - 1) / (80 - 1);
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Component.literal(String.valueOf(this.getDistanceValue())));
    }

    @Override
    protected void applyValue() {
        this.state.maidCanChatDistance = this.getDistanceValue();
        this.updateMessage();
    }

    public int getDistanceValue() {
        return (int) Math.round(1 + this.value * (80 - 1));
    }
}
