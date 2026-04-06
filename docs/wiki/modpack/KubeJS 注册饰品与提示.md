---
title: KubeJS 注册饰品与提示
createTime: 2026/04/07 00:16:05
permalink: /wiki/modpack/kjs-bauble/
icon: fe:magic
tags:
  - KubeJS
  - 饰品
---

通过 `MaidRegister.BAUBLE`，你可以将任意物品注册成女仆饰品，并为其添加装备、卸下、受伤等各种触发时的回调逻辑。除此之外，`MaidRegister.TIPS` 还允许你注册自定义的物品提示，当玩家手持特定物品面对女仆时，屏幕上会显示对应的提示文字。

## 一、注册饰品

### 基础写法

`MaidRegister.BAUBLE.bind(物品ID)` 是注册饰品最基本的方式，第二个参数可以传入一个 tick 回调函数，女仆每 tick 都会执行它。

```js
// 绑定原版金斧作为饰品
let GOLDEN_AXE_BAUBLE = MaidRegister.BAUBLE.bind("minecraft:golden_axe",
    // @param maid  - 女仆实体
    // @param stack - 饰品绑定的物品对象
    (maid, stack) => {
        // 每 150 tick 检查一次，避免频繁触发消耗性能
        if (maid.level.server?.tickCount % 150 === 0) {
            maid.potionEffects.add("minecraft:glowing", 200, 1);
        }
    });
```

::: tip
`bind()` 的返回值是饰品对象本身，建议存下来，后续在 `maidDamage` 等事件中查找饰品时需要用到它。
:::

### 装备 / 卸下 / 受伤回调（1.4.2+）

1.4.2 版本起，你可以使用链式调用为饰品添加三种触发时机的回调：

::: tabs

@tab **1.20.1 Forge**

```js
let AttributeModifierCls = Java.loadClass("net.minecraft.world.entity.ai.attributes.AttributeModifier")
let OperationCls = Java.loadClass("net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation")
let IRON_PICKAXE_UUID = UUID.fromString("35c5574f-9d1e-4534-9e77-68b5051b87dc")

MaidRegister.BAUBLE.bind("minecraft:iron_pickaxe")
    // 当女仆装备上该饰品时触发
    .triggerPutOn((maid, baubleStack) => {
        let maxHealth = maid.getAttribute("minecraft:generic.max_health");
        // 让女仆最大生命值变为原来的 3 倍
        let modifier = new AttributeModifierCls(IRON_PICKAXE_UUID, "Iron Pickaxe Bauble Health Boost", 3, OperationCls.MULTIPLY_TOTAL);
        maxHealth.addPermanentModifier(modifier);
    })
    // 当女仆卸下该饰品时触发
    .triggerTakeOff((maid, baubleStack) => {
        let maxHealth = maid.getAttribute("minecraft:generic.max_health");
        // 按 UUID 移除对应的属性修改器
        maxHealth.removeModifier(IRON_PICKAXE_UUID);
    })
    // 每当女仆受到伤害时触发
    .triggerInjured((maid, baubleStack, damageSource, damageAmount) => {
        // 消耗一点耐久度
        baubleStack.hurtAndBreak(1, maid, e => maid.sendItemBreakMessage(baubleStack));
        // 返回 true 则取消此次伤害，返回 false 则不取消
        return true;
    });
```

@tab **1.21.1 NeoForge**

1.21.1 中耐久消耗的写法有所不同，改为由 `maid` 来调用：

```js
MaidRegister.BAUBLE.bind("minecraft:iron_pickaxe")
    .triggerPutOn((maid, baubleStack) => {
        // ... 装备时的逻辑
    })
    .triggerTakeOff((maid, baubleStack) => {
        // ... 卸下时的逻辑
    })
    .triggerInjured((maid, baubleStack, damageSource, damageAmount) => {
        // 1.21.1 中耐久消耗写法：由 maid 来调用 hurtAndBreak
        maid.hurtAndBreak(baubleStack, 1);
        return true;
    });
```

:::

### 通过受伤事件实现饰品（1.4.1 及以前）

1.4.1 及更早的版本没有 `triggerInjured` 等链式方法，当时实现饰品保护效果需要配合 `MaidEvents.maidDamage` 事件，并用 `MaidItemsUtil.getBaubleSlotInMaid` 来查找饰品：

::: tabs

@tab **1.20.1 Forge**

```js
let IRON_AXE_BAUBLE = MaidRegister.BAUBLE.bind("minecraft:iron_axe");

MaidEvents.maidDamage(event => {
    let maid = event.getMaid();
    // 在女仆的饰品栏中查找铁斧饰品，返回槽位编号，找不到则返回 -1
    // 注意：使用 /reload 重载脚本后，需要重进存档饰品查找才能正常工作
    let slot = MaidItemsUtil.getBaubleSlotInMaid(maid, IRON_AXE_BAUBLE);
    if (slot >= 0) {
        let stack = maid.getMaidBauble().getStackInSlot(slot);
        stack.hurtAndBreak(1, maid, m => maid.sendItemBreakMessage(stack));
        event.cancel();
    }
});
```

@tab **1.21.1 NeoForge**

```js
let IRON_AXE_BAUBLE = MaidRegister.BAUBLE.bind("minecraft:iron_axe");

MaidEvents.maidDamage(event => {
    let maid = event.getMaid();
    let slot = MaidItemsUtil.getBaubleSlotInMaid(maid, IRON_AXE_BAUBLE);
    if (slot >= 0) {
        let stack = maid.getMaidBauble().getStackInSlot(slot);
        // 1.21.1 中耐久消耗写法不同
        maid.hurtAndBreak(stack, 1);
        event.cancel();
    }
});
```

:::

## 二、注册物品提示

通过 `MaidRegister.TIPS`，你可以为指定物品注册一段提示文字。当玩家手持该物品准备与女仆交互时，屏幕上会显示这段提示。

```js
MaidRegister.TIPS.tips(overlay => {
    // 基础写法：手持石头时显示提示
    // 第一个参数是语言文件的 key，对应 lang 文件中的 tips.test.stone
    overlay.addTips("tips.test.stone", "minecraft:stone");

    // 高级写法：可以附加自定义的显示条件
    // 这里只有手持苹果数量超过 2 个时才会显示提示
    overlay.addSpecialTips("tips.test.apple",
        // @param stack  - 玩家手持的物品
        // @param maid   - 女仆实体
        // @param player - 玩家实体
        // 返回 true 则显示提示，返回 false 则不显示
        (stack, maid, player) => stack.is("minecraft:apple") && stack.getCount() > 2
    );
});
```

::: tip
`addTips` 和 `addSpecialTips` 的第一个参数会被拼接成完整的语言文件 key，例如 `"tips.test.stone"` 最终对应的 key 是 `tips.test.stone`，你需要在自己的语言文件中添加对应的翻译才能正常显示文字。
:::
