package com.github.tartaricacid.touhoulittlemaid.client.gui.item;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.SendNameTagMessage;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.StringUtils;

public class NameTagGui extends Screen {
    private static final ResourceLocation TEXTURES = new ResourceLocation("textures/gui/container/beacon.png");
    private final EntityMaid maid;
    private TextFieldWidget textField;
    private Button alwaysShowButton;
    private boolean alwaysShow = false;

    public NameTagGui(EntityMaid maid) {
        super(StringTextComponent.EMPTY);
        this.maid = maid;
    }

    @Override
    protected void init() {
        this.getMinecraft().keyboardHandler.setSendRepeatsToGui(true);
        this.buttons.clear();
        this.children.clear();
        int middleX = this.width / 2;
        int middleY = this.height / 2;
        textField = new TextFieldWidget(getMinecraft().font, middleX - 99, middleY - 26, 176, 20,
                new TranslationTextComponent("gui.touhou_little_maid.name_tag.edit_box"));
        this.children.add(this.textField);
        this.setInitialFocus(this.textField);
        addButton(new Button(middleX - 100, middleY, 98, 20, new TranslationTextComponent("gui.done"), this::sendDoneMessage));
        addButton(new Button(middleX + 2, middleY, 98, 20, new TranslationTextComponent("gui.cancel"), b -> onClose()));
        alwaysShowButton = new Button(middleX + 80, middleY - 26, 20, 20, StringTextComponent.EMPTY, b -> alwaysShow = !alwaysShow);
        addButton(alwaysShowButton);
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        String value = this.textField.getValue();
        super.resize(minecraft, width, height);
        this.textField.setValue(value);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        int middleX = this.width / 2;
        int middleY = this.height / 2;
        renderBackground(matrixStack);
        textField.render(matrixStack, mouseX, mouseY, partialTicks);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        getMinecraft().textureManager.bind(TEXTURES);
        blit(matrixStack, middleX + 80, middleY - 26, alwaysShow ? 88 : 110, 220, 20, 20);
        if (alwaysShowButton.isHovered()) {
            renderTooltip(matrixStack, new TranslationTextComponent("gui.touhou_little_maid.tag.always_show"), mouseX, mouseY);
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
