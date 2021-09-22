package com.github.tartaricacid.touhoulittlemaid.util;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class GuiTools {
    public static final Button.IPressable NO_ACTION = (button) -> {
    };
}
