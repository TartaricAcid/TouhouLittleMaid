package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.MaidSoundFreqButton;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.InGameMaidConfig;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.MaidConfigContainer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.StateSwitchingButton;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static com.github.tartaricacid.touhoulittlemaid.config.subconfig.InGameMaidConfig.INSTANCE;

public class MaidConfigContainerGui extends AbstractMaidContainerGui<MaidConfigContainer> {
    private static final ResourceLocation ICON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_config.png");
    private int left;
    private int top;

    public MaidConfigContainerGui(MaidConfigContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        InGameMaidConfig.read();
    }

    @Override
    protected void init() {
        super.init();
        left = leftPos + 81;
        top = topPos + 28;

        StateSwitchingButton showChatBubble = new StateSwitchingButton(left + 10, top + 10, 22, 22, INSTANCE.isShowChatBubble()) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                this.isStateTriggered = !this.isStateTriggered;
                INSTANCE.setShowChatBubble(this.isStateTriggered);
                InGameMaidConfig.save();
            }
        };
        showChatBubble.initTextureValues(22, 0, -22, 0, ICON);

        StateSwitchingButton showBackpack = new StateSwitchingButton(left + 10, top + 10 + 24, 22, 22, INSTANCE.isShowBackpack()) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                this.isStateTriggered = !this.isStateTriggered;
                INSTANCE.setShowBackpack(this.isStateTriggered);
                InGameMaidConfig.save();
            }
        };
        showBackpack.initTextureValues(22, 0, -22, 0, ICON);

        this.addRenderableWidget(showChatBubble);
        this.addRenderableWidget(showBackpack);
        this.addRenderableWidget(new MaidSoundFreqButton(left + 10, top + 15 + 24 * 2));
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        super.renderBg(matrixStack, partialTicks, x, y);
        font.draw(matrixStack, new TranslatableComponent("gui.touhou_little_maid.maid_config.show_chat_bubble"), left + 38, top + 17, 0x333333);
        font.draw(matrixStack, new TranslatableComponent("gui.touhou_little_maid.maid_config.show_backpack"), left + 38, top + 17 + 24, 0x333333);
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