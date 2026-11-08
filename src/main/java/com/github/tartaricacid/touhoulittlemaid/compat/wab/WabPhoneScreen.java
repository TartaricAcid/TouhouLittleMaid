package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

/**
 * 纯客户端手机界面：左联系人、右消息、底部发送、顶部状态/加微信/刷新。
 * 所有业务数据都来自 WabPhoneData 回执（宿主算），这里只转发请求 + 渲染，不落任何业务逻辑。
 * 布局全部按 width/height 在 init() 里算，避免小窗口越界。
 */
@OnlyIn(Dist.CLIENT)
public class WabPhoneScreen extends Screen {
    private static final int PANEL_W = 460;
    private static final int PANEL_H = 340;
    private static final int ROW_H = 14;
    private static final int MSG_ROW_H = 16;
    private static final int PANEL = 0xF0181818;
    private static final int LIST_BG = 0xE0101010;
    private static final int SELECTED = 0x1F55FF55;
    private static final int HOVER = 0xFF3C3C3C;
    private static final int TEXT = 0xFFF2F2F2;
    private static final int DIM = 0xFF9A9A9A;
    private static final int ERROR = 0xFFFF5555;

    private record Contact(String characterId, String name, String lastMessage, boolean unread, String lastMessageAt) {
    }

    private record Msg(String id, String text, boolean isUser, String senderName, String at) {
    }

    private final String id;
    private final String initialAction;
    private final List<Contact> contacts = new ArrayList<>();
    private final List<Msg> messages = new ArrayList<>();

    private int selected = -1;
    private int contactScroll;
    private int messageScroll;
    private String status = "";
    private boolean statusError;
    private String characterId = "";
    private String characterName = "";
    private String lastSentText = "";
    private boolean requestedWhoami;

    private EditBox input;
    private EditBox addFriendInput;

    // init() 里按窗口算出来的布局
    private int left, top, right, bottom;
    private int contactsLeft, contactsRight, contactsTop, contactsBottom;
    private int msgLeft, msgRight, msgTop, msgBottom;

    public WabPhoneScreen(String id, String initialAction, String initialPayload) {
        super(Component.translatable("wab.phone.title"));
        this.id = id == null ? "" : id;
        this.initialAction = initialAction == null ? "contacts" : initialAction;
        // 打开时先显示「正在读取…」；桥没连就直接给「WAB 未连接」，不留空白。
        this.status = Component.translatable("wab.phone.loading").getString();
        if (!WabClientState.connected()) {
            this.status = Component.translatable("wab.phone.not_connected").getString();
            this.statusError = true;
        }
    }

    @Override
    protected void init() {
        int pw = Math.max(1, Math.min(width - 16, PANEL_W));
        int ph = Math.max(1, Math.min(height - 16, PANEL_H));
        left = Math.max(0, (width - pw) / 2);
        top = Math.max(0, (height - ph) / 2);
        right = Math.min(width, left + pw);
        bottom = Math.min(height, top + ph);

        int line1 = top + 2;
        int line3 = top + 46;
        int mainTop = top + 68;
        int mainBottom = Math.max(mainTop + 8, bottom - 30);

        int btnX = Math.max(left, right - 56);
        addRenderableWidget(Button.builder(
                        Component.translatable("wab.phone.refresh"), b -> request("contacts", new JsonObject()))
                .bounds(btnX, line1, 50, 18).build());

        int addW = Math.max(60, pw / 2 - 60);
        addFriendInput = new EditBox(font, left + 6, line3, addW, 18,
                Component.translatable("wab.phone.add_friend_hint"));
        addFriendInput.setMaxLength(64);
        addRenderableWidget(addFriendInput);
        addRenderableWidget(Button.builder(
                        Component.translatable("wab.phone.add_friend"), b -> doAddFriend())
                .bounds(left + 6 + addW + 4, line3, 50, 18).build());

        int inputW = Math.max(60, pw - 12 - 56);
        input = new EditBox(font, left + 6, bottom - 26, inputW, 20,
                Component.translatable("wab.phone.input_hint"));
        input.setMaxLength(256);
        addRenderableWidget(input);
        setInitialFocus(input);
        addRenderableWidget(Button.builder(
                        Component.translatable("wab.phone.send"), b -> doSend())
                .bounds(btnX, bottom - 26, 50, 20).build());

        int mid = left + 6 + Math.min(120, pw / 3);
        contactsLeft = left + 6;
        contactsRight = Math.max(contactsLeft + 40, mid - 4);
        contactsTop = mainTop;
        contactsBottom = mainBottom;
        msgLeft = contactsRight + 8;
        msgRight = right - 6;
        msgTop = mainTop;
        msgBottom = mainBottom;

        // 顶部要显示「当前角色名」：开屏发一次 whoami，resize 触发 init 重跑也不重复发。
        if (!requestedWhoami && WabClientState.connected()) {
            requestedWhoami = true;
            request("whoami", new JsonObject());
        }
    }

