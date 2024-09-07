package com.github.tartaricacid.touhoulittlemaid.client.gui.item;

import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.FlatColorButton;
import com.github.tartaricacid.touhoulittlemaid.entity.misc.DefaultMonsterType;
import com.github.tartaricacid.touhoulittlemaid.entity.misc.MonsterType;
import com.github.tartaricacid.touhoulittlemaid.network.message.SetMonsterListMessage;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.Map;

public class MonsterListScreen extends Screen {
    private static final int PER_MAX_COUNT = 10;

    private final Map<EntityType<?>, MonsterType> monsterList;
    private final List<EntityType<?>> monsterListIndex;

    private int page = 0;
    private int posX;
    private int posY;

    public MonsterListScreen(ItemStack stack) {
        super(Component.literal("Monster List Screen"));
        this.monsterList = DefaultMonsterType.getMonsterList(stack, Minecraft.getInstance().level);
        this.monsterListIndex = this.monsterList.keySet().stream().toList();
    }

    @Override
    protected void init() {
        this.clearWidgets();

        this.posX = this.width / 2;
        this.posY = this.height / 2;

        int startY = posY - 16 * PER_MAX_COUNT / 2 - 8;
        int size = this.monsterListIndex.size();
        for (int i = 0; i < PER_MAX_COUNT; i++) {
            final int index = page * PER_MAX_COUNT + i;
            if (index >= size) {
                break;
            }

            EntityType<?> entityType = this.monsterListIndex.get(index);
            MonsterType monsterType = this.monsterList.get(entityType);
            Component text = monsterType.getComponent()
                    .copy()
                    .append(CommonComponents.SPACE)
                    .append(entityType.getDescription());

            FlatColorButton button = new FlatColorButton(posX - 100, startY, 200, 14, text, b -> {
                MonsterType next = monsterType.getNext();
                this.monsterList.put(entityType, next);
                this.init();
            });

            this.addRenderableWidget(button);
            startY += 16;
        }

        startY = posY + 16 * PER_MAX_COUNT / 2 - 8;
        this.addRenderableWidget(new FlatColorButton(posX - 100, startY, 28, 14, Component.literal("<"), b -> {
            if (this.page > 0) {
                this.page--;
                this.init();
            }
        }));

        this.addRenderableWidget(new FlatColorButton(posX + 100 - 28, startY, 28, 14, Component.literal(">"), b -> {
            int nextStartIndex = (this.page + 1) * PER_MAX_COUNT;
            if (nextStartIndex < this.monsterListIndex.size()) {
                this.page++;
                this.init();
            }
        }));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);

        String pageText = String.format("%d/%d", page + 1, this.monsterListIndex.size() / PER_MAX_COUNT + 1);
        int startY = posY + 16 * PER_MAX_COUNT / 2 - 5;
        graphics.drawCenteredString(font, pageText, this.posX, startY, 0xFFFFFF);
    }

    @Override
    public void onClose() {
        Map<ResourceLocation, MonsterType> monsterListOutput = Maps.newHashMap();
        this.monsterList.forEach((type, monsterType) -> {
            ResourceLocation key = BuiltInRegistries.ENTITY_TYPE.getKey(type);
            monsterListOutput.put(key, monsterType);
        });
        PacketDistributor.sendToServer(new SetMonsterListMessage(monsterListOutput));
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
