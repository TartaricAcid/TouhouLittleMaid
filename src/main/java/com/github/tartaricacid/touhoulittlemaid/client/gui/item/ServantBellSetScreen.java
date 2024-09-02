package com.github.tartaricacid.touhoulittlemaid.client.gui.item;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.message.ServantBellSetPackage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class ServantBellSetScreen extends Screen {
    private final int maidId;
    private final UUID maidUuid;
    private EditBox textField;

    public ServantBellSetScreen(EntityMaid maid) {
        super(Component.empty());
        this.maidId = maid.getId();
        this.maidUuid = maid.getUUID();
    }

    @Override
    protected void init() {
        this.clearWidgets();
        int middleX = this.width / 2;
        int middleY = this.height / 2;
        textField = new EditBox(getMinecraft().font, middleX - 99, middleY - 26, 200, 20,
                Component.translatable("gui.touhou_little_maid.servant_bell.edit_box"));
        this.addWidget(this.textField);
        this.setInitialFocus(this.textField);
        this.addRenderableWidget(Button.builder(Component.translatable("gui.done"), this::sendDoneMessage)
                .pos(middleX - 100, middleY + 10).size(98, 20).build());
        this.addRenderableWidget(Button.builder(Component.translatable("gui.cancel"), b -> onClose())
                .pos(middleX + 4, middleY + 10).size(98, 20).build());
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        String value = this.textField.getValue();
        super.resize(minecraft, width, height);
        this.textField.setValue(value);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int middleX = this.width / 2;
        int middleY = this.height / 2;
        renderBackground(graphics, mouseX, mouseY, partialTicks);
        super.render(graphics, mouseX, mouseY, partialTicks);
        textField.render(graphics, mouseX, mouseY, partialTicks);
        if (textField.getValue().isEmpty()) {
            graphics.drawString(font, Component.translatable("gui.touhou_little_maid.servant_bell.edit_box").withStyle(ChatFormatting.ITALIC), middleX - 94, middleY - 20, ChatFormatting.DARK_GRAY.getColor(), false);
        }
        graphics.drawCenteredString(font, Component.translatable("tooltips.touhou_little_maid.servant_bell.desc", this.maidUuid.toString()), middleX, middleY - 50, 0xFFFFFF);
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
            PacketDistributor.sendToServer(new ServantBellSetPackage(this.maidId, textField.getValue()));
        }
        this.onClose();
    }
}
