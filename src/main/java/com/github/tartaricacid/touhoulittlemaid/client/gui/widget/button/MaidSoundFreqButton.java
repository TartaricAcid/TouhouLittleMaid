package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.config.InGameMaidConfig;
import net.minecraft.client.gui.widget.AbstractSlider;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import static com.github.tartaricacid.touhoulittlemaid.config.InGameMaidConfig.INSTANCE;

public class MaidSoundFreqButton extends AbstractSlider {
    public MaidSoundFreqButton(int x, int y) {
        super(x, y, 156, 20, StringTextComponent.EMPTY, MathHelper.clamp(INSTANCE.getSoundFrequency(), 0, 1.0));
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        ITextComponent number = new StringTextComponent((int) (this.value * 100.0D) + "%");
        this.setMessage((new TranslationTextComponent("gui.touhou_little_maid.maid_config.sound_frequency")).append(": ").append(number));
    }

    @Override
    protected void applyValue() {
        INSTANCE.setSoundFrequency(this.value);
        InGameMaidConfig.save();
    }
}
