---
title: KubeJS 自定义工作模式
createTime: 2026/04/07 00:16:21
permalink: /wiki/modpack/kjs-task/
icon: ri:settings-3-line
tags:
  - KubeJS
  - 工作模式
---

`MaidRegister.TASK` 提供了多种预设模板，让你可以快速注册全新的女仆工作模式，而无需从头编写 AI 逻辑。目前支持以下几种类型：

| 预设类型 | 方法 | 说明 |
|---------|------|------|
| 近战攻击 | `meleeTask` | 内置近战 AI，自动寻找并攻击目标 |
| 远程攻击 | `rangedAttackTask` | 内置远程 AI，可自定义发射逻辑 |
| 种田/交互 | `farmTask` | 走向方块并执行种植/收割逻辑 |
| 走向方块 | `walkToBlockTask` | 搜索特定方块并在到达后执行自定义操作 |
| 走向生物 | `walkToLivingEntityTask` | 搜索特定实体并在到达后执行自定义操作 |

两个版本的工作模式 API 基本一致，主要差异在于 BlockState 的判断方式，会在示例中特别标注。

## 一、近战攻击

`meleeTask` 内置了完整的近战攻击 AI，你只需要告诉它攻击什么目标、用什么武器就够了，其余的寻路和攻击逻辑都由模组自动处理。

::: tabs

@tab **1.20.1 Forge**

```js
MaidRegister.TASK
    // 设定工作模式的 ID 和图标物品
    .meleeTask("test:attack_cat", "minecraft:apple")
    // 启用条件（可选，默认永远启用）
    // 这里要求女仆好感度达到 2 级才能启用此工作模式
    .enable(maid => maid.favorabilityManager.getLevel() >= 2)
    // 在切换界面显示的启用条件提示（可选）
    // key 会被拼接为：task.test.attack_cat.enable_condition.need_level_2
    .addEnableConditionDesc("need_level_2", maid => maid.favorabilityManager.getLevel() >= 2)
    // 攻击目标的判断（可选，默认攻击所有敌对生物）
    .canAttack((maid, target) => target.type === "minecraft:cow")
    // 判断主手持有的物品是否是武器（必填，不填女仆不会执行任何攻击）
    .isWeapon((maid, stack) => stack.is("minecraft:wooden_sword"))
    // 是否执行额外攻击逻辑（可选，默认 false）
    .hasExtraAttack((maid, target) => true)
    // 额外攻击逻辑（可选，需要 hasExtraAttack 返回 true 才会触发）
    .doExtraAttack((maid, target) => {
        // 把目标往上弹飞
        target.addMotion(0, 1, 0);
        // 延迟 10 tick 后在目标位置爆炸
        target.level.server.scheduleInTicks(10, e => {
            let pos = target.position();
            target.level.createExplosion(pos.x, pos.y, pos.z).strength(1).exploder(maid).explode();
        });
        // 返回 true 表示额外攻击成功，false 表示失败
        return true;
    });
```

@tab **1.21.1 NeoForge**

1.21.1 的近战攻击 API 与 1.20.1 完全相同，直接参考上方示例即可。

:::

## 二、远程攻击

`rangedAttackTask` 内置了远程攻击的寻路和蓄力逻辑，你只需要实现 `performRangedAttack` 来定义实际的发射行为。

以下示例演示了一个让女仆向目标投掷 TNT 的工作模式：

::: tabs

@tab **1.20.1 Forge**

```js
MaidRegister.TASK
    .rangedAttackTask("test:tnt_attack", "minecraft:tnt")
    // 判断武器（必填）
    // 这里要求女仆手持木棍，并且背包里有 TNT
    .isWeapon((maid, stack) => {
        return stack.is("minecraft:stick") &&
            MaidItemsUtil.isStackIn(maid, item => item.is("minecraft:tnt"));
    })
    // 攻击目标（可选，默认攻击所有敌对生物）
    .canAttack((maid, target) => target.type === "minecraft:cow")
    // 搜索半径（可选，默认使用女仆工作范围）
    .searchRadius(48)
    // 弹射物有效距离（可选，默认 16）
    .projectileRange(32)
    // 蓄力时间（可选，默认 20 tick）
    .chargeDurationTick(10)
    // 女仆移动速度（可选，默认 0.5）
    .walkSpeed(0.4)
    // 执行远程攻击的实际逻辑（必填）
    // distanceFactor：0~1 之间，表示目标距离女仆的远近，可直接用于弹射物速度
    .performRangedAttack((maid, target, distanceFactor) => {
        let start = maid.position();
        let end = target.position();

        // 创建并设置 TNT 实体
        let tnt = maid.level.createEntity("minecraft:tnt");
        tnt.setPosition(start.x, start.y + 1.0, start.z);
        tnt.fuse = 10; // 爆炸倒计时（tick）

        // 计算速度向量
        let dx = end.x - start.x;
        let dy = (end.y + 0.5) - (start.y + 1.0);
        let dz = end.z - start.z;
        let dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
        tnt.setMotion(
            dx / dist * distanceFactor,
            dy / dist * distanceFactor + 0.2,
            dz / dist * distanceFactor
        );
        tnt.spawn();

        // 消耗一个背包里的 TNT
        MaidItemsUtil.getStack(maid, item => item.is("minecraft:tnt")).shrink(1);
    });
```

