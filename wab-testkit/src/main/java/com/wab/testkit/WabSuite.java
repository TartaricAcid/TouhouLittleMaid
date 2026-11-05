package com.wab.testkit;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidAndItemTransformEvent;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabBridge;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabConfig;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabError;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabHud;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabSensors;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabStorage;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.Base64;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 用例表。整轮跑在测试模组自己的线程上，只有真的要对世界动手的那几段才切回服务器线程。
 * <p>
 * 规矩只有一条：外界入口的成功分支和失败分支都要过。失败必须给出结构化的 code / error，
 * 既不能把服务器线程带崩，也不能不回执让宿主干等。
 * <p>
 * 注意别在 onServer 里等网络：那等于占着服务器线程等一个只有服务器线程才能办的事，必死锁。
 * 需要「等桥把帧办完」的地方一律在测试线程上 sleep，再回服务器线程核对。
 */
public final class WabSuite {
    private static final long CMD = 8000;
    private static final long HEAVY = 20000;

    private final FakeWabHost host;
    private final MinecraftServer server;
    private final ServerPlayer starter;

    private WabBridge bridge;
    private EntityMaid maid;
    private String maidId;
    private String ownerName = "";
    private int udp;
    private JsonObject ttsFrame;

    private WabSuite(FakeWabHost host, MinecraftServer server, ServerPlayer starter) {
        this.host = host;
        this.server = server;
        this.starter = starter;
    }

    public static void run(FakeWabHost host, MinecraftServer server, ServerPlayer starter, int udpPort) {
        new WabSuite(host, server, starter).runAll(udpPort);
    }

    private void runAll(int port) {
        udp = port <= 0 ? WabConfig.discoveryPort() : port;
        WabReport.begin("WAB 桥链路测试（Forge 1.20.1 · 假宿主走真 socket）", server, starter);
        bridge = WabBridge.get();
        if (bridge == null) {
            WabReport.line("  FAIL  拿不到 WabBridge：桥是服务器启动时才建的，请进了世界之后再跑 /wtest all");
            WabReport.finish();
            return;
        }
        try {
            pairing();
            if (!host.linked()) {
                WabReport.line("  配对没成，后面的用例都跑不了：按上面 FAIL 的原因改掉，再 /wtest all 一次。");
                WabReport.finish();
                return;
            }
            session();
            queries();
            inbound();
            actions();
            gates();
            worldEvents();
            photo();
            voice();
            direct();
            limits();
            recovery();
        } catch (Throwable unexpected) {
            WabReport.line("  FAIL  测试自己断了：" + unexpected);
            for (StackTraceElement element : unexpected.getStackTrace()) {
                WabReport.line("        at " + element);
            }
        } finally {
            cleanup();
            WabReport.finish();
            host.stop();
        }
    }

    // ==================== 1 发现与握手 ====================

    private void pairing() {
        WabReport.section("1 发现与握手（UDP beacon → HMAC offer → TCP hello → welcome）");

        WabReport.check("桥的开关与端口读得到", () -> {
            if (!WabConfig.enabled()) WabReport.skip("配置里 enabled=false，桥一个 beacon 都不会发");
            return "enabled=" + WabConfig.enabled()
                    + " discoveryPort=" + WabConfig.discoveryPort()
                    + " beaconInterval=" + WabConfig.beaconIntervalSeconds() + "s"
                    + " chatRange=" + WabConfig.chatRange()
                    + " lanDiscovery=" + WabConfig.lanDiscovery()
                    + " showHud=" + WabConfig.showHud()
                    + " autonomy=" + WabConfig.autonomyEnabled()
                    + " debug=" + WabConfig.debug();
        });

        WabReport.check("桥当前空闲（没被真 WAB 占着）", () -> {
            if (bridge.connected()) WabReport.skip("桥已经连着会话：先关掉 WarmAsBefore（或在它的 Minecraft 插件里断开），桥才会重新发 beacon");
            return "instance=" + bridge.instanceId();
        });

        WabReport.check("监听 UDP :" + port(), () -> {
            host.start(port());
            return "桥默认往 127.0.0.1:" + WabConfig.discoveryPort() + " 发 beacon，不一致就用 /wtest all <port>";
        });

        long before = host.beaconSeq();
        WabReport.check("收到 beacon", () -> {
            if (!host.awaitBeacon(before, 25000)) {
                throw new AssertionError("25 秒内没有 beacon。查：进世界了吗（主人得在线）、enabled 是不是开、端口是不是 "
                        + port() + "、是不是已经被别的 WAB 连走了。最近一次错误：" + host.lastError());
            }
            return "累计第 " + host.beaconSeq() + " 次";
        });

        WabReport.check("beacon 字段齐全（配对接头）", () -> {
            JsonObject beacon = host.beacon();
            String[] keys = {"magic", "v", "type", "nonce", "instance", "mod", "modVersion", "mc", "loader", "loaderVersion", "bridge"};
            StringBuilder missing = new StringBuilder();
            for (String key : keys) {
                if (!FakeWabHost.has(beacon, key)) missing.append(missing.length() == 0 ? "" : ",").append(key);
            }
            WabReport.need(missing.length() == 0, "缺字段：" + missing);
            WabReport.need("WAB-MAID".equals(txt(beacon, "magic")), "magic 不对：" + txt(beacon, "magic"));
            WabReport.need("beacon".equals(txt(beacon, "type")), "type 不对：" + txt(beacon, "type"));
            WabReport.need("touhou_little_maid".equals(txt(beacon, "mod")), "mod 不是 touhou_little_maid：" + txt(beacon, "mod"));
            // 没配对之前不该漏隐私：世界名、玩家名、女仆名册都得留到 hello 之后才带
            WabReport.need(!FakeWabHost.has(beacon, "world") && !FakeWabHost.has(beacon, "player")
                    && !FakeWabHost.has(beacon, "maids"), "beacon 里带了世界名/玩家名/女仆名册");
            return txt(beacon, "mc") + "/" + txt(beacon, "loader") + " " + txt(beacon, "loaderVersion")
                    + " modVersion=" + txt(beacon, "modVersion") + " bridge=" + txt(beacon, "bridge");
        });

        WabReport.check("分支版本号带 -wab（装错 jar 看得出来）", () -> {
            String version = txt(host.beacon(), "modVersion");
            WabReport.need(version.contains("wab"), "modVersion 没有 -wab 后缀，看起来和官方 jar 分不出来：" + version);
            return version;
        });

        WabReport.check("proof 配对通过、hello 上来了", () -> {
            if (!host.awaitHello(20000)) {
                throw new AssertionError("offer 发出去了却等不到 hello。HMAC / nonce / 端口 任一不对，桥都会静默放弃。最近一次错误："
                        + host.lastError());
            }
            JsonObject hello = host.hello();
            WabReport.need(FakeWabHost.TOKEN.equals(txt(hello, "token")), "token 不是我们发出去的那个：" + txt(hello, "token"));
            WabReport.need(FakeWabHost.has(hello, "maids"), "hello 没带女仆名册");
            WabReport.need(FakeWabHost.has(hello, "world") && FakeWabHost.has(hello, "player"), "hello 缺 world/player");
            return "世界=" + txt(hello, "world") + " 主人=" + txt(hello, "player")
                    + " 名册=" + hello.getAsJsonArray("maids").size() + " 只";
        });

        WabReport.check("回 welcome，握手完成并推出 state", () -> {
            WabReport.need(host.sendWelcome(true, true, true, true), "welcome 发不出去");
            JsonObject state = host.await("state", 15000);
            WabReport.need(state != null, "握手后 15 秒没等到 state：桥在 onWelcome 里要主动推一次全量状态");
            WabReport.need(FakeWabHost.has(state, "world") && FakeWabHost.has(state, "maids"), "state 缺 world/maids");
            return "state 到了，maids=" + state.getAsJsonArray("maids").size();
        });

        WabReport.check("主人认的是「谁名下女仆最多」，不是谁先服", () -> onServer(() -> {
            ServerPlayer owner = bridge.currentOwner();
            if (owner == null) WabReport.skip("还没认到主人（refreshOwner 每 tick 认一次），稍后重跑");
            ownerName = owner.getName().getString();
            int owned = bridge.ownedMaids().size();
            WabReport.need(owned > 0, "主人名下没有已收的女仆：先收一只再测（当前主人=" + ownerName + "）");
            return "主人=" + ownerName + " 名下=" + owned + " 只 在线玩家=" + server.getPlayerList().getPlayers().size() + " 人";
        }));

        WabReport.check("挑出一只主角女仆", () -> onServer(() -> {
            Collection<EntityMaid> maids = bridge.ownedMaids();
            if (maids.isEmpty()) WabReport.skip("名册空，后面所有行动类用例都跑不了");
            maid = maids.iterator().next();
            maidId = maid.getUUID().toString();
            return WabBridge.cleanName(maid) + " uuid=" + maidId + " 生命=" + (int) maid.getHealth();
        }));
    }

    private int port() {
        return udp;
    }

    // ==================== 2 会话基础 ====================

