package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.network.message.SetBeaconPotionPackage;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.function.Consumer;

public class BeaconEffectButton extends TouhouStateSwitchButton {
    private static final ResourceLocation BG = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/gui/maid_beacon.png");
    private final TextureAtlasSprite sprite;
    private final Component tooltips;
    private final int potionIndex;
    private final BlockPos pos;
    private final Consumer<Boolean> onClick;

    public BeaconEffectButton(TileEntityMaidBeacon.BeaconEffect effect, int xIn, int yIn, int potionIndex, TileEntityMaidBeacon beacon, Consumer<Boolean> onClick) {
        super(xIn, yIn, 22, 22, potionIndex == effect.ordinal());
        this.initTextureValues(0, 111, 22, 22, BG);
        this.sprite = Minecraft.getInstance().getMobEffectTextures().get(effect.getEffect());
        this.tooltips = effect.getEffect().value().getDisplayName();
        this.potionIndex = effect.ordinal();
        this.pos = beacon.getBlockPos();
        this.onClick = onClick;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.isStateTriggered = !this.isStateTriggered;
        PacketDistributor.sendToServer(new SetBeaconPotionPackage(pos, isStateTriggered ? potionIndex : -1));
        this.onClick.accept(this.isStateTriggered);
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.renderWidget(graphics, mouseX, mouseY, partialTicks);
        graphics.blit(this.getX() + 2, this.getY() + 2, 0, 18, 18, this.sprite);
    }

    public void renderToolTip(GuiGraphics graphics, Screen screen, int pMouseX, int pMouseY) {
        if (this.isHovered) {
            graphics.renderTooltip(screen.getMinecraft().font, tooltips, pMouseX, pMouseY);
        }
    }
}