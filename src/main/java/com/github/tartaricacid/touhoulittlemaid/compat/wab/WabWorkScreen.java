package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.UUID;

/**
 * 女仆工作设置：正方形面板，可在没有选中女仆时打开（改全局模板），
 * 也可带某个女仆 UUID 打开（保存时写该女仆 NBT）。所有业务逻辑走 WabWorkSync 到服务端。
 */
@OnlyIn(Dist.CLIENT)
public class WabWorkScreen extends Screen {
    private static final int PANEL_W = 300;
    private static final int PANEL_H = 300;
    private static final int PANEL_BG = 0xF0101010;
    private static final int TEXT = 0xFFFFFFFF;
    private static final int LABEL = 0xFFB0B0B0;

    private final UUID maid;
    private String action;
    private boolean dropExtra;
    private int maxRadius;

    private EditBox filterBox;
    private EditBox dropBlockBox;
    private Button dropExtraButton;
    private Button modeButton;

    private int left;
    private int top;

    public WabWorkScreen() {
        this(Util.NIL_UUID);
    }

    public WabWorkScreen(UUID maid) {
        super(Component.translatable("wab.work.title"));
        this.maid = maid == null ? Util.NIL_UUID : maid;
        this.action = WabWorkConfig.getTemplateAction();
        this.dropExtra = WabWorkConfig.getTemplateDropExtra();
        this.maxRadius = WabWorkConfig.getTemplateMaxRadius();
    }

    /** 三个入口统一走这里：空魂符 / WAB 设置按钮 / 暂停页按钮。 */
    public static void open() {
        open(Util.NIL_UUID);
    }

    public static void open(UUID maid) {
        try {
            Minecraft.getInstance().setScreen(new WabWorkScreen(maid));
        } catch (Exception ignored) {
            // 打开设置失败绝不影响原功能
        }
    }

    @Override
    protected void init() {
        left = Math.max(0, (width - PANEL_W) / 2);
        top = Math.max(0, (height - PANEL_H) / 2);

        filterBox = new EditBox(font, left + 10, top + 54, PANEL_W - 20, 20,
                Component.translatable("wab.work.filter.hint"));
        filterBox.setMaxLength(128);
        filterBox.setValue(WabWorkConfig.getTemplateFilter());
        addRenderableWidget(filterBox);

        dropBlockBox = new EditBox(font, left + 10, top + 100, PANEL_W - 20, 20,
                Component.translatable("wab.work.drop_block.hint"));
        dropBlockBox.setMaxLength(128);
        dropBlockBox.setValue(WabWorkConfig.getTemplateDropBlock());
        addRenderableWidget(dropBlockBox);

        dropExtraButton = Button.builder(dropExtraLabel(), b -> {
            dropExtra = !dropExtra;
            b.setMessage(dropExtraLabel());
        }).bounds(left + 10, top + 132, 135, 20).build();
        addRenderableWidget(dropExtraButton);

        modeButton = Button.builder(modeLabel(), b -> {
            action = "chop".equals(action) ? "mine" : "chop";
            b.setMessage(modeLabel());
        }).bounds(left + PANEL_W - 145, top + 132, 135, 20).build();
        addRenderableWidget(modeButton);

        addRenderableWidget(Button.builder(Component.literal("-"), b -> {
            maxRadius = WabWorkConfig.clampRadius(maxRadius - 8);
        }).bounds(left + 10, top + 178, 26, 20).build());
        addRenderableWidget(Button.builder(Component.literal("+"), b -> {
            maxRadius = WabWorkConfig.clampRadius(maxRadius + 8);
        }).bounds(left + PANEL_W - 36, top + 178, 26, 20).build());

        addRenderableWidget(Button.builder(Component.translatable("wab.work.save"), b -> save())
                .bounds(left + 10, top + 250, 135, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("wab.work.close"), b -> onClose())
                .bounds(left + PANEL_W - 145, top + 250, 135, 20).build());
    }

    private Component dropExtraLabel() {
        return Component.translatable("wab.work.drop_extra").append(Component.literal(": "))
                .append(Component.translatable(dropExtra ? "wab.work.on" : "wab.work.off"));
    }

    private Component modeLabel() {
        return Component.translatable("wab.work.mode").append(Component.literal(": "))
                .append(Component.translatable("chop".equals(action) ? "wab.work.mode_chop" : "wab.work.mode_mine"));
    }

    private void save() {
        String filter = filterBox == null ? "" : filterBox.getValue();
        String dropBlock = dropBlockBox == null ? "" : dropBlockBox.getValue();
        try {
            NetworkHandler.CHANNEL.sendToServer(
                    new WabWorkSync(maid, action, filter, dropExtra, dropBlock, maxRadius));
        } catch (Exception ignored) {
            // 网络失败也只是设置没写进去，不抛给玩家
        }
        onClose();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        graphics.fill(left, top, left + PANEL_W, top + PANEL_H, PANEL_BG);
        graphics.drawCenteredString(font, Component.translatable("wab.work.title"), width / 2, top + 12, TEXT);

        graphics.drawString(font, Component.translatable("wab.work.filter"), left + 10, top + 40, LABEL);
        graphics.drawString(font, Component.translatable("wab.work.drop_block"), left + 10, top + 86, LABEL);
        graphics.drawCenteredString(font, Component.translatable("wab.work.max_radius"), width / 2, top + 158, LABEL);
        graphics.drawCenteredString(font, Component.literal(String.valueOf(maxRadius)), width / 2, top + 181, TEXT);

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