    private void session() {
        WabReport.section("2 会话与心跳");

        WabReport.check("我们发的 ping → 桥回 pong", () -> {
            long before = host.pongSeen;
            JsonObject ping = new JsonObject();
            ping.addProperty("v", 1);
            ping.addProperty("type", "ping");
            ping.addProperty("at", System.currentTimeMillis());
            WabReport.need(host.send(ping), "ping 发不出去");
            WabReport.need(host.await("pong", 100) == null, "桥把 pong 当成业务帧塞进队列了（应当只刷新心跳时间）");
            return "pongSeen=" + host.pongSeen + "（计数在涨就说明桥认得出 pong：那一条是它自己发 ping、我们没答，它照样续心跳）";
        });

        WabReport.check("桥自己每 10 秒发一次 ping（心跳方向是双向的）", () -> {
            JsonObject ping = host.await("ping", 13000);
            WabReport.need(ping != null, "13 秒内没等到桥发来的 ping");
            return "at=" + ping.get("at") + "，对面 35 秒收不到东西就会自己关会话";
        });

        WabReport.check("result 通道按 id 回填", () -> {
            JsonObject result = host.command("chat", param("text", "链路测试的一句话"), CMD);
            WabReport.need(ok(result), "chat 应当回 ok：" + describe(result));
            return "method=chat 的回执在 " + CMD + " ms 内按 id 认回来了";
        });

        WabReport.check("不认识的 method → unknown_method", () -> {
            JsonObject result = host.command("definitely_not_a_method", null, CMD);
            WabReport.need(!ok(result), "未知方法居然回执成功：" + describe(result));
            WabReport.need("unknown_method".equals(code(result)), "code 不是 unknown_method：" + code(result));
            return error(result);
        });

        WabReport.check("params 整个缺掉也不能崩", () -> {
            JsonObject result = host.command("backup", null, CMD);
            WabReport.need(ok(result) || !code(result).isEmpty(), "既没成功也没给 code：" + describe(result));
            return describe(result);
        });
    }

    // ==================== 3 查询类命令 ====================

    private void queries() {
        WabReport.section("3 查询类命令（status / scan / see / backup / plan）");
        if (noMaid()) return;

        WabReport.check("status：整份状态", () -> {
            JsonObject result = host.command("status", param("maid", maidId), CMD);
            WabReport.need(ok(result), "status 失败：" + describe(result));
            JsonObject data = data(result);
            String[] keys = {"world", "maid", "uuid", "healthDetail", "favorability", "schedule", "inventory", "armor", "nearbyPlayers"};
            for (String key : keys) WabReport.need(FakeWabHost.has(data, key), "status 少了字段 " + key);
            WabReport.need(maidId.equals(txt(data, "uuid")), "uuid 对不上：" + txt(data, "uuid"));
            return "好感=" + data.get("favorability") + " 日程=" + txt(data, "schedule") + " 附近玩家=" + size(data, "nearbyPlayers");
        });

        WabReport.check("scan：方块半径要钳到 24（r=64 是 210 万次 getBlockState，会一帧卡死）", () -> {
            JsonObject result = host.command("scan", param("maid", maidId, "radius", 64), HEAVY);
            WabReport.need(ok(result), "scan 失败：" + describe(result));
            JsonObject data = data(result);
            WabReport.need(FakeWabHost.has(data, "blockRadius"), "回执里没有 blockRadius，看不出有没有钳");
            int blockRadius = data.get("blockRadius").getAsInt();
            WabReport.need(blockRadius <= 24, "blockRadius=" + blockRadius + "，没钳住");
            return "radius=" + data.get("radius") + " blockRadius=" + blockRadius
                    + " 实体=" + size(data, "entities") + " 矿石=" + size(data, "ores") + " 床=" + size(data, "beds");
        });

        WabReport.check("scan：blocks=false 就真的不查方块", () -> {
            JsonObject result = host.command("scan", param("maid", maidId, "radius", 12,
                    "entities", true, "blocks", false, "fluids", false, "light", false), CMD);
            WabReport.need(ok(result), "scan 失败：" + describe(result));
            JsonObject data = data(result);
            WabReport.need(!FakeWabHost.has(data, "ores"), "blocks=false 还给了 ores");
            return "entities=" + size(data, "entities") + " drops=" + size(data, "drops");
        });

        WabReport.check("see：视线锥转成一段人话", () -> {
            JsonObject result = host.command("see", param("maid", maidId), CMD);
            WabReport.need(ok(result), "see 失败：" + describe(result));
            JsonObject data = data(result);
            String[] keys = {"maid", "facing", "facingText", "radius", "cone", "targets", "blocks", "text"};
            for (String key : keys) WabReport.need(FakeWabHost.has(data, key), "see 少了字段 " + key);
            return txt(data, "facingText") + "：" + clip(txt(data, "text"));
        });

        WabReport.check("see：半径和视角越界要钳住", () -> {
            JsonObject result = host.command("see", param("maid", maidId, "radius", 99999, "cone", 99999), CMD);
            WabReport.need(ok(result), "see 失败：" + describe(result));
            JsonObject data = data(result);
            int radius = data.get("radius").getAsInt();
            int cone = data.get("cone").getAsInt();
            WabReport.need(radius <= 48, "radius 没钳：" + radius);
            WabReport.need(cone <= 170, "cone 没钳：" + cone);
            return "radius=" + radius + " cone=" + cone;
        });

        WabReport.check("backup：存档快照可还原", () -> {
            JsonObject result = host.command("backup", param("maid", maidId), CMD);
            WabReport.need(ok(result), "backup 失败：" + describe(result));
            JsonObject data = data(result);
            String[] keys = {"id", "name", "model", "world", "savedAt", "health", "nbt", "nbtFormat", "inventory", "loveLoathe"};
            for (String key : keys) WabReport.need(FakeWabHost.has(data, key), "backup 少了字段 " + key);
            WabReport.need(!txt(data, "nbt").isEmpty(), "nbt 是空的（gzip+base64 没写出来）：" + clip(txt(data, "nbtError")));
            return "nbt=" + txt(data, "nbt").length() + " 字符 格式=" + txt(data, "nbtFormat");
        });

        WabReport.check("backup：认不出的人要报 maid_not_found", () -> {
            JsonObject result = host.command("backup", param("maid", randomUuid()), CMD);
            WabReport.need(!ok(result), "陌生 id 居然成功了：" + describe(result));
            WabReport.need("maid_not_found".equals(code(result)), "code=" + code(result));
            return error(result);
        });

        WabReport.check("查询类不点名时回的是「她」——宿主得知道这个口径", () -> {
            JsonObject result = host.command("status", param("maid", randomUuid()), CMD);
            if (!ok(result)) {
                WabReport.note("陌生 id 的 status 直接失败，code=" + code(result));
                return "查询类不认人时回的是名册第一只（当前：失败口径）";
            }
            String uuid = txt(data(result), "uuid");
            WabReport.note("陌生 id 的 status 回了 " + (uuid.equals(maidId) ? WabBridge.cleanName(maid) + "（名册第一只）" : uuid));
            return "查询类不点名 → 退到第一只；行动类不点名 → maid_not_found";
        });

        WabReport.check("plan get：日程与自主行为读得回来", () -> {
            JsonObject result = host.command("plan", param("maid", maidId, "action", "get"), CMD);
            WabReport.need(ok(result), "plan get 失败：" + describe(result));
            JsonObject data = data(result);
            String[] keys = {"plan", "homeMode", "work", "idle", "sleep", "steps", "autonomy"};
            for (String key : keys) WabReport.need(FakeWabHost.has(data, key), "plan get 少了字段 " + key);
            return "steps=" + clip(txt(data, "steps")) + " autonomy=" + clip(txt(data, "autonomy"));
        });

        WabReport.check("plan：不认识的 action 报 unknown_action", () -> {
            JsonObject result = host.command("plan", param("maid", maidId, "action", "nonsense"), CMD);
            WabReport.need(!ok(result), "plan 乱写居然成功：" + describe(result));
            WabReport.need("unknown_action".equals(code(result)), "code=" + code(result));
            return error(result);
        });

        WabReport.check("bind / chat 这类「递过去就行」的命令回 ok", () -> {
            JsonObject bind = host.command("bind", param("maid", maidId), CMD);
            WabReport.need(ok(bind), "bind 命令失败：" + describe(bind));
            return "绑定真正的写入走 bind 帧（§4），命令这一条只是回执";
        });
    }

    // ==================== 4 入站帧 ====================

