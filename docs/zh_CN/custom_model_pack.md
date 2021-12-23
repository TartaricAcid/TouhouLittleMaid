# Custom Model Pack
Touhou Little Maid mod supports custom model packs, you can quickly make your favorite models through software like Blockbench. The game can read the model package in zip compressed file format, or directly read the folder.

The model packs compatible with the mod of `1.12.2` and `1.16.5` are exactly the same, just that the loading method is slightly different:

- `1.12.2`: The model package needs to be loaded like the vanilla resource package.
- `1.16.5`: The model package needs to be placed in the `tlm_custom_pack` folder under the game's directory.

It is not difficult to make a custom model package from scratch, even if you are a novice. You only need the following free software:

- Blockbench: A software develped by `JannisX11` specifically for Minecraft 3D modelling.

  Official website: <https://blockbench.net/>

  The mod only supports Bedrock Edition model format, and does not support Bedrock Edition animation files.

- Visual Studio Code: An Integrated Development Environment made by Microsoft.

  Official website: <https://code.visualstudio.com/>

  Mainly used to edit language files, optional.

In addition, I also wrote a blockbench plugin for exporting custom models.
| Version |                                                Link                                                |       Remark       |
|:-------:|:--------------------------------------------------------------------------------------------------:|:------------------:|
| `1.0.5` | [Download](https://github.com/TartaricAcid/TLM-Utils-Plugins/releases/download/1.0.5/tlm-utils.js) | For Blockbench 4.x |
| `1.0.4` | [Download](https://github.com/TartaricAcid/TLM-Utils-Plugins/releases/download/1.0.4/tlm-utils.js) | For Blockbench 3.x |

# Quick Start

First you need to understand how to use Blockbench, and second you need to install the `tlm-utils` plugin.    
When everything is done, you can start to make a model package according to the instructions below.

## 1. Create a default model

Create a default model, it will automatically create a default Bedrock Edition model, you can modify on this basis.     
If you choose `chair`, there is no default model.

![img](https://media.discordapp.net/attachments/760041309481336843/760046338317615124/1.png)

## 2. Modify it to what you want based on the default model
Need to pay attention to several points:

1. The maid animation is automatically added based on the **group name**, so please do not delete or rename at will.

   > Want to know what group names are available? Please see the [Preset Animation](/preset_animation.md) chapter.

2. Pay attention to the parent-child relationship of the group.

3. Pay attention to the pivot point.

![img](https://media.discordapp.net/attachments/760041309481336843/760046542287142952/2.png)

## 3. Create textures

You can edit the textures directly in Blockbench, or you can edit the textures through other image editors.

![img](https://media.discordapp.net/attachments/760041309481336843/760046813163683860/3.png)

## 4. Create a resource pack

Follow the prompts to fill out the form, it will create an empty model package folder.

![img](https://media.discordapp.net/attachments/760041309481336843/760046894617067530/4.png)

## 5. Export the model into resource pack

Follow the prompts to fill out the form,

:::tip Because there are still some problems with the plugin, after exporting successfully, remember to press `ctrl s` to save,  and need to wait 10 seconds before closing Blockbench, otherwise it may not be able to generate the correct animation reference based on the group name. :::

![img](https://media.discordapp.net/attachments/760041309481336843/760047013047173120/5.png)

## 6. Load the resource pack

![img](https://media.discordapp.net/attachments/760041309481336843/760047185193730058/6.png)
