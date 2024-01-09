# 坐垫模型包详解
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
        ├─maid_chair.json
        ├─lang
        │    ├─en_us.lang
        │    └─zh_cn.lang
        ├─models
        │     └─entity
        │            ├─cushion.json
        │            └─wheel_chair.json
        └─textures
                 └─entity
                        ├─cushion.png
                        └─wheel_chair.png
```

## 模型包描述文件

文件完整的结构如下，只有标记为“（必须）”的部分才是必须的，你不必填写所有内容。

模型支持带有注释的 JSON 文件，请酌情使用。

```json
{
    // 模型包名（必须）
    "pack_name": "Custom Chair Model Packs",
    // 作者列表
    "author": [
        "TartaricAcid",
        "SuccinicAcid"
    ],
    // 模型包描述文本
    "description": [
        "Default Model Packs"
    ],
    // 模型包版本
    "version": "1.0.3",
    // 创建模型包的日期
    "date": "2019-08-20",
    // 模型包图标，没有此字段，模型包将没有图标
    "icon": "touhou_little_maid:textures/chair_icon.png",
    // 模型列表（必须）
    "model_list": [
        {
            // 模型 id，不允许重复（必须）
            "model_id": "touhou_little_maid:cushion",
            // 模型的路径，请使用完整路径名
            "model": "touhou_little_maid:models/entity/cushion.json",
            // 材质的路径，请使用完整路径名
            "texture": "touhou_little_maid:textures/entity/cushion.png",
            // 以物品形式渲染时的缩放大小，默认为 1.0
            "render_item_scale": 0.9,
            // 以实体形式渲染时的缩放大小，范围为 0.2~2.0，默认为 1.0
            "render_entity_scale": 0.75,
            // 坐上去的实体距离地面的高度，单位为距离地面的像素点数，默认为 3
            "mounted_height": 3,
            // 女仆等可驯服生物会主动坐上去，默认为 true
            "tameable_can_ride": false,
            // 坐垫是否受重力影响，默认为 false
            "no_gravity": true,
            // 模型名
            "name": "Cushion",
            // 该模型的描述文本
            "description": [
                "Just A Normal Cushion"
            ],
            // 动画脚本，如果没有此字段，坐垫将不会拥有任何动画
            // tlm-utils 插件会自动依据组名分析动画脚本引用
            "animation": [
                "touhou_little_maid:animation/chair/passenger/rotation.js"
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
    "pack_name": "Custom Chair Model Packs",
    // 模型列表（必须）
    "model_list": [
        {
            // 模型 id，不允许重复（必须）
            "model_id": "touhou_little_maid:cushion"
        }
    ]
}
```

如果我们不填写 `model` 或 `texture`，它将基于 `model_id` 选择默认的模型和材质。

比如上述案例中 `model_id` 为 `touhou_little_maid:cushion`，那么此时模型文件就为该资源域下的 `models/entity` 文件夹下名为 `cushion.json` 的文件，材质就为该资源域下的 `textures/entity` 文件夹下名为 `cushion.png` 的文件。

如果我们书写了 `model` 或者 `texture` 字段，那么其内容并无限制，你甚至可以调用其他模型包中的模型，只需要书写对资源地址即可。

`mounted_height` 为实体坐上该模型时所处的高度，单位为距离地面的像素点，可以为小数。
> 需要注意的是坐上的生物的 `X` 和 `Z` 位置总是会在坐垫实体所处位置的正中央，请在设计模型时就预设好坐垫模型的 `X` 和 `Z` 位置。

## 动态图标
图标没有尺寸限制，支持静态和动态图标。

任何长宽比为 1:1 的图标将被解析为静态图标。 非 1:1 长宽比的长图标将会以 0.1 秒间隔循环显示，从而产生动态效果。

> 下面的图像是模组中的图标，左边是静态图标，右边是动态图标。

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
"pack_name": "{pack.vanilla_chair_model.name}",
"description": ["{pack.vanilla_chair_model.desc}"]
```

而后在模型包资源域下的 `lang` 文件夹下创建 `en_us.lang` 文件，书写如下内容即可：

```properties
pack.vanilla_chair_model.name=Custom Chair Model Packs
pack.vanilla_chair_model.desc=Default Model Packs
```

我们只添加了英文文件，如果还想再支持中文，创建 `zh_cn.lang` 文件书写如下内容即可：

```properties
pack.vanilla_chair_model.name=自定义坐垫模型包
pack.vanilla_chair_model.desc=默认的模型包
```

在前面的案例中，如果我们没有书写 `name` 字段，那么系统会依据 `model_id` 自动生成本地化 key，比如 `model_id` 为 `touhou_little_maid:cushion`，那么生成的语言文件 key 为 `model.touhou_little_maid.cushion.name`。

`description` 字段默认不生成，需要自行主动书写。