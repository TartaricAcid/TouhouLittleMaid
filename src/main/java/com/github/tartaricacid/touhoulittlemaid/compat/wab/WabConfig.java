package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * 模组侧配置。端口与允许的动作由 WAB 插件决定；这里只放“这台游戏愿不愿意参与”的开关。
 */
public final class WabConfig {
    public static final ForgeConfigSpec SPEC;

    // 这些值对象要给 Cloth 设置页直接读写，所以是 public；
    // 下面那批 enabled()/chatRange() 之类仍保留，它们带兜底，供可能在配置加载前取值的场合用。
    public static final ForgeConfigSpec.BooleanValue ENABLED;
    public static final ForgeConfigSpec.BooleanValue SHOW_HUD;
    public static final ForgeConfigSpec.BooleanValue DEBUG;
    public static final ForgeConfigSpec.IntValue CHAT_RANGE;
    public static final ForgeConfigSpec.IntValue DISCOVERY_PORT;
    public static final ForgeConfigSpec.BooleanValue LAN_DISCOVERY;
    public static final ForgeConfigSpec.IntValue BEACON_INTERVAL_SECONDS;
    public static final ForgeConfigSpec.BooleanValue AUTONOMY_ENABLED;
    public static final ForgeConfigSpec.BooleanValue PLAYER_IN_MAID_BED;
    public static final ForgeConfigSpec.IntValue MAID_CHAT_COOLDOWN;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("WarmAsBefore maid bridge. WAB controls a real maid body through this channel.");
        builder.push("bridge");
        ENABLED = builder.comment("Broadcast to WAB and accept its commands.")
                .define("enabled", true);
        DISCOVERY_PORT = builder.comment("UDP discovery port. Keep 27321 unless WAB is configured otherwise.")
                .defineInRange("discoveryPort", 27321, 1, 65535);
        LAN_DISCOVERY = builder.comment("Send the discovery beacon to LAN interfaces too. Leave false unless WAB runs on another machine; WAB's own lan setting must be on for the other side.")
                .define("lanDiscovery", false);
        BEACON_INTERVAL_SECONDS = builder.comment("Beacon interval while disconnected.")
                .defineInRange("beaconIntervalSeconds", 3, 1, 60);
        CHAT_RANGE = builder.comment("A plain chat line is sent to the nearest maid within this range.")
                .defineInRange("chatRange", 8, 1, 64);
        SHOW_HUD = builder.comment("Show WAB affection/trust on the action bar.")
                .define("showHud", true);
        AUTONOMY_ENABLED = builder.comment("Let scheduled and random autonomy run on the mod side.")
                .define("autonomyEnabled", true);
        PLAYER_IN_MAID_BED = builder.comment("Let the player sleep in a maid bed. Off by default: this changes what right-clicking a maid bed does, so it is opt-in only.")
                .define("playerCanSleepInMaidBed", false);
        MAID_CHAT_COOLDOWN = builder.comment("Minimum seconds between two maids reporting each other to WAB.")
                .defineInRange("maidChatCooldownSeconds", 90, 10, 3600);
        DEBUG = builder.comment("Log every bridge frame.")
                .define("debug", false);
        builder.pop();
        SPEC = builder.build();
    }

    private WabConfig() {
    }

    /**
     * 这份配置是 SERVER 型的，要存档跑起来才加载得动；在主菜单点「配置」时它还没进内存，
     * 读会拿到默认值、写会直接抛。设置页先用这一句问明白，答 no 就只给一行提示。
     */
    public static boolean loaded() {
        try {
            boolean current = bool(ENABLED, true);
            ENABLED.set(current);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean bool(ForgeConfigSpec.BooleanValue value, boolean fallback) {
        try {
            return value.get();
        } catch (Exception e) {
            return fallback;
        }
    }

    private static int integer(ForgeConfigSpec.IntValue value, int fallback) {
        try {
            return value.get();
        } catch (Exception e) {
            return fallback;
        }
    }

    public static boolean enabled() {
        return bool(ENABLED, true);
    }

    public static boolean showHud() {
        return bool(SHOW_HUD, true);
    }

    public static boolean debug() {
        return bool(DEBUG, false);
    }

    public static boolean autonomyEnabled() {
        return bool(AUTONOMY_ENABLED, true);
    }

    public static int chatRange() {
        return integer(CHAT_RANGE, 8);
    }

    public static int discoveryPort() {
        return integer(DISCOVERY_PORT, 27321);
    }

    /** 默认关：beacon 只发回环。WAB 的 lan 关着时它本来就收不到局域网包，广播出去纯属泄漏。 */
    public static boolean lanDiscovery() {
        return bool(LAN_DISCOVERY, false);
    }

    public static int beaconIntervalSeconds() {
        return integer(BEACON_INTERVAL_SECONDS, 3);
    }

    /**
     * 玩家能不能睡女仆的床。默认关：开了之后右键女仆床的行为就变了，
     * 而「换上我们的 jar、不连 WAB 也和原版一样」这条不变量得靠这个开关守住。
     */
    public static boolean playerCanSleepInMaidBed() {
        return bool(PLAYER_IN_MAID_BED, false);
    }

    public static int maidChatCooldownSeconds() {
        return integer(MAID_CHAT_COOLDOWN, 90);
    }
}
