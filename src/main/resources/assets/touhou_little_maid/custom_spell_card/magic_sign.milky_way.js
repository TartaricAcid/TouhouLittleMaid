var Task = Java.type("com.github.tartaricacid.touhoulittlemaid.util.DelayedTask");
var Color = Java.type("com.github.tartaricacid.touhoulittlemaid.danmaku.DanmakuColor");
var Type = Java.type("com.github.tartaricacid.touhoulittlemaid.danmaku.DanmakuType");
var Danmaku = Java.type("com.github.tartaricacid.touhoulittlemaid.danmaku.script.EntityDanmakuWrapper");
var Vec3d = Java.type("com.github.tartaricacid.touhoulittlemaid.danmaku.script.Vec3dWrapper");

Java.asJSONCompatible({
    // 符卡的 id，字符串，必需参数
    // 推荐格式：资源域:X符.符卡名
    id: "touhou_little_maid:magic_sign.milky_way",
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
        // 中心散发的大星弹
        for (var i = 0; i < 50; i++) {
            Task.add(function (times) {
                for (var j = 0; j < 9; j++) {
                    var danmaku = new Danmaku(world, entity, 2.0, 0.0, Type.BIG_STAR, Color.RED);
                    if (times % 2 == 1) {
                        danmaku.setColor(Color.BLUE);
                    }
                    danmaku.shoot(entity, 0, entity.getYaw() - 5 * times + 40 * j, 0, 0.7, 0);
                    world.spawnDanmaku(danmaku);
                }
            }, 5 * i, i);
        }

        // 一段时间后的斜向弹幕
        for (i = 0; i < 20; i++) {
            Task.add(function (times) {
                for (var j = 0; j < 5; j++) {
                    var pos = Vec3d.getRotationVector(-15, 0, Math.random() * 30 - 10, 0, -0.1, entity);
                    var danmaku = new Danmaku(world, entity, 2.0, 0.0, Type.STAR, Color.YELLOW);
                    danmaku.setPosition(pos);
                    danmaku.shoot(entity, 0, entity.getYaw() - 60, 0, 0.3, 0);
                    world.spawnDanmaku(danmaku);
                }
            }, 10 * i + 50, i);

            Task.add(function (times) {
                for (var j = 0; j < 5; j++) {
                    var pos = Vec3d.getRotationVector(15, 0, Math.random() * 30 - 10, 0, -0.1, entity);
                    var danmaku = new Danmaku(world, entity, 2.0, 0.0, Type.STAR, Color.GREEN);
                    danmaku.setPosition(pos);
                    danmaku.shoot(entity, 0, entity.getYaw() + 60, 0, 0.3, 0);
                    world.spawnDanmaku(danmaku);
                }
            }, 10 * i + 50, i);
        }
    }
});