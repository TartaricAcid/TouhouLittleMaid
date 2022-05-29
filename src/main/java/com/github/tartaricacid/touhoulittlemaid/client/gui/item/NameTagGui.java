package com.github.tartaricacid.touhoulittlemaid.client.gui.item;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.SendNameTagMessage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

public class NameTagGui extends Screen {
    private static final ResourceLocation TEXTURES = new ResourceLocation("textures/gui/container/beacon.png");
    private final EntityMaid maid;
    private EditBox textField;
    private Button alwaysShowButton;
    private boolean alwaysShow = false;

    public NameTagGui(EntityMaid maid) {
        super(TextComponent.EMPTY);
        this.maid = maid;
    }

    @Override
    protected void init() {
        this.getMinecraft().keyboardHandler.setSendRepeatsToGui(true);
        this.clearWidgets();
        int middleX = this.width / 2;
        int middleY = this.height / 2;
        textField = new EditBox(getMinecraft().font, middleX - 99, middleY - 26, 176, 20,
                new TranslatableComponent("gui.touhou_little_maid.name_tag.edit_box"));
        this.addWidget(this.textField);
        this.setInitialFocus(this.textField);
        addRenderableWidget(new Button(middleX - 100, middleY, 98, 20, new TranslatableComponent("gui.done"), this::sendDoneMessage));
        addRenderableWidget(new Button(middleX + 2, middleY, 98, 20, new TranslatableComponent("gui.cancel"), b -> onClose()));
        alwaysShowButton = new Button(middleX + 80, middleY - 26, 20, 20, TextComponent.EMPTY, b -> alwaysShow = !alwaysShow);
        addRenderableWidget(alwaysShowButton);
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        String value = this.textField.getValue();
        super.resize(minecraft, width, height);
        this.textField.setValue(value);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        int middleX = this.width / 2;
        int middleY = this.height / 2;
        renderBackground(poseStack);
        textField.render(poseStack, mouseX, mouseY, partialTicks);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURES);
        blit(poseStack, middleX + 80, middleY - 26, alwaysShow ? 88 : 110, 220, 20, 20);
        if (alwaysShowButton.isHoveredOrFocused()) {
            renderTooltip(poseStack, new TranslatableComponent("gui.touhou_little_maid.tag.always_show"), mouseX, mouseY);
        }
    }

    @Override
    public void tick() {
        this.textField.tick();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return this.textField.mouseClicked(mouseX, mouseY, button) || super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void insertText(String text, boolean overwrite) {
        if (overwrite) {
            this.textField.setValue(text);
        } else {
            this.textField.insertText(text);
        }
    }

    private void sendDoneMessage(Button button) {
        if (StringUtils.isNotBlank(textField.getValue())) {
            NetworkHandler.CHANNEL.sendToServer(new SendNameTagMessage(maid.getId(), textField.getValue(), alwaysShow));
        }
        this.onClose();
    }
}
