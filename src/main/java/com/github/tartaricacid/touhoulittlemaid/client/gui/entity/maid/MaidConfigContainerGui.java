package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.MaidSoundFreqButton;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.InGameMaidConfig;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.MaidConfigContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.ToggleWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import static com.github.tartaricacid.touhoulittlemaid.config.subconfig.InGameMaidConfig.INSTANCE;

public class MaidConfigContainerGui extends AbstractMaidContainerGui<MaidConfigContainer> {
    private static final ResourceLocation ICON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_config.png");
    private int left;
    private int top;

    public MaidConfigContainerGui(MaidConfigContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        InGameMaidConfig.read();
    }

    @Override
    protected void init() {
        super.init();
        left = leftPos + 81;
        top = topPos + 28;

        ToggleWidget showChatBubble = new ToggleWidget(left + 10, top + 10, 22, 22, INSTANCE.isShowChatBubble()) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                this.isStateTriggered = !this.isStateTriggered;
                INSTANCE.setShowChatBubble(this.isStateTriggered);
                InGameMaidConfig.save();
            }
        };
        showChatBubble.initTextureValues(22, 0, -22, 0, ICON);

        ToggleWidget showBackpack = new ToggleWidget(left + 10, top + 10 + 24, 22, 22, INSTANCE.isShowBackpack()) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                this.isStateTriggered = !this.isStateTriggered;
                INSTANCE.setShowBackpack(this.isStateTriggered);
                InGameMaidConfig.save();
            }
        };
        showBackpack.initTextureValues(22, 0, -22, 0, ICON);

        this.addButton(showChatBubble);
        this.addButton(showBackpack);
        this.addButton(new MaidSoundFreqButton(left + 10, top + 15 + 24 * 2));
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        super.renderBg(matrixStack, partialTicks, x, y);
        font.draw(matrixStack, new TranslationTextComponent("gui.touhou_little_maid.maid_config.show_chat_bubble"), left + 38, top + 17, 0x333333);
        font.draw(matrixStack, new TranslationTextComponent("gui.touhou_little_maid.maid_config.show_backpack"), left + 38, top + 17 + 24, 0x333333);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.getFocused() != null && this.isDragging() && button == 0) {
            this.getFocused().mouseDragged(mouseX, mouseY, button, dragX, dragY);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }
}
