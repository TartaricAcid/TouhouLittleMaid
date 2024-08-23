package com.github.tartaricacid.touhoulittlemaid.client.gui.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.TouhouImageButton;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.WirelessIOButton;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.WirelessIOContainer;
import com.github.tartaricacid.touhoulittlemaid.item.ItemWirelessIO;
import com.github.tartaricacid.touhoulittlemaid.network.message.WirelessIOGuiPackage;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import org.anti_ad.mc.ipn.api.IPNIgnore;

@IPNIgnore
public class WirelessIOContainerGui extends AbstractContainerScreen<WirelessIOContainer> {
    private static final ResourceLocation MAIN = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/gui/wireless_io.png");
    private boolean isMaidToChest;
    private boolean isBlacklist;

    public WirelessIOContainerGui(WirelessIOContainer container, Inventory inv, Component titleIn) {
        super(container, inv, titleIn);
        this.isMaidToChest = ItemWirelessIO.isMaidToChest(container.getWirelessIO());
        this.isBlacklist = ItemWirelessIO.isBlacklist(container.getWirelessIO());
    }

    @Override
    protected void init() {
        super.init();
        this.clearWidgets();

        WirelessIOButton ioModeToggle = new WirelessIOButton(leftPos + 23, topPos + 34, 18, 18, isMaidToChest,
                (x, y) -> {
                    isMaidToChest = !isMaidToChest;
                    PacketDistributor.sendToServer(new WirelessIOGuiPackage(isMaidToChest, isBlacklist));
                }, (m, x, y) -> m.renderTooltip(font, Component.translatable("gui.touhou_little_maid.wireless_io.io_mode"), x, y));

        ioModeToggle.initTextureValues(194, 32, -18, 18, MAIN);
        WirelessIOButton filterModeToggle = new WirelessIOButton(leftPos + 136, topPos + 26, 16, 16, isBlacklist,
                (x, y) -> {
                    isBlacklist = !isBlacklist;
                    PacketDistributor.sendToServer(new WirelessIOGuiPackage(isMaidToChest, isBlacklist));
                }, (m, x, y) -> m.renderTooltip(font, Component.translatable("gui.touhou_little_maid.wireless_io.filter_mode"), x, y));
        filterModeToggle.initTextureValues(176, 0, 16, 16, MAIN);

        TouhouImageButton configButton = new TouhouImageButton(leftPos + 136, topPos + 44, 16, 16, 208, 0, 16,
                MAIN, 256, 256, buttons -> getMinecraft().setScreen(new WirelessIOConfigSlotGui(menu.getWirelessIO())));
        configButton.setTooltip(Tooltip.create(Component.translatable("gui.touhou_little_maid.wireless_io.config_slot")));

        addRenderableWidget(filterModeToggle);
        addRenderableWidget(configButton);
        addRenderableWidget(ioModeToggle);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
        super.renderTransparentBackground(graphics);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, MAIN);
        graphics.blit(MAIN, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        if (isBlacklist) {
            graphics.blit(MAIN, leftPos + 61, topPos + 15, 0, 166, 54, 55);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int x, int y) {
    }
}
