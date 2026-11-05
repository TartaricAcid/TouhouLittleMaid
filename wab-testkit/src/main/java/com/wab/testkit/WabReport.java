package com.wab.testkit;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 结果表。每条入口一行 PASS / FAIL / SKIP，同时打到聊天框、模组日志和游戏目录下的文本文件。
 * 单独一个类是为了让用例本体只写「做什么、期望什么」，不用到处穿插打印。
 */
public final class WabReport {
    private static final Logger LOGGER = LoggerFactory.getLogger("wab-testkit");

    /** 用例体：返回一句补充说明（可以是空），抛异常就算 FAIL，抛 Skipped 算 SKIP。 */
    public interface Body {
        String run() throws Throwable;
    }

    public static final class Skipped extends RuntimeException {
        public Skipped(String why) {
            super(why);
        }
    }

    private static final List<String> LINES = new ArrayList<>();
    private static final Object LOCK = new Object();

    private static int pass;
    private static int fail;
    private static int skip;
    private static MinecraftServer server;
    private static UUID speaker;

    private WabReport() {
    }

    public static void begin(String title, MinecraftServer mcServer, ServerPlayer player) {
        synchronized (LOCK) {
            LINES.clear();
        }
        pass = 0;
        fail = 0;
        skip = 0;
        server = mcServer;
        speaker = player == null ? null : player.getUUID();
        line("======== " + title + " ========");
    }

    public static void section(String name) {
        line("");
        line("【" + name + "】");
    }

    public static void check(String name, Body body) {
        String detail;
        String mark;
        try {
            detail = body.run();
            mark = "PASS";
            pass++;
        } catch (Skipped why) {
            detail = why.getMessage();
            mark = "SKIP";
            skip++;
        } catch (Throwable error) {
            detail = error.getMessage() == null ? error.toString() : error.getMessage();
            mark = "FAIL";
            fail++;
        }
        line("  " + mark + "  " + name + (detail == null || detail.isEmpty() ? "" : " — " + clip(detail)));
    }

    /** 只记现象、不计成败的观察项（协议里没写死、需要人看一眼的行为）。 */
    public static void note(String text) {
        line("  NOTE  " + clip(text));
    }

    public static void line(String text) {
        synchronized (LOCK) {
            LINES.add(text);
        }
        LOGGER.info(text);
        MinecraftServer mcServer = server;
        UUID who = speaker;
        if (mcServer == null || who == null) return;
        mcServer.execute(() -> {
            ServerPlayer player = mcServer.getPlayerList().getPlayer(who);
            if (player != null) player.displayClientMessage(Component.literal(text), false);
        });
    }

    public static void need(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    public static void skip(String why) {
        throw new Skipped(why);
    }

    /** 收尾：打一行汇总，并把全文写到游戏目录（跑服务端时就是服务端根目录）。 */
    public static String finish() {
        String summary = "合计 " + (pass + fail + skip) + " 条：PASS " + pass + " / FAIL " + fail + " / SKIP " + skip;
        line("");
        line(summary);
        String written = "";
        try {
            List<String> copy;
            synchronized (LOCK) {
                copy = new ArrayList<>(LINES);
            }
            Path file = Path.of("wab-testkit-report.txt");
            Files.write(file, String.join(System.lineSeparator(), copy).getBytes(StandardCharsets.UTF_8));
            written = file.toAbsolutePath().toString();
        } catch (Exception e) {
            written = "文本报告没写出来：" + e;
        }
        line("报告文件：" + written);
        lastFile = written;
        return summary;
    }

    /** 上一次跑完写出的报告文件绝对路径；没跑过就是空。 */
    public static String lastFile() {
        return lastFile;
    }

    private static volatile String lastFile = "";

    private static String clip(String text) {
        String one = text.replace("\r", " ").replace("\n", " ");
        return one.length() <= 260 ? one : one.substring(0, 260) + "…";
    }
}
