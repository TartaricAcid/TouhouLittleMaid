package com.github.tartaricacid.touhoulittlemaid.client.gui.item;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.SendNameTagMessage;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

/**
 * @author TartaricAcid
 */
@SideOnly(Side.CLIENT)
public class NameTagGui extends GuiScreen {
    private static final ResourceLocation TEXTURES = new ResourceLocation("textures/gui/container/beacon.png");
    private final EntityMaid maid;
    private GuiTextField textField;
    private boolean alwaysShow = false;

    public NameTagGui(EntityMaid maid) {
        this.maid = maid;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        buttonList.clear();
        int middleX = this.width / 2;
        int middleY = this.height / 2;
        textField = new GuiTextField(0, mc.fontRenderer, middleX - 99, middleY - 26, 176, 20);
        textField.setFocused(true);
        buttonList.add(new GuiButton(1, middleX - 100, middleY, 98, 20, I18n.format("gui.done")));
        buttonList.add(new GuiButton(2, middleX + 2, middleY, 98, 20, I18n.format("gui.cancel")));
        buttonList.add(new GuiButton(3, middleX + 80, middleY - 26, 20, 20, ""));
    }

    @Override
    public void updateScreen() {
        textField.updateCursorCounter();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int middleX = this.width / 2;
        int middleY = this.height / 2;
        drawDefaultBackground();
        textField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.color(255, 255, 255);
        mc.renderEngine.bindTexture(TEXTURES);
        drawTexturedModalRect(middleX + 80, middleY - 26, alwaysShow ? 88 : 110, 220, 20, 20);
        if (middleX + 80 < mouseX && mouseX < middleX + 100 && middleY - 26 < mouseY && mouseY < middleY - 6) {
            drawHoveringText(I18n.format("gui.touhou_little_maid.tag.always_show"), mouseX, mouseY);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 1) {
            if (StringUtils.isNotBlank(textField.getText())) {
                CommonProxy.INSTANCE.sendToServer(new SendNameTagMessage(maid.getUniqueID(), textField.getText(), alwaysShow));
            }
            mc.addScheduledTask(() -> mc.displayGuiScreen(null));
            return;
        }
        if (button.id == 2) {
            mc.addScheduledTask(() -> mc.displayGuiScreen(null));
            return;
        }
        if (button.id == 3) {
            alwaysShow = !alwaysShow;
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
