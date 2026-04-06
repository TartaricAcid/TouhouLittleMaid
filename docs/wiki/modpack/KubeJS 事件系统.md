---
title: KubeJS 事件系统
createTime: 2026/04/07 00:15:48
permalink: /wiki/modpack/kjs-event/
icon: ri:calendar-event-line
tags:
  - KubeJS
  - 事件
---

车万女仆原生提供了一套事件系统，允许你通过 `MaidEvents` 对象来监听各种女仆行为，并在事件发生时执行自定义逻辑。

::: info 所有可用的事件可以在 GitHub 上查看源码
- [1.20.1 MaidEventsJS.java](https://github.com/TartaricAcid/TouhouLittleMaid/blob/1.20/src/main/java/com/github/tartaricacid/touhoulittlemaid/compat/kubejs/event/MaidEventsJS.java)
- [1.21.1 MaidEventsJS.java](https://github.com/TartaricAcid/TouhouLittleMaid/blob/1.21/src/main/java/com/github/tartaricacid/touhoulittlemaid/compat/kubejs/event/MaidEventsJS.java)
:::

目前暴露的事件均为**双端事件**，可以根据需要分别放在 `client_scripts` 或 `server_scripts` 下。

## 一、女仆交互事件

当玩家对自己的女仆右击时触发。你可以通过此事件实现各种自定义交互逻辑，比如给予物品、播放音效，或者干脆——爆炸（不）。

调用 `event.cancel()` 可以阻止默认行为（即阻止打开女仆的 GUI 界面）。

```js
// 监听所有右击交互
MaidEvents.interactMaid(event => {
    let maid = event.getMaid();      // 被交互的女仆
    let stack = event.getStack();    // 玩家主手持有的物品
    let player = event.getPlayer();  // 交互的玩家
    let world = event.getWorld();    // 当前世界

    if (stack.is("minecraft:apple")) {
        world.createExplosion(maid.getX(), maid.getY(), maid.getZ()).strength(5).explode();
        // 取消后续操作，防止打开 GUI
        event.cancel();
    }
});

// 也可以传入物品 ID，只有玩家手持该物品时才会触发
MaidEvents.interactMaid("minecraft:diamond", event => {
    let maid = event.getMaid();
    maid.chatBubbleManager.addTextChatBubble("我喜欢钻石！");
    event.cancel();
});
```

## 二、工作模式启用条件

`MaidEvents.maidTaskEnable` 事件会在女仆尝试启用某个工作模式时触发，调用 `event.cancel()` 则表明该工作模式当前无法启用。

::: warning 注意
这是一个**双端事件**，如果你只在服务端脚本中添加，客户端切换界面时就不会显示"锁住"的图标，用户体验会差一些。建议同时在 `server_scripts` 和 `client_scripts` 两处都添加。
:::

```js
// 监听所有工作模式的启用条件
MaidEvents.maidTaskEnable(event => {
    let task = event.getTargetTask();    // 当前尝试启用的工作模式
    let maid = event.getEntityMaid();   // 女仆对象

    // task id 可以在游戏内按 F3+H 开启高级信息提示后，
    // 将鼠标悬停在工作模式选择按钮上看到
    if (task.getUid() === "touhou_little_maid:fishing" && maid.favorabilityManager.getLevel() < 2) {
        // 添加提示文本（鼠标悬停时显示）
        // 第一个参数是语言文件 key 的后缀，最终 key 为：
        // task.touhou_little_maid.fishing.enable_condition.need_level_2
        // 第二个参数控制文字颜色：返回 true 显示绿色，返回 false 显示红色
        event.addEnableConditionDesc("need_level_2", m => m.favorabilityManager.getLevel() >= 2);
        event.cancel();
    }
});

// 同样可以传入任务 UID，只有特定工作模式触发时才执行
MaidEvents.maidTaskEnable("touhou_little_maid:torch", event => {
    let maid = event.getEntityMaid();
    if (maid.favorabilityManager.getLevel() < 1) {
        event.addEnableConditionDesc("need_level_1", m => m.favorabilityManager.getLevel() >= 1);
        event.cancel();
    }
});
```

## 三、女仆受伤事件

`MaidEvents.maidDamage` 事件会在女仆受到伤害时触发，调用 `event.cancel()` 可以取消这次伤害。

```js
// 监听所有伤害
MaidEvents.maidDamage(event => {
    let maid = event.getMaid();
    // ... 在这里执行自定义逻辑
});

// 也可以传入伤害类型 ID，只有受到该类型伤害时才触发
// 原版伤害类型 ID 可在 Minecraft Wiki 伤害类型页面查看（表格第一列加上 minecraft: 前缀）
MaidEvents.maidDamage("minecraft:lava", event => {
    // 直接取消熔岩伤害，让女仆免疫岩浆
    event.cancel();
});
```

受伤事件也是实现自定义饰品保护效果的常用方式，相关内容可以参考[注册饰品与提示](./KubeJS%20注册饰品与提示.md)章节。
