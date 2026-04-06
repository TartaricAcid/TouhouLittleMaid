---
title: KubeJS 祭坛合成
createTime: 2026/04/07 00:15:32
permalink: /wiki/modpack/kjs-altar/
icon: ri:flask-line
tags:
  - KubeJS
  - 祭坛合成
---

通过 KubeJS，你可以用脚本的方式来添加或修改祭坛合成配方，相比手写 JSON 数据包要灵活不少。祭坛合成属于服务端逻辑，脚本需要放置在 `kubejs/server_scripts/` 目录下。

## 一、生成物品

最基础的用法：合成输出一个普通物品。

::: tabs

@tab **1.20.1 Forge**

```js
ServerEvents.recipes(event => {
    // 祭坛合成为无序合成，材料放在哪根柱子上都无所谓
    event.recipes.touhou_little_maid.altar_crafting(
        // 输出物品，完整支持 KubeJS 的物品写法
        // 比如 "5x minecraft:apple" 或者 Item.of(...).withName(...) 都可以
        Item.of("minecraft:apple").withCount(6).withName("林檎"),
        // 输入材料，无序合成，祭坛有六根御柱，最多可以设置六个输入
        [
            { "item": "minecraft:stone", "count": 1 },
            { "item": "minecraft:stone", "count": 1 },
            { "item": "minecraft:stone", "count": 1 },
            { "item": "minecraft:stone", "count": 1 }
        ],
        // 消耗的 P 点数，玩家最多可存储 5 点，物品合成一般填 0.1 ~ 0.2
        0.1
    );
});
```

@tab **1.21.1 NeoForge**

```js
ServerEvents.recipes(event => {
    // 1.21.1 的方法名有所不同，注意区分
    event.recipes.touhou_little_maid.altar_recipe_serializers(
        // withName 在 1.21.1 中改为了 withCustomName
        Item.of("minecraft:apple").withCount(6).withCustomName("林檎"),
        [
            { "item": "minecraft:stone", "count": 1 },
            { "item": "minecraft:stone", "count": 1 },
            { "item": "minecraft:stone", "count": 1 },
            { "item": "minecraft:stone", "count": 1 }
        ],
        0.1
    );
});
```

:::

## 二、召唤实体

祭坛不只能产出物品，还可以直接在祭坛位置召唤实体，甚至可以附加 NBT 数据，非常灵活。

::: tabs

@tab **1.20.1 Forge**

使用 `MaidAltarOutput.entity(id, nbt)` 来指定召唤的实体：

```js
ServerEvents.recipes(event => {
    event.recipes.touhou_little_maid.altar_crafting(
        // 召唤一个物品实体，并附加 NBT 数据
        // 当然，用来召唤猫猫狗狗也完全没问题
        MaidAltarOutput.entity("minecraft:item", {
            "Item": {
                "id": "touhou_little_maid:film",
                "Count": 10
            }
        }),
        ["minecraft:diorite", "minecraft:diorite", "minecraft:diorite", "minecraft:diorite"],
        // 实体相关合成一般消耗的 P 点略高于普通物品
        0.5
    );
});
```

@tab **1.21.1 NeoForge**

1.21.1 中召唤实体的写法有较大改动，需要使用占位物品 `touhou_little_maid:entity_placeholder`，并在第四个参数传入实体 ID：

```js
ServerEvents.recipes(event => {
    event.recipes.touhou_little_maid.altar_recipe_serializers(
        // 固定使用占位物品，通过 recipe_id 标识此配方
        Item.of("touhou_little_maid:entity_placeholder").set("touhou_little_maid:recipe_id", "cat"),
        ["minecraft:diorite", "minecraft:diorite", "minecraft:diorite", "minecraft:diorite"],
        0.5,
        // 第四个参数：召唤的实体 ID
        "minecraft:cat",
        // 第五个参数：JEI 界面中显示的提示文字（语言文件 key 或直接写字符串）
        "生成猫猫"
    );
});
```

:::

## 三、生成女仆

生成全新的女仆需要使用专用方法，两个版本的写法差异较大。

::: tabs

@tab **1.20.1 Forge**

```js
ServerEvents.recipes(event => {
    event.recipes.touhou_little_maid.altar_crafting(
        // 使用专用方法 spawnMaidWithBox()
        MaidAltarOutput.spawnMaidWithBox(),
        ["minecraft:apple", "minecraft:apple", "minecraft:apple", "minecraft:apple", "minecraft:apple"],
        0.8
    );
});
```

@tab **1.21.1 NeoForge**

```js
ServerEvents.recipes(event => {
    event.recipes.touhou_little_maid.altar_recipe_serializers(
        Item.of("touhou_little_maid:entity_placeholder").set("touhou_little_maid:recipe_id", "spawn_box"),
        ["minecraft:apple", "minecraft:apple", "minecraft:apple", "minecraft:apple", "minecraft:apple"],
        0.8,
        // 实体 ID 固定为 touhou_little_maid:box
        "touhou_little_maid:box",
        "jei.touhou_little_maid.altar_craft.spawn_box.result"
    );
});
```

:::

## 四、复活女仆

复活已死亡的女仆同样需要专用写法，并且输入材料里**必须包含一个胶片**（`touhou_little_maid:film`），缺少胶片将无法触发复活。

::: tabs

@tab **1.20.1 Forge**

```js
ServerEvents.recipes(event => {
    event.recipes.touhou_little_maid.altar_crafting(
        MaidAltarOutput.rebornMaid(),
        // 注意：材料中必须包含一个胶片！
        ["touhou_little_maid:film", "minecraft:apple", "minecraft:apple", "minecraft:apple", "minecraft:apple"],
        0.8
    );
});
```

@tab **1.21.1 NeoForge**

```js
ServerEvents.recipes(event => {
    event.recipes.touhou_little_maid.altar_recipe_serializers(
        Item.of("touhou_little_maid:entity_placeholder").set("touhou_little_maid:recipe_id", "reborn_maid"),
        // 注意：材料中必须包含一个胶片！
        ["touhou_little_maid:film", "minecraft:apple", "minecraft:apple", "minecraft:apple", "minecraft:apple"],
        0.8,
        // 实体 ID 固定为 touhou_little_maid:maid
        "touhou_little_maid:maid",
        "jei.touhou_little_maid.altar_craft.reborn_maid.result"
    );
});
```

:::