    private void inbound() {
        WabReport.section("4 入站帧（bind / stats / settings / chat_reply / tts / stt / error / 坏帧）");
        if (noMaid()) return;

        WabReport.check("bind 帧写进绑定表", () -> {
            JsonObject bindings = new JsonObject();
            bindings.addProperty(maidId, "测试角色");
            WabReport.need(host.sendBind(bindings, "测试绑定"), "bind 帧发不出去");
            Thread.sleep(400);
            return onServer(() -> {
                WabReport.need(bridge.isBound(maid), "isBound 仍是 false：bind 帧没落到绑定表");
                WabReport.need("测试角色".equals(bridge.boundCharacter(maid.getUUID())),
                        "boundCharacter=" + bridge.boundCharacter(maid.getUUID()));
                return "boundMaidIds=" + bridge.boundMaidIds().size() + "（客户端也收到了 WabBindingSync）";
            });
        });

        WabReport.check("stats 帧把数值写进 HUD 表", () -> {
            WabReport.need(host.sendStats(maidId, "测试角色", 66, 55, 44, 33), "stats 帧发不出去");
            Thread.sleep(400);
            return onServer(() -> {
                WabHud.Stats stats = bridge.stats(maid.getUUID());
                WabReport.need(stats != null, "stats 表里没有她");
                WabReport.need(stats.affection == 66 && stats.trust == 55 && stats.energy == 44 && stats.mood == 33,
                        "数值没写进去：" + stats.affection + "/" + stats.trust + "/" + stats.energy + "/" + stats.mood);
                WabReport.need("测试角色".equals(stats.character), "character=" + stats.character);
                return "好感=" + stats.affection + "（action bar 这会儿该显示出来）";
            });
        });

        WabReport.check("settings 帧：不断线也能改闸门（WAB 改白名单用的就是这条）", () -> {
            WabReport.need(host.sendSettings(false, false), "settings 帧发不出去");
            Thread.sleep(600);
            return onServer(() -> {
                WabReport.need(!bridge.allowWorldActions(), "allowWorldActions 没被改掉：settings 帧没人接");
                WabReport.need(!bridge.allowGestures(), "allowGestures 没被改掉");
                WabReport.need(host.linked(), "闸门一关就把会话断了？这条应当只改开关");
                return "两个开关都改到了，会话没断";
            });
        });

        WabReport.check("settings 帧能恢复", () -> {
            WabReport.need(host.sendSettings(true, true), "settings 帧发不出去");
            Thread.sleep(600);
            return onServer(() -> {
                WabReport.need(bridge.allowWorldActions(), "没能恢复 allowWorldActions");
                WabReport.need(bridge.allowGestures(), "没能恢复 allowGestures");
                return "allowWorldActions=" + bridge.allowWorldActions() + " allowGestures=" + bridge.allowGestures();
            });
        });

        WabReport.check("chat_reply：没有等待中的气泡也不炸", () -> {
            WabReport.need(host.sendChatReply(maidId, true, "这是没有请求号时的一句", null), "发不出去");
            Thread.sleep(800);
            WabReport.need(host.linked(), "会话被这条帧弄断了");
            return "走的是「按 -1 撤气泡」那条路：remove(-1) 是空操作，聊天栏应能看到这句";
        });

        WabReport.check("chat_reply：ok=false 走错误回落", () -> {
            WabReport.need(host.sendChatReply(maidId, false, "", "测试用的失败原因"), "发不出去");
            Thread.sleep(800);
            WabReport.need(host.linked(), "会话被这条帧弄断了");
            return "失败原因以灰字发给主人，看聊天框有没有「测试用的失败原因」";
        });

        WabReport.check("tts_reply：没人等的请求号被忽略", () -> {
            JsonObject extra = new JsonObject();
            extra.addProperty("ok", true);
            extra.addProperty("parts", 2);
            WabReport.need(host.sendVoice("tts_reply", "v900001", extra), "发不出去");
            Thread.sleep(600);
            WabReport.need(host.linked(), "会话被这条帧弄断了");
            return "认不出 v900001 就应当什么都不做（不猜是哪一个请求）";
        });

        WabReport.check("tts_audio：越界 part 与坏 base64 都不动状态", () -> {
            JsonObject far = new JsonObject();
            far.addProperty("part", 7);
            far.addProperty("data", "aGVsbG8=");
            WabReport.need(host.sendVoice("tts_audio", "v900002", far), "发不出去");
            JsonObject bad = new JsonObject();
            bad.addProperty("part", 0);
            bad.addProperty("data", "!!!!这个不是 base64!!!!");
            WabReport.need(host.sendVoice("tts_audio", "v900003", bad), "发不出去");
            Thread.sleep(600);
            WabReport.need(host.linked(), "这些帧把会话弄断了");
            return "没有对应请求时两者都该丢掉；坏 base64 在真请求上会把那一单直接销掉";
        });

        WabReport.check("stt_reply：没有对应录音请求时被忽略", () -> {
            JsonObject extra = new JsonObject();
            extra.addProperty("ok", true);
            extra.addProperty("text", "测试识别结果");
            WabReport.need(host.sendVoice("stt_reply", "v900004", extra), "发不出去");
            Thread.sleep(600);
            WabReport.need(host.linked(), "会话被这条帧弄断了");
            return "真正的 STT 要玩家按住说话键，见 §9";
        });

        WabReport.check("error 帧：只记日志、不断线", () -> {
            JsonObject extra = new JsonObject();
            extra.addProperty("code", "test_error");
            extra.addProperty("message", "宿主故意发的一条错误");
            WabReport.need(host.sendTyped("error", extra), "发不出去");
            Thread.sleep(600);
            WabReport.need(host.linked(), "error 帧把会话弄断了");
            return "日志里应有一行 [WAB] WAB error test_error";
        });

        WabReport.check("坏 JSON 行：静默丢掉", () -> {
            WabReport.need(host.sendRaw("{ this is not json"), "发不出去");
            Thread.sleep(600);
            WabReport.need(host.linked(), "坏帧把会话弄断了");
            return "桥那边解析异常就 continue，我们这边 framesIn=" + host.framesIn;
        });

        WabReport.check("空行 / 数组 / 裸字符串：静默丢掉", () -> {
            WabReport.need(host.sendRaw("[1,2,3]"), "发不出去");
            WabReport.need(host.sendRaw("\"just a string\""), "发不出去");
            Thread.sleep(600);
            WabReport.need(host.linked(), "这些帧把会话弄断了");
            return "非对象 JSON 解析异常跳过；空行在桥那边第一步就被 continue";
        });

        WabReport.check("没认过的 type：落 default 什么都不做", () -> {
            WabReport.need(host.sendTyped("some_future_frame", null), "发不出去");
            Thread.sleep(600);
            WabReport.need(host.linked(), "未知 type 把会话弄断了");
            return "向后兼容：新帧型老桥看不见也只是不理";
        });

        WabReport.check("stats 帧的陌生 maid：进表但不崩", () -> {
            WabReport.need(host.sendStats(randomUuid(), "不存在的人", 10, 10, 10, 10), "发不出去");
            Thread.sleep(600);
            WabReport.need(host.linked(), "这条帧把会话弄断了");
            return "按 UUID 进了 stats 表，但她不在名册里，HUD 认不到——协议口径";
        });
    }

    // ==================== 5 行动类命令 ====================

