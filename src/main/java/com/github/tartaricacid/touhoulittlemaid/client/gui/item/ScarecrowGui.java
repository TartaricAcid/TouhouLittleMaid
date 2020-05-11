package com.github.tartaricacid.touhoulittlemaid.client.gui.item;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityScarecrow;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.SetScarecrowDataMessage;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class ScarecrowGui extends GuiScreen {
    private EntityScarecrow scarecrow;
    private GuiTextField nameField;
    private GuiTextField textField;

    public ScarecrowGui(EntityScarecrow scarecrow) {
        this.scarecrow = scarecrow;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        buttonList.clear();
        int middleX = this.width / 2;
        int middleY = this.height / 2;

        nameField = new GuiTextField(0, mc.fontRenderer, middleX - 134, middleY - 43, 263, 20);
        nameField.setFocused(true);
        nameField.setText(scarecrow.getCustomNameTag());
        nameField.setMaxStringLength(16);

        textField = new GuiTextField(0, mc.fontRenderer, middleX - 134, middleY - 5, 263, 20);
        textField.setText(scarecrow.getText());
        textField.setMaxStringLength(28);
        buttonList.add(new GuiButton(1, middleX - 134, middleY + 20, 130, 20, I18n.format("gui.done")));
        buttonList.add(new GuiButton(2, middleX, middleY + 20, 130, 20, I18n.format("gui.cancel")));
    }

    @Override
    public void updateScreen() {
        nameField.updateCursorCounter();
        textField.updateCursorCounter();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int middleX = this.width / 2;
        int middleY = this.height / 2;

        drawDefaultBackground();
        nameField.drawTextBox();
        textField.drawTextBox();
        drawString(fontRenderer, I18n.format("gui.touhou_little_maid.scarecrow_gui.name.desc"), middleX - 134, middleY - 55, 0xffffff);
        drawString(fontRenderer, I18n.format("gui.touhou_little_maid.scarecrow_gui.text.desc"), middleX - 134, middleY - 17, 0xffffff);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 1) {
            CommonProxy.INSTANCE.sendToServer(new SetScarecrowDataMessage(scarecrow.getUniqueID(), nameField.getText(), textField.getText()));
            mc.addScheduledTask(() -> mc.displayGuiScreen(null));
            return;
        }
        if (button.id == 2) {
            mc.addScheduledTask(() -> mc.displayGuiScreen(null));
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        nameField.mouseClicked(mouseX, mouseY, mouseButton);
        textField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char c, int keyCode) throws IOException {
        super.keyTyped(c, keyCode);
        nameField.textboxKeyTyped(c, keyCode);
        textField.textboxKeyTyped(c, keyCode);
    }
}
