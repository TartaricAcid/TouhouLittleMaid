var Task = Java.type("com.github.tartaricacid.touhoulittlemaid.util.DelayedTask");
var Color = Java.type("com.github.tartaricacid.touhoulittlemaid.danmaku.DanmakuColor");
var Type = Java.type("com.github.tartaricacid.touhoulittlemaid.danmaku.DanmakuType");
var Danmaku = Java.type("com.github.tartaricacid.touhoulittlemaid.danmaku.script.EntityDanmakuWrapper");
var Vec3d = Java.type("com.github.tartaricacid.touhoulittlemaid.danmaku.script.Vec3dWrapper");

function fibonacciSphere(radius, samples, rotation) {
    rotation += 1;
    var offset = 2.0 / samples;
    var increment = Math.PI * (3 - Math.sqrt(5));
    var points = [];
    for (var i = 0; i < samples; i++) {
        var y = ((i * offset) - 1) + (offset / 2);
        var r = Math.sqrt(1 - y * y) * radius;
        var phi = ((i + rotation) % samples) * increment;
        var x = Math.cos(phi) * r;
        var z = Math.sin(phi) * r;
        points.push(new Vec3d(x, y * radius, z));
    }
    return points;
}

Java.asJSONCompatible({
    id: "touhou_little_maid:border_sign.boundary_between_wave_and_particle_3d",
    author: "snownee",
    version: "1.0.0",
    cooldown: 150,
    /**
     * 执行的符卡逻辑，函数签名固定，会直接调用
     * @param world 当前所处的世界
     * @param shooter 释放符卡的实体
     */
    spellCard: function (world, shooter) {
        var pos = new Vec3d(shooter.getPos().getX(), shooter.getPos().getY() + 1, shooter.getPos().getZ());
        var d = 0.0;
        for (var i = 0; i < 30; i++) {
            Task.add(function (times) {
                d += times;
                fibonacciSphere(0.2, 50, d / 100).forEach(function (v) {
                    var danmaku = new Danmaku(world, shooter, 2, 0, Type.ORBS, Color.MAGENTA);
                    danmaku.setMotion(v);
                    danmaku.setPosition(pos);
                    danmaku.setTicksExisted(100);
                    world.spawnDanmaku(danmaku);
                });
            }, 4 * i, i);
        }
    }
});
