package com.wab.testkit;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 游戏里的「假 WAB 宿主」：走的是真链路，不是内部方法直调。
 * <p>
 * 车万女仆这一侧是主动连出的客户端，所以外界入口只有一条来路——
 * 它发 UDP beacon，我们回带 HMAC proof 的 offer，它再拿 TCP 连上来发 hello。
 * 于是这个类按协议把那套握手做一遍，之后就能从真正的入站口往桥里灌任何帧，
 * 并把 result / state / event / chat / photo / tts 收回来核对。
 * 桥里那些 package-private 的执行器一个都不用碰，测的就是线上真实会走的那条路。
 */
public final class FakeWabHost {
    public static final String MAGIC = "WAB-MAID";
    /** 协议要求 token 至少 16 位；proof 用 HmacSHA256(token, "WAB-MAID|nonce|port")。 */
    public static final String TOKEN = "wab-testkit-shared-token-0";

    private static final int MAX_KEPT = 512;

    private final AtomicLong sequence = new AtomicLong();
    private final ConcurrentHashMap<String, LinkedBlockingQueue<JsonObject>> awaiting = new ConcurrentHashMap<>();
    private final LinkedBlockingQueue<JsonObject> frames = new LinkedBlockingQueue<>();
    private final ConcurrentLinkedDeque<JsonObject> backlog = new ConcurrentLinkedDeque<>();

    private volatile boolean running;
    private volatile DatagramSocket udp;
    private volatile Socket link;
    private volatile BufferedWriter writer;
    private volatile BufferedReader reader;

    private volatile JsonObject beacon;
    private volatile long beaconSeq;
    private volatile JsonObject hello;
    private volatile long helloSeq;
    private volatile int tcpPort;
    private volatile String lastError = "";

    public volatile long framesIn;
    public volatile long framesOut;
    public volatile long malformedSeen;
    public volatile long pongSeen;
    public volatile long unmatchedResult;
    public volatile long linkClosed;
    public volatile long offerSent;

    public void start(int udpPort) throws IOException {
        stop();
        DatagramSocket socket = new DatagramSocket(udpPort);
        socket.setBroadcast(true);
        socket.setSoTimeout(300);
        udp = socket;
        running = true;
        thread("WAB-TestKit-UDP", this::udpLoop);
    }

    public void stop() {
        running = false;
        closeQuietly(udp);
        udp = null;
        closeQuietly(link);
        link = null;
        writer = null;
        reader = null;
        hello = null;
        beacon = null;
    }

    public boolean linked() {
        Socket current = link;
        return current != null && !current.isClosed();
    }

    public long beaconSeq() {
        return beaconSeq;
    }

    public JsonObject beacon() {
        return beacon;
    }

    public JsonObject hello() {
        return hello;
    }

    public boolean gotHello() {
        return hello != null;
    }

    public long helloCount() {
        return helloSeq;
    }

    public boolean listening() {
        return running;
    }

    public boolean awaitBeacon(long sinceSeq, long timeoutMs) {
        long deadline = System.currentTimeMillis() + timeoutMs;
        while (System.currentTimeMillis() < deadline) {
            if (beaconSeq > sinceSeq) return true;
            sleep(120);
        }
        return false;
    }

    public boolean awaitHello(long timeoutMs) {
        long deadline = System.currentTimeMillis() + timeoutMs;
        while (System.currentTimeMillis() < deadline) {
            if (hello != null) return true;
            if (lastError.startsWith("配对中断")) return false;
            sleep(120);
        }
        return false;
    }

    // ==================== 发送 ====================

    public boolean send(JsonObject frame) {
        return sendRaw(frame == null ? "" : frame.toString());
    }

    /** 直接写一整行：畸形 JSON、超长帧这些「不该出现的输入」只能从这里进去。 */
    public boolean sendRaw(String line) {
        BufferedWriter out = writer;
        if (out == null || line == null || line.isEmpty()) return false;
        synchronized (this) {
            try {
                out.write(line);
                out.newLine();
                out.flush();
                framesOut++;
                return true;
            } catch (IOException e) {
                lastError = "写帧失败：" + e;
                return false;
            }
        }
    }

    public boolean sendWelcome(boolean allowWorldActions, boolean allowGestures, boolean voiceTts, boolean voiceStt) {
        return sendWelcome(allowWorldActions, allowGestures, voiceTts, voiceStt, null);
    }

