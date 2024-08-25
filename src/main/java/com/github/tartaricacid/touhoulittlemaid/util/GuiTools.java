package com.github.tartaricacid.touhoulittlemaid.util;

import net.minecraft.client.gui.components.Button;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class GuiTools {
    public static final Button.OnPress NO_ACTION = (button) -> {
    };
}
