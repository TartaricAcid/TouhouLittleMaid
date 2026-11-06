package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.google.gson.JsonObject;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

/**
 * 详细信息分页屏：WAB 数值 + 模组数值全量展示（动作栏那一行放不下才开这一页）。
 * 行数据由服务器拼好（label 是语言键），这里只管排版翻页；滚轮和左右方向键都能翻。
 */
@OnlyIn(Dist.CLIENT)
public class WabDetailsScreen extends Screen {
    private static final int ROWS_PER_PAGE = 12;
    private static final int LABEL_COLOR = 0xFF909090;
    private static final int VALUE_COLOR = 0xFFF2F2F2;
    private static final int SECTION_COLOR = 0xFFFFD98A;
    private static final int RULE_COLOR = 0x33FFFFFF;

    private record Row(boolean section, Component label, Component value) {
    }

    private final List<Row> rows = new ArrayList<>();
    private final String name;
    private final String error;
    private int page;

    public WabDetailsScreen(JsonObject data) {
        super(Component.literal("WAB"));
        this.name = WabJson.str(data, "name");
        this.error = WabJson.str(data, "error");
        if (error.isEmpty()) {
            for (var element : WabJson.arr(data, "rows")) {
                if (!element.isJsonObject()) continue;
                JsonObject r = element.getAsJsonObject();
                String key = WabJson.str(r, "k");
                String value = WabJson.str(r, "v");
                rows.add(new Row(value.isEmpty(), Component.translatable(key), valueOf(value)));
            }
        }
    }

    /** 以 wab. 开头的值本身是语言键（是/否、尚无数据这类），其余按纯文本。 */
    private static Component valueOf(String value) {
        return value.startsWith("wab.") ? Component.translatable(value) : Component.literal(value);
    }

    @Override
    protected void init() {
        addRenderableWidget(Button.builder(Component.literal("<"), b -> turnPage(-1))
                .bounds(width / 2 - 72, height - 30, 20, 20).build());
        addRenderableWidget(Button.builder(Component.literal(">"), b -> turnPage(1))
                .bounds(width / 2 + 52, height - 30, 20, 20).build());
    }

    private int pageCount() {
        return Math.max(1, (rows.size() + ROWS_PER_PAGE - 1) / ROWS_PER_PAGE);
    }

    private void turnPage(int delta) {
        page = Math.max(0, Math.min(pageCount() - 1, page + delta));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        if (!error.isEmpty()) {
            graphics.drawCenteredString(font, Component.translatable(error), width / 2, height / 2 - 4, 0xFFFF9090);
            super.render(graphics, mouseX, mouseY, partialTick);
            return;
        }
        graphics.drawCenteredString(font, Component.literal(name), width / 2, 16, 0xFFFFFFFF);
        int left = width / 2 - 132;
        int right = width / 2 + 132;
        int start = page * ROWS_PER_PAGE;
        int end = Math.min(rows.size(), start + ROWS_PER_PAGE);
        for (int i = start; i < end; i++) {
            Row row = rows.get(i);
            int y = 42 + (i - start) * 17;
            if (row.section()) {
                graphics.drawCenteredString(font, row.label(), width / 2, y, SECTION_COLOR);
                graphics.fill(left, y + 12, right, y + 13, RULE_COLOR);
            } else {
                graphics.drawString(font, row.label(), left, y, LABEL_COLOR);
                Component value = trim(row.value(), 160);
                graphics.drawString(font, value, right - font.width(value), y, VALUE_COLOR);
            }
        }
        graphics.drawCenteredString(font, Component.literal((page + 1) + " / " + pageCount()),
                width / 2, height - 24, 0xFF808080);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private Component trim(Component value, int maxWidth) {
        String text = value.getString();
        if (font.width(text) <= maxWidth) return value;
        return Component.literal(font.plainSubstrByWidth(text, maxWidth - font.width("...")) + "...");
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_LEFT) {
            turnPage(-1);
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_RIGHT) {
            turnPage(1);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        turnPage(delta < 0 ? 1 : -1);
        return true;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
