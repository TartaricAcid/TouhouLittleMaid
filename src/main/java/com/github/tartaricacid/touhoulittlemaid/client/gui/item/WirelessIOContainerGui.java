package com.github.tartaricacid.touhoulittlemaid.client.gui.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.WirelessIOButton;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.WirelessIOContainer;
import com.github.tartaricacid.touhoulittlemaid.item.ItemWirelessIO;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.WirelessIOGuiMessage;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class WirelessIOContainerGui extends ContainerScreen<WirelessIOContainer> {
    private static final ResourceLocation MAIN = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/wireless_io.png");
    private boolean isMaidToChest;
    private boolean isBlacklist;

    public WirelessIOContainerGui(WirelessIOContainer container, PlayerInventory inv, ITextComponent titleIn) {
        super(container, inv, titleIn);
        this.isMaidToChest = ItemWirelessIO.isMaidToChest(container.getWirelessIO());
        this.isBlacklist = ItemWirelessIO.isBlacklist(container.getWirelessIO());
    }

    @Override
    protected void init() {
        super.init();
        this.buttons.clear();
        this.children.clear();

        WirelessIOButton ioModeToggle = new WirelessIOButton(leftPos + 23, topPos + 34, 18, 18, isMaidToChest,
                (x, y) -> {
                    isMaidToChest = !isMaidToChest;
                    NetworkHandler.CHANNEL.sendToServer(new WirelessIOGuiMessage(isMaidToChest, isBlacklist));
                }, (m, x, y) -> this.renderTooltip(m, new TranslationTextComponent("gui.touhou_little_maid.wireless_io.io_mode"), x, y));

        ioModeToggle.initTextureValues(194, 32, -18, 18, MAIN);
        WirelessIOButton filterModeToggle = new WirelessIOButton(leftPos + 136, topPos + 26, 16, 16, isBlacklist,
                (x, y) -> {
                    isBlacklist = !isBlacklist;
                    NetworkHandler.CHANNEL.sendToServer(new WirelessIOGuiMessage(isMaidToChest, isBlacklist));
                }, (m, x, y) -> this.renderTooltip(m, new TranslationTextComponent("gui.touhou_little_maid.wireless_io.filter_mode"), x, y));
        filterModeToggle.initTextureValues(176, 0, 16, 16, MAIN);

        ImageButton configButton = new ImageButton(leftPos + 136, topPos + 44, 16, 16, 208, 0, 16,
                MAIN, 256, 256, buttons -> getMinecraft().setScreen(new WirelessIOConfigSlotGui(menu.getWirelessIO())),
                (b, m, x, y) -> this.renderTooltip(m, new TranslationTextComponent("gui.touhou_little_maid.wireless_io.config_slot"), x, y),
                StringTextComponent.EMPTY);

        addButton(filterModeToggle);
        addButton(configButton);
        addButton(ioModeToggle);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        this.renderBackground(matrixStack);
        getMinecraft().textureManager.bind(MAIN);
        blit(matrixStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        if (isBlacklist) {
            blit(matrixStack, leftPos + 61, topPos + 15, 0, 166, 54, 55);
        }
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int x, int y) {
    }
}