    /** fields 非空时一起下发插件字段表——真 WAB 就是把 plugin.json 描成一张表推给游戏的。 */
    public boolean sendWelcome(boolean allowWorldActions, boolean allowGestures, boolean voiceTts, boolean voiceStt, JsonArray fields) {
        JsonObject welcome = new JsonObject();
        welcome.addProperty("v", 1);
        welcome.addProperty("type", "welcome");
        welcome.addProperty("saveId", "wab-testkit-save");
        welcome.addProperty("characterId", "wab-testkit-character");
        JsonObject settings = new JsonObject();
        settings.addProperty("allowWorldActions", allowWorldActions);
        settings.addProperty("allowGestures", allowGestures);
        welcome.add("settings", settings);
        JsonObject voice = new JsonObject();
        voice.addProperty("tts", voiceTts);
        voice.addProperty("stt", voiceStt);
        welcome.add("voice", voice);
        if (fields != null) welcome.add("fields", fields);
        welcome.add("bound", new JsonObject());
        return send(welcome);
    }

    public boolean sendSettings(boolean allowWorldActions, boolean allowGestures) {
        JsonObject frame = new JsonObject();
        frame.addProperty("v", 1);
        frame.addProperty("type", "settings");
        JsonObject settings = new JsonObject();
        settings.addProperty("allowWorldActions", allowWorldActions);
        settings.addProperty("allowGestures", allowGestures);
        frame.add("settings", settings);
        return send(frame);
    }

    /** 热推一帧完整形状（settings + voice + fields，任一可为 null），验证「WAB 里改完，游戏里跟着变」。 */
    public boolean sendSettings(JsonObject settings, JsonObject voice, JsonArray fields) {
        JsonObject frame = new JsonObject();
        frame.addProperty("v", 1);
        frame.addProperty("type", "settings");
        if (settings != null) frame.add("settings", settings);
        if (voice != null) frame.add("voice", voice);
        if (fields != null) frame.add("fields", fields);
        return send(frame);
    }

    /** 假扮宿主拒绝一次从游戏里发上来的设置改动。 */
    public boolean sendConfigRejected(String key, String message) {
        JsonObject frame = new JsonObject();
        frame.addProperty("v", 1);
        frame.addProperty("type", "error");
        frame.addProperty("code", "config_rejected");
        frame.addProperty("key", key == null ? "" : key);
        frame.addProperty("message", message == null ? "" : message);
        return send(frame);
    }

    /** plugin.json 里一个设置字段在桥上的形状：宿主那边就是这么描的，游戏侧照 this 长控件。 */
    public static JsonObject fieldRow(String key, String label, String type, String value, String defaultValue,
                                      String help, boolean locked, String... options) {
        JsonObject row = new JsonObject();
        row.addProperty("key", key);
        row.addProperty("label", label);
        row.addProperty("type", type);
        row.addProperty("value", value);
        row.addProperty("default", defaultValue);
        row.addProperty("help", help);
        row.addProperty("locked", locked);
        if (options.length > 0) {
            JsonArray list = new JsonArray();
            for (String option : options) list.add(option);
            row.add("options", list);
        }
        return row;
    }

    public JsonObject awaitConfigSet(long timeoutMs) {
        return await("config_set", timeoutMs);
    }

    public boolean sendBind(JsonObject bindings, String notes) {
        JsonObject frame = new JsonObject();
        frame.addProperty("v", 1);
        frame.addProperty("type", "bind");
        frame.add("bindings", bindings);
        frame.addProperty("notes", notes == null ? "" : notes);
        return send(frame);
    }

    public boolean sendStats(String maidId, String character, int affection, int trust, int energy, int mood) {
        JsonObject frame = new JsonObject();
        frame.addProperty("v", 1);
        frame.addProperty("type", "stats");
        frame.addProperty("maid", maidId);
        frame.addProperty("character", character);
        frame.addProperty("affection", affection);
        frame.addProperty("affectionPoints", affection * 10);
        frame.addProperty("trust", trust);
        frame.addProperty("energy", energy);
        frame.addProperty("mood", mood);
        return send(frame);
    }

