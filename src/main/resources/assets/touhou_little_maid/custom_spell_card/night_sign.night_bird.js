var DelayedTask = Java.type("com.github.tartaricacid.touhoulittlemaid.util.DelayedTask");
var DanmakuColor = Java.type("com.github.tartaricacid.touhoulittlemaid.danmaku.DanmakuColor");
var DanmakuType = Java.type("com.github.tartaricacid.touhoulittlemaid.danmaku.DanmakuType");
var EntityDanmaku = Java.type("com.github.tartaricacid.touhoulittlemaid.entity.projectile.EntityDanmaku");

// 符卡的 id，字符串，必需参数
// 推荐格式：资源域:X符.符卡名
var id = "touhou_little_maid:night_sign.night_bird";
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
    for (var i = 0; i < 1; i++) {
        // 第 1，3 次左侧扇形弹幕
        DelayedTask.add(function () {
            for (var j = 0; j < 16; j++) {
                for (var k = 0; k < 3; k++) {
                    var danmaku = new EntityDanmaku(world, player, 2.0, 0.0, DanmakuType.ORBS, DanmakuColor.PURPLE);
                    danmaku.shoot(player, 0, player.rotationYaw + 90.0 - 135.0 / 16.0 * j - 3, 0, 0.04 + 0.04 * Math.pow(1.09, 15 - j + k * 5), 0);
                    world.spawnEntity(danmaku);
                }
            }
        }, i * 60);

        // 第 1，3 次右侧扇形弹幕
        DelayedTask.add(function () {
            for (var j = 0; j < 16; j++) {
                for (var k = 0; k < 3; k++) {
                    var danmaku = new EntityDanmaku(world, player, 2.0, 0.0, DanmakuType.ORBS, DanmakuColor.CYAN);
                    danmaku.shoot(player, 0, player.rotationYaw - 90.0 + 135.0 / 16.0 * j, 0, 0.04 + 0.04 * Math.pow(1.09, 15 - j + k * 5), 0);
                    world.spawnEntity(danmaku);
                }
            }
        }, i * 60 + 15);

        // 第 2，4 次左侧扇形弹幕
        DelayedTask.add(function () {
            for (var j = 0; j < 16; j++) {
                for (var k = 0; k < 3; k++) {
                    var danmaku = new EntityDanmaku(world, player, 2.0, 0.0, DanmakuType.ORBS, DanmakuColor.PURPLE);
                    danmaku.shoot(player, 0, player.rotationYaw + 90.0 - 135.0 / 16.0 * j - 2, 0, 0.04 + 0.04 * Math.pow(1.09, 15 - j + k * 5), 0);
                    world.spawnEntity(danmaku);
                }
            }
        }, i * 60 + 30);

        // 第 2，4 次右侧扇形弹幕
        DelayedTask.add(function () {
            for (var j = 0; j < 16; j++) {
                for (var k = 0; k < 3; k++) {
                    var danmaku = new EntityDanmaku(world, player, 2.0, 0.0, DanmakuType.ORBS, DanmakuColor.CYAN);
                    danmaku.shoot(player, 0, player.rotationYaw - 90.0 + 135.0 / 16.0 * j - 1, 0, 0.04 + 0.04 * Math.pow(1.09, 15 - j + k * 5), 0);
                    world.spawnEntity(danmaku);
                }
            }
        }, i * 60 + 45);
    }
}