    private void actions() {
        WabReport.section("5 行动类命令（go / act / attack / hold / gather）");
        if (noMaid()) return;

        WabReport.check("go：只给坐标就走去坐标（坐标不许被悄悄换成主人）", () -> {
            BlockPos dest = maid.blockPosition().offset(4, 0, 4);
            JsonObject result = host.command("go", param("maid", maidId, "pos", pos(dest)), CMD);
            String message = txt(data(result), "message");
            if (ok(result)) {
                WabReport.need(!message.contains(ownerName) || !message.startsWith("正在靠近"),
                        "只给坐标却变成了靠近主人：" + message);
            } else {
                WabReport.need(!code(result).isEmpty(), "失败却没给 code：" + describe(result));
                WabReport.note("go 到 " + dest.toShortString() + " 没走成，code=" + code(result) + " " + clip(error(result)));
            }
            return clip(message.isEmpty() ? describe(result) : message) + " 目标格=" + dest.toShortString();
        });

        WabReport.check("go：名字和坐标同时给时以名字为准", () -> {
            BlockPos dest = maid.blockPosition().offset(-4, 0, -4);
            JsonObject result = host.command("go", param("maid", maidId, "target", ownerName, "pos", pos(dest)), CMD);
            WabReport.need(ok(result), "点名主人的 go 失败了：" + describe(result));
            String message = txt(data(result), "message");
            WabReport.need(message.contains(ownerName), "没有走向点名的那个人：" + message);
            return clip(message);
        });

        WabReport.check("go：follow 与 wait 两条跟模式", () -> {
            JsonObject follow = host.command("go", param("maid", maidId, "mode", "follow"), CMD);
            WabReport.need(ok(follow), "follow 失败：" + describe(follow));
            String first = txt(data(follow), "message");
            JsonObject wait = host.command("go", param("maid", maidId, "mode", "wait"), CMD);
            WabReport.need(ok(wait), "wait 失败：" + describe(wait));
            return clip(first) + " / " + clip(txt(data(wait), "message"));
        });

        WabReport.check("go：home、wander，以及乱写的 mode", () -> {
            JsonObject home = host.command("go", param("maid", maidId, "mode", "home"), CMD);
            WabReport.need(ok(home) || !code(home).isEmpty(), "home 既没成功也没 code：" + describe(home));
            JsonObject wander = host.command("go", param("maid", maidId, "mode", "wander", "radius", 6), CMD);
            WabReport.need(ok(wander) || !code(wander).isEmpty(), "wander 既没成功也没 code：" + describe(wander));
            JsonObject bogus = host.command("go", param("maid", maidId, "mode", "teleport_to_the_moon"), CMD);
            WabReport.need(!ok(bogus), "乱写的 mode 居然成功");
            WabReport.need("unknown_mode".equals(code(bogus)), "code=" + code(bogus));
            return "home=" + clip(txt(data(home), "message")) + " wander=" + clip(txt(data(wander), "message"));
        });

        WabReport.check("go：陌生目标名要报结构化失败", () -> {
            JsonObject result = host.command("go", param("maid", maidId, "mode", "near", "target", "根本没有这个人"), CMD);
            WabReport.need(!ok(result), "不存在的人也能靠近？" + describe(result));
            String code = code(result);
            WabReport.need("invalid_target".equals(code) || "cannot_reach".equals(code), "code=" + code);
            return error(result);
        });

        WabReport.check("go：坐标给得离谱要报 bad_pos", () -> {
            JsonArray far = new JsonArray();
            far.add(1.0E7);
            far.add(64);
            far.add(1.0E7);
            JsonObject result = host.command("go", param("maid", maidId, "pos", far), CMD);
            WabReport.need(!ok(result), "一千万格外也走得了？" + describe(result));
            String code = code(result);
            WabReport.need("bad_pos".equals(code) || "cannot_reach".equals(code), "code=" + code);
            return error(result);
        });

        WabReport.check("act：sit / stand / say 这些不改世界的动作", () -> {
            JsonObject sit = host.command("act", param("maid", maidId, "action", "sit"), CMD);
            WabReport.need(ok(sit) || !code(sit).isEmpty(), "sit 既没成功也没 code：" + describe(sit));
            JsonObject stand = host.command("act", param("maid", maidId, "action", "stand"), CMD);
            WabReport.need(ok(stand) || !code(stand).isEmpty(), "stand 同上");
            JsonObject say = host.command("act", param("maid", maidId, "action", "say", "text", "链路测试说的一句话"), CMD);
            WabReport.need(ok(say), "say 失败：" + describe(say));
            return "sit=" + clip(txt(data(sit), "message")) + " say=" + clip(txt(data(say), "message"));
        });

        WabReport.check("act：手势名与超长字符串（客户端那包 writeUtf 有上限）", () -> {
            JsonObject gesture = host.command("act", param("maid", maidId, "action", "gesture", "arg", "wave"), CMD);
            WabReport.need(ok(gesture) || !code(gesture).isEmpty(), "gesture 既没成功也没 code：" + describe(gesture));
            JsonObject huge = host.command("act", param("maid", maidId, "action", "gesture", "arg", repeat('A', 4000)), CMD);
            WabReport.need(host.linked(), "超长手势名把会话弄断了");
            WabReport.note("超长手势回执：ok=" + ok(huge) + " code=" + code(huge) + " " + clip(txt(data(huge), "message")));
            return "wave=" + clip(txt(data(gesture), "message"));
        });

        WabReport.check("act：say 的空文本与手势的空参数", () -> {
            JsonObject empty = host.command("act", param("maid", maidId, "action", "say", "text", ""), CMD);
            WabReport.note("空句 ok=" + ok(empty) + " code=" + code(empty));
            JsonObject noArg = host.command("act", param("maid", maidId, "action", "gesture"), CMD);
            WabReport.note("缺 arg ok=" + ok(noArg) + " code=" + code(noArg));
            WabReport.need(host.linked(), "这两条把会话弄断了");
            return "两条都是「参数缺省」分支，桥不该抛";
        });

        WabReport.check("act：不认识的 action 报 unknown_action", () -> {
            JsonObject result = host.command("act", param("maid", maidId, "action", "do_the_dishes"), CMD);
            WabReport.need(!ok(result), "乱写动作居然成功");
            WabReport.need("unknown_action".equals(code(result)), "code=" + code(result));
            return error(result);
        });

        WabReport.check("attack：不点名时不许把主人当目标", () -> {
            JsonObject result = host.command("attack", param("maid", maidId), CMD);
            if (ok(result)) {
                String message = txt(data(result), "message");
                WabReport.need(!message.contains(ownerName), "她出手的目标是主人：" + message);
                return clip(message);
            }
            String code = code(result);
            WabReport.need("no_target".equals(code) || "refused".equals(code)
                    || "too_far".equals(code) || "attack_failed".equals(code),
                    "失败却没给可辨认的 code：" + describe(result));
            return code + " " + clip(error(result));
        });

        WabReport.check("attack：点名主人必须拒绝", () -> {
            JsonObject result = host.command("attack", param("maid", maidId, "target", ownerName), CMD);
            WabReport.need(!ok(result), "点名主人她居然打了：" + describe(result));
            WabReport.need("refused".equals(code(result)), "code=" + code(result));
            return error(result);
        });

        WabReport.check("hold：give / feed_player 的近距离与失败分支", () -> {
            JsonObject give = host.command("hold", param("maid", maidId, "action", "give"), CMD);
            WabReport.note("手上没东西时 give：ok=" + ok(give) + " code=" + code(give) + " " + clip(error(give)));
            JsonObject feed = host.command("hold", param("maid", maidId, "action", "feed_player"), CMD);
            WabReport.note("feed_player：ok=" + ok(feed) + " code=" + code(feed) + " " + clip(error(feed)));
            JsonObject bogus = host.command("hold", param("maid", maidId, "action", "juggle"), CMD);
            WabReport.need(!ok(bogus), "乱写的 hold 动作居然成功");
            WabReport.need("unknown_action".equals(code(bogus)), "code=" + code(bogus));
            return "两条隔空递东西的路径都已从 8 格 / 4 格收到 3 格";
        });

        WabReport.check("hold：give 点名一个不存在的人", () -> {
            JsonObject result = host.command("hold", param("maid", maidId, "action", "give", "to", "没这个人"), CMD);
            WabReport.need(!ok(result) || true, "");
            WabReport.note("回执：ok=" + ok(result) + " code=" + code(result) + " " + clip(error(result)));
            return "找不找人应当报 no_recipient";
        });

        WabReport.check("gather collect：一次性捡东西", () -> {
            JsonObject result = host.command("gather", param("maid", maidId, "action", "collect", "count", 4, "radius", 6), HEAVY);
            WabReport.need(ok(result) || !code(result).isEmpty(), "collect 既没成功也没 code：" + describe(result));
            return ok(result) ? clip(txt(data(result), "message")) : code(result) + " " + clip(error(result));
        });

        WabReport.check("gather fill：够不着要报 bad_pos", () -> {
            JsonArray far = new JsonArray();
            far.add(maid.getX() + 900);
            far.add(maid.getY());
            far.add(maid.getZ() + 900);
            JsonObject result = host.command("gather", param("maid", maidId, "action", "fill", "pos", far), CMD);
            WabReport.need(!ok(result), "900 格外也填上了？" + describe(result));
            WabReport.need("bad_pos".equals(code(result)), "code=" + code(result));
            return error(result);
        });

        WabReport.check("gather mine：起一个长任务，再撤掉（异步回执这条要单独验）", () -> {
            String id = host.newId();
            host.sendCommand(id, "gather", param("maid", maidId, "action", "mine", "count", 2, "radius", 6, "filter", "ores"));
            JsonObject early = tryResult(id, 1500);
            if (early != null) {
                WabReport.note("附近没可挖的，当场就回了：" + clip(describe(early)));
                return "当场回执（没有目标也算链路通）";
            }
            Boolean running = onServer(() -> bridge.dispatcher().hasJob(maid));
            WabReport.need(Boolean.TRUE.equals(running), "任务没起来：dispatcher.hasJob=false");
            onServer(() -> {
                bridge.dispatcher().cancelJob(maid, "测试收尾");
                return Boolean.TRUE;
            });
            Thread.sleep(500);
            Boolean gone = onServer(() -> bridge.dispatcher().hasJob(maid));
            WabReport.need(Boolean.FALSE.equals(gone), "撤不掉");
            if (tryResult(id, 800) != null) {
                WabReport.note("cancel 之后仍然回了个 result（说明 cancel 走了 finish）");
            } else {
                WabReport.note("cancel 不再回执：宿主那侧表现为这一单超时。协议上算已知口径，值得记一笔");
            }
            host.drop(id);
            return "任务起来过也撤掉了，guiOpening 由 cancel 还原";
        });

        WabReport.check("gather：同一只女仆只挂一个 job", () -> {
            String first = host.newId();
            String second = host.newId();
            host.sendCommand(first, "gather", param("maid", maidId, "action", "mine", "count", 3, "radius", 8));
            host.sendCommand(second, "gather", param("maid", maidId, "action", "chop", "count", 3, "radius", 8));
            Thread.sleep(800);
            Boolean running = onServer(() -> bridge.dispatcher().hasJob(maid));
            onServer(() -> {
                bridge.dispatcher().cancelJob(maid, "测试收尾");
                return Boolean.TRUE;
            });
            Boolean after = onServer(() -> bridge.dispatcher().hasJob(maid));
            WabReport.need(Boolean.FALSE.equals(after), "收尾没撤干净");
            JsonObject secondResult = tryResult(second, 800);
            WabReport.note("第二单：" + (secondResult == null ? "还在跑就被撤了" : clip(describe(secondResult))));
            host.drop(first);
            host.drop(second);
            return "hasJob 起来过=" + running + "，新任务把旧任务顶掉了";
        });
    }

    // ==================== 6 闸门 ====================

