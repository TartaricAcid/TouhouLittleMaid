package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.config.InGameMaidConfig;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;

import static com.github.tartaricacid.touhoulittlemaid.config.InGameMaidConfig.INSTANCE;

public class MaidSoundFreqButton extends AbstractSliderButton {
    public MaidSoundFreqButton(int x, int y) {
        super(x, y, 156, 20, Component.empty(), Mth.clamp(INSTANCE.getSoundFrequency(), 0, 1.0));
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        Component number = Component.literal((int) (this.value * 100.0D) + "%");
        this.setMessage((Component.translatable("gui.touhou_little_maid.maid_config.sound_frequency")).append(": ").append(number));
    }

    @Override
    protected void applyValue() {
        INSTANCE.setSoundFrequency(this.value);
        InGameMaidConfig.save();
    }
}