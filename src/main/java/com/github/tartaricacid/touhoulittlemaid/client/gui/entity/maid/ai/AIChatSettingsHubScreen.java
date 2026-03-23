package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Objects;

public class AIChatSettingsHubScreen extends Screen {
    private final Screen parent;
    private final EntityMaid maid;

    public AIChatSettingsHubScreen(Screen parent, EntityMaid maid) {
        super(Component.translatable("gui.touhou_little_maid.ai_settings.screen.title"));
        this.parent = parent;
        this.maid = maid;
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            Screen screen = Objects.requireNonNullElse(this.parent, new AIChatScreen(this.maid));
            this.minecraft.setScreen(screen);
        }
    }
}