    public boolean sendChatReply(String maidId, boolean ok, String text, String error) {
        JsonObject frame = new JsonObject();
        frame.addProperty("v", 1);
        frame.addProperty("type", "chat_reply");
        frame.addProperty("maid", maidId);
        frame.addProperty("ok", ok);
        frame.addProperty("text", text == null ? "" : text);
        if (error != null) frame.addProperty("error", error);
        return send(frame);
    }

    public boolean sendVoice(String type, String id, JsonObject extra) {
        JsonObject frame = new JsonObject();
        frame.addProperty("v", 1);
        frame.addProperty("type", type);
        frame.addProperty("id", id);
        if (extra != null) {
            for (String key : extra.keySet()) frame.add(key, extra.get(key));
        }
        return send(frame);
    }

    public boolean sendTyped(String type, JsonObject extra) {
        JsonObject frame = new JsonObject();
        frame.addProperty("v", 1);
        frame.addProperty("type", type);
        if (extra != null) {
            for (String key : extra.keySet()) frame.add(key, extra.get(key));
        }
        return send(frame);
    }

    public String newId() {
        return "k" + sequence.incrementAndGet();
    }

    /** 只发不等：采集那种「几十秒以后才回执」的长任务要分两步看。 */
    public boolean sendCommand(String id, String method, JsonObject params) {
        awaiting.put(id, new LinkedBlockingQueue<>(4));
        JsonObject frame = new JsonObject();
        frame.addProperty("v", 1);
        frame.addProperty("type", "command");
        frame.addProperty("id", id);
        frame.addProperty("method", method);
        frame.add("params", params == null ? new JsonObject() : params);
        if (send(frame)) return true;
        awaiting.remove(id);
        throw new IllegalStateException("发不出 " + method + "：会话不在" + (lastError.isEmpty() ? "" : "（" + lastError + "）"));
    }