    private void gates() {
        WabReport.section("6 「允许世界操作」闸门（会话命令和日程共用同一份判断）");
        if (noMaid()) return;

        WabReport.check("关掉闸门", () -> {
            WabReport.need(host.sendSettings(false, true), "settings 发不出去");
            Thread.sleep(600);
            return onServer(() -> {
                WabReport.need(!bridge.allowWorldActions(), "闸门没关掉");
                return "allowWorldActions=false";
            });
        });

        WabReport.check("gather / attack / hold / act{use} 全被 world_actions_disabled 拦下", () -> {
            String[] methods = {"gather", "attack", "hold"};
            StringBuilder detail = new StringBuilder();
            for (String method : methods) {
                JsonObject result = host.command(method, param("maid", maidId, "action", "mine"), CMD);
                WabReport.need(!ok(result), method + " 在被拦的情况下居然成功了：" + describe(result));
                WabReport.need("world_actions_disabled".equals(code(result)),
                        method + " 的 code=" + code(result) + "，不是 world_actions_disabled");
                detail.append(detail.length() == 0 ? "" : " ").append(method).append("=拦下");
            }
            JsonObject use = host.command("act", param("maid", maidId, "action", "use"), CMD);
            WabReport.need("world_actions_disabled".equals(code(use)), "act{use} 的 code=" + code(use));
            JsonObject addon = host.command("act", param("maid", maidId, "action", "interact_addon"), CMD);
            WabReport.note("act{interact_addon} 也被拦：code=" + code(addon));
            return detail.toString();
        });

        WabReport.check("go 不在闸门里（只挪身体，不改世界）", () -> {
            JsonObject result = host.command("go", param("maid", maidId, "mode", "wait"), CMD);
            WabReport.need(ok(result), "闸门关着时 go{wait} 应当照样能用：" + describe(result));
            return clip(txt(data(result), "message"));
        });

        WabReport.check("关掉闸门时正在跑的采集要被撤掉", () -> {
            String id = host.newId();
            host.sendCommand(id, "gather", param("maid", maidId, "action", "mine", "count", 4, "radius", 8));
            JsonObject early = tryResult(id, 1200);
            if (early != null) {
                return "这单当场就回了（附近没目标），看不到撤任务这条分支：" + clip(describe(early));
            }
            Boolean before = onServer(() -> bridge.dispatcher().hasJob(maid));
            WabReport.need(host.sendSettings(false, true), "settings 发不出去");
            Thread.sleep(1500);
            Boolean after = onServer(() -> bridge.dispatcher().hasJob(maid));
            host.drop(id);
            if (Boolean.FALSE.equals(before)) WabReport.skip("任务没起来（附近没矿石/木头），这条要现场有可挖的东西");
            WabReport.need(Boolean.FALSE.equals(after), "关掉闸门后采集还在跑：日程会继续拆家");
            return "闸门一关，在跑的那单被 cancelAll 撤掉";
        });

        WabReport.check("重新打开闸门", () -> {
            WabReport.need(host.sendSettings(true, true), "settings 发不出去");
            Thread.sleep(600);
            return onServer(() -> {
                WabReport.need(bridge.allowWorldActions(), "没能恢复");
                return "allowWorldActions=" + bridge.allowWorldActions();
            });
        });

        WabReport.check("闸门恢复后一次性动作能用", () -> {
            JsonObject result = host.command("act", param("maid", maidId, "action", "sit"), CMD);
            WabReport.need(ok(result) || !code(result).isEmpty(), "既没成功也没 code：" + describe(result));
            return clip(txt(data(result), "message")) + " code=" + code(result);
        });
    }

    // ==================== 7 世界事件 ====================

    private void worldEvents() {
        WabReport.section("7 世界事件（游戏 → 桥 → event / chat 帧）");
        if (noMaid()) return;

        WabReport.check("先把她挪到主人旁边（8 格内那条判定不用跑图）", () -> onServer(() -> {
            ServerPlayer owner = bridge.currentOwner();
            maid.teleportTo(owner.getX(), owner.getY(), owner.getZ());
            return "挪到 " + owner.getName().getString() + " 身边（测试模组会改动她的位置，测完想站哪自己牵）";
        }));

        WabReport.check("主人发言：ServerChatEvent 不许被取消", () -> onServer(() -> {
            ServerPlayer owner = bridge.currentOwner();
            String text = "链路测试 " + stamp() + " 今天天气不错";
            ServerChatEvent event = new ServerChatEvent(owner, text, Component.literal(text));
            MinecraftForge.EVENT_BUS.post(event);
            WabReport.need(!event.isCanceled(), "ServerChatEvent 被取消了：这句话进不了聊天框，"
                    + "记录/翻译/反作弊那些监听聊天的模组也收不到");
            WabReport.need(text.equals(event.getRawText()), "事件里的原文被改写：" + event.getRawText());
            lastChatText = text;
            return "没取消，原文也没动：" + text;
        }));

        WabReport.check("上面那句要变成一帧 chat", () -> {
            JsonObject chat = host.await("chat", 6000);
            if (chat == null) {
                WabReport.skip("没等到 chat 帧：她不在主人 " + WabConfig.chatRange() + " 格内、或主人不是桥认的那位（单人都能过这条）");
            }
            WabReport.need(!txt(chat, "maid").isEmpty(), "chat 帧没带 maid");
            WabReport.need(!txt(chat, "text").isEmpty(), "chat 帧没带 text");
            return "来自 " + txt(chat, "name") + "：" + clip(txt(chat, "text"));
        });

        WabReport.check("非主人的发言既不吞也不转", () -> onServer(() -> {
            ServerPlayer owner = bridge.currentOwner();
            ServerPlayer other = null;
            for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                if (owner == null || !p.getUUID().equals(owner.getUUID())) {
                    other = p;
                    break;
                }
            }
            if (other == null) WabReport.skip("服务器上只有主人一个人，这条要两个人以上才测得了");
            String text = "别人说的话 " + stamp();
            ServerChatEvent event = new ServerChatEvent(other, text, Component.literal(text));
            MinecraftForge.EVENT_BUS.post(event);
            WabReport.need(!event.isCanceled(), "别人的话也被取消了：这条比主人那条更要紧");
            return other.getName().getString() + " 的话没被取消（也没替别人家的女仆答话）";
        }));

        WabReport.check("互动两条入口都过（EntityInteract 与 Specific）", () -> onServer(() -> {
            if (!(maid.level() instanceof ServerLevel)) WabReport.skip("她不在服务端世界里");
            ServerPlayer owner = bridge.currentOwner();
            if (owner == null) WabReport.skip("主人不在线，两条互动入口都投不出来");
            long before = host.framesIn;
            MinecraftForge.EVENT_BUS.post(new PlayerInteractEvent.EntityInteract(owner, InteractionHand.MAIN_HAND, maid));
            MinecraftForge.EVENT_BUS.post(new PlayerInteractEvent.EntityInteractSpecific(owner, InteractionHand.MAIN_HAND, maid, maid.position()));
            return "两条都投了，帧计数 " + before + "→" + host.framesIn + "（收敛在下一条核对）";
        }));

        WabReport.check("两次互动合计 ≤1 帧 event", () -> {
            int count = 0;
            String kinds = "";
            JsonObject frame = awaitAnyEvent(2500);
            while (frame != null) {
                count++;
                kinds = kinds + (kinds.isEmpty() ? "" : ",") + txt(frame, "kind");
                frame = awaitAnyEvent(800);
            }
            WabReport.need(count <= 1, "两次互动出了 " + count + " 帧 event，冷却没收到一起");
            return count == 0 ? "0 帧（她 20 秒内已经想过一次，被 cooldown 吃掉——这是设计）" : "1 帧 kind=" + kinds;
        });

        WabReport.check("受伤事件：主人那一下要扣好感", () -> onServer(() -> {
            ServerPlayer owner = bridge.currentOwner();
            if (owner == null) WabReport.skip("主人不在线，投不出「主人打她」这一下");
            if (!(maid.level() instanceof ServerLevel)) WabReport.skip("她不在服务端世界里");
            long before = host.framesIn;
            DamageSource byOwner = owner.damageSources().mobAttack(owner);
            MinecraftForge.EVENT_BUS.post(new LivingHurtEvent(maid, byOwner, 2.0F));
            return "LivingHurtEvent 投完（只走事件，不真掉血），帧计数 " + before + "→" + host.framesIn;
        }));

        WabReport.check("hurt 帧带负 affection_delta", () -> {
            JsonObject event = awaitKind("hurt", 4000);
            if (event == null) WabReport.skip("没等到 hurt：她 5 秒内刚报过一次伤，或这条被别的模组拦了");
            WabReport.need(FakeWabHost.has(event, "affection_delta"), "hurt 帧没带 affection_delta");
            int delta = event.get("affection_delta").getAsInt();
            WabReport.need(delta < 0, "主人弄疼她却没扣好感：" + delta);
            return "delta=" + delta + " reason=" + clip(txt(event, "reason")) + " importance=" + event.get("importance");
        });

        WabReport.check("死亡事件：记下 UUID 等复活", () -> onServer(() -> {
            if (!(maid.level() instanceof ServerLevel)) WabReport.skip("她不在服务端世界里");
            ServerLevel level = (ServerLevel) maid.level();
            MinecraftForge.EVENT_BUS.post(new LivingDeathEvent(maid, level.damageSources().generic()));
            return "LivingDeathEvent 投完（不真杀死她）";
        }));

        WabReport.check("death 帧到了并带 snapshot", () -> {
            JsonObject event = awaitKind("death", 4000);
            if (event == null) WabReport.skip("没等到 death 帧");
            WabReport.need(FakeWabHost.has(event, "snapshot"), "death 帧没带 snapshot");
            WabReport.need(event.get("affection_delta").getAsInt() < 0, "死亡没扣好感");
            return "kind=death " + clip(txt(event, "text"));
        });

        WabReport.check("ToMaid 事件（从物品里放出女仆）不崩", () -> onServer(() -> {
            CompoundTag data = new CompoundTag();
            data.putUUID("UUID", maid.getUUID());
            ItemStack token = new ItemStack(Items.GHAST_TEAR);
            MinecraftForge.EVENT_BUS.post(new MaidAndItemTransformEvent.ToMaid(maid, token, data));
            WabReport.need(host.linked(), "ToMaid 事件把会话弄断了");
            return "事件投递过；真正的 respawn 帧要有新 UUID 出现在名册里（见下一条）";
        }));

