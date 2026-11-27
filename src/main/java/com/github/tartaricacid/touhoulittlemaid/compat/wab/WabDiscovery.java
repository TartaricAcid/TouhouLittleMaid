package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * UDP 发现：默认只向本机发 beacon（局域网要 lanDiscovery 打开才发），
 * 收到带 HMAC 证明的 offer 后建立 TCP 会话。
 * beacon 里的字段全部来自服务器线程维护的缓存，发现线程不直接碰实体。
 */
public final class WabDiscovery {
    private static final String MAGIC = "WAB-MAID";
    private final WabBridge bridge;
    private final SecureRandom random = new SecureRandom();
    private volatile boolean running;
    private volatile boolean connecting;
    private Thread thread;
    private DatagramSocket socket;
    private String nonce = "";
    private long nonceAt;
    private long lastBeaconAt;

    public WabDiscovery(WabBridge bridge) {
        this.bridge = bridge;
    }

    public void start() {
        if (running) return;
        running = true;
        thread = new Thread(this::loop, "WAB-Discovery");
        thread.setDaemon(true);
        thread.start();
    }

    public void stop() {
        running = false;
        DatagramSocket current = socket;
        socket = null;
        if (current != null) {
            try {
                current.close();
            } catch (Exception ignored) {
            }
        }
        if (thread != null) thread.interrupt();
        thread = null;
    }

    private void loop() {
        while (running) {
            try {
                if (bridge.owner() == null || bridge.connected() || connecting) {
                    sleep(400);
                    continue;
                }
                if (socket == null || socket.isClosed()) {
                    socket = new DatagramSocket(0, InetAddress.getByName("0.0.0.0"));
                    socket.setBroadcast(true);
                    socket.setSoTimeout(400);
                }
                long now = System.currentTimeMillis();
                if (now - lastBeaconAt >= WabConfig.beaconIntervalSeconds() * 1000L) {
                    lastBeaconAt = now;
                    ensureNonce(now);
                    sendBeacon(bridge.beacon(nonce));
                }
                DatagramPacket packet = new DatagramPacket(new byte[65535], 65535);
                try {
                    socket.receive(packet);
                } catch (java.net.SocketTimeoutException timeout) {
                    continue;
                }
                handleOffer(packet);
            } catch (Exception e) {
                if (running) {
                    TouhouLittleMaid.LOGGER.warn("[WAB] discovery error: {}", e.toString());
                    sleep(2000);
                }
            }
        }
    }

    private void ensureNonce(long now) {
        if (nonce.isEmpty() || now - nonceAt > 20000) {
            byte[] bytes = new byte[16];
            random.nextBytes(bytes);
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            nonce = sb.toString();
            nonceAt = now;
        }
    }

    private void sendBeacon(JsonObject beacon) {
        byte[] data = beacon.toString().getBytes(StandardCharsets.UTF_8);
        int port = WabConfig.discoveryPort();
        for (InetAddress address : broadcastTargets()) {
            try {
                socket.send(new DatagramPacket(data, data.length, address, port));
            } catch (Exception ignored) {
            }
        }
    }

    private Set<InetAddress> broadcastTargets() {
        Set<InetAddress> targets = new LinkedHashSet<>();
        try {
            targets.add(InetAddress.getByName("127.0.0.1"));
            if (!WabConfig.lanDiscovery()) return targets;
            targets.add(InetAddress.getByName("255.255.255.255"));
        } catch (Exception ignored) {
        }
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface network = interfaces.nextElement();
                if (!network.isUp() || network.isLoopback()) continue;
                for (InterfaceAddress address : network.getInterfaceAddresses()) {
                    InetAddress broadcast = address.getBroadcast();
                    if (broadcast != null) targets.add(broadcast);
                }
            }
        } catch (Exception ignored) {
        }
        return targets;
    }

    private void handleOffer(DatagramPacket packet) {
        if (packet.getLength() <= 0 || packet.getLength() > 65535) return;
        String text = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8).trim();
        if (text.isEmpty() || text.charAt(0) != '{') return;
        JsonObject offer;
        try {
            offer = JsonParser.parseString(text).getAsJsonObject();
        } catch (Exception e) {
            return;
        }
        if (!MAGIC.equals(WabJson.str(offer, "magic"))) return;
        if (!"offer".equals(WabJson.str(offer, "type"))) return;
        if (WabJson.integer(offer, "v", 0) != 1) return;

        String offerNonce = WabJson.str(offer, "nonce");
        if (!offerNonce.equals(nonce)) return;
        String token = WabJson.str(offer, "token");
        int port = WabJson.integer(offer, "port", 0);
        if (token.length() < 16 || port <= 0 || port > 65535) return;
        String proof = WabJson.str(offer, "proof");
        if (!proof.equalsIgnoreCase(proof(token, offerNonce, port))) return;

        String host = WabJson.str(offer, "host", packet.getAddress().getHostAddress());
        if (host.isEmpty()) host = packet.getAddress().getHostAddress();
        if (connecting || bridge.connected()) return;

        connecting = true;
        String finalHost = host;
        Thread connector = new Thread(() -> connect(finalHost, port, offerNonce, token), "WAB-Connector");
        connector.setDaemon(true);
        connector.start();
    }

    private void connect(String host, int port, String offerNonce, String token) {
        try {
            WabSession session = WabSession.open(bridge, host, port, offerNonce, token);
            bridge.attach(session, session.welcome());
            nonce = "";
            lastBeaconAt = 0;
            TouhouLittleMaid.LOGGER.info("[WAB] connected to {}:{}", host, port);
        } catch (Exception e) {
            TouhouLittleMaid.LOGGER.info("[WAB] connect failed: {}", e.getMessage());
            nonce = "";
            sleep(1500);
        } finally {
            connecting = false;
        }
    }

    private static String proof(String token, String nonce, int port) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(token.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = mac.doFinal(("WAB-MAID|" + nonce + "|" + port).getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format(Locale.ROOT, "%02x", b));
            return sb.toString();
        } catch (Exception e) {
            return "";
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
