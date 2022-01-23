package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.SetBeaconPotionMessage;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ToggleWidget;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import java.util.function.Consumer;

public class BeaconEffectButton extends ToggleWidget {
    private static final ResourceLocation BG = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_beacon.png");
    private final TextureAtlasSprite sprite;
    private final int potionIndex;
    private final BlockPos pos;
    private final Consumer<Boolean> onClick;

    public BeaconEffectButton(TileEntityMaidBeacon.BeaconEffect effect, int xIn, int yIn, int potionIndex, TileEntityMaidBeacon beacon, Consumer<Boolean> onClick) {
        super(xIn, yIn, 22, 22, potionIndex == effect.ordinal());
        this.initTextureValues(0, 111, 22, 22, BG);
        this.sprite = Minecraft.getInstance().getMobEffectTextures().get(effect.getEffect());
        this.potionIndex = effect.ordinal();
        this.pos = beacon.getBlockPos();
        this.onClick = onClick;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.isStateTriggered = !this.isStateTriggered;
        NetworkHandler.CHANNEL.sendToServer(new SetBeaconPotionMessage(pos, isStateTriggered ? potionIndex : -1));
        this.onClick.accept(this.isStateTriggered);
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.renderButton(matrixStack, mouseX, mouseY, partialTicks);
        Minecraft.getInstance().getTextureManager().bind(this.sprite.atlas().location());
        blit(matrixStack, this.x + 2, this.y + 2, this.getBlitOffset(), 18, 18, this.sprite);
    }
}
