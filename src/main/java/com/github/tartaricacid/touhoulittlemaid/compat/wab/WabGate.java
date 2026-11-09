package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.google.gson.JsonObject;

import java.util.Locale;

/**
 * WAB 的「允许世界操作」开关到底拦什么。
 * 会话命令和自主日程是两条独立入口，判断必须共用这一份，
 * 否则关掉开关后日程里的挖矿步骤照样能拆家。
 */
final class WabGate {
    private WabGate() {
    }

    /** 会话发来的这条命令会不会改世界。go 只挪身体；act 里 attack/use 和 interact_addon 会（后者要调附属模组的行为，可能打别的女仆、去翻主人背包）。 */
    static boolean commandNeedsWorldActions(String method, JsonObject params) {
        switch (method.toLowerCase(Locale.ROOT)) {
            case "gather":
            case "attack":
            case "hold":
            case "container":
            case "craft":
            case "build":
            case "ride":
            case "crew":
                return true;
            case "act":
                return switchesWorld(WabJson.str(params, "action").toLowerCase(Locale.ROOT));
            default:
                return false;
        }
    }

    /** 日程里的这一步会不会改世界。 */
    static boolean stepNeedsWorldActions(String doWhat) {
        return switchesWorld(doWhat.toLowerCase(Locale.ROOT));
    }

    private static boolean switchesWorld(String action) {
        return switch (action) {
            case "mine", "chop", "harvest", "scoop", "collect", "fill",
                    "place", "drop", "give", "equip", "feed_player", "take",
                    "use", "attack", "interact_addon" -> true;
            default -> false;
        };
    }
}
