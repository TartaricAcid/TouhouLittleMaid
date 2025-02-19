package com.github.tartaricacid.touhoulittlemaid.compat.ysm.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.compat.ysm.data.YsmMaidInfo;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MiscConfig;
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
    private final YsmMaidInfo ysmMaidInfo;
    private final boolean needAuth;
    private final String modelId;
    private final String modelTexture;
    private final List<Component> tooltips;
    private final ResourceLocation cacheIconId;
    @Nullable
    private EntityMaid maid;
    private boolean createdMaid = false;

    public YsmModelButton(YsmMaidInfo ysmModel, int pX, int pY, int pWidth, int pHeight, Component pMessage, OnPress pOnPress) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress, Supplier::get);
        this.ysmMaidInfo = ysmModel;
        this.needAuth = ysmModel.needAuth();
        this.modelId = ysmModel.modelId();
        this.modelTexture = ysmModel.textureId();
        this.tooltips = buildTooltips(ysmModel.tooltips());
        this.cacheIconId = ysmModel.cacheIconId();
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
        }
        drawYsmEntity(pGuiGraphics, getX() + (width / 2), getY() + this.height);
    }

    public void renderTooltip(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (isHovered) {
            pGuiGraphics.renderComponentTooltip(Minecraft.getInstance().font, this.tooltips, pMouseX, pMouseY);
        }
    }

    private void drawYsmEntity(GuiGraphics graphics, int posX, int posY) {
        var allTextures = Minecraft.getInstance().textureManager.byPath;
        if (MiscConfig.MODEL_ICON_CACHE.get() && cacheIconId != null && allTextures.containsKey(cacheIconId)) {
            int textureSize = 24;
            graphics.blit(cacheIconId, posX - textureSize / 2, posY - textureSize, textureSize, textureSize, 0, 0, textureSize, textureSize, textureSize, textureSize);
        } else {
            if (!createdMaid) {
                this.createEntityMaid();
                createdMaid = true;
            }

            if (maid == null) {
                return;
            }
            InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, posX, posY, (int) (12 * 1.0f), -25, -20, maid);
        }
    }

    @Override
    protected boolean clicked(double pMouseX, double pMouseY) {
        return !needAuth && super.clicked(pMouseX, pMouseY);
    }

    private void createEntityMaid() {
        Level world = Minecraft.getInstance().level;
        if (world == null) {
            return;
        }

        EntityMaid maid = new EntityMaid(world);
        clearMaidDataResidue(maid, false);
        maid.setIsYsmModel(true);
        maid.setYsmModel(this.modelId, this.modelTexture);

        this.maid = maid;
    }
}
