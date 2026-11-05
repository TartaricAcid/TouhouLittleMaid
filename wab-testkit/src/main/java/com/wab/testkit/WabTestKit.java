package com.wab.testkit;

import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabBridge;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabConfig;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

/**
 * 临时测试模组的入口：一个假 WAB 宿主 + /wtest 命令族。
 * <p>
 * /wtest all [port] 跑完整用例表；/wtest start [port] 只配对不驱动；
 * /wtest status 看当前计数；/wtest stop 收摊；/wtest report 把上次报告的 FAIL/SKIP 再打一遍。
 * <p>
 * 全轮用例都跑在自己的线程上：里面几十处「发一帧等回执」的等待，放到服务器线程上会把游戏卡死甚至互锁。
 */
@Mod("wab_testkit")
public final class WabTestKit {
    private static final Logger LOGGER = LoggerFactory.getLogger("wab-testkit");
    private static final FakeWabHost HOST = new FakeWabHost();

    private static volatile Thread suite;

    public WabTestKit() {
        LOGGER.info("[wab-testkit] 临时测试模组已加载。进世界后 /wtest all 开跑，测完请把本模组从 mods/ 删掉。");
    }

    @Mod.EventBusSubscriber(modid = "wab_testkit", bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static final class Registrar {
        @SubscribeEvent
        public static void onRegisterCommands(RegisterCommandsEvent event) {
            register(event.getDispatcher());
        }
    }

    private static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("wtest")
                // 专用服要 OP；单人存档不卡权限，免得没开作弊就进不去
                .requires(src -> src.hasPermission(2) || !src.getServer().isDedicatedServer())
                .then(Commands.literal("all")
                        .then(Commands.argument("port", IntegerArgumentType.integer(1, 65535))
                                .executes(context -> runAll(context, IntegerArgumentType.getInteger(context, "port"))))
                        .executes(context -> runAll(context, 0)))
                .then(Commands.literal("start")
                        .then(Commands.argument("port", IntegerArgumentType.integer(1, 65535))
                                .executes(context -> startOnly(context, IntegerArgumentType.getInteger(context, "port"))))
                        .executes(context -> startOnly(context, 0)))
                .then(Commands.literal("status").executes(WabTestKit::status))
                .then(Commands.literal("stop").executes(WabTestKit::stop))
                .then(Commands.literal("report").executes(WabTestKit::report)));
    }

    // ==================== all ====================

    private static int runAll(CommandContext<CommandSourceStack> context, int port) {
        ServerPlayer player = player(context);
        if (player == null) {
            context.getSource().sendFailure(Component.literal("/wtest all 得由玩家执行（要拿主人身份和名册）"));
            return 0;
        }
        if (suite != null) {
            say(context.getSource(), player.getUUID(), "上一轮还在跑：先 /wtest stop 再重来", ChatFormatting.RED);
            return 0;
        }
        if (WabBridge.get() == null) {
            say(context.getSource(), player.getUUID(), "桥还没建：这模组是服务器启动时才建桥的，请进了世界（主人在线）之后再跑", ChatFormatting.RED);
            return 0;
        }
        MinecraftServer server = context.getSource().getServer();
        int udp = port <= 0 ? WabConfig.discoveryPort() : port;
        Thread worker = new Thread(() -> {
            try {
                WabSuite.run(HOST, server, player, udp);
            } catch (Throwable unexpected) {
                LOGGER.warn("[wab-testkit] 用例线程断了", unexpected);
            } finally {
                suite = null;
                say(server, player.getUUID(), "本轮跑完，报告见 " + WabReport.lastFile(), ChatFormatting.GREEN);
            }
        }, "WAB-TestSuite");
        worker.setDaemon(true);
        suite = worker;
        worker.start();
        say(context.getSource(), player.getUUID(), "开跑：假宿主监听 UDP :" + udp
                + "，" + WabConfig.discoveryPort() + " 是桥现在的发现端口。结果一行行发到聊天框，日志关键字 wab-testkit。",
                ChatFormatting.YELLOW);
        return 1;
    }

    // ==================== start ====================

    private static int startOnly(CommandContext<CommandSourceStack> context, int port) {
        ServerPlayer player = player(context);
        if (player == null) {
            context.getSource().sendFailure(Component.literal("/wtest start 得由玩家执行"));
            return 0;
        }
        if (suite != null) {
            say(context.getSource(), player.getUUID(), "完整用例正在跑，别同时开只配对模式", ChatFormatting.RED);
            return 0;
        }
        int udp = port <= 0 ? WabConfig.discoveryPort() : port;
        MinecraftServer server = context.getSource().getServer();
        Thread worker = new Thread(() -> {
            String first;
            try {
                HOST.start(udp);
                first = HOST.lastError();
            } catch (Exception e) {
                first = "监听 UDP :" + udp + " 失败：" + e;
                say(server, player.getUUID(), first, ChatFormatting.RED);
                return;
            }
            boolean gotBeacon = HOST.awaitBeacon(0, 25000);
            boolean gotHello = gotBeacon && HOST.awaitHello(10000);
            StringBuilder text = new StringBuilder();
            text.append(gotBeacon ? "收到 beacon 第 " + HOST.beaconSeq() + " 次" : "25 秒内没有 beacon");
            text.append(gotHello ? "，hello 也上来了（TCP :" + HOST.tcpPort() + "）" : "，配对没成");
            if (!text.toString().contains("hello 也上来了")) text.append("；最近错误：").append(safe(HOST.lastError(), first));
            text.append("；此刻桥连着吗：").append(WabBridge.get() != null && WabBridge.get().connected());
            say(server, player.getUUID(), text.toString(), gotHello ? ChatFormatting.GREEN : ChatFormatting.RED);
        }, "WAB-TestKit-Pair");
        worker.setDaemon(true);
        worker.start();
        say(context.getSource(), player.getUUID(), "只配对：监听 UDP :" + udp + "，收到 beacon 就回 offer，之后不再驱动任何命令。/wtest stop 收摊。",
                ChatFormatting.YELLOW);
        return 1;
    }

    // ==================== status ====================

    private static int status(CommandContext<CommandSourceStack> context) {
        CommandSourceStack src = context.getSource();
        src.sendSuccess(() -> Component.literal("假宿主：" + (HOST.listening() ? "在听" : "没在听")
                + (HOST.listening() ? "" : "（/wtest start 或 /wtest all 才会起）")).withStyle(ChatFormatting.AQUA), false);
        src.sendSuccess(() -> Component.literal("  链路：" + (HOST.linked() ? "已连 TCP :" + HOST.tcpPort() : "未连")
                + "  beacon " + HOST.beaconSeq() + " 次 / offer " + HOST.offerSent + " 次 / hello " + HOST.helloCount() + " 次"
                + "  断链 " + HOST.linkClosed + " 次"), false);
        src.sendSuccess(() -> Component.literal("  帧：进 " + HOST.framesIn + " / 出 " + HOST.framesOut
                + "  pong " + HOST.pongSeen + "  坏行 " + HOST.malformedSeen + "  无主回执 " + HOST.unmatchedResult), false);
        if (!safe(HOST.lastError(), "").isEmpty()) {
            src.sendSuccess(() -> Component.literal("  最近错误：" + HOST.lastError()).withStyle(ChatFormatting.YELLOW), false);
        }
        WabBridge bridge = WabBridge.get();
        if (bridge == null) {
            src.sendSuccess(() -> Component.literal("桥：还没建（服务器启动时才会建）").withStyle(ChatFormatting.RED), false);
            return 1;
        }
        src.sendSuccess(() -> Component.literal("桥：instance=" + bridge.instanceId()
                + " " + (bridge.connected() ? "已连" : "空闲")
                + " 主人=" + safe(bridge.ownerName(), "(无)")
                + " 世界=" + safe(bridge.worldName(), "(无)")
                + " 名下女仆 " + bridge.ownedMaids().size() + " / 绑角色 " + bridge.boundMaidIds().size()), false);
        src.sendSuccess(() -> Component.literal("闸门：世界操作=" + bridge.allowWorldActions()
                + " 手势=" + bridge.allowGestures()
                + " 合成语音=" + bridge.voiceTts() + " 听写=" + bridge.voiceStt()
                + " 自主日程=" + (bridge.autonomy() != null)), false);
        src.sendSuccess(() -> Component.literal("（status 只读计数，不发任何命令；要过用例跑 /wtest all）")
                .withStyle(ChatFormatting.GRAY), false);
        return 1;
    }

    // ==================== stop ====================

    private static int stop(CommandContext<CommandSourceStack> context) {
        Thread worker = suite;
        if (worker != null) worker.interrupt();
        suite = null;
        HOST.stop();
        context.getSource().sendSuccess(() -> Component.literal("已停：假宿主关掉监听和链路。用例线程是守护线程，"
                + "它剩下的等待会自己超时收尾；实在卡住就重开游戏。").withStyle(ChatFormatting.YELLOW), false);
        return 1;
    }

    // ==================== report ====================

    private static int report(CommandContext<CommandSourceStack> context) {
        String file = WabReport.lastFile();
        if (file.isEmpty()) {
            context.getSource().sendFailure(Component.literal("这一局还没跑过 /wtest all，报告是空的"));
            return 0;
        }
        Path path = Path.of(file);
        if (!Files.isRegularFile(path)) {
            context.getSource().sendFailure(Component.literal("报告文件不在了：" + file));
            return 0;
        }
        List<String> lines;
        try {
            lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("读报告失败：" + e));
            return 0;
        }
        int shown = 0;
        context.getSource().sendSuccess(() -> Component.literal("报告 " + file
                + " 共 " + lines.size() + " 行，下面只挑 FAIL / SKIP / 合计").withStyle(ChatFormatting.AQUA), false);
        for (String line : lines) {
            if (line.contains("FAIL") || line.contains("SKIP") || line.startsWith("合计") || line.startsWith("【")) {
                ChatFormatting color = line.contains("FAIL") ? ChatFormatting.RED
                        : line.contains("SKIP") ? ChatFormatting.YELLOW
                        : ChatFormatting.GRAY;
                String copy = line;
                context.getSource().sendSuccess(() -> Component.literal(copy).withStyle(color), false);
                if (++shown >= 60) {
                    context.getSource().sendSuccess(() -> Component.literal("……后面直接开文件看").withStyle(ChatFormatting.GRAY), false);
                    break;
                }
            }
        }
        if (shown == 0) {
            context.getSource().sendSuccess(() -> Component.literal("一条 FAIL/SKIP 都没有：全绿").withStyle(ChatFormatting.GREEN), false);
        }
        return 1;
    }

    // ==================== 小工具 ====================

    private static ServerPlayer player(CommandContext<CommandSourceStack> context) {
        return context.getSource().getEntity() instanceof ServerPlayer player ? player : null;
    }

    /** 发一条给玩家：命令线程上当场回，后台线程上排队到服务器线程再回（人若已下线就丢掉）。 */
    private static void say(CommandSourceStack src, UUID who, String text, ChatFormatting color) {
        Component message = Component.literal(text).withStyle(color);
        if (src.isPlayer()) src.sendSuccess(() -> message, false);
        MinecraftServer server = src.getServer();
        server.execute(() -> {
            ServerPlayer target = server.getPlayerList().getPlayer(who);
            if (target != null) target.displayClientMessage(message, false);
        });
    }

    private static void say(MinecraftServer server, UUID who, String text, ChatFormatting color) {
        Component message = Component.literal(text).withStyle(color);
        server.execute(() -> {
            ServerPlayer target = server.getPlayerList().getPlayer(who);
            if (target != null) target.displayClientMessage(message, false);
        });
    }

    private static String safe(String value, String fallback) {
        return value == null || value.isEmpty() ? fallback : value;
    }
}
