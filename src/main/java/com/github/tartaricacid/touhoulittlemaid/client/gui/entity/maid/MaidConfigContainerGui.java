package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.MaidSoundFreqButton;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.InGameMaidConfig;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.MaidConfigContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StateSwitchingButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.anti_ad.mc.ipn.api.IPNButton;
import org.anti_ad.mc.ipn.api.IPNGuiHint;
import org.anti_ad.mc.ipn.api.IPNPlayerSideOnly;

import static com.github.tartaricacid.touhoulittlemaid.config.subconfig.InGameMaidConfig.INSTANCE;

@IPNPlayerSideOnly
@IPNGuiHint(button = IPNButton.SORT, horizontalOffset = -36, bottom = -12)
@IPNGuiHint(button = IPNButton.SORT_COLUMNS, horizontalOffset = -24, bottom = -24)
@IPNGuiHint(button = IPNButton.SORT_ROWS, horizontalOffset = -12, bottom = -36)
@IPNGuiHint(button = IPNButton.SHOW_EDITOR, horizontalOffset = -5)
@IPNGuiHint(button = IPNButton.SETTINGS, horizontalOffset = -5)
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

        StateSwitchingButton showBackItem = new StateSwitchingButton(left + 10, top + 10 + 24 * 2, 22, 22, INSTANCE.isShowBackItem()) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                this.isStateTriggered = !this.isStateTriggered;
                INSTANCE.setShowBackItem(this.isStateTriggered);
                InGameMaidConfig.save();
            }
        };
        showBackItem.initTextureValues(22, 0, -22, 0, ICON);

        this.addRenderableWidget(showChatBubble);
        this.addRenderableWidget(showBackpack);
        this.addRenderableWidget(showBackItem);
        this.addRenderableWidget(new MaidSoundFreqButton(left + 10, top + 15 + 24 * 3));
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
        super.renderBg(graphics, partialTicks, x, y);
        graphics.drawString(font, Component.translatable("gui.touhou_little_maid.maid_config.show_chat_bubble"), left + 38, top + 17, 0x333333, false);
        graphics.drawString(font, Component.translatable("gui.touhou_little_maid.maid_config.show_backpack"), left + 38, top + 17 + 24, 0x333333, false);
        graphics.drawString(font, Component.translatable("gui.touhou_little_maid.maid_config.show_back_item"), left + 38, top + 17 + 24 * 2, 0x333333, false);
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