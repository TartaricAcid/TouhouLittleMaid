# 预设动画

模组内置了许多预设动画，你只需要：

1. 在 Blockbench 中为想要添加动画的组设置正确的组名。
2. 在 `maid_model.json` 文件的 `animation` 字段中调用正确的动画脚本引用。

> `tlm-utils` 插件可以自动依据组名调用正确的动画脚本引用，你只需点击 `分析动画` 按钮。 ![img](https://i.imgur.com/iyCKwMx.gif)
> ![img](https://i.imgur.com/iyCKwMx.gif)
> ![img](https://i.imgur.com/iyCKwMx.gif)

Blockbech 插件提供了所有可用的预设动画，你可以右击大纲界面打开预设动画菜单\
![img](https://i.imgur.com/N17PbiE.gif)

## 定位组

用于渲染定位，基于组的旋转点来的，组内可以为空。

### `armLeftPositioningBone`

左手物品的位置，其父组必须是 `armLeft`

### `armRightPositioningBone`

右手物品的位置，其父组必须是 `armRight`

### `backpackPositioningBone`

背包肩带位置，它必须位于根目录下
