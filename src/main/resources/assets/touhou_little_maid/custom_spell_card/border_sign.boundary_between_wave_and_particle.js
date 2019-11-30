var Task = Java.type("com.github.tartaricacid.touhoulittlemaid.util.DelayedTask");
var Color = Java.type("com.github.tartaricacid.touhoulittlemaid.danmaku.DanmakuColor");
var Type = Java.type("com.github.tartaricacid.touhoulittlemaid.danmaku.DanmakuType");
var Danmaku = Java.type("com.github.tartaricacid.touhoulittlemaid.danmaku.script.EntityDanmakuWrapper");
var Vec3d = Java.type("com.github.tartaricacid.touhoulittlemaid.danmaku.script.Vec3dWrapper");

Java.asJSONCompatible({
    // 符卡的 id，字符串，必需参数
    // 推荐格式：资源域:X符.符卡名
    id: "touhou_little_maid:border_sign.boundary_between_wave_and_particle",
    // 作者，字符串
    author: "tartaric_acid",
    // 版本，字符串
    version: "1.0.0",
    // 冷却时间，整型数
    cooldown: 250,
    /**
     * 执行的符卡逻辑，函数签名固定，会直接调用
     * @param world 当前所处的世界
     * @param entity 释放符卡的实体
     */
    spellCard: function (world, entity) {
        for (var i = 0; i < 120; i++) {
            Task.add(function (times) {
                for (var j = 0; j < 9; j++) {
                    var danmaku = new Danmaku(world, entity, 2.0, 0.0, Type.PETAL, Color.MAGENTA);
                    danmaku.shoot(entity, 0, entity.getYaw() - 40 * j + 5 * Math.pow(times / 4, 2), 0, 0.4, 0);
                    world.spawnDanmaku(danmaku);
                }
            }, 2 * i, i);
        }
    }
});
