package com.github.tartaricacid.touhoulittlemaid.client.gui.compass;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.item.ItemKappaCompass;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.SetCompassModeMessage;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Locale;

import static com.github.tartaricacid.touhoulittlemaid.item.ItemKappaCompass.Mode.*;

@SideOnly(Side.CLIENT)
public class GuiKappaCompass extends GuiScreen {
    private static final ResourceLocation BG = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/compass_background.png");
    List<BlockPos> blockPosList;
    ItemKappaCompass.Mode currentMode;

    public GuiKappaCompass(ItemStack compass) {
        this.blockPosList = ItemKappaCompass.getPos(compass);
        this.currentMode = ItemKappaCompass.getMode(compass);
    }

    @Override
    public void initGui() {
        int middleX = this.width / 2;
        int middleY = this.height / 2;
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(SINGLE_POINT.ordinal(), middleX - 117, middleY - 63, 100, 20,
                I18n.format(String.format("compass.touhou_little_maid.mode.%s", SINGLE_POINT.name().toLowerCase(Locale.US)))));
        this.buttonList.add(new GuiButton(MULTI_POINT_REENTRY.ordinal(), middleX - 117, middleY - 42, 100, 20,
                I18n.format(String.format("compass.touhou_little_maid.mode.%s", MULTI_POINT_REENTRY.name().toLowerCase(Locale.US)))));
        this.buttonList.add(new GuiButton(MULTI_POINT_CLOSURE.ordinal(), middleX - 117, middleY - 21, 100, 20,
                I18n.format(String.format("compass.touhou_little_maid.mode.%s", MULTI_POINT_CLOSURE.name().toLowerCase(Locale.US)))));
        this.buttonList.add(new GuiButton(SET_RANGE.ordinal(), middleX - 117, middleY, 100, 20,
                I18n.format(String.format("compass.touhou_little_maid.mode.%s", SET_RANGE.name().toLowerCase(Locale.US)))));
        this.buttonList.add(new GuiButton(-1, middleX - 117, middleY + 42, 234, 20,
                I18n.format("gui.touhou_little_maid.compass.close")));
        this.enableCurrentModeButton();
    }

    private void enableCurrentModeButton() {
        if (currentMode != NONE) {
            for (GuiButton button : buttonList) {
                if (button.id == currentMode.ordinal()) {
                    button.enabled = false;
                    return;
                }
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int middleX = this.width / 2;
        int middleY = this.height / 2;
        drawDefaultBackground();
        mc.renderEngine.bindTexture(BG);
        drawTexturedModalRect(middleX - 124, middleY - 71, 0, 0, 248, 142);
        drawPosInfo(middleX, middleY);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawPosInfo(int middleX, int middleY) {
        switch (currentMode) {
            default:
            case NONE:
                return;
            case SINGLE_POINT:
                if (blockPosList.size() > 0) {
                    BlockPos pos = blockPosList.get(blockPosList.size() - 1);
                    fontRenderer.drawString(String.format("A: [%d, %d, %d]", pos.getX(), pos.getY(), pos.getZ()),
                            middleX - 5, middleY - 60, 0xff000000, false);
                }
                return;
            case SET_RANGE:
                if (blockPosList.size() > 0) {
                    BlockPos pos = blockPosList.get(blockPosList.size() - 1);
                    fontRenderer.drawString(String.format("A: [%d, %d, %d]", pos.getX(), pos.getY(), pos.getZ()),
                            middleX - 5, middleY - 60, 0xff000000, false);
                }
                if (blockPosList.size() > 1) {
                    BlockPos pos = blockPosList.get(blockPosList.size() - 2);
                    fontRenderer.drawString(String.format("B: [%d, %d, %d]", pos.getX(), pos.getY(), pos.getZ()),
                            middleX - 5, middleY - 50, 0xff000000, false);
                }
                return;
            case MULTI_POINT_REENTRY:
            case MULTI_POINT_CLOSURE:
                for (int i = 0; i < blockPosList.size(); i++) {
                    BlockPos pos = blockPosList.get(i);
                    fontRenderer.drawString(String.format("%s: [%d, %d, %d]", (char) (i + 65), pos.getX(), pos.getY(), pos.getZ()),
                            middleX - 5, middleY - 60 + i * 10, 0xff000000, false);
                }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (0 <= button.id && button.id < ItemKappaCompass.Mode.values().length) {
            this.currentMode = ItemKappaCompass.Mode.getModeByIndex(button.id);
            this.initGui();
            CommonProxy.INSTANCE.sendToServer(new SetCompassModeMessage(this.currentMode));
            return;
        }
        if (button.id == -1) {
            mc.addScheduledTask(() -> mc.displayGuiScreen(null));
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
