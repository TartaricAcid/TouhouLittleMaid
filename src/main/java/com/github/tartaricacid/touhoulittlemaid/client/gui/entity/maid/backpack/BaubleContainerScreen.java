package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.backpack;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.BaubleButton;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.TabIndex;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack.BaubleContainer;
import com.github.tartaricacid.touhoulittlemaid.network.message.OpenMaidGuiPackage;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import org.anti_ad.mc.ipn.api.IPNButton;
import org.anti_ad.mc.ipn.api.IPNGuiHint;
import org.anti_ad.mc.ipn.api.IPNPlayerSideOnly;

@IPNPlayerSideOnly
@IPNGuiHint(button = IPNButton.SORT, horizontalOffset = -36, bottom = -12)
@IPNGuiHint(button = IPNButton.SORT_COLUMNS, horizontalOffset = -24, bottom = -24)
@IPNGuiHint(button = IPNButton.SORT_ROWS, horizontalOffset = -12, bottom = -36)
@IPNGuiHint(button = IPNButton.SHOW_EDITOR, horizontalOffset = -5)
@IPNGuiHint(button = IPNButton.SETTINGS, horizontalOffset = -5)
public class BaubleContainerScreen extends AbstractMaidContainerGui<BaubleContainer> implements IBackpackContainerScreen {
    private static final ResourceLocation BAUBLE_BG = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_bauble.png");
    private final EntityMaid maid;
    private final int favorabilityLevel;

    public BaubleContainerScreen(BaubleContainer container, Inventory inv, Component titleIn) {
        super(container, inv, titleIn);
        this.imageHeight = 256;
        this.imageWidth = 256;
        this.maid = menu.getMaid();
        this.favorabilityLevel = this.maid.getFavorabilityManager().getLevel();
    }

    @Override
    protected void initAdditionWidgets() {
        BaubleButton baubleButton = new BaubleButton(leftPos, topPos, true, btn -> {
            OpenMaidGuiPackage message = new OpenMaidGuiPackage(maid.getId(), TabIndex.MAIN);
            PacketDistributor.sendToServer(message);
        });
        this.addRenderableWidget(baubleButton);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
        super.renderBg(graphics, partialTicks, x, y);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BAUBLE_BG);
        graphics.blit(BAUBLE_BG, leftPos + 85, topPos + 36, 0, 0, 165, 128);

        // 0 级和 1 级：只有前两层
        // 2 级，前四层
        // 3 级及以上，全部开放
        if (favorabilityLevel < 2) {
            graphics.fill(leftPos + 152, topPos + 81, leftPos + 240, topPos + 115, 0xaa222222);
            graphics.blit(BAUBLE_BG, leftPos + 190, topPos + 92, 165, 0, 11, 11);
        }
        if (favorabilityLevel < 3) {
            graphics.fill(leftPos + 152, topPos + 117, leftPos + 240, topPos + 151, 0xaa222222);
            graphics.blit(BAUBLE_BG, leftPos + 190, topPos + 127, 165, 0, 11, 11);
        }
    }

    @Override
    protected void renderAdditionTransTooltip(GuiGraphics graphics, int x, int y) {
        if (favorabilityLevel < 2) {
            if (leftPos + 152 <= x && x < leftPos + 240 && topPos + 81 <= y && y < topPos + 115) {
                graphics.renderTooltip(font, Component.translatable("gui.touhou_little_maid.bauble_button.need_favorability_level", 2), x, y);
            }
        }
        if (favorabilityLevel < 3) {
            if (leftPos + 152 <= x && x < leftPos + 240 && topPos + 117 <= y && y < topPos + 151) {
                graphics.renderTooltip(font, Component.translatable("gui.touhou_little_maid.bauble_button.need_favorability_level", 3), x, y);
            }
        }
    }
}
