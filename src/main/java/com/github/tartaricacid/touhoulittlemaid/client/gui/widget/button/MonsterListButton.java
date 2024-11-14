package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.task.AttackTaskConfigGui;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class MonsterListButton extends Button {
    private static final ResourceLocation ICON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/attack_task_config.png");
    private final AttackTaskConfigGui parents;
    private final ResourceLocation entityId;

    public MonsterListButton(Component entityName, int x, int y, ResourceLocation entityId, AttackTaskConfigGui parents) {
        super(Button.builder(entityName, b -> {
        }).pos(x, y).size(164, 13));
        this.parents = parents;
        this.entityId = entityId;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float pPartialTick) {
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.enableDepthTest();
        if (deleteClick(mouseX, mouseY)) {
            graphics.blit(ICON, this.getX(), this.getY(), 0, 163, this.width, this.height, 256, 256);
        } else if (leftClick(mouseX, mouseY) || rightClick(mouseX, mouseY)) {
            graphics.blit(ICON, this.getX(), this.getY(), 0, 150, this.width, this.height, 256, 256);
        } else {
            graphics.blit(ICON, this.getX(), this.getY(), 0, 137, this.width, this.height, 256, 256);
        }
        graphics.drawString(mc.font, this.getMessage(), this.getX() + 5, this.getY() + 3, 0x444444, false);
        graphics.drawCenteredString(mc.font, this.parents.getAttackGroups().get(entityId).getComponent(), this.getX() + 142, this.getY() + 3, 0xFFFFFF);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (deleteClick(mouseX, mouseY)) {
            this.parents.removeMonsterType(this.entityId);
        } else if (leftClick(mouseX, mouseY)) {
            this.parents.getAttackGroups().computeIfPresent(this.entityId, (k, monsterType) -> monsterType.getPrevious());
        } else if (rightClick(mouseX, mouseY)) {
            this.parents.getAttackGroups().computeIfPresent(this.entityId, (k, monsterType) -> monsterType.getNext());
        }
    }

    private boolean deleteClick(double mouseX, double mouseY) {
        boolean clickY = this.getY() <= mouseY && mouseY <= (this.getY() + this.getHeight());
        boolean deleteClickX = (this.getX() + 107) <= mouseX && mouseX <= (this.getX() + 120);
        return clickY && deleteClickX;
    }

    private boolean leftClick(double mouseX, double mouseY) {
        boolean clickY = this.getY() <= mouseY && mouseY <= (this.getY() + this.getHeight());
        boolean leftClickX = (this.getX() + 120) <= mouseX && mouseX <= (this.getX() + 130);
        return clickY && leftClickX;
    }

    private boolean rightClick(double mouseX, double mouseY) {
        boolean clickY = this.getY() <= mouseY && mouseY <= (this.getY() + this.getHeight());
        boolean rightClickX = (this.getX() + 154) <= mouseX && mouseX <= (this.getX() + 164);
        return clickY && rightClickX;
    }
}
