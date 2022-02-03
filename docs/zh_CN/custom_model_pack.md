# 自定义模型包
Touhou Little Maid模组支持自定义模型包，您可以通过 Blockbench 这样的软件快速制作您最喜欢的模型。 游戏可以以 zip 压缩文件格式读取模型包，或者直接读取文件夹。

该模型包兼容的模组版本 `1.12.2` 和 `1.16.5` 完全相同，只是加载方法略有不同:

- `1.12.2`: 模型包需要像原版资源包一样加载。
- `1.16.5`: 模型包需要放置在 `tlm_custom_pack` 游戏目录下的文件夹。

即使你是新手，从零开始制作一个自定义模型包也不难。 您只需要以下免费软件：

- Blockbench: 一个由 `JannisX11` 开发的专门用于Minecraft 3D 建模的软件。

  官方网站: [https://blockbie.net/](https://blockbench.net/)

  Mod只支持 Bedrock 模式格式，不支持 Bedrock 动画文件。

- Visual Studio Code: 微软开发的集成开发环境。

  官方网站: <https://code.visualstudio.com/>

  主要用于编辑语言文件，可选文件。

此外，我还编写了一个用于导出自定义模型的Blockbench插件，仅适用于Blockbench 4.0

插件网站： <http://page.cfpa.team/TLM-Utils-Plugins/>

# 快速入门

首先你需要了解如何使用Blockbench，其次你需要安装 `tlm-utils` 插件。    
当一切完成后，您可以根据下面的说明开始制作一个模型包。

## 1. 创建默认模型

创建一个默认模型，它将自动创建一个默认的Bedrock模型，您可以在此基础上进行修改。     
如果您选择 `Chair`, 没有默认模型。

![img](https://i.imgur.com/h6ufpuS.gif)

## 2. 根据默认模型修改它
需要注意以下几点：

1. 女仆的动画是根据 **组名**自动添加的，所以请不要随意删除或重命名。

   > 想知道哪个组名可用？ 请查看 [预设动画](/preset_animation.md) 章。

   > 您可以右键点击outliner界面打开预设动画菜单   
   > ![img](https://i.imgur.com/N17PbiE.gif)

2. 注意该组的父子集。

3. 注意枢纽点（the pivot point）。

## 3. 创建材质

您可以直接在 Blockbench 编辑纹理，或者您可以通过其他图像编辑器编辑纹理。

![img](https://i.imgur.com/4JOKLMd.gif)

## 4. 创建一个模型包

![img](https://i.imgur.com/RHq9zf1.gif)

## 5. 导出模型到模型包

![img](https://i.imgur.com/Mux4TwJ.gif)

## 6. 加载模型包 & 享受它！
