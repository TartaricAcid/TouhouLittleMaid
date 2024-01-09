# Chair Model Pack Details

- This wiki applies to Touhou Little Maid mod in `1.12.2` or `1.16.5` latest version;
- Requires understanding of vanilla Minecraft's resource pack structure;
- Requires understanding of JSON format;
- Currently only supports models for **1.10.0 or 1.12.0 Bedrock Edition Model**.
- For file editing software, we recommend `Visual Studio Code`, all related files requires to be saved using `UTF-8 without BOM`.

## Model Packs Structure

To better understand how to create model packs, we listed here the structure format for model packs

```
model pack folder
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

## Model Packs Description Files

The file complete structure is as below, only the parts marked '(Required)' requires to be filled, you don't have to fill everything.

The model supports JSON files with comments, please use at your discretion.

```json
{
    // Model pack name (Required)
    "pack_name": "Custom Chair Model Packs",
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
    "version": "1.0.3",
    // Date creation for model pack
    "date": "2019-08-20",
    // Model pack icon, without this, the model pack will have no icon
    "icon": "touhou_little_maid:textures/chair_icon.png",
    // Model list (Required)
    "model_list": [
        {
            // Model id, no duplication allowed (Required)
            "model_id": "touhou_little_maid:cushion",
            // The path for the model, use the full resource path
            "model": "touhou_little_maid:models/entity/cushion.json",
            // The path for the texture, use the full resource path
            "texture": "touhou_little_maid:textures/entity/cushion.png",
            // The model size when rendering the item form, default is 1.0
            "render_item_scale": 0.9,
            // The size when render the entity, range is between 0.2~2.0, default is 1.0
            "render_entity_scale": 0.75,
            // The entity height(pixels) above ground when sat on, default is 3
            "mounted_height": 3,
            // Will tamed entities such as maids can sit on it, default is true
            "tameable_can_ride": false,
            // Whether the chair has gravity, the default is false
            "no_gravity": true,
            // Model name
            "name": "Cushion",
            // The description for the model
            "description": [
                "Just A Normal Cushion"
            ],
            // Animation script reference, without this part, chair will not have any animation
            // tlm-utils plugins can auto generate the correct animation reference based on the group name
            "animation": [
                "touhou_little_maid:animation/chair/passenger/rotation.js"
            ]
        }
    ]
}
```

The example above listed all usable field, only the field with '(Required)' are needed, the rest can be omitted.

For simplicity, you could write a file like this:

```json
{
    // Model pack name (Required)
    "pack_name": "Custom Chair Model Packs",
    // Model list (Required)
    "model_list": [
        {
            // Model id, no duplication allowed (Required)
            "model_id": "touhou_little_maid:cushion"
        }
    ]
}
```

If we do not fill the field for `model` or `texture`, it will choose the default model and texture based on `model_id`.

For the example above, `model_id` is `touhou_little_maid:cushion`, then the model file will be `cushion.json` under `models/entity` folder, the texture will be `cushion.png` under `textures/entity` folder.

If we filled the field for `model` or `texture`, then the content has no limit, and can even share a model or texture for multiple characters.

`mounted_height` is the height of the entity when it is riding the model, the unit is the pixels above ground, you can use decimals.

> The `X` and `Z` position of the riding entity will always be the center of the chair entity, please preset the `X` and `Z` of the chair models when designing the models.

## Animated Icon

Icon does not have size limit, supports both static and animated icons.

Any icon with a scale of 1:1 will be interpreted as static icon. Any long icon that is not 1:1 scale, will be displayed slowly with a 0.1 second interval, which creates the animated effect.

> The image below is the icon in the mod, left is static icon, and right is the animated icon.

![020](https://i.imgur.com/VoulqpR.png)

## Model Files

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
