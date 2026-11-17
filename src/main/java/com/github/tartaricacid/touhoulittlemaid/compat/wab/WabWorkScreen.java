package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.UUID;

/**
 * 女仆工作设置面板：正方形 300x300，配置挖矿/伐木的过滤矿物、多余方块、最远距离。
 * 选中了女仆时保存到她的 NBT，没选中（空魂符/设置页/暂停页）时写到全局模板。
 */
@OnlyIn(Dist.CLIENT)
public class WabWorkScreen extends Screen {
    private static final int PANEL = 300;
    private static final int PANEL_COLOR = 0xF0101010;
    private static final int BORDER_COLOR = 0xFF555555;
    private static final int TEXT_COLOR = 0xFFF2F2F2;
    private static final int DIM_COLOR = 0xFF9A9A9A;

    // 客户端记住上次填的值，重开面板不丢（与服务端 NBT 无关，纯显示用）
    private static String lastFilter = WabWorkConfig.DEFAULT_FILTER;
    private static boolean lastDropExtra = WabWorkConfig.DEFAULT_DROP_EXTRA;
    private static String lastDropBlock = WabWorkConfig.DEFAULT_DROP_BLOCK;
    private static int lastMaxRadius = WabWorkConfig.DEFAULT_MAX_RADIUS;
    private static String lastAction = "mine";

    private final UUID maid;
    private String action;
    private int maxRadius;

    private EditBox filterBox;
    private EditBox dropBlockBox;
    private Button modeButton;
    private Button dropToggleButton;

    private int left;
    private int top;

    /** 无女仆：配全局模板（设置页 / 暂停页 / 空魂符用）。 */
    public WabWorkScreen() {
        this((UUID) null, null);
    }

    /** 传 null / NIL_UUID 表示配全局模板。 */
    public WabWorkScreen(UUID maid) {
        this(maid, null);
    }

    public WabWorkScreen(UUID maid, String action) {
        super(Component.translatable("wab.work.title"));
        this.maid = maid == null ? Util.NIL_UUID : maid;
        this.action = action == null || action.isEmpty() ? lastAction : action;
        this.maxRadius = WabWorkConfig.clampRadius(lastMaxRadius);
    }

    @Override
    protected void init() {
        left = width / 2 - PANEL / 2;
        top = height / 2 - PANEL / 2;

        modeButton = addRenderableWidget(Button.builder(modeLabel(), b -> {
                    action = action.equals("mine") ? "chop" : "mine";
                    b.setMessage(modeLabel());
                }).bounds(left + 12, top + 48, 276, 20).build());

        filterBox = new EditBox(font, left + 12, top + 94, 276, 20,
                Component.translatable("wab.work.filter.hint"));
        filterBox.setMaxLength(128);
        filterBox.setValue(lastFilter);
        addRenderableWidget(filterBox);

        dropBlockBox = new EditBox(font, left + 12, top + 140, 196, 20,
                Component.translatable("wab.work.drop_block.hint"));
        dropBlockBox.setMaxLength(128);
        dropBlockBox.setValue(lastDropBlock);
        addRenderableWidget(dropBlockBox);

        dropToggleButton = addRenderableWidget(Button.builder(dropLabel(), b -> {
                    lastDropExtra = !lastDropExtra;
                    b.setMessage(dropLabel());
                }).bounds(left + 214, top + 140, 74, 20).build());

        addRenderableWidget(Button.builder(Component.literal("-"), b -> {
                    maxRadius = WabWorkConfig.clampRadius(maxRadius - 8);
                }).bounds(left + 12, top + 186, 24, 20).build());
        addRenderableWidget(Button.builder(Component.literal("+"), b -> {
                    maxRadius = WabWorkConfig.clampRadius(maxRadius + 8);
                }).bounds(left + 264, top + 186, 24, 20).build());

        addRenderableWidget(Button.builder(Component.translatable("wab.work.save"), b -> save())
                .bounds(left + 12, top + 256, 132, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("wab.work.close"), b -> onClose())
                .bounds(left + 156, top + 256, 132, 20).build());
    }

    private Component modeLabel() {
        return Component.translatable("wab.work.mode",
                Component.translatable(action.equals("mine") ? "wab.work.mode.mine" : "wab.work.mode.chop"));
    }

    private Component dropLabel() {
        return Component.translatable(lastDropExtra ? "wab.work.on" : "wab.work.off");
    }

    private void save() {
        String filter = filterBox.getValue().trim();
        String dropBlock = dropBlockBox.getValue().trim();
        if (dropBlock.isEmpty()) dropBlock = WabWorkConfig.DEFAULT_DROP_BLOCK;
        lastFilter = filter;
        lastDropBlock = dropBlock;
        lastMaxRadius = maxRadius;
        lastAction = action;
        PacketDistributor.sendToServer(new WabWorkSync(
                maid, action, filter, lastDropExtra, dropBlock, maxRadius));
        onClose();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.fill(left, top, left + PANEL, top + PANEL, PANEL_COLOR);
        graphics.fill(left, top, left + PANEL, top + 1, BORDER_COLOR);
        graphics.fill(left, top + PANEL - 1, left + PANEL, top + PANEL, BORDER_COLOR);
        graphics.fill(left, top, left + 1, top + PANEL, BORDER_COLOR);
        graphics.fill(left + PANEL - 1, top, left + PANEL, top + PANEL, BORDER_COLOR);

        graphics.drawCenteredString(font, Component.translatable("wab.work.title"), width / 2, top + 14, TEXT_COLOR);
        graphics.drawString(font, Component.translatable("wab.work.mode.label"), left + 12, top + 36, DIM_COLOR);
        graphics.drawString(font, Component.translatable("wab.work.filter"), left + 12, top + 82, DIM_COLOR);
        graphics.drawString(font, Component.translatable("wab.work.drop_extra"), left + 12, top + 128, DIM_COLOR);
        graphics.drawString(font, Component.translatable("wab.work.radius"), left + 12, top + 174, DIM_COLOR);
        graphics.drawCenteredString(font, Component.literal(String.valueOf(maxRadius)), width / 2, top + 192, TEXT_COLOR);
        graphics.drawCenteredString(font, Component.translatable("wab.work.maid",
                maid.equals(Util.NIL_UUID) ? Component.translatable("wab.work.maid.global") : Component.literal(maid.toString())),
                width / 2, top + 224, DIM_COLOR);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
