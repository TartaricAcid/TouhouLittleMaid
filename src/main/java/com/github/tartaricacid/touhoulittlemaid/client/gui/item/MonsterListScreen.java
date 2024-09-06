package com.github.tartaricacid.touhoulittlemaid.client.gui.item;

import com.github.tartaricacid.touhoulittlemaid.entity.misc.DefaultMonsterType;
import com.github.tartaricacid.touhoulittlemaid.entity.misc.MonsterType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class MonsterListScreen extends Screen {
    private final Map<EntityType<?>, MonsterType> monsterList;
    private int posX;
    private int posY;

    public MonsterListScreen(ItemStack stack) {
        super(Component.literal("Monster List Screen"));
        this.monsterList = DefaultMonsterType.getMonsterList(stack, Minecraft.getInstance().level);
    }

    @Override
    protected void init() {
        this.posX = this.width / 2;
        this.posY = this.height / 2;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);

        int i = 20;
        for (EntityType<?> type : this.monsterList.keySet()) {
            Component description = type.getDescription();
            MonsterType monsterType = this.monsterList.get(type);

            graphics.drawCenteredString(font, description, this.posX - 50, i, 0xFFFFFF);
            graphics.drawCenteredString(font, monsterType.name(), this.posX + 50, i, 0xFFFFFF);
            i = i + 10;
        }
    }
}
