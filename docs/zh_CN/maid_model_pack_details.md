# Maid Model Pack Details
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

## Model Packs Description Files

The file complete structure is as below, only the parts marked '(Required)' requires to be filled, you don't have to fill everything.

The model supports JSON files with comments, please use at your discretion.

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
            // The size when render the entity, range is between 0.7~1.3, default is 1.0
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

The example above listed all usable field, only the field with '(Required)' are needed, the rest can be omitted.

For simplicity, you could write a file like this:

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

If we do not fill the field for `model` or `texture`, it will choose the default model and texture based on `model_id`.

For the example above, `model_id` is `touhou_little_maid:hakurei_reimu`, then the model file will be `hakurei_reimu.json` under `models/entity` folder, the texture will be `hakurei_reimu.png` under `textures/entity` folder.

If we filled the field for `model` or `texture`, then the content has no limit, and can even share a model or texture for multiple characters.

## Animated Icon
Icon does not have size limit, supports both static and animated icons.

Any icon with a scale of 1:1 will be interpreted as static icon. Any long icon that is not 1:1 scale, will be displayed slowly with a 0.1 second interval, which creates the animated effect.

:::tip The image below is the icon in the mod, left is static icon, and right is the animated icon. :::

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
"pack_name": "{pack.vanilla_touhou_model.name}",
"description": ["{pack.vanilla_touhou_model.desc}"]
```

and then under model pack namespace, in the `lang` folder we create `en_us.lang` file, and write the content below:

```properties
pack.vanilla_touhou_model.name=Vanilla Touhou Model
pack.vanilla_touhou_model.desc=Default Model Packs
```

We only added the English file, but if we want to support Chinese, we can create `zh_cn.lang` file and write the content as below:

```properties
pack.vanilla_touhou_model.name=原版东方资源包
pack.vanilla_touhou_model.desc=默认的模型包
```

For the first example, if we did not fill the `name` file, then the system will automatically create the local key based on `model_id`, for example `model_id` is `touhou_little_maid:cushion`, then the generated language key is `model.touhou_little_maid.cushion.name`.

`description` file is not generated by default, you will need to fill that in.

## Compatibility Issue

Since models made by some authors are more unique, they may have some compability issues with maids' various addtional appearance parts. Here we address the issue with these non-standard models and how you can overcome it:

|                     Issues                      |                                                   Solution                                                   |
|:-----------------------------------------------:|:------------------------------------------------------------------------------------------------------------:|
|             Uncoordinated animation             |                                  Write a custom JavaScript animation script                                  |
|      Hold items are in incorrect position       |                                 Use positional group to define the position                                  |
|           Disable showing hold items            | As long as `armLeft` or `armRight` group does not exist, then the corresponding hold items will not be shown |
|        Backpack is in incorrect position        |                                 Use positional group to define the position                                  |
| Backpack, trolley, vehicles, broom, custom head |                         Write some fields as shown in the example below to close it                          |

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

## Maid Easter Eggs

We added maid naming easter egg function, specially named maid can use special models.

Writing easter egg script is pretty simple, the model will automatically detect it as easter egg models, and it won't show in skin menu.

### Normal Easter Eggs
Below is the script for normal naming easter egg. Under normal naming easter egg, maid only need to be named as shown in the `tag` field to use the model.
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
### Encrypted Easter Eggs
Below  is the script for encrypted naming easter egg. For encrypted naming easter egg, maid has to be specially named, the naming has to be the same as SHA-1 value in the `tag` below to use the model.

As the script below, when maid is named `IKUN~`, because the characters' SHA-1 value is `6dadb86d91cc4c0c2c7860e1cb16cec01e1b6511`, same as `tag` field, it will use said model.

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

## Other Questions

### Z-fighting Issue

This is an issue with OpenGL itself, during the process of creating models if we used flat or two coinciding solids, we will have this issue.

![004](https://i.imgur.com/daYk77e.png)

For the issue of one flat cube, you can add texture to one of the sides and keep the others empty, it will solve the issue; for two coinciding cube, move the cube slightly, or delete the coinciding cube.

### Uppercase & Lowercase

In Minecraft, all the file names needs to be lowercase.
