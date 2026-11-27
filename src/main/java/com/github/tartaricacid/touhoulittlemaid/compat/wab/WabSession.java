package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 一条 WAB TCP 会话。读写都在独立线程/锁内完成；
 * 收到的业务帧交给服务器线程，避免任何实体操作跨线程。
 * 写出同样是异步的：socket 的发送缓冲写满时阻塞的是写线程，服务器主循环不会陪着卡死。
 */
public final class WabSession {
    private static final int MAX_LINE = 4 * 1024 * 1024;
    private static final int HEARTBEAT_MS = 10000;
    private static final int TIMEOUT_MS = 35000;
    private static final int MAX_OUTBOX = 256;

    private final WabBridge bridge;
    private final Socket socket;
    private final BufferedReader reader;
    private final BufferedWriter writer;
    private final JsonObject welcome;
    private final AtomicBoolean closed = new AtomicBoolean();
    private final Object writeLock = new Object();
    private final ArrayBlockingQueue<String> outbox = new ArrayBlockingQueue<>(MAX_OUTBOX);
    private final long openedAt = System.currentTimeMillis();
    private volatile long lastReadAt = System.currentTimeMillis();
    private volatile long lastPingAt = System.currentTimeMillis();
    private Thread readerThread;
    private Thread writerThread;

    private WabSession(WabBridge bridge, Socket socket, BufferedReader reader, BufferedWriter writer, JsonObject welcome) {
        this.bridge = bridge;
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
        this.welcome = welcome;
    }

    public static WabSession open(WabBridge bridge, String host, int port, String nonce, String token) throws IOException {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port), 5000);
            socket.setTcpNoDelay(true);
            socket.setSoTimeout(10000);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            writer.write(bridge.hello(nonce, token));
            writer.newLine();
            writer.flush();

            String line = reader.readLine();
            if (line == null) throw new IOException("WAB closed the connection during handshake");
            if (line.length() > MAX_LINE) throw new IOException("handshake frame too large");
            JsonObject answer = JsonParser.parseString(line).getAsJsonObject();
            String type = WabJson.str(answer, "type");
            if ("error".equals(type)) {
                throw new IOException(WabJson.str(answer, "code") + ": " + WabJson.str(answer, "message"));
            }
            if (!"welcome".equals(type)) throw new IOException("expected welcome, got " + type);
            socket.setSoTimeout(0);
            return new WabSession(bridge, socket, reader, writer, answer);
        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
            throw e;
        }
    }

    public JsonObject welcome() {
        return welcome;
    }

    public boolean isClosed() {
        return closed.get();
    }

    public void startReader() {
        if (readerThread == null) {
            readerThread = new Thread(this::readLoop, "WAB-Session-Reader");
            readerThread.setDaemon(true);
            readerThread.start();
        }
        if (writerThread == null) {
            writerThread = new Thread(this::writeLoop, "WAB-Session-Writer");
            writerThread.setDaemon(true);
            writerThread.start();
        }
        sendPing();
    }

    private void readLoop() {
        try {
            String line;
            while (!closed.get() && (line = reader.readLine()) != null) {
                lastReadAt = System.currentTimeMillis();
                if (line.isEmpty()) continue;
                if (line.length() > MAX_LINE) {
                    TouhouLittleMaid.LOGGER.warn("[WAB] oversized frame, closing session");
                    break;
                }
                JsonObject frame;
                try {
                    frame = JsonParser.parseString(line).getAsJsonObject();
                } catch (Exception e) {
                    continue;
                }
                String type = WabJson.str(frame, "type");
                if ("ping".equals(type)) {
                    JsonObject pong = new JsonObject();
                    pong.addProperty("v", 1);
                    pong.addProperty("type", "pong");
                    pong.addProperty("at", System.currentTimeMillis());
                    send(pong);
                } else if ("pong".equals(type)) {
                    lastReadAt = System.currentTimeMillis();
                } else {
                    bridge.server().execute(() -> bridge.handleFrame(this, frame));
                }
            }
        } catch (IOException e) {
            if (!closed.get()) TouhouLittleMaid.LOGGER.info("[WAB] session read ended: {}", e.getMessage());
        } finally {
            close("connection ended");
        }
    }

    public void tick() {
        if (closed.get()) return;
        long now = System.currentTimeMillis();
        if (now - lastReadAt > TIMEOUT_MS) {
            TouhouLittleMaid.LOGGER.info("[WAB] heartbeat timeout");
            close("heartbeat timeout");
            return;
        }
        if (now - lastPingAt > HEARTBEAT_MS) sendPing();
    }

    private void sendPing() {
        lastPingAt = System.currentTimeMillis();
        JsonObject ping = new JsonObject();
        ping.addProperty("v", 1);
        ping.addProperty("type", "ping");
        ping.addProperty("at", System.currentTimeMillis());
        send(ping);
    }

    private void writeLoop() {
        while (!closed.get()) {
            String text;
            try {
                text = outbox.take();
            } catch (InterruptedException e) {
                return;
            }
            synchronized (writeLock) {
                if (closed.get()) return;
                try {
                    writer.write(text);
                    writer.newLine();
                    writer.flush();
                } catch (IOException e) {
                    close("write failed");
                    return;
                }
            }
        }
    }

    public void send(JsonObject message) {
        if (closed.get()) return;
        if (!outbox.offer(message.toString())) {
            // 对端不收包时队列会满。此刻关掉会话（socket 一关，写线程立刻解开），
            // 而不是让发起调用的服务器线程等 TCP 缓冲腾出来。
            TouhouLittleMaid.LOGGER.warn("[WAB] write buffer full ({} frames), closing session", MAX_OUTBOX);
            close("write buffer full");
        }
    }

    public void sendResult(String id, boolean ok, JsonObject data, String code, String error) {
        JsonObject result = new JsonObject();
        result.addProperty("v", 1);
        result.addProperty("type", "result");
        result.addProperty("id", id == null ? "" : id);
        result.addProperty("ok", ok);
        if (ok) {
            if (data != null) result.add("data", data);
        } else {
            result.addProperty("code", code == null ? "command_failed" : code);
            result.addProperty("error", error == null ? "命令没有成功。" : error);
        }
        send(result);
    }

    public void close(String reason) {
        if (!closed.compareAndSet(false, true)) return;
        outbox.clear();
        try {
            socket.close();
        } catch (IOException ignored) {
        }
        Thread writerCurrent = writerThread;
        if (writerCurrent != null && writerCurrent != Thread.currentThread()) {
            writerCurrent.interrupt();
        }
        bridge.detach(this);
        TouhouLittleMaid.LOGGER.info("[WAB] session closed after {} ms: {}", System.currentTimeMillis() - openedAt, reason);
    }
}
