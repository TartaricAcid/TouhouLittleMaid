package com.github.tartaricacid.touhoulittlemaid.client.gui.mod;

import com.github.tartaricacid.touhoulittlemaid.init.registry.CompatRegistry;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import org.apache.commons.lang3.StringUtils;

public class ClothConfigScreen extends Screen {
    private final String clothConfigUrl = "https://www.curseforge.com/minecraft/mc-mods/cloth-config";
    private final Screen lastScreen;
    private MultiLineLabel message = MultiLineLabel.EMPTY;

    protected ClothConfigScreen(Screen lastScreen) {
        super(Component.literal("Cloth Config API"));
        this.lastScreen = lastScreen;
    }

    @Override
    protected void init() {
        int posX = (this.width - 200) / 2;
        int posY = this.height / 2;
        this.message = MultiLineLabel.create(this.font, Component.translatable("gui.touhou_little_maid.cloth_config_warning.tips"), 300);
        this.addRenderableWidget(Button.builder(Component.translatable("gui.touhou_little_maid.cloth_config_warning.download"), b -> openUrl(clothConfigUrl)).bounds(posX, posY - 15, 200, 20).build());
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_BACK, (pressed) -> Minecraft.getInstance().setScreen(this.lastScreen)).bounds(posX, posY + 50, 200, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(graphics,pMouseX,pMouseY,pPartialTick);
        this.message.renderCentered(graphics, this.width / 2, 80);
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
    }

    private void openUrl(String url) {
        if (StringUtils.isNotBlank(url) && minecraft != null) {
            minecraft.setScreen(new ConfirmLinkScreen(yes -> {
                if (yes) {
                    Util.getPlatform().openUri(url);
                }
                minecraft.setScreen(this);
            }, url, true));
        }
    }

//    public static void registerNoClothConfigPage() {
//        if (!ModList.get().isLoaded(CompatRegistry.CLOTH_CONFIG)) {
//            ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
//                    new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> new ClothConfigScreen(parent)));
//        }
//    }
}
