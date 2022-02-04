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

For the example above, `model_id` is `touhou_little_maid:cushion`, then the model file will be `cushion.json` under `models/entity` folder, the texture will be `cushion.png` under `textures/entity` folder.

If we filled the field for `model` or `texture`, then the content has no limit, and can even share a model or texture for multiple characters.

`mounted_height` is the height of the entity when it is riding the model, the unit is the pixels above ground, you can use decimals.
> The `X` and `Z` position of the riding entity will always be the center of the chair entity, please preset the `X` and `Z` of the chair models when designing the models.

## 动态图标
图标没有尺寸限制，支持静态和动态图标。

任何长宽比为 1:1 的图标将被解析为静态图标。 非 1:1 长宽比的长图标将会以 0.1 秒间隔循环显示，从而产生动态效果。

> 下面的图像是模组中的图标，左边是静态图标，右边是动态图标。

![020](https://i.imgur.com/VoulqpR.png)

## 模型文件

- This mod is using JSON files in Bedrock `1.10.0` or `1.12.0` for model loading, the document can be exported via model building software [Blockbench](https://blockbench.net/), without additional edits.
- There are many preset animations, you only need to name a specific group, and then the plugin will automatically generate the corresponding animation script reference when exporting the model. For all available names, please see the [Preset Animation](/preset_animation.md) chapter.
- Model also support JavaScript custom animations, you can find the introduction in the custom animation chapter.

## Internationlization

As a game that are facing internationlization, part of the contents of model packs also have internationalization compability.

- `pack_name` and `description` field in model packs support internationalization;
- `name` and `description` field in model list support internationalization.

The method of adding internationalization is pretty simple, just need to begin it using `{` and end it using `}`, the middle section is the internationlization key, and then followed by the corresponding language file.

For example we wrote the description as follow (taking just a small section)

```json
"pack_name": "{pack.vanilla_chair_model.name}",
"description": ["{pack.vanilla_chair_model.desc}"]
```

and then under model pack namespace, in the `lang` folder we create `en_us.lang` file, and write the content below:

```properties
pack.vanilla_chair_model.name=Custom Chair Model Packs
pack.vanilla_chair_model.desc=Default Model Packs
```

We only added the English file, but if we want to support Chinese, we can create `zh_cn.lang` file and write the content as below:

```properties
pack.vanilla_chair_model.name=自定义坐垫模型包
pack.vanilla_chair_model.desc=默认的模型包
```

For the first example, if we did not fill the `name` file, then the system will automatically create the local key based on `model_id`, for example `model_id` is `touhou_little_maid:cushion`, then the generated language key is `model.touhou_little_maid.cushion.name`.

`description` file is not generated by default, you will need to fill that in.