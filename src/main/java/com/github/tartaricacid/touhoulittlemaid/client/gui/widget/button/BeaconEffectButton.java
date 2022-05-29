package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.SetBeaconPotionMessage;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.StateSwitchingButton;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class BeaconEffectButton extends StateSwitchingButton {
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
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        super.renderButton(poseStack, mouseX, mouseY, partialTicks);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, this.sprite.atlas().location());
        blit(poseStack, this.x + 2, this.y + 2, this.getBlitOffset(), 18, 18, this.sprite);
    }
}
