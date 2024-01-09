# 女仆模型包详解
- 本 wiki 适用于 Touhou Little Maid 模组 `1.12.2` 和 `1.16.5` 最新版；
- 需要了解原版 Minecraft 的资源包结构；
- 需要了解 JSON 格式；
- 当前仅支持 **1.10.0 和 1.12.0 版本基岩版模型**；
- 对于文本编辑软件，我们推荐使用 `Visual Studio Code`，所有的文件都需要以 `UTF-8 无 BOM` 编码保存。

## 模型包结构

为了更好地理解如何创建模型包，我们在此列出了模型包的结构

```
模型包文件夹
│
├─pack.mcmeta
├─pack.png
└─assets
    └─my_model_pack
        ├─maid_model.json
        ├─lang
        │    ├─en_us.lang
        │    └─zh_cn.lang
        ├─models
        │     └─entity
        │            ├─cirno.json
        │            └─daiyousei.json
        └─textures
                 └─entity
                        ├─cirno.png
                        └─daiyousei.png
```

## 模型包描述文件

文件完整的结构如下，只有标记为“（必须）”的部分才是必须的，你不必填写所有内容。

模型支持带有注释的 JSON 文件，请酌情使用。

```json
{
    // 模型包包名（必须）
    "pack_name": "Touhou Project Model Packs",
    // 作者列表
    "author": [
        "TartaricAcid",
        "SuccinicAcid"
    ],
    // 模型包的描述文本
    "description": [
        "Default Model Packs"
    ],
    // 模型包的版本
    "version": "1.0.0",
    // 模型包的制作时间
    "date": "2019-07-14",
    // 模型包的图标，缺失此字段，材质包将没有图标
    "icon": "touhou_little_maid:textures/maid_icon.png",
    // 模型列表（必须）
    "model_list": [
        {
            // 模型 id，不允许重复（必须）
            "model_id": "touhou_little_maid:hakurei_reimu",
            // 模型所在的位置，使用完整的资源地址
            "model": "touhou_little_maid:models/entity/hakurei_reimu.json",
            // 模型所使用的材质位置，使用完整的资源地址
            "texture": "touhou_little_maid:textures/entity/hakurei_reimu.png",
            // 渲染成物品形态时的模型大小，默认为 1.0
            "render_item_scale": 0.9,
            // 渲染成实体时的大小，范围为 0.2~2.0，默认为 1.0
            "render_entity_scale": 0.75,
            // 模型名称
            "name": "Reimu Hakurei",
            // 该模型的描述文本
            "description": [
                "Shrine Maiden of Hakurei"
            ],
            // 动画脚本，如果没有此字段，将会自动调用默认动画
            // tlm-utils 插件会自动依据组名生成对应的动画脚本引用
            "animation": [
                "touhou_little_maid:animation/maid.default.js"
            ]
        }
    ]
}
```

上面的示例列出了所有可用字段，只需要带有“（必须）”的字段，其余部分可以省略。

为了简单起见，你可以写一个像这样的文件：

```json
{
    // 模型包包名（必须）
    "pack_name": "Touhou Project Model Packs",
    // 模型列表（必须）
    "model_list": [
        {
            // 模型 id，不允许重复（必须）
            "model_id": "touhou_little_maid:hakurei_reimu"
        }
    ]
}
```

如果我们不填写 `model` 或 `texture`，它将基于 `model_id` 选择默认的模型和材质。

比如上述案例中 `model_id` 为 `touhou_little_maid:hakurei_reimu`，那么此时模型文件就为该资源域下的  `models/entity` 文件夹下名为 `hakurei_reimu.json`  的文件，材质就为该资源域下的  `textures/entity` 文件夹下名为 `hakurei_reimu.png` 的文件。

如果我们书写了 `model` 或者 `texture` 字段，那么其内容并无限制，你甚至可以调用其他模型包中的模型，只需要书写对资源地址即可。

## 动态图标
图标没有大小限制，支持静态图标和动态图标。

任意为 1:1 的图标均会被解析为静态图标。 不为 1:1 的长图，会以 0.1 秒的间隔逐次显示，从而形成动态图效果。

> 下图就为模组自带的图标，左侧被解析为静态图标，右侧被解析为动态图标

