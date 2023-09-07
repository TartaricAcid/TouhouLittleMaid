package com.github.tartaricacid.touhoulittlemaid.client.gui.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.WirelessIOButton;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.WirelessIOContainer;
import com.github.tartaricacid.touhoulittlemaid.item.ItemWirelessIO;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.WirelessIOGuiMessage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class WirelessIOContainerGui extends AbstractContainerScreen<WirelessIOContainer> {
    private static final ResourceLocation MAIN = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/wireless_io.png");
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
                    NetworkHandler.CHANNEL.sendToServer(new WirelessIOGuiMessage(isMaidToChest, isBlacklist));
                }, (m, x, y) -> this.renderTooltip(m, Component.translatable("gui.touhou_little_maid.wireless_io.io_mode"), x, y));

        ioModeToggle.initTextureValues(194, 32, -18, 18, MAIN);
        WirelessIOButton filterModeToggle = new WirelessIOButton(leftPos + 136, topPos + 26, 16, 16, isBlacklist,
                (x, y) -> {
                    isBlacklist = !isBlacklist;
                    NetworkHandler.CHANNEL.sendToServer(new WirelessIOGuiMessage(isMaidToChest, isBlacklist));
                }, (m, x, y) -> this.renderTooltip(m, Component.translatable("gui.touhou_little_maid.wireless_io.filter_mode"), x, y));
        filterModeToggle.initTextureValues(176, 0, 16, 16, MAIN);

        ImageButton configButton = new ImageButton(leftPos + 136, topPos + 44, 16, 16, 208, 0, 16,
                MAIN, 256, 256, buttons -> getMinecraft().setScreen(new WirelessIOConfigSlotGui(menu.getWirelessIO())),
                (b, m, x, y) -> this.renderTooltip(m, Component.translatable("gui.touhou_little_maid.wireless_io.config_slot"), x, y),
                Component.empty());

        addRenderableWidget(filterModeToggle);
        addRenderableWidget(configButton);
        addRenderableWidget(ioModeToggle);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        super.render(poseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int x, int y) {
        this.renderBackground(poseStack);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, MAIN);
        blit(poseStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        if (isBlacklist) {
            blit(poseStack, leftPos + 61, topPos + 15, 0, 166, 54, 55);
        }
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int x, int y) {
    }
}
