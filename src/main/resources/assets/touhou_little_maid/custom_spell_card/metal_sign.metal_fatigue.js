var DelayedTask = Java.type("com.github.tartaricacid.touhoulittlemaid.util.DelayedTask");
var DanmakuColor = Java.type("com.github.tartaricacid.touhoulittlemaid.danmaku.DanmakuColor");
var DanmakuType = Java.type("com.github.tartaricacid.touhoulittlemaid.danmaku.DanmakuType");
var EntityDanmaku = Java.type("com.github.tartaricacid.touhoulittlemaid.entity.projectile.EntityDanmaku");
var RotationTool = Java.type("com.github.tartaricacid.touhoulittlemaid.util.RotationTool");

// 符卡的 id，字符串，必需参数
// 推荐格式：资源域:X符.符卡名
var id = "touhou_little_maid:metal_sign.metal_fatigue";
// 作者，字符串
var author = "tartaric_acid";
// 版本，字符串
var version = "1.0.0";
// 冷却时间，整型数
var cooldown = 170;

/**
 * 执行的符卡逻辑，函数签名固定，会直接调用
 * @param world 当前所处的世界
 * @param player 释放符卡的玩家
 */
function spell_card(world, player) {
    // 第一波角度和后面不一致
    DelayedTask.add(function () {
        for (var j = 0; j < 8; j++) {
            var danmaku = new EntityDanmaku(world, player, 2.0, 0.0, DanmakuType.BIG_BALL, DanmakuColor.YELLOW);
            danmaku.shoot(player, 0, player.rotationYaw + 15 + 45 * j, 0, 0.2, 0);
            danmaku.setDanmakuTicksExisted(31);
            world.spawnEntity(danmaku);
        }
    }, 0);

    DelayedTask.add(function () {
        for (var i = 0; i < 8; i++) {
            for (var j = 0; j < 8; j++) {
                var danmaku = new EntityDanmaku(world, player, 2.0, 0.0, DanmakuType.BIG_BALL, DanmakuColor.YELLOW);
                var pos = RotationTool.getRotationVector(0, 0, 5.6, 15 + 45 * i, -0.1, player);
                danmaku.setPosition(pos.x, pos.y, pos.z);
                danmaku.shoot(player, 0, player.rotationYaw + 45 * j, 0, 0.2, 0);
                world.spawnEntity(danmaku);
            }
        }
    }, 30);


    // 后面来上 5 波即可，此时角度是固定的
    for (var k = 0; k < 5; k++) {
        DelayedTask.add(function () {
            for (var j = 0; j < 8; j++) {
                var danmaku = new EntityDanmaku(world, player, 2.0, 0.0, DanmakuType.BIG_BALL, DanmakuColor.YELLOW);
                danmaku.shoot(player, 0, player.rotationYaw + 30 + 45 * j, 0, 0.2, 0);
                danmaku.setDanmakuTicksExisted(31);
                world.spawnEntity(danmaku);
            }
        }, 20 * (k + 1));

        DelayedTask.add(function () {
            for (var i = 0; i < 8; i++) {
                for (var j = 0; j < 8; j++) {
                    var danmaku = new EntityDanmaku(world, player, 2.0, 0.0, DanmakuType.BIG_BALL, DanmakuColor.YELLOW);
                    var pos = RotationTool.getRotationVector(0, 0, 5.6, 30 + 45 * i, -0.1, player);
                    danmaku.setPosition(pos.x, pos.y, pos.z);
                    danmaku.shoot(player, 0, player.rotationYaw + 30 + 45 * j, 0, 0.2, 0);
                    world.spawnEntity(danmaku);
                }
            }
        }, 30 + 20 * (k + 1));
    }
}