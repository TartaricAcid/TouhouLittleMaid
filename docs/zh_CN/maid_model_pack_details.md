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

文件完整的结构如下，只有标记为 '(必须)' 的部分才是必须的，你不必填写所有内容。

模型支持带有注释的 JSON 文件，请酌情使用。

```json
{
    // Model pack name (Required)
    "pack_name": "Touhou Project Model Packs",
    // Author list
    "author": [
        "TartaricAcid",
        "SuccinicAcid"
    ],
    // Description for model packs
    "description": [
        "Default Model Packs"
    ],
    // Model pack version
    "version": "1.0.0",
    // Date creation for model pack
    "date": "2019-07-14",
    // Model pack icon, without this, the model pack will have no icon
    "icon": "touhou_little_maid:textures/maid_icon.png",
    // Model list (Required)
    "model_list": [
        {
            // Model id, no duplication allowed (Required)
            "model_id": "touhou_little_maid:hakurei_reimu",
            // The path for the model, use the full resource path
            "model": "touhou_little_maid:models/entity/hakurei_reimu.json",
            // The path for the texture, use the full resource path
            "texture": "touhou_little_maid:textures/entity/hakurei_reimu.png",
            // The model size when rendering the item form, default is 1.0
            "render_item_scale": 0.9,
            // The size when render the entity, range is between 0.2~2.0, default is 1.0
            "render_entity_scale": 0.75,
            // Model name
            "name": "Reimu Hakurei",
            // The description for the model
            "description": [
                "Shrine Maiden of Hakurei"
            ],
            // Animation script reference, without this part, model will have some default animations
            // tlm-utils plugins can auto generate the correct animation reference based on the group name
            "animation": [
                "touhou_little_maid:animation/maid.default.js"
            ]
        }
    ]
}
```

上面的示例列出了所有可用字段，只需要带有 '(必须)'的字段，其余部分可以省略。

为了简单起见，你可以写一个像这样的文件：

```json
{
    // Model pack name (Required)
    "pack_name": "Touhou Project Model Packs",
    // Model list (Required)
    "model_list": [
        {
            // Model id, no duplication allowed (Required)
            "model_id": "touhou_little_maid:hakurei_reimu"
        }
    ]
}
```

如果我们不填写`model` 或 `texture`,，它将基于 `model_id`选择默认的模型和材质。

就以上例子而言， `model_id` 是 `touhou_little_maid:hakurei_reimu`, 然后模型文件将是`hakurei_reimu.json`在 `models/entity` 文件夹下，纹理将是 `hakurei_reimu.png` 在 `textures/entity` 文件夹。

如果我们填写了 `model` 或 `texture`, 然后内容没有限制，甚至可以共享多个字符的模型或纹理。

## 动态图标
图标没有尺寸限制，支持静态和动态图标。

任何比例尺为1:1的图标将被解释为静态图标。 非1:1比例尺的任何长图标将会在0.1秒间隔内缓慢显示，从而产生动态效果。

> 下面的图像是模组中的图标，左边是静态图标，右边是动态图标。

