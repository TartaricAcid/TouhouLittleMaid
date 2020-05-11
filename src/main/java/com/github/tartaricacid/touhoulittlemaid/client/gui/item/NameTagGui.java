package com.github.tartaricacid.touhoulittlemaid.client.gui.item;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.SendNameTagMessage;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class NameTagGui extends GuiScreen {
    private EntityMaid maid;
    private GuiTextField textField;

    public NameTagGui(EntityMaid maid) {
        this.maid = maid;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        buttonList.clear();
        int middleX = this.width / 2;
        int middleY = this.height / 2;
        textField = new GuiTextField(0, mc.fontRenderer, middleX - 99, middleY - 26, 198, 20);
        textField.setFocused(true);
        buttonList.add(new GuiButton(1, middleX - 100, middleY, 98, 20, I18n.format("gui.done")));
        buttonList.add(new GuiButton(2, middleX + 2, middleY, 98, 20, I18n.format("gui.cancel")));
    }

    @Override
    public void updateScreen() {
        textField.updateCursorCounter();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        textField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 1) {
            if (StringUtils.isNotBlank(textField.getText())) {
                CommonProxy.INSTANCE.sendToServer(new SendNameTagMessage(maid.getUniqueID(), textField.getText()));
            }
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
        textField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char c, int keyCode) throws IOException {
        super.keyTyped(c, keyCode);
        textField.textboxKeyTyped(c, keyCode);
    }
}