@tab **1.21.1 NeoForge**

1.21.1 的远程攻击 API 与 1.20.1 完全相同，直接参考上方示例即可。

:::

## 三、种田任务

`farmTask` 适合实现涉及"种植 + 收割"的工作模式，但其实也可以只写收割部分，用来做挖矿、砍树等不需要种植的任务。

以下示例演示了让女仆在沙子上种植枯萎灌木，并顺手打掉钻石矿的任务：

::: tabs

@tab **1.20.1 Forge**

```js
MaidRegister.TASK.farmTask("test:dead_bush", "minecraft:dead_bush")
    // 判断种子（可选，如果只做收割类任务可以不写）
    // 后续 canPlant 和 plant 方法中的 seed 参数就是从这里判断得到的
    .isSeed(stack => stack.is("minecraft:dead_bush"))
    // 到达目标方块多少格以内才执行种植/收割逻辑（可选，默认 2）
    .closeEnoughDist(2)
    // 是否检查目标方块上方有足够空间让女仆到达（可选，默认 false）
    .checkCropPosAbove(true)
    // 判断是否可以种植（可选）
    // blockPos 是作物基底方块的位置（比如耕地或沙子），种植应在其上方进行
    .canPlant((maid, blockPos, blockState, seed) => {
        // 只允许在沙子上种植，且沙子上方需要有空间
        return blockState.is("minecraft:sand") && maid.level.getBlockState(blockPos.above()).isAir();
    })
    // 执行种植逻辑（可选）
    // 返回值是消耗种子后剩余的物品对象
    .plant((maid, blockPos, blockState, seed) => {
        let checkPos = blockPos.above();
        // placeItemBlock 会自动处理物品消耗和碰撞箱判断，无需手动 shrink
        maid.placeItemBlock(checkPos, seed);
        return seed;
    })
    // 判断是否可以收割（可选）
    .canHarvest((maid, blockPos, blockState) => {
        // 这里顺手让女仆打掉钻石矿
        return blockState.is("minecraft:diamond_ore");
    })
    // 执行收割逻辑（可选）
    .harvest((maid, blockPos, blockState) => {
        maid.destroyBlock(blockPos);
    });
```

@tab **1.21.1 NeoForge**

1.21.1 中 BlockState 的判断方式不同，需要使用 `blockState.getId()` 而不是 `blockState.is()`：

```js
MaidRegister.TASK.farmTask("test:dead_bush", "minecraft:dead_bush")
    .isSeed(stack => stack.is("minecraft:dead_bush"))
    .closeEnoughDist(2)
    .checkCropPosAbove(true)
    .canPlant((maid, blockPos, blockState, seed) => {
        // 1.21.1 中使用 blockState.getId() 进行判断
        return blockState.getId() === "minecraft:sand" && maid.level.getBlockState(blockPos.above()).isAir();
    })
    .plant((maid, blockPos, blockState, seed) => {
        maid.placeItemBlock(blockPos.above(), seed);
        return seed;
    })
    .canHarvest((maid, blockPos, blockState) => {
        return blockState.getId() === "minecraft:diamond_ore";
    })
    .harvest((maid, blockPos, blockState) => {
        maid.destroyBlock(blockPos);
    });
```

:::

## 四、走向方块任务

`walkToBlockTask` 比 `farmTask` 更通用，适合"找到某个方块 → 走过去 → 执行操作"的场景，挖矿、操作机器等都可以用这个。

::: warning 注意
`setVerticalSearchRange` 的数值不宜设置过大，搜索范围越大，遍历方块的性能开销越高。
:::

::: tabs

@tab **1.20.1 Forge**