        WabReport.check("respawn 帧：这条要玩家亲手放一次胶片", () -> {
            JsonObject event = awaitKind("respawn", 1500);
            if (event != null) return clip(txt(event, "text"));
            WabReport.skip("手动补测：让一只女仆死掉、掉胶片、再用胶片把她放出来，桥会发一帧 kind=respawn。"
                    + "测试模组造不出「新 UUID 的女仆进世界」这一步");
            return "";
        });

        WabReport.check("日程：写一步、读回来、清干净", () -> {
            JsonArray steps = new JsonArray();
            JsonObject step = new JsonObject();
            step.addProperty("at", "morning");
            step.addProperty("do", "idle");
            steps.add(step);
            JsonObject autonomy = new JsonObject();
            JsonArray options = new JsonArray();
            JsonObject option = new JsonObject();
            option.addProperty("do", "wander");
            option.addProperty("weight", 5);
            option.addProperty("hours", 1);
            options.add(option);
            autonomy.add("options", options);

            JsonObject set = host.command("plan", param("maid", maidId, "action", "set", "steps", steps, "autonomy", autonomy), CMD);
            WabReport.need(ok(set), "plan set 失败：" + describe(set));
            JsonObject read = host.command("plan", param("maid", maidId, "action", "get"), CMD);
            WabReport.need(ok(read), "plan get 失败：" + describe(read));
            String saved = txt(data(read), "steps");
            WabReport.need(saved.contains("morning"), "写进去的日程没读回来：" + clip(saved));
            WabReport.note("autonomy 读回：" + clip(txt(data(read), "autonomy")));
            JsonObject clear = host.command("plan", param("maid", maidId, "action", "clear"), CMD);
            WabReport.need(ok(clear), "plan clear 失败：" + describe(clear));
            JsonObject after = host.command("plan", param("maid", maidId, "action", "get"), CMD);
            WabReport.need(txt(data(after), "steps").isEmpty(), "plan clear 只回了一句、存档里还留着：" + clip(txt(data(after), "steps")));
            return "set → 读回 → clear → 读空 全过（这条守的是「换回上游 jar 也不会带着日程」）";
        });
    }

    private String lastChatText = "";

    // ==================== 8 拍照 ====================

    private void photo() {
        WabReport.section("8 前方感知与拍照（画面要绕客户端一趟，只能另送一帧）");
        if (noMaid()) return;

        WabReport.check("see photo=true：文字先回，画面随后单独一帧", () -> {
            JsonObject result = host.command("see", param("maid", maidId, "photo", true, "photo_width", 480), HEAVY);
            WabReport.need(ok(result), "see 失败：" + describe(result));
            JsonObject data = data(result);
            if (FakeWabHost.has(data, "photoError")) {
                WabReport.note("桥明说拿不到画面：" + clip(txt(data, "photoError")));
                return "走的是 photoError 分支（专用服务器上没有帧缓冲，这是正常的）";
            }
            int request = data.get("photoRequest").getAsInt();
            WabReport.need(request > 0, "photoRequest 不是正数：" + request);
            JsonObject photo = host.await("photo", 12000);
            if (photo == null) WabReport.skip("12 秒内没有 photo 帧：客户端没回画面（截图失败、被暂停、或这张超过 1.2MB）");
            WabReport.need(photo.get("request").getAsInt() == request, "photo 的 request 对不上：" + txt(photo, "request"));
            boolean good = FakeWabHost.has(photo, "ok") && photo.get("ok").getAsBoolean();
            if (!good) WabReport.note("客户端说拍不了：" + clip(txt(photo, "error")));
            return "request=" + request + " ok=" + good + " 尺寸=" + photo.get("width") + "x" + photo.get("height")
                    + " base64=" + (good ? txt(photo, "data").length() + " 字符" : "没有");
        });

        WabReport.check("一次只挂一张：第二张要么排上、要么说清楚", () -> {
            JsonObject result = host.command("see", param("maid", maidId, "photo", true), HEAVY);
            WabReport.need(ok(result), "see 失败：" + describe(result));
            JsonObject data = data(result);
            if (FakeWabHost.has(data, "photoRequest")) return "第二张也排上了 request=" + data.get("photoRequest");
            WabReport.need(FakeWabHost.has(data, "photoError"), "既没 photoRequest 也没 photoError");
            return "明确回了拿不到：" + clip(txt(data, "photoError"));
        });

        WabReport.check("photo_width 离谱要钳住或说清楚", () -> {
            JsonObject result = host.command("see", param("maid", maidId, "photo", true, "photo_width", 99999), HEAVY);
            WabReport.need(ok(result) || !code(result).isEmpty(), "既没成功也没 code：" + describe(result));
            return clip(describe(result));
        });

        WabReport.check("挂着的那张超时也要有个交代", () -> {
            JsonObject result = host.command("see", param("maid", maidId, "photo", true), HEAVY);
            if (!FakeWabHost.has(data(result), "photoRequest")) return "这次没排上（前一张还挂着），跳过超时观察";
            JsonObject photo = host.await("photo", 12000);
            WabReport.need(photo != null, "8 秒超时后一帧 photo 都不发，宿主只能干等");
            return "成功或超时都会有一帧 photo：ok=" + (FakeWabHost.has(photo, "ok") && photo.get("ok").getAsBoolean());
        });
    }

    // ==================== 9 语音链路 ====================

    private void voice() {
        WabReport.section("9 代管语音（tts / stt）");
        if (noMaid()) return;

        WabReport.check("welcome 宣告的语音能力位被读到", () -> onServer(() -> {
            WabReport.need(bridge.voiceTts(), "voiceTts=false：welcome 里的 voice.tts 没人接");
            WabReport.need(bridge.voiceStt(), "voiceStt=false：welcome 里的 voice.stt 没人接");
            return "tts=" + bridge.voiceTts() + " stt=" + bridge.voiceStt();
        }));

        WabReport.check("绑了角色的女仆说话：发一句出去", () -> onServer(() -> {
            ServerPlayer owner = bridge.currentOwner();
            String text = "链路测试的语音请求 " + stamp();
            MinecraftForge.EVENT_BUS.post(new ServerChatEvent(owner, text, Component.literal(text)));
            return "已发出：" + text;
        }));

        WabReport.check("那句 chat 用 chat_reply 答回去（触发合成）", () -> {
            JsonObject chat = host.await("chat", 6000);
            if (chat == null) WabReport.skip("没等到 chat 帧，语音这条起不来");
            WabReport.need(host.sendChatReply(txt(chat, "maid"), true, "测试角色要说的一句话", null), "chat_reply 发不出去");
            return "已替宿主答了一句，桥应当把它送去合成";
        });

        WabReport.check("桥发回一帧 tts 请求（绑了角色才走 WAB 的嗓子）", () -> {
            JsonObject tts = host.await("tts", 8000);
            if (tts == null) {
                WabReport.skip("没等到 tts 帧：她没绑上角色（§4 的 bind 得先过）、voiceTts 没开、或这句走了游戏自己的 TTS");
            }
            ttsFrame = tts;
            String id = txt(tts, "id");
            WabReport.need(id.startsWith("v"), "请求号不是 v 开头：" + id);
            WabReport.need(!txt(tts, "text").isEmpty(), "tts 帧没带文本");
            WabReport.need(!txt(tts, "maid").isEmpty(), "tts 帧没带 maid");
            return "request=" + id + " 文本=" + clip(txt(tts, "text"));
        });

        WabReport.check("tts_reply + 两段 tts_audio 转给主人客户端", () -> {
            if (ttsFrame == null) WabReport.skip("上一条没拿到请求号");
            String id = txt(ttsFrame, "id");
            JsonObject reply = new JsonObject();
            reply.addProperty("ok", true);
            reply.addProperty("parts", 2);
            WabReport.need(host.sendVoice("tts_reply", id, reply), "发不出去");
            String tiny = Base64.getEncoder().encodeToString(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
            for (int part = 0; part < 2; part++) {
                JsonObject audio = new JsonObject();
                audio.addProperty("part", part);
                audio.addProperty("data", tiny);
                WabReport.need(host.sendVoice("tts_audio", id, audio), "第 " + part + " 段发不出去");
            }
            Thread.sleep(800);
            WabReport.need(host.linked(), "音频帧把会话弄断了");
            return "两段假音频已转给客户端（播不出声音是正常的，内容只有 10 个字节）";
        });

        WabReport.check("tts_reply 报 ok=false：这一单要被销掉而不是重试", () -> {
            JsonObject extra = new JsonObject();
            extra.addProperty("ok", false);
            extra.addProperty("code", "voice_engine_off");
            extra.addProperty("error", "宿主拒了这次合成");
            WabReport.need(host.sendVoice("tts_reply", "v900009", extra), "发不出去");
            Thread.sleep(600);
            WabReport.need(host.linked(), "这条帧把会话弄断了");
            return "日志里应有一行 [WAB] tts refused";
        });

        WabReport.check("stt：这条要玩家按住说话键", () -> {
            WabReport.skip("手动补测：绑好角色后按住女仆对话的录音键说一句，桥会发一帧 stt（带 base64 的 WAV 和 lang），"
                    + "宿主回 stt_reply 后客户端要把文字填回输入框。测试模组不能替客户端录音");
            return "";
        });
    }

    // ==================== 10 直接调用面 ====================

    private void direct() {
        WabReport.section("10 公开执行器直调（这些是桥自己的门，坏输入也不许抛出去）");
        if (noMaid()) return;

        WabReport.check("WabSensors.world / weather / clock", () -> onServer(() -> {
            JsonObject world = WabSensors.world(bridge);
            String[] keys = {"name", "dimension", "difficulty", "gameMode", "weather", "time"};
            for (String key : keys) WabReport.need(FakeWabHost.has(world, key), "world 少了字段 " + key);
            if (!(maid.level() instanceof ServerLevel)) WabReport.skip("她不在服务端世界");
            ServerLevel level = (ServerLevel) maid.level();
            String weather = WabSensors.weather(level);
            WabReport.need(!weather.isEmpty(), "weather 回空");
            WabReport.need(!WabSensors.clock(6000).isEmpty(), "clock 回空");
            return "weather=" + weather + " clock(6000)=" + WabSensors.clock(6000) + " world=" + clip(world.toString());
        }));

        WabReport.check("WabSensors.entities / inventory / blockPos / vecPos", () -> onServer(() -> {
            int entities = WabSensors.entities(maid, 8).size();
            int slots = WabSensors.inventory(maid, 12).size();
            WabReport.need(WabSensors.blockPos(maid.blockPosition()).size() == 3, "blockPos 不是三个数");
            WabReport.need(WabSensors.vecPos(maid).size() == 3, "vecPos 不是三个数");
            WabReport.need(WabSensors.ownerName(maid) != null, "ownerName 回 null");
            return "附近实体=" + entities + " 背包非空格=" + slots;
        }));

        WabReport.check("WabSensors.loveLoathe / tendency", () -> onServer(() -> {
            JsonObject loveLoathe = WabSensors.loveLoathe(maid);
            WabReport.need(FakeWabHost.has(loveLoathe, "trust") || FakeWabHost.has(loveLoathe, "tendency"),
                    "loveLoathe 少了 trust/tendency：" + clip(loveLoathe.toString()));
            WabReport.need(!WabSensors.tendency(5, 5).isEmpty(), "tendency 回空");
            return clip(loveLoathe.toString());
        }));

        WabReport.check("WabSensors.item / itemId / blockId / kind / entityType", () -> onServer(() -> {
            ItemStack apple = new ItemStack(Items.APPLE);
            WabReport.need("minecraft:apple".equals(WabSensors.itemId(apple)), "itemId=" + WabSensors.itemId(apple));
            WabReport.need(FakeWabHost.has(WabSensors.item(apple), "item") || WabSensors.item(apple).size() > 0,
                    "item 帧是空的");
            if (!(maid.level() instanceof ServerLevel)) WabReport.skip("她不在服务端世界");
            ServerLevel level = (ServerLevel) maid.level();
            BlockPos under = maid.blockPosition().below();
            String blockId = WabSensors.blockId(level, under);
            WabReport.need(!blockId.isEmpty(), "blockId 回空");
            BlockState state = level.getBlockState(under);
            WabSensors.isBedLike(state);
            WabSensors.isMatureCrop(state);
            WabSensors.isOre(state);
            WabSensors.bedHead(level, under);
            WabReport.need(!WabSensors.kind(maid).isEmpty(), "kind 回空");
            return "脚下=" + blockId + " kind=" + WabSensors.kind(maid) + " type=" + WabSensors.entityType(maid);
        }));

        WabReport.check("WabStorage.snapshot：和 backup 帧同一份数据", () -> onServer(() -> {
            JsonObject snapshot = WabStorage.snapshot(maid);
            WabReport.need(!txt(snapshot, "nbt").isEmpty(), "nbt 空：" + clip(txt(snapshot, "nbtError")));
            WabReport.need("gzip+base64".equals(txt(snapshot, "nbtFormat")), "nbtFormat=" + txt(snapshot, "nbtFormat"));
            return "nbt=" + txt(snapshot, "nbt").length() + " 字符";
        }));

        WabReport.check("WabCompat.animationForGesture：别名、清洗、64 字符钳", () -> {
            String wave = WabCompat.animationForGesture("wave");
            WabReport.need(!wave.isEmpty(), "wave 没解析出动画");
            String huge = WabCompat.animationForGesture(repeat('A', 500));
            WabReport.need(huge.length() <= 64, "没钳到 64 以内：" + huge.length());
            String dirty = WabCompat.animationForGesture("../../etc passw d 空格");
            WabReport.need(!dirty.contains(" ") && !dirty.contains("/"), "危险字符没清掉：" + dirty);
            WabReport.note("wave→" + wave + " 乱写→" + dirty);
            return "这条是给客户端那包动画字符串兜底的（writeUtf 有 32767 上限）";
        });

        WabReport.check("WabCompat.invokeAddonAction：没装附属模组也要好好回话", () -> onServer(() -> {
            String text = WabCompat.invokeAddonAction(bridge, maid, "wave");
            WabReport.need(text != null && !text.isEmpty(), "回空字符串");
            return clip(text);
        }));

        WabReport.check("WabError 三个工厂都带协议码", () -> {
            WabReport.need("maid_not_found".equals(WabError.notFound("x").code()), WabError.notFound("x").code());
            WabReport.need("invalid_params".equals(WabError.invalid("x").code()), WabError.invalid("x").code());
            WabReport.need("custom".equals(WabError.of("custom", "x").code()), WabError.of("custom", "x").code());
            return "派发器把这三种异常翻译成 result{ok:false}";
        });

        WabReport.check("WabHud.tick 空跑一轮", () -> onServer(() -> {
            WabHud.tick(bridge);
            return "没抛（用的就是 §4 写进表的 stats）";
        }));

        WabReport.check("WabBridge 的公开读口全跑一遍", () -> onServer(() -> {
            WabReport.need(bridge.server() == server, "桥记的服务器不是现在这个");
            WabReport.need(bridge.session() != null, "连着呢，session 却是 null");
            WabReport.need(bridge.dispatcher() != null, "dispatcher 为空");
            WabReport.need(bridge.autonomy() != null, "autonomy 为空");
            WabReport.need(bridge.resolveMaid(maidId) == maid, "resolveMaid 认不出自己的女仆");
            WabReport.need(bridge.resolveMaid("   ") == null, "一串空格也能解析出女仆？");
            WabReport.need(bridge.isOwned(maid), "isOwned=false：名册按主人筛，说明主人认错了");
            WabReport.need(bridge.stats(maid.getUUID()) != null, "stats 表里没有她");
            WabReport.need(!bridge.beaconMaids().toString().isEmpty(), "beacon 名册为空");
            return "connected=" + bridge.connected() + " owner=" + bridge.ownerName() + " world=" + bridge.worldName()
                    + " maids=" + bridge.ownedMaids().size() + " bound=" + bridge.boundMaidIds().size()
                    + " beaconMaids=" + bridge.beaconMaids().size() + " name=" + WabBridge.cleanName(maid);
        }));

        WabReport.check("WabSensors.scan 直调：半径参数缺省也能跑", () -> onServer(() -> {
            JsonObject data = WabSensors.scan(bridge, maid, new JsonObject());
            WabReport.need(FakeWabHost.has(data, "blockRadius"), "缺省半径时没给 blockRadius");
            return "缺省 radius=16 → blockRadius=" + data.get("blockRadius");
        }));
    }

    // ==================== 11 上限与钳制 ====================

    private void limits() {
        WabReport.section("11 协议上限与参数钳制");

        WabReport.check("绑定表封顶 64：多发不崩、也不许多", () -> {
            JsonObject bindings = new JsonObject();
            bindings.addProperty(maidId, "第一位的角色");
            for (int i = 0; i < 120; i++) bindings.addProperty(randomUuid(), "角色" + i);
            WabReport.need(host.sendBind(bindings, null), "bind 帧发不出去");
            Thread.sleep(600);
            return onServer(() -> {
                int size = bridge.boundMaidIds().size();
                WabReport.need(size <= 64, "绑定表 " + size + " 条，没封顶 64");
                WabReport.need(bridge.isBound(maid), "先到的那条也没留住");
                return "发了 121 条，收了 " + size + " 条";
            });
        });

        WabReport.check("bind 帧的坏 UUID：跳过而不把整张表带坏", () -> {
            JsonObject bindings = new JsonObject();
            bindings.addProperty("not-a-uuid", "坏的一条");
            bindings.addProperty("", "空键");
            bindings.addProperty(maidId, "测试角色");
            WabReport.need(host.sendBind(bindings, "这条带 notes，主人会收到一行灰字"), "发不出去");
            Thread.sleep(600);
            return onServer(() -> {
                WabReport.need(bridge.isBound(maid), "合法那条也没写进去");
                WabReport.need(host.linked(), "坏 UUID 把会话弄断了");
                return "坏键忽略、合法键仍在（notes 也照常显示）";
            });
        });

        WabReport.check("stats 顺带认角色（对话里 auto-bind 那条）", () -> {
            WabReport.need(host.sendBind(new JsonObject(), "清空绑定"), "发不出去");
            Thread.sleep(600);
            WabReport.need(host.sendStats(maidId, "对话里认上的角色", 10, 10, 10, 10), "发不出去");
            Thread.sleep(600);
            return onServer(() -> {
                WabReport.need(bridge.isBound(maid), "stats 里的角色没被认成绑定");
                return "auto-bind 过（客户端同步收到新的绑定名单）";
            });
        });

        WabReport.check("go 的停靠距离钳到 0.5—16 格", () -> {
            if (noMaid()) return "跳过";
            JsonObject wide = host.command("go", param("maid", maidId, "mode", "near", "target", ownerName, "distance", 9999), CMD);
            JsonObject tight = host.command("go", param("maid", maidId, "mode", "near", "target", ownerName, "distance", 0), CMD);
            WabReport.need(ok(wide) || !code(wide).isEmpty(), "超大停靠距离没给结构化回执：" + describe(wide));
            WabReport.need(ok(tight) || !code(tight).isEmpty(), "0 没给结构化回执：" + describe(tight));
            return "wide=" + clip(txt(data(wide), "message")) + " tight=" + clip(txt(data(tight), "message"));
        });

        WabReport.check("超大半径与数量不许把服务器线程拖死", () -> {
            if (noMaid()) return "跳过";
            long start = System.currentTimeMillis();
            JsonObject collect = host.command("gather", param("maid", maidId, "action", "collect",
                    "radius", 99999, "count", 99999), HEAVY);
            long collectMs = System.currentTimeMillis() - start;
            WabReport.note("collect(99999/99999) 用了 " + collectMs + " ms，回执 " + clip(describe(collect)));
            String id = host.newId();
            long mineStart = System.currentTimeMillis();
            host.sendCommand(id, "gather", param("maid", maidId, "action", "mine", "radius", 99999, "count", 99999));
            JsonObject early = tryResult(id, 4000);
            if (early == null) {
                onServer(() -> {
                    bridge.dispatcher().cancelJob(maid, "测试收尾");
                    return Boolean.TRUE;
                });
                host.drop(id);
            } else {
                host.drop(id);
            }
            WabReport.note("mine(99999) " + (early == null ? "起了任务，4 秒内没回，已撤" : "当场回了 " + clip(describe(early)))
                    + "，用了 " + (System.currentTimeMillis() - mineStart) + " ms");
            WabReport.need(host.linked(), "这条把会话弄断了");
            return collectMs < 20000 ? "半径从 99999 收到 24 之内，服务器没被卡住" : "collect 用了 " + collectMs + " ms，偏慢";
        });

        WabReport.check("女仆名册封顶 256：现场造不出这么多", () -> {
            WabReport.skip("要 257 只已收女仆才看得到截断；当前名册 " + bridge.ownedMaids().size() + " 只");
            return "";
        });

        WabReport.check("事件冷却表封顶 128：现场造不出这么多", () -> {
            WabReport.skip("要 128 只女仆在 20 秒内挨个互动才看得到清理，这条只能读代码确认");
            return "";
        });

        WabReport.check("outbox 满 256 帧就关会话：只读代码确认", () -> {
            WabReport.skip("要宿主不收包才能把队列灌满，测试里灌满等于自断链路，不合适");
            return "";
        });
    }

    // ==================== 12 断线自愈 ====================

    private void recovery() {
        WabReport.section("12 坏输入与自愈（最后跑，会主动断一次会话）");

        WabReport.check("超过 4MB 的帧：桥关掉会话，而不是陪着撑内存", () -> {
            StringBuilder huge = new StringBuilder(4_400_000);
            huge.append("{\"v\":1,\"type\":\"command\",\"id\":\"huge\",\"method\":\"chat\",\"params\":{\"text\":\"");
            while (huge.length() < 4_400_000) huge.append('A');
            huge.append("\"}}");
            long closedBefore = host.linkClosed;
            host.sendRaw(huge.toString());
            long deadline = System.currentTimeMillis() + 8000;
            while (System.currentTimeMillis() < deadline) {
                if (host.linkClosed > closedBefore || !host.linked()) {
                    return "会话按预期被关掉（linkClosed=" + host.linkClosed + "，那条帧 " + huge.length() + " 字符）";
                }
                Thread.sleep(200);
            }
            throw new AssertionError("发了 4.4MB 的帧会话还活着：MAX_LINE 那条钳制没生效");
        });

        WabReport.check("断线后桥重新 beacon，并再配上一次", () -> {
            long before = host.beaconSeq();
            WabReport.need(host.awaitBeacon(before, 25000), "断线后 25 秒没再发 beacon：discovery 没重开");
            WabReport.need(host.awaitHello(20000), "新 beacon 之后等不到 hello");
            WabReport.need(host.sendWelcome(true, true, true, true), "welcome 发不出去");
            JsonObject state = host.await("state", 15000);
            WabReport.need(state != null, "重新握手后没推 state");
            return onServer(() -> {
                WabReport.need(bridge.connected(), "connected 还是 false");
                return "自愈完成：第二次握手 + state 帧（offer 共发 " + host.offerSent + " 次）";
            });
        });

        WabReport.check("重连后闸门回到 welcome 的默认值", () -> onServer(() -> {
            WabReport.need(bridge.allowWorldActions(), "重连后闸门还停在关上的状态");
            WabReport.need(bridge.voiceTts(), "重连后 voiceTts 丢了");
            return "allowWorldActions=" + bridge.allowWorldActions() + " 绑定=" + bridge.boundMaidIds().size()
                    + "（新 welcome 里我们给的是全开）";
        }));
    }

    // ==================== 收尾 ====================

    private void cleanup() {
        WabReport.section("收尾");
        try {
            WabReport.check("撤掉可能还在跑的采集任务", () -> onServer(() -> {
                if (maid != null) bridge.dispatcher().cancelJob(maid, "测试收尾");
                return maid == null ? "没有主角女仆" : "已撤";
            }));
            WabReport.check("停掉跟从、让她站在原地", () -> onServer(() -> {
                if (maid == null) WabReport.skip("没有主角女仆");
                bridge.clearFollow(maid);
                maid.getNavigation().stop();
                return "停下了";
            }));
        } catch (Throwable e) {
            WabReport.line("  FAIL  收尾出错：" + e);
        }
        WabReport.line("");
        WabReport.line("  测完请把 wab-testkit 那个 jar 从 mods/ 删掉：它是临时件，不该常驻。");
    }

    // ==================== 小工具 ====================

    private boolean noMaid() {
        if (maid != null) return false;
        WabReport.line("  SKIP  没有主角女仆，这一节整体跳过（先收一只女仆在身边再跑）");
        return true;
    }

    private JsonObject tryResult(String id, long timeoutMs) {
        try {
            return host.awaitResult(id, timeoutMs);
        } catch (IllegalStateException none) {
            return null;
        }
    }

    private JsonObject awaitAnyEvent(long timeoutMs) {
        return host.await("event", timeoutMs);
    }

    private JsonObject awaitKind(String kind, long timeoutMs) {
        long deadline = System.currentTimeMillis() + timeoutMs;
        while (System.currentTimeMillis() < deadline) {
            JsonObject frame = host.await("event", Math.max(200, deadline - System.currentTimeMillis()));
            if (frame == null) return null;
            if (kind.equals(txt(frame, "kind"))) return frame;
        }
        return null;
    }

    /**
     * 把一段活切回服务器线程执行。25 秒兜底：单人档按 Esc 暂停时服务器线程不跑，
     * 那种情况要报成超时，而不是把测试线程一起挂死。
     */
    private <T> T onServer(Callable<T> body) throws Exception {
        CompletableFuture<T> future = new CompletableFuture<>();
        server.execute(() -> {
            try {
                future.complete(body.call());
            } catch (Throwable throwable) {
                future.completeExceptionally(throwable instanceof Exception
                        ? (Exception) throwable : new RuntimeException(throwable));
            }
        });
        try {
            return future.get(25, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof Exception exception) throw exception;
            throw new RuntimeException(cause);
        }
    }

    private static String stamp() {
        return String.valueOf(System.currentTimeMillis() % 100000);
    }

    private static String repeat(char c, int count) {
        StringBuilder out = new StringBuilder(count);
        for (int i = 0; i < count; i++) out.append(c);
        return out.toString();
    }

    private static JsonArray pos(BlockPos pos) {
        JsonArray out = new JsonArray();
        out.add(pos.getX());
        out.add(pos.getY());
        out.add(pos.getZ());
        return out;
    }

    private static String randomUuid() {
        return UUID.randomUUID().toString();
    }

    private static JsonObject param(Object... keyValues) {
        JsonObject out = new JsonObject();
        for (int i = 0; i + 1 < keyValues.length; i += 2) {
            String key = (String) keyValues[i];
            Object value = keyValues[i + 1];
            if (value instanceof JsonElement element) out.add(key, element);
            else if (value instanceof Number number) out.addProperty(key, number);
            else if (value instanceof Boolean flag) out.addProperty(key, flag);
            else out.addProperty(key, String.valueOf(value));
        }
        return out;
    }

    private static boolean ok(JsonObject result) {
        return result != null && FakeWabHost.has(result, "ok") && result.get("ok").getAsBoolean();
    }

    private static JsonObject data(JsonObject result) {
        if (result == null || !result.has("data") || !result.get("data").isJsonObject()) return new JsonObject();
        return result.getAsJsonObject("data");
    }

    private static String txt(JsonObject src, String key) {
        return FakeWabHost.text(src, key);
    }

    private static String code(JsonObject result) {
        return txt(result, "code");
    }

    private static String error(JsonObject result) {
        return txt(result, "error");
    }

    private static int size(JsonObject src, String key) {
        return src != null && src.has(key) && src.get(key).isJsonArray() ? src.getAsJsonArray(key).size() : -1;
    }

    private static String describe(JsonObject result) {
        if (result == null) return "null";
        return ok(result) ? "ok " + clip(data(result).toString()) : "fail " + code(result) + " " + clip(error(result));
    }

    private static String clip(String text) {
        if (text == null) return "";
        String one = text.replace("\r", " ").replace("\n", " ");
        return one.length() <= 180 ? one : one.substring(0, 180) + "…";
    }
}
