# 自定义模型包

Touhou Little Maid 模组支持自定义模型包，你可以通过 Blockbench 这样的软件快速制作你喜欢的模型。 游戏可以读取 zip 压缩文件格式的模型包，或者直接读取文件夹。
The game can read the model package in zip compressed file format, or directly read the folder.

该模型包兼容的模组版本 `1.12.2` 和 `1.16.5` 完全相同，只是加载方法略有不同：

- `1.12.2`：模型包需要像原版资源包一样加载。
- `1.16.5`：模型包需要放置在游戏目录下的 `tlm_custom_pack` 文件夹中。

即使你是新手，从零开始制作一个自定义模型包也不难。 你只需要以下免费软件： You only need the following free software:

- Blockbench: 一个由 `JannisX11` 开发的专门用于 Minecraft 3D 建模的软件。

  官方网站：https\://blockbench.net/

  模组目前只支持基岩版模型格式，不支持基岩版动画文件。

- Visual Studio Code：微软开发的一个集成开发环境软件。

  官方网站：https\://code.visualstudio.com/

  主要用于编辑语言文件，可选。

此外，我还编写了一个用于导出自定义模型包的 Blockbench 插件，仅适用于 Blockbench 4.0 版本

插件网站： http\://page.cfpa.team/TLM-Utils-Plugins/

# 快速入门

首先你需要了解如何使用 Blockbench，其次你需要安装 `tlm-utils` 插件。\
当一切完成后，你可以根据下面的说明开始制作一个模型包。\
When everything is done, you can start to make a model package according to the instructions below.

## 1. 创建默认模型

创建一个默认模型，它将自动创建一个默认的基岩版模型，你可以在此基础上进行修改。\
如果你选择 `坐垫`，没有默认模型。\
If you choose `chair`, there is no default model.

![img](https://i.imgur.com/h6ufpuS.gif)

## 2. 基于默认模型，随心所欲的修改它

需要注意以下几点：

1. 女仆的动画是根据**组名**自动添加的，所以请不要随意删除或重命名组名。

   > Want to know what group names are available? 想知道哪些组名可用？ 请查看 [预设动画](/preset_animation.md) 篇章。

   > 你可以右击大纲界面打开预设动画菜单\
   > ![img](https://i.imgur.com/N17PbiE.gif)

2. 注意组的子父级关系。

3. 注意旋转点。

## 3) 创建材质

你可以直接在 Blockbench 中编辑材质，或者通过其他图像编辑器编辑材质。

![img](https://i.imgur.com/4JOKLMd.gif)

## 4. 创建一个模型包

![img](https://i.imgur.com/RHq9zf1.gif)

## 5. 将模型导出到模型包

![img](https://i.imgur.com/Mux4TwJ.gif)

## 6. 愉快的加载模型吧！
