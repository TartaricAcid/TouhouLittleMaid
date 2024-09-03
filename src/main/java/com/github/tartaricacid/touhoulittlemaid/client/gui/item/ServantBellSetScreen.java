package com.github.tartaricacid.touhoulittlemaid.client.gui.item;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.ServantBellSetMessage;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class ServantBellSetScreen extends Screen {
    private final int maidId;
    private final UUID maidUuid;
    private EditBox textField;

    public ServantBellSetScreen(EntityMaid maid) {
        super(TextComponent.EMPTY);
        this.maidId = maid.getId();
        this.maidUuid = maid.getUUID();
    }

    @Override
    protected void init() {
        this.clearWidgets();
        int middleX = this.width / 2;
        int middleY = this.height / 2;
        textField = new EditBox(getMinecraft().font, middleX - 99, middleY - 26, 200, 20,
                new TranslatableComponent("gui.touhou_little_maid.servant_bell.edit_box"));
        this.addWidget(this.textField);
        this.setInitialFocus(this.textField);
        this.addRenderableWidget(new Button(middleX - 100, middleY + 10, 98, 20, new TranslatableComponent("gui.done"), this::sendDoneMessage));
        this.addRenderableWidget(new Button(middleX + 4, middleY + 10, 98, 20, new TranslatableComponent("gui.cancel"), b -> onClose()));
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
        if (textField.getValue().isEmpty()) {
            drawString(poseStack, font, new TranslatableComponent("gui.touhou_little_maid.servant_bell.edit_box").withStyle(ChatFormatting.ITALIC), middleX - 94, middleY - 20, ChatFormatting.DARK_GRAY.getColor());
        }
        drawCenteredString(poseStack, font, new TranslatableComponent("tooltips.touhou_little_maid.servant_bell.uuid", this.maidUuid.toString()), middleX, middleY - 50, 0xFFFFFF);
        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void tick() {
        this.textField.tick();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.textField.mouseClicked(mouseX, mouseY, button)) {
            this.setFocused(this.textField);
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
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
            NetworkHandler.CHANNEL.sendToServer(new ServantBellSetMessage(this.maidId, textField.getValue()));
        }
        this.onClose();
    }
}