    /** 等某个 id 的 result。超时不动登记表：迟到的回执仍然认得出是谁的。 */
    public JsonObject awaitResult(String id, long timeoutMs) {
        LinkedBlockingQueue<JsonObject> queue = awaiting.get(id);
        if (queue == null) throw new IllegalStateException("没有为 " + id + " 留回执的位子");
        JsonObject result;
        try {
            result = queue.poll(timeoutMs, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("等回执时线程被打断");
        }
        if (result == null) throw new IllegalStateException("id=" + id + " 在 " + timeoutMs + " ms 内没有回执（服务器线程卡住、游戏被暂停，或这单根本不会回执）");
        return result;
    }

    public void drop(String id) {
        awaiting.remove(id);
    }

    /**
     * 发一条命令并等它的 result。命令在桥里是交给服务器线程执行的，
     * 所以这条只能跑在测试线程上——卡住服务器线程的话，永远也等不到回执。
     */
    public JsonObject command(String method, JsonObject params, long timeoutMs) {
        String id = newId();
        sendCommand(id, method, params);
        try {
            return awaitResult(id, timeoutMs);
        } finally {
            drop(id);
        }
    }

    /** 等某一类帧（state / event / chat / photo / tts / stt）。先看攒下的，再等新来的。 */
    public JsonObject await(String type, long timeoutMs) {
        for (Iterator<JsonObject> it = backlog.iterator(); it.hasNext(); ) {
            JsonObject frame = it.next();
            if (type.equals(text(frame, "type"))) {
                it.remove();
                return frame;
            }
        }
        long deadline = System.currentTimeMillis() + timeoutMs;
        while (System.currentTimeMillis() < deadline) {
            JsonObject frame;
            try {
                frame = frames.poll(150, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
            if (frame == null) continue;
            if (type.equals(text(frame, "type"))) return frame;
            backlog.addLast(frame);
            while (backlog.size() > MAX_KEPT) backlog.pollFirst();
        }
        return null;
    }

    // ==================== 三条循环 ====================

    private void udpLoop() {
        byte[] buffer = new byte[65535];
        while (running) {
            DatagramSocket socket = udp;
            if (socket == null || socket.isClosed()) return;
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                try {
                    socket.receive(packet);
                } catch (SocketTimeoutException timeout) {
                    continue;
                }
                onBeacon(socket, packet);
            } catch (Exception e) {
                if (running) lastError = "收 beacon 出错：" + e;
            }
        }
    }

    private void onBeacon(DatagramSocket socket, DatagramPacket packet) {
        String raw = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8).trim();
        if (raw.isEmpty() || raw.charAt(0) != '{') return;
        JsonObject frame;
        try {
            frame = JsonParser.parseString(raw).getAsJsonObject();
        } catch (Exception e) {
            return;
        }
        if (!MAGIC.equals(text(frame, "magic")) || !"beacon".equals(text(frame, "type"))) return;
        String nonce = text(frame, "nonce");
        if (nonce.isEmpty()) return;
        beacon = frame;
        beaconSeq++;
        if (linked()) return;

        try {
            ServerSocket server = new ServerSocket(0, 4, InetAddress.getByName("127.0.0.1"));
            tcpPort = server.getLocalPort();
            thread("WAB-TestKit-Accept", () -> acceptOnce(server));

            JsonObject offer = new JsonObject();
            offer.addProperty("magic", MAGIC);
            offer.addProperty("v", 1);
            offer.addProperty("type", "offer");
            offer.addProperty("nonce", nonce);
            offer.addProperty("token", TOKEN);
            offer.addProperty("port", tcpPort);
            offer.addProperty("host", "127.0.0.1");
            offer.addProperty("proof", proof(TOKEN, nonce, tcpPort));
            byte[] out = offer.toString().getBytes(StandardCharsets.UTF_8);
            socket.send(new DatagramPacket(out, out.length, packet.getSocketAddress()));
            offerSent++;
        } catch (Exception e) {
            lastError = "回 offer 失败：" + e;
        }
    }

    private void acceptOnce(ServerSocket server) {
        try {
            Socket socket = server.accept();
            socket.setTcpNoDelay(true);
            socket.setSoTimeout(0);
            link = socket;
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            hello = null;
            backlog.clear();
            while (frames.poll() != null) {
                // 上一轮攒的帧丢掉：新会话不该看见断线前的旧回执
            }
            server.close();
            thread("WAB-TestKit-Read", this::readLoop);
        } catch (Exception e) {
            if (running) lastError = "TCP accept 失败：" + e;
            closeQuietly(server);
        }
    }

    private void readLoop() {
        try {
            BufferedReader in = reader;
            String line;
            while (running && in != null && (line = in.readLine()) != null) {
                framesIn++;
                if (line.isEmpty()) continue;
                JsonObject frame;
                try {
                    frame = JsonParser.parseString(line).getAsJsonObject();
                } catch (Exception e) {
                    malformedSeen++;
                    continue;
                }
                String type = text(frame, "type");
                if ("hello".equals(type)) {
                    hello = frame;
                    helloSeq++;
                    continue;
                }
                if ("result".equals(type)) {
                    LinkedBlockingQueue<JsonObject> queue = awaiting.remove(text(frame, "id"));
                    if (queue != null) queue.offer(frame);
                    else unmatchedResult++;
                    continue;
                }
                if ("pong".equals(type)) {
                    pongSeen++;
                    continue;
                }
                frames.offer(frame);
                while (frames.size() > MAX_KEPT) frames.poll();
            }
        } catch (IOException e) {
            // 桥关掉会话都会走到这里：超帧、写缓冲满、心跳超时、关服。
            lastError = "配对中断：" + e;
        } finally {
            linkClosed++;
            closeQuietly(link);
            link = null;
            writer = null;
            reader = null;
        }
    }

    // ==================== 小工具 ====================

    public static String text(JsonObject src, String key) {
        if (src == null || !src.has(key) || !src.get(key).isJsonPrimitive()) return "";
        return src.get(key).getAsString();
    }

    public static boolean has(JsonObject src, String key) {
        return src != null && src.has(key);
    }

    public int tcpPort() {
        return tcpPort;
    }

    public String lastError() {
        return lastError;
    }

    private static String proof(String token, String nonce, int port) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(token.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = mac.doFinal((MAGIC + "|" + nonce + "|" + port).getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format(Locale.ROOT, "%02x", b));
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    private static void thread(String name, Runnable body) {
        Thread worker = new Thread(body, name);
        worker.setDaemon(true);
        worker.start();
    }

    private static void closeQuietly(java.io.Closeable closeable) {
        try {
            if (closeable != null) closeable.close();
        } catch (Exception ignored) {
        }
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
