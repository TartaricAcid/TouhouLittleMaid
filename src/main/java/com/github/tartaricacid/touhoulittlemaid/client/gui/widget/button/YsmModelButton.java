package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.model.MaidModelGui;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

import static com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil.clearMaidDataResidue;

public class YsmModelButton extends Button {
    private static final ResourceLocation BG = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/skin_select.png");
    @Nullable
    private final EntityMaid maid;
    private final MaidModelGui.YsmMaidInfo ysmMaidInfo;
    private final String modelId;
    private final String modelTexture;
    private final List<Component> tooltips;

    public YsmModelButton(MaidModelGui.YsmMaidInfo ysmModel, int pX, int pY, int pWidth, int pHeight, Component pMessage, OnPress pOnPress) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress, Supplier::get);
        this.ysmMaidInfo = ysmModel;
        this.modelId = ysmModel.modelId();
        this.modelTexture = ysmModel.textureId();
        this.tooltips = buildTooltips(ysmModel.tooltips());
        this.maid = createEntityMaid();
    }

    private List<Component> buildTooltips(List<Component> tooltips) {
        List<Component> modelTooltips = Lists.newArrayList(tooltips);
        modelTooltips.add(Component.empty());
        modelTooltips.add(Component.translatable("gui.touhou_little_maid.skin.tooltips.show_details")
                .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));

        return modelTooltips;
    }

    @Override
    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (isHovered) {
            pGuiGraphics.blit(BG, getX(), getY(), 42, 225, 15, 24);
            pGuiGraphics.renderComponentTooltip(Minecraft.getInstance().font, this.tooltips, pMouseX, pMouseY);
        }
        drawYsmEntity(pGuiGraphics, getX() + (width / 2), getY() + this.height);
    }

    private void drawYsmEntity(GuiGraphics graphics, int posX, int posY) {
        if (maid == null) return;
        InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, posX, posY, (int) (12 * 1.0f), -25, -20, maid);
    }

    @Nullable
    private EntityMaid createEntityMaid() {
        Level world = Minecraft.getInstance().level;
        if (world == null) {
            return null;
        }

        EntityMaid maid = new EntityMaid(world);
        clearMaidDataResidue(maid, false);
        maid.setIsYsmModel(true);
        maid.setYsmModel(this.modelId, this.modelTexture);
        return maid;
    }
}