    /** 每个请求都用一个全新的 id 发一条 C2S，宿主在 phone_reply 里原样回。 */
    private void request(String action, JsonObject params) {
        String rid = WabPhoneAction.nextId();
        String json = params == null ? "{}" : params.toString();
        NetworkHandler.CHANNEL.sendToServer(new WabPhoneAction(rid, action, json));
    }

    private void requestMessages(int index) {
        if (index < 0 || index >= contacts.size()) {
            return;
        }
        JsonObject p = new JsonObject();
        p.addProperty("characterId", contacts.get(index).characterId());
        p.addProperty("limit", 50);
        request("messages", p);
    }

    private void doSend() {
        String text = input == null ? "" : input.getValue().trim();
        if (text.isEmpty() || selected < 0 || selected >= contacts.size()) {
            setStatus(Component.translatable("wab.phone.no_selection").getString(), true);
            return;
        }
        String flat = oneLine(text);
        JsonObject p = new JsonObject();
        p.addProperty("characterId", contacts.get(selected).characterId());
        p.addProperty("text", flat);
        lastSentText = flat;
        input.setValue("");
        setStatus(Component.translatable("wab.phone.loading").getString(), false);
        request("send", p);
    }

    private void doAddFriend() {
        String v = addFriendInput == null ? "" : addFriendInput.getValue().trim();
        if (v.isEmpty()) {
            return;
        }
        JsonObject p = new JsonObject();
        p.addProperty("nameOrId", oneLine(v));
        addFriendInput.setValue("");
        setStatus(Component.translatable("wab.phone.loading").getString(), false);
        request("add_friend", p);
    }

