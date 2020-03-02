package com.github.tartaricacid.touhoulittlemaid.client.gui.inventory;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.MaidInventoryContainer;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.SwitchMaidGuiMessage;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.Locale;

import static com.github.tartaricacid.touhoulittlemaid.network.MaidGuiHandler.MAIN_GUI.INVENTORY;

@SideOnly(Side.CLIENT)
public class MaidInventoryGuiContainer extends AbstractMaidGuiContainer {
    private static final ResourceLocation STORAGE_TEX = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/inventory_storage.png");
    /**
     * 当前滚动的位置，0 为顶部，1 为底部
     */
    private float currentScroll;
    private boolean needScroll;
    private int startRow;

    public MaidInventoryGuiContainer(InventoryPlayer inventory, EntityMaid maid, int taskIndex, int startRow) {
        super(new MaidInventoryContainer(inventory, maid, taskIndex, startRow), INVENTORY.getId());
        this.startRow = MathHelper.clamp(startRow, 0, maid.getBackLevel().getLevel() * 2);
        this.needScroll = this.maid.getBackLevel() != EntityMaid.EnumBackPackLevel.EMPTY;
        this.currentScroll = (float) (this.startRow * (1.0 / (maid.getBackLevel().getLevel() * 2)));
        this.currentScroll = MathHelper.clamp(this.currentScroll, 0, 1);
    }

    @Override
    public void drawCustomScreen(int mouseX, int mouseY, float partialTicks) {
    }

    @Override
    public void drawCustomTooltips(int mouseX, int mouseY, float partialTicks) {
        // 滚动物品栏描述
        boolean xInRange = (guiLeft + 174) < mouseX && mouseX < (guiLeft + 186);
        boolean yInRange = (guiTop + 8) < mouseY && mouseY < (guiTop + 60);
        if (needScroll && xInRange && yInRange) {
            this.drawHoveringText(I18n.format("gui.touhou_little_maid.inventory.scroll.desc"), mouseX, mouseY);
        }
    }

    @Override
    public void drawCustomBackground(int mouseX, int mouseY, float partialTicks) {
        mc.getTextureManager().bindTexture(STORAGE_TEX);
        drawTexturedModalRect(guiLeft + 79, guiTop, 0, 0, 115, 84);
        if (needScroll) {
            drawTexturedModalRect(guiLeft + 79 + 95, guiTop + 8 + 37 * currentScroll, 115, 0, 12, 15);
        } else {
            drawTexturedModalRect(guiLeft + 79 + 95, guiTop + 8, 127, 0, 12, 15);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int dWheel = Mouse.getEventDWheel();

        if (dWheel != 0 && needScroll) {
            // 向上滚
            if (dWheel > 0) {
                dWheel = 1;
            }
            // 向下滚
            if (dWheel < 0) {
                dWheel = -1;
            }
            int maxStartRow = maid.getBackLevel().getLevel() * 2;
            this.startRow = MathHelper.clamp(this.startRow - dWheel, 0, maxStartRow);
            this.currentScroll = (float) MathHelper.clamp(startRow * (1.0 / maxStartRow), 0.0, 1.0);
            CommonProxy.INSTANCE.sendToServer(new SwitchMaidGuiMessage(maid.getEntityId(),
                    BUTTON.INVENTORY.getGuiId(), container.taskIndex, this.startRow));
        }
    }

    @Override
    public int getRenderPotionStartXOffset() {
        return 18;
    }

    @Override
    public String getGuiName() {
        return INVENTORY.name().toLowerCase(Locale.US);
    }
}
