package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.InGameMaidConfig;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;

import static com.github.tartaricacid.touhoulittlemaid.config.subconfig.InGameMaidConfig.INSTANCE;

public class MaidSoundFreqButton extends AbstractSliderButton {
    public MaidSoundFreqButton(int x, int y) {
        super(x, y, 156, 20, TextComponent.EMPTY, Mth.clamp(INSTANCE.getSoundFrequency(), 0, 1.0));
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        Component number = new TextComponent((int) (this.value * 100.0D) + "%");
        this.setMessage((new TranslatableComponent("gui.touhou_little_maid.maid_config.sound_frequency")).append(": ").append(number));
    }

    @Override
    protected void applyValue() {
        INSTANCE.setSoundFrequency(this.value);
        InGameMaidConfig.save();
    }
}