![020](https://i.imgur.com/VoulqpR.png)

## 模型文件

- 本模组使用基岩版 `1.10.0` 或 `1.12.0` JSON 文件来进行模型的加载，该文件可以通过建模软件 [Blockbench](https://blockbench.net/) 直接导出，不需要对其做任何修改。
- 我们提供了许多预设动画，你只需要创建一个特殊名称的组，那么插件就会在导出模型是依据组名生成对应的动画脚本引用。 想要了解所有可用动画，请查看 [预设动画](/preset_animation.md) 篇章。
- 模型也支持 JavaScript 自定义动画，你可以在自定义动画章节中找到对应介绍。

## 国际化

作为一个面向国际化的游戏，模型包的部分内容自然也兼容国际化。

- 模型包中的 `pack_name` 和 `description` 字段支持国际化；
- 模型列表的 `name` 和 `description` 字段支持国际化。

国际化的添加方式很简单，只需要书写以 `{` 开头，`}` 结尾的字符串即可，中间部分为语言文件的 Key，而后书写对应语言文件即可。

比如我们书写了如下的内容（只截取了一小段）

```json
"pack_name": "{pack.vanilla_touhou_model.name}",
"description": ["{pack.vanilla_touhou_model.desc}"]
```

而后在模型包资源域下的 `lang` 文件夹下创建 `en_us.lang` 文件，书写如下内容即可：

```properties
pack.vanilla_touhou_model.name=Vanilla Touhou Model
pack.vanilla_touhou_model.desc=Default Model Packs
```

我们只添加了英文文件，如果还想再支持中文，创建 `zh_cn.lang` 文件书写如下内容即可：

```properties
pack.vanilla_touhou_model.name=原版东方资源包
pack.vanilla_touhou_model.desc=默认的模型包
```

在前面的案例中，如果我们没有书写 `name` 字段，那么系统会依据 `model_id` 自动生成本地化 key，比如 `model_id` 为 `touhou_little_maid:cushion`，那么生成的语言文件 key 为 `model.touhou_little_maid.cushion.name`。

`description` 字段默认不生成，需要自行主动书写。

## 兼容性问题

鉴于部分作者制作的模型比较特殊，与女仆本身拥有多种附加的外形显示不兼容。 此处专门对非标模型提供了适配的做法：

|            问题             |                      解决方法                       |
|:-------------------------:|:-----------------------------------------------:|
|           动画不协调           |              编写自定义 JavaScript 动画脚本              |
|        手臂持有物品位置不对         |                   使用定位骨骼进行定位                    |
|         禁止显示手部物品          | 只要 `armLeft` 或者 `armRight` 骨骼不存在，那么对应的手持物品就不会显示 |
|          背包位置不正确          |                   使用定位骨骼进行定位                    |
| 背包、拉杆箱、载具、扫帚，自定义头颅等位置兼容不对 |                  采用如下书写关闭这些功能                   |

```json {5-10}
{
    "pack_name": "Touhou Project Pack",
    "model_list": [{
        "model_id": "touhou_little_maid:hakurei_reimu",
        "show_backpack": false,      // 阻止背包的显示
        "show_custom_head": false,   // 阻止女仆显示自定义头颅
        "show_hata": false,          // 阻止旗指物的显示，在 1.16 中弃用
        "can_hold_trolley": false,   // 阻止女仆持有拉杆箱，在 1.16 中弃用
        "can_hold_vehicle": false,   // 阻止女仆持有载具，在 1.16 中弃用
        "can_riding_broom": false    // 阻止女仆骑乘扫帚，在 1.16 中弃用
    }]
}
```

## 女仆彩蛋

我们添加了女仆命名彩蛋功能，特殊命名的女仆可以直接调用特定模型。

彩蛋的书写方式非常简单，模组会自动识别其为彩蛋模型，也不会出现在皮肤选择界面。

### 普通彩蛋
如下为普通命名彩蛋的书写方式。 普通命名彩蛋下，女仆只需要命名为下面 `tag` 字段，就会调用该模型。
```json {5-7}
{
    "pack_name": "Touhou Project Model Pack",
    "model_list": [{
        "model_id": "touhou_little_maid:hakurei_reimu",
        "easter_egg": {
            "tag": "IKUN~"
        }
    }]
}
```
### 加密彩蛋
如下为加密彩蛋的书写方式。 加密彩蛋彩蛋下，女仆需要特定命名，该命名的 SHA-1 值为如下的 `tag` 字段，才会调用该模型。

如下书写方式，当女仆命名为 `IKUN~` 时，因为这个字符的 SHA-1 值为 `6dadb86d91cc4c0c2c7860e1cb16cec01e1b6511`，符合 `tag` 字段，会调用该模型。

```json {5-8}
{
    "pack_name": "Touhou Project Model Pack",
    "model_list": [{
        "model_id": "touhou_little_maid:hakurei_reimu",
        "easter_egg": {
            "encrypt": true,
            "tag": "6dadb86d91cc4c0c2c7860e1cb16cec01e1b6511"
        }
    }]
}
```

## 其他问题

### 贴图闪烁（Z-fighting）问题

这是 OpenGL 本身的一个问题，我们在制作模型过程中使用了平面、或者是两个重合的立方体，就会出现此问题。

![004](https://i.imgur.com/daYk77e.png)

对于平面图形导致的问题，只为其中某一面附上材质，另一面留空可以解决此问题；对于两个重合几何体，只需要微移几何体，或者对重合部分的材质进行剔除即可。

### 文件名大小写问题

在 Minecraft 中，所有文件名都必须是小写。
