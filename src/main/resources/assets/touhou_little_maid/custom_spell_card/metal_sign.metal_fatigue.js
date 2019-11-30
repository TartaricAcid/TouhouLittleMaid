var Task = Java.type("com.github.tartaricacid.touhoulittlemaid.util.DelayedTask");
var Color = Java.type("com.github.tartaricacid.touhoulittlemaid.danmaku.DanmakuColor");
var Type = Java.type("com.github.tartaricacid.touhoulittlemaid.danmaku.DanmakuType");
var Danmaku = Java.type("com.github.tartaricacid.touhoulittlemaid.danmaku.script.EntityDanmakuWrapper");
var Vec3d = Java.type("com.github.tartaricacid.touhoulittlemaid.danmaku.script.Vec3dWrapper");

Java.asJSONCompatible({
    // 符卡的 id，字符串，必需参数
    // 推荐格式：资源域:X符.符卡名
    id: "touhou_little_maid:metal_sign.metal_fatigue",
    // 作者，字符串
    author: "tartaric_acid",
    // 版本，字符串
    version: "1.0.0",
    // 冷却时间，整型数
    cooldown: 170,
    /**
     * 执行的符卡逻辑，函数签名固定，会直接调用
     * @param world 当前所处的世界
     * @param entity 释放符卡的实体
     */
    spellCard: function (world, entity) {
        // 第一波角度和后面不一致
        Task.add(function () {
            for (var j = 0; j < 8; j++) {
                var danmaku = new Danmaku(world, entity, 2.0, 0.0, Type.BIG_BALL, Color.YELLOW);
                danmaku.shoot(entity, 0, entity.getYaw() + 15 + 45 * j, 0, 0.2, 0);
                danmaku.setTicksExisted(31);
                world.spawnDanmaku(danmaku);
            }
        }, 0);

        Task.add(function () {
            for (var i = 0; i < 8; i++) {
                for (var j = 0; j < 8; j++) {
                    var danmaku = new Danmaku(world, entity, 2.0, 0.0, Type.BIG_BALL, Color.YELLOW);
                    var pos = Vec3d.getRotationVector(0, 0, 5.6, 15 + 45 * i, -0.1, entity);
                    danmaku.setPosition(pos);
                    danmaku.shoot(entity, 0, entity.getYaw() + 45 * j, 0, 0.2, 0);
                    world.spawnDanmaku(danmaku);
                }
            }
        }, 30);


        // 后面来上 5 波即可，此时角度是固定的
        for (var k = 0; k < 5; k++) {
            Task.add(function () {
                for (var j = 0; j < 8; j++) {
                    var danmaku = new Danmaku(world, entity, 2.0, 0.0, Type.BIG_BALL, Color.YELLOW);
                    danmaku.shoot(entity, 0, entity.getYaw() + 30 + 45 * j, 0, 0.2, 0);
                    danmaku.setTicksExisted(31);
                    world.spawnDanmaku(danmaku);
                }
            }, 20 * (k + 1));

            Task.add(function () {
                for (var i = 0; i < 8; i++) {
                    for (var j = 0; j < 8; j++) {
                        var danmaku = new Danmaku(world, entity, 2.0, 0.0, Type.BIG_BALL, Color.YELLOW);
                        var pos = Vec3d.getRotationVector(0, 0, 5.6, 30 + 45 * i, -0.1, entity);
                        danmaku.setPosition(pos);
                        danmaku.shoot(entity, 0, entity.getYaw() + 30 + 45 * j, 0, 0.2, 0);
                        world.spawnDanmaku(danmaku);
                    }
                }
            }, 30 + 20 * (k + 1));
        }
    }
});