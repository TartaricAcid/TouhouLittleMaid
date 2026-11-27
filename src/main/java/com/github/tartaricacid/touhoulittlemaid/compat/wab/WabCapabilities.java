package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * 能力握手：随 hello 帧上报「这一版桥真的认哪些方法、哪些玩法特性」。
 *
 * <p>存在的理由是版本错配：宿主会在提示词里摆出 maid_* 工具，而工具能不能用取决于装的是哪一版模组。
 * 旧模组不认 hug，模型却照样能看见「抱抱」并调用，拿到的一定是 {@code unknown_method}。
 * 所以把能力表交给宿主，让它只显示这台游戏真能执行的那部分。
 *
 * <p>兼容两条线：
 * <ul>
 *   <li><b>旧宿主</b>不认识 {@code capabilities} 字段，多出来的一块 JSON 直接被忽略，行为与之前完全一样；</li>
 *   <li><b>旧模组</b>整块都不发，新宿主按「不支持」处理——宁可少显示一个工具，也不给一个必然报错的工具。</li>
 * </ul>
 * 这份表只描述能力，不描述权限：开关（世界动作、抱抱总开关等）仍由各自的配置项在运行时决定。
 */
public final class WabCapabilities {
    /**
     * 能力表自己的版本号，与线上帧协议 {@code WabBridge.PROTOCOL} 分开走：
     * 加一个方法/特性不动它（宿主按名字问，不认识的名字自动忽略），改这一块的结构或语义才 +1。
     */
    public static final int PROTOCOL = 1;

    /**
     * 跨方法的玩法特性。宿主可以只问「支不支持抱抱」，不必知道抱抱落在 hug 方法还是 act 的枚举里。
     * 只列对宿主可见性有影响的：别把整张配置表搬进来。
     */
    private static final List<String> FEATURES = List.of("hug", "headpat", "plan");

    private WabCapabilities() {
    }

    /** hello 帧里的 capabilities 对象：{protocol, methods, features}。方法表以 WabDispatcher 的路由表为准。 */
    public static JsonObject json() {
        JsonObject out = new JsonObject();
        out.addProperty("protocol", PROTOCOL);
        JsonArray methods = new JsonArray();
        for (String method : WabDispatcher.methods()) methods.add(method);
        out.add("methods", methods);
        JsonArray features = new JsonArray();
        for (String feature : FEATURES) features.add(feature);
        out.add("features", features);
        return out;
    }

    public static List<String> features() {
        return FEATURES;
    }
}
