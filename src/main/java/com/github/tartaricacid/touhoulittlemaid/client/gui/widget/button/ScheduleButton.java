package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.MaidSchedule;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.MaidConfigMessage;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

public class ScheduleButton extends Button {
    private static final ResourceLocation BUTTON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_button.png");
    private final EntityMaid maid;
    private MaidSchedule mode;

    public ScheduleButton(int x, int y, EntityMaid maid) {
        super(x, y, 61, 13, StringTextComponent.EMPTY, (b) -> {
        });
        this.maid = maid;
        this.mode = maid.getSchedule();
    }

    @Override
    public void onPress() {
        int index = mode.ordinal() + 1;
        int length = MaidSchedule.values().length;
        this.mode = MaidSchedule.values()[index % length];
        NetworkHandler.CHANNEL.sendToServer(new MaidConfigMessage(maid.getId(), maid.isHomeModeEnable(), maid.isPickup(), maid.isRideable(), this.mode));
    }

    @Override
    public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {

    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bind(BUTTON);
        RenderSystem.enableDepthTest();
        blit(matrixStack, this.x, this.y, 82, 43 + 14 * mode.ordinal(), this.width, this.height, 256, 256);
        if (this.isHovered()) {
            this.renderToolTip(matrixStack, mouseX, mouseY);
        }
    }
}
