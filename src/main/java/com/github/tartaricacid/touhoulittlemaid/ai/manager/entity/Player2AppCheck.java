package com.github.tartaricacid.touhoulittlemaid.ai.manager.entity;

import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * 因为默认推荐的 AI 对话工具是 Player2，所以需要对其做额外判断，提示玩家安装使用
 */
public class Player2AppCheck {
    private static final Duration MAX_TIMEOUT = Duration.ofSeconds(15);
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(MAX_TIMEOUT)
            .version(HttpClient.Version.HTTP_1_1)
            .build();
    private static final HttpRequest REQUEST = HttpRequest.newBuilder()
            .uri(URI.create("http://127.0.0.1:4315/v1/health"))
            .header(HttpHeaders.ACCEPT, MediaType.JSON_UTF_8.toString())
            .header("player2-game-key", "TouhouLittleMaid")
            .timeout(MAX_TIMEOUT)
            .GET().build();

    public static void checkPlayer2App(ServerPlayer player, Runnable runnable) {
        HTTP_CLIENT.sendAsync(REQUEST, HttpResponse.BodyHandlers.ofString())
                .whenComplete((response, exception) -> {
                    if (exception != null) {
                        sendTip(player);
                        return;
                    }
                    if (response.statusCode() != 200) {
                        player.sendSystemMessage(Component.translatable("ai.touhou_little_maid.chat.player2_error", response.statusCode()));
                        return;
                    }
                    if (player.getServer() != null) {
                        player.getServer().submit(runnable);
                    }
                });
    }

    /**
     * STT 语音识别是在客户端进行的，所以需要在客户端检查 Player2 是否安装
     */
    @OnlyIn(Dist.CLIENT)
    public static void checkPlayer2AppInStt(Player player, Runnable runnable) {
        HTTP_CLIENT.sendAsync(REQUEST, HttpResponse.BodyHandlers.ofString())
                .whenComplete((response, exception) -> {
                    if (exception != null) {
                        sendTip(player);
                        return;
                    }
                    if (response.statusCode() != 200) {
                        player.sendSystemMessage(Component.translatable("ai.touhou_little_maid.chat.player2_error", response.statusCode()));
                        return;
                    }
                    Minecraft.getInstance().submit(runnable);
                });
    }

    private static void sendTip(Player player) {
        MutableComponent tip = Component.translatable("ai.touhou_little_maid.chat.need_player2_install")
                .withStyle(ChatFormatting.RED);
        MutableComponent url = Component.literal("https://player2.game/");
        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, "https://player2.game/");
        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.link.open"));
        url.withStyle(style -> style.withHoverEvent(hoverEvent).withClickEvent(clickEvent)
                .withUnderlined(true).withColor(ChatFormatting.BLUE));
        player.sendSystemMessage(tip);
        player.sendSystemMessage(Component.translatable("ai.touhou_little_maid.chat.download_url").append(url));
    }
}
