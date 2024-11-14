package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.MonsterListButton;
import com.github.tartaricacid.touhoulittlemaid.entity.data.inner.AttackListData;
import com.github.tartaricacid.touhoulittlemaid.entity.misc.MonsterType;
import com.github.tartaricacid.touhoulittlemaid.init.InitTaskData;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.task.TaskConfigContainer;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.SetAttackListMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.registries.ForgeRegistries;
import org.anti_ad.mc.ipn.api.IPNButton;
import org.anti_ad.mc.ipn.api.IPNGuiHint;
import org.anti_ad.mc.ipn.api.IPNPlayerSideOnly;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@IPNPlayerSideOnly
@IPNGuiHint(button = IPNButton.SORT, horizontalOffset = -36, bottom = -12)
@IPNGuiHint(button = IPNButton.SORT_COLUMNS, horizontalOffset = -24, bottom = -24)
@IPNGuiHint(button = IPNButton.SORT_ROWS, horizontalOffset = -12, bottom = -36)
@IPNGuiHint(button = IPNButton.SHOW_EDITOR, horizontalOffset = -5)
@IPNGuiHint(button = IPNButton.SETTINGS, horizontalOffset = -5)
public class AttackTaskConfigGui extends MaidTaskConfigGui<TaskConfigContainer> {
    private static final ResourceLocation BG = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/attack_task_config.png");

    private final Map<ResourceLocation, MonsterType> attackGroups;
    private final List<ResourceLocation> attackGroupsKey;
    private EditBox inputField;
    private int page = 0;

    public AttackTaskConfigGui(TaskConfigContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.attackGroups = Objects.requireNonNullElse(this.getMaid().getData(InitTaskData.ATTACK_LIST), AttackListData.empty()).attackGroups();
        this.attackGroupsKey = Lists.newArrayList();
        this.sortKey();
    }

    private void sortKey() {
        this.attackGroupsKey.clear();

        List<ResourceLocation> hostile = Lists.newArrayList();
        List<ResourceLocation> neutral = Lists.newArrayList();
        List<ResourceLocation> friendly = Lists.newArrayList();

        for (ResourceLocation id : attackGroups.keySet()) {
            if (attackGroups.get(id) == MonsterType.HOSTILE) {
                hostile.add(id);
            }
            if (attackGroups.get(id) == MonsterType.NEUTRAL) {
                neutral.add(id);
            }
            if (attackGroups.get(id) == MonsterType.FRIENDLY) {
                friendly.add(id);
            }
        }

        attackGroupsKey.addAll(hostile);
        attackGroupsKey.addAll(neutral);
        attackGroupsKey.addAll(friendly);

        this.page = Mth.clamp(this.page, 0, (this.attackGroupsKey.size() - 1) / 7);
    }

    @Override
    protected void initAdditionWidgets() {
        int startLeft = leftPos + 87;
        int startTop = topPos + 36;

        this.inputField = new EditBox(this.font, startLeft, startTop, 117, 16, Component.literal("Monster List"));
        this.inputField.setMaxLength(256);
        this.addWidget(this.inputField);

        this.addRenderableWidget(Button.builder(Component.translatable("gui.touhou_little_maid.monster_type.add"), b -> addMonsterType())
                .pos(startLeft + 119, startTop - 1).size(44, 18).build());

        this.addRenderableWidget(new ImageButton(startLeft + 121, startTop + 20, 5, 9, 0, 176, 9, BG, b -> {
            this.page = this.page - 1;
            this.page = Mth.clamp(this.page, 0, (this.attackGroupsKey.size() - 1) / 7);
            this.init();
        }));
        this.addRenderableWidget(new ImageButton(startLeft + 156, startTop + 20, 5, 9, 5, 176, 9, BG, b -> {
            this.page = this.page + 1;
            this.page = Mth.clamp(this.page, 0, (this.attackGroupsKey.size() - 1) / 7);
            this.init();
        }));

        for (int i = 0; i < 7; i++) {
            int index = page * 7 + i;
            if (index >= attackGroupsKey.size()) {
                return;
            }
            ResourceLocation id = attackGroupsKey.get(index);
            EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(id);
            if (type == null) {
                continue;
            }
            Component name = type.getDescription();
            int yOffset = startTop + 31 + 13 * i;
            this.addRenderableWidget(new MonsterListButton(name, startLeft - 1, yOffset, id, this));
        }
    }

    private void addMonsterType() {
        String value = this.inputField.getValue();
        if (StringUtils.isBlank(value)) {
            return;
        }
        if (!ResourceLocation.isValidResourceLocation(value)) {
            return;
        }
        ResourceLocation id = ResourceLocation.tryParse(value);
        if (ForgeRegistries.ENTITY_TYPES.containsKey(id)) {
            this.attackGroups.put(id, MonsterType.NEUTRAL);
            this.sortKey();
            super.init();
        }
    }

    public void removeMonsterType(ResourceLocation id) {
        this.attackGroups.remove(id);
        this.sortKey();
        super.init();
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        String value = this.inputField.getValue();
        super.resize(minecraft, width, height);
        this.inputField.setValue(value);
    }

    @Override
    protected void renderAddition(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.inputField.render(graphics, mouseX, mouseY, partialTicks);

        MutableComponent pageText = Component.literal(String.format("%d/%d", this.page + 1, (this.attackGroupsKey.size() - 1) / 7 + 1));
        graphics.drawCenteredString(font, pageText, leftPos + 228, topPos + 57, 0xFFFFFF);
        graphics.drawCenteredString(font, Component.translatable("gui.touhou_little_maid.monster_type.title"), leftPos + 147, topPos + 57, 0xFFFFFF);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.inputField.tick();
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
        super.renderBg(graphics, partialTicks, x, y);
        graphics.blit(BG, leftPos + 80, topPos + 28, 0, 0, imageWidth, 137);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE && this.getMinecraft().player != null) {
            this.getMinecraft().player.closeContainer();
        }
        return this.inputField.keyPressed(keyCode, scanCode, modifiers) || this.inputField.canConsumeInput() || super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void onClose() {
        NetworkHandler.CHANNEL.sendToServer(new SetAttackListMessage(this.getMaid().getId(), this.attackGroups));
        super.onClose();
    }

    public Map<ResourceLocation, MonsterType> getAttackGroups() {
        return attackGroups;
    }
}