    /** 回执到了就原地刷新数据模型；失败把 error 摆在顶部状态行（警示色）。 */
    public void apply(String action, boolean ok, String code, String error, String payload) {
        if (!ok) {
            String msg = error == null ? "" : error;
            if (msg.isEmpty()) {
                if ("timeout".equals(code)) {
                    msg = Component.translatable("wab.phone.timeout").getString();
                } else if ("not_connected".equals(code)) {
                    msg = Component.translatable("wab.phone.not_connected").getString();
                } else {
                    msg = Component.translatable("wab.phone.error").getString();
                }
            }
            setStatus(msg, true);
            return;
        }
        JsonObject data = parse(payload);
        switch (action == null ? "" : action) {
            case "contacts" -> {
                contacts.clear();
                for (JsonElement e : WabJson.arr(data, "contacts")) {
                    if (!e.isJsonObject()) {
                        continue;
                    }
                    JsonObject o = e.getAsJsonObject();
                    contacts.add(new Contact(
                            WabJson.str(o, "characterId"),
                            WabJson.str(o, "name"),
                            WabJson.str(o, "lastMessage"),
                            WabJson.bool(o, "unread", false),
                            WabJson.str(o, "lastMessageAt")));
                }
                contactScroll = 0;
                if (selected < 0 || selected >= contacts.size()) {
                    selected = contacts.isEmpty() ? -1 : 0;
                }
                setStatus(Component.translatable("wab.phone.status_ready").getString(), false);
                // contacts 成功后顺带把当前联系人的会话拉下来。
                requestMessages(selected);
            }
            case "messages" -> {
                messages.clear();
                for (JsonElement e : WabJson.arr(data, "messages")) {
                    if (!e.isJsonObject()) {
                        continue;
                    }
                    JsonObject o = e.getAsJsonObject();
                    messages.add(new Msg(
                            WabJson.str(o, "id"),
                            WabJson.str(o, "text"),
                            WabJson.bool(o, "isUser", false),
                            WabJson.str(o, "senderName"),
                            WabJson.str(o, "at")));
                }
                messageScroll = Math.max(0, messages.size() - visibleMessageRows());
                setStatus(Component.translatable("wab.phone.status_ready").getString(), false);
            }
            case "send" -> {
                String reply = WabJson.str(data, "reply");
                if (!lastSentText.isEmpty()) {
                    messages.add(new Msg("", lastSentText, true, "", ""));
                    lastSentText = "";
                }
                if (!reply.isEmpty()) {
                    messages.add(new Msg("", reply, false, characterName, ""));
                }
                messageScroll = Math.max(0, messages.size() - visibleMessageRows());
                // 本地先给个即时反馈，再拉宿主那边的权威列表覆盖。
                requestMessages(selected);
            }
            case "add_friend" -> {
                boolean added = WabJson.bool(data, "added", false);
                String m = WabJson.str(data, "message");
                setStatus(m.isEmpty()
                        ? Component.translatable(added ? "wab.phone.added" : "wab.phone.add_failed").getString()
                        : m, !added);
                request("contacts", new JsonObject());
            }
            case "whoami" -> {
                characterId = WabJson.str(data, "characterId");
                characterName = WabJson.str(data, "characterName");
            }
            case "mark_read", "remove_friend" -> {
                String m = WabJson.str(data, "message");
                if (!m.isEmpty()) {
                    setStatus(m, false);
                }
            }
            default -> {
            }
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        // 纯色面板，不新增 PNG。
        graphics.fill(left, top, right, bottom, PANEL);

        String conn = Component.translatable(WabClientState.connected()
                ? "wab.phone.connected" : "wab.phone.not_connected").getString();
        String head = conn + (characterName.isEmpty() ? "" : " · " + characterName);
        graphics.drawString(font, trim(head, right - left - 70), left + 6, top + 6,
                WabClientState.connected() ? TEXT : ERROR);
        graphics.drawString(font, trim(status, right - left - 12), left + 6, top + 28,
                statusError ? ERROR : DIM);

        // 联系人列
        graphics.fill(contactsLeft, contactsTop, contactsRight, contactsBottom, LIST_BG);
        if (contacts.isEmpty()) {
            graphics.drawString(font, Component.translatable("wab.phone.empty_contacts").getString(),
                    contactsLeft + 4, contactsTop + 4, DIM);
        } else {
            int end = Math.min(contacts.size(), contactScroll + visibleContactRows());
            for (int i = contactScroll; i < end; i++) {
                Contact c = contacts.get(i);
                int y = contactsTop + (i - contactScroll) * ROW_H;
                boolean sel = i == selected;
                boolean hov = mouseX >= contactsLeft && mouseX <= contactsRight
                        && y <= mouseY && mouseY < y + ROW_H;
                if (sel) {
                    graphics.fill(contactsLeft + 1, y + 1, contactsRight - 1, y + ROW_H - 1, SELECTED);
                } else if (hov) {
                    graphics.fill(contactsLeft + 1, y + 1, contactsRight - 1, y + ROW_H - 1, HOVER);
                }
                String label = (c.unread() ? "· " : "  ") + c.name();
                graphics.drawString(font, trim(label, contactsRight - contactsLeft - 8),
                        contactsLeft + 4, y + 3, sel ? TEXT : DIM);
            }
        }

        // 消息列（手写滚动列表）
        graphics.fill(msgLeft, msgTop, msgRight, msgBottom, LIST_BG);
        if (messages.isEmpty()) {
            graphics.drawString(font, Component.translatable("wab.phone.empty_messages").getString(),
                    msgLeft + 4, msgTop + 4, DIM);
        } else {
            int end = Math.min(messages.size(), messageScroll + visibleMessageRows());
            for (int i = messageScroll; i < end; i++) {
                Msg m = messages.get(i);
                int y = msgTop + (i - messageScroll) * MSG_ROW_H;
                String line = (m.isUser() ? "> " : "< ") + m.text();
                graphics.drawString(font, trim(line, msgRight - msgLeft - 8), msgLeft + 4, y + 3,
                        m.isUser() ? TEXT : DIM);
            }
        }
        drawScrollBar(graphics, msgRight - 3, msgTop, msgBottom, messages.size(),
                visibleMessageRows(), messageScroll);

        // 自己渲染 widget，避免 super.render 再画一次背景把上面的文字盖掉。
        for (var listener : this.children()) {
            if (listener instanceof Renderable renderable) {
                renderable.render(graphics, mouseX, mouseY, partialTick);
            }
        }
    }

    private void drawScrollBar(GuiGraphics g, int x, int y0, int y1, int total, int visible, int offset) {
        if (total <= visible) {
            return;
        }
        int h = y1 - y0;
        int thumb = Math.max(8, h * visible / total);
        int range = Math.max(1, total - visible);
        int y = y0 + (h - thumb) * Math.min(offset, range) / range;
        g.fill(x - 1, y, x + 1, y + thumb, 0xFF888888);
    }

    private int visibleContactRows() {
        return Math.max(1, (contactsBottom - contactsTop) / ROW_H);
    }

    private int visibleMessageRows() {
        return Math.max(1, (msgBottom - msgTop) / MSG_ROW_H);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        if (button == 0 && mouseX >= contactsLeft && mouseX <= contactsRight
                && mouseY >= contactsTop && mouseY <= contactsBottom) {
            int row = (int) ((mouseY - contactsTop) / ROW_H) + contactScroll;
            if (row >= 0 && row < contacts.size()) {
                selected = row;
                messageScroll = 0;
                setStatus(Component.translatable("wab.phone.loading").getString(), false);
                requestMessages(row);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (mouseX >= msgLeft && mouseX <= msgRight && mouseY >= msgTop && mouseY <= msgBottom) {
            int max = Math.max(0, messages.size() - visibleMessageRows());
            messageScroll = clampScroll(messageScroll - (int) Math.signum(delta), max);
            return true;
        }
        if (mouseX >= contactsLeft && mouseX <= contactsRight && mouseY >= contactsTop && mouseY <= contactsBottom) {
            int max = Math.max(0, contacts.size() - visibleContactRows());
            contactScroll = clampScroll(contactScroll - (int) Math.signum(delta), max);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ENTER && input != null && input.isFocused()) {
            doSend();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void setStatus(String text, boolean error) {
        this.status = oneLine(text);
        this.statusError = error;
    }

    private String trim(String text, int maxWidth) {
        if (font.width(text) <= maxWidth) {
            return text;
        }
        return font.plainSubstrByWidth(text, Math.max(0, maxWidth - font.width("..."))) + "...";
    }

    private static int clampScroll(int v, int max) {
        return Math.max(0, Math.min(max, v));
    }

    private static JsonObject parse(String payload) {
        try {
            return JsonParser.parseString(payload == null ? "{}" : payload).getAsJsonObject();
        } catch (Exception ignored) {
            return new JsonObject();
        }
    }

    /** 参数字符串里的裸换行压成空格，保证桥上 NDJSON 一行一帧。 */
    private static String oneLine(String s) {
        return s == null ? "" : s.replace('\r', ' ').replace('\n', ' ');
    }
}
