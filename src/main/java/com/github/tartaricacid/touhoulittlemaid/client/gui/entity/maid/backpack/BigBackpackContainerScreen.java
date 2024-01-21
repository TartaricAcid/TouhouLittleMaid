package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.backpack;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack.BigBackpackContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.anti_ad.mc.ipn.api.IPNButton;
import org.anti_ad.mc.ipn.api.IPNGuiHint;
import org.anti_ad.mc.ipn.api.IPNPlayerSideOnly;

@IPNPlayerSideOnly
@IPNGuiHint(button = IPNButton.SORT, horizontalOffset = -36, bottom = -12)
@IPNGuiHint(button = IPNButton.SORT_COLUMNS, horizontalOffset = -24, bottom = -24)
@IPNGuiHint(button = IPNButton.SORT_ROWS, horizontalOffset = -12, bottom = -36)
@IPNGuiHint(button = IPNButton.SHOW_EDITOR, horizontalOffset = -5)
@IPNGuiHint(button = IPNButton.SETTINGS, horizontalOffset = -5)
public class BigBackpackContainerScreen extends AbstractMaidContainerGui<BigBackpackContainer> implements IBackpackContainerScreen {
    private static final ResourceLocation BACKPACK = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_backpack.png");
    private final EntityMaid maid;

    public BigBackpackContainerScreen(BigBackpackContainer container, PlayerInventory inv, ITextComponent titleIn) {
        super(container, inv, titleIn);
        this.imageHeight = 256;
        this.imageWidth = 256;
        this.maid = menu.getMaid();
    }


    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        super.renderBg(matrixStack, partialTicks, x, y);
        getMinecraft().textureManager.bind(BACKPACK);
        blit(matrixStack, leftPos + 85, topPos + 36, 0, 0, 165, 128);
    }
}