![020](https://i.imgur.com/VoulqpR.png)

## 模型文件

- 这个模组正在使用Bedrock的 JSON 文件 `1.10.0` 或 `1.12。` 对于模型加载，文档可以通过模型构建软件导出 [Blockbench](https://blockbench.net/), 无需额外编辑。
- 有许多预设动画，您只需要命名一个特定组。 然后当导出模型时，插件会自动生成对应的动画脚本参考。 所有可用的名字，请查看 [预设动画](/preset_animation.md) 章。
- 模型也支持 JavaScript 自定义动画，您可以在自定义动画章节中找到导言。

## 多语言

作为一个面临国际化的游戏，模型包的部分内容也具有多语言的兼容性。

- 模型包中的`pack_name` 和 `description` 支持多语言;
- 模型列表中的`name` 和 `description` 支持多语言。

添加多语言的方法相当简单， 只需要开始使用 `{` 并以`}`结束， 中间部分是国际化密钥，然后是相应的语言文件。

例如，我们编写的描述如下(只取一小部分)

```json
"pack_name": "{pack.vanilla_touhou_model.name}",
"description": ["{pack.vanilla_touhou_model.desc}"]
```

然后在模型包命名空间下，在 `lang` 文件夹中，我们可以创建 `en_us.lang` 文件并写下以下内容：

```properties
pack.vanilla_touhou_model.name=Vanilla Touhou Model
pack.vanilla_touhou_model.desc=Default Model Packs
```

我们只添加了英文文件，但如果我们想支持中文，我们可以创建 `zh_cn.lang` 文件并写入以下内容：

```properties
pack.vanilla_touhou_model.name=原版东方资源包
pack.vanilla_touhou_model.desc=默认的模型包
```

对于第一个例子，如果我们没有填写 `name` 文件， 然后系统将基于 `model_id`自动创建本地密钥 例如 `model_id` 是 `touhou_little_maid:cushion`, 然后生成的语言键是 `model.touhou_little_maid.cushion.name`

`description` 文件不是默认生成的，您需要填写。

## 兼容性问题

由于某些作者所做的模型比较独特，他们可能会有某些复杂性问题，包括女佣的各种附加外观部分。 我们在这里用这些非标准模型以及你如何能够克服这个问题：

|         问题         |                    解决方法                    |
|:------------------:|:------------------------------------------:|
|       不协调的动画       |           编写自定义 JavaScript 动画脚本            |
|     持有项目的位置不正确     |                 使用位置组来定义位置                 |
|      禁用显示按住项目      | 只要 `Arlet` 或 `armRight` 组不存在，那么相应的持有项将不会显示 |
|      背包位置不正确       |                 使用位置组来定义位置                 |
| 背包、手推车、车辆、扫帚、自定义头部 |             写下下面示例中显示的一些字段以便关闭             |

```json {5-10}
{
    "pack_name": "Touhou Project Pack",
    "model_list": [{
        "model_id": "touhou_little_maid:hakurei_reimu",
        "show_backpack": false,      // Prevent displaying of backpack
        "show_custom_head": false,   // Prevent displaying of maid's custom heads
        "show_hata": false,          // Prevent displaying of hata sasimono, deprecated in 1.16
        "can_hold_trolley": false,   // prevent maid to hold the trolley, deprecated in 1.16
        "can_hold_vehicle": false,   // Prevent maid to hold the vehicle, deprecated in 1.16
        "can_riding_broom": false    // Prevent maid to riding the broom, deprecated in 1.16
    }]
}
```

## 女仆复活节彩蛋

我们添加了女仆命名复命名蛋功能，专门命名女仆可以使用特殊模型。

写入复活节彩蛋脚本非常简单，模型将自动检测到它作为复活节彩蛋模型，它不会在皮肤菜单中显示。

### 普通复活节蛋
下面是正常命名复位蛋的脚本。 在普通的命名复位蛋下，女仆只需要在 `标签` 字段中被命名才能使用模型。
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
### 加密复活节蛋
下面是加密的命名复位蛋的脚本。 要加密命名复命名蛋，必须特别命名， 命名必须与 `标签` 中的 SHA-1 值相同才能使用模型。

作为下面的脚本，当maid 被命名为 `IKUN~`因为字符串的 SHA-1 值是 `6dadb86d91cc4c0c2c7860e1cb16cec01e1b6511`和 `标签` 字段相同，它将使用所说的模型。

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

这是一个 OpenGL 本身的问题。 在创建模型过程中，如果我们使用平面或两个在同一位置的方块，将有这个问题。

![004](https://i.imgur.com/daYk77e.png)

关于平面方块的问题， 您可以将纹理添加到一侧并保持其他方为空，它将解决这个问题； 对于两个在同一位置的方块，稍微移动方块，或删除重合方块。

### 大写 & 小写

在Minecraft中，所有文件名称都必须是小写的。
