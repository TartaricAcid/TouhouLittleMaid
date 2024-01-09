# Custom Altar Recipes

## 1.16.5

Touhou Little Maid在 1.16.5 使用数据包添加祭坛配方

**请确保您知道如何使用 Minecaft 原版数据包**

```json
{
  // 固定类型
  "type": "touhou_little_maid:altar_crafting",
  "output": {
    // 实体类型名称
    "type": "minecraft:item",
    // 实体附加的 NBT 数据
    "nbt": {
      "Item": {
        "id": "touhou_little_maid:hakurei_gohei",
        "Count": 1
      }
    }
  },
  // 合成所必须的 Power 值
  "power": 0.15,
  // 你最多可以写 6 个参数，全是合成的输入物品
  "ingredients": [
    {
      "tag": "forge:rods/wooden"
    },
    {
      "tag": "forge:rods/wooden"
    },
    {
      "tag": "forge:rods/wooden"
    },
    {
      "item": "minecraft:paper"
    },
    {
      "item": "minecraft:paper"
    },
    {
      "item": "minecraft:paper"
    }
  ]
}
```

## 1.12.2

Touhou Little Maid 在 1.12.2 中添加对 `CraftTweaker` 的兼容，现在可以通过 `CraftTweaker` 进行祭坛物品合成的修改了。

**使用此功能前请确保你已经了解基本的 `CraftTweaker` 脚本书写方式。**

```js
// 导入相关的 ZenClass，简化后面合成的书写
import mods.touhoulittlemaid.Altar;

// 添加物品合成
// 第一个参数：字符串，表明该合成的 ID，建议书写格式 xxxx:xxxx，且最好与其他合成不重名
// 第二个参数：浮点数，表明该合成需要消耗的 P 点
// 第三个参数：IItemStack 类型，该合成的输出物品
// 后续几个为可变参数，可书写 1-6 个参数，均为 IIngredient 类型，表示该合成的输入物品
Altar.addItemCraftRecipe("touhou_little_maid:craft_camera", 0.2,
                         <touhou_little_maid:camera>*2,
                         <ore:blockGlass>, <ore:sand>, <minecraft:diamond_sword>);

// 可变参数的示例，这里仅写成一个合成的输入物品
Altar.addItemCraftRecipe("touhou_little_maid:craft_apple", 0.2,
                         <touhou_little_maid:apple>*2,
                         <ore:blockGlass>);
// 生成女仆的合成添加
Altar.addMaidSpawnCraftRecipe("test:replace_maid", 0.5, 
                              <minecraft:grass>, <minecraft:grass>, <minecraft:grass>);

// 女仆手办复活的合成添加
// 这个合成只能书写 1-5 个物品，手办物品是直接强制内嵌在合成中的
Altar.addMaidReviveCraftRecipe("test:reborn_maid", 0.3, 
                               <minecraft:grass>, <minecraft:grass>, <minecraft:grass>);

// 其他生物的合成
// 第三个参数需要书写完整的实体 id
Altar.addEntitySpawnCraftRecipe("test:spawn_pig", 0.2, "minecraft:pig", 
                                <minecraft:stone>, <minecraft:stone>, <minecraft:stone>);

// 删除合成，通过合成的 id 来删除合成，这也是为什么上面多次提到需要设置独立合成 id 的原因
// 只有一个参数，即合成的 id，只需要通过 JEI 就能查到模组自带合成的 id
Altar.removeRecipe("touhou_little_maid:craft_camera");
```