```js
MaidRegister.TASK.walkToBlockTask("test:walk_to_block", "minecraft:iron_ore")
    // 垂直搜索范围（可选，默认 ±2 格）
    // 水平范围固定为女仆的工作范围，无法自定义
    .setVerticalSearchRange(2)
    // 开始搜索的前置条件（必填，不填则不搜索）
    // 请只在必要时才开始搜索，减少性能消耗
    .setSearchCondition(maid => maid.mainHandItem.is("minecraft:iron_pickaxe"))
    // 搜索到的目标方块的判断条件（必填）
    .setBlockPredicate((maid, blockPos) => {
        return maid.level.getBlockState(blockPos).is("minecraft:iron_ore");
    })
    // 到达目标多少格以内才执行后续逻辑（可选，默认 2）
    .setCloseEnoughDist(2)
    // 到达目标后执行的逻辑（必填，不填则什么都不做）
    .setArriveAction((maid, blockPos) => {
        maid.destroyBlock(blockPos);
        // 消耗主手物品一点耐久
        maid.mainHandItem.hurtAndBreak(1, maid, m => m.broadcastBreakEvent("main_hand"));
    });
```

@tab **1.21.1 NeoForge**

```js
MaidRegister.TASK.walkToBlockTask("test:walk_to_block", "minecraft:iron_ore")
    .setVerticalSearchRange(2)
    .setSearchCondition(maid => maid.mainHandItem.is("minecraft:iron_pickaxe"))
    // 1.21.1 中使用 getId() 判断方块
    .setBlockPredicate((maid, blockPos) => {
        return maid.level.getBlockState(blockPos).getId() === "minecraft:iron_ore";
    })
    .setCloseEnoughDist(2)
    .setArriveAction((maid, blockPos) => {
        maid.destroyBlock(blockPos);
        // 1.21.1 中耐久消耗写法不同
        maid.hurtAndBreak(maid.mainHandItem, 1);
    });
```

:::

## 五、走向生物任务

`walkToLivingEntityTask` 用于"找到某个生物 → 走过去 → 执行操作"的场景。以下示例演示了让女仆手持碗走向蘑菇牛，取得蘑菇煲的任务：

::: tabs

@tab **1.20.1 Forge**

```js
MaidRegister.TASK.walkToLivingEntityTask("test:walk_to_living_entity", "minecraft:bowl")
    // 到达目标多少格以内才执行后续逻辑（可选，默认 2）
    .setCloseEnoughDist(2)
    // 开始搜索实体的前置条件（必填）
    .setStartSearchPredicate(maid => maid.mainHandItem.is("minecraft:bowl"))
    // 搜索到的目标实体的判断条件（必填）
    .setEntityPredicate((maid, entity) => {
        return entity.type === "minecraft:mooshroom";
    })
    // 到达目标附近后执行的逻辑（必填）
    .setArriveAction((maid, entity) => {
        if (maid.mainHandItem.is("minecraft:bowl")) {
            // 给女仆添加一个蘑菇煲
            MaidItemsUtil.giveItemToMaid(maid, "minecraft:mushroom_stew");
            // 消耗一个碗
            maid.mainHandItem.shrink(1);
            // 女仆挥手动作
            maid.swing();
            // 播放挤奶音效
            let pos = maid.position();
            maid.level.playSound(null, pos.x, pos.y, pos.z, "entity.mooshroom.milk", "neutral", 1, 1);
        }
    });
```

@tab **1.21.1 NeoForge**

1.21.1 中 `playSound` 的方法签名有所变化，需要使用完整签名字符串来调用：

```js
MaidRegister.TASK.walkToLivingEntityTask("test:walk_to_living_entity", "minecraft:bowl")
    .setCloseEnoughDist(2)
    .setStartSearchPredicate(maid => maid.mainHandItem.is("minecraft:bowl"))
    .setEntityPredicate((maid, entity) => {
        return entity.type === "minecraft:mooshroom";
    })
    .setArriveAction((maid, entity) => {
        if (maid.mainHandItem.is("minecraft:bowl")) {
            MaidItemsUtil.giveItemToMaid(maid, "minecraft:mushroom_stew");
            maid.mainHandItem.shrink(1);
            maid.swing();
            // 1.21.1 中需要用完整方法签名字符串来调用 playSound
            let pos = maid.position();
            maid.level["playSound(net.minecraft.world.entity.player.Player,double,double,double,net.minecraft.sounds.SoundEvent,net.minecraft.sounds.SoundSource)"](
                null, pos.x, pos.y, pos.z, "entity.mooshroom.milk", "neutral"
            );
        }
    });
```